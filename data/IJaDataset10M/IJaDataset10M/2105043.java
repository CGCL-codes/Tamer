package net.ontopia.topicmaps.webed.impl.utils;

import javax.servlet.ServletContext;
import net.ontopia.topicmaps.webed.impl.basic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Keeps track with registering the action registry object
 * in the web application scope.
 */
public class ActionConfigRegistrator implements ConfigurationObserverIF {

    private static Logger log = LoggerFactory.getLogger(ActionConfigRegistrator.class.getName());

    protected ServletContext ctxt;

    public ActionConfigRegistrator(ServletContext ctxt) {
        this.ctxt = ctxt;
    }

    public void configurationChanged(Object configuration) {
        ctxt.removeAttribute(Constants.AA_REGISTRY);
        log.info("setting action registry in application scope.");
        ctxt.setAttribute(Constants.AA_REGISTRY, configuration);
    }
}
