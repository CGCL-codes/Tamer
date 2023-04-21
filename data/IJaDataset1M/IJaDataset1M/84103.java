package com.gever.sysman.log.util;

import java.sql.*;
import com.gever.jdbc.connection.*;

public class IdManager {

    /**
   * DbSequenceManager���캯��.
   * @param type SequenceManager����
   */
    public IdManager(int type) {
        this.type = type;
        currentID = 0l;
        maxID = 0l;
    }

    private static final String LOAD_ID = "SELECT id FROM T_SYSTEM_ID WHERE idType=?";

    private static final String UPDATE_ID = "UPDATE T_SYSTEM_ID SET id=? WHERE idType=? AND id=?";

    private int type;

    private long currentID;

    private long maxID;

    /**
   * <pre>
   * Ϊ�˾���������ݿ�Ķ�д����,����ÿȡһ��ID��͸�����ݿ�,����INCREMENT��֮���ٸ�����ݿ�.
   * INCREMENT����̫��,��Ȼ��������������ʱ,�����ID�˷�,���Ը��������INCREMENT.
   * maxID = currentID + INCREMENT
   * if (currentID >= maxID) {
   *    currentID = id(����ݿ��ȡ);
   *    id = id + INCREMENT;
   *    ������ݿ�(����id).
   *    maxID = id;
   * } else {
   *    ����currentID;
   *    currentID++;
   * }
   * </pre>
   */
    private static final int INCREMENT = 15;

    /**
   * ����Ϊ��ͬ�ı����÷ֱ�����һ��SequenceManager.
   */
    private static IdManager[] managers;

    static {
        managers = new IdManager[28];
        for (int i = 0; i < managers.length; i++) {
            managers[i] = new IdManager(i);
        }
    }

    /**
   * ��ȡtype���͵�next ID.
   *
   * @param type SequenceManager����,��Ӧ��Table T_system_id�е�idType.
   * @return ��ȡtype���͵�next ID.
   */
    public static long nextID(int type) {
        long id = managers[type].nextUniqueID();
        return id;
    }

    /**
   * ��?��һ��SequenceManager,Ĭ�Ϸ��ص�һ��SequenceManager��next ID.
   */
    public static long nextID() {
        return managers[0].nextUniqueID();
    }

    /**
   * ����next Unique ID.
   */
    public synchronized long nextUniqueID() {
        if (!(currentID < maxID)) {
            getNextBlock(5);
        }
        long id = currentID;
        currentID++;
        return id;
    }

    /**
   * ��ȡnext���õ�ID block. �㷨����:
   * <ol>
   *  <li> ����ص���ݿ��¼�л�ȡ currentID.
   *  <li> ������ݿ��ȡ��id����INCREMENT.
   *  <li> ������ݿ��¼.
   *  <li> ������ʧ��,�ظ�ִ�е�һ��.
   * </ol>
   */
    private void getNextBlock(int count) {
        if (count == 0) {
            System.err.println("���һ�γ��Ի�ȡID blockʧ��.");
            return;
        }
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            com.gever.jdbc.connection.ConnectionProvider cp = ConnectionProviderFactory.getConnectionProvider("gdp");
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(LOAD_ID);
            pstmt.setInt(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("��ȡcurrent IDʧ��. ����������" + type + "�ļ�¼.");
            }
            long currentID = rs.getLong(1);
            pstmt.close();
            long newID = currentID + INCREMENT;
            pstmt = conn.prepareStatement(UPDATE_ID);
            pstmt.setLong(1, newID);
            pstmt.setInt(2, type);
            pstmt.setLong(3, currentID);
            success = pstmt.executeUpdate() == 1;
            if (success) {
                this.currentID = currentID;
                this.maxID = newID;
            }
        } catch (Exception sqle) {
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        if (!success) {
            System.err.println(count + ":����next ID blockʧ��,���³���...");
            try {
                Thread.currentThread().sleep(75);
            } catch (InterruptedException ie) {
            }
            getNextBlock(count - 1);
        }
    }
}
