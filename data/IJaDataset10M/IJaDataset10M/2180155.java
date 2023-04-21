package com.tll.dao.db4o;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springextensions.db4o.Db4oTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;
import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.IConfigAware;
import com.tll.config.IConfigKey;
import com.tll.dao.IEntityDao;
import com.tll.model.EntityMetadata;
import com.tll.model.IEntityFactory;
import com.tll.model.IEntityMetadata;

/**
 * AbstractDb4oDaoModule - Db4o dao impl module.
 * @author jpk
 */
public abstract class AbstractDb4oDaoModule extends AbstractModule implements IConfigAware {

    private static final int DEFAULT_TRANS_TIMEOUT = 60;

    private static final String DEFAULT_DB4O_FILENAME = "db4o";

    private static final boolean DEFAULT_EMPLOY_SPRING_TRANSACTIONS = false;

    static final Log log = LogFactory.getLog(AbstractDb4oDaoModule.class);

    /**
	 * Db4oFile annotation
	 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.PARAMETER })
    @BindingAnnotation
    public @interface Db4oFile {
    }

    /**
	 * ConfigKeys.
	 * @author jpk
	 */
    public static enum ConfigKeys implements IConfigKey {

        DB4O_FILENAME("db.db4o.filename"), DB_TRANS_TIMEOUT("db.transaction.timeout"), DB_TRANS_BINDTOSPRING("db.transaction.bindToSpringAtTransactional");

        private final String key;

        private ConfigKeys(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    /**
	 * @param path the db4o file path
	 * @return the corresponding URI
	 */
    public static URI getDb4oFileRef(String path) {
        try {
            final URL url = AbstractDb4oDaoModule.class.getClassLoader().getResource(path);
            URI uri = url == null ? null : url.toURI();
            if (uri == null) {
                final File f = new File(path);
                uri = f.toURI();
            }
            return uri;
        } catch (final URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    Config config;

    /**
	 * Constructor
	 */
    public AbstractDb4oDaoModule() {
        super();
    }

    /**
	 * Constructor
	 * @param config
	 */
    public AbstractDb4oDaoModule(Config config) {
        super();
        this.config = config;
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
	 * Opportunity for concrete impls to tailor the Configuration on an entity
	 * (object) level.<br>
	 * E.g.: setting updateDepth(...) and/or cascadeOnUpdate(...).
	 * @param c
	 */
    protected abstract void configureConfiguration(EmbeddedConfiguration c);

    /**
	 * @return The db4o named query translator implmentation type.
	 */
    protected abstract Class<? extends IDb4oNamedQueryTranslator> getNamedQueryTranslatorImpl();

    @Override
    protected final void configure() {
        log.info("Loading db4o dao module...");
        bind(URI.class).annotatedWith(Db4oFile.class).toInstance(getDb4oFileRef(config == null ? DEFAULT_DB4O_FILENAME : config.getString(ConfigKeys.DB4O_FILENAME.getKey())));
        bind(EmbeddedConfiguration.class).toProvider(new Provider<EmbeddedConfiguration>() {

            @Override
            public EmbeddedConfiguration get() {
                final EmbeddedConfiguration c = Db4oEmbedded.newConfiguration();
                configureConfiguration(c);
                return c;
            }
        }).in(Scopes.NO_SCOPE);
        bind(EmbeddedObjectContainer.class).toProvider(new Provider<EmbeddedObjectContainer>() {

            @Inject
            @Db4oFile
            URI db4oUri;

            @Inject
            Provider<EmbeddedConfiguration> c;

            @Override
            public EmbeddedObjectContainer get() {
                log.info("Creating db4o session for: " + db4oUri);
                return Db4oEmbedded.openFile(c.get(), db4oUri.getPath());
            }
        }).in(Scopes.SINGLETON);
        final boolean dst = config == null ? DEFAULT_EMPLOY_SPRING_TRANSACTIONS : config.getBoolean(ConfigKeys.DB_TRANS_BINDTOSPRING.getKey(), DEFAULT_EMPLOY_SPRING_TRANSACTIONS);
        if (dst) {
            log.info("Binding Spring's Db4oTransactionManager to Spring's @Transactional annotation..");
            bind(PlatformTransactionManager.class).toProvider(new Provider<PlatformTransactionManager>() {

                @Inject
                EmbeddedObjectContainer oc;

                @Override
                public PlatformTransactionManager get() {
                    final Db4oTransactionManager db4oTm = new Db4oTransactionManager(oc);
                    final int timeout = config == null ? DEFAULT_TRANS_TIMEOUT : config.getInt(ConfigKeys.DB_TRANS_TIMEOUT.getKey(), DEFAULT_TRANS_TIMEOUT);
                    db4oTm.setDefaultTimeout(timeout);
                    log.info("Set DB4O default transaction timeout to: " + timeout);
                    try {
                        db4oTm.afterPropertiesSet();
                    } catch (final Exception e) {
                        throw new IllegalStateException(e);
                    }
                    AnnotationTransactionAspect.aspectOf().setTransactionManager(db4oTm);
                    return db4oTm;
                }
            }).asEagerSingleton();
        }
        bind(IEntityMetadata.class).to(EntityMetadata.class);
        bind(IEntityFactory.class).to(Db4oEntityFactory.class).in(Scopes.SINGLETON);
        bind(IDb4oNamedQueryTranslator.class).to(getNamedQueryTranslatorImpl()).in(Scopes.SINGLETON);
        bind(IEntityDao.class).to(Db4oEntityDao.class).in(Scopes.SINGLETON);
    }
}
