package ca.uhn.hl7v2.llp.tests;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.junit.MavenConfiguredJUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@RunWith(MavenConfiguredJUnit4TestRunner.class)
public class paxTest {

    @Inject
    BundleContext bundleContext;

    @Before
    public void BeforeTheTest() {
        System.out.println("*****************************BeforeTheTest******************************************");
    }

    @After
    public void AfterTheTest() {
        System.out.println("*****************************AfterTheTest******************************************");
    }

    @Test
    public void listBundles() throws IOException {
        for (Bundle b : bundleContext.getBundles()) {
            System.out.println("Bundle " + b.getBundleId() + " : " + b.getSymbolicName());
        }
    }
}
