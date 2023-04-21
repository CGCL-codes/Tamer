package net.sf.ajaxplus.core.factory;

import java.util.HashMap;
import java.util.Map;
import net.sf.ajaxplus.core.bind.BindBean;
import bm.validusre.vcaps.service.CacheService;
import bm.validusre.vcaps.service.EntityService;
import bm.validusre.vcaps.service.LookupService;
import bm.validusre.vcaps.service.SessionService;

public class ServiceFactory extends Factory<Object> {

    protected Map<String, BeanLoader<Object>> beanLoaderMapping = new HashMap<String, BeanLoader<Object>>();

    protected Map<String, BeanLoader<Object>> getBeanLoaderMapping() {
        return this.beanLoaderMapping;
    }

    private DaoFactory daoFactory;

    public ServiceFactory() {
        daoFactory = DaoFactory.getInstance();
    }

    private static ServiceFactory instance;

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
            instance.loadMappingFromAnnotation(instance);
        }
        return instance;
    }

    public static final String ENTITY_SERVICE = "entityService";

    public static final String SESSION_SERVICE = "sessionService";

    public static final String LOOKUP_SERVICE = "lookupService";

    public static final String CACHE_SERVICE = "cacheService";

    private EntityService entityService;

    private SessionService sessionService;

    private LookupService lookupService;

    private CacheService cacheService;

    @BindBean(id = ENTITY_SERVICE)
    public EntityService getEntityService() {
        if (entityService == null) {
            entityService = new EntityService();
            entityService.setEntityDao(daoFactory.getEntityDao());
        }
        return entityService;
    }

    @BindBean(id = SESSION_SERVICE)
    public SessionService getSessionService() {
        if (sessionService == null) {
            sessionService = new SessionService();
            sessionService.setSessionDao(daoFactory.getSessionDao());
            sessionService.setSessionObjectDao(daoFactory.getSessionObjectDao());
            sessionService.setEntityService(this.getEntityService());
        }
        return sessionService;
    }

    @BindBean(id = LOOKUP_SERVICE)
    public LookupService getLookupService() {
        if (lookupService == null) {
            lookupService = new LookupService();
            lookupService.setLookupDao(daoFactory.getLookupDao());
            lookupService.setLookupItemDao(daoFactory.getLookupItemDao());
        }
        return lookupService;
    }

    @BindBean(id = CACHE_SERVICE)
    public CacheService getCacheService() {
        if (cacheService == null) {
            cacheService = new CacheService();
            cacheService.setLookupService(this.getLookupService());
        }
        return cacheService;
    }
}
