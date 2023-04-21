package lusc.net.sourceforge;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sound.sampled.*;
import java.nio.*;

public class DbConnection {

    Connection con = null;

    boolean connected = false;

    boolean oldloader = false;

    String loc = "localhost";

    String dbase = null;

    String pword = null;

    String uname = null;

    int DBMODE = 0;

    public DbConnection(int DBMODE, String loc, String dbase, String uname, String pword) {
        this.DBMODE = DBMODE;
        this.loc = loc;
        this.dbase = dbase;
        this.uname = uname;
        this.pword = pword;
    }

    @SuppressWarnings("finally")
    public boolean doConnect() {
        try {
            String url = " ";
            if (DBMODE == 1) {
                url = "jdbc:mysql://" + loc + ":3306/" + dbase;
            } else if (DBMODE == 0) {
                System.out.println(loc);
                url = "jdbc:hsqldb:file:" + loc;
            } else if (DBMODE == 2) {
                System.out.println(loc);
                url = "jdbc:h2:" + loc;
            }
            System.out.println("DBC: " + DBMODE);
            try {
                if (DBMODE == 1) {
                    Class.forName("com.mysql.jdbc.Driver");
                } else if (DBMODE == 0) {
                    Class.forName("org.hsqldb.jdbcDriver");
                } else if (DBMODE == 2) {
                    Class.forName("org.h2.Driver");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("DBC2: " + DBMODE);
            con = DriverManager.getConnection(url, uname, pword);
            if (con != null) {
                connected = true;
                if (DBMODE == 1) {
                    setMaxPacket();
                }
            }
        } catch (SQLException ex) {
            System.out.println(DBMODE + " " + uname);
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            ex.printStackTrace();
        } finally {
            return connected;
        }
    }

    public void setMaxPacket() {
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SET SESSION max_allowed_packet=100000000";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void disconnect() {
        if (connected != false) {
            try {
                con.close();
                connected = false;
            } catch (Exception e) {
            }
        }
    }

    public void shutdown() {
        try {
            Statement st = con.createStatement();
            st.execute("SHUTDOWN");
            con.close();
            connected = false;
        } catch (SQLException ex) {
        } finally {
        }
    }

    public void clearUp() {
        if (DBMODE == 0) {
            shutdown();
        }
        if (connected) {
            disconnect();
        }
    }

    public String[] showColumns(String table) {
        String[] out = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SHOW COLUMNS FROM " + table;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if (stmt.execute(query)) {
                rs = stmt.getResultSet();
                LinkedList<String> t = new LinkedList<String>();
                while (rs.next()) {
                    String nam = rs.getString(1);
                    t.add(nam);
                }
                out = new String[t.size()];
                for (int i = 0; i < t.size(); i++) {
                    out[i] = t.get(i);
                }
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return out;
    }

    public void writeToDataBase(String query) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                ;
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public LinkedList<String[]> readFromDataBase(String query, int[] whattoget) {
        LinkedList<String[]> store = new LinkedList<String[]>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if (stmt.execute(query)) {
                rs = stmt.getResultSet();
                while (rs.next()) {
                    String[] nam = new String[whattoget.length];
                    for (int i = 0; i < whattoget.length; i++) {
                        nam[i] = rs.getString(whattoget[i]);
                    }
                    store.add(nam);
                }
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return store;
    }

    public int[] readFromDataBase() {
        LinkedList<int[]> store = new LinkedList<int[]>();
        int[] results = new int[1];
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT id FROM songdata";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if (stmt.execute(query)) {
                rs = stmt.getResultSet();
                while (rs.next()) {
                    int[] h = new int[1];
                    h[0] = rs.getInt("id");
                    store.add(h);
                }
            }
            results = new int[store.size()];
            for (int i = 0; i < store.size(); i++) {
                int[] h = store.get(i);
                results[i] = h[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return results;
    }

    public LinkedList<String> populateContentPane(int ID) {
        LinkedList<String> Results = new LinkedList<String>();
        String query = "SELECT name, SpecID, PopID, locdesc, gridtype, gridx, gridy FROM individual WHERE id = " + ID;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Results.add(rs.getString("name"));
                Results.add(rs.getString("locdesc"));
                Results.add(rs.getString("gridtype"));
                Results.add(rs.getString("gridx"));
                Results.add(rs.getString("gridy"));
                Results.add(rs.getString("SpecID"));
                Results.add(rs.getString("PopID"));
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return Results;
    }

    public int readIndividualNameFromDB(String name) {
        int result = -1;
        String query = "SELECT id FROM individual WHERE name = '" + name + "'";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return result;
    }

    public LinkedList<Element> loadElementsFromDatabaseOld(int id, Song song) {
        LinkedList<Element> eleList = new LinkedList<Element>();
        int measureSize = 3;
        int output[][];
        double output2[][];
        String query = "SELECT data, data2, starttime FROM element WHERE songID = " + id;
        oldloader = true;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Element ele = new Element();
                String s = rs.getString("data2");
                LinkedList<double[]> LList2 = new LinkedList<double[]>();
                int ind = 0;
                int ind2 = 0;
                if (s == null) {
                } else {
                    ind2 = s.indexOf(" ", ind);
                    while (ind2 != -1) {
                        double v[] = new double[measureSize];
                        for (int i = 0; i < measureSize; i++) {
                            String t = s.substring(ind, ind2);
                            ind = ind2 + 1;
                            ind2 = s.indexOf(" ", ind);
                            v[i] = Double.parseDouble(t);
                        }
                        LList2.add(v);
                    }
                }
                output2 = new double[LList2.size()][measureSize];
                for (int i = 0; i < LList2.size(); i++) {
                    double[] a = LList2.get(i);
                    for (int j = 0; j < measureSize; j++) {
                        output2[i][j] = a[j];
                    }
                }
                ele.measurements = output2;
                s = rs.getString("data");
                int Le = rs.getInt("starttime");
                LinkedList<int[]> LList = new LinkedList<int[]>();
                ind = 0;
                ind2 = 0;
                ind2 = s.indexOf(" ", ind);
                while (ind2 != -1) {
                    String t = s.substring(ind, ind2);
                    ind = ind2 + 1;
                    ind2 = s.indexOf(" ", ind);
                    int v = Integer.parseInt(t);
                    int[] out = new int[1 + v];
                    out[0] = Le;
                    for (int i = 0; i < v; i++) {
                        String u = s.substring(ind, ind2);
                        out[i + 1] = Integer.parseInt(u);
                        ind = ind2 + 1;
                        ind2 = s.indexOf(" ", ind);
                    }
                    LList.add(out);
                    Le++;
                }
                output = new int[LList.size()][];
                for (int i = 0; i < LList.size(); i++) {
                    int[] a = LList.get(i);
                    output[i] = new int[a.length];
                    for (int j = 0; j < a.length; j++) {
                        output[i][j] = a[j];
                    }
                }
                ele.signal = output;
                ele.update(song);
                eleList.add(ele);
            }
        } catch (Exception e) {
            System.out.println("Error importing old data");
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return eleList;
    }

    public LinkedList<Element> loadElementsFromDatabase(int id, Song song) {
        LinkedList<Element> eleList = new LinkedList<Element>();
        oldloader = false;
        String query = "SELECT signal, peakfreq, meanfreq, medianfreq, fundfreq, peakfreqchange, meanfreqchange, medianfreqchange, fundfreqchange, harmonicity, wiener, bandwidth, amplitude, reverberation, trillamp, trillrate, powerspectrum, songID, id, overallpeakfreq1, overallpeakfreq2, starttime, timelength, gapbefore, gapafter, timestep, framelength, dy, maxf, windowmethod, echocomp, echorange, dyncomp, dynrange FROM element WHERE songID = " + id;
        String[] fields = { "peakfreq", "meanfreq", "medianfreq", "fundfreq", "peakfreqchange", "meanfreqchange", "medianfreqchange", "fundfreqchange", "harmonicity", "wiener", "bandwidth", "amplitude", "reverberation", "trillamp", "trillrate" };
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String s;
        int ind, ind2;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Element ele = new Element();
                s = rs.getString("signal");
                int Le = rs.getInt("starttime");
                LinkedList<int[]> LList = new LinkedList<int[]>();
                ind = 0;
                ind2 = 0;
                ind2 = s.indexOf(" ", ind);
                while (ind2 != -1) {
                    String t = s.substring(ind, ind2);
                    ind = ind2 + 1;
                    ind2 = s.indexOf(" ", ind);
                    int v = Integer.parseInt(t);
                    int[] out = new int[v];
                    int q = 0;
                    if (v % 2 == 0) {
                        out = new int[1 + v];
                        out[q] = Le;
                        q++;
                    }
                    for (int i = 0; i < v; i++) {
                        String u = s.substring(ind, ind2);
                        out[q] = Integer.parseInt(u);
                        q++;
                        ind = ind2 + 1;
                        ind2 = s.indexOf(" ", ind);
                    }
                    LList.add(out);
                    Le++;
                }
                ele.signal = new int[LList.size()][];
                for (int i = 0; i < LList.size(); i++) {
                    int[] a = LList.get(i);
                    ele.signal[i] = new int[a.length];
                    for (int j = 0; j < a.length; j++) {
                        ele.signal[i][j] = a[j];
                    }
                }
                ele.length = ele.signal.length;
                ele.measurements = new double[ele.length + 5][15];
                try {
                    for (int i = 0; i < 15; i++) {
                        s = rs.getString(fields[i]);
                        ind = 0;
                        ind2 = 0;
                        int count = 0;
                        if (s != null) {
                            ind2 = s.indexOf(" ", ind);
                            while (ind2 != -1) {
                                String t = s.substring(ind, ind2);
                                ind = ind2 + 1;
                                ind2 = s.indexOf(" ", ind);
                                ele.measurements[count][i] = Double.parseDouble(t);
                                count++;
                            }
                        }
                    }
                } catch (Exception e) {
                    for (int i = 0; i < 13; i++) {
                        s = rs.getString(fields[i]);
                        ind = 0;
                        ind2 = 0;
                        int count = 0;
                        if (s != null) {
                            ind2 = s.indexOf(" ", ind);
                            while (ind2 != -1) {
                                String t = s.substring(ind, ind2);
                                ind = ind2 + 1;
                                ind2 = s.indexOf(" ", ind);
                                ele.measurements[count][i] = Double.parseDouble(t);
                                count++;
                            }
                        }
                    }
                }
                ele.timeStep = rs.getDouble("timestep");
                ele.frameLength = rs.getDouble("framelength");
                ele.maxf = rs.getInt("maxf");
                ele.windowMethod = rs.getInt("windowmethod");
                ele.dynEqual = rs.getDouble("dynrange");
                ;
                ele.dynRange = rs.getDouble("dyncomp");
                ele.echoRange = rs.getInt("echorange");
                ele.echoComp = rs.getDouble("echocomp");
                ele.dy = rs.getDouble("dy");
                ele.timeBefore = rs.getFloat("gapbefore");
                ele.timeAfter = rs.getFloat("gapafter");
                ele.begintime = rs.getInt("starttime");
                ele.timelength = rs.getFloat("timelength");
                ele.overallPeak1 = rs.getFloat("overallpeakfreq1");
                ele.overallPeak2 = rs.getFloat("overallpeakfreq2");
                ele.id = rs.getInt("id");
                int ny = (int) Math.floor(ele.maxf / ele.dy);
                ele.powerSpectrum = new double[ny];
                s = rs.getString("powerspectrum");
                ind2 = 0;
                ind = 0;
                int co = 0;
                if (s != null) {
                    ind2 = s.indexOf(" ", ind);
                    while (ind2 != -1) {
                        String t = s.substring(ind, ind2);
                        ele.powerSpectrum[co] = Double.parseDouble(t);
                        ind = ind2 + 1;
                        ind2 = s.indexOf(" ", ind);
                        co++;
                    }
                }
                eleList.add(ele);
            }
        } catch (Exception e) {
            e.printStackTrace();
            eleList = loadElementsFromDatabaseOld(id, song);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return eleList;
    }

    public void updateDB() {
        Statement stmt = null;
        ResultSet rs = null;
        String queryc = "ALTER TABLE songdata ADD COLUMN (echocomp float, echorange int, dyncomp float, dynrange float)";
        String queryb = "ALTER TABLE element ADD COLUMN (signal mediumtext, peakfreq mediumtext, fundfreq mediumtext, meanfreq mediumtext, medianfreq mediumtext, peakfreqchange mediumtext, fundfreqchange mediumtext, meanfreqchange mediumtext, medianfreqchange mediumtext, harmonicity mediumtext, wiener mediumtext, bandwidth mediumtext, amplitude mediumtext, reverberation mediumtext)";
        String queryd = "UPDATE songdata SET ";
        String names2[] = { "echocomp", "echorange", "dyncomp", "dynrange" };
        int vals[] = { 0, 50, 65, 0 };
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(queryc);
            for (int i = 0; i < names2.length; i++) {
                stmt = con.createStatement();
                stmt.executeUpdate(queryd + names2[i] + " = " + vals[i]);
            }
            stmt = con.createStatement();
            stmt.executeUpdate(queryb);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("oops");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void updateDB3() {
        Statement stmt = null;
        ResultSet rs = null;
        String queryb = "ALTER TABLE element ADD COLUMN (trillrate mediumtext, trillamp mediumtext)";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(queryb);
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void updateDB2() {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String wavindex1 = "OPTIMIZE TABLE wavs";
            stmt = con.createStatement();
            stmt.executeUpdate(wavindex1);
            String songindex1 = "OPTIMIZE TABLE songdata";
            stmt = con.createStatement();
            stmt.executeUpdate(songindex1);
            String eleindex1 = "OPTIMIZE TABLE element";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex1);
            String wavindex = "ALTER TABLE wavs ADD INDEX (songid)";
            stmt = con.createStatement();
            stmt.executeUpdate(wavindex);
            String songindex = "ALTER TABLE songdata ADD INDEX (IndividualID)";
            stmt = con.createStatement();
            stmt.executeUpdate(songindex);
            String eleindex = "ALTER TABLE element ADD INDEX (SongID)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void updateDB4() {
        Statement stmt = null;
        ResultSet rs = null;
        String s = dbase;
        try {
            String queryb = "CREATE TABLE " + s + ".";
            String comp2 = "comparesong (user CHAR(50), song1 INT, song2 INT, score FLOAT, max_score FLOAT, scheme_id INT)";
            stmt = con.createStatement();
            stmt.executeUpdate(queryb + comp2);
            String comp3 = "comparesyll (user CHAR(50), song1 INT, song2 INT, syll1 INT, syll2 INT, score FLOAT, max_score FLOAT, scheme_id INT)";
            stmt = con.createStatement();
            stmt.executeUpdate(queryb + comp3);
            String comp4 = "compareele (user CHAR(50), song1 INT, song2 INT, syll1 INT, syll2 INT, ele1 INT, ele2 INT, score FLOAT, max_score FLOAT, scheme_id INT)";
            stmt = con.createStatement();
            stmt.executeUpdate(queryb + comp4);
            String eleindex = "ALTER TABLE " + s + ".comparesong ADD INDEX (song1)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesong ADD INDEX (song2)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesong ADD INDEX (scheme_id)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesyll ADD INDEX (song1)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesyll ADD INDEX (song2)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesyll ADD INDEX (syll1)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesyll ADD INDEX (syll2)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesyll ADD INDEX (scheme_id)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".compareele ADD INDEX (song1)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".compareele ADD INDEX (song2)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".compareele ADD INDEX (syll1)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".compareele ADD INDEX (syll2)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".compareele ADD INDEX (scheme_id)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void updateDB5() {
        Statement stmt = null;
        ResultSet rs = null;
        String s = dbase;
        try {
            String queryb = "CREATE TABLE " + s + ".";
            String comp5 = "comparesongcomp (song1 INT, song2 INT, score BLOB)";
            stmt = con.createStatement();
            stmt.executeUpdate(queryb + comp5);
            String eleindex = "ALTER TABLE " + s + ".comparesongcomp ADD INDEX (song1)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesongcomp ADD INDEX (song2)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void updateDB6() {
        resetCompTable();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String query = "ALTER TABLE comparesongcomp max_rows = 20000000000 avg_row_length = 50";
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void updateDB7() {
        resetCompTable();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String query = "ALTER TABLE element ADD COLUMN (eform mediumtext)";
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void updateDB8() {
        Statement stmt = null;
        ResultSet rs = null;
        String s = dbase;
        try {
            String eleindex = "ALTER TABLE " + s + ".comparesongcomp ADD INDEX (song1)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
            eleindex = "ALTER TABLE " + s + ".comparesongcomp ADD INDEX (song2)";
            stmt = con.createStatement();
            stmt.executeUpdate(eleindex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public LinkedList<int[]> loadSyllablesFromDatabase(int id) {
        LinkedList<int[]> outList = new LinkedList<int[]>();
        String query = "SELECT starttime, endtime FROM syllable WHERE songID = " + id;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int e = rs.getInt("endtime");
                int s = rs.getInt("starttime");
                int[] t = { s, e };
                outList.add(t);
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return outList;
    }

    public LinkedList<float[]> loadParametersFromDatabase(int id) {
        LinkedList<float[]> output = new LinkedList<float[]>();
        String query = "SELECT starttime, maxfreq, minfreq, startfreq, endfreq, avfreq, maxpeakfreq, minpeakfreq, maxfundfreq, minfundfreq, startfundfreq, endfundfreq, avfundfreq, overallpeakfreq, peakfreq, timelength, gapbefore, gapafter, timemax, timemin, timemaxfund, timeminfund, maxband, minband, startband, endband, avband FROM element WHERE songid = " + id;
        int params = 27;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                float out[] = new float[params];
                for (int i = 0; i < params; i++) {
                    out[i] = rs.getFloat(i + 1);
                }
                output.add(out);
            }
        } catch (Exception e) {
            System.out.println("not loaded");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return output;
    }

    public Song loadSongFromDatabase(int id, int info) {
        Song song = new Song();
        String query = "SELECT songid, filename, wav, samplerate, framesize, stereo, bigend, ssizeinbits, time, signed FROM wavs WHERE songid = " + id;
        String query2 = "SELECT name, echocomp, echorange, dyncomp, dynrange, maxfreq, framelength, timestep, filtercutoff, windowmethod, dx, dy, IndividualID FROM songdata WHERE id = " + id;
        String query3 = "SELECT call_location FROM songdata WHERE id = " + id;
        String query4 = "SELECT call_context FROM songdata WHERE id = " + id;
        String query5 = "SELECT RecordingEquipment FROM songdata WHERE id = " + id;
        String query6 = "SELECT Recorder FROM songdata WHERE id = " + id;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            song.loaded = true;
            if (info != 1) {
                stmt = con.prepareStatement(query);
                rs = stmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("No such file stored. " + id);
                    song.loaded = false;
                } else {
                    int fs = rs.getInt("framesize");
                    if (fs > 0) {
                        song.frameSize = fs;
                    }
                    int stereo = rs.getInt("stereo");
                    if (stereo == 0) {
                        stereo = 1;
                    }
                    song.stereo = stereo;
                    song.songID = rs.getInt("songid");
                    song.sampleRate = rs.getDouble("samplerate");
                    Blob b = rs.getBlob(3);
                    song.rawData = b.getBytes(1L, (int) b.length());
                    song.name = rs.getString("filename");
                    int bigend = rs.getInt("bigend");
                    if (bigend == 0) {
                        song.bigEnd = false;
                    } else {
                        song.bigEnd = true;
                    }
                    song.ssizeInBits = rs.getInt("ssizeinbits");
                    song.tDate = rs.getLong("time");
                    int signed = rs.getInt("signed");
                    if (signed == 0) {
                        song.signed = false;
                    } else {
                        song.signed = true;
                    }
                    System.out.println(song.rawData.length + " " + song.frameSize);
                    if (song.frameSize == 0) {
                        song.frameSize++;
                    }
                    song.overallSize = (song.rawData.length / song.frameSize);
                }
            }
            stmt = con.prepareStatement(query2);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No such file stored. " + id);
            } else {
                song.songID = id;
                song.name = rs.getString("name");
                song.maxf = rs.getInt("maxfreq");
                song.frameLength = rs.getDouble("framelength");
                song.timeStep = rs.getDouble("timestep");
                song.frequencyCutOff = rs.getDouble("filtercutoff");
                song.windowMethod = rs.getInt("windowmethod");
                song.echoComp = rs.getDouble("echocomp");
                song.echoRange = rs.getInt("echorange");
                song.dynRange = rs.getDouble("dyncomp");
                song.dynEqual = rs.getInt("dynrange");
                song.dx = rs.getDouble("dx");
                song.dy = rs.getDouble("dy");
                song.individualID = rs.getInt("IndividualID");
            }
            stmt = con.prepareStatement(query3);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No such file stored. " + id);
            } else {
                song.location = rs.getString("call_location");
            }
            stmt = con.prepareStatement(query4);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No such file stored. " + id);
            } else {
                song.notes = rs.getString("call_context");
            }
            stmt = con.prepareStatement(query5);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No such file stored. " + id);
            } else {
                song.recordEquipment = rs.getString("RecordingEquipment");
            }
            stmt = con.prepareStatement(query6);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No such file stored. " + id);
            } else {
                song.recordist = rs.getString("Recorder");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return song;
    }

    public int readSongID(String name, int ID) {
        int result = -1;
        String query = "SELECT id FROM songdata WHERE name = '" + name + "' AND IndividualID = " + ID;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt("id");
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return result;
    }

    public void writeSongIntoDatabase(String name, int p, File f) {
        String insertStmt = "INSERT INTO wavs (songid, filename, wav, samplerate, framesize, stereo, bigend, ssizeinbits, time, signed) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            long modtime = f.lastModified();
            AudioInputStream AFStreamA = AudioSystem.getAudioInputStream(f);
            AudioFormat afFormat = AFStreamA.getFormat();
            if (afFormat.isBigEndian()) {
                AudioFormat targetFormat = new AudioFormat(afFormat.getEncoding(), afFormat.getSampleRate(), afFormat.getSampleSizeInBits(), afFormat.getChannels(), afFormat.getFrameSize(), afFormat.getFrameRate(), false);
                afFormat = targetFormat;
            }
            if (afFormat.getEncoding().toString().startsWith("MPEG")) {
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, afFormat.getSampleRate(), 16, afFormat.getChannels(), afFormat.getChannels() * 2, afFormat.getSampleRate(), false);
                afFormat = targetFormat;
            }
            AudioInputStream AFStream = AudioSystem.getAudioInputStream(afFormat, AFStreamA);
            double samplerate = AFStream.getFormat().getSampleRate();
            int stereo = AFStream.getFormat().getChannels();
            int FrameSize = AFStream.getFormat().getFrameSize();
            int length = (int) (FrameSize * AFStream.getFrameLength());
            int bigend = 0;
            if (AFStream.getFormat().isBigEndian()) {
                bigend = 1;
            }
            AudioFormat.Encoding afe = AFStream.getFormat().getEncoding();
            int signed = 0;
            if (afe.toString().startsWith("PCM_SIGNED")) {
                signed = 1;
            }
            int ssizebits = AFStream.getFormat().getSampleSizeInBits();
            System.out.println(samplerate + " " + stereo + " " + FrameSize + " " + length + " " + afe.toString());
            stmt = con.prepareStatement(insertStmt);
            stmt.setString(2, name);
            stmt.setInt(1, p);
            stmt.setBinaryStream(3, AFStream, length);
            stmt.setDouble(4, samplerate);
            stmt.setInt(5, FrameSize);
            stmt.setInt(6, stereo);
            stmt.setInt(7, bigend);
            stmt.setInt(8, ssizebits);
            stmt.setLong(9, modtime);
            stmt.setInt(10, signed);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void writeSongMeasurements(Song song) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String u = "DELETE FROM element WHERE songID=" + song.songID;
            stmt = con.createStatement();
            stmt.executeUpdate(u);
            String t = "INSERT INTO element (signal, peakfreq, meanfreq, medianfreq, fundfreq, peakfreqchange, meanfreqchange, medianfreqchange, fundfreqchange, harmonicity, wiener, bandwidth, amplitude, reverberation, trillamp, trillrate, powerspectrum, songID, overallpeakfreq1, overallpeakfreq2, starttime, timelength, gapbefore, gapafter, timestep, framelength, dy, maxf, echocomp, echorange, dyncomp, dynrange) VALUES (";
            String b = " , ";
            song.sortSyllsEles();
            for (int i = 0; i < song.eleList.size(); i++) {
                Element ele = (Element) song.eleList.get(i);
                StringBuffer bus = new StringBuffer();
                for (int j = 0; j < ele.length; j++) {
                    bus.append(ele.signal[j].length);
                    bus.append(" ");
                    for (int k = 0; k < ele.signal[j].length; k++) {
                        bus.append(ele.signal[j][k]);
                        bus.append(" ");
                    }
                }
                StringBuffer bu = new StringBuffer();
                for (int j = 0; j < 15; j++) {
                    bu.append("'");
                    for (int k = 0; k < ele.measurements.length; k++) {
                        bu.append(ele.measurements[k][j]);
                        bu.append(" ");
                    }
                    bu.append("' , ");
                }
                StringBuffer bup = new StringBuffer();
                for (int j = 0; j < ele.powerSpectrum.length; j++) {
                    bup.append(ele.powerSpectrum[j]);
                    bup.append(" ");
                }
                String e = bus.toString();
                String f = bup.toString();
                String g = bu.toString();
                String v = b + song.songID + b + ele.overallPeak1 + b + ele.overallPeak2 + b + ele.begintime + b + ele.timelength + b + ele.timeBefore + b + ele.timeAfter + b + ele.timeStep + b + ele.frameLength + b + ele.dy + b + ele.maxf + b + ele.echoComp + b + ele.echoRange + b + ele.dynRange + b + ele.dynEqual + ")";
                v = t + "'" + e + "'" + b + g + "'" + f + "'" + v;
                stmt = con.createStatement();
                stmt.executeUpdate(v);
            }
            String su = "DELETE FROM syllable WHERE songID=" + song.songID;
            stmt = con.createStatement();
            stmt.executeUpdate(su);
            String sv = "INSERT INTO syllable (songID, starttime, endtime)VALUES (";
            for (int i = 0; i < song.syllList.size(); i++) {
                int[] ele = (int[]) song.syllList.get(i);
                stmt = con.createStatement();
                stmt.executeUpdate(sv + song.songID + b + ele[0] + b + ele[1] + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void writeSongInfo(Song song) {
        String b = " , ";
        String w = " WHERE id=";
        int sa = (int) song.sampleRate;
        String s1 = "UPDATE songdata SET ";
        String details = s1 + "echocomp=" + song.echoComp + b + "echorange=" + song.echoRange + b + "dyncomp=" + song.dynRange + b + "dynrange=" + song.dynEqual + b + "maxfreq=" + song.maxf + b + "framelength=" + song.frameLength + b + "timestep=" + song.timeStep + b + "filtercutoff=" + song.frequencyCutOff + b + "windowmethod=" + song.windowMethod + b + "dx=" + song.dx + b + "dy=" + song.dy + b + "samplerate=" + sa + w + song.songID;
        writeToDataBase(details);
        String query = "UPDATE wavs SET time='" + song.tDate + "' WHERE songid=" + song.songID;
        writeToDataBase(query);
        query = "UPDATE songdata SET call_location='" + song.location + "' WHERE id=" + song.songID;
        writeToDataBase(query);
        query = "UPDATE songdata SET call_context='" + song.notes + "' WHERE id=" + song.songID;
        writeToDataBase(query);
        query = "UPDATE songdata SET RecordingEquipment='" + song.recordEquipment + "' WHERE id=" + song.songID;
        writeToDataBase(query);
        query = "UPDATE songdata SET Recorder='" + song.recordist + "' WHERE id=" + song.songID;
        writeToDataBase(query);
        query = "UPDATE songdata SET Name='" + song.name + "' WHERE id=" + song.songID;
        writeToDataBase(query);
    }

    public int writeIndividualIntoDatabase(String[] details) {
        String insertStmt = "INSERT INTO individual (name, locdesc, gridtype, gridx, gridy, SpecID, PopID) values(?, ?, ?, ?, ?, ?, ?)";
        String query = "SELECT id FROM individual WHERE name = '" + details[0] + "'";
        int id = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String[] d = new String[8];
        for (int i = 0; i < 8; i++) {
            d[i] = " ";
        }
        int q = 8;
        if (details.length < 8) {
            q = details.length;
        }
        for (int i = 0; i < q; i++) {
            d[i] = details[i];
        }
        try {
            stmt = con.prepareStatement(insertStmt);
            for (int i = 1; i < 8; i++) {
                stmt.setString(i, d[i - 1]);
            }
            stmt.executeUpdate();
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println("failed");
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return id;
    }

    public void writeElementComparison(int song1, int ele1, int song2, int ele2, float score) {
        String insertStmt = "INSERT INTO compareele (song1, song2, ele1, ele2, score) values(?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(insertStmt);
            stmt.setInt(1, song1);
            stmt.setInt(2, song2);
            stmt.setInt(3, ele1);
            stmt.setInt(4, ele2);
            stmt.setFloat(5, score);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("failed");
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void resetCompTable() {
        String truncateStmt = "DELETE FROM comparesongcomp";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(truncateStmt);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("failed");
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public void writeCompSongComparison(int song1, int song2, float[][] score) {
        String insertStmt = "INSERT INTO comparesongcomp (song1, song2, score) values(?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            int length = score[0].length * score.length * 4;
            byte[] buf = new byte[length];
            int a = 0;
            for (int i = 0; i < score.length; i++) {
                for (int j = 0; j < score[i].length; j++) {
                    ByteBuffer bb;
                    bb = ByteBuffer.allocate(4);
                    bb.putFloat(0, score[i][j]);
                    for (int k = 0; k < 4; k++) {
                        buf[a] = bb.get(k);
                        a++;
                    }
                }
            }
            ByteArrayInputStream bstream = new ByteArrayInputStream(buf);
            stmt = con.prepareStatement(insertStmt);
            stmt.setInt(1, song1);
            stmt.setInt(2, song2);
            stmt.setBinaryStream(3, bstream, length);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public float[][][] getSongComp(int a, int b, int[] c) {
        float[][][] results = new float[c.length][][];
        String query1 = "SELECT song2, score FROM comparesongcomp WHERE song1 = ";
        String query2 = "SELECT song1, score FROM comparesongcomp WHERE song2 = ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = query1 + a;
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int e = rs.getInt(1);
                for (int w = 0; w < c.length; w++) {
                    if (c[w] == e) {
                        Blob blob = rs.getBlob(2);
                        byte[] buf = blob.getBytes(1L, (int) blob.length());
                        int d = buf.length / 4;
                        d /= b;
                        results[w] = new float[b][d];
                        int x = 0;
                        for (int j = 0; j < b; j++) {
                            for (int k = 0; k < d; k++) {
                                ByteBuffer bb;
                                bb = ByteBuffer.allocate(4);
                                bb.put(buf, x, 4);
                                x += 4;
                                results[w][j][k] = bb.getFloat(0);
                            }
                        }
                        w = c.length;
                    }
                }
            }
            query = query2 + a;
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int e = rs.getInt(1);
                for (int w = 0; w < c.length; w++) {
                    if (c[w] == e) {
                        Blob blob = rs.getBlob(2);
                        byte[] buf = blob.getBytes(1L, (int) blob.length());
                        int d = buf.length / 4;
                        d /= b;
                        results[w] = new float[b][d];
                        int x = 0;
                        for (int j = 0; j < d; j++) {
                            for (int k = 0; k < b; k++) {
                                ByteBuffer bb;
                                bb = ByteBuffer.allocate(4);
                                bb.put(buf, x, 4);
                                x += 4;
                                results[w][k][j] = bb.getFloat(0);
                            }
                        }
                        w = c.length;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return results;
    }

    public float[][][] getSongComp2(int a, int b, int[] c) {
        float[][][] results = new float[c.length][][];
        String query1 = "SELECT song1, song2, score FROM comparesongcomp WHERE song1 = ";
        String query2 = " OR song2 = ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = query1 + a + query2 + b;
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int e = rs.getInt(1);
                int f = rs.getInt(2);
                if (e == a) {
                    for (int w = 0; w < c.length; w++) {
                        if (c[w] == f) {
                            Blob blob = rs.getBlob(3);
                            byte[] buf = blob.getBytes(1L, (int) blob.length());
                            int d = buf.length / 4;
                            d /= b;
                            results[w] = new float[b][d];
                            int x = 0;
                            for (int j = 0; j < b; j++) {
                                for (int k = 0; k < d; k++) {
                                    ByteBuffer bb;
                                    bb = ByteBuffer.allocate(4);
                                    bb.put(buf, x, 4);
                                    x += 4;
                                    results[w][j][k] = bb.getFloat(0);
                                }
                            }
                            w = c.length;
                        }
                    }
                }
                if (f == a) {
                    for (int w = 0; w < c.length; w++) {
                        if (c[w] == e) {
                            Blob blob = rs.getBlob(3);
                            byte[] buf = blob.getBytes(1L, (int) blob.length());
                            int d = buf.length / 4;
                            d /= b;
                            results[w] = new float[b][d];
                            int x = 0;
                            for (int j = 0; j < d; j++) {
                                for (int k = 0; k < b; k++) {
                                    ByteBuffer bb;
                                    bb = ByteBuffer.allocate(4);
                                    bb.put(buf, x, 4);
                                    x += 4;
                                    results[w][k][j] = bb.getFloat(0);
                                }
                            }
                            w = c.length;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return results;
    }

    public float[][] getSongComp(int a, int b, int c, int d) {
        float[][] results = new float[1][1];
        String query1 = "SELECT score FROM comparesongcomp WHERE song1 = ";
        String query2 = " AND song2 = ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean found = false;
        try {
            String query = query1 + a + query2 + b;
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                found = true;
                Blob blob = rs.getBlob(1);
                byte[] buf = blob.getBytes(1L, (int) blob.length());
                results = new float[c][d];
                if (c * d * 4 != buf.length) {
                    System.out.println("MISMATCHING SIZES!");
                } else {
                    int aa = 0;
                    for (int j = 0; j < c; j++) {
                        for (int k = 0; k < d; k++) {
                            ByteBuffer bb;
                            bb = ByteBuffer.allocate(4);
                            bb.put(buf, aa, 4);
                            aa += 4;
                            results[j][k] = bb.getFloat(0);
                        }
                    }
                }
            }
            if (!found) {
                query = query1 + b + query2 + a;
                stmt = con.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    Blob blob = rs.getBlob(1);
                    byte[] buf = blob.getBytes(1L, (int) blob.length());
                    results = new float[c][d];
                    if (c * d * 4 != buf.length) {
                        System.out.println("MISMATCHING SIZES!");
                    } else {
                        int aa = 0;
                        for (int j = 0; j < c; j++) {
                            for (int k = 0; k < d; k++) {
                                ByteBuffer bb;
                                bb = ByteBuffer.allocate(4);
                                bb.put(buf, aa, 4);
                                aa += 4;
                                results[j][k] = bb.getFloat(0);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return results;
    }

    public void writeEleForm(float[] results, int id) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            StringBuffer bu = new StringBuffer();
            bu.append(results.length);
            for (int i = 0; i < results.length; i++) {
                bu.append(" ");
                bu.append(results[i]);
            }
            String e = bu.toString();
            String query = "UPDATE element SET eform='" + e + "' WHERE id=" + id;
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }

    public LinkedList loadSchemes(boolean complex) {
        LinkedList poss = new LinkedList();
        String query = "SELECT id, name, song1, song2, max_score, syll_comp, song_comp FROM comparescheme";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                int[] songs = new int[6];
                songs[0] = rs.getInt("song1");
                songs[1] = rs.getInt("song2");
                songs[2] = rs.getInt("id");
                songs[3] = rs.getInt("max_score");
                if (((songs[1] == -1) && (!complex)) || ((complex) && (songs[1] > -1))) {
                    songs[4] = rs.getInt("syll_comp");
                    songs[5] = rs.getInt("song_comp");
                    boolean found = false;
                    for (int i = 0; i < poss.size(); i++) {
                        LinkedList t = (LinkedList) poss.get(i);
                        String s = (String) t.get(0);
                        if (name.equals(s)) {
                            found = true;
                            t.add(songs);
                            poss.remove(i);
                            poss.add(i, t);
                        }
                    }
                    if (!found) {
                        LinkedList<Object> t = new LinkedList<Object>();
                        t.add(name);
                        t.add(songs);
                        poss.add(t);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return poss;
    }

    public boolean testScheme(String name) {
        boolean result = false;
        String query = "SELECT 1 FROM comparescheme WHERE name ='" + name + "'";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = true;
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return result;
    }

    public LinkedList<Object> getOutputVisual(int id) {
        LinkedList<Object> outd = new LinkedList<Object>();
        String query1 = "SELECT user, song1, song2, syll1, syll2, score, max_score FROM comparesyll WHERE scheme_id=" + id;
        String query2 = "SELECT user, song1, song2, score, max_score FROM comparesong WHERE scheme_id=" + id;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(query1);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String user = rs.getString("user");
                int[] data = new int[6];
                data[0] = rs.getInt("song1");
                data[1] = rs.getInt("song2");
                data[2] = rs.getInt("syll1");
                data[3] = rs.getInt("syll2");
                data[4] = rs.getInt("score");
                data[5] = rs.getInt("max_score");
                outd.add(user);
                outd.add(data);
            }
            stmt = con.prepareStatement(query2);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String user = rs.getString("user");
                int[] data = new int[4];
                data[0] = rs.getInt("song1");
                data[1] = rs.getInt("song2");
                data[2] = rs.getInt("score");
                data[3] = rs.getInt("max_score");
                outd.add(user);
                outd.add(data);
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
        return outd;
    }
}
