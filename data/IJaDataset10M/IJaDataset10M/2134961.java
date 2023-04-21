package es.rediris.searchy.engine.provider;

import org.apache.log4j.Logger;

/** 
 * @version 0.1
 * @author David Fernandez Barrero
 */
public class ProviderFactory {

    /**
	 *	It implements the factory design pattern, depending of the 
	 *  configuration, this class instanciates the correct class
	 */
    static Logger logger = Logger.getLogger(ProviderFactory.class);

    public Provider getProvider(ProviderConfig conf) {
        Provider provider = null;
        String type = conf.getType();
        try {
            if (type.equals("SQL")) {
                logger.info("Building SQL provider: " + conf.getName());
                provider = new ProviderSQL(conf);
                logger.info(conf.getName() + " correcty built");
                return provider;
            } else if (type.equals("Searchy")) {
                logger.info("Building Searchy provider: " + conf.getName());
                provider = new ProviderSearchy(conf);
                logger.info(conf.getName() + " correcty built");
                return provider;
            } else if (type.equals("LDAP")) {
                logger.info("Building LDAP provider: " + conf.getName());
                provider = new ProviderLDAP(conf);
                logger.info(conf.getName() + " correcty built");
                return provider;
            } else if (type.equals("Google")) {
                logger.info("Building Google provider: " + conf.getName());
                provider = new ProviderGoogle(conf);
                logger.info(conf.getName() + " correcty built");
                return provider;
            } else if (type.equals("Harvest")) {
                logger.info("Building Harvest provider: " + conf.getName());
                provider = new ProviderHarvest(conf);
                logger.info(conf.getName() + " correcty built");
                return provider;
            } else if (type.equals("WSDL")) {
                logger.info("Building WSDL provider: " + conf.getName());
                provider = new ProviderWSDL(conf);
                logger.info(conf.getName() + " correcty built");
                return provider;
            } else if (type.equals("custom")) {
                logger.info("Building custom provider: " + conf.getName());
                if (conf.getParameter("class") == null) {
                    throw new IllegalArgumentException("Class not given, please, chech the config");
                }
                logger.info("Using class " + conf.getParameter("class"));
                Class temp = Class.forName(conf.getParameter("class"));
                provider = (Provider) temp.newInstance();
                provider.setConfig(conf);
                logger.info(conf.getName() + " correcty built");
                return provider;
            } else {
                throw new IllegalArgumentException("Provider type " + type + " not supported");
            }
        } catch (ClassNotFoundException e) {
            logger.error("Class not found. " + e.getMessage());
        } catch (InstantiationException e) {
            logger.error("Class not instanciated. " + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccess " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unknown error. " + e.getMessage());
        }
        logger.error("Provider " + conf.getName() + " could not be created");
        return null;
    }
}
