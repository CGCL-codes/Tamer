package spidr.dbaccess;

import java.sql.*;
import java.io.*;
import java.util.*;
import wdc.dbaccess.*;
import wdc.settings.*;
import wdc.utils.*;
import spidr.datamodel.*;

/**The class extends DBAccess to correct its activity for KpEst*/
public class KpEstDBAccess extends DBAccess {

    /** Returns minimal actual sampling in minutes for all elements and stations.  * @param element The element to be consider  * @param station The station to be consider  * @return 180 for element kp or ap and 1440 otherwise  */
    public int getMinSampling(String element, String station) {
        return 180;
    }

    /** Creates a sequence of DailyData objects.  * @param stmt The statement object  * @param descr The description of the data to be received  * @param station The parameter should be null  * @param dateInterval The time period of data to be received  * @param sampling The sampling of the data to be received in minutes [0,1440], 0 is min found sampling (without scaling)  * @return Vector of a sequences of daily data sets  */
    public Vector getDataSequence(Statement stmt, DataDescription descr, Station station, DateInterval dateInterval, int sampling) throws WDCDatabaseException, WrongDataException {
        if (!(sampling == MONTHLY_SAMPLING || sampling == YEARLY_SAMPLING || sampling >= 0 && sampling <= 1440)) throw new WrongDataException("Invalid sampling: " + sampling, WrongDataException.WRONG_SAMPLING);
        if (dateInterval == null) throw new WrongDataException("Unknown date interval", WrongDataException.UNDEF_DATEINTERVAL);
        if (descr == null) throw new WrongDataException("Unknown description", WrongDataException.UNDEF_DESCRIPTION);
        if (descr.getTable() == null || !descr.getTable().equals("KpEst")) throw new WrongDataException("The class can not be used for the table", WrongDataException.UNDEF_DESCRIPTION);
        String element = descr.getElement();
        int dbSampling = 180;
        if (sampling == 0) sampling = dbSampling;
        Vector dsList = new Vector();
        DataSequence ds = new DataSequence(descr, station, sampling);
        dsList.add(ds);
        if (!dateInterval.isValid()) return dsList;
        float multiplier = descr.getMultiplier();
        float missingValue = descr.getMissingValue();
        if (!element.equals("kpEst") || (sampling == MONTHLY_SAMPLING || sampling == YEARLY_SAMPLING)) return dsList;
        try {
            extendDateIntervalForSampling(dateInterval, sampling);
            String sqlStr = "SELECT DATE_FORMAT(obsdate, '%Y%m%d'), ek1, ek2, ek3, ek4, ek5, ek6, ek7, ek8 FROM kp WHERE " + " obsdate BETWEEN '" + dateInterval.getDateFrom() + "' AND '" + dateInterval.getDateTo() + "'" + " ORDER BY obsdate";
            ResultSet rs = stmt.executeQuery(sqlStr);
            while (rs.next()) {
                int dayId = rs.getInt(1);
                float[] data = new float[8];
                for (int k = 0; k < data.length; k++) data[k] = rs.getInt(k + 2);
                if (multiplier != 1) for (int k = 0; k < data.length; k++) if (data[k] != missingValue) data[k] *= multiplier;
                float[] newData = changeSampling(data, sampling, missingValue);
                if (newData != data) {
                    String[] descrArray = new String[newData.length];
                    for (int k = 0; k < descrArray.length; k++) descrArray[k] = "_Interpolated_";
                    ds.add(new DailyData(dayId, newData, descrArray, descr, station));
                } else ds.add(new DailyData(dayId, data, descr, station));
            }
            rs.close();
        } catch (SQLException e) {
            throw new WDCDatabaseException("KpEst data selection error: " + e.toString());
        }
        return dsList;
    }

    /** Used to test this class  */
    public static void main(String args[]) {
        System.out.println("Activate settings");
        try {
            if (args.length == 0) {
                System.out.println("No parameters: base conf-file required.");
                return;
            }
            Settings.getInstance().load(args[0]);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        Connection con = null;
        Statement stmt = null;
        DataDescription descr = new DataDescription("KpEst", "kpEst", "", "this is element kpEst", "elemUnits", "kpEst");
        DateInterval dateInterval = new DateInterval(new WDCDay(20030501), new WDCDay(20030506));
        try {
            KpEstDBAccess db = new KpEstDBAccess();
            con = ConnectionPool.getConnection("KpEst");
            stmt = con.createStatement();
            System.out.println("Start");
            long curTime = (new java.util.Date()).getTime();
            DataSequence ds = (DataSequence) db.getDataSequence(stmt, descr, null, dateInterval, 0).get(0);
            for (int n = 0; n < ds.size(); n++) {
                DailyData dd = (DailyData) ds.get(n);
                System.out.println("dayId: " + dd.getDayId());
                float[] data = dd.getData();
                if (data != null) for (int k = 0; k < data.length; k++) System.out.print(" " + data[k]);
                System.out.println();
            }
            curTime = (new java.util.Date()).getTime() - curTime;
            System.out.println("Stop.");
            System.out.println("Time: " + curTime + "; Num days: " + ((ds != null) ? ds.size() : 0));
        } catch (Exception e) {
            System.out.println("Error:" + e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (Exception e) {
            }
            ;
        }
    }
}
