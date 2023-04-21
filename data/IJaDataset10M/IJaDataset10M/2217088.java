package org.jactr.eclipse.core.bundles;

import org.eclipse.core.runtime.Plugin;

public class BundlesActivator extends Plugin {

    public static final String PLUGIN_ID = BundlesActivator.class.getName();

    private static BundlesActivator _default;

    public BundlesActivator() {
        super();
        _default = this;
    }

    public static BundlesActivator getDefault() {
        return _default;
    }
}
