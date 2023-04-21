package services.core.commands.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import junit.framework.TestCase;
import services.core.commands.SimpleCommandRequest;
import services.core.commands.SimpleCommandResponse;

public class FetchPropertiesFromFileCommandTest extends TestCase {

    SimpleCommandRequest commandRequest;

    FetchPropertiesFromFileCommand command;

    SimpleCommandResponse commandResponse;

    protected void setUp() throws Exception {
        System.setProperty(PropertyFileSupport.PROPERTY_FILE_HOME, System.getProperty("user.dir") + "/src/test/resources/PropertyFileHome");
        commandRequest = SimpleCommandRequest.createRequest();
        command = new FetchPropertiesFromFileCommand();
        commandResponse = null;
    }

    public void testFetchFromPropertiesFileWithDotPropertiesExtension() {
        commandRequest.put("propertyFilePath", new String[] { "default.properties" });
        commandRequest.put("propertyFilter", new String[] { ".*" });
        commandResponse = command.execute(commandRequest);
        assertEquals(0, commandResponse.getResponseCode());
        assertEquals("", commandResponse.getResponseMessage());
        assertTrue(commandResponse.getResponseResult().contains("key1=value1" + PropertyFileSupport.PROPERTY_LINE_SEPARATOR));
        commandRequest.put("propertyFilePath", new String[] { "default.properties" });
        commandRequest.put("propertyFilter", new String[] { "key1" });
        commandResponse = command.execute(commandRequest);
        assertEquals(0, commandResponse.getResponseCode());
        assertEquals("", commandResponse.getResponseMessage());
        assertEquals(commandResponse.getResponseResult(), "<![CDATA[key1=value1" + PropertyFileSupport.PROPERTY_LINE_SEPARATOR + "]]>");
    }

    public void testFetchFromPropertiesFileWithDotCsvExtension() throws IOException {
        commandRequest.put("propertyFilePath", new String[] { "customers.csv" });
        commandRequest.put("propertyFilter", new String[] { "" });
        commandResponse = command.execute(commandRequest);
        assertEquals(0, commandResponse.getResponseCode());
        assertEquals("", commandResponse.getResponseMessage());
        assertTrue(commandResponse.getResponseResult().contains("customerId="));
        Properties properties = new Properties();
        String line;
        BufferedReader br;
        br = new BufferedReader(new StringReader(commandResponse.getResponseResult().replace("<![CDATA[", "").replace("]]>", "")));
        while ((line = br.readLine()) != null) {
            String[] data = line.split("=");
            properties.setProperty(data[0], data[1]);
        }
        commandRequest.put("propertyFilePath", new String[] { "customers.csv" });
        commandRequest.put("propertyFilter", new String[] { ".*" });
        commandResponse = command.execute(commandRequest);
        assertEquals(0, commandResponse.getResponseCode());
        assertEquals("", commandResponse.getResponseMessage());
        assertTrue(commandResponse.getResponseResult().contains("customerId="));
        br = new BufferedReader(new StringReader(commandResponse.getResponseResult()));
        while ((line = br.readLine()) != null) {
            String[] data = line.split("=");
            if ("customerId".equals(data[0])) {
                assertTrue(!data[1].equals(properties.getProperty(data[0])));
            }
        }
    }

    public void testFetchFromPropertiesFromRemoteUrl() {
        commandRequest.put("propertyFilePath", new String[] { "http://java.sun.com/j2se/1.3/font.properties" });
        commandRequest.put("propertyFilter", new String[] { ".*" });
        commandResponse = command.execute(commandRequest);
        assertEquals(0, commandResponse.getResponseCode());
        assertEquals("", commandResponse.getResponseMessage());
        assertTrue(commandResponse.getResponseResult().contains("alias.timesroman=serif" + PropertyFileSupport.PROPERTY_LINE_SEPARATOR));
        assertTrue(commandResponse.getResponseResult().contains("alias.courier=monospaced" + PropertyFileSupport.PROPERTY_LINE_SEPARATOR));
    }
}
