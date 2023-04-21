package gui;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JComponent;
import preferences.Preference;
import soundengine.ButtonHandler;
import soundengine.IPeer;

public abstract class BaseProperties {

    private static final int BYTES_IN_DOUBLE = Double.SIZE / 8;

    private String m_soundFile;

    private String m_intermediateFile;

    private double[] m_intermediateBuf;

    CommonProperties m_commonProperties;

    BaseProperties() {
    }

    BaseProperties(BaseProperties prop) {
        m_soundFile = prop.m_soundFile;
        m_intermediateFile = prop.m_intermediateFile;
        m_intermediateBuf = prop.m_intermediateBuf;
    }

    public abstract IPeer createPeer();

    /**
     * @return Returns the soundFile.
     */
    public String getSoundFile() {
        return m_soundFile;
    }

    /**
     * @param soundFile The soundFile to set.
     */
    public void setSoundFile(String soundFile) {
        m_soundFile = soundFile;
    }

    /**
     * @return Returns the intermediateFile.
     */
    public String getIntermediateFile() {
        return m_intermediateFile;
    }

    /**
     * @param intermediateFile The intermediateFile to set.
     */
    public void setIntermediateFile(String intermediateFile) {
        m_intermediateFile = intermediateFile;
    }

    public void clearIntermediateBuf() {
        m_intermediateBuf = null;
    }

    public double[] getIntermediateBuf() throws Exception {
        if (m_intermediateFile == null || !isKeepIntermediateFile()) {
            m_intermediateBuf = null;
            return null;
        }
        if (m_intermediateBuf != null) {
            return m_intermediateBuf;
        }
        File interFile = new File(m_intermediateFile);
        if (!interFile.exists()) {
            m_intermediateFile = Preference.getIntermediateFileDir() + ButtonHandler.FILE_SEP + interFile.getName();
            interFile = new File(m_intermediateFile);
        }
        long fileLen = interFile.length();
        long doublesInFile = fileLen / BYTES_IN_DOUBLE;
        double[] buf = new double[(int) doublesInFile];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(interFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        DataInputStream is = new DataInputStream(new BufferedInputStream(fis));
        for (int i = 0; i < buf.length; i++) {
            buf[i] = is.readDouble();
        }
        m_intermediateBuf = buf;
        is.close();
        fis.close();
        return buf;
    }

    /**
     * @return Returns the keepIntermediateFile.
     */
    public boolean isKeepIntermediateFile() {
        return m_commonProperties == null ? false : m_commonProperties.isKeepIntermediateFile();
    }

    public void setIsKeepIntermediateFile(boolean keep) {
        if (m_commonProperties != null) {
            m_commonProperties.setKeepIntermediateFile(keep);
        }
    }

    /**
     * @return Returns the commonProperties.
     */
    public CommonProperties getCommonProperties() {
        return m_commonProperties;
    }

    public String getDurationAdditionalMsg() throws Exception {
        return null;
    }

    public abstract void save(Connection conn, Component frame, int indexInParent, int parentDbId, JComponent component, boolean parentIsSequence) throws SQLException;

    public abstract String getBoxType();
}
