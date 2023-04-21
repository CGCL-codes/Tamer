package net.sf.jimo.modules.tests.impl;

import java.util.Dictionary;
import java.util.Hashtable;
import junit.framework.Test;
import net.sf.jimo.api.Application;
import net.sf.jimo.api.CommandContext;
import net.sf.jimo.api.CommandHandler;
import net.sf.jimo.api.JIMOConstants;
import net.sf.jimo.modules.tests.api.TestService;
import net.sf.jimo.modules.tests.impl.junit.AllTests;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;

public class TestBundleActivator implements BundleActivator {

    public static final String BUNDLE_NAME = "jimo.tests";

    private static final String COMMANDNAME = "test";

    private static BundleContext bundleContext;

    public void start(final BundleContext context) throws Exception {
        bundleContext = context;
        Object service = new TestService() {

            public Test getTest() {
                return AllTests.suite();
            }
        };
        context.registerService(TestService.class.getName(), service, null);
        Dictionary appProperties = new Hashtable();
        appProperties.put(JIMOConstants.SERVICE_APPID, TestApplication.class.getName());
        context.registerService(new String[] { TestApplication.class.getName(), Application.class.getName() }, new TestApplication(), appProperties);
        Dictionary commandProperties = new Hashtable();
        commandProperties.put(JIMOConstants.SERVICE_COMMANDNAME, COMMANDNAME);
        context.registerService(CommandHandler.class.getName(), new CommandHandler() {

            public void onCommand(String command, CommandContext context) {
                try {
                    TestApplication.runAllTests();
                } catch (InvalidSyntaxException e) {
                    context.error(e);
                }
            }
        }, commandProperties);
    }

    public void stop(BundleContext context) throws Exception {
    }

    public static BundleContext getBundleContext() {
        return bundleContext;
    }
}
