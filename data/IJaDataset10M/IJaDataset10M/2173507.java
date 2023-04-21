package org.openXpertya.util;

import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.openXpertya.util.Ini;

/**
 * Descripción de Clase
 *
 *
 * @version 2.2, 21.04.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CLogFile extends Handler {

    /**
     * Descripción de Método
     *
     *
     * @param create
     * @param OXPHome
     *
     * @return
     */
    public static CLogFile get(boolean create, String OXPHome) {
        if ((s_logFile == null) && create) {
            s_logFile = new CLogFile(OXPHome, true);
        }
        return s_logFile;
    }

    /** Descripción de Campos */
    private static CLogFile s_logFile = null;

    /**
     * Constructor de la clase ...
     *
     */
    public CLogFile() {
        this(null, true);
    }

    public CLogFile(String OXPHome, boolean createLogDir) {
        this(OXPHome, createLogDir, false);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param OXPHome
     * @param createLogDir
     * @param isClient
     */
    public CLogFile(String OXPHome, boolean createLogDir, boolean isClient) {
        if (s_logFile == null) {
            s_logFile = this;
        } else {
            reportError("El manejador del fichero ya existe ", new IllegalStateException("Existing Handler"), ErrorManager.GENERIC_FAILURE);
        }
        if ((OXPHome != null) && (OXPHome.length() > 0)) {
            m_OXPHome = OXPHome;
        } else {
            m_OXPHome = Ini.findOXPHome();
        }
        initialize(m_OXPHome, createLogDir, isClient);
    }

    /** Descripción de Campos */
    private String m_OXPHome = null;

    /** Descripción de Campos */
    private boolean m_doneHeader = false;

    /** Descripción de Campos */
    private File m_file = null;

    /** Descripción de Campos */
    private FileWriter m_writer = null;

    /** Descripción de Campos */
    private String m_fileNameDate = "";

    /** Descripción de Campos */
    private int m_records = 0;

    /**
     * Descripción de Método
     *
     *
     * @param fileName
     * @param createLogDir
     */
    private void initialize(String fileName, boolean createLogDir, boolean isClient) {
        if (m_writer != null) {
            close();
        }
        m_doneHeader = false;
        if (!createFile(fileName, createLogDir, isClient)) {
            return;
        }
        try {
            m_writer = new FileWriter(m_file, true);
            m_records = 0;
        } catch (Exception ex) {
            reportError("writer", ex, ErrorManager.OPEN_FAILURE);
            m_writer = null;
        }
        setFormatter(CLogFormatter.get());
        setLevel(Level.SEVERE);
        setFilter(CLogFilter.get());
    }

    /**
     * Descripción de Método
     *
     *
     * @param fileName
     * @param createLogDir
     *
     * @return
     */
    private boolean createFile(String fileName, boolean createLogDir, boolean isClient) {
        try {
            if (fileName != null) {
                File dir = new File(fileName);
                if (!dir.exists() || !dir.isDirectory()) {
                    reportError("Directorio base incorrecto: " + fileName, null, ErrorManager.OPEN_FAILURE);
                    fileName = null;
                }
            }
            if ((fileName != null) && createLogDir) {
                fileName += File.separator + "log";
                File dir = new File(fileName);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                if (!dir.exists() || !dir.isDirectory()) {
                    reportError("Directorio de logs incorrecto: " + fileName, null, ErrorManager.OPEN_FAILURE);
                    fileName = null;
                }
            }
            if (fileName != null) {
                m_fileNameDate = getFileNameDate(System.currentTimeMillis());
                fileName += File.separator + (isClient ? "client_" : "") + "ServidorOXP_" + m_fileNameDate + "_";
                for (int i = 0; i < 1440; i++) {
                    String finalName = fileName + i + ".log";
                    File file = new File(finalName);
                    if (!file.exists()) {
                        m_file = file;
                        break;
                    }
                }
            }
            if (m_file == null) {
                m_file = File.createTempFile("openXpertya", ".log");
            }
        } catch (Exception ex) {
            reportError("fichero", ex, ErrorManager.OPEN_FAILURE);
            m_file = null;
            return false;
        }
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @param time
     *
     * @return
     */
    public static String getFileNameDate(long time) {
        Timestamp ts = new Timestamp(time);
        String s = ts.toString();
        return s.substring(0, 10);
    }

    /**
     * Descripción de Método
     *
     *
     * @param time
     */
    private void rotateLog(long time) {
        if ((m_fileNameDate == null) || m_fileNameDate.equals(getFileNameDate(time))) {
            return;
        }
        rotateLog();
    }

    /**
     * Descripción de Método
     *
     */
    public void rotateLog() {
        initialize(m_OXPHome, true, Ini.isClient());
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getFileName() {
        if (m_file != null) {
            return m_file.getAbsolutePath();
        }
        return "";
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public File getLogDirectory() {
        if (m_file != null) {
            return m_file.getParentFile();
        }
        return null;
    }

    /**
     * Descripción de Método
     *
     *
     * @param newLevel
     *
     * @throws SecurityException
     */
    public synchronized void setLevel(Level newLevel) throws SecurityException {
        if (newLevel == null) {
            return;
        }
        super.setLevel(newLevel);
    }

    /**
     * Descripción de Método
     *
     *
     * @param record
     */
    public void publish(LogRecord record) {
        if (!isLoggable(record) || (m_writer == null)) {
            return;
        }
        rotateLog(record.getMillis());
        String msg = null;
        try {
            msg = getFormatter().format(record);
        } catch (Exception ex) {
            reportError("formateando", ex, ErrorManager.FORMAT_FAILURE);
            return;
        }
        try {
            if (!m_doneHeader) {
                m_writer.write(getFormatter().getHead(this));
                m_doneHeader = true;
            }
            m_writer.write(msg);
            m_records++;
            if ((record.getLevel() == Level.SEVERE) || (record.getLevel() == Level.WARNING) || (m_records % 10 == 0)) {
                flush();
            }
        } catch (Exception ex) {
            reportError("escribiendo", ex, ErrorManager.WRITE_FAILURE);
        }
    }

    /**
     * Descripción de Método
     *
     */
    public void flush() {
        try {
            if (m_writer != null) {
                m_writer.flush();
            }
        } catch (Exception ex) {
            reportError("flush", ex, ErrorManager.FLUSH_FAILURE);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @throws SecurityException
     */
    public void close() throws SecurityException {
        if (m_writer == null) {
            return;
        }
        try {
            if (!m_doneHeader) {
                m_writer.write(getFormatter().getHead(this));
            }
            m_writer.write(getFormatter().getTail(this));
        } catch (Exception ex) {
            reportError("tail", ex, ErrorManager.WRITE_FAILURE);
        }
        flush();
        try {
            m_writer.close();
        } catch (Exception ex) {
            reportError("close", ex, ErrorManager.CLOSE_FAILURE);
        }
        m_writer = null;
        m_file = null;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("CLogFile[");
        sb.append(getFileName()).append(",Level=").append(getLevel()).append("]");
        return sb.toString();
    }
}
