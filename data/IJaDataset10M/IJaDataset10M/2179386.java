package org.proteusframework.core.base;

import org.proteusframework.core.api.IPlatformDelegate;
import org.proteusframework.core.api.IRuntimeConfiguration;
import org.proteusframework.core.api.model.INamespace;
import org.proteusframework.core.util.Assert;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Base class for all {@link IRuntimeConfiguration} instances. Each platform, e.g. a
 * {@link org.proteusframework.core.api.RuntimeEnvironment#Desktop}, must define its own runtime configuration
 * instance. This supports disparate runtime configuration architectures, e.g. the desktop uses the Windows Registry
 * while a mobile might use a unique Android OS capability.
 *
 * @author Tacoma Four
 */
public abstract class AbstractRuntimeConfiguration implements IRuntimeConfiguration {

    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger(AbstractRuntimeConfiguration.class.getCanonicalName());

    /**
     * Platform delegate.
     */
    protected IPlatformDelegate delegate = null;

    /**
     * Generic configuration map that links arbitrary configuration data with an <code>INamespace</code>. Each
     * key can be associated with only a single configuration object; it is the responsibility of the developer to
     * create an appropriate wrapper Object implementation if their object requires more than one object worth of
     * configuration data.
     */
    protected Map<INamespace, Object> configurationMap = new HashMap<INamespace, Object>();

    /**
     * Delegate's actual interface class.
     */
    private Class<? extends IPlatformDelegate> delegateInterface;

    /**
     * {@inheritDoc}
     */
    @Override
    public final Map<INamespace, Object> getConfigurationMap() {
        Map<INamespace, Object> map = new HashMap<INamespace, Object>();
        for (INamespace namespace : configurationMap.keySet()) {
            map.put(namespace, configurationMap.get(namespace));
        }
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object getValue(INamespace namespace) {
        Assert.parameterNotNull(namespace, "Parameter 'namespace' must not be null");
        return configurationMap.get(namespace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean hasValue(INamespace namespace) {
        Assert.parameterNotNull(namespace, "Parameter 'namespace' must not be null");
        return configurationMap.containsKey(namespace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object getAdapter(Class adapter) {
        return delegate.getAdapterManager().getAdapter(this, adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends IPlatformDelegate> void setPlatformDelegate(IPlatformDelegate delegate, Class<T> delegateInterface) {
        Assert.unitialized(this.delegate, "Platform delegate is implicitly final and cannot be modified.");
        Assert.parameterNotNull(delegate, "Parameter 'delegate' must not be null");
        this.delegate = delegate;
        this.delegateInterface = delegateInterface;
        onDelegateInjected();
    }

    /**
     * Empty delegate injection event handler for descendent classes.
     */
    protected abstract void onDelegateInjected();
}
