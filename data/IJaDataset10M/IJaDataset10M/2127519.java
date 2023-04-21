package spidr.dbload;

import java.io.*;
import java.util.*;
import java.sql.*;
import spidr.dbaccess.*;
import wdc.dbaccess.*;
import wdc.utils.*;
import spidr.datamodel.*;

/*** The class contains static methods to load GOES data into the database*/
public class GOESDBLoad {

    /**Used as default to replace unknown float values*/
    public static final float BADVALUE = 1.0e+33f;

    /**   * Reads data from source file and returns a vector of DailyData objects.   * @param fileName The file name   * @return a vector of DailyData objects (may be empty)   * @throws DBLoadException   */
    public static Vector readData(String fileName, String fileParams) throws DBLoadException {
        RandomAccessFile fileIn = null;
        try {
            String str;
            String s;
            StringTokenizer st;
            boolean isUncorrType;
            float badMagValue;
            float badXRayValue;
            final int NUM_COLS = 14;
            final char[] DATATYPES = { 'g', 'z', 'i', 'a', 'h' };
            int dataTypeNum;
            final String[] CHECKSTRINGS = { "YYMMDDHHMMDAYXLXSHPHEHNHTE1P1P2P3P4P5P6P7", "YYMMDDHHMMDAYXLXSHPHEHNHTE1P1P2P3P4P5P6P7", "YYMMDDHHMMDAYXLXSHPHEHNHTE1I1I2I3I4I5I6I7", "YYMMDDHHMMDAYXLXSHPHEHNHTE1A1A2A3A4A5A6", "YYMMDDHHMMDAYXLXSHPHEHNHTE1P8P9P10P11A7A8" };
            final int NUM_DESCRCOLS = 3;
            final String[] MAINCOLS = { "xl", "xs", "hp", "he", "hn", "ht", "el" };
            final String[][] DATACOLS = { { "g_p1", "g_p2", "g_p3", "g_p4", "g_p5", "g_p6", "g_p7" }, { "z_p1", "z_p2", "z_p3", "z_p4", "z_p5", "z_p6", "z_p7" }, { "i_i1", "i_i2", "i_i3", "i_i4", "i_i5", "i_i6", "i_i7" }, { "a_a1", "a_a2", "a_a3", "a_a4", "a_a5", "a_a6" }, { "h_p8", "h_p9", "h_p10", "h_p11", "h_a7", "h_a8" } };
            fileIn = new RandomAccessFile(fileName, "r");
            Vector dailyRecords = new Vector();
            str = fileIn.readLine();
            if (str == null) return dailyRecords;
            st = new StringTokenizer(str, "-(\" ");
            if (st.countTokens() < 6) throw new DBLoadException("Corrupted header: unstandard first line");
            s = st.nextToken();
            int satid;
            int sampling;
            if (!s.equals("GOES")) throw new DBLoadException("Corrupted header: tag \"GOES\" not found");
            try {
                satid = Integer.parseInt(st.nextToken());
            } catch (NumberFormatException e) {
                throw new DBLoadException("Corrupted header: GOES Id not found in line 1");
            }
            st.nextToken();
            st.nextToken();
            st.nextToken();
            try {
                sampling = Integer.parseInt(st.nextToken());
            } catch (NumberFormatException e) {
                throw new DBLoadException("Corrupted header: sampling not found in line 1");
            }
            if (satid <= 0 || satid > 99) throw new DBLoadException("Corrupted header: satid is out of range in line 1");
            Station station = new Station("" + satid, "GOES", "GOES-" + satid);
            int recsPerDay;
            if (sampling == 1) recsPerDay = 1440; else if (sampling == 5) recsPerDay = 288; else throw new DBLoadException("Corrupted header: sampling is not valid in line 1");
            float[][] data = new float[NUM_COLS][recsPerDay];
            fileIn.readLine();
            int year;
            str = fileIn.readLine();
            st = new StringTokenizer(str, " \t\n\r:,");
            if (st.countTokens() == 2) st.nextToken(); else if (st.countTokens() == 6) {
                st.nextToken();
                st.nextToken();
                st.nextToken();
                fileIn.readLine();
            } else throw new DBLoadException("Corrupted header: can not extract a year");
            try {
                year = Integer.parseInt(st.nextToken());
            } catch (NumberFormatException e) {
                throw new DBLoadException("Corrupted header: can not extract a year");
            }
            if (year < 1970) year += 100;
            for (int k = 0; k < 11; k++) fileIn.readLine();
            str = fileIn.readLine();
            if (str.indexOf("Uncorrected") == -1) isUncorrType = false; else isUncorrType = true;
            do {
                str = fileIn.readLine();
                st = new StringTokenizer(str, " \t\n\r");
            } while (st.hasMoreTokens());
            try {
                str = fileIn.readLine();
                st = new StringTokenizer(str);
                s = "";
                while (st.hasMoreTokens()) s = st.nextToken();
                badMagValue = Float.valueOf(s).floatValue();
                str = fileIn.readLine();
                st = new StringTokenizer(str);
                s = "";
                while (st.hasMoreTokens()) s = st.nextToken();
                badXRayValue = Float.valueOf(s).floatValue();
            } catch (NumberFormatException e) {
                throw new DBLoadException("Corrupted header: can not extract bad values");
            }
            fileIn.readLine();
            str = fileIn.readLine();
            st = new StringTokenizer(str, " \t\n\r");
            str = "";
            while (st.hasMoreTokens()) str += st.nextToken();
            int i = 0;
            while (i < CHECKSTRINGS.length && !CHECKSTRINGS[i].equals(str)) i++;
            if (i == CHECKSTRINGS.length) throw new DBLoadException("Corrupted header: unknown file type");
            if (DATATYPES[i] == 'g' || DATATYPES[i] == 'z') {
                if (isUncorrType) dataTypeNum = 0; else dataTypeNum = 1;
            } else dataTypeNum = i;
            fileIn.readLine();
            int numRecs = 0;
            int prevDate = -1;
            byte[] buf = new byte[100000];
            int curPtr = 0;
            long oldFilePointer = fileIn.getFilePointer();
            int bytesRead = fileIn.read(buf);
            long length = fileIn.length();
            while (bytesRead != -1) {
                int newCurPtr = curPtr;
                while (newCurPtr < bytesRead && buf[newCurPtr] != 0x0a) newCurPtr++;
                if (newCurPtr >= bytesRead) {
                    if (bytesRead == buf.length && curPtr > 0) {
                        oldFilePointer += curPtr;
                        fileIn.seek(oldFilePointer);
                        bytesRead = fileIn.read(buf);
                        curPtr = 0;
                        continue;
                    } else bytesRead = -1;
                }
                str = new String(buf, curPtr, newCurPtr - curPtr);
                curPtr = newCurPtr + 1;
                st = new StringTokenizer(str, " \t\n\r");
                if (!st.hasMoreTokens()) continue;
                if (st.countTokens() < NUM_DESCRCOLS + MAINCOLS.length + DATACOLS[dataTypeNum].length) throw new DBLoadException("Corrupted data: to few values per line");
                int date;
                try {
                    date = Integer.parseInt(st.nextToken());
                } catch (NumberFormatException e) {
                    throw new DBLoadException("Corrupted data: can not extract date");
                }
                if (numRecs > 0 && prevDate != date) throw new DBLoadException("Corrupted data: day is not complete");
                if (numRecs == 0 && prevDate == date) {
                    System.out.println(date);
                    throw new DBLoadException("Corrupted data: too long day");
                }
                prevDate = date;
                st.nextToken();
                st.nextToken();
                for (int k = 0; k < MAINCOLS.length + DATACOLS[dataTypeNum].length; k++) {
                    try {
                        data[k][numRecs] = Float.parseFloat(st.nextToken());
                        if (k >= 2 && k <= 5) {
                            if (data[k][numRecs] == badMagValue) data[k][numRecs] = BADVALUE;
                        } else if (data[k][numRecs] == badXRayValue) data[k][numRecs] = BADVALUE;
                    } catch (NumberFormatException e) {
                        throw new DBLoadException("Corrupted data: can not parse float value");
                    }
                }
                numRecs++;
                if (numRecs == recsPerDay) {
                    int month = (date / 100) % 100;
                    int day = date % 100;
                    int dayid = year * 10000 + month * 100 + day;
                    for (int k = 0; k < MAINCOLS.length + DATACOLS[dataTypeNum].length; k++) {
                        String chn;
                        if (k < MAINCOLS.length) chn = MAINCOLS[k]; else chn = DATACOLS[dataTypeNum][k - MAINCOLS.length];
                        float[] curData = new float[data[k].length];
                        for (int j = 0; j < curData.length; j++) curData[j] = data[k][j];
                        DataDescription descr = new DataDescription("GOES", chn);
                        dailyRecords.add(new DailyData(dayid, curData, descr, station));
                    }
                    numRecs = 0;
                }
            }
            if (numRecs != 0) throw new DBLoadException("Corrupted data: last day is not complete");
            return dailyRecords;
        } catch (Exception e) {
            throw new DBLoadException("Parsing error: " + e.toString());
        } finally {
            try {
                if (fileIn != null) fileIn.close();
            } catch (Exception e) {
            }
        }
    }

