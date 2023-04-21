package com.manning.sdmia.dbapp.client;

import java.sql.Connection;
import javax.sql.DataSource;

public class DataBaseClient {

    private DataSource dataSource;

    public void init() throws Exception {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            System.out.println("database is: " + conn.getMetaData().getDatabaseProductName());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
