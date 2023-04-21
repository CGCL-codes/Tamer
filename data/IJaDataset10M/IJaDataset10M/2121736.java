package org.opennms.netmgt.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.ConfigFileConstants;

/**
 * @author david
 *
 */
public class ChartConfigFactory extends ChartConfigManager {

    private static boolean m_initialized = false;

    private static ChartConfigFactory m_instance = null;

    private static File m_chartConfigFile;

    private static long m_lastModified;

    public static synchronized void init() throws MarshalException, ValidationException, FileNotFoundException, IOException {
        if (!m_initialized) {
            m_instance = new ChartConfigFactory();
            reload();
            m_initialized = true;
        }
    }

    /**
     * @throws IOException
     * @throws FileNotFoundException
     * @throws MarshalException
     * @throws ValidationException
     */
    public static synchronized void reload() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        m_chartConfigFile = ConfigFileConstants.getFile(ConfigFileConstants.CHART_CONFIG_FILE_NAME);
        InputStream configIn = null;
        try {
            configIn = new FileInputStream(m_chartConfigFile);
            m_lastModified = m_chartConfigFile.lastModified();
            parseXml(configIn);
        } finally {
            if (configIn != null) {
                IOUtils.closeQuietly(configIn);
            }
        }
    }

    /**
     * @param xml
     * @throws IOException
     */
    protected void saveXml(String xml) throws IOException {
        if (xml != null) {
            FileWriter fileWriter = new FileWriter(m_chartConfigFile);
            fileWriter.write(xml);
            fileWriter.flush();
            fileWriter.close();
        }
    }

    /**
     * 
     */
    public void update() throws IOException, MarshalException, ValidationException {
        if (m_lastModified != m_chartConfigFile.lastModified()) {
            NotifdConfigFactory.getInstance().reload();
        }
    }

    public static ChartConfigFactory getInstance() {
        if (!m_initialized) {
            throw new IllegalStateException("Factory not initialized");
        }
        return m_instance;
    }

    public static void setInstance(ChartConfigFactory instance) {
        m_instance = instance;
        m_initialized = true;
    }
}