    /**   * Inserts (replaces) data into the database.   * All passed data should have the same station, sampling, year and month.   * @param con The database connection object.   * @param dailyRecords The vector of DailyData objects.   * @throws DBLoadException   */
    public static void loadData(Connection con, Vector dailyRecords) throws DBLoadException {
        if (dailyRecords == null || dailyRecords.size() == 0) return;
        int year = (((DailyData) dailyRecords.get(0)).getDayId()) / 10000;
        int month = ((((DailyData) dailyRecords.get(0)).getDayId()) / 100) % 100;
        int numData = (((DailyData) dailyRecords.get(0)).getData()).length;
        if (((DailyData) dailyRecords.get(0)).getStation() == null) throw new DBLoadException("No station: station required to load data");
        String stCode = ((DailyData) dailyRecords.get(0)).getStation().getStn();
        String stName = ((DailyData) dailyRecords.get(0)).getStation().getName();
        if (numData != 1440 && numData != 288) throw new DBLoadException("Wrong data: all data must have 1-minute or 5-minute sampling");
        int sampling = 1440 / numData;
        for (int k = 0; k < dailyRecords.size(); k++) {
            DailyData dd = (DailyData) dailyRecords.get(k);
            if (!dd.getDescription().getTable().equals("GOES")) throw new DBLoadException("Wrong table: the method can be used to load only GOES data.");
            if (dd.getStation() == null) throw new DBLoadException("No station: station required to load data");
            if (!stCode.equals(dd.getStation().getStn())) throw new DBLoadException("Wrong station: all data must have the same station");
            if ((dd.getDayId()) / 100 != year * 100 + month || (dd.getData()).length != numData) throw new DBLoadException("Wrong data: all data must have the same sampling, year and month");
        }
        String tableName = "goes_" + sampling + "min_" + year;
        try {
            Statement stmt = con.createStatement();
            String sqlStr = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "satid int NOT NULL, " + "dayid int NOT NULL, " + "element varchar(5) NOT NULL, " + "bindata blob, " + "datamin float, " + "datamax float, " + "PRIMARY KEY (satid, element, dayid), " + "INDEX elem_dayid_index (element, dayid), " + "INDEX dayid_index (dayid) )";
            stmt.executeUpdate(sqlStr);
            sqlStr = "REPLACE INTO " + tableName + "(satid,element,dayid,bindata,datamin,datamax) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sqlStr);
            for (int nRec = 0; nRec < dailyRecords.size(); nRec++) {
                DailyData dd = (DailyData) dailyRecords.get(nRec);
                int satid = Integer.parseInt(dd.getStation().getStn());
                int dayid = dd.getDayId();
                String element = dd.getDescription().getElement();
                float[] data = dd.getData();
                if (data == null) continue;
                ByteArrayOutputStream bostr = new ByteArrayOutputStream();
                DataOutputStream dostr = new DataOutputStream(bostr);
                float datamin = BADVALUE;
                float datamax = BADVALUE;
                for (int k = 0; k < data.length; k++) {
                    if (data[k] != BADVALUE) {
                        if (datamin > data[k] || datamin == BADVALUE) datamin = data[k];
                        if (datamax < data[k] || datamax == BADVALUE) datamax = data[k];
                    }
                    dostr.writeFloat(data[k]);
                }
                byte[] buf = bostr.toByteArray();
                ByteArrayInputStream binstr = new ByteArrayInputStream(buf);
                ps.setInt(1, satid);
                ps.setString(2, element);
                ps.setInt(3, dayid);
                ps.setBinaryStream(4, binstr, buf.length);
                ps.setString(5, "" + datamin);
                ps.setString(6, "" + datamax);
                ps.executeUpdate();
                binstr.close();
                dostr.close();
                bostr.close();
            }
            ps.close();
            {
                Vector iuList = new Vector();
                WDCDay dateFrom = new WDCDay(year, month, 1);
                WDCDay dateTo = new WDCDay(year, month, WDCDay.daysInMonth(year, month));
                Vector tables = DBAccess.getAllTables(stmt);
                String[] tableList = { "goes_1min_" + year, "goes_5min_" + year };
                for (int num = 0; num < tableList.length; num++) {
                    if (tables.indexOf(tableList[num]) == -1) continue;
                    sqlStr = "SELECT \"GOES\", satid, element, FLOOR(dayid/100) AS yrMon, count(*)" + " FROM " + tableList[num] + " WHERE satid=" + stCode + " AND dayid BETWEEN \"" + dateFrom.getDayId() + "\" AND \"" + dateTo.getDayId() + "\"" + " GROUP BY satid, element, yrMon";
                    ResultSet rs = stmt.executeQuery(sqlStr);
                    while (rs.next()) {
                        InventoryUnit iu = new InventoryUnit(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
                        int ind = iuList.indexOf(iu);
                        if (ind == -1) iuList.add(iu); else ((InventoryUnit) iuList.get(ind)).incNumRecs(iu.getNumRecs());
                    }
                    rs.close();
                }
                GOESMetadata.updateInventory(con, iuList, false);
            }
            {
                sqlStr = "SELECT station FROM goes_stations WHERE station=\"" + stCode + "\"";
                ResultSet rs = stmt.executeQuery(sqlStr);
                if (!rs.next()) {
                    sqlStr = "INSERT INTO goes_stations(station, description)" + " VALUES(\"" + stCode + "\",\"" + stName + "\")";
                    stmt.executeUpdate(sqlStr);
                }
            }
            stmt.close();
        } catch (Exception e) {
            throw new DBLoadException("Loading error: " + e.toString());
        }
    }

    /**   * Inserts sync data into database (1 month, 1 station, 1 element)   * @param con The database connection object.   * @param dataTable The dataTable to check configuration   * @param list Vector of HashMap objects (records in DB)   * @return number of loaded new records   * @throws DBLoadException   */
    public static Integer loadSyncData(Connection con, String dataTable, Vector list) throws DBLoadException {
        if (dataTable == null || !dataTable.equals("GOES")) throw new DBLoadException("The method is used to load data for GOES and it can't be used for table " + dataTable);
        int numNewRecs = 0;
        if (list == null || list.size() == 0) return new Integer(0);
        PreparedStatement ps1 = null, ps5 = null;
        Statement stmt = null;
        int year, month;
        String firstStn, firstElem;
        try {
            stmt = con.createStatement();
            String tableName1, tableName5;
            {
                HashMap rec = (HashMap) list.get(0);
                try {
                    year = ((Integer) rec.get("dayId")).intValue() / 10000;
                    if (year < 1980 || year > 2030) throw new Exception("Corrupted data: bad year=" + year);
                    tableName1 = "goes_1min_" + year;
                    tableName5 = "goes_5min_" + year;
                    month = ((Integer) rec.get("dayId")).intValue() % 10000 / 100;
                    if (month < 1 || month > 12) throw new Exception("Corrupted data: bad month=" + month);
                    firstStn = (String) rec.get("stn");
                    firstElem = (String) rec.get("element");
                } catch (Exception e) {
                    throw new Exception("Corrupted data: problem to get year or month");
                }
                {
                    String sqlStr = "CREATE TABLE IF NOT EXISTS " + tableName1 + " (" + "satid int NOT NULL, " + "dayid int NOT NULL, " + "element varchar(5) NOT NULL, " + "bindata blob, " + "datamin float, " + "datamax float, " + "PRIMARY KEY (satid, element, dayid), " + "INDEX elem_dayid_index (element, dayid), " + "INDEX dayid_index (dayid) )";
                    stmt.executeUpdate(sqlStr);
                    sqlStr = "INSERT IGNORE INTO " + tableName1 + "(satid,element,dayid,bindata,datamin,datamax) VALUES(?,?,?,?,?,?)";
                    ps1 = con.prepareStatement(sqlStr);
                }
                {
                    String sqlStr = "CREATE TABLE IF NOT EXISTS " + tableName5 + " (" + "satid int NOT NULL, " + "dayid int NOT NULL, " + "element varchar(5) NOT NULL, " + "bindata blob, " + "datamin float, " + "datamax float, " + "PRIMARY KEY (satid, element, dayid), " + "INDEX elem_dayid_index (element, dayid), " + "INDEX dayid_index (dayid) )";
                    stmt.executeUpdate(sqlStr);
                    sqlStr = "INSERT IGNORE INTO " + tableName5 + "(satid,element,dayid,bindata,datamin,datamax) VALUES(?,?,?,?,?,?)";
                    ps5 = con.prepareStatement(sqlStr);
                }
            }
            for (int nRec = 0; nRec < list.size(); nRec++) {
                HashMap rec = (HashMap) list.get(nRec);
                int dayId = ((Integer) rec.get("dayId")).intValue();
                int sampling = ((Integer) rec.get("sampling")).intValue();
                String stn = (String) rec.get("stn");
                String elem = (String) rec.get("element");
                float dataMin = ((Float) rec.get("dataMin")).floatValue();
                float dataMax = ((Float) rec.get("dataMax")).floatValue();
                byte[] data = (byte[]) rec.get("data");
                PreparedStatement ps;
                if (sampling == 1) ps = ps1; else if (sampling == 5) ps = ps5; else continue;
                ps.setString(1, stn);
                ps.setString(2, elem);
                ps.setInt(3, dayId);
                ps.setBytes(4, data);
                ps.setString(5, "" + dataMin);
                ps.setString(6, "" + dataMax);
                numNewRecs += ps.executeUpdate();
            }
            {
                Vector iuList = new Vector();
                WDCDay dateFrom = new WDCDay(year, month, 1);
                WDCDay dateTo = new WDCDay(year, month, WDCDay.daysInMonth(year, month));
                Vector tables = DBAccess.getAllTables(stmt);
                String[] tableList = { "goes_1min_" + year, "goes_5min_" + year };
                for (int num = 0; num < tableList.length; num++) {
                    if (tables.indexOf(tableList[num]) == -1) continue;
                    String sqlStr = "SELECT \"GOES\", satid, element, FLOOR(dayid/100) AS yrMon, count(*)" + " FROM " + tableList[num] + " WHERE satid=" + firstStn + " AND element=\"" + firstElem + "\"" + " AND dayid BETWEEN \"" + dateFrom.getDayId() + "\" AND \"" + dateTo.getDayId() + "\"" + " GROUP BY satid, element, yrMon";
                    ResultSet rs = stmt.executeQuery(sqlStr);
                    while (rs.next()) {
                        InventoryUnit iu = new InventoryUnit(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
                        int ind = iuList.indexOf(iu);
                        if (ind == -1) iuList.add(iu); else ((InventoryUnit) iuList.get(ind)).incNumRecs(iu.getNumRecs());
                    }
                    rs.close();
                }
                GOESMetadata.updateInventory(con, iuList, false);
            }
            {
                String sqlStr = "INSERT IGNORE INTO goes_stations(station, description)" + " VALUES(\"" + firstStn + "\",\"" + firstStn + "\")";
                stmt.executeUpdate(sqlStr);
            }
        } catch (Exception e) {
            throw new DBLoadException("loadSyncData@GOESDBLoad: Problem to load data: " + e.toString());
        } finally {
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (Exception e) {
                }
            }
            if (ps5 != null) {
                try {
                    ps5.close();
                } catch (Exception e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
        }
        return new Integer(numNewRecs);
    }
}
