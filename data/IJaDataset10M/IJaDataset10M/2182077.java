package uk.co.thirstybear.hectorj;

import junit.framework.TestCase;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProjectBuildDataParserTest extends TestCase {

    public void testShouldParseDataSuccessfully() {
        String xmlFromHudson = readProjectData();
        ProjectBuildDataParser parser = new ProjectBuildDataParser();
        try {
            Project project = parser.parse(xmlFromHudson);
        } catch (Exception e) {
            fail("UnexpectedException " + e.getMessage());
        }
    }

    private String readProjectData() {
        InputStream is = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            is = this.getClass().getResourceAsStream("/data/projectspecific.xml");
            assertNotNull("Could not find test resource", is);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, bytesRead));
            }
        } catch (IOException e) {
            fail("Could not read test data from file:" + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    fail("Could not close stream to test data");
                }
            }
        }
        return stringBuffer.toString();
    }
}
