package com.vlee.ejb.ecommerce;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.BigDecimal;
import javax.ejb.*;
import javax.naming.*;
import com.vlee.local.*;
import com.vlee.util.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.inventory.*;

public class EProOptionBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String PRODUCT_ID = "product_id";

    public static final String VISIBLE = "visible";

    public static final String SORT = "sort";

    public static final String TITLE = "title";

    public static final String INV_ID = "inv_id";

    public static final String INV_TYPE = "inv_type";

    public static final String MODULENAME = "ecommerce";

    public static final Long PKID_START = new Long(100001);

    EProOptionObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "ecom_product_options";

    public static final String TABLE_PRODUCT = "ecom_product";

    public static final String TABLE_ITEM = "inv_item";

    public static final String TABLE_BOM = "inv_bom_link";

    private static final String strObjectName = "EProOptionBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public EProOptionObject getObject() {
        return this.valObj;
    }

    public void setObject(EProOptionObject newVal) {
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
    public Long ejbCreate(EProOptionObject newObj) throws CreateException {
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
            deleteObject(this.valObj.pkid, this.valObj.productid);
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
            this.valObj = new EProOptionObject();
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
    public void ejbPostCreate(EProOptionObject newObj) {
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

    public void ejbHomeMoveUp(Long pkid, int sort, Long productid) {
        try {
            MoveUp(pkid, sort, productid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ejbHomeMoveDown(Long pkid, int sort, Long productid) {
        try {
            MoveDown(pkid, sort, productid);
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

    private EProOptionObject insertObject(EProOptionObject newObj) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String selectStmt = "";
            con = makeConnection();
            try {
                newObj.pkid = getNextPKId(con, TABLENAME);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(strObjectName + ex.getMessage());
            }
            try {
                int sort = 1;
                selectStmt = "SELECT max(" + SORT + ") as lastOrder FROM " + TABLENAME + " WHERE product_id = ?";
                prepStmt = con.prepareStatement(selectStmt);
                prepStmt.setLong(1, newObj.productid.longValue());
                rs = prepStmt.executeQuery();
                if (rs.next()) {
                    sort = rs.getInt("lastOrder");
                    sort = sort + 1;
                }
                newObj.sort = sort;
                String insertStatement = " INSERT INTO " + TABLENAME + " ( " + PKID + ", " + PRODUCT_ID + ", " + VISIBLE + ", " + SORT + ", " + TITLE + ", " + INV_ID + ", " + INV_TYPE + " " + ") VALUES ( ?, ?, ?, ?, ?, ?, ?)";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setLong(1, newObj.pkid.longValue());
                prepStmt.setLong(2, newObj.productid.longValue());
                prepStmt.setBoolean(3, newObj.visible);
                prepStmt.setInt(4, newObj.sort);
                prepStmt.setString(5, newObj.title);
                prepStmt.setLong(6, newObj.invid.longValue());
                prepStmt.setString(7, newObj.invtype);
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

    private void deleteObject(Long pkid, Long productId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String deleteStatement = "";
            String selectStatement = "";
            String updateStatement = "";
            con = makeConnection();
            deleteStatement = " DELETE FROM " + TABLENAME + " WHERE " + PKID + " = ?";
            prepStmt = con.prepareStatement(deleteStatement);
            prepStmt.setLong(1, pkid.longValue());
            prepStmt.executeUpdate();
            selectStatement = "SELECT " + PKID + " FROM " + TABLENAME + " WHERE " + PRODUCT_ID + " = ? ORDER BY " + SORT + " ASC";
            prepStmt = con.prepareStatement(selectStatement);
            prepStmt.setLong(1, productId.longValue());
            rs = prepStmt.executeQuery();
            if (rs != null) {
                int count = 1;
                while (rs.next()) {
                    updateStatement = "UPDATE " + TABLENAME + " SET " + SORT + " = ? WHERE " + PKID + " = ?";
                    prepStmt = con.prepareStatement(updateStatement);
                    prepStmt.setInt(1, count);
                    prepStmt.setLong(2, rs.getLong(PKID));
                    prepStmt.executeUpdate();
                    count = count + 1;
                }
            }
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

    private void loadObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        try {
            con = makeConnection();
            String selectStmt = " SELECT * FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setLong(1, this.valObj.pkid.longValue());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                this.valObj = getObject(rs);
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
            if (rs2 != null) {
                rs2.close();
            }
            closeConnection(con);
        }
    }

    private void storeObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String updateStatement = " UPDATE " + TABLENAME + " SET " + VISIBLE + " = ?, " + TITLE + " = ?, " + INV_ID + " = ?, " + INV_TYPE + " = ? " + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setBoolean(1, this.valObj.visible);
            prepStmt.setString(2, this.valObj.title);
            prepStmt.setLong(3, this.valObj.invid.longValue());
            prepStmt.setString(4, this.valObj.invtype);
            prepStmt.setLong(5, this.valObj.pkid.longValue());
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

    private static synchronized Long getNextPKId(Connection con, String tableName) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, PKID, tableName, MODULENAME, PKID_START);
    }

    private static synchronized Long getNextStmtNo(Connection con, String stmtType, Long pcCenter) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, pcCenter.toString(), TABLENAME, stmtType, PKID_START);
    }

    public static EProOptionObject getObject(ResultSet rs) throws Exception {
        EProOptionObject theObj = null;
        try {
            theObj = new EProOptionObject();
            theObj.pkid = new Long(rs.getLong(PKID));
            theObj.productid = new Long(rs.getLong(PRODUCT_ID));
            theObj.visible = rs.getBoolean(VISIBLE);
            theObj.sort = rs.getInt(SORT);
            theObj.title = rs.getString(TITLE);
            theObj.invid = new Long(rs.getLong(INV_ID));
            theObj.invtype = rs.getString(INV_TYPE);
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
        ResultSet rs2 = null;
        try {
            con = makeConnection();
            String selectStmt = " SELECT * FROM " + TABLENAME;
            selectStmt = query.appendQuery(selectStmt);
            prepStmt = con.prepareStatement(selectStmt);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                EProOptionObject theObj = getObject(rs);
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
            if (rs2 != null) {
                rs2.close();
            }
            closeConnection(con);
        }
        return result;
    }

    private void MoveUp(Long pkid, int sort, Long productid) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String selectStmt = "";
            String updateStmt = "";
            con = makeConnection();
            selectStmt = "SELECT " + PKID + ", " + SORT + " FROM " + TABLENAME + " WHERE " + SORT + " < ? AND product_id = ? ORDER BY " + SORT + " DESC";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setInt(1, sort);
            prepStmt.setLong(2, productid.longValue());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                Long previousid = new Long(rs.getLong(PKID));
                int decrementedorder = rs.getInt(SORT);
                updateStmt = "UPDATE " + TABLENAME + " SET " + SORT + " = ? WHERE " + PKID + " = ?  AND product_id = ?";
                prepStmt = con.prepareStatement(updateStmt);
                prepStmt.setInt(1, sort);
                prepStmt.setLong(2, previousid.longValue());
                prepStmt.setLong(3, productid.longValue());
                prepStmt.executeUpdate();
                updateStmt = "UPDATE " + TABLENAME + " SET " + SORT + " = ? WHERE " + PKID + " = ?  AND product_id = ?";
                prepStmt = con.prepareStatement(updateStmt);
                prepStmt.setInt(1, decrementedorder);
                prepStmt.setLong(2, pkid.longValue());
                prepStmt.setLong(3, productid.longValue());
                prepStmt.executeUpdate();
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
            if (rs != null) {
                rs.close();
            }
            closeConnection(con);
        }
    }

    private void MoveDown(Long pkid, int sort, Long productid) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String selectStmt = "";
            String updateStmt = "";
            con = makeConnection();
            selectStmt = "SELECT " + PKID + ", " + SORT + " FROM " + TABLENAME + " WHERE " + SORT + " > ? AND product_id = ? ORDER BY " + SORT + " ASC ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setInt(1, sort);
            prepStmt.setLong(2, productid.longValue());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                Long nextid = new Long(rs.getLong(PKID));
                int incrementedorder = rs.getInt(SORT);
                updateStmt = "UPDATE " + TABLENAME + " SET " + SORT + " = ? WHERE " + PKID + " = ?  AND product_id = ?";
                prepStmt = con.prepareStatement(updateStmt);
                prepStmt.setInt(1, sort);
                prepStmt.setLong(2, nextid.longValue());
                prepStmt.setLong(3, productid.longValue());
                prepStmt.executeUpdate();
                updateStmt = "UPDATE " + TABLENAME + " SET " + SORT + " = ? WHERE " + PKID + " = ?  AND product_id = ?";
                prepStmt = con.prepareStatement(updateStmt);
                prepStmt.setInt(1, incrementedorder);
                prepStmt.setLong(2, pkid.longValue());
                prepStmt.setLong(3, productid.longValue());
                prepStmt.executeUpdate();
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
            if (rs != null) {
                rs.close();
            }
            closeConnection(con);
        }
    }
}
