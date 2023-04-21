package org.kommando.web;

import org.kommando.core.action.Action;
import org.kommando.core.catalog.ObjectSource;
import org.kommando.web.actions.OpenAction;
import org.kommando.web.actions.OpenURLAction;
import org.kommando.web.objectsource.WebObjectSource;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        context.registerService(ObjectSource.class.getName(), new WebObjectSource(), null);
        context.registerService(Action.class.getName(), new OpenAction(), null);
        context.registerService(Action.class.getName(), new OpenURLAction(), null);
    }

    public void stop(BundleContext context) throws Exception {
    }
}
