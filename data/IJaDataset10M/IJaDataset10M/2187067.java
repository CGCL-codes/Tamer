package org.ops4j.peaberry.activation.tests;

import static junit.framework.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.swissbox.tinybundles.core.TinyBundles.*;
import static org.ops4j.peaberry.activation.Constants.*;
import static org.ops4j.peaberry.activation.invocations.util.Matchers.*;
import static org.ops4j.peaberry.activation.tests.TinyBundleProvisionOption.*;
import static org.osgi.framework.Constants.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.peaberry.activation.examples.export.ExportRoot;
import org.ops4j.peaberry.activation.examples.singleton.SingletonRoot;
import org.ops4j.peaberry.activation.invocations.util.InvocationTracking;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;

/**
 * @author rinsvind@gmail.com (Todor Boev)
 */
@RunWith(JUnit4TestRunner.class)
public class ActivtionTest extends InvocationTracking {

    private static final String ACTIVATION_MODULE = "org.ops4j.peaberry.activation";

    private static final String EXPORT_MODULE = packageOf(org.ops4j.peaberry.activation.examples.export.Config.class);

    private static final String SINGLETON_MODULE = packageOf(org.ops4j.peaberry.activation.examples.singleton.Config.class);

    @Configuration(extend = PeaberryConfiguration.class)
    public static Option[] configure() {
        return options(tinyBundle().set(BUNDLE_SYMBOLICNAME, EXPORT_MODULE).set(EXPORT_PACKAGE, EXPORT_MODULE).set(BUNDLE_MODULE, org.ops4j.peaberry.activation.examples.export.Config.class.getName()).add(org.ops4j.peaberry.activation.examples.export.Config.class).add(org.ops4j.peaberry.activation.examples.export.ExportRoot.class).add(org.ops4j.peaberry.activation.examples.export.ExportRootImpl.class).build(withBnd()).noStart(), tinyBundle().set(BUNDLE_SYMBOLICNAME, SINGLETON_MODULE).set(EXPORT_PACKAGE, SINGLETON_MODULE).set(BUNDLE_MODULE, org.ops4j.peaberry.activation.examples.singleton.Config.class.getName()).add(org.ops4j.peaberry.activation.examples.singleton.Config.class).add(org.ops4j.peaberry.activation.examples.singleton.SingletonRoot.class).build(withBnd()).noStart());
    }

    private Bundle exportRoot;

    private Bundle singletonRoot;

    private Bundle activation;

    @Before
    public void setup() throws BundleException {
        exportRoot = getBundle(EXPORT_MODULE);
        singletonRoot = getBundle(SINGLETON_MODULE);
        activation = getBundle(ACTIVATION_MODULE);
    }

    @Test
    public void testExportRoot() throws BundleException {
        activation.start();
        assertNull(getReference(ExportRoot.class));
        exportRoot.start();
        assertEquals(exportRoot, getReference(ExportRoot.class).getBundle());
    }

    @Test
    public void testSingletonRoot() throws BundleException {
        activation.start();
        singletonRoot.start();
        assertInvoked(type(SingletonRoot.class), method("start"));
        singletonRoot.stop();
        assertInvoked(type(SingletonRoot.class), method("stop"));
    }

    @Test
    public void testExtenderRestart() throws BundleException, InvalidSyntaxException {
        exportRoot.start();
        assertNull(getReference(ExportRoot.class));
        singletonRoot.start();
        assertNotInvoked(type(SingletonRoot.class), method("start").or(method("stop")));
        activation.start();
        assertEquals(1, getReferenceList(ExportRoot.class).length);
        assertInvoked(type(SingletonRoot.class), method("start"));
        activation.stop();
        assertNull(getReference(ExportRoot.class));
        assertInvoked(type(SingletonRoot.class), method("stop"));
        resetInvocations();
        activation.start();
        assertEquals(1, getReferenceList(ExportRoot.class).length);
        assertInvoked(type(SingletonRoot.class), method("start"));
    }
}
