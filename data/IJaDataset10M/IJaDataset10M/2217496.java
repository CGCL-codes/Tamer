package org.fto.jthink.jdbc;

import java.util.EventObject;

/**
 * SQLExecutor在执行SQL语句时事件,在SQLExecutorListener监听器的
 * executeSQLCommand()方法中获取此类型SQLExecutorEvent的实例。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-05  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class SQLExecutorEvent extends EventObject {

    private static final long serialVersionUID = -1217423206151676393L;

    private SQLExecutor sqlExecutor;

    private SQL sql;

    /**
	 * 创建SQLExecutorEvent的实例
	 * 
	 * @param sqlExecutor 正在执行SQL语句的SQLExecutor
	 * @param sql 正在被执行的SQL语句串
	 * @param type SQL语句的类型
	 */
    public SQLExecutorEvent(SQLExecutor sqlExecutor, String sql, int type) {
        this(sqlExecutor, new SQL(type, sql, null));
    }

    /**
	 * 创建SQLExecutorEvent的实例
	 * 
	 * @param sqlExecutor 正在执行SQL语句的SQLExecutor
	 * @param sql 正在被执行的SQL语句对象
	 */
    public SQLExecutorEvent(SQLExecutor sqlExecutor, SQL sql) {
        super(sqlExecutor);
        this.sqlExecutor = sqlExecutor;
        this.sql = sql;
    }

    /**
	 * 创建SQLExecutorEvent的实例
	 * 
	 * @param sqlExecutor 正在执行SQL语句的SQLExecutor
	 * @param sql 正在被执行的SQL语句串
	 * @param values 被执行的SQL语句中的值
	 * @param type SQL语句的类型
	 */
    public SQLExecutorEvent(SQLExecutor sqlExecutor, String sql, Object[] values, int type) {
        this(sqlExecutor, new SQL(type, sql, values));
    }

    /**
	 * 返回被执行的SQL语句对象
	 */
    public SQL getSQL() {
        return sql;
    }
}
