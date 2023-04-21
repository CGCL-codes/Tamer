package com.exedosoft.plat.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.exedosoft.plat.bo.BOInstance;
import com.exedosoft.plat.bo.DOBO;
import com.exedosoft.plat.bo.SQLTypes;
import com.exedosoft.plat.bo.DODataSource;

/**
 * 类型就是收敛到一定的程度，比如就是现在的4个类型。 但是还要支持扩展的类型。
 * 
 * @author anolesoft
 * 
 */
public class DBTransUtil {

    private static Log log = LogFactory.getLog(DBTransUtil.class);

    /**
	 * 
	 * @param rs
	 * @param isOracle
	 * @return
	 * @throws SQLException
	 */
    public static void makInsertSql(String aTargetTable, String aHistoryTable) {
        List<String> targetCols = getTableCols(aTargetTable);
        log.info("Target Table Cols:::" + targetCols);
        List<String> hisCols = getTableCols(aHistoryTable);
        log.info("History Table Cols:::" + hisCols);
        StringBuffer insertSql = new StringBuffer("insert into ").append(aTargetTable).append("(");
        for (Iterator<String> it = targetCols.iterator(); it.hasNext(); ) {
            String aTargetCol = it.next();
            insertSql.append(aTargetCol);
            if (it.hasNext()) {
                insertSql.append(",");
            }
        }
        insertSql.append(") ");
        insertSql.append(" select ");
        for (Iterator<String> it = targetCols.iterator(); it.hasNext(); ) {
            String targetCol = it.next();
            if (hisCols.contains(targetCol)) {
                insertSql.append(targetCol);
            } else {
                insertSql.append("#rep#");
            }
            if (it.hasNext()) {
                insertSql.append(",");
            }
        }
        insertSql.append(" from ").append(aHistoryTable);
        log.info("Insert Sql:::" + insertSql);
        StringBuffer notInCols = new StringBuffer("");
        for (Iterator<String> it = hisCols.iterator(); it.hasNext(); ) {
            String hisCol = it.next();
            if (!targetCols.contains(hisCol)) {
                notInCols.append(hisCol);
            } else {
                continue;
            }
            if (it.hasNext()) {
                notInCols.append(",");
            }
        }
        log.info("Target notInCols:::" + notInCols);
        if (targetCols.contains("fdprovince")) {
            StringBuffer buffer = new StringBuffer(" update ").append(aTargetTable).append(" set fdprovince=(select OBJUID from tbprovince  WHERE ").append(aTargetTable).append(".fdprovince=fdtemppro)");
            log.info("Replace fdprovince::" + buffer);
        }
        if (targetCols.contains("fdcity")) {
            StringBuffer buffer = new StringBuffer(" update ").append(aTargetTable).append(" set fdcity=(select OBJUID from tbcity  WHERE ").append(aTargetTable).append(".fdcity=replace(tbcity.fdcity,'市',''))");
            log.info("Replace fdcity::" + buffer);
        }
        if (targetCols.contains("fdaddprovince")) {
            StringBuffer buffer = new StringBuffer(" update ").append(aTargetTable).append(" set fdaddprovince=(select OBJUID from tbprovince  WHERE ").append(aTargetTable).append(".fdaddprovince=fdtemppro)");
            log.info("Replace fdprovince::" + buffer);
        }
        if (targetCols.contains("fdaddcity")) {
            StringBuffer buffer = new StringBuffer(" update ").append(aTargetTable).append(" set fdaddcity=(select OBJUID from tbcity  WHERE ").append(aTargetTable).append(".fdaddcity=replace(tbcity.fdcity,'市',''))");
            log.info("Replace fdcity::" + buffer);
        }
    }

