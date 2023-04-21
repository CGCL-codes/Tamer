package com.jyams.util;

public class SQLUtils {

    /**
	 * 替换sql语句中的特殊字符，防止SQL注入
	 * @param sql
	 * @return
	 */
    public static String escapeBadSqlPatternChars(String sql) {
        return sql.replaceAll("([\\.`\"'%\\\\])", "\\\\$1");
    }
}
