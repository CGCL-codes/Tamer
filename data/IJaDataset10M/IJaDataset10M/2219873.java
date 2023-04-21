package com.germinus.xpression.cms.categories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LoadMigrationCategorysTable {

    public void loadCategoriesPath(List<Map<String, String>> categoriesUrlPaths) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://fenix:5432/educamadrid_migracion", "xpression", "xpression");
            String queryInsert = "insert into migration_categories values (?,?,?)";
            int j = 0;
            for (Map<String, String> categoryMap : categoriesUrlPaths) {
                for (Iterator iterator = categoryMap.entrySet().iterator(); iterator.hasNext(); ) {
                    stmt = conn.prepareStatement(queryInsert);
                    stmt.setInt(1, j);
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    stmt.setString(2, key);
                    stmt.setString(3, value);
                    stmt.execute();
                    j++;
                }
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                }
                stmt = null;
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlex) {
                }
                conn = null;
            }
        }
    }

    private void printCategoriesSql(List<Map<String, String>> categoriesUrlPaths) {
        StringBuffer query = new StringBuffer();
        int i = 10;
        for (Map<String, String> categoryMap : categoriesUrlPaths) {
            int j = 0;
            for (Iterator iterator = categoryMap.entrySet().iterator(); iterator.hasNext(); ) {
                query = new StringBuffer("insert into migration_categories values (");
                StringBuffer id = new StringBuffer();
                id.append(String.valueOf(i));
                id.append(String.valueOf(j));
                query.append(id.toString()).append(",");
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                query.append(key).append(",").append(value).append(")");
                System.out.println(query.toString());
                j++;
            }
            i++;
        }
    }
}
