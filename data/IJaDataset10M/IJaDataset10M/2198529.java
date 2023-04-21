package com.vlee.ejb.accounting;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import com.vlee.local.*;
import com.vlee.util.*;

public class FiscalYearBean implements EntityBean {

    private String dsName = ServerConfig.DATA_SOURCE;

    protected final String TABLENAME = "acc_fiscal_year";

    protected final String strObjectName = "FiscalYearBean: ";

    private EntityContext mContext;

    private Integer pkId;

    private Integer fiscalYear;

    private String beginMonth;

    private String endMonth;

    private String status;

    public Integer getPkId() {
        return this.pkId;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    public String getBeginMonth() {
        return this.beginMonth;
    }

    public String getEndMonth() {
        return this.endMonth;
    }

    public String getStatus() {
        return this.status;
    }

    public void setPkId(Integer pkid) {
        this.pkId = pkid;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public void setBeginMonth(String beginMonth) {
        this.beginMonth = beginMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FiscalYearObject getValueObject() {
        FiscalYearObject fyo = new FiscalYearObject();
        fyo.pkId = this.pkId;
        fyo.fiscalYear = this.fiscalYear;
        fyo.beginMonth = this.beginMonth;
        fyo.endMonth = this.endMonth;
        fyo.status = this.status;
        return fyo;
    }

    public void setValueObject(FiscalYearObject fyo) throws Exception {
        if (fyo == null) {
            throw new Exception("Object undefined");
        }
        this.fiscalYear = fyo.fiscalYear;
        this.beginMonth = fyo.beginMonth;
        this.endMonth = fyo.endMonth;
        this.status = fyo.status;
    }

    public void setEntityContext(EntityContext mContext) {
        Log.printVerbose(strObjectName + " In setEntityContext");
        this.mContext = mContext;
        Log.printVerbose(strObjectName + " Leaving setEntityContext");
    }

    public void unsetEntityContext() {
        Log.printVerbose(strObjectName + " In unsetEntityContext");
        this.mContext = null;
        Log.printVerbose(strObjectName + " Leaving unsetEntityContext");
    }

    public Integer ejbCreate(Integer fiscalYear, String beginMonth, String endMonth, String status) throws CreateException {
        Integer newPkId = null;
        Log.printVerbose(strObjectName + " In ejbCreate");
        newPkId = insertNewRow(fiscalYear, beginMonth, endMonth, status);
        if (newPkId != null) {
            this.pkId = newPkId;
            this.fiscalYear = fiscalYear;
            this.beginMonth = beginMonth;
            this.endMonth = endMonth;
            this.status = status;
        }
        Log.printVerbose(strObjectName + " Leaving ejbCreate");
        return newPkId;
    }

    public void ejbRemove() {
        Log.printVerbose(strObjectName + " In ejbRemove");
        deleteObject(this.pkId);
        Log.printVerbose(strObjectName + " Leaving ejbRemove");
    }

    public void ejbActivate() {
        Log.printVerbose(strObjectName + " In ejbActivate");
        this.pkId = (Integer) mContext.getPrimaryKey();
        Log.printVerbose(strObjectName + " Leaving ejbActivate");
    }

    public void ejbPassivate() {
        Log.printVerbose(strObjectName + " In ejbPassivate");
        this.pkId = null;
        Log.printVerbose(strObjectName + " Leaving ejbPassivate");
    }

    public void ejbLoad() {
        Log.printVerbose(strObjectName + " In ejbLoad");
        loadObject();
        Log.printVerbose(strObjectName + " Leaving ejbLoad");
    }

    public void ejbStore() {
        Log.printVerbose(strObjectName + " In ejbStore");
        storeObject();
        Log.printVerbose(strObjectName + " Leaving ejbStore");
    }

    public void ejbPostCreate(Integer fiscalYear, String beginMonth, String endMonth, String status) {
    }

    public Integer ejbFindByPrimaryKey(Integer pkid) throws FinderException {
        Log.printVerbose(strObjectName + " In ejbFindByPrimaryKey");
        boolean result;
        result = selectByPrimaryKey(pkid);
        Log.printVerbose(strObjectName + " Leaving ejbFindByPrimaryKey");
        if (result) {
            return pkid;
        } else {
            throw new ObjectNotFoundException("Row for id " + pkid.toString() + " not found.");
        }
    }

    public Collection ejbFindAllObjects() throws FinderException {
        Log.printVerbose(strObjectName + " In ejbFindAllObjects");
        Collection col = selectAllObjects();
        Log.printVerbose(strObjectName + " Leaving ejbFindAllObjects");
        return col;
    }

    public Collection ejbFindObjectsGiven(String fieldName, String value) throws FinderException {
        Log.printVerbose(strObjectName + " In ejbFindObjectsGiven");
        Collection col = selectObjectsGiven(fieldName, value);
        Log.printVerbose(strObjectName + " Leaving ejbFindObjectsGiven");
        return col;
    }

    public Vector ejbHomeGetValueObjectsGiven(String fieldName1, String value1, String fieldName2, String value2) {
        Log.printVerbose(strObjectName + " In ejbHomeGetValueObjectsGiven");
        Vector vecValObj = selectValueObjectsGiven(fieldName1, value1, fieldName2, value2);
        Log.printVerbose(strObjectName + " Leaving ejbHomeGetValueObjectsGiven");
        return vecValObj;
    }

    /** ***************** Database Routines ************************ */
    private DataSource getDataSource() {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(dsName);
            return ds;
        } catch (NamingException ne) {
            throw new EJBException("Naming lookup failure: " + ne.getMessage());
        }
    }

    private void cleanup(Connection cn, PreparedStatement ps) {
        try {
            if (ps != null) ps.close();
        } catch (Exception e) {
            throw new EJBException(e);
        }
        try {
            if (cn != null) cn.close();
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    private Integer insertNewRow(Integer fiscalYear, String beginMonth, String endMonth, String status) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            Integer newPkId = getNextPKId();
            String sqlStatement = "INSERT INTO " + TABLENAME + " (pkid, fiscalyear, beginmonth, endmonth, status) VALUES (?, ?, ?, ?, ?)";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setInt(1, newPkId.intValue());
            ps.setInt(2, fiscalYear.intValue());
            ps.setString(3, beginMonth);
            ps.setString(4, endMonth);
            ps.setString(5, status);
            ps.executeUpdate();
            Log.printAudit(strObjectName + " Created New Row:" + newPkId.toString());
            return newPkId;
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Integer getNextPKId() {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "SELECT MAX(pkid) as max_pkid FROM " + TABLENAME;
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int max = rs.getInt("max_pkid");
            if (max == 0) {
                max = 1000;
            } else {
                max += 1;
            }
            return new Integer(max);
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private void deleteObject(Integer pkid) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "DELETE FROM " + TABLENAME + " WHERE pkid = ?";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setInt(1, pkid.intValue());
            ps.executeUpdate();
            Log.printAudit(strObjectName + " Deleted Object: " + pkid.toString());
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private void loadObject() {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "SELECT pkid, fiscalyear, beginmonth, endmonth, status FROM " + TABLENAME + " WHERE pkid = ?";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setInt(1, this.pkId.intValue());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.pkId = new Integer(rs.getInt("pkid"));
                this.fiscalYear = new Integer(rs.getInt("fiscalyear"));
                this.beginMonth = rs.getString("beginmonth");
                this.endMonth = rs.getString("endmonth");
                this.status = rs.getString("status");
            } else {
                throw new NoSuchEntityException("Row for this EJB Object is not found in database.");
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private void storeObject() {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "UPDATE " + TABLENAME + " SET fiscalyear = ?, beginmonth = ?, endmonth = ?, status = ? WHERE pkid = ?";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setInt(1, this.fiscalYear.intValue());
            ps.setString(2, this.beginMonth);
            ps.setString(3, this.endMonth);
            ps.setString(4, this.status);
            ps.setInt(5, this.pkId.intValue());
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                throw new EJBException("Storing ejb object " + this.pkId.toString() + " failed.");
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private boolean selectByPrimaryKey(Integer pkid) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "SELECT * FROM " + TABLENAME + " WHERE pkid = ?";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setInt(1, pkid.intValue());
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            return result;
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Collection selectAllObjects() {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            ArrayList objectSet = new ArrayList();
            String sqlStatement = "SELECT pkid FROM " + TABLENAME + " ORDER BY pkid";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                while (rs.next()) {
                    objectSet.add(new Integer(rs.getInt(1)));
                }
                return objectSet;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Collection selectObjectsGiven(String fieldName, String value) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            ArrayList objectSet = new ArrayList();
            String sqlStatement = "SELECT pkid FROM " + TABLENAME + " WHERE " + fieldName + " = ? ORDER BY pkid";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                while (rs.next()) {
                    objectSet.add(new Integer(rs.getInt(1)));
                }
                return objectSet;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Vector selectValueObjectsGiven(String fieldName1, String value1, String fieldName2, String value2) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        Vector vecValObj = new Vector();
        try {
            String sqlStatement = "SELECT pkid, fiscalyear, beginmonth, endmonth, status FROM " + TABLENAME + " WHERE " + fieldName1 + " = ? ORDER BY fiscalyear, beginmonth, endmonth, pkid ";
            if (fieldName2 != null && value2 != null) {
                sqlStatement = sqlStatement + " AND " + fieldName2 + " = ?";
            }
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setString(1, value1);
            if (fieldName2 != null && value2 != null) {
                ps.setString(2, value2);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FiscalYearObject fyo = new FiscalYearObject();
                fyo.pkId = new Integer(rs.getInt("pkid"));
                fyo.fiscalYear = new Integer(rs.getInt("fiscalyear"));
                fyo.beginMonth = rs.getString("beginmonth");
                fyo.endMonth = rs.getString("endmonth");
                fyo.status = rs.getString("status");
                vecValObj.add(fyo);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
        return vecValObj;
    }
}
