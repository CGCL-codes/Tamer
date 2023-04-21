package org.foment.joliage3.reports;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import org.foment.joliage3.core.string.SplitString;
import static org.foment.joliage3.core.logging.Logger.log;
import org.foment.joliage3.core.logging.Logger;
import org.foment.joliage3.sql.Database;
import static org.foment.joliage3.core.string.StringIO.*;

/**
 *
 * @author ivan
 */
public class Report {

    public static Report createNewReport(int reportID, HashMap<String, String> parameters) {
        return new Report(reportID, parameters);
    }

    public static Report loadReportFromCache(int reportCacheID) {
        return new Report(reportCacheID);
    }

    private boolean _loadedFromCache;

    private int _id = -1;

    private boolean _valid = false;

    private boolean _loaded = false;

    private String _foFileName, _fieldsFileName;

    private String _foContents, _fieldsContents;

    private String _title;

    private Date _timestamp;

    private Date _date;

    private int _reportCacheID = -1;

    private Paper _paper = null;

    private java.lang.String _description;

    private HashMap<String, String> _parameters;

    private String _cachedParams;

    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public Report(int reportID, HashMap<String, String> parameters) {
        _valid = false;
        try {
            String query = "SELECT * FROM Report WHERE ReportID = '" + reportID + "';";
            ResultSet rs = Database.getConnection().createStatement().executeQuery(query);
            if (!rs.next()) return;
            _foFileName = rs.getString("ReportFOFile");
            if (_foFileName == null || _foFileName.equals("")) return;
            if (getClass().getResource(_foFileName) == null) return;
            _fieldsFileName = rs.getString("ReportFieldsFile");
            if (_fieldsFileName == null || _fieldsFileName.equals("")) {
                _fieldsFileName = "";
            } else if (getClass().getResource(_fieldsFileName) == null) return;
            _title = processVariablesAndCommands(rs.getString("ReportTitle"), parameters);
            _description = processVariablesAndCommands(rs.getString("ReportDescription"), parameters);
            _parameters = parameters;
            _id = reportID;
            _loadedFromCache = false;
            _valid = true;
            _timestamp = new Date();
            _date = null;
            rs.close();
        } catch (SQLException ex) {
        }
    }

    public Report(int reportCacheID) {
        _valid = false;
        try {
            String query = "SELECT * FROM ReportCache WHERE ReportCacheID = '" + reportCacheID + "';";
            ResultSet rs = Database.getConnection().createStatement().executeQuery(query);
            if (!rs.next()) return;
            _foFileName = rs.getString("ReportCacheFOFile");
            if (_foFileName == null || _foFileName.equals("")) return;
            if (!(new java.io.File(_foFileName)).exists()) return;
            _fieldsFileName = rs.getString("ReportCacheFieldsFile");
            if (_fieldsFileName == null || _fieldsFileName.equals("")) {
                _fieldsFileName = "";
            } else if (!(new java.io.File(_fieldsFileName)).exists()) return;
            _title = rs.getString("ReportCacheTitle");
            _description = rs.getString("ReportCacheDescription");
            _cachedParams = rs.getString("ReportCacheFieldValues");
            _parameters = null;
            try {
                _date = rs.getDate("ReportCacheTimestamp");
            } catch (Exception ex) {
                _date = null;
            }
            _id = reportCacheID;
            _loadedFromCache = true;
            _valid = true;
            _timestamp = new Date();
            rs.close();
        } catch (SQLException ex) {
        }
    }

    public Paper getPaper() {
        if (!_valid) return null;
        if (!_loaded) if (!load()) return null;
        return _paper;
    }

    public boolean load() {
        if (!_valid) return false;
        try {
            if (!_loadedFromCache) {
                _foContents = readFromResource(_foFileName);
                _fieldsContents = readFromResource(_fieldsFileName);
            } else {
                _foContents = readFromFile(_foFileName);
                _fieldsContents = readFromFile(_fieldsFileName);
            }
            if (!_fieldsContents.equals("")) {
                _paper = new Paper();
                _paper.loadFromString(_fieldsContents);
                for (String fieldName : _paper.getFieldNames()) {
                    String fieldValue = _paper.getFieldValue(fieldName);
                    fieldValue = processVariables(fieldValue, _parameters);
                    fieldValue = processCommands(fieldValue);
                    _paper.setFieldValue(fieldName, fieldValue);
                }
            }
            if (_loadedFromCache) {
                if (_paper != null) {
                    for (String keyval : _cachedParams.split("&")) {
                        String kv[] = keyval.split("=");
                        _paper.setFieldValue(java.net.URLDecoder.decode(kv[0], "UTF-8"), java.net.URLDecoder.decode(kv[1], "UTF-8"));
                    }
                }
            }
        } catch (IOException ex) {
            _valid = false;
            return false;
        }
        _loaded = true;
        return true;
    }

