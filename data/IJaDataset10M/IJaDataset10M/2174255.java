package com.log;

import java.sql.Connection;
import com.sql.SQLFactory;
import com.sql.SQLHelper;

public class SQLLogger {

    private SQLLogger() {
    }

    public static final void log(final String host) {
        Thread t1 = new Thread(new Runnable() {

            public void run() {
                Connection con = null;
                try {
                    con = SQLFactory.getConection();
                    SQLHelper.executeUpdate(con, "insert into URL_LOG values('" + host + "')");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    SQLFactory.close(con);
                }
            }
        });
        t1.start();
    }
}
