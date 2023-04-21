package org.springframework.richclient.samples.swingdocking.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.ApplicationLauncher;

/**
 * This application provides a very simple introduction to the Spring Richclient platform. It is intended to highlight
 * the common models for working with the platform (including configuration and runtime access to platform services) and
 * how to organize the basic pieces of an application using the platform.
 * <p>
 * This sample provides an implementation of a&nbsp;<em>trivial</em> address book. Trust me when I say trivial :-) It
 * has no persistence, no security, and no complex business logic. The focus of the sample is how to work with the
 * Spring Rich platform, not about how to build a great address book.
 * <p>
 * Other samples will introduce more complex topics like user management (security), alternate page/view layouts,
 * complex data binding, remoting, etc.
 * <p>
 * The Spring Rich platform relies on the <a href="http://www.springframework.org/">Spring</a> project to manage the
 * application context with all the associated benefits it offers.
 * <p>
 * A start at the Spring Rich Client documentation can be found on the <a
 * href="http://opensource.atlassian.com/confluence/spring/display/RCP/Home">wiki</a>.
 * </p>
 * @author Larry Streepy
 * @see The <a href="http://www.springframework.org/">Spring project</a>
 * @see The <a href="http://opensource.atlassian.com/confluence/spring/display/RCP/Home">Spring Rich Wiki</a>
 */
public class SimpleSwingDockingApp {

    private static final Log logger = LogFactory.getLog(SimpleSwingDockingApp.class);

    /**
	 * Main routine for the simple sample application.
	 * @param args
	 */
    public static void main(String[] args) {
        logger.info("SimpleSwingDockingApp starting up");
        String startupContextPath = "/org/springframework/richclient/samples/simple/ctx" + "/richclient-startup-context.xml";
        String richclientApplicationContextPath = "/org/springframework/richclient/samples/swingdocking/ctx" + "/richclient-application-context.xml";
        try {
            new ApplicationLauncher(startupContextPath, new String[] { richclientApplicationContextPath });
        } catch (RuntimeException e) {
            logger.error("RuntimeException during startup", e);
        }
    }
}
