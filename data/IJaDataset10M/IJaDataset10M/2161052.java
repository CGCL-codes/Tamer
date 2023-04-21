package org.jowidgets.impl.toolkit;

import java.util.Iterator;
import java.util.ServiceLoader;
import org.jowidgets.api.toolkit.IToolkit;
import org.jowidgets.api.toolkit.IToolkitProvider;
import org.jowidgets.spi.IWidgetsServiceProvider;

public class DefaultToolkitProvider implements IToolkitProvider {

    private final IToolkit toolkit;

    public DefaultToolkitProvider() {
        final ServiceLoader<IWidgetsServiceProvider> widgetServiceLoader = ServiceLoader.load(IWidgetsServiceProvider.class);
        final Iterator<IWidgetsServiceProvider> iterator = widgetServiceLoader.iterator();
        if (iterator.hasNext()) {
            this.toolkit = new DefaultToolkit(iterator.next());
            if (iterator.hasNext()) {
                throw new IllegalStateException("More than one implementation found for '" + IWidgetsServiceProvider.class.getName() + "'");
            }
        } else {
            throw new IllegalStateException("No implementation found for '" + IWidgetsServiceProvider.class.getName() + "'");
        }
    }

    public DefaultToolkitProvider(final IWidgetsServiceProvider toolkitSpi) {
        this.toolkit = new DefaultToolkit(toolkitSpi);
    }

    @Override
    public IToolkit get() {
        return toolkit;
    }
}
