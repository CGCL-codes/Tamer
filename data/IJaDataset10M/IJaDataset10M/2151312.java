package org.cyberaide.gridshell.commands.workflow.test;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileNotFoundException;
import org.cyberaide.gridshell.commands.GSSystem;
import org.cyberaide.gridshell.commands.object.GSBasicObject;
import org.cyberaide.gridshell.commands.object.GSObject;
import org.cyberaide.gridshell.commands.util.ObjectLoader;
import org.cyberaide.gridshell.commands.workflow.Asynchronous;
import org.cyberaide.gridshell.commands.workflow.Dependency;
import org.junit.Before;
import org.junit.Test;

public class AsynchTest {

    private static final String name = "testasynch";

    @Before
    public void setUp() throws Exception {
        GSSystem.setupClient(new File("").getAbsolutePath(), null, null);
    }

    /**
	 * Basic test to test operations
	 */
    @Test
    public void testExecute() {
        String obj = "awesome";
        String time = "06/16/198613:03:45";
        Asynchronous dep = new Asynchronous("-name " + name + " -t " + time + " -o " + obj);
        dep.execute();
        GSBasicObject wf = null;
        wf = (GSBasicObject) ObjectLoader.loadObject(name, null);
        assertEquals(wf.getName(), name);
        assertEquals(wf.getAttrib("time"), time);
        assertEquals(wf.getAttrib("obj"), obj);
        wf.delete();
    }
}
