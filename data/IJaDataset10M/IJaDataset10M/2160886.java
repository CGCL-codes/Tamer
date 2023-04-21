package com.myechelon.preparser;

import com.memnon.sql.*;

import com.myechelon.util.Logger;

import java.sql.*;

import java.util.*;


public class PatternPositionDB
{
    public static final void getPatternPosition(ConnectionManager conmgr, 
                                                DataSecurityManager secmgr, 
                                                PatternPosition obj)
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
            pstmt = conmgr.getConnection().prepareCall(
                            "{call getPatternPosition(?)}");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            rs = pstmt.executeQuery();
            rs.next();
            rs = pstmt.executeQuery(
                         "fetch all in \"" + rs.getString(1) + "\";");

            if (!rs.next())
                throw new NoSuchElementException("No matching entry found.");

            secmgr.checkSelect(rs.getInt(2), "PatternPosition");

            // REMOVED @[getOwnersAssoc]s
            obj.setChronologicIndex(rs.getInt(3));

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

    public static final void insertPatternPosition(ConnectionManager conmgr, 
                                                   DataSecurityManager secmgr, 
                                                   PatternPosition obj)
                                            throws SQLException
    {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Enumeration enum;

        if (obj == null)

            return;

        secmgr.checkInsert("PatternPosition");

        try
        {
            pstmt = conmgr.getConnection().prepareCall(
                            "{call insertPatternPosition(?,?,?)}");
            pstmt.setInt(1, 0);
            pstmt.setInt(2, secmgr.getAccountID());
            pstmt.setInt(3, obj.getChronologicIndex());
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

    public static final void updatePatternPosition(ConnectionManager conmgr, 
                                                   DataSecurityManager secmgr, 
                                                   PatternPosition obj)
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
                            "{call getPatternPositionAccountID(?)}");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            rs = pstmt.executeQuery();
            rs.next();
            rs = pstmt.executeQuery(
                         "fetch all in \"" + rs.getString(1) + "\";");

            if (!rs.next())
                throw new SQLException("No matching entry found.");

            secmgr.checkUpdate(rs.getInt(1), "PatternPosition");
            rs.close();
            pstmt.close();
            pstmt = conmgr.getConnection().prepareCall(
                            "{call updatePatternPosition(?,?,?)}");
            pstmt.setInt(1, obj.getPrimaryKey().getPK());
            pstmt.setInt(2, secmgr.getAccountID());
            pstmt.setInt(3, obj.getChronologicIndex());
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

    public static final void deletePatternPosition(ConnectionManager conmgr, 
                                                   DataSecurityManager secmgr, 
                                                   PatternPosition obj)
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
                            "{call getPatternPositionAccountID(?) }");
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

            secmgr.checkUpdate(rs.getInt(1), "PatternPosition");
            rs.close();
            pstmt.close();
            pstmt = conmgr.getConnection().prepareCall(
                            "{call deletePatternPosition(?)}");
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