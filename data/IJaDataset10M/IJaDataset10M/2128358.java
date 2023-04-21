package org.apache.harmony.lang.generics;

import junit.framework.TestCase;

public class SpecialD extends TestCase {

    public void test() throws Throwable {
        new SpecialC();
        if (((SpecialClassLoader) this.getClass().getClassLoader()).checkFind("org.apache.harmony.lang.generics.SpecialC") == null) {
            fail("FAILED: " + this.getClass().getClassLoader().getClass() + " wasn't marked as initiating classloader for SpecialC");
        }
        ClassLoaderTest.flag++;
        try {
            ((SpecialClassLoader) this.getClass().getClassLoader()).loadClass("");
            fail("FAILED: LinkageError wasn't thrown");
        } catch (LinkageError err) {
        }
    }
}
