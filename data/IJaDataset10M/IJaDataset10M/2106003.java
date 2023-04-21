package com.googlecode.yoohoo.xmppcore.parsing;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import com.googlecode.yoohoo.utils.FilterCreator;
import com.googlecode.yoohoo.xmppcore.protocol.parsing.IXmppParsingFactory;

public class ParsersTracker extends ServiceTracker {

    public ParsersTracker(BundleContext context, IXmppParsingFactory parsingFactory, String factoryName) {
        super(context, createFilter(context, factoryName), new ParsersTrackerCustomizer(context, parsingFactory));
    }

    private static Filter createFilter(BundleContext context, String factoryName) {
        String filter = FilterCreator.and(FilterCreator.equal(Constants.OBJECTCLASS, ParserContribution.class.getName()), FilterCreator.equal(com.googlecode.yoohoo.utils.Constants.KEY_SERVICE_INSTANCE_NAME, factoryName));
        try {
            return context.createFilter(filter);
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("Can't get parser filter.");
        }
    }

    private static class ParsersTrackerCustomizer implements ServiceTrackerCustomizer {

        private BundleContext context;

        private IXmppParsingFactory parsingFactory;

        public ParsersTrackerCustomizer(BundleContext context, IXmppParsingFactory parsingFactory) {
            this.context = context;
            this.parsingFactory = parsingFactory;
        }

        @Override
        public Object addingService(ServiceReference reference) {
            ParserContribution contribution = (ParserContribution) context.getService(reference);
            if (contribution != null) {
                parsingFactory.registerParser(contribution.getParserFactory(), contribution.getRelations());
            }
            return contribution;
        }

        @Override
        public void modifiedService(ServiceReference reference, Object service) {
        }

        @Override
        public void removedService(ServiceReference reference, Object service) {
            if (service == null) return;
            ParserContribution contribution = (ParserContribution) service;
            parsingFactory.unregisterParser(contribution.getParserFactory());
        }
    }
}
