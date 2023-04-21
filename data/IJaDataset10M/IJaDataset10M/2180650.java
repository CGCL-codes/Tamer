package org.kommando.shelf;

import org.kommando.core.action.Action;
import org.kommando.core.catalog.Catalog;
import org.kommando.core.catalog.ObjectSource;
import org.kommando.util.osgi.ServiceUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Peter De Bruycker
 */
public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        Catalog catalog = ServiceUtils.getService(context, Catalog.class);
        Shelf shelf = new Shelf(catalog);
        context.registerService(ObjectSource.class.getName(), new ShelfObjectSource(shelf), null);
        context.registerService(Action.class.getName(), new ShowShelfAction(shelf), null);
        context.registerService(Action.class.getName(), new PutOnShelfAction(shelf), null);
    }

    public void stop(BundleContext context) throws Exception {
    }
}
