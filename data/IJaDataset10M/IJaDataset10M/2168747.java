package com.myechelon.data;

import com.memnon.sql.*;

import com.myechelon.util.Logger;

import java.sql.*;

import java.util.*;


public class DataDB
{
    public static final void getData(ConnectionManager conmgr, 
                                     DataSecurityManager secmgr, Data obj)
                              throws SQLException
    {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Enumeration enum;

        if (obj == null)

            return;

        try
        {
            conmgr.getConnection().createStatement().execute("begin;");
            pstmt = conmgr.getConnection().prepareCall("{call getData(?)}");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            rs = pstmt.executeQuery();
            rs.next();
            rs = pstmt.executeQuery(
                         "fetch all in \"" + rs.getString(1) + "\";");

            if (!rs.next())
                throw new NoSuchElementException("No matching entry found.");

            secmgr.checkSelect(rs.getInt(2), "Data");

            // REMOVED @[getOwnersAssoc]s
            obj.setSummary(rs.getString(3));
            obj.setUrl(rs.getString(4));

            // REMOVED @[getAssoc]s
        }
        finally
        {

            if (rs != null)
                rs.close();

            if (pstmt != null)
            {
                conmgr.getConnection().createStatement().execute("end;");
                pstmt.close();
            }
        }
    }

    public static final void insertData(ConnectionManager conmgr, 
                                        DataSecurityManager secmgr, Data obj)
                                 throws SQLException
    {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Enumeration enum;

        if (obj == null)

            return;

        secmgr.checkInsert("Data");

        try
        {
            pstmt = conmgr.getConnection().prepareCall(
                            "{call insertData(?,?,?,?)}");
            pstmt.setInt(1, 0);
            pstmt.setInt(2, secmgr.getAccountID());

            if (obj.getSummary() == null)
                pstmt.setNull(3, Types.VARCHAR);
            else
                pstmt.setString(3, obj.getSummary());

            if (obj.getUrl() == null)
                pstmt.setNull(4, Types.VARCHAR);
            else
                pstmt.setString(4, obj.getUrl());

            rs = pstmt.executeQuery();

            if (!rs.next())
                throw new SQLException("Could not add row to table.");

            obj.setPrimaryKey(new PK(rs.getInt(1)));

            // REMOVED @[insertAssoc]s
        }
        finally
        {

            if (rs != null)
                rs.close();

            if (pstmt != null)
            {
                pstmt.close();
            }
        }
    }

    public static final void updateData(ConnectionManager conmgr, 
                                        DataSecurityManager secmgr, Data obj)
                                 throws SQLException
    {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Enumeration enum;

        if (obj == null)

            return;

        try
        {

            // security check!
            conmgr.getConnection().createStatement().execute("begin;");
            pstmt = conmgr.getConnection().prepareCall(
                            "{call getDataAccountID(?)}");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            rs = pstmt.executeQuery();
            rs.next();
            rs = pstmt.executeQuery(
                         "fetch all in \"" + rs.getString(1) + "\";");

            if (!rs.next())
                throw new SQLException("No matching entry found.");

            secmgr.checkUpdate(rs.getInt(1), "Data");
            rs.close();
            pstmt.close();
            pstmt = conmgr.getConnection().prepareCall(
                            "{call updateData(?,?,?,?)}");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            pstmt.setInt(2, secmgr.getAccountID());

            if (obj.getSummary() == null)
                pstmt.setNull(3, Types.VARCHAR);
            else
                pstmt.setString(3, obj.getSummary());

            if (obj.getUrl() == null)
                pstmt.setNull(4, Types.VARCHAR);
            else
                pstmt.setString(4, obj.getUrl());

            rs = pstmt.executeQuery();

            // if(!rs.next()) throw new SQLException("Could not update row to table.");
            // REMOVED @[updateAssoc]s
        }
        finally
        {

            if (rs != null)
                rs.close();

            if (pstmt != null)
            {
                conmgr.getConnection().createStatement().execute("end;");
                pstmt.close();
            }
        }
    }

    public static final void deleteData(ConnectionManager conmgr, 
                                        DataSecurityManager secmgr, Data obj)
                                 throws SQLException
    {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Enumeration enum;

        if (obj == null)

            return;

        try
        {

            // security check!
            conmgr.getConnection().createStatement().execute("begin;");
            pstmt = conmgr.getConnection().prepareCall(
                            "{call getDataAccountID(?) }");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            rs = pstmt.executeQuery();
            rs.next();
            rs = pstmt.executeQuery(
                         "fetch all in \"" + rs.getString(1) + "\";");

            if (!rs.next())
            {
                pstmt.executeQuery("end;");
                throw new NoSuchElementException("No matching entry found.");
            }

            secmgr.checkUpdate(rs.getInt(1), "Data");
            rs.close();
            pstmt.close();
            pstmt = conmgr.getConnection().prepareCall("{call deleteData(?)}");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            rs = pstmt.executeQuery();

            // REMOVED @[deleteAssoc]s
        }
        finally
        {

            if (rs != null)
                rs.close();

            if (pstmt != null)
            {
                conmgr.getConnection().createStatement().execute("end;");
                pstmt.close();
            }
        }
    }
}