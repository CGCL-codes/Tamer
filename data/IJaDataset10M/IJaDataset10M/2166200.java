package com.ewaloo.impl.cms.app.view;

import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.app.db.DatabasePool;
import org.atlantal.api.app.exception.ServiceException;
import org.atlantal.api.cache.Cache;
import org.atlantal.api.cache.CacheLoader;
import org.atlantal.api.cache.CacheLoaderParams;
import org.atlantal.api.cache.app.CacheService;
import org.atlantal.api.cms.exception.DisplayException;
import org.atlantal.impl.cache.AttributesInstance;
import org.atlantal.impl.cache.base.BaseCacheLoaderParamsInstance;
import org.atlantal.impl.cms.display.app.InterfaceServiceInstance;
import org.atlantal.utils.UpdatedEvent;
import org.atlantal.utils.wrapper.ObjectWrapper;
import com.ewaloo.impl.cms.loader.view.MySQLEnvironmentLoader;
import com.ewaloo.impl.cms.loader.view.MySQLInterfaceChildLoader;
import com.ewaloo.impl.cms.loader.view.MySQLInterfaceLoader;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public class EwSQLInterfaceService extends InterfaceServiceInstance {

    private static final Logger LOGGER = Logger.getLogger(EwSQLInterfaceService.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private DatabasePool dbpool;

    private CacheService cacheservice;

    private Cache systemCache;

    private AttributesInstance envAttributes;

    private AttributesInstance ifaceAttributes;

    private AttributesInstance ifceChdAttributes;

    /**
     * {@inheritDoc}
     */
    public void init(Map params) throws ServiceException {
        super.init(params);
        String dbservice = (String) params.get("databasepool");
        dbpool = (DatabasePool) this.getService(dbservice);
        String cacheservicestr = (String) params.get("cache");
        cacheservice = (CacheService) this.getService(cacheservicestr);
        systemCache = cacheservice.getCache("system");
        CacheLoader loader;
        String jclass;
        CacheLoaderParams clparams = new BaseCacheLoaderParamsInstance(this, dbpool);
        jclass = MySQLEnvironmentLoader.class.getName();
        loader = cacheservice.getLoader(jclass);
        envAttributes = new AttributesInstance();
        envAttributes.setLoader(loader);
        envAttributes.setLoaderParams(clparams);
        envAttributes.setPermanent(true);
        jclass = MySQLInterfaceLoader.class.getName();
        loader = cacheservice.getLoader(jclass);
        ifaceAttributes = new AttributesInstance();
        ifaceAttributes.setLoader(loader);
        ifaceAttributes.setLoaderParams(clparams);
        ifaceAttributes.setPermanent(true);
        jclass = MySQLInterfaceChildLoader.class.getName();
        loader = cacheservice.getLoader(jclass);
        ifceChdAttributes = new AttributesInstance();
        ifceChdAttributes.setLoader(loader);
        ifceChdAttributes.setPermanent(true);
    }

    /**
     * {@inheritDoc}
     */
    public Cache getSystemCache() {
        return this.systemCache;
    }

    /**
     * {@inheritDoc}
     */
    public ObjectWrapper getEnvironment(Integer id) throws DisplayException {
        ObjectWrapper environment = getSystemCache().getHandle(id, id, envAttributes);
        return environment;
    }

    /**
     * {@inheritDoc}
     */
    public ObjectWrapper getInterface(Integer id) throws DisplayException {
        ObjectWrapper document = getSystemCache().getHandle(id, id, ifaceAttributes);
        return document;
    }

    /**
     * {@inheritDoc}
     */
    public ObjectWrapper getInterfaceChild(Integer id) throws DisplayException {
        ObjectWrapper ifacechild = getSystemCache().getHandle(id, id, ifceChdAttributes);
        return ifacechild;
    }

    /**
     * {@inheritDoc}
     */
    public void update(UpdatedEvent event) {
    }
}
