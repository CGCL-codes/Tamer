package de.creatronix.artist3k.jbpl;

import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import junit.framework.TestCase;

public class SimpleProcessTest extends TestCase {

    public void testSimpleProcess() throws Exception {
        ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource("de/creatronix/artist3k/jbpl/processdefinition.xml");
        assertNotNull("Definition should not be null", processDefinition);
        ProcessInstance instance = new ProcessInstance(processDefinition);
        assertEquals("Instance is in start state", instance.getRootToken().getNode().getName(), "eventEnteredInNextstep");
        instance.signal();
        assertEquals("Instance is in first state", instance.getRootToken().getNode().getName(), "notifyAllBookers");
        instance.signal();
        assertEquals("Instance is in end state", instance.getRootToken().getNode().getName(), "end");
        assertTrue("Instance has ended", instance.hasEnded());
    }
}
