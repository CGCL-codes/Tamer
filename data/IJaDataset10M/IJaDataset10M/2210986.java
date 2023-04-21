package com.ganzhavitaliy.copslocator.gae.server;

import com.ganzhavitaliy.copslocator.gae.server.logic.GeoLocationsManagerImpl;
import com.ganzhavitaliy.copslocator.gae.server.model.dao.GeoLocationsDao;
import com.ganzhavitaliy.copslocator.gae.server.model.dao.GeoLocationsDaoImpl;
import com.ganzhavitaliy.copslocator.gae.server.logic.GeoLocationsManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * This class defines dependency injection for the application  
 * @author Vitaliy Ganzha
 *
 */
public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GeoLocationsDao.class).to(GeoLocationsDaoImpl.class).in(Singleton.class);
        bind(GeoLocationsManager.class).to(GeoLocationsManagerImpl.class).in(Singleton.class);
    }
}
