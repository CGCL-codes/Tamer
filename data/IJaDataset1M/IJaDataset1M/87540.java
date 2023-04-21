package org.brainypdm.modules.commons.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.brainypdm.exceptions.BaseException;

/*******************************************************************************
 * 
 * This singleton store commons properties used in more than one module
 * 
 * @author <a HREF="mailto:thomas@brainypdm.org">Thomas Buffagni</a>
 * 
 */
public class CommonsConfiguration extends ConfigurationWrapper {

    private static final long serialVersionUID = 8598654693717557234L;

    /***************************************************************************
	 * this is the name of commons's module properties file
	 */
    private static final String K_PROPERTY_FILE_NAME = "commons_configuration.properties";

    /***************************************************************************
	 * this is the name of system's property that contains the path of
	 * properties file for overload the defaul configuration
	 */
    private static final String K_OVERLOADING_SYS_PROPERTY_NAME = "commons_configuration_file";

    /***************************************************************************
	 * this is the instance of the singleton
	 */
    private static CommonsConfiguration instance;

    private CommonsConfiguration(String propertiesFileName, String overloadingSysProperties) throws BaseException {
        super(propertiesFileName, overloadingSysProperties);
    }

    /***************************************************************************
	 * 
	 * @return the VM's instance of this class
	 * @throws ConfigurationException
	 * @author <a HREF="mailto:thomas@brainypdm.org">Thomas Buffagni</a>
	 */
    public synchronized CommonsConfiguration getInstance() throws BaseException {
        if (instance == null) {
            instance = new CommonsConfiguration(K_PROPERTY_FILE_NAME, K_OVERLOADING_SYS_PROPERTY_NAME);
        }
        return instance;
    }
}