    private static List<String> getTableCols(String aTableName) {
        List<String> list = new ArrayList<String>();
        try {
            Connection con = DODataSource.getDefaultCon_Busi();
            PreparedStatement pstmt = con.prepareStatement("select * from " + aTableName);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();
            for (int col = 1; col <= rsMeta.getColumnCount(); col++) {
                String metaName = rsMeta.getColumnName(col).toLowerCase().trim();
                list.add(metaName.toLowerCase());
            }
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static StringBuffer changeBitType() {
        StringBuffer buffer = new StringBuffer();
        Connection con = DODataSource.getDefaultCon();
        try {
            DatabaseMetaData meta = con.getMetaData();
            String[] tblTypes = new String[] { "TABLE" };
            DOBO bo = DOBO.getDOBOByName("do_datasource");
            DODataSource dss = DODataSource.getDataSourceByL10n(bo.getCorrInstance().getValue("l10n"));
            String schema = null;
            if (dss.isOracle()) {
                schema = dss.getUserName().trim().toUpperCase();
            }
            ResultSet rsDB = meta.getTables(null, schema, null, tblTypes);
            while (rsDB.next()) {
                String tableName = rsDB.getString("TABLE_NAME").toLowerCase();
                PreparedStatement pstmt = con.prepareStatement("select * from C192391203." + tableName);
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsMeta = rs.getMetaData();
                for (int col = 1; col <= rsMeta.getColumnCount(); col++) {
                    if (rsMeta.getColumnType(col) == Types.BIT) {
                        buffer.append(" deldefault '").append(tableName).append("','").append(rsMeta.getColumnName(col)).append("';\n");
                        buffer.append(" alter table C192391203.").append(tableName).append(" alter column ").append(rsMeta.getColumnName(col)).append(" int;\n");
                    }
                }
                pstmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
        }
        return buffer;
    }

    public static void checkDecimal() {
        Connection con = DODataSource.getDefaultCon_Busi();
        try {
            DatabaseMetaData meta = con.getMetaData();
            String[] tblTypes = new String[] { "TABLE" };
            DOBO bo = DOBO.getDOBOByName("do_datasource");
            DODataSource dss = DODataSource.getDataSourceByL10n(bo.getCorrInstance().getValue("l10n"));
            String schema = null;
            if (dss.isOracle()) {
                schema = dss.getUserName().trim().toUpperCase();
            }
            ResultSet rsDB = meta.getTables(null, schema, null, tblTypes);
            while (rsDB.next()) {
                String tableName = rsDB.getString("TABLE_NAME");
                System.out.println("TableName::::::::::" + tableName);
                if (!tableName.toUpperCase().equals(tableName)) {
                    continue;
                }
                PreparedStatement pstmt = con.prepareStatement("select * from " + tableName);
                ResultSet rs = null;
                try {
                    rs = pstmt.executeQuery();
                } catch (Exception e) {
                    continue;
                }
                if (rs == null) {
                    continue;
                }
                ResultSetMetaData rsMeta = rs.getMetaData();
                for (int col = 1; col <= rsMeta.getColumnCount(); col++) {
                    if (SQLTypes.isDouble(rsMeta.getColumnType(col)) && (rsMeta.getScale(col) != 2 && rsMeta.getScale(col) != 0)) {
                        String metaName = rsMeta.getColumnName(col).toLowerCase().trim();
                        System.out.println("This::::::::::" + tableName + "-----" + metaName);
                    }
                }
                pstmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
        }
    }

    public static StringBuffer changeTextType() {
        StringBuffer buffer = new StringBuffer();
        Connection con = DODataSource.getDefaultCon();
        try {
            DatabaseMetaData meta = con.getMetaData();
            String[] tblTypes = new String[] { "TABLE" };
            DOBO bo = DOBO.getDOBOByName("do_datasource");
            DODataSource dss = DODataSource.getDataSourceByL10n(bo.getCorrInstance().getValue("l10n"));
            String schema = null;
            if (dss.isOracle()) {
                schema = dss.getUserName().trim().toUpperCase();
            }
            ResultSet rsDB = meta.getTables(null, schema, null, tblTypes);
            while (rsDB.next()) {
                String tableName = rsDB.getString("TABLE_NAME").toLowerCase();
                PreparedStatement pstmt = con.prepareStatement("select * from C192391203." + tableName);
                ResultSet rs = pstmt.executeQuery();
                ResultSetMetaData rsMeta = rs.getMetaData();
                for (int col = 1; col <= rsMeta.getColumnCount(); col++) {
                    if (rsMeta.getColumnType(col) == Types.CLOB) {
                        buffer.append(" alter table C192391203.").append(tableName).append(" add ").append(rsMeta.getColumnName(col)).append("1 varchar(4000);\n");
                        buffer.append(" update C192391203.").append(tableName).append(" set ").append(rsMeta.getColumnName(col)).append("1 =").append(rsMeta.getColumnName(col)).append(";\n");
                        buffer.append(" alter table C192391203.").append(tableName).append(" drop column ").append(rsMeta.getColumnName(col)).append(";\n");
                        buffer.append(" sp_rename 'C192391203.").append(tableName).append(".[").append(rsMeta.getColumnName(col)).append("1]','").append(rsMeta.getColumnName(col)).append("','COLUMN';\n\n");
                    }
                }
                pstmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
        }
        return buffer;
    }

    public static void test() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate("drop table if exists people;");
            stat.executeUpdate("create table people (name, occupation);");
            PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?);");
            prep.setString(1, "Gandhi");
            prep.setString(2, "politics");
            prep.addBatch();
            prep.setString(1, "Turing");
            prep.setString(2, "computers");
            prep.addBatch();
            prep.setString(1, "Wittgenstein");
            prep.setString(2, "smartypants");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            ResultSet rs = stat.executeQuery("select * from people;");
            while (rs.next()) {
                System.out.println("name = " + rs.getString("name"));
                System.out.println("job = " + rs.getString("occupation"));
            }
            rs.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBTransUtil.test();
    }
}
