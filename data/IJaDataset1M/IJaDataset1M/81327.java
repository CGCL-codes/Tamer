package org.piuframework.service.config;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.piuframework.config.ConfigProperties;

/**
 * TODO
 *
 * @author Dirk Mascher
 */
public class ServiceTemplateConfig implements Serializable {

    private static final long serialVersionUID = -3314366700819066278L;

    private String name;

    private String lifecycleHandlerName;

    private String invocationHandlerName;

    private String interceptorChainName;

    private LifecycleHandlerRefConfig lifecycleHandlerRefConfig;

    private InvocationHandlerRefConfig invocationHandlerRefConfig;

    private InterceptorChainRefConfig interceptorChainRefConfig;

    private LifecycleHandlerConfig lifecycleHandlerConfig;

    private InvocationHandlerConfig invocationHandlerConfig;

    private InterceptorChainConfig interceptorChainConfig;

    private ConfigProperties lifecycleHandlerProperties;

    private ConfigProperties invocationHandlerProperties;

    public ServiceTemplateConfig() {
        super();
    }

    public void initReferences(ServiceFactoryConfig factoryConfig) {
        if (lifecycleHandlerConfig != null) {
            lifecycleHandlerProperties = lifecycleHandlerConfig.getProperties();
        } else if (lifecycleHandlerRefConfig != null) {
            lifecycleHandlerRefConfig.initReferences(factoryConfig);
            lifecycleHandlerProperties = lifecycleHandlerRefConfig.getMergedProperties();
        } else if (lifecycleHandlerName != null) {
            lifecycleHandlerConfig = factoryConfig.getLifecycleHandlerConfig(lifecycleHandlerName);
            lifecycleHandlerProperties = lifecycleHandlerConfig.getProperties();
        }
        if (invocationHandlerConfig != null) {
            invocationHandlerProperties = invocationHandlerConfig.getProperties();
        } else if (invocationHandlerRefConfig != null) {
            invocationHandlerRefConfig.initReferences(factoryConfig);
            invocationHandlerProperties = invocationHandlerRefConfig.getMergedProperties();
        } else if (invocationHandlerName != null) {
            invocationHandlerConfig = factoryConfig.getInvocationHandlerConfig(invocationHandlerName);
            invocationHandlerProperties = invocationHandlerConfig.getProperties();
        }
        if (interceptorChainRefConfig != null) {
            interceptorChainRefConfig.initReferences(factoryConfig);
        } else {
            if (interceptorChainName != null) {
                interceptorChainConfig = factoryConfig.getInterceptorChainConfig(interceptorChainName);
            }
        }
        if (interceptorChainConfig != null) {
            interceptorChainConfig.initReferences(factoryConfig);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LifecycleHandlerConfig getLifecycleHandlerConfig() {
        LifecycleHandlerConfig config = null;
        if (lifecycleHandlerConfig != null) {
            config = lifecycleHandlerConfig;
        } else {
            if (lifecycleHandlerRefConfig != null) {
                config = lifecycleHandlerRefConfig.getReference();
            }
        }
        return config;
    }

    public void setLifecycleHandlerConfig(LifecycleHandlerConfig lifecycleHandlerConfig) {
        this.lifecycleHandlerConfig = lifecycleHandlerConfig;
    }

    public ConfigProperties getLifecycleHandlerProperties() {
        return lifecycleHandlerProperties;
    }

    public void setLifecycleHandlerRefConfig(LifecycleHandlerRefConfig lifecycleHandlerRefConfig) {
        this.lifecycleHandlerRefConfig = lifecycleHandlerRefConfig;
    }

    public void setLifecycleHandlerName(String name) {
        this.lifecycleHandlerName = name;
    }

    public InvocationHandlerConfig getInvocationHandlerConfig() {
        InvocationHandlerConfig config = null;
        if (invocationHandlerConfig != null) {
            config = invocationHandlerConfig;
        } else {
            if (invocationHandlerRefConfig != null) {
                config = invocationHandlerRefConfig.getReference();
            }
        }
        return config;
    }

    public void setInvocationHandlerConfig(InvocationHandlerConfig invocationHandlerConfig) {
        this.invocationHandlerConfig = invocationHandlerConfig;
    }

    public ConfigProperties getInvocationHandlerProperties() {
        return invocationHandlerProperties;
    }

    public void setInvocationHandlerRefConfig(InvocationHandlerRefConfig invocationHandlerRefConfig) {
        this.invocationHandlerRefConfig = invocationHandlerRefConfig;
    }

    public void setInvocationHandlerName(String name) {
        this.invocationHandlerName = name;
    }

    public void setInterceptorChainName(String name) {
        this.interceptorChainName = name;
    }

    public void setInterceptorChainRefConfig(InterceptorChainRefConfig interceptorChainRefConfig) {
        this.interceptorChainRefConfig = interceptorChainRefConfig;
    }

    public InterceptorChainConfig getInterceptorChainConfig() {
        InterceptorChainConfig config = null;
        if (interceptorChainConfig != null) {
            config = interceptorChainConfig;
        } else {
            if (interceptorChainRefConfig != null) {
                config = interceptorChainRefConfig.getReference();
            }
        }
        return config;
    }

    public void setInterceptorChainConfig(InterceptorChainConfig interceptorChainConfig) {
        this.interceptorChainConfig = interceptorChainConfig;
    }

    public String toString() {
        return new ToStringBuilder(this).append("name", getName()).toString();
    }
}
