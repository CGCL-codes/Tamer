package com.gever.sysman.privilege.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import com.gever.sysman.privilege.dao.ResOrderDAO;
import com.gever.config.Constants;
import com.gever.exception.db.DAOException;
import com.gever.jdbc.connection.ConnectionProvider;
import com.gever.jdbc.connection.ConnectionProviderFactory;
import java.util.Collection;
import java.util.ArrayList;
import com.gever.sysman.privilege.model.Operation;
import com.gever.sysman.privilege.dao.PrivilegeFactory;
import com.gever.sysman.privilege.model.Resource;
import com.gever.sysman.privilege.dao.SQLFactory;

/**
 * 
 * <p>
 * Title: ʵ�ֶ���Դ�����DAO
 * </p>
 * <p>
 * Description:ʵ�ֶ���Դ�����DAO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004-11-19
 * </p>
 * <p>
 * Company: GEVER
 * </p>
 * 
 * @author Hu.Walker
 * @version 1.0
 */
public class ResOrderDAOIMP implements ResOrderDAO {

    private static String GET_ORDERID = "SELECT ORDERID,PARENT_ID FROM  T_RESOURCE  WHERE ID=? ";

    private static String CHANGE_ORDERID = "UPDATE T_RESOURCE SET ORDERID=? WHERE ID=?";

    private static String CHANGE_ALLORDERID = "UPDATE T_RESOURCE SET ORDERID=ORDERID+1 WHERE ORDERID>=?  AND PARENT_ID=?";

    private static String CHANGE_ONEORDERID = "UPDATE T_RESOURCE SET ORDERID=? WHERE ID=?";

    private static String CHANGE_ALLORDERID2 = "UPDATE T_RESOURCE SET ORDERID=ORDERID-1 WHERE ORDERID<=?  AND PARENT_ID=?";

    private static String CHANGE_PARENTID = "UPDATE T_RESOURCE SET PARENT_ID=? WHERE ID=?";

    private static String GET_RESOPTIONID = "SELECT ID FROM  T_RESOPERATION WHERE ID=? ";

    private static String CHANGE_RESOPTIONID = "UPDATE T_RESOPERATION SET ORDERID=? WHERE ID=?";

    private static String CHANGE_RSOPTION_ALLORDERID = "UPDATE T_RESOPERATION SET ORDERID=ORDERID+1 WHERE ORDERID>?";

    private static String CHANGE_RSOPTION_ALLORDERID2 = "UPDATE T_RESOPERATION SET ORDERID=ORDERID-1 WHERE ORDERID>?";

    private static String CHANGE_RESOPTION_ORDERID = "UPDATE T_RESOPERATION SET ORDERID=? WHERE ID=?";

    private static String GET_OPERATIONRESID = "SELECT * FROM T_RESOPERATION WHERE Resource_ID=? order by orderid";

    private static String GET_one_OPERATIONRESID = "SELECT * FROM T_RESOPERATION WHERE id=?";

    private static String UPDATA_ORDERID = " UPDATE T_RESOURCE SET ORDERID=ORDERID+1 WHERE ID!=? AND ORDERID>=?";

    private static String UPDATA_OPERATION_ORDERID = " UPDATE T_RESOPERATION SET ORDERID=ORDERID+1 WHERE ID!=? AND ORDERID>=?";

    private static String GET_ResourceId = "SELECT * FROM T_RESOURCE WHERE PARENT_ID=? order by orderid";

    private static SQLFactory sqlFactory = PrivilegeFactory.getInstance().getSQLFactory();

