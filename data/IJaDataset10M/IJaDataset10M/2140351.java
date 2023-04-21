package org.dozer.config;

/**
 * Internal constants file that holds all the keys to configurable properties in the dozer.properties file
 * 
 * @author tierney.matt
 */
public final class PropertyConstants {

    private PropertyConstants() {
    }

    public static final String STATISTICS_ENABLED = "dozer.statistics.enabled";

    public static final String CONVERTER_CACHE_MAX_SIZE = "dozer.cache.converter.by.dest.type.maxsize";

    public static final String SUPERTYPE_CACHE_MAX_SIZE = "dozer.cache.super.type.maxsize";

    public static final String AUTOREGISTER_JMX_BEANS = "dozer.autoregister.jmx.beans";

    public static final String EL_ENABLED = "dozer.el.enabled";

    public static final String CLASS_LOADER_BEAN = "org.dozer.util.DozerClassLoader";

    public static final String PROXY_RESOLVER_BEAN = "org.dozer.util.DozerProxyResolver";
}
