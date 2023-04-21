package uk.ac.ed.rapid.variables;

import java.net.URL;
import junit.framework.TestCase;
import uk.ac.ed.rapid.button.Button;
import uk.ac.ed.rapid.data.RapidData;
import uk.ac.ed.rapid.data.impl.RapidDataImpl;
import uk.ac.ed.rapid.value.Value;
import uk.ac.ed.rapid.xml.RapidXML;

public class LoadFileTest extends TestCase {

    public void testLoadFile() {
        try {
            RapidData rapidData = new RapidDataImpl(RapidXML.getCustomXMLFile("jobtest/loadfile.xml"), null, null, null);
            URL path = RapidXML.getCustomXMLFile("loadfile/textfile.txt");
            String pathName = path.toString();
            pathName = pathName.substring("file:/".length());
            Value pathValue = rapidData.getJobData().getVariable("pathvar");
            pathValue.put(pathName);
            Button button = rapidData.getButtonTable().getButton("symbol1");
            button.performAction(rapidData);
            assertEquals("This is\na test file", rapidData.getJobData().getVariable("var").get());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }
}