    /**
	 * ��ʼ��T_RESOURCE���е�ORDERID
	 * 
	 * @param parentid
	 *            int
	 * @throws Exception
	 */
    public void initOrderID(int parentid) throws Exception {
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(sqlFactory.get("INIT_ORDERID"));
            pstmt.setString(1, String.valueOf(parentid));
            pstmt.setString(2, String.valueOf(parentid));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt = conn.prepareStatement(UPDATA_ORDERID);
                pstmt.setString(1, String.valueOf(rs.getInt("id")));
                pstmt.setString(2, String.valueOf(rs.getInt("orderid")));
                pstmt.executeUpdate();
                pstmt.close();
                rs.close();
                initOrderID(parentid);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
	 * ��ʼ��T_OPETRESOURCE���е�ORDERID
	 * 
	 * @param parentid
	 *            int
	 * @throws Exception
	 */
    public void initOperationOrderID(int parentid) throws Exception {
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(sqlFactory.get("INIT_OPERATION_ORDERID"));
            pstmt.setString(1, String.valueOf(parentid));
            pstmt.setString(2, String.valueOf(parentid));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt = conn.prepareStatement(UPDATA_OPERATION_ORDERID);
                pstmt.setString(1, String.valueOf(rs.getInt("id")));
                pstmt.setString(2, String.valueOf(rs.getInt("orderid")));
                pstmt.executeUpdate();
                pstmt.close();
                rs.close();
                initOrderID(parentid);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean exchangeResID(int id1, int id2) throws DAOException {
        boolean result = false;
        int temporderid1, temporderid2, parentid;
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            parentid = rs.getInt("PARENT_ID");
            initOrderID(parentid);
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid1 = rs.getInt("ORDERID");
            rs.close();
            pstmt.close();
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id2));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid2 = rs.getInt("ORDERID");
            rs.close();
            pstmt.close();
            pstmt = conn.prepareStatement(CHANGE_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid2));
            pstmt.setString(2, String.valueOf(id1));
            pstmt.executeUpdate();
            rs.close();
            pstmt.close();
            pstmt = conn.prepareStatement(CHANGE_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid1));
            pstmt.setString(2, String.valueOf(id2));
            pstmt.executeUpdate();
            result = true;
            return result;
        } catch (Exception e) {
            throw new DAOException("PRV_RES_001", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean moveResID(int id1, int id2) throws DAOException {
        int temporderid1, temporderid2, parentid1, parentid2;
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid1 = rs.getInt("ORDERID");
            parentid1 = rs.getInt("PARENT_ID");
            initOrderID(parentid1);
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id2));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid2 = rs.getInt("ORDERID");
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            parentid1 = rs.getInt("PARENT_ID");
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id2));
            rs = pstmt.executeQuery();
            rs.next();
            parentid2 = rs.getInt("PARENT_ID");
            if (parentid1 == parentid2) {
                if (temporderid1 < temporderid2) {
                    pstmt = conn.prepareStatement(CHANGE_ALLORDERID);
                    pstmt.setString(1, String.valueOf(temporderid2));
                    pstmt.setString(2, String.valueOf(parentid1));
                    pstmt.executeUpdate();
                    pstmt = conn.prepareStatement(CHANGE_ONEORDERID);
                    pstmt.setString(1, String.valueOf(temporderid2));
                    pstmt.setString(2, String.valueOf(id1));
                    pstmt.executeUpdate();
                } else {
                    pstmt = conn.prepareStatement(CHANGE_ALLORDERID2);
                    pstmt.setString(1, String.valueOf(temporderid2));
                    pstmt.setString(2, String.valueOf(parentid1));
                    pstmt.executeUpdate();
                    pstmt = conn.prepareStatement(CHANGE_ONEORDERID);
                    pstmt.setString(1, String.valueOf(temporderid2));
                    pstmt.setString(2, String.valueOf(id1));
                    pstmt.executeUpdate();
                }
                return true;
            } else {
                System.out.println("����ͬһ��Ŀ¼");
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("PRV_RES_001", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean exchangePID(int id1, int id2) throws DAOException {
        int parentid1, parentid2;
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            parentid1 = rs.getInt("PARENT_ID");
            pstmt = conn.prepareStatement(CHANGE_PARENTID);
            pstmt.setString(1, String.valueOf(id2));
            pstmt.setString(2, String.valueOf(id1));
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new DAOException("PRV_RES_001", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean exchangeOptionID(int id1, int id2) throws DAOException {
        boolean result = false;
        int temporderid1, temporderid2;
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_RESOPTIONID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid1 = rs.getInt("ORDERID");
            pstmt = conn.prepareStatement(GET_RESOPTIONID);
            pstmt.setString(1, String.valueOf(id2));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid2 = rs.getInt("ORDERID");
            pstmt = conn.prepareStatement(CHANGE_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid2));
            pstmt.setString(2, String.valueOf(id1));
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(CHANGE_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid1));
            pstmt.setString(2, String.valueOf(id2));
            pstmt.executeUpdate();
            result = true;
            return result;
        } catch (Exception e) {
            throw new DAOException("PRV_RES_001", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public Collection getOptionResOrderId(int resid) throws Exception {
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Collection list = new ArrayList();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_OPERATIONRESID);
            pstmt.setString(1, String.valueOf(resid));
            rs = pstmt.executeQuery();
            PrivilegeFactory factory = PrivilegeFactory.getInstance();
            while (rs.next()) {
                Operation opt = factory.createOperation();
                opt.setId(rs.getLong("ID"));
                opt.setName(rs.getString("Name"));
                opt.setDescription(rs.getString("Description"));
                opt.setCode(rs.getString("CODE"));
                list.add(opt);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    public Collection getResourceId(int resid) throws Exception {
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Collection list = new ArrayList();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_ResourceId);
            pstmt.setString(1, String.valueOf(resid));
            rs = pstmt.executeQuery();
            PrivilegeFactory factory = PrivilegeFactory.getInstance();
            while (rs.next()) {
                Resource opt = factory.createResource();
                opt.setId(rs.getLong("ID"));
                opt.setName(rs.getString("Name"));
                opt.setOrderId(rs.getInt("ORDERID"));
                list.add(opt);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    public boolean moveOptionID(int id1, int id2) throws Exception {
        int temporderid1, temporderid2, parentid;
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_one_OPERATIONRESID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid1 = rs.getInt("ORDERID");
            parentid = rs.getInt("resource_id");
            initOperationOrderID(parentid);
            rs.close();
            pstmt.close();
            pstmt = conn.prepareStatement(GET_one_OPERATIONRESID);
            pstmt.setString(1, String.valueOf(id2));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid2 = rs.getInt("ORDERID");
            rs.close();
            pstmt.close();
            pstmt = conn.prepareStatement(CHANGE_RESOPTION_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid2));
            pstmt.setString(2, String.valueOf(id1));
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(CHANGE_RESOPTION_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid1));
            pstmt.setString(2, String.valueOf(id2));
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            return false;
        }
    }

    public boolean moveResourceOrderId(int id1, int id2) throws Exception {
        int temporderid1, temporderid2, parentid;
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id1));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid1 = rs.getInt("ORDERID");
            parentid = rs.getInt("PARENT_ID");
            initOrderID(parentid);
            rs.close();
            pstmt.close();
            pstmt = conn.prepareStatement(GET_ORDERID);
            pstmt.setString(1, String.valueOf(id2));
            rs = pstmt.executeQuery();
            rs.next();
            temporderid2 = rs.getInt("ORDERID");
            rs.close();
            pstmt.close();
            pstmt = conn.prepareStatement(CHANGE_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid2));
            pstmt.setString(2, String.valueOf(id1));
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(CHANGE_ORDERID);
            pstmt.setString(1, String.valueOf(temporderid1));
            pstmt.setString(2, String.valueOf(id2));
            pstmt.executeUpdate();
            rs.close();
            conn.close();
            pstmt.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            return false;
        }
    }
}
