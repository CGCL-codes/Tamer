package org.rg.workflow.support;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rg.common.properties.CommonProperties;
import org.rg.common.properties.IntegratedProperties;
import org.rg.common.properties.PropertiesFactory;

/**
 * This class provides an interface to the integrated properties service. It requires the name of the machine containing
 * the integrated properties, and the port number for the properties service.
 * <p>
 * The most common properties are provided by explicit methods as a convenience, but any properties can be accessed by
 * name.
 * @author redman
 */
public class IntegratedPropertiesService {

    /** this logger is used for only processing batches. */
    private final Log log = LogFactory.getLog(IntegratedPropertiesService.class.getName());

    /** the properties. */
    private IntegratedProperties props = null;

    /** the machine where remote props are. */
    private String machine = "127.0.0.1";

    /** port for the remote props service. */
    private int port = 1099;

    /**
    * the name of the file from which we will load the properties. If this field is non-null, we will load the file from
    * disk, otherwise, we will load it from the remote properties.
    */
    private String filename = null;

    /**
    * @return the filename as an alternative to the remove.
    */
    public String getFilename() {
        return filename;
    }

    /**
    * change the properties filename.
    * @param fn the machine name.
    */
    public void setFilename(String fn) {
        this.filename = fn;
    }

    /**
    * @return the machine serving the remote props.
    */
    public String getMachine() {
        return machine;
    }

    /**
    * change the machine serving the properties.
    * @param machine
    */
    public void setMachine(String machine) {
        this.machine = machine;
    }

    /**
    * @return the port where the remote props are served.
    */
    public int getPort() {
        return port;
    }

    /**
    * @param port where the remote props are servced up.
    */
    public void setPort(int port) {
        this.port = port;
    }

    /** get access to the remote properties. */
    public IntegratedPropertiesService() {
        if (IntegratedProperties.getIntegratedProperties() == null) {
            boolean reported = false;
            while (true) {
                log.warn("Properties loaded by service");
                try {
                    if (filename != null) PropertiesFactory.getIntegratedProperties(filename); else PropertiesFactory.getIntegratedProperties(machine, port);
                    break;
                } catch (IOException e) {
                    if (!reported) {
                        log.warn("Error getting properties, will keep trying till they are available.", e);
                        reported = true;
                    }
                }
            }
            if (reported) {
                log.warn("Got the properties.");
            }
        }
        log.warn("Properties acquired");
        props = IntegratedProperties.getIntegratedProperties();
    }

    /**
    * @return a reference to the properties.
    */
    public IntegratedProperties getProperties() {
        return props;
    }

    /**
    * @return the url for the content database.
    */
    public String getContentDatabaseURL() {
        return props.getProperty(CommonProperties.GLOBAL_DB_URL);
    }

    /**
    * @return the user for the content database.
    */
    public String getContentDatabaseUser() {
        return props.getProperty(CommonProperties.GLOBAL_DB_USER);
    }

    /**
    * @return the password for the content database.
    */
    public String getContentDatabasePassword() {
        return props.getProperty(CommonProperties.GLOBAL_DB_PASSWORD);
    }

    /**
    * @return the jdbc driver for the content database.
    */
    public String getContentDatabaseDriver() {
        return props.getProperty(CommonProperties.GLOBAL_DB_DRIVER);
    }

    /**
    * @return the url for the control database.
    */
    public String getControlDatabaseURL() {
        return props.getProperty(CommonProperties.CONTROL_DATABASE_URL_PROP);
    }

    /**
    * @return the user for the control database.
    */
    public String getControlDatabaseUser() {
        return props.getProperty(CommonProperties.CONTROL_DATABASE_USER_ID_PROP);
    }

    /**
    * @return the password for the control database.
    */
    public String getControlDatabasePassword() {
        return props.getProperty(CommonProperties.CONTROL_DATABASE_PASSWORD_PROP);
    }

    /**
    * @return the jdbc driver for the control database.
    */
    public String getControlDatabaseDriver() {
        return props.getProperty(CommonProperties.CONTROL_DATABASE_DRIVER_PROP);
    }
}