    public boolean print() {
        return print(true);
    }

    public boolean print(boolean cacheReport) {
        if (!_valid) return false;
        if (!_loaded) if (!load()) return false;
        try {
            String foContentsProcessedOnce = _foContents;
            if (_parameters != null) foContentsProcessedOnce = processVariables(foContentsProcessedOnce, _parameters);
            foContentsProcessedOnce = processCommands(foContentsProcessedOnce);
            String processing = foContentsProcessedOnce;
            if (_paper != null) {
                HashMap<String, String> paperParams = new HashMap();
                for (String fieldName : _paper.getFieldNames()) {
                    paperParams.put(fieldName, toXslFo(_paper.getFieldValue(fieldName)));
                }
                processing = processVariables(processing, paperParams);
            }
            writeAndPrint(processing);
            _reportCacheID = -1;
            if (cacheReport) {
                Database.getTable("ReportCache").pushState();
                ResultSet rs = Database.getConnection().createStatement().executeQuery("SELECT MAX(ReportCacheID) + 1 AS ID FROM ReportCache;");
                if (rs.next()) _reportCacheID = rs.getInt("ID"); else _reportCacheID = 0;
                rs.close();
                Database.getTable("ReportCache").getResultSet().updateInt("ReportCacheID", _reportCacheID);
                String timestamp = (new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(_timestamp);
                Database.getTable("ReportCache").getResultSet().updateString("ReportCacheTimestamp", timestamp);
                Database.getTable("ReportCache").getResultSet().updateString("ReportCacheTitle", _title);
                Database.getTable("ReportCache").getResultSet().updateString("ReportCacheDescription", _description);
                if (_loadedFromCache) {
                    Database.getTable("ReportCache").getResultSet().updateString("ReportCacheFOFile", _foFileName);
                    Database.getTable("ReportCache").getResultSet().updateString("ReportCacheFieldsFile", _fieldsFileName);
                } else {
                    String foCacheFileName = getCachePath() + (new java.io.File(_foFileName)).getName() + "." + _reportCacheID;
                    writeToFile(foCacheFileName, foContentsProcessedOnce);
                    String fieldsCacheFileName = "";
                    if (!_fieldsFileName.equals("")) {
                        fieldsCacheFileName = getCachePath() + (new java.io.File(_fieldsFileName)).getName() + "." + _reportCacheID;
                        writeToFile(fieldsCacheFileName, _fieldsContents);
                    }
                    Database.getTable("ReportCache").getResultSet().updateString("ReportCacheFOFile", foCacheFileName);
                    Database.getTable("ReportCache").getResultSet().updateString("ReportCacheFieldsFile", fieldsCacheFileName);
                }
                String encodedFields = "";
                if (_paper != null) for (String fieldName : _paper.getFieldNames()) {
                    if (!encodedFields.equals("")) encodedFields += "&";
                    encodedFields += java.net.URLEncoder.encode(fieldName, "UTF-8") + "=";
                    encodedFields += java.net.URLEncoder.encode(_paper.getFieldValue(fieldName), "UTF-8");
                }
                Database.getTable("ReportCache").getResultSet().updateString("ReportCacheFieldValues", encodedFields);
                Database.getTable("ReportCache").insertRow();
                Database.getTable("ReportCache").popState();
            }
        } catch (java.io.IOException e) {
            log("joliage3", "org.foment.joliage3.reports.Report", Logger.DEBUG, "Error manipulating the files", e);
        } catch (Exception e) {
            log("joliage3", "org.foment.joliage3.reports.Report", Logger.DEBUG, "Unknown error", e);
        }
        return true;
    }

    public int getReportCacheID() {
        return _reportCacheID;
    }

    public String toString() {
        return "";
    }

    private void writeAndPrint(String fileContents) {
        (new WriterThread(fileContents)).start();
    }

    private class WriterThread extends Thread {

        private String _file, _fileContents, _exec;

        public WriterThread(String contents) {
            super();
            _fileContents = contents;
        }

        public void run() {
            try {
                String tempPath = org.foment.joliage3.core.system.Paths.getApplicationHomePath("/temp/");
                java.io.File destDir = new java.io.File(tempPath);
                destDir.mkdirs();
                java.io.File destFile = java.io.File.createTempFile("tmp", ".fo", destDir);
                writeToFile(destFile, _fileContents);
                try {
                    java.util.ResourceBundle bundle;
                    try {
                        bundle = java.util.ResourceBundle.getBundle("config.fop");
                    } catch (java.util.MissingResourceException e) {
                        log("joliage2", "org.foment.joliage3.reports.Report.WriterThread", Logger.FATAL_ERROR, "Missing configuration file for FOP", e);
                        return;
                    }
                    String exec = bundle.getString("fop.exec");
                    exec = exec.replace("%file", destFile.getAbsolutePath());
                    System.err.println("Executing FOP: " + exec);
                    Process fop = Runtime.getRuntime().exec(exec);
                    java.io.InputStream is = fop.getInputStream();
                    int outch = 0;
                    while ((outch = is.read()) != -1) {
                    }
                    Runtime.getRuntime().gc();
                } catch (java.io.IOException e) {
                    log("joliage2", "org.foment.joliage3.reports.Report.WriterThread", Logger.ERROR, "Error starting FOP", e);
                }
            } catch (java.io.IOException e) {
                log("joliage2", "org.foment.joliage3.reports.Report.WriterThread", Logger.ERROR, "I/O Error", e);
            }
        }
    }

    private String processVariables(String input, HashMap<String, String> variables) {
        if (variables != null) {
            String varName, varVal;
            for (String key : variables.keySet()) {
                input = input.replace(getFullTag("var." + key), variables.get(key));
            }
        }
        return input;
    }

    private String processCommands(String input) {
        SplitString splsMain = new SplitString(input, getOpenTag("sql"), getCloseTag("sql"));
        if (!splsMain.found) return input;
        String result = splsMain.pre;
        String parsing = splsMain.main;
        String pending = splsMain.post;
        SplitString spls = new SplitString(parsing, getOpenTag("sql.query"), getCloseTag("sql.query"));
        if (!spls.found) return input;
        String query = spls.main;
        parsing = spls.pre + spls.post;
        spls = new SplitString(parsing, getOpenTag("sql.echo.rows"), getCloseTag("sql.echo.rows"));
        try {
            ResultSet rs = null;
            for (String qu : query.split("\\n")) {
                if (!qu.trim().equals("")) rs = Database.getConnection().createStatement().executeQuery(qu);
            }
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = 0;
            String main = "";
            while (rs.next()) {
                String rowFmt = spls.main;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    try {
                        String fieldName = rsmd.getColumnName(i);
                        String fieldValue;
                        try {
                            fieldValue = rs.getString(fieldName);
                        } catch (Exception ex) {
                            fieldValue = "";
                        }
                        if (fieldValue == null) fieldValue = "";
                        rowFmt = rowFmt.replace(getFullTag("sql.var." + fieldName), fieldValue);
                    } catch (Exception e) {
                        log("joliage2", "org.foment.joliage3.reports.Report", Logger.ERROR, "SQL Error", e);
                    }
                }
                main += rowFmt;
                count++;
            }
            if (count != 0) parsing = spls.pre + main + spls.post; else parsing = "";
            if (rs != null) rs.close();
        } catch (SQLException e) {
            log("joliage3", "org.foment.joliage3.reports.Report", Logger.ERROR, "SQL Error", e);
        }
        return result + parsing + processCommands(pending);
    }

