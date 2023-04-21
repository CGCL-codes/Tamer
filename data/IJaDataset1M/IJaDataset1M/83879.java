package com.knowgate.crm;

import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.PreparedStatement;
import com.knowgate.debug.DebugFile;
import com.knowgate.misc.Gadgets;
import com.knowgate.jdc.JDCConnection;
import com.knowgate.dataobjs.DB;
import com.knowgate.dataobjs.DBBind;
import com.knowgate.dataobjs.DBSubset;
import com.knowgate.dataobjs.DBPersist;

/**
 * <p>Company</p>
 * <p>Copyright: Copyright (c) KnowGate 2003</p>
 * @author Sergio Montoro Ten
 * @version 2.1
 */
public class Company extends DBPersist {

    /**
   * Create Empty Company.
  */
    public Company() {
        super(DB.k_companies, "Company");
    }

    /**
   * Create Company and set gu_company field.
   * Does not load other fields from database.
   * @param sCompanyId Company GUID
   */
    public Company(String sCompanyId) {
        super(DB.k_companies, "Company");
        put(DB.gu_company, sCompanyId);
    }

    /**
   * Store Company
   * Automatically generates gu_company GUID and dt_modified DATE if not explicitly set.
   * @param oConn Database Connection
   * @throws SQLException
   */
    public boolean store(JDCConnection oConn) throws SQLException {
        java.sql.Timestamp dtNow = new java.sql.Timestamp(DBBind.getTime());
        if (!AllVals.containsKey(DB.gu_company)) put(DB.gu_company, Gadgets.generateUUID());
        replace(DB.dt_modified, dtNow);
        return super.store(oConn);
    }

    /**
   * Delete Company
   * @param oConn Database Connection
   * @throws SQLException
   */
    public boolean delete(JDCConnection oConn) throws SQLException {
        return Company.delete(oConn, getString(DB.gu_company));
    }

    /**
   * Add an Address to this Company
   * @param oConn Database Connection
   * @throws SQLException
   */
    public boolean addAddress(JDCConnection oConn, String sAddrGUID) throws SQLException {
        PreparedStatement oStmt = null;
        boolean bRetVal;
        try {
            oStmt = oConn.prepareStatement("INSERT INTO " + DB.k_x_company_addr + " (" + DB.gu_company + "," + DB.gu_address + ") VALUES (?,?)");
            oStmt.setString(1, getStringNull(DB.gu_company, null));
            oStmt.setString(2, sAddrGUID);
            int iAffected = oStmt.executeUpdate();
            oStmt.close();
            oStmt = null;
            bRetVal = (iAffected > 0);
        } catch (SQLException sqle) {
            bRetVal = false;
            try {
                if (oStmt != null) oStmt.close();
            } catch (Exception ignore) {
            }
        }
        return bRetVal;
    }

    /**
   * <p>Get Company Addresses</p>
   * @param oConn Database Connection
   * @return A DBSubset with all columns from k_addresses for Company
   * @throws SQLException
   */
    public DBSubset getAddresses(JDCConnection oConn) throws SQLException {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin Company.getAddresses([Connection])");
            DebugFile.incIdent();
        }
        com.knowgate.hipergate.Address oAddr = new com.knowgate.hipergate.Address();
        DBSubset oAddrs = new DBSubset(DB.k_addresses, oAddr.getTable(oConn).getColumnsStr(), DB.gu_address + " IN (SELECT " + DB.gu_address + " FROM " + DB.k_x_company_addr + " WHERE " + DB.gu_company + "='" + getString(DB.gu_company) + "')", 10);
        int iAddrs = oAddrs.load(oConn);
        oAddr = null;
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End Company.getAddresses() : " + String.valueOf(iAddrs));
        }
        return oAddrs;
    }

    /**
   * <p>Delete Company.</p>
   * Delete all associated contacts and call k_sp_del_company stored procedure.<br>
   * If k_orders table exists, then Orders for this Company are deleted.<br>
   * If k_projects table exists, then Projects for this Company are deleted.<br>
   * @param oConn Database Connection
   * @param sCompanyGUID Company GUID
   * @throws SQLException
   */
    public static boolean delete(JDCConnection oConn, String sCompanyGUID) throws SQLException {
        boolean bRetVal;
        Statement oStmt;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin Company.delete([Connection], " + sCompanyGUID + ")");
            DebugFile.incIdent();
        }
        if (DBBind.exists(oConn, DB.k_inet_addrs, "U")) {
            oStmt = oConn.createStatement();
            if (DebugFile.trace) DebugFile.writeln("UPDATE " + DB.k_inet_addrs + " SET " + DB.gu_company + "=NULL WHERE " + DB.gu_company + "='" + sCompanyGUID + "'");
            oStmt.executeUpdate("UPDATE " + DB.k_inet_addrs + " SET " + DB.gu_company + "=NULL WHERE " + DB.gu_company + "='" + sCompanyGUID + "'");
            oStmt.close();
        }
        if (DBBind.exists(oConn, DB.k_projects, "U")) {
            DBSubset oProjs = new DBSubset(DB.k_projects, DB.gu_project, DB.gu_company + "='" + sCompanyGUID + "'", 10);
            int iProjs = oProjs.load(oConn);
            for (int p = 0; p < iProjs; p++) com.knowgate.projtrack.Project.delete(oConn, oProjs.getString(0, p));
        }
        if (DBBind.exists(oConn, DB.k_orders, "U")) {
            DBSubset oOrders = new DBSubset(DB.k_orders, DB.gu_order, DB.gu_company + "='" + sCompanyGUID + "'", 1000);
            int iOrders = oOrders.load(oConn);
            for (int o = 0; o < iOrders; o++) com.knowgate.hipergate.Order.delete(oConn, oOrders.getString(0, o));
        }
        DBSubset oContacts = new DBSubset(DB.k_contacts, DB.gu_contact, DB.gu_company + "='" + sCompanyGUID + "'", 1000);
        int iContacts = oContacts.load(oConn);
        for (int c = 0; c < iContacts; c++) Contact.delete(oConn, oContacts.getString(0, c));
        oContacts = null;
        if (oConn.getDataBaseProduct() == JDCConnection.DBMS_POSTGRESQL) {
            oStmt = oConn.createStatement();
            if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(SELECT k_sp_del_company ('" + sCompanyGUID + "')");
            oStmt.executeQuery("SELECT k_sp_del_company ('" + sCompanyGUID + "')");
            oStmt.close();
            bRetVal = true;
        } else {
            if (DebugFile.trace) DebugFile.writeln("Conenction.prepareCall({call k_sp_del_company ('" + sCompanyGUID + "')}");
            CallableStatement oCall = oConn.prepareCall("{call k_sp_del_company ('" + sCompanyGUID + "')}");
            bRetVal = oCall.execute();
            oCall.close();
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End Company.delete() : " + String.valueOf(bRetVal));
        }
        return bRetVal;
    }

    public static final short ClassId = 91;
}
