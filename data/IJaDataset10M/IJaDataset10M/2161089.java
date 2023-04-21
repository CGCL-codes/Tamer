package net.sf.epfe.core.test;

import static org.junit.Assert.*;
import net.sf.epfe.utils.LocaleBundleHelper;
import org.junit.Test;

public class TestBundleFind {

    @Test
    public void testGetBundleName() {
        assertEquals("test", LocaleBundleHelper.generateBundleDescriptor("test_de_DE_Du.properties").getName());
        assertEquals("test", LocaleBundleHelper.generateBundleDescriptor("test_de_DE.properties").getName());
        assertEquals("test", LocaleBundleHelper.generateBundleDescriptor("test_de.properties").getName());
        assertEquals("test", LocaleBundleHelper.generateBundleDescriptor("test.properties").getName());
        assertEquals("test_de", LocaleBundleHelper.generateBundleDescriptor("test_de_de_DE_Du.properties").getName());
    }

    @Test
    public void testGenerateBundleDescriptor() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindFiles() {
        fail("Not yet implemented");
    }
}
