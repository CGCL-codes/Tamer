package net.sf.insim4j.test.osgi;

import net.sf.insim4j.test.GeneralTest;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class GeneralTestActivator implements BundleActivator {

    private GeneralTest fTest;

    @Override
    public void start(final BundleContext context) throws Exception {
        fTest = new GeneralTest();
        System.out.println("Starting GeneralTest");
        fTest.startTest();
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        System.out.println("Stopping GeneralTest");
        fTest.stopTest();
    }
}
