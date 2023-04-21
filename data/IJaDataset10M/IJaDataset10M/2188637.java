package org.portablerule.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.portablerule.service.RuleData;

/**
 * @author Colin Zhao
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PortableRuleDB {

    Connection conn;

    public PortableRuleDB(String dbName) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        conn = DriverManager.getConnection("jdbc:hsqldb:mem:" + dbName, "sa", "");
        conn.setAutoCommit(false);
    }

    public void loadRules(byte[] rules) throws SQLException {
        ByteArrayInputStream fis = null;
        ObjectInputStream in = null;
        List ruleData = null;
        try {
            fis = new ByteArrayInputStream(rules);
            in = new ObjectInputStream(fis);
            ruleData = (List) in.readObject();
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        loadRules(ruleData);
    }

    public void loadRules(List rules) throws SQLException {
        for (int i = 0; i < rules.size(); i++) {
            RuleData rule = (RuleData) rules.get(i);
            loadRule(rule);
        }
        conn.commit();
    }

    private void loadRule(RuleData rule) throws SQLException {
        System.out.println("Load table: " + rule.getTableName());
        PreparedStatement pstmt = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(rule.getTableCreateScript());
            String[] indices = rule.getIndexSqls();
            for (int i = 0; i < indices.length; i++) {
                stmt.execute(indices[i]);
            }
            pstmt = conn.prepareStatement(rule.getInsertSql());
            List data = rule.getData();
            int batchsize = 5000;
            int batchCount = 0;
            for (int i = 0; i < data.size(); i++) {
                List row = (List) data.get(i);
                for (int j = 0; j < row.size(); j++) {
                    Object obj = row.get(j);
                    if (obj == null) pstmt.setNull(j + 1, rule.getColumnTypes()[j]); else pstmt.setObject(j + 1, obj);
                }
                batchCount++;
                pstmt.addBatch();
                if (batchCount == batchsize) {
                    pstmt.executeUpdate();
                    batchCount = 0;
                    System.out.println("Execute batch: " + i);
                }
            }
            if (batchCount > 0) {
                pstmt.executeUpdate();
                batchCount = 0;
                System.out.println("Execute last batch");
            }
        } finally {
            if (stmt != null) stmt.close();
            if (pstmt != null) pstmt.close();
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
