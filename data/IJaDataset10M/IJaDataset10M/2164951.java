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

public class GroupFactory extends GroupManager {

    /**
     * The static singleton instance object
     */
    private static GroupManager s_instance;

    /**
     * Boolean indicating if the init() method has been called
     */
    private static boolean s_initialized = false;

    /**
     * 
     */
    private File m_groupsConfFile;

    /**
     * 
     */
    private long m_lastModified;

    /**
     * Constructor which parses the file
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ValidationException 
     * @throws MarshalException 
     */
    public GroupFactory() throws MarshalException, ValidationException, FileNotFoundException, IOException {
        reload();
    }

    public static synchronized void init() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        if (s_instance == null || !s_initialized) {
            s_instance = new GroupFactory();
            s_initialized = true;
        }
    }

    /**
     * Singleton static call to get the only instance that should exist for the
     * GroupFactory
     * 
     * @return the single group factory instance
     */
    public static synchronized GroupManager getInstance() {
        return s_instance;
    }

    public static synchronized void setInstance(GroupManager mgr) {
        s_initialized = true;
        s_instance = mgr;
    }

    /**
     * Parses the groups.xml via the Castor classes
     */
    public synchronized void reload() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        File confFile = ConfigFileConstants.getFile(ConfigFileConstants.GROUPS_CONF_FILE_NAME);
        reloadFromFile(confFile);
    }

    /**
     * @param confFile
     * @throws FileNotFoundException
     * @throws MarshalException
     * @throws ValidationException
     */
    private void reloadFromFile(File confFile) throws FileNotFoundException, MarshalException, ValidationException {
        m_groupsConfFile = confFile;
        InputStream configIn = null;
        try {
            configIn = new FileInputStream(m_groupsConfFile);
            m_lastModified = m_groupsConfFile.lastModified();
            parseXml(configIn);
        } finally {
            if (configIn != null) {
                IOUtils.closeQuietly(configIn);
            }
        }
    }

    /**
     * @param data
     * @throws IOException
     */
    protected void saveXml(String data) throws IOException {
        if (data != null) {
            FileWriter fileWriter = new FileWriter(m_groupsConfFile);
            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();
        }
    }

    /**
     * 
     */
    public void update() throws IOException, MarshalException, ValidationException {
        if (m_lastModified != m_groupsConfFile.lastModified()) {
            reload();
        }
    }
}
