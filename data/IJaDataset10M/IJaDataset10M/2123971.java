package com.vlee.ejb.mrp;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.BigDecimal;
import javax.ejb.*;
import javax.naming.*;
import com.vlee.local.*;
import com.vlee.util.*;
import com.vlee.ejb.inventory.*;

public class PrdTrackIndexItemBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String PRD_INDEX_ID = "prd_index_id";

    public static final String INV_ITEM_ID = "inv_item_id";

    public static final String INV_ITEM_CODE = "inv_item_code";

    public static final String INV_ITEM_NAME = "inv_item_name";

    public static final String INV_ITEM_UOM = "inv_item_uom";

    public static final String INV_ITEM_RATIO = "inv_item_ratio";

    public static final String INV_ITEM_QTY_BALANCE = "inv_item_qty_balance";

    public static final String INV_ITEM_QTY_USED = "inv_item_qty_used";

    public static final String INV_ITEM_QTY_WASTAGE = "inv_item_qty_wastage";

    public static final String MODULENAME = "mrp";

    public static final Long PKID_START = new Long(100001);

    PrdTrackIndexItemObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "mrp_prdtrack_index_item";

    private static final String strObjectName = "PrdTrackIndexItemBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public PrdTrackIndexItemObject getObject() {
        return this.valObj;
    }

    public void setObject(PrdTrackIndexItemObject newVal) {
        Long pkid = this.valObj.pkid;
        this.valObj = newVal;
        this.valObj.pkid = pkid;
    }

    public Long getPkid() {
        return this.valObj.pkid;
    }

    public Long getPrimaryKey() {
        return this.valObj.pkid;
    }

    public void setPrimaryKey(Long pkid) {
        this.valObj.pkid = pkid;
    }

    /***************************************************************************
	 * ejbCreate
	 **************************************************************************/
    public Long ejbCreate(PrdTrackIndexItemObject newObj) throws CreateException {
        try {
            this.valObj = insertObject(newObj);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbCreate: " + ex.getMessage());
        }
        return this.valObj.pkid;
    }

    /***************************************************************************
	 * ejbFindByPrimaryKey
	 **************************************************************************/
    public Long ejbFindByPrimaryKey(Long primaryKey) throws FinderException {
        boolean result = false;
        try {
            result = selectByPrimaryKey(primaryKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbFindByPrimaryKey: " + ex.getMessage());
        }
        if (result) {
            return primaryKey;
        } else {
            throw new ObjectNotFoundException("Row for id " + primaryKey.toString() + "not found.");
        }
    }

    /***************************************************************************
	 * ejbRemove
	 **************************************************************************/
    public void ejbRemove() {
        try {
            deleteObject(this.valObj.pkid);
        } catch (Exception ex) {
            throw new EJBException("ejbRemove: " + ex.getMessage());
        }
    }

    /***************************************************************************
	 * setEntityContext
	 **************************************************************************/
    public void setEntityContext(EntityContext context) {
        this.context = context;
    }

    /***************************************************************************
	 * unsetEntityContext
	 **************************************************************************/
    public void unsetEntityContext() {
        this.context = null;
    }

    /***************************************************************************
	 * ejbActivate
	 **************************************************************************/
    public void ejbActivate() {
        try {
            this.valObj = new PrdTrackIndexItemObject();
            this.valObj.pkid = (Long) context.getPrimaryKey();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
	 * ejbPassivate
	 **************************************************************************/
    public void ejbPassivate() {
        this.valObj = null;
    }

    /***************************************************************************
	 * ejbLoad
	 **************************************************************************/
    public void ejbLoad() {
        try {
            loadObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbLoad: " + ex.getMessage());
        }
    }

    /***************************************************************************
	 * ejbStore
	 **************************************************************************/
    public void ejbStore() {
        try {
            storeObject();
        } catch (Exception ex) {
            throw new EJBException("ejbStore: " + ex.getMessage());
        }
    }

    /***************************************************************************
	 * ejbPostCreate (1)
	 **************************************************************************/
    public void ejbPostCreate(PrdTrackIndexItemObject newObj) {
    }

    public Collection ejbHomeGetObjects(QueryObject query) {
        Collection col = null;
        try {
            col = selectObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
    }

    public void ejbHomeUpdQtyUsed(Long pt_index_id, Integer inv_item_id) {
        try {
            updQtyUsed(pt_index_id, inv_item_id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ejbHomeUpdQtyWastage(Long pt_index_id, Integer inv_item_id) {
        try {
            updQtyWastage(pt_index_id, inv_item_id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** ********************* Database Routines ************************ */
    private Connection makeConnection() throws NamingException, SQLException {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(dsName);
            return ds.getConnection();
        } catch (Exception ex) {
            throw new EJBException("Unable to connect to database. " + ex.getMessage());
        }
    }

    private void closeConnection(Connection con) throws NamingException, SQLException {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            throw new EJBException("closeConnection: " + ex.getMessage());
        }
    }

    private PrdTrackIndexItemObject insertObject(PrdTrackIndexItemObject newObj) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String selectStmt = "";
            con = makeConnection();
            try {
                newObj.pkid = getNextPKId(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(strObjectName + ex.getMessage());
            }
            try {
                String insertStatement = " INSERT INTO " + TABLENAME + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setLong(1, newObj.pkid.longValue());
                prepStmt.setLong(2, newObj.prd_index_id.longValue());
                prepStmt.setInt(3, newObj.inv_item_id.intValue());
                prepStmt.setString(4, newObj.inv_item_code);
                prepStmt.setString(5, newObj.inv_item_name);
                prepStmt.setString(6, newObj.inv_item_uom);
                prepStmt.setBigDecimal(7, newObj.inv_item_ratio);
                prepStmt.setInt(8, newObj.inv_item_qty_balance.intValue());
                prepStmt.setInt(9, newObj.inv_item_qty_used.intValue());
                prepStmt.setInt(10, newObj.inv_item_qty_wastage.intValue());
                prepStmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw ex;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            closeConnection(con);
        }
        return newObj;
    }

    private boolean selectByPrimaryKey(Long primaryKey) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            con = makeConnection();
            String selectStmt = " SELECT " + PKID + " FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setLong(1, primaryKey.longValue());
            rs = prepStmt.executeQuery();
            boolean result = rs.next();
            return result;
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            closeConnection(con);
        }
    }

    private void deleteObject(Long pkid) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String deleteStatement = " DELETE FROM " + TABLENAME + " WHERE " + PKID + " = ?";
            prepStmt = con.prepareStatement(deleteStatement);
            prepStmt.setLong(1, pkid.longValue());
            prepStmt.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private void loadObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            con = makeConnection();
            String selectStmt = " SELECT * FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setLong(1, this.valObj.pkid.longValue());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                this.valObj = getObject(rs, "");
            } else {
                throw new NoSuchEntityException("Row for pkid " + this.valObj.pkid.toString() + " not found in database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            closeConnection(con);
        }
    }

    private void storeObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String updateStatement = " UPDATE " + TABLENAME + " SET " + PRD_INDEX_ID + " = ?, " + INV_ITEM_ID + " = ?, " + INV_ITEM_CODE + " = ?, " + INV_ITEM_NAME + " = ?, " + INV_ITEM_UOM + " = ?, " + INV_ITEM_RATIO + " = ?, " + INV_ITEM_QTY_BALANCE + " = ?, " + INV_ITEM_QTY_USED + " = ?, " + INV_ITEM_QTY_WASTAGE + " = ? " + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setLong(1, this.valObj.prd_index_id.longValue());
            prepStmt.setInt(2, valObj.inv_item_id.intValue());
            prepStmt.setString(3, this.valObj.inv_item_code);
            prepStmt.setString(4, this.valObj.inv_item_name);
            prepStmt.setString(5, this.valObj.inv_item_uom);
            prepStmt.setBigDecimal(6, this.valObj.inv_item_ratio);
            prepStmt.setInt(7, this.valObj.inv_item_qty_balance.intValue());
            prepStmt.setInt(8, this.valObj.inv_item_qty_used.intValue());
            prepStmt.setInt(9, this.valObj.inv_item_qty_used.intValue());
            prepStmt.setLong(10, this.valObj.pkid.longValue());
            int rowCount = prepStmt.executeUpdate();
            if (rowCount == 0) {
                throw new EJBException("Storing row for pkid " + this.valObj.pkid + " failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private static synchronized Long getNextPKId(Connection con) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, PKID, TABLENAME, MODULENAME, PKID_START);
    }

    private static synchronized Long getNextStmtNo(Connection con, String stmtType, Long pcCenter) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, pcCenter.toString(), TABLENAME, stmtType, PKID_START);
    }

    public static PrdTrackIndexItemObject getObject(ResultSet rs, String prefix) throws Exception {
        PrdTrackIndexItemObject theObj = null;
        try {
            theObj = new PrdTrackIndexItemObject();
            theObj.pkid = new Long(rs.getLong(PKID));
            theObj.prd_index_id = new Long(rs.getLong(PRD_INDEX_ID));
            theObj.inv_item_id = new Integer(rs.getInt(INV_ITEM_ID));
            theObj.inv_item_code = rs.getString(INV_ITEM_CODE);
            theObj.inv_item_name = rs.getString(INV_ITEM_NAME);
            theObj.inv_item_uom = rs.getString(INV_ITEM_UOM);
            theObj.inv_item_ratio = rs.getBigDecimal(INV_ITEM_RATIO);
            theObj.inv_item_qty_balance = new Integer(rs.getInt(INV_ITEM_QTY_BALANCE));
            theObj.inv_item_qty_used = new Integer(rs.getInt(INV_ITEM_QTY_USED));
            theObj.inv_item_qty_wastage = new Integer(rs.getInt(INV_ITEM_QTY_WASTAGE));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return theObj;
    }

    private Collection selectObjects(QueryObject query) throws NamingException, SQLException {
        Collection result = new Vector();
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            con = makeConnection();
            String selectStmt = " SELECT * FROM " + TABLENAME;
            if (query != null) selectStmt = query.appendQuery(selectStmt);
            System.out.println("selectStmt: " + selectStmt);
            prepStmt = con.prepareStatement(selectStmt);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                PrdTrackIndexItemObject theObj = getObject(rs, "");
                if (theObj != null) {
                    result.add(theObj);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            closeConnection(con);
        }
        return result;
    }

    private void updQtyUsed(Long pt_index_id, Integer inv_item_id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String updateStatement = " UPDATE mrp_prdtrack_index_item SET inv_item_qty_used = ( SELECT SUM(b.inv_item_qty_used) FROM mrp_prdtrack_batch_item b, mrp_prdtrack_batch a WHERE b.batch_index_id = a.pkid AND a.prd_index_id = " + pt_index_id + " AND b.inv_item_id = " + inv_item_id + ") WHERE prd_index_id = " + pt_index_id + " AND inv_item_id = " + inv_item_id + "";
            prepStmt = con.prepareStatement(updateStatement);
            int rowCount = prepStmt.executeUpdate();
            System.out.println(">>" + rowCount + ">>updQtyUsed >>" + updateStatement);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private void updQtyWastage(Long pt_index_id, Integer inv_item_id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String updateStatement = " UPDATE mrp_prdtrack_index_item SET inv_item_qty_wastage = ( SELECT SUM(b.inv_item_qty_wastage) FROM mrp_prdtrack_batch_item b, mrp_prdtrack_batch a WHERE b.batch_index_id = a.pkid AND a.prd_index_id = " + pt_index_id + " AND b.inv_item_id = " + inv_item_id + ") WHERE prd_index_id = " + pt_index_id + " AND inv_item_id = " + inv_item_id + "";
            prepStmt = con.prepareStatement(updateStatement);
            int rowCount = prepStmt.executeUpdate();
            System.out.println(">>" + rowCount + ">>updQtyWastage >>" + updateStatement);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }
}
