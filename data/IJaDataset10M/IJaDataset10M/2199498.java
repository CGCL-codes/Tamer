package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import javax.transaction.RollbackException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestDBUtil1 {

    public static void test1() {
        com.frameworkset.common.poolman.PreparedDBUtil d = new com.frameworkset.common.poolman.PreparedDBUtil();
        String sql1 = "select '0_10000104_'||'l' as id,'0_1000010'||'4' as parentid from dual";
        try {
            d.executeSelect(sql1);
            for (int i = 0; d.size() > 0 && i < d.size(); i++) {
                System.out.println("'" + d.getString(i, "id") + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testBatch() {
        DBUtil dbUtil = new DBUtil();
        try {
            dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.executeBatch();
            System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        } catch (SQLException e) {
            e.printStackTrace();
            dbUtil.resetBatch();
        }
    }

    public static void testBatchNoexecute() {
        DBUtil dbUtil = new DBUtil();
        try {
            dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        } catch (SQLException e) {
            e.printStackTrace();
            dbUtil.resetBatch();
        }
    }

    public static void testBatchWithConnection() {
        DBUtil dbUtil = new DBUtil();
        Connection con = null;
        try {
            dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            con = dbUtil.getConection();
            dbUtil.executeBatch(con);
            System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        } catch (SQLException e) {
            e.printStackTrace();
            dbUtil.resetBatch();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                con = null;
            }
            System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        }
    }

    public static void testBatchWithConnectionNoexecute() {
        DBUtil dbUtil = new DBUtil();
        Connection con = null;
        try {
            dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            con = dbUtil.getConection();
            System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        } catch (SQLException e) {
            e.printStackTrace();
            dbUtil.resetBatch();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                con = null;
            }
            System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        }
    }

    public static void testTXConnectionRollback() {
        DBUtil dbUtil = new DBUtil();
        Connection con = null;
        try {
            dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("qqinsert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            con = dbUtil.getConection();
            dbUtil.executeBatch(con);
            System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            dbUtil.resetBatch();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                con = null;
            }
            System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        }
    }

    public static void testConnection() {
        DBUtil dbUtil = new DBUtil();
        Connection con = null;
        try {
            dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + "," + dbUtil.getDBDate(new Date()) + ")");
            con = dbUtil.getConection();
            dbUtil.executeBatch(con);
            System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        } catch (SQLException e) {
            e.printStackTrace();
            dbUtil.resetBatch();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                con = null;
            }
            System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
            System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
        }
    }

    public static void testTXConnection() {
        TransactionManager tm = new TransactionManager();
        try {
            tm.begin();
            testConnection();
            tm.commit();
        } catch (Exception e) {
            try {
                tm.rollback();
            } catch (RollbackException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("testTXConnection dbUtil.getNumActive():" + DBUtil.getNumActive());
        System.out.println("testTXConnection dbUtil.getNumIdle():" + DBUtil.getNumIdle());
        System.out.println("testTXConnection dbUtil.getMaxNumActive():" + DBUtil.getMaxNumActive());
    }

    public static void testTXConnectionWithRollBack() {
        TransactionManager tm = new TransactionManager();
        try {
            tm.begin();
            testTXConnectionRollback();
            tm.commit();
        } catch (Exception e) {
            try {
                tm.rollback();
            } catch (RollbackException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("testTXConnection dbUtil.getNumActive():" + DBUtil.getNumActive());
        System.out.println("testTXConnection dbUtil.getNumIdle():" + DBUtil.getNumIdle());
        System.out.println("testTXConnection dbUtil.getMaxNumActive():" + DBUtil.getMaxNumActive());
    }

    public static void main(String args[]) {
        TestDBUtil1.testTXConnectionWithRollBack();
    }
}
