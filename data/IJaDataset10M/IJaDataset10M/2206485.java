package com.sitechasia.webx.core.dao.jdbc.mysql;

import com.sitechasia.webx.core.dao.jdbc.IPageSqlProvider;

/**
 * MySql分页
 * @author
 * @version 1.2
 * @since JDK1.5
 */
public class MySqlPageSqlProvider implements IPageSqlProvider {

    /**
	 * 返回分页Sql语句
	 *
	 * @param oldSqlStr
	 * 			需要分页的sql语句
	 * @param firstResult
	 * 			第一条记录所在位置
	 * @param numOfRows
	 * 			最后一条记录的位置
	 * @return 返回分页Sql语句
	 */
    public String getPageSql(String oldSqlStr, int firstResult, int numOfRows) {
        StringBuffer sb = new StringBuffer(oldSqlStr);
        if (firstResult >= 0 && numOfRows > 0) sb.append(" limit ").append(firstResult).append(",").append(numOfRows);
        return sb.toString();
    }
}
