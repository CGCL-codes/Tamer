package org.specrunner.sql;

import java.util.HashMap;
import java.util.Map;
import org.specrunner.SpecRunnerServices;
import org.specrunner.context.IContext;
import org.specrunner.features.FeatureManagerException;
import org.specrunner.features.IFeatureManager;
import org.specrunner.plugins.ENext;
import org.specrunner.plugins.PluginException;
import org.specrunner.plugins.impl.AbstractPluginValue;
import org.specrunner.result.IResultSet;
import org.specrunner.result.Status;
import org.specrunner.reuse.IReusable;
import org.specrunner.reuse.IReusableManager;
import org.specrunner.sql.impl.SimpleDataSource;
import org.specrunner.util.UtilLog;

/**
 * Plugin to set connection information. The connection information must be
 * based on a provider (DataSource- see <code>IDataSourceProvider</code>), or
 * direct information with driver/url/user/password.
 * 
 * @author Thiago Santos
 * 
 */
public class PluginConnection extends AbstractPluginValue {

    /**
     * Default connection provider name.
     */
    public static final String CONNECTION_PROVIDER = "connectionProvider";

    /**
     * Connection driver.
     */
    public static final String FEATURE_DRIVER = PluginConnection.class.getName() + ".driver";

    private String driver;

    /**
     * Connection url.
     */
    public static final String FEATURE_URL = PluginConnection.class.getName() + ".url";

    private String url;

    /**
     * Connection user.
     */
    public static final String FEATURE_USER = PluginConnection.class.getName() + ".user";

    private String user;

    /**
     * Connection password.
     */
    public static final String FEATURE_PASSWORD = PluginConnection.class.getName() + ".password";

    private String password;

    /**
     * When using direct connection information, use threadsafe='true' to append
     * thread name to data base URL.
     */
    public static final String FEATURE_THREADSAFE = PluginConnection.class.getName() + ".threadsafe";

    private Boolean threadsafe = false;

    /**
     * Provider name feature.
     */
    public static final String FEATURE_PROVIDER = PluginConnection.class.getName() + ".provider";

    private String provider;

    /**
     * Provider instance feature.
     */
    public static final String FEATURE_PROVIDER_INSTANCE = PluginConnection.class.getName() + ".providerInstance";

    private IDataSourceProvider providerInstance;

    /**
     * Default connection setting for reuse.
     */
    public static final String FEATURE_REUSE = PluginConnection.class.getName() + ".reuse";

    private Boolean reuse = false;

    /**
     * Driver information.
     * 
     * @return The driver name.
     */
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * The database URL connection.
     * 
     * @return The URL.
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The database user.
     * 
     * @return The user.
     */
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * The database password.
     * 
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The thread mode.
     * 
     * @return true, for thread safe connections.
     */
    public Boolean getThreadsafe() {
        return threadsafe;
    }

    public void setThreadsafe(Boolean threadsafe) {
        this.threadsafe = threadsafe;
    }

    /**
     * The provide class name. Something that implements the interface
     * <code>IDataSourceProvider</code>.
     * 
     * @return The provider class name, fully qualified.
     */
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * The instance of a data source.
     * 
     * @return Data source provider.
     */
    public IDataSourceProvider getProviderInstance() {
        return providerInstance;
    }

    public void setProviderInstance(IDataSourceProvider providerInstance) {
        this.providerInstance = providerInstance;
    }

    /**
     * Gets the reuse status. If reuse is true, the object can be reused by
     * multiple tests.
     * 
     * @return The reuse status.
     */
    public Boolean getReuse() {
        return reuse;
    }

    public void setReuse(Boolean reuse) {
        this.reuse = reuse;
    }

