package com.googlecode.g2re.domain;

import com.googlecode.g2re.Settings;
import javax.jdo.PersistenceManagerFactory;

/**
 * Connection to google app engine data store using JDO.
 * @author Brad Rydzewski
 */
public class AppEngineConnection extends DataConnection {

    /**
     * Gets the PersistenceManagerFactory instance used by Google App Engine.
     * @return PersistenceManagerFactory.
     */
    public PersistenceManagerFactory getPersistenceManagerFactory() {
        return Settings.get().getPersistenceManagerFactory();
    }
}
