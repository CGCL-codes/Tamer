package com.zzsoft.framework.e2p.frame.app.db.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import org.junit.Test;
import com.zzsoft.framework.e2p.frame.app.db.pool.OpConnection;
import com.zzsoft.framework.e2p.frame.app.db.pool.OpConnectionI;
import framework.zze2p.mod.pojodb.PojoDB_I;

public class DBSavePreBatch0 extends Thread {

    protected String[] colNames;

    protected int[] colTypes;

    protected OpConnectionI op;

    protected Connection con;

    protected PreparedStatement pre;

    String sql;

    String dbName;

    long dTime;

    /**
	 * 
	 * @param colNames
	 *            ���ֵ��pojo�е�����
	 * @param colTypes
	 *            ������ͣ�����Ϊ��
	 * @param sql
	 *            SQL��䣨��ţ�
	 * @param dbName
	 *            ��ݿ�DB��
	 * @param time
	 *            ��ѯʱ��
	 */
    public DBSavePreBatch0(String[] colNames, int[] colTypes, String sql, String dbName, long time) {
        super();
        this.colNames = colNames;
        this.colTypes = colTypes;
        this.sql = sql;
        this.dbName = dbName;
        dTime = time;
        this.init();
    }

    public void init() {
        try {
            if (this.pre == null) {
                if (this.con == null || con.isClosed()) {
                    if (this.op == null) this.op = new OpConnection();
                    if (this.op != null || this.dbName != null) this.con = op.getConnection(this.dbName);
                }
                this.pre = con.prepareStatement(this.sql);
                this.con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void add(PojoDB_I pojo) {
        try {
            if (this.colTypes == null) {
                for (int i = 0; i < colNames.length; i++) {
                    Object value = pojo.get(colNames[i]);
                    pre.setObject(i + 1, value);
                }
            } else {
                for (int i = 0; i < colNames.length; i++) {
                    Object value = pojo.get(colNames[i]);
                    pre.setObject(i + 1, value, colTypes[i]);
                }
            }
            pre.addBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveBatch() {
        try {
            init();
            this.con.setAutoCommit(false);
            pre.executeBatch();
            con.commit();
            this.con.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            this.pre = null;
            this.con = null;
        }
    }

    public void warmUpSave() {
        synchronized (this) {
            this.notify();
        }
    }

    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    this.wait(dTime);
                }
                this.saveBatch();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test$C$Base() {
        DBSavePreBatch0 dbSaveBatch = new DBSavePreBatch0(colNames, colTypes, sql, dbName, dTime);
    }
}
