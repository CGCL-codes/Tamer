package services.core.commands.execute;

import junit.framework.TestCase;
import services.core.commands.SimpleCommandRequest;
import services.core.commands.SimpleCommandResponse;

public class ExecuteGroovyClassCommandTest extends TestCase {

    SimpleCommandRequest commandRequest;

    GroovyClassCommand command;

    SimpleCommandResponse commandResponse;

    protected void setUp() throws Exception {
        System.setProperty(GroovyClassCommand.GROOVY_SERVICE_HOME, System.getProperty("user.dir") + "/src/test/resources/GroovyServiceHome");
        commandRequest = SimpleCommandRequest.createRequest();
        command = new GroovyClassCommand();
        commandResponse = null;
    }

    public void testGetMillTimeCall() {
        commandRequest.put("target", new String[] { "GetMilliTime.groovy" });
        commandRequest.put("value", new String[] { "" });
        commandResponse = command.execute(commandRequest);
        assertEquals(0, commandResponse.getResponseCode());
        assertEquals("", commandResponse.getResponseMessage());
        assertTrue(Long.parseLong(commandResponse.getResponseResult()) > System.currentTimeMillis() - 5000);
    }

    public void testHelloWorldCall() {
        commandRequest.put("target", new String[] { "HelloClass.groovy" });
        commandRequest.put("value", new String[] { "World" });
        commandResponse = command.execute(commandRequest);
        assertEquals(0, commandResponse.getResponseCode());
        assertEquals("", commandResponse.getResponseMessage());
        assertEquals("Hello World!", commandResponse.getResponseResult());
    }
}
