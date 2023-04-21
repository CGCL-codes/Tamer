package com.manning.sdmia.ch04;

import junit.framework.Assert;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.BundleContextAware;

/**
 * @author acogoluegnes
 * 
 */
public class BundleContextAwareTest extends AbstractTest {

    /** just for the test to automatically adjust the generated manifest */
    private BundleContextAware dummy;

    public void testBundleContextAwareTest() throws Exception {
        OsgiAddict osgiAddict = (OsgiAddict) applicationContext.getBean("osgiAddict");
        Assert.assertNotNull("bundle context is null, it should have been automatically injected", osgiAddict.getBundleContext());
        Assert.assertTrue("osgi application context should contain a bundleContext bean", applicationContext.containsBean("bundleContext"));
        Assert.assertTrue("bundleContext bean is not a bundle context!", applicationContext.getBean("bundleContext") instanceof BundleContext);
        PojoOsgiAddict pojoOsgiAddict = (PojoOsgiAddict) applicationContext.getBean("pojoOsgiAddict");
        Assert.assertNotNull("bundle context is null, it should have been injected", pojoOsgiAddict.getBundleContext());
    }

    @Override
    protected String[] getConfigPaths() {
        return new String[] { "/com/manning/sdmia/ch04/BundleContextAwareTest-context.xml" };
    }
}
