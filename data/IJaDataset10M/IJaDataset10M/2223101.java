package org.codehaus.groovy.grails.commons;

import groovy.lang.GroovyClassLoader;
import junit.framework.TestCase;

/**
 * 
 * 
 * @author Steven Devijver
 * @since Jul 2, 2005
 */
public class GrailsClassTests extends TestCase {

    public GrailsClassTests() {
        super();
    }

    public GrailsClassTests(String arg0) {
        super(arg0);
    }

    public void testAbstractGrailsClassNoPackage() throws Exception {
        GroovyClassLoader cl = new GroovyClassLoader();
        Class clazz = cl.parseClass("class TestService { }");
        GrailsClass grailsClass = new AbstractGrailsClass(clazz, "Service") {
        };
        assertEquals("TestService", clazz.getName());
        assertEquals("Test", grailsClass.getName());
        assertEquals("TestService", grailsClass.getFullName());
        assertNotNull(grailsClass.newInstance());
    }

    public void testAbstractGrailsClassPackage() throws Exception {
        GroovyClassLoader cl = new GroovyClassLoader();
        Class clazz = cl.parseClass("package test.casey; class TestService { }");
        GrailsClass grailsClass = new AbstractGrailsClass(clazz, "Service") {
        };
        assertEquals("test.casey.TestService", clazz.getName());
        assertEquals("Test", grailsClass.getName());
        assertEquals("test.casey.TestService", grailsClass.getFullName());
        assertNotNull(grailsClass.newInstance());
    }

    public void testGrailsClassNonPublicConstructor() throws Exception {
        GroovyClassLoader cl = new GroovyClassLoader();
        Class clazz = cl.parseClass("class ProtectedConstructor { protected ProtectedConstructor(){}}");
        GrailsClass grailsClass = new AbstractGrailsClass(clazz, "ProtectedConstructor") {
        };
        assertNotNull(grailsClass.newInstance());
    }
}
