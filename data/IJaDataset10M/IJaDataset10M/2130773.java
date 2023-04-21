package java.lang;

import junit.framework.TestCase;

public class PackageTest extends TestCase {

    static String vendor = System.getProperty("java.vm.vendor");

    /**
     *  
     */
    public void test_getImplementationTitle_V() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getImplementationTitle();
        if (s == null) return;
        if (vendor.equals("Sun Microsystems Inc.")) {
            assertEquals("Error1: unexpected title:", "Java Runtime Environment", s);
        } else if (vendor.equals("BEA Systems, Inc.")) {
            assertEquals("Error2: unexpected title:", "...", s);
        } else if (vendor.equals("Apache Software Foundation")) {
            assertEquals("Error3: unexpected title:", "Apache Harmony", s);
        }
    }

    /**
     *  
     */
    public void test_getImplementationVendor_V() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getImplementationVendor();
        if (s == null) return;
        if (vendor.equals("Sun Microsystems Inc.")) {
            assertEquals("Error1: unexpected vendor:", "Sun Microsystems, Inc.", s);
        } else if (vendor.equals("BEA Systems, Inc.")) {
            assertEquals("Error2: unexpected vendor:", "...", s);
        } else if (vendor.equals("Apache Software Foundation")) {
            assertEquals("Error3: unexpected vendor:", "The Apache Software Foundation", s);
        }
    }

    /**
     *  
     */
    public void test_getImplementationVersion_V() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getImplementationVersion();
        if (s == null) return;
        assertTrue("unexpected implementation version: " + s, s.matches("[[0-9]._]+"));
    }

    /**
     *  
     */
    public void test_getName_V() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getName();
        assertEquals("unexpected name", "java.lang", s);
    }

    /**
     *  
     */
    public void test_getPackage_Str_1() {
        assertNull("null expected", Package.getPackage("ABSOLUTELY.UNKNOWN.PACKAGE"));
    }

    /**
     *  
     */
    public void test_getPackage_Str_2() {
        try {
            Package.getPackage((String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException _) {
            return;
        }
    }

    /**
     *  
     */
    public void test_getPackage_Str_3() {
        Package p = Package.getPackage("java.lang");
        if (p == null) return;
        String s = p.getName();
        assertEquals("unexpected package", "java.lang", s);
    }

    /**
     *  
     */
    public void test_getPackages_V() {
        Package ap[] = Package.getPackages();
        for (int i = 0; i < ap.length; i++) {
            if (ap[i].getName().indexOf("java.lang") != -1) return;
        }
        fail("at least java.lang should be returned");
    }

    /**
     *  
     */
    public void test_getSpecificationTitle_V() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getSpecificationTitle();
        if (s == null) return;
        String specName = System.getProperty("java.specification.name");
        assertEquals("unexpected specification title:", specName, s);
    }

    /**
     *  
     */
    public void test_getSpecificationVendor_V() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getSpecificationVendor();
        if (s == null) return;
        assertEquals("unexpected specification vendor:", "Sun Microsystems, Inc.", s);
    }

    /**
     *  
     */
    public void test_getSpecificationVersion_V() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getSpecificationVersion();
        assertNotNull("spec version is null", s);
        if (s == null) return;
        assertTrue("unexpected specification version: " + s, s.matches("([0-9]+?\\.)\\*?[0-9]+?"));
    }

    /**
     *  
     */
    public void test_hashCode_V() {
        Package p1 = java.lang.Character.class.getPackage();
        Package p2 = java.io.File.class.getPackage();
        if (p1 == null || p2 == null) return;
        assertTrue("hash codes should differ", p1.hashCode() != p2.hashCode());
    }

    /**
     *  
     */
    public void test_isCompatibleWith_Str_1() {
        Package p = Package.getPackage("java.lang");
        if (p == null) return;
        try {
            p.isCompatibleWith("");
        } catch (NumberFormatException _) {
        } catch (Throwable e) {
            fail("unexpected error: " + e.toString());
        }
    }

    /**
     *  
     */
    public void test_isCompatibleWith_Str_2() {
        Package p = java.lang.Character.class.getPackage();
        if (p == null) return;
        String s = p.getSpecificationVersion();
        if (s == null) return;
        try {
            assertTrue("should be compatible with its own spec version", p.isCompatibleWith(s));
        } catch (NumberFormatException _) {
            fail("wrong version format");
        }
    }

    /**
     *  
     */
    public void test_isSealed_V() {
        Package p1 = java.lang.Character.class.getPackage();
        Package p2 = java.lang.Void.class.getPackage();
        if (p1 == null || p2 == null) return;
        assertEquals("values should be equal", p1.isSealed(), p2.isSealed());
    }

    /**
     *  
     */
    public void test_isSealed_URL_1() {
        try {
            Package p1 = java.lang.Character.class.getPackage();
            Package p2 = java.lang.Void.class.getPackage();
            if (p1 == null || p2 == null) return;
            java.net.URL url = new java.net.URL("http://intel.com/");
            assertEquals("values should be equal", p1.isSealed(url), p2.isSealed(url));
        } catch (java.net.MalformedURLException _) {
            fail("unexpected MalformedURLException");
        }
    }

    /**
     *  
     */
    public void test_isSealed_URL_2() {
        try {
            Package p = Package.getPackage("java.lang");
            if (p == null) return;
            p.isSealed((java.net.URL) null);
            fail("NullPointerException has not been thrown");
        } catch (NullPointerException _) {
            return;
        }
    }

    /**
     *  
     */
    public void test_toString_V() {
        Package p = PackageTest.class.getPackage();
        if (p == null) return;
        assertTrue("unexpected: package name must be printed: " + p.toString(), p.toString().indexOf("package " + p.getName()) != -1);
    }
}
