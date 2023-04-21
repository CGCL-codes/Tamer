package net.sf.persistant;

import net.sf.persistant.attribute.MethodAttributeStrategy;
import net.sf.persistant.proxy.ProxyStrategy;

/**
 * <p>
 * <code>PersistantContextBuilder</code> defines the public interface to an object which is used to configure and
 * build {@link PersistantContext} instances.
 * </p>
 */
public interface PersistantContextBuilder {

    /**
     * <p>
     * Build the {@link PersistantContext} instance according to the internal state of this builder object.
     * </p>
     *
     * @return {@link PersistantContext} instance.
     */
    public PersistantContext toPersistantContext();

    /**
     * <p>
     * Set the method attribute discovery strategy for generated resources.
     * </p>
     *
     * @param methodAttributeStrategy the method attribute strategy.
     */
    public void setMethodAttributeStrategy(MethodAttributeStrategy methodAttributeStrategy);

    /**
     * <p>
     * Set debug mode. When debug mode is enabled, various additional initialization and runtime checks are performed.
     * </p>
     *
     * @param debug the debug mode flag.
     */
    public void setDebug(boolean debug);

    /**
     * <p>
     * Set the proxy generation strategy for generated resources.
     * </p>
     *
     * @param proxyStrategy the proxy generation strategy.
     */
    public void setProxyStrategy(ProxyStrategy proxyStrategy);
}