    private String processVariablesAndCommands(String input, HashMap<String, String> variables) {
        return processCommands(processVariables(input, variables));
    }

    private static String getCachePath() {
        String cachePath = org.foment.joliage3.core.system.Paths.getApplicationHomePath("/cache/");
        (new java.io.File(cachePath)).mkdirs();
        return cachePath;
    }

    public static final String tagNamespace = "joliage3";

    private static String getOpenTag(String name) {
        return "<" + tagNamespace + ":" + name + ">";
    }

    private static String getCloseTag(String name) {
        return "</" + tagNamespace + ":" + name + ">";
    }

    private static String getFullTag(String name) {
        return "<" + tagNamespace + ":" + name + " />";
    }

    private static String toXslFo(String input) {
        String result = "";
        input = input.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
        for (String line : input.split("\n")) {
            if (!line.trim().equals("")) {
                result += "<fo:block>" + line + "</fo:block>";
            } else {
                result += "<fo:block>&#x00a0;</fo:block>";
            }
        }
        return result;
    }

    public static void main(String[] args) throws SQLException {
        javax.swing.JFrame mainframe = new javax.swing.JFrame();
        Report reps[] = new Report[4];
        for (int i = 0; i < reps.length; i++) {
            switch(i) {
                case 0:
                    reps[0] = createNewReport(777, null);
                    break;
                case 1:
                    java.util.HashMap<String, String> rargs = new java.util.HashMap();
                    rargs.put("EmployeeID", "1");
                    reps[1] = createNewReport(1000, rargs);
                    break;
                case 2:
                    reps[2] = loadReportFromCache(0);
                    break;
                case 3:
                    reps[3] = loadReportFromCache(1);
                    break;
            }
            if (reps[i].getPaper() != null) {
                javax.swing.JDialog dlg = new javax.swing.JDialog(mainframe, true);
                dlg.setSize(400, 300);
                dlg.add(reps[i].getPaper());
                dlg.setDefaultCloseOperation(dlg.DISPOSE_ON_CLOSE);
                dlg.setVisible(true);
            }
            reps[i].print();
        }
    }

    public static void initializeReports() {
    }
}
