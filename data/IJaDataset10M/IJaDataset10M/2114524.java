package org.phenoscape.orb.application;

import org.apache.log4j.Logger;
import org.phenoscape.orb.resources.OrbTermRequestHomepageResource;
import org.phenoscape.orb.resources.TermListResource;
import org.phenoscape.orb.resources.TermResource;
import org.phenoscape.orb.testing.testing;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import freemarker.template.Configuration;

public class ORBApplication extends Application {

    public static final String FREEMARKER_CONFIG_CONTEXT_KEY = "freemarkerConfig";

    public ORBApplication(Context context) {
        super(context);
    }

    @Override
    public synchronized Restlet getInboundRoot() {
        this.initializeFreemarkerConfig();
        final Router router = new Router(this.getContext());
        router.attach("/terms/", TermListResource.class);
        router.attach("/terms/{term_id}", TermResource.class);
        router.attach("", OrbTermRequestHomepageResource.class);
        router.attach("/testing/", testing.class);
        return router;
    }

    private void initializeFreemarkerConfig() {
        final Configuration freemarker = new Configuration();
        freemarker.setClassForTemplateLoading(this.getClass(), "templates");
        freemarker.setDefaultEncoding("utf-8");
        freemarker.setURLEscapingCharset("utf-8");
        this.getContext().getAttributes().put(FREEMARKER_CONFIG_CONTEXT_KEY, freemarker);
    }

    private Logger log() {
        return Logger.getLogger(this.getClass());
    }
}