    @Override
    public void initialize(IContext context) throws PluginException {
        super.initialize(context);
        IFeatureManager fh = SpecRunnerServices.get(IFeatureManager.class);
        try {
            fh.set(FEATURE_DRIVER, "driver", String.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        try {
            fh.set(FEATURE_URL, "url", String.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        try {
            fh.set(FEATURE_USER, "user", String.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        try {
            fh.set(FEATURE_PASSWORD, "password", String.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        try {
            fh.set(FEATURE_PROVIDER, "provider", String.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        try {
            fh.set(FEATURE_PROVIDER_INSTANCE, "providerInstance", IDataSourceProvider.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        try {
            fh.set(FEATURE_REUSE, "reuse", Boolean.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
        try {
            fh.set(FEATURE_THREADSAFE, "threadsafe", Boolean.class, this);
        } catch (FeatureManagerException e) {
            if (UtilLog.LOG.isDebugEnabled()) {
                UtilLog.LOG.debug(e.getMessage(), e);
            }
        }
    }

    @Override
    public ENext doStart(IContext context, IResultSet result) throws PluginException {
        final String currentName = getName() != null ? getName() : CONNECTION_PROVIDER;
        IReusableManager rm = SpecRunnerServices.get(IReusableManager.class);
        if (reuse) {
            IReusable ir = rm.get(currentName);
            if (ir != null) {
                Map<String, Object> cfg = new HashMap<String, Object>();
                cfg.put("provider", provider);
                cfg.put("driver", driver);
                cfg.put("url", url);
                cfg.put("user", user);
                cfg.put("password", password);
                if (ir.canReuse(cfg)) {
                    ir.reset();
                    if (UtilLog.LOG.isInfoEnabled()) {
                        UtilLog.LOG.info("Reusing DataSource " + ir.getObject());
                    }
                    context.saveGlobal(currentName, ir.getObject());
                    result.addResult(Status.SUCCESS, context.peek());
                    return ENext.DEEP;
                }
            }
        }
        if (providerInstance == null) {
            if (provider != null) {
                try {
                    providerInstance = (IDataSourceProvider) Class.forName(provider).newInstance();
                } catch (Exception e) {
                    throw new PluginException("Invalid DataSource provider '" + provider + "'.", e);
                }
            } else {
                if (driver != null && url != null && user != null && password != null) {
                    String suffix = Thread.currentThread().getName();
                    suffix = suffix.replace("-", "");
                    providerInstance = new SimpleDataSource(driver, (threadsafe ? url + suffix : url), user, password);
                } else {
                    throw new PluginException(PluginConnection.class.getSimpleName() + " must have a provider instance set using feature 'FEATURE_PROVIDER_INSTANCE', a generator of DataSource using feature 'FEATURE_PROVIDER', or connection informations 'driver/url/user/password' passed as attributes or their specific 'FEATURE_...'.");
                }
            }
        }
        if (reuse) {
            rm.put(currentName, new IReusable() {

                @Override
                public boolean canReuse(Map<String, Object> cfg) {
                    return (provider != null && provider.equalsIgnoreCase((String) cfg.get("provider"))) || (driver.equalsIgnoreCase((String) cfg.get("driver")) && url.equalsIgnoreCase((String) cfg.get("url")) && user.equalsIgnoreCase((String) cfg.get("user")) && password.equalsIgnoreCase((String) cfg.get("password")));
                }

                @Override
                public void reset() {
                }

                @Override
                public void release() {
                    providerInstance.release();
                }

                @Override
                public Object getObject() {
                    return providerInstance;
                }

                @Override
                public String getName() {
                    return currentName;
                }
            });
        }
        context.saveGlobal(currentName, providerInstance);
        result.addResult(Status.SUCCESS, context.peek());
        return ENext.DEEP;
    }

    public static IDataSourceProvider getProvider(IContext context, String name) throws PluginException {
        if (name == null) {
            name = CONNECTION_PROVIDER;
        }
        IDataSourceProvider provider = (IDataSourceProvider) context.getByName(name);
        if (provider == null) {
            throw new PluginException("Instance of '" + IDataSourceProvider.class.getName() + "' not found. Use " + PluginConnection.class.getSimpleName() + " before.");
        }
        return provider;
    }
}
