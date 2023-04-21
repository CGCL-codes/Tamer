package com.patientis.upgrade.migrate;

import com.patientis.framework.utility.FileSystemUtil;
import com.patientis.upgrade.common.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
* @author ch117275
*
*/
public class SourceMigration {

    private JDBCAccess source = null;

    private JDBCAccess target = null;

    private String sql = null;

    private String tableName = null;

    public SourceMigration(JDBCAccess source, JDBCAccess target) {
        this.source = source;
        this.target = target;
    }

    /**
       * @param args
       */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("SourceMigration <sqlfile> <tablename>");
        }
        String sql = FileSystemUtil.getTextContents(args[0]);
        String tableName = args[1];
        JDBCAccess source = connect("src.xml");
        JDBCAccess target = connect("target.xml");
        try {
            SourceMigration test = new SourceMigration(source, target);
            test.sql = sql;
            test.tableName = tableName;
            Vector<Object> selectParams = new Vector<Object>();
            test.createTable(selectParams);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void createTable(Vector<Object> selectParams) throws Exception {
        System.out.println(sql);
        try {
            target.executeUpdate("drop table " + tableName + " cascade;");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        SQLResult result = source.executeQuery(sql, selectParams);
        String createSql = "create table " + tableName + "( ";
        try {
            ResultSet rset = result.getRset();
            ResultSetMetaData meta = rset.getMetaData();
            if (rset.next()) {
                for (int i = 0; i < meta.getColumnCount(); i++) {
                    if (i > 0) {
                        createSql += ", ";
                    }
                    createSql += " " + meta.getColumnName(i + 1).toLowerCase();
                    if (meta.getColumnClassName(i + 1).equals(String.class.getName())) {
                        createSql += " text ";
                    } else if (meta.getColumnClassName(i + 1).equals(Integer.class.getName())) {
                        createSql += " bigint not null default 0";
                    } else if (meta.getColumnClassName(i + 1).equals(java.math.BigDecimal.class.getName())) {
                        createSql += " bigint not null default 0";
                    } else if (meta.getColumnClassName(i + 1).equals(Double.class.getName())) {
                        createSql += " double precision not null default 0";
                    } else if (meta.getColumnClassName(i + 1).equals(Double.class.getName())) {
                        createSql += " double precision not null default 0";
                    } else if (meta.getColumnClassName(i + 1).equals(java.lang.Boolean.class.getName())) {
                        createSql += " int not null default 0";
                    } else if (meta.getColumnClassName(i + 1).equals(java.lang.Short.class.getName())) {
                        createSql += " bigint not null default 0";
                    } else {
                        System.out.println(meta.getColumnName(i + 1) + ":" + meta.getColumnClassName(i + 1));
                        throw new Exception();
                    }
                }
                createSql += ")";
                target.executeUpdate(createSql);
            }
            rset.close();
        } catch (Exception ex) {
            System.err.println(createSql);
            throw ex;
        }
    }

    public void run(Vector<Object> selectParams) throws Exception {
        SQLResult result = source.executeQuery(sql, selectParams);
        int cnt = 0;
        try {
            ResultSet rset = result.getRset();
            ResultSetMetaData meta = rset.getMetaData();
            String insertSql = getInsertSql(meta, tableName);
            while (rset.next()) {
                Vector<Object> params = new Vector<Object>();
                for (int i = 0; i < meta.getColumnCount(); i++) {
                    Object o = rset.getObject(i + 1);
                    if (o == null) {
                        if (meta.getColumnClassName(i + 1).equals(String.class.getName())) {
                            o = new DataNull(new String());
                        } else if (meta.getColumnClassName(i + 1).equals(Integer.class.getName())) {
                            o = new DataNull(new Integer(0));
                        } else if (meta.getColumnClassName(i + 1).equals(java.math.BigDecimal.class.getName())) {
                            o = new DataNull(new Integer(0));
                        } else if (meta.getColumnClassName(i + 1).equals(Double.class.getName())) {
                            o = new DataNull(new Double(0));
                        } else if (meta.getColumnClassName(i + 1).equals(java.sql.Timestamp.class.getName())) {
                            o = new DataNull(new java.sql.Timestamp(0));
                        } else if (meta.getColumnClassName(i + 1).equals(java.lang.Short.class.getName())) {
                            o = new DataNull(new Integer(0));
                        } else {
                            System.out.println(meta.getColumnName(i + 1) + ":" + meta.getColumnClassName(i + 1));
                        }
                    }
                    params.add(o);
                }
                cnt++;
                target.executeUpdate(insertSql, params);
            }
            rset.close();
            System.out.println(cnt);
        } catch (Exception ex) {
            System.err.println(sql);
            throw ex;
        }
    }

    public static String getInsertSql(ResultSetMetaData meta, String tablename) throws SQLException {
        String sql = "insert into " + tablename + " (";
        for (int i = 0; i < meta.getColumnCount(); i++) {
            String col = meta.getColumnName(i + 1);
            if (i > 0) {
                sql += ",";
            }
            sql += col + " ";
        }
        sql += ") values (";
        for (int i = 0; i < meta.getColumnCount(); i++) {
            if (i > 0) {
                sql += ",";
            }
            sql += "? ";
        }
        sql += ")";
        return sql;
    }

    /**
       * Connect to the database defined in the connection.xml
       *
       * @param connectionFileName
       * @return
       * @throws Exception
       */
    public static JDBCAccess connect(String connectionFileName) throws Exception {
        ConnectionList.setConfigFileName(connectionFileName);
        ConnectionList list = ConnectionList.loadConnections();
        ConnectionProperty connectionProperty = (ConnectionProperty) list.getElementAt(0);
        JDBCAccess access = new JDBCAccess(connectionProperty);
        return access;
    }
}
