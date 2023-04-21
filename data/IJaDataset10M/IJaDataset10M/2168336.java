package symore.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @author Frank Bregulla, Manuel Scholz
 *
 */
public class ConcurrentTransactionsTest extends ConcurrentTest {

    private Connection connB2;

    private Connection connA2;

    public void nottestConcurrentTransactions() {
        String create1 = "CREATE TABLE test1 (rowId CHAR(36) primary Key, x int, y int, sentinel row)";
        String select1 = "SELECT * FROM test1";
        String select2 = "SELECT * FROM stable.test1";
        final String insertA1 = "INSERT INTO test1 VALUES ('B1', 0, 0), ('B2', 0,0), ('A1', 0, 0), ('A2', 0, 0)";
        final String updateB11 = "UPDATE test1 SET x=1 WHERE rowId='B1'";
        final String updateB12 = "UPDATE test1 SET x=1 WHERE rowId='B2'";
        final String updateB21 = "UPDATE test1 SET y=2 WHERE rowId='B1'";
        final String updateB22 = "UPDATE test1 SET y=2 WHERE rowId='B2'";
        final String updateA11 = "UPDATE test1 SET x=6 WHERE rowId='A1'";
        final String updateA12 = "UPDATE test1 SET x=6 WHERE rowId='A2'";
        final String updateA21 = "UPDATE test1 SET y=7 WHERE rowId='A1'";
        final String updateA22 = "UPDATE test1 SET y=7 WHERE rowId='A2'";
        final boolean stop = false;
        try {
            initDatabases();
        } catch (SQLException e) {
            SQLException ex = e.getNextException();
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return;
        }
        initCreateTables(connA, create1);
        initCreateTables(connB, create1);
        executeUpdate(connA, insertA1);
        executeUpdate(connA, "synchronize");
        executeUpdate(connB, "synchronize");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            connB2 = dsB.getConnection();
            connA2 = dsA.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new Thread() {

            public void run() {
                while (!stop) {
                    executeUpdate(connB2, updateB11);
                    executeUpdate(connB2, updateB12);
                    commit(connB2);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
        new Thread() {

            int c = 0;

            public void run() {
                while (!stop) {
                    executeUpdate(connB, updateB21);
                    executeUpdate(connB, updateB22);
                    commit(connB);
                    executeUpdate(connB, "synchronize");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
        new Thread() {

            public void run() {
                while (!stop) {
                    executeUpdate(connA2, updateA11);
                    executeUpdate(connA2, updateA12);
                    commit(connA2);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
        new Thread() {

            int c = 2;

            public void run() {
                while (!stop) {
                    executeUpdate(connA, updateA21);
                    executeUpdate(connA, updateA22);
                    commit(connA);
                    c++;
                    executeUpdate(connA, "synchronize");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initCreateTables(Connection conn, String createSql) {
        String drop1 = "DROP TABLE test1";
        this.executeUpdate(conn, drop1);
        this.executeUpdate(conn, createSql);
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
