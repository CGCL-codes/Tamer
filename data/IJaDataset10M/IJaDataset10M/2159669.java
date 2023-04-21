package org.cyberaide.gridshell.commands.object.test;

import static org.junit.Assert.assertEquals;
import java.io.File;
import org.cyberaide.gridshell.commands.GSSystem;
import org.cyberaide.gridshell.commands.object.Create;
import org.cyberaide.gridshell.commands.object.GSBasicObject;
import org.cyberaide.gridshell.commands.object.Modify;
import org.cyberaide.gridshell.commands.util.ObjectLoader;
import org.junit.Before;
import org.junit.Test;

public class ModifyTest {

    @Before
    public void setUp() throws Exception {
        GSSystem.setupClient(new File("").getAbsolutePath(), null, null);
    }

    /**
	 * Basic test to test operations
	 */
    @Test
    public void modificationTest() {
        String name = "testjob";
        String newTaskName = "HelloWorld";
        Create create = new Create("-type job -name " + name + " -task sleep -resource resource1");
        create.execute();
        Modify mod = new Modify(name + " task " + newTaskName);
        mod.execute();
        GSBasicObject obj = (GSBasicObject) ObjectLoader.loadObject(name, null);
        assertEquals(obj.getAttrib("task"), newTaskName);
        obj.delete();
    }
}
