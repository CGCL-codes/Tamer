package mn.more.mock.io.xml;

import java.io.IOException;
import junit.framework.TestCase;
import mn.more.foundation.io.FileOperator;
import mn.more.foundation.io.FileUtil;
import mn.more.mock.io.MockModel;

/**
 * @author <a href="mailto:mike.liu@aptechmongolia.edu.mn">Mike Liu</a>
 * @version $Revision: 119 $
 */
public class ReaderTestDrive extends TestCase {

    private String filename = ReaderTestDrive.class.getSimpleName() + "-fixture.xml";

    private String fixture = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!DOCTYPE models SYSTEM \"model.dtd\">\n" + "<models>\n" + "  <model id=\"user2\">\n" + "    <property name=\"userId\" type=\"String\"><![CDATA[&#009;]]></property>\n" + "    <property name=\"password\" type=\"String\"><![CDATA[Mohammed&#15;]]></property>\n" + "  </model>\n" + "  <model id=\"user1\">\n" + "    <property name=\"userId\" type=\"String\"><![CDATA[&nbsp;&nbsp;&nbsp;]]></property>\n" + "    <property name=\"password\" type=\"String\"><![CDATA[dudW[]]></property>\n" + "  </model>\n" + "  <model id=\"user4\">\n" + "    <property name=\"userId\" type=\"String\"><![CDATA[Gu]]></property>\n" + "    <property name=\"password\" type=\"String\"><![CDATA[Lucas&#4;&#1;]]></property>\n" + "  </model>\n" + "  <model id=\"user3\">\n" + "    <property name=\"userId\" type=\"String\"><![CDATA[G]]></property>\n" + "    <property name=\"password\" type=\"String\"><![CDATA[&#8;&#14;&#14&#]]></property>\n" + "  </model>\n" + "  <model id=\"user5\">\n" + "    <property name=\"userId\" type=\"String\"><![CDATA[GuI]]></property>\n" + "    <property name=\"password\" type=\"String\"><![CDATA[Dylan&#23;&#5;]]></property>\n" + "  </model>\n" + "  <model id=\"_all\">\n" + "    <property name=\"user2\" refid=\"user2\" />\n" + "    <property name=\"user1\" refid=\"user1\" />\n" + "    <property name=\"user4\" refid=\"user4\" />\n" + "    <property name=\"user3\" refid=\"user3\" />\n" + "    <property name=\"user5\" refid=\"user5\" />\n" + "  </model>\n" + "</models>\n";

    protected void setUp() throws Exception {
        super.setUp();
        FileUtil.writeTextToFile(filename, fixture);
    }

    public void testReadStrangeCharacters() {
        XMLReader reader = new XMLReader();
        try {
            reader.setFilename(filename, false);
            reader.read();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        MockModel model = reader.getModel("user1");
        assertEquals(model.getId(), "user1");
        assertEquals(model.getString("userId"), "   ");
        assertEquals(model.getString("password"), "dudW[");
        model = reader.getModel("user2");
        assertEquals(model.getId(), "user2");
        assertEquals(model.getString("userId"), "\t");
        assertEquals(model.getString("password"), "Mohammed" + (char) 15);
        model = reader.getModel("user3");
        assertEquals(model.getId(), "user3");
        assertEquals(model.getString("userId"), "G");
        assertEquals(model.getString("password"), (char) 8 + "" + (char) 14 + "&#14&#");
        model = reader.getModel("user4");
        assertEquals(model.getId(), "user4");
        assertEquals(model.getString("userId"), "Gu");
        assertEquals(model.getString("password"), "Lucas" + (char) 4 + (char) 1);
        model = reader.getModel("user5");
        assertEquals(model.getId(), "user5");
        assertEquals(model.getString("userId"), "GuI");
        assertEquals(model.getString("password"), "Dylan" + (char) 23 + (char) +5);
    }

    protected void tearDown() throws Exception {
        FileOperator.delete(filename);
        super.tearDown();
    }
}
