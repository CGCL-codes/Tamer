package org.aspectme.cldc.reflect.model.java;

import org.aspectme.cldc.reflect.model.ReflectClass;
import org.aspectme.cldc.reflect.model.java.JavaClassLoader;
import org.aspectme.instrument.ClassPath;
import junit.framework.TestCase;

public class JavaClassLoaderTest extends TestCase {

    public void testPrimitives() throws Exception {
        ClassPath cp = ClassPath.parseClassPath(System.getProperty("java.class.path"));
        JavaClassLoader cl = new JavaClassLoader(cp);
        ReflectClass type = cl.loadClass("int");
        assertEquals("Wrong type name", "int", type.getName());
    }
}
