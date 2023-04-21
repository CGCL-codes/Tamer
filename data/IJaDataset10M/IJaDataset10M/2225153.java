package org.opennms.web.map.config;

import java.util.*;
import java.util.Date;
import java.io.*;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.*;
import org.opennms.web.map.config.*;

public class MapsConfigFactory {

    /**
	 * Singleton instance
	 */
    private static MapsConfigFactory instance;

    /**
	 * Object containing all Configuration info and objects parsed from the xml file
	 */
    protected static MapsConfiguration m_mapsConfiguration;

    /**
	 * Input stream for the general map configuration xml
	*/
    protected static InputStream configIn;

    /**
	 * Boolean indicating if the init() method has been called
	 */
    private static boolean initialized = false;

    /**
	 * The Maps Configuration File
	 */
    private static File m_mapsConfFile;

    /**
	 * A long Date Representing map Configuration File Last Modified Date
	*/
    private static long m_lastModified;

    /**
	 * Object Containig Header parsed from xml file
	 */
    private static Header oldHeader;

    private MapsConfigFactory() {
    }

    public static synchronized MapsConfigFactory getInstance() {
        if (!initialized) return null;
        if (instance == null) {
            instance = new MapsConfigFactory();
        }
        return instance;
    }

    /**
 * 
 * @throws IOException
 * @throws FileNotFoundException
 * @throws MarshalException
 * @throws ValidationException
 */
    public static synchronized void init() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        if (!initialized) {
            reload();
            initialized = true;
        }
    }

    public static synchronized void reload() throws IOException, MarshalException, ValidationException {
        m_mapsConfFile = ConfigFileConstants.getFile(ConfigFileConstants.MAP_CONF_FILE_NAME);
        InputStream configIn = new FileInputStream(m_mapsConfFile);
        m_lastModified = m_mapsConfFile.lastModified();
        m_mapsConfiguration = (MapsConfiguration) Unmarshaller.unmarshal(MapsConfiguration.class, new InputStreamReader(configIn));
        oldHeader = m_mapsConfiguration.getHeader();
    }

    /**
	 * 
	 * @return Hash Table containing  Object Building as function of Building Name
	 * @throws IOException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
    public java.util.Map getMapsPlugin() throws IOException, MarshalException, ValidationException {
        updateFromFile();
        java.util.Map newMap = new HashMap();
        Plugins plugins = m_mapsConfiguration.getPlugins();
        MapsPlugin mapsPlugin[] = plugins.getMapsPlugin();
        for (int i = 0; i < mapsPlugin.length; i++) {
            newMap.put(mapsPlugin[i].getName(), mapsPlugin[i]);
        }
        return newMap;
    }

    private Header rebuildHeader() {
        Header header = oldHeader;
        header.setCreated(EventConstants.formatToString(new Date()));
        return header;
    }

    private static void updateFromFile() throws IOException, MarshalException, ValidationException {
        if (m_lastModified != m_mapsConfFile.lastModified()) {
            reload();
        }
    }

    public synchronized void saveCurrent() throws MarshalException, ValidationException, IOException, ClassNotFoundException {
        m_mapsConfiguration.setHeader(rebuildHeader());
    }
}
