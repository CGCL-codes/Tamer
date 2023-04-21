package net.sf.husky.utils;

public class SqlUtils {

    static final int ORACLE_DIALECT = 1;

    static final int MYSQL_DIALECT = 3;

    /**
     * <p/>
     * 取得分页查询的Sql语句。在查询大数据量的时候，如果将全部数据都装载到内存中，
     * <p/>
     * 无论对应用还是对数据库都需要承担很大的压力。一般的处理方式是将数据分批量的取出。
     * <p/>
     * 该方法适用于oracle/MySql/SQLServer/Sybase，输入普通的查询语句（不带分页），取出记录的起始位置、终止位置
     *
     * @param sql  String
     * @param from int
     * @return String
     */
    public static String getPagingSqlString(String sql, int from, int size, int dialect) {
        switch(dialect) {
            case ORACLE_DIALECT:
                return getOraclePagingSqlString(sql, from, size);
            case MYSQL_DIALECT:
                return getMysqlPagingSqlString(sql, from, size);
            default:
                return sql;
        }
    }

    private static String getMysqlPagingSqlString(String sql, int from, int size) {
        boolean hasOffset = false;
        if (from != 0) hasOffset = true;
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 20).append(sql);
        if (hasOffset) {
            pagingSelect.append(" limit ").append(from).append(", ").append(size);
        } else {
            pagingSelect.append(" limit ").append(size);
        }
        return pagingSelect.toString();
    }

    private static String getOraclePagingSqlString(String sql, int from, int size) {
        int to = from + size;
        boolean hasOffset = false;
        if (from != 0) hasOffset = true;
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
        if (hasOffset) {
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if (hasOffset) {
            pagingSelect.append(" ) row_ where rownum <= ").append(to).append(") where rownum_ > ").append(from);
        } else {
            pagingSelect.append(" ) where rownum <= ").append(to);
        }
        return pagingSelect.toString();
    }

    public static void main(String args[]) {
        String sql = "select * from ab01";
        System.out.println(SqlUtils.getPagingSqlString(sql, 100, 200, 1));
        System.out.println(SqlUtils.getPagingSqlString(sql, 0, 200, 1));
        System.out.println(SqlUtils.getPagingSqlString(sql, 100, 200, 3));
        System.out.println(SqlUtils.getPagingSqlString(sql, 0, 200, 3));
    }
}
