package org.drftpd.tools.installer;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author djb61
 * @version $Id: InstallerConfig.java 1887 2008-03-02 16:10:37Z djb61 $
 */
public class InstallerConfig implements Serializable {

    private String _installDir;

    private int _logLevel;

    private boolean _fileLogging;

    private boolean _clean;

    private boolean _convertUsers;

    private boolean _suppressLog;

    private boolean _printTrace;

    private boolean _devMode;

    private HashMap<String, Boolean> _pluginSelections;

    public InstallerConfig() {
    }

    public void setInstallDir(String installDir) {
        _installDir = installDir;
    }

    public void setLogLevel(int logLevel) {
        _logLevel = logLevel;
    }

    public void setFileLogging(boolean fileLogging) {
        _fileLogging = fileLogging;
    }

    public void setClean(boolean clean) {
        _clean = clean;
    }

    public void setConvertUsers(boolean convertUsers) {
        _convertUsers = convertUsers;
    }

    public void setSuppressLog(boolean suppressLog) {
        _suppressLog = suppressLog;
    }

    public void setPrintTrace(boolean printTrace) {
        _printTrace = printTrace;
    }

    public void setDevMode(boolean devMode) {
        _devMode = devMode;
    }

    public void setPluginSelections(HashMap<String, Boolean> pluginSelections) {
        _pluginSelections = pluginSelections;
    }

    public String getInstallDir() {
        return _installDir;
    }

    public int getLogLevel() {
        return _logLevel;
    }

    public boolean getFileLogging() {
        return _fileLogging;
    }

    public boolean getClean() {
        return _clean;
    }

    public boolean getConvertUsers() {
        return _convertUsers;
    }

    public boolean getSuppressLog() {
        return _suppressLog;
    }

    public boolean getPrintTrace() {
        return _printTrace;
    }

    public boolean getDevMode() {
        return _devMode;
    }

    public HashMap<String, Boolean> getPluginSelections() {
        return _pluginSelections;
    }

    public void writeToDisk() throws IOException {
        XMLEncoder out = null;
        try {
            out = new XMLEncoder(new FileOutputStream("build.conf"));
            out.writeObject(this);
        } finally {
            if (out != null) out.close();
        }
    }
}
