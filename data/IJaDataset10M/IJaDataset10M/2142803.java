package com.hp.hpl.mars.portal.widget;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.mars.portal.vocabulary.PORTAL;

/**
 *
 */
public class DefaultBadPageSetWidget extends Widget implements BadPageSetWidget {

    public static final String DEFAULT_VIEWNAME = "com/hp/hpl/mars/portal/widget/system/DefaultBadPageSetView";

    /**
	 * TODO bwm
	 * This resource is used temporarily to initialize the default widget
	 * in the StandardController. Not sure, if it fits into the overall
	 * architecture (esp., if we decide to have a single default configuration)
	 */
    public static final Resource RDEFAULT;

    static {
        Model model = ModelFactory.createDefaultModel();
        RDEFAULT = model.createResource(PORTAL.DefaultBadPageSetWidgetInstance);
        RDEFAULT.addProperty(RDF.type, PORTAL.DefaultBadPageSetWidget);
    }

    public DefaultBadPageSetWidget() {
        this.setViewName(DEFAULT_VIEWNAME);
    }

    public void setBadPageSetId(String pageSetId) {
        addObject("pageSetId", pageSetId);
    }
}
