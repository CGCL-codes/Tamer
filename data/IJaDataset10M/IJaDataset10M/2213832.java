package org.granite.config.api;

/**
 * API use to override the default GraniteDS configuration.
 * @author <a href="mailto:gembin@gmail.com">gembin@gmail.com</a>
 * @since 1.1.0
 */
public interface Configuration {

    /**
	 * set the granite-config.xml path
	 * @param cfgFile
	 */
    public void setGraniteConfig(String cfgFile);

    public String getGraniteConfig();

    /**
	 * set the  granite-config.properties path
	 * @param granitecfgPropFile
	 */
    public void setGraniteConfigProperties(String granitecfgPropFile);

    public String getGraniteConfigProperties();

    /**
	 * set the services-config.xml path
	 * @param flexCfgFile
	 */
    public void setFlexServicesConfig(String flexCfgFile);

    public String getFlexServicesConfig();

    /**
	 *  set the services-config.properties path
	 * @param flexCfgPropFile
	 */
    public void setFlexServicesConfigProperties(String flexCfgPropFile);

    public String getFlexServicesConfigProperties();
}
