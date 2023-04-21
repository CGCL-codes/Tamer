package trungsi.gea.photos.cache;

import java.util.Collections;
import java.util.Map;
import javax.cache.Cache;
import javax.cache.CacheManager;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author trungsi
 *
 */
public class CacheFactoryBean implements FactoryBean<Cache> {

    private Map<Object, Object> configurations;

    public void setConfigurations(Map<Object, Object> config) {
        this.configurations = config;
    }

    @Override
    public Cache getObject() throws Exception {
        javax.cache.CacheFactory cf = CacheManager.getInstance().getCacheFactory();
        if (configurations == null) {
            configurations = Collections.emptyMap();
        }
        System.out.println(configurations);
        return cf.createCache(configurations);
    }

    @Override
    public Class<? extends Cache> getObjectType() {
        return Cache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
