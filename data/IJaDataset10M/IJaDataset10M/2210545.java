package junit.jbjf.xjc;

import static org.junit.Assert.*;
import java.io.File;
import junit.framework.TestCase;
import org.jbjf.api.JBJFXmlDefinition;
import org.jbjf.xjc.JbjfBatchDefinition.JbjfConnections;
import org.jbjf.xjc.JbjfBatchDefinition.JbjfDirectories;
import org.jbjf.xjc.JbjfBatchDefinition.JbjfConnections.Connection;
import org.jbjf.xjc.JbjfBatchDefinition.JbjfDirectories.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJbjfDirectories extends TestCase {

    /** 
     * Stores a fully qualified class name.  Used for debugging and 
     * auditing.
     * @since 1.0.0
     */
    public static final String ID = TestJbjfDirectories.class.getName();

    /** 
     * Stores the class name, primarily used for debugging and so 
     * forth.  Used for debugging and auditing.
     * @since 1.0.0
     */
    private String SHORT_NAME = "TestJbjfDirectories()";

    /** 
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>.  Used for
     * debugging and auditing.
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    private JBJFXmlDefinition mxmlDefinition;

    private JbjfDirectories mtestItem;

    private String mstrXMLFile = "jbjf-base-definition.xml";

    @Before
    public void setUp() throws Exception {
        try {
            this.mxmlDefinition = new JBJFXmlDefinition("." + File.separator + "etc" + File.separator + this.mstrXMLFile);
            this.mxmlDefinition.parseFile();
            this.mtestItem = this.mxmlDefinition.getJbjfDirectories();
        } catch (Exception ltheXCP) {
            ltheXCP.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        this.mxmlDefinition = null;
        this.mstrXMLFile = null;
    }

    @Test
    public void testGetDirectory() {
        TestCase.assertEquals(3, this.mtestItem.getDirectory().size());
    }

    @Test
    public void testGetADirectory() {
        Directory ltestItem = this.mtestItem.getDirectory().get(0);
        TestCase.assertEquals("base", ltestItem.getId());
        TestCase.assertEquals("base", ltestItem.getName());
        TestCase.assertEquals("relative", ltestItem.getPath());
        TestCase.assertEquals(".", ltestItem.getValue());
    }

    @Test
    public void testGetBDirectory() {
        Directory ltestItem = this.mtestItem.getDirectory().get(1);
        TestCase.assertEquals("archivist", ltestItem.getId());
        TestCase.assertEquals("archivist", ltestItem.getName());
        TestCase.assertEquals("relative", ltestItem.getPath());
        TestCase.assertEquals("archives", ltestItem.getValue());
    }

    @Test
    public void testGetCDirectory() {
        Directory ltestItem = this.mtestItem.getDirectory().get(2);
        TestCase.assertEquals("log4j", ltestItem.getId());
        TestCase.assertEquals("log4j", ltestItem.getName());
        TestCase.assertEquals("relative", ltestItem.getPath());
        TestCase.assertEquals("etc", ltestItem.getValue());
    }
}
