package uk.ac.ed.rapid.variables;

import java.io.FileInputStream;
import java.net.URL;
import junit.framework.TestCase;
import uk.ac.ed.rapid.button.Button;
import uk.ac.ed.rapid.data.RapidData;
import uk.ac.ed.rapid.data.impl.RapidDataFactory;
import uk.ac.ed.rapid.jobdata.VariableResolver;
import uk.ac.ed.rapid.value.Value;
import uk.ac.ed.rapid.xml.RapidXML;

public class SaveFileTest extends TestCase {

    public void testSaveFile() {
        try {
            RapidData rapidData = RapidDataFactory.createRapidData(RapidXML.getCustomXMLFile("jobtest/loadfile.xml"), null, null, null);
            URL path = RapidXML.getCustomXMLFile("loadfile/overwrite.txt");
            String pathName = path.toString();
            pathName = pathName.substring("file:".length());
            Value pathValue = VariableResolver.getVariable("pathvar", rapidData);
            pathValue.put(pathName);
            String contents = "" + Math.random();
            VariableResolver.getVariable("var", rapidData).put(contents);
            Button button = rapidData.getButtonTable().getButton("symbol2");
            button.performAction(rapidData);
            FileInputStream fstream = new FileInputStream(pathName);
            byte[] buf = new byte[100];
            int numBytes = fstream.read(buf);
            fstream.close();
            assertEquals(contents, new String(buf, 0, numBytes));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }
}
