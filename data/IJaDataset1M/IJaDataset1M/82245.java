package com.googlecode.yoohoo.xmppcore.stanza;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import com.googlecode.yoohoo.utils.FilterCreator;
import com.googlecode.yoohoo.xmppcore.connection.IConnectionContext;

public class StanzaHandlerTracker<K extends IConnectionContext> extends ServiceTracker {

    public StanzaHandlerTracker(BundleContext context, IStanzasHandler<K> compositeStanzaHandler, String stanzaHandlerType) {
        super(context, createFilter(context, stanzaHandlerType), new StanzaHandlerTrackerCustomizer<K>(context, compositeStanzaHandler));
    }

    private static Filter createFilter(BundleContext context, String stanzaHandlerType) {
        try {
            String filter = FilterCreator.and(FilterCreator.equal(Constants.OBJECTCLASS, IStanzaHandlerFactory.class.getName()), FilterCreator.equal(IStanzaHandler.KEY_STANZA_HANDLER_TYPE, stanzaHandlerType));
            return context.createFilter(filter);
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("Can't get stanza handler type filter.");
        }
    }

    private static class StanzaHandlerTrackerCustomizer<K extends IConnectionContext> implements ServiceTrackerCustomizer {

        private BundleContext context;

        private IStanzasHandler<K> compositeStanzaHandler;

        public StanzaHandlerTrackerCustomizer(BundleContext context, IStanzasHandler<K> compositeStanzaHandler) {
            this.context = context;
            this.compositeStanzaHandler = compositeStanzaHandler;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object addingService(ServiceReference reference) {
            IStanzaHandlerFactory<K, ?> handlerFactory = (IStanzaHandlerFactory<K, ?>) context.getService(reference);
            if (handlerFactory != null) {
                compositeStanzaHandler.registerHandler(handlerFactory);
            }
            return handlerFactory;
        }

        @Override
        public void modifiedService(ServiceReference reference, Object service) {
        }

        @SuppressWarnings("unchecked")
        @Override
        public void removedService(ServiceReference reference, Object service) {
            if (service == null) return;
            IStanzaHandlerFactory<K, ?> handlerFactory = (IStanzaHandlerFactory<K, ?>) service;
            compositeStanzaHandler.unregisterHandler(handlerFactory);
        }
    }
}
