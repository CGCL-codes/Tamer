package org.codehaus.groovy.grails.plugins.quartz;

import groovy.lang.GroovyClassLoader;
import junit.framework.TestCase;
import org.codehaus.groovy.grails.commons.ArtefactHandler;

/**
 * @author Marc Palmer
 * @since 22-Feb-2007
 */
public class TaskArtefactHandlerTests extends TestCase {

    public void testIsTaskClass() throws Exception {
        GroovyClassLoader gcl = new GroovyClassLoader();
        Class c = gcl.parseClass("class TestJob { def execute() { }}\n");
        ArtefactHandler handler = new TaskArtefactHandler();
        assertTrue(handler.isArtefact(c));
    }
}
