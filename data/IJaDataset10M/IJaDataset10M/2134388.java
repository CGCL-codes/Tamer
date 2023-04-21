package com.frinika.client.factory;

import com.frinika.ejb.DatabaseBeanIF;

/**
 * 
 * Interface for providers of DatabaseBeanIF
 * 
 * @author pjl
 *
 */
public interface DatabaseBeanFactory {

    DatabaseBeanIF createDatabaseBean(String key) throws Exception;
}
