package com.noasking.page.parser.impl;

import com.noasking.page.Page;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * @author MaJing
 */
public class Db2Parser extends AbstractParser {
    @Override
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("select * from (select tmp_page.*,rownumber() over() as row_id from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) as tmp_page) where row_id between  ? and ?");
        return sqlBuilder.toString();
    }

    @Override
    public Map<String, Object> setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page<?> page) {
        Map<String, Object> paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
        paramMap.put(PAGEPARAMETER_FIRST, page.getStartRow() + 1);
        paramMap.put(PAGEPARAMETER_SECOND, page.getEndRow());
        return paramMap;
    }
}