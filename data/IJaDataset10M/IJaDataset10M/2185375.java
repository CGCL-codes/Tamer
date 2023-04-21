package com.codemonster.surinam.core;

import com.codemonster.surinam.export.service.VersionedProviderImplementationName;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Service implementations are versioned. This is based on the expectation that implementations
 * evolve over time within
 */
public class VersionedServiceKeyTest {

    String implName = "com.codemonster.Foo";

    String implVersion = "1.0";

    /**
     *
     */
    @org.junit.Test
    public void checkNameAndVersion() {
        VersionedProviderImplementationName impl = new VersionedProviderImplementationName(implName, implVersion);
        assertTrue("The implNames should match.", implName.equals(impl.getName()));
        assertTrue("The implVersions should match.", implVersion.equals(impl.getVersion()));
    }

    /**
     */
    @org.junit.Test
    public void checkFullVersionedName() {
        VersionedProviderImplementationName impl = new VersionedProviderImplementationName(implName, implVersion);
        String fullVName = "com.codemonster.Foo(1.0)";
        assertTrue("The full, versioned name should match.", fullVName.equals(impl.toString()));
    }

    /**
     * Here, we will take two identical, but different name object instances and execute the 'equals' method.
     */
    @org.junit.Test
    public void checkEquals() {
        VersionedProviderImplementationName impl1 = new VersionedProviderImplementationName(implName, implVersion);
        VersionedProviderImplementationName impl2 = new VersionedProviderImplementationName(implName, implVersion);
        assertFalse("The full, versioned name should not match 'null'.", impl1.equals(null));
        assertFalse("The full, versioned name should not match the wrong object type.", impl1.equals(new Integer(5)));
        assertTrue("The full, versioned name should match.", impl1.equals(impl1));
        assertTrue("The full, versioned name should match.", impl1.equals(impl2));
    }

    @org.junit.Test
    public void checkHash() {
        VersionedProviderImplementationName impl1 = new VersionedProviderImplementationName(implName, implVersion);
        VersionedProviderImplementationName impl2 = new VersionedProviderImplementationName(implName, implVersion);
        assertTrue("The two hash codes should match.", impl1.hashCode() == impl2.hashCode());
        String implVersion = "2.4";
        impl1.setVersionString(implVersion);
        assertTrue("The two hash codes should NOT match.", impl1.hashCode() != impl2.hashCode());
    }

    /**
     */
    @org.junit.Test
    public void setAndGetTheName() {
        String newName = "com.codemonster.newFoo";
        VersionedProviderImplementationName impl = new VersionedProviderImplementationName(implName, implVersion);
        impl.setName(newName);
        assertTrue("The name should match the new name.", newName.equals(impl.getName()));
    }

    /**
     */
    @org.junit.Test
    public void setAndGetTheVersion() {
        String implVersion = "2.4";
        VersionedProviderImplementationName impl = new VersionedProviderImplementationName(implName, implVersion);
        impl.setVersionString(implVersion);
        assertTrue("The version should match the new version.", implVersion.equals(impl.getVersion()));
    }

    @org.junit.Test
    public void compareVersionedServices() {
        VersionedProviderImplementationName impl1 = new VersionedProviderImplementationName(implName, implVersion);
        VersionedProviderImplementationName impl2 = new VersionedProviderImplementationName(implName, implVersion);
        assertTrue("The two services should be equal.", 0 == impl1.compareTo(impl2));
        String implVersion = "2.4";
        impl1.setVersionString(implVersion);
        assertTrue("The services should be equal.", 1 == impl1.compareTo(impl2));
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new JUnit4TestAdapter(VersionedServiceKeyTest.class);
    }
}
