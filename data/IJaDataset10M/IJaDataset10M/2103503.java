package org.mortbay.jetty.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.mortbay.jetty.annotations.resources.ResourceA;
import org.mortbay.jetty.annotations.resources.ResourceB;
import org.mortbay.jetty.plus.annotation.InjectionCollection;
import org.mortbay.jetty.plus.annotation.LifeCycleCallbackCollection;
import org.mortbay.jetty.plus.annotation.RunAsCollection;
import org.mortbay.jetty.plus.naming.NamingEntry;
import junit.framework.TestCase;

/**
 * TestAnnotationInheritance
 *
 *
 */
public class TestAnnotationInheritance extends TestCase {

    public void testInheritance() throws Exception {
        NamingEntry.setScope(NamingEntry.SCOPE_LOCAL);
        AnnotationParser processor = new AnnotationParser();
        AnnotationCollection collection = processor.processClass(ClassB.class);
        List classes = collection.getClasses();
        assertEquals(2, classes.size());
        List methods = collection.getMethods();
        assertTrue(methods != null);
        assertFalse(methods.isEmpty());
        assertEquals(methods.size(), 4);
        Method m = ClassB.class.getDeclaredMethod("a", new Class[] {});
        assertTrue(methods.indexOf(m) >= 0);
        Sample s = (Sample) m.getAnnotation(Sample.class);
        assertEquals(51, s.value());
        m = ClassA.class.getDeclaredMethod("a", new Class[] {});
        assertTrue(methods.indexOf(m) < 0);
        m = ClassA.class.getDeclaredMethod("b", new Class[] {});
        assertTrue(methods.indexOf(m) >= 0);
        m = ClassB.class.getDeclaredMethod("c", new Class[] {});
        assertTrue(methods.indexOf(m) >= 0);
        m = ClassA.class.getDeclaredMethod("c", new Class[] {});
        assertTrue(methods.indexOf(m) < 0);
        m = ClassA.class.getDeclaredMethod("d", new Class[] {});
        assertTrue(methods.indexOf(m) >= 0);
        List fields = collection.getFields();
        assertFalse(fields.isEmpty());
        assertEquals(1, fields.size());
        Field f = ClassA.class.getDeclaredField("m");
        assertTrue(fields.indexOf(f) >= 0);
        NamingEntry.setScope(NamingEntry.SCOPE_GLOBAL);
    }

    public void testResourceAnnotations() throws Exception {
        InitialContext ic = new InitialContext();
        Context comp = (Context) ic.lookup("java:comp");
        Context env = comp.createSubcontext("env");
        org.mortbay.jetty.plus.naming.EnvEntry resourceA = new org.mortbay.jetty.plus.naming.EnvEntry("resA", new Integer(1000));
        org.mortbay.jetty.plus.naming.EnvEntry resourceB = new org.mortbay.jetty.plus.naming.EnvEntry("resB", new Integer(2000));
        NamingEntry.setScope(NamingEntry.SCOPE_LOCAL);
        AnnotationParser processor = new AnnotationParser();
        processor.processClass(ResourceB.class);
        InjectionCollection injections = new InjectionCollection();
        LifeCycleCallbackCollection callbacks = new LifeCycleCallbackCollection();
        RunAsCollection runAses = new RunAsCollection();
        AnnotationCollection collection = processor.processClass(ResourceB.class);
        assertEquals(1, collection.getClasses().size());
        assertEquals(3, collection.getMethods().size());
        assertEquals(6, collection.getFields().size());
        processor.parseAnnotations(ResourceB.class, runAses, injections, callbacks);
        assertEquals(resourceB.getObjectToBind(), env.lookup("myf"));
        assertEquals(resourceA.getObjectToBind(), env.lookup("mye"));
        assertEquals(resourceA.getObjectToBind(), env.lookup("resA"));
        assertEquals(resourceA.getObjectToBind(), env.lookup("org.mortbay.jetty.annotations.resources.ResourceA/g"));
        assertEquals(resourceA.getObjectToBind(), env.lookup("org.mortbay.jetty.annotations.resources.ResourceA/h"));
        assertEquals(resourceB.getObjectToBind(), env.lookup("org.mortbay.jetty.annotations.resources.ResourceB/f"));
        assertEquals(resourceB.getObjectToBind(), env.lookup("org.mortbay.jetty.annotations.resources.ResourceA/n"));
        assertNotNull(injections);
        List fieldInjections = injections.getFieldInjections(ResourceB.class);
        assertNotNull(fieldInjections);
        assertEquals(5, fieldInjections.size());
        List methodInjections = injections.getMethodInjections(ResourceB.class);
        assertNotNull(methodInjections);
        assertEquals(3, methodInjections.size());
    }
}
