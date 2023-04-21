package org.jcvi.vapor;

import java.util.Set;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.jcvi.glk.session.SessionExtension;

/**
 *
 *
 * @author jsitz
 */
public class VaporSessionExtension implements SessionExtension {

    private final VaporConfig config;

    /**
     * Creates a new <code>VaporSessionExtension</code>.
     */
    public VaporSessionExtension(VaporConfig config) {
        super();
        this.config = config;
    }

    @Override
    public Set<Class<?>> getAdditionalMappedClasses() {
        return null;
    }

    @Override
    public void overrideConfiguration(Configuration sessionConfig) {
        sessionConfig.setProperty(Environment.SHOW_SQL, this.config.get("db.printDebug"));
        if (this.config.getBoolean("db.use_cache")) {
            sessionConfig.setProperty(Environment.CACHE_PROVIDER, "org.hibernate.cache.OSCacheProvider");
            sessionConfig.setProperty(Environment.USE_QUERY_CACHE, this.config.get("db.use_querycache"));
            sessionConfig.setProperty(Environment.USE_SECOND_LEVEL_CACHE, this.config.get("db.use_cache"));
        }
    }
}
