package org.extwind.osgi.tapestry.internal.proxy.pageload;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.internal.structure.ComponentPageElement;

/**
 * @author Donf Yang
 *
 */
public class ProxyParameterBinderImpl implements ProxyParameterBinder {

    private final String mixinId;

    private final String parameterName;

    private final String defaultBindingPrefix;

    protected ProxyParameterBinderImpl(String mixinId, String parameterName, String defaultBindingPrefix) {
        this.mixinId = mixinId;
        this.parameterName = parameterName;
        this.defaultBindingPrefix = defaultBindingPrefix;
    }

    public void bind(ComponentPageElement element, Binding binding) {
        if (mixinId == null) {
            element.bindParameter(parameterName, binding);
            return;
        }
        element.bindMixinParameter(mixinId, parameterName, binding);
    }

    public String getDefaultBindingPrefix(String metaDefault) {
        return defaultBindingPrefix != null ? defaultBindingPrefix : metaDefault;
    }
}
