package jlib.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import jlib.misc.FileSystem;
import jlib.misc.StringUtil;
import jlib.misc.Time_ms;
import jlib.xml.Tag;

/**
 * @author U930DI
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogCenterFile extends LogCenter {

    public LogCenterFile(LogCenterLoader logCenterLoader) {
        super(logCenterLoader);
    }

    public void loadSpecificsEntries(Tag tagLogCenter) {
        m_csFormat = tagLogCenter.getVal("Format");
        String csFileStrategy = tagLogCenter.getVal("FileStrategy");
        String csFilePath = tagLogCenter.getVal("FilePath");
        csFilePath = FileSystem.normalizePath(csFilePath);
        String csFileName = tagLogCenter.getVal("FileName");
        m_csFile = FileSystem.buildFileName(csFilePath, csFileName, null);
        FileSystem.createPath(m_csFile);
        if (csFileStrategy.equalsIgnoreCase("Append")) m_bAppend = true; else if (csFileStrategy.equalsIgnoreCase("BackupOnstart")) {
            m_bAppend = false;
            Tag tagBackup = tagLogCenter.getChild("Backup");
            if (tagBackup != null) {
                String csBackupPath = tagBackup.getVal("BackupPath");
                csBackupPath = FileSystem.normalizePath(csBackupPath);
                if (csBackupPath.length() > 0 && csBackupPath.startsWith(".")) csBackupPath = csFilePath + csBackupPath;
                csBackupPath = FileSystem.normalizePath(csBackupPath);
                FileSystem.createPath(csBackupPath);
                String csBackupFileFormat = tagBackup.getVal("BackupFileFormat");
                csBackupFileFormat = normalizeBackupFileFormat(csBackupFileFormat);
                String csBackupFile = FileSystem.buildFileName(csBackupPath, csBackupFileFormat, null);
                FileSystem.moveOrCopy(m_csFile, csBackupFile);
                int nMaxBackupFileCount = tagBackup.getValAsInt("MaxBackupFileCount");
                if (nMaxBackupFileCount >= 0) FileSystem.keepMoreRecentFile(csBackupPath, nMaxBackupFileCount);
            }
        } else m_bAppend = false;
    }

    private String normalizeBackupFileFormat(String csBackupFileFormat) {
        if (csBackupFileFormat.indexOf("[BackupDateTime]") != -1) {
            String csDateTime = Time_ms.formatYYYYMMDDHHMMSS_ms(Time_ms.getCurrentTime_ms());
            csBackupFileFormat = StringUtil.replace(csBackupFileFormat, "[BackupDateTime]", csDateTime, false);
        }
        return csBackupFileFormat;
    }

    boolean open() {
        try {
            m_printWriter = new PrintWriter(new BufferedWriter(new FileWriter(m_csFile, m_bAppend)));
        } catch (Exception e) {
            System.err.println("Error writing to file");
            return false;
        }
        return true;
    }

    boolean closeLogCenter() {
        m_printWriter.close();
        return true;
    }

    void preSendOutput() {
    }

    void sendOutput(LogParams logParam) {
        if (m_printWriter != null) {
            int nNbLoops = m_patternLayout.getNbLoop(logParam);
            for (int n = 0; n < nNbLoops; n++) {
                String csOut = m_patternLayout.format(logParam, n);
                m_printWriter.print(csOut);
            }
        }
    }

    void postSendOutput() {
        if (m_printWriter != null) m_printWriter.flush();
    }

    String getFormat() {
        return m_csFormat;
    }

    private String m_csFile = null;

    private boolean m_bAppend = false;

    private PrintWriter m_printWriter = null;

    private String m_csFormat = null;

    public String getType() {
        return "LogCenterFile";
    }
}
