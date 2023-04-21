package org.cyberaide.gridshell.commands.object.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import org.cyberaide.gridshell.commands.GSException;
import org.cyberaide.gridshell.commands.GSSystem;
import org.cyberaide.gridshell.commands.object.Add;
import org.cyberaide.gridshell.commands.object.Create;
import org.cyberaide.gridshell.commands.object.GSBasicObject;
import org.cyberaide.gridshell.commands.object.GSGroup;
import org.cyberaide.gridshell.commands.object.GSObject;
import org.cyberaide.gridshell.commands.object.Remove;
import org.cyberaide.gridshell.commands.util.ObjectLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoveTest {

    private static final String name = "testremoveobj";

    @Before
    public void setUp() throws Exception {
        GSSystem.setupClient(new File("").getAbsolutePath(), null, null);
    }

    /**
	 * Basic test to test operations
	 */
    @Test
    public void removeSingleTest() {
        String elem1 = "first";
        String elem2 = "second";
        Create create = new Create("-type group -name " + name + " -elements " + elem1 + " " + elem2);
        create.execute();
        Remove remove = new Remove(name + " " + elem1);
        remove.execute();
        GSObject obj = ObjectLoader.loadObject(name, null);
        if (obj == null) {
            fail("Object not created.  Run Create test");
        } else {
            GSGroup grp = (GSGroup) obj;
            if (grp != null) {
                List<String> elements = grp.getElementList();
                assertEquals(elem2, elements.get(0));
                assertEquals(1, elements.size());
                grp.delete();
            } else {
                fail("Wrong object type");
            }
        }
    }

    /**
	 * Basic test to test operations
	 */
    @Test
    public void addMultiTest() {
        String elem1 = "first";
        String elem2 = "second";
        String elem3 = "third";
        String elem4 = "forth";
        Create create = new Create("-type group -name " + name + " -elements " + elem1 + " " + elem2 + " " + elem3 + " " + elem4);
        create.execute();
        Remove remove = new Remove(name + " " + elem1 + " " + elem2);
        remove.execute();
        GSObject obj = ObjectLoader.loadObject(name, null);
        if (obj == null) {
            fail("Object not created.  Run Create test");
        } else {
            GSGroup grp = (GSGroup) obj;
            if (grp != null) {
                List<String> elements = grp.getElementList();
                assertEquals(elem3, elements.get(0));
                assertEquals(elem4, elements.get(1));
                assertEquals(2, elements.size());
                grp.delete();
            } else {
                fail("Wrong object type");
            }
        }
    }

    /**
	 * Basic test to test operations
	 */
    @Test
    public void testInvalidCollectionObj() {
        Create create = new Create("-type job -name testjob -command \"hi\"");
        create.execute();
        String elem2 = "second";
        Remove remove = new Remove(name + " " + elem2);
        List<String> testList = new LinkedList<String>();
        testList.add("one");
        try {
            remove.remove("newgroup", testList);
            fail("Should have gotten GSException");
        } catch (GSException exception) {
            GSObject obj = ObjectLoader.loadObject("testjob", null);
            obj.delete();
            assertEquals(true, true);
        }
    }
}
