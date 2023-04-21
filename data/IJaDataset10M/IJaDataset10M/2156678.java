package net.mchaplin.ioc.service;

import net.mchaplin.ioc.component.DefaultComponent;

/**
 * Core abstract class used by internal
 * services. 
 * 
 * @author mchaplin@users.sourceforge.net
 */
public abstract class Service extends DefaultComponent implements ServiceI {

    /**
     * @see net.mchaplin.ioc.service.ServiceI#getState()
     */
    public String getState() {
        return null;
    }
}
