package com.nhncorp.cubridqa.replication.compare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import com.nhncorp.cubridqa.replication.parameters.Distribution;
import com.nhncorp.cubridqa.replication.parameters.Master;
import com.nhncorp.cubridqa.replication.parameters.Slave;
import com.nhncorp.cubridqa.utils.CubridUtil;
import com.nhncorp.cubridqa.utils.MyDriverManager;

/**
 * 
 * @ClassName: PerformanceOfReplication
 * @Description: the performance test of replication
 * 
 * 
 * @date 2009-9-1
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class PerformanceOfReplication {

    public Master master;

    private static final int dataCount = 2000000;

    private static final int selectCount = 500000;

    private static Slave currentslave = null;

    /**
	 * get connection from master broker
	 * 
	 * @return
	 */
    public Connection getConnectionToMaster() {
        String jdbcURL = "jdbc:CUBRID:brokerip:brokerport:databasename:::";
        String ip = (String) this.master.getServer().get("ip");
        String port = (String) this.master.getServer().get("port");
        String usr = (String) this.master.getDatabase().get("user");
        String pwd = (String) this.master.getDatabase().get("password");
        String dbname = (String) this.master.getDatabase().get("name");
        jdbcURL = jdbcURL.replace("brokerip", ip);
        jdbcURL = jdbcURL.replace("brokerport", port);
        jdbcURL = jdbcURL.replace("databasename", dbname);
        Connection conn = MyDriverManager.giveConnection("cubrid.jdbc.driver.CUBRIDDriver", jdbcURL, usr, pwd);
        return conn;
    }

    /**
	 * get connection from slave broer
	 * 
	 * @param slave
	 * @return
	 */
    public static Connection getConnectionToSlave(Slave slave) {
        String jdbcURL = "jdbc:CUBRID:activebrokerip:activebrokerport:databasename:::";
        String ip = (String) slave.getServer().get("ip");
        String port = (String) slave.getServer().get("port");
        String usr = (String) slave.getDatabase().get("user");
        String pwd = (String) slave.getDatabase().get("password");
        String dbname = (String) slave.getDatabase().get("name");
        jdbcURL = jdbcURL.replace("brokerip", ip);
        jdbcURL = jdbcURL.replace("brokerport", port);
        jdbcURL = jdbcURL.replace("databasename", dbname);
        Connection conn = MyDriverManager.giveConnection("cubrid.jdbc.driver.CUBRIDDriver", jdbcURL, usr, pwd);
        return conn;
    }

    /**
	 * create table in master
	 */
    public void createTableInMaster() {
        String sql = "CREATE CLASS tmp15 ( f1 char(10) PRIMARY KEY  ,f2 int  )";
        Connection conn = this.getConnectionToMaster();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CubridUtil.closeStatment(stmt);
            CubridUtil.closeConnection(conn);
        }
    }

    /**
	 * insert data to master ,then compare time
	 */
    public void insertToMaster() {
        int it = 0;
        int selectbegin = 0;
        Connection conn = this.getConnectionToMaster();
        for (int i = 1; i < PerformanceOfReplication.dataCount; i++) {
            if ((i > PerformanceOfReplication.selectCount) && (selectbegin == 0)) {
                selectbegin = 1;
                this.selectFromSlaves();
            }
            String sql = "INSERT INTO tmp15 (f1, f2) VALUES ('id" + i + "', " + i + "  )";
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    stmt.close();
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                CubridUtil.closeStatment(stmt);
            }
        }
        this.selectFromSlaves();
        CubridUtil.closeConnection(conn);
    }

    /**
	 * get select from slaves
	 */
    public void selectFromSlaves() {
        List dists = this.master.getDistributions();
        for (Iterator iterator = dists.iterator(); iterator.hasNext(); ) {
            Distribution dist = (Distribution) iterator.next();
            for (Iterator iterator2 = dist.getSlaves().iterator(); iterator2.hasNext(); ) {
                Slave slave = (Slave) iterator2.next();
                PerformanceOfReplication.currentslave = slave;
                Thread thread = new Thread() {

                    public void run() {
                        PerformanceOfReplication.selectFromSlave();
                    }
                };
                thread.start();
            }
        }
    }

    /**
	 * get select from slave
	 */
    private static void selectFromSlave() {
        Slave slave = PerformanceOfReplication.currentslave;
        Connection conn = PerformanceOfReplication.getConnectionToSlave(slave);
        int start = 0;
        int end = 0;
        long begintime = System.currentTimeMillis();
        System.out.println("Slave:" + slave.getDatabase().get("name"));
        System.out.println("begin:" + begintime);
        for (int i = 1; i <= PerformanceOfReplication.selectCount; i = i + 10000) {
            start = i;
            end = end + 10000;
            String sql = "select ORDERBY_NUM() as r, t.* from " + " tmp15 " + " t order by " + " f1 " + " for ORDERBY_NUM() between " + start + " and " + end;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                CubridUtil.closeResultSet(rs);
                CubridUtil.closeStatment(stmt);
            }
        }
        long endtime = System.currentTimeMillis();
        System.out.println("end:" + endtime);
        long result = end - start;
        System.out.println("Total:" + result + " ms");
        CubridUtil.closeConnection(conn);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
