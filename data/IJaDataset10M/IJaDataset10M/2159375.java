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

public class EHeaderLogoBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String ACTIVE = "active";

    public static final String TITLE = "title";

    public static final String WLINK = "wlink";

    public static final String IMG_LOGO = "img_logo";

    public static final String EXTENSION = "extension";

    public static final String WIDTH = "width";

    public static final String HEIGHT = "height";

    public static final String MODULENAME = "ecommerce";

    public static final Long PKID_START = new Long(100001);

    EHeaderLogoObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "ecom_header_logo";

    public static final String TABLE_IMAGES = "ecom_images";

    private static final String strObjectName = "EHeaderLogoBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public EHeaderLogoObject getObject() {
        return this.valObj;
    }

    public void setObject(EHeaderLogoObject newVal) {
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
    public Long ejbCreate(EHeaderLogoObject newObj) throws CreateException {
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
            this.valObj = new EHeaderLogoObject();
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
    public void ejbPostCreate(EHeaderLogoObject newObj) {
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

    public Collection ejbHomeGetObjectsInfo(String query) {
        Collection col = null;
        try {
            col = selectObjectsInfo(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
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

    private EHeaderLogoObject insertObject(EHeaderLogoObject newObj) throws NamingException, SQLException {
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
                String insertStatement = " INSERT INTO " + TABLENAME + " VALUES ( ?, ?, ?, ?, ? )";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setLong(1, newObj.pkid.longValue());
                prepStmt.setBoolean(2, newObj.active);
                prepStmt.setString(3, newObj.title);
                prepStmt.setString(4, newObj.wlink);
                prepStmt.setLong(5, newObj.img_logo.longValue());
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
            String deleteStatement = "";
            String selectStatement = "";
            String updateStatement = "";
            con = makeConnection();
            deleteStatement = " DELETE FROM " + TABLENAME + " WHERE " + PKID + " = ?";
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
            closeConnection(con);
        }
    }

    private void storeObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String updateStatement = "UPDATE " + TABLENAME + " SET " + ACTIVE + " = ?, " + TITLE + " = ?, " + WLINK + " = ?, " + IMG_LOGO + " = ? WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setBoolean(1, this.valObj.active);
            prepStmt.setString(2, this.valObj.title);
            prepStmt.setString(3, this.valObj.wlink);
            prepStmt.setLong(4, this.valObj.img_logo.longValue());
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

    private static synchronized Long getNextPKId(Connection con) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, PKID, TABLENAME, MODULENAME, PKID_START);
    }

    private static synchronized Long getNextStmtNo(Connection con, String stmtType, Long pcCenter) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, pcCenter.toString(), TABLENAME, stmtType, PKID_START);
    }

    public static EHeaderLogoObject getObject(ResultSet rs) throws Exception {
        EHeaderLogoObject theObj = null;
        try {
            theObj = new EHeaderLogoObject();
            theObj.pkid = new Long(rs.getLong(PKID));
            theObj.active = rs.getBoolean(ACTIVE);
            theObj.title = rs.getString(TITLE);
            theObj.wlink = rs.getString(WLINK);
            theObj.img_logo = new Long(rs.getLong(IMG_LOGO));
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
            prepStmt = con.prepareStatement(selectStmt);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                EHeaderLogoObject theObj = getObject(rs);
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

    private void setFalseActive(Connection con) throws NamingException, SQLException {
        PreparedStatement prepStmt = null;
        try {
            String updateStatement = " UPDATE " + TABLENAME + " SET " + ACTIVE + " = false";
            prepStmt = con.prepareStatement(updateStatement);
            int rowCount = prepStmt.executeUpdate();
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
        }
    }

    private Collection selectObjectsInfo(String query) throws NamingException, SQLException {
        Collection result = new Vector();
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String selectStmt = "";
            String updateStmt = "";
            con = makeConnection();
            selectStmt = "SELECT b.*, i." + WIDTH + " as " + WIDTH + ", i." + HEIGHT + " as " + HEIGHT + ", i." + EXTENSION + " as " + EXTENSION + " from " + TABLENAME + " b, " + TABLE_IMAGES + " i where b.img_logo = i.pkid " + query;
            prepStmt = con.prepareStatement(selectStmt);
            rs = prepStmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                EHeaderLogoObject theObj = new EHeaderLogoObject();
                theObj.pkid = new Long(rs.getLong(PKID));
                theObj.active = rs.getBoolean(ACTIVE);
                theObj.title = rs.getString(TITLE);
                theObj.wlink = rs.getString(WLINK);
                theObj.img_logo = new Long(rs.getLong(IMG_LOGO));
                theObj.width = rs.getInt(WIDTH);
                theObj.height = rs.getInt(HEIGHT);
                theObj.extension = rs.getString(EXTENSION);
                result.add(theObj);
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
}
