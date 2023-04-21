package jam4j;

import static org.testng.Assert.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class OutputTest {

    private interface Resources {

        String SAMPLES_PREFIX = "/jam4j/test/samples", SAMPLES_PROPERTIES = SAMPLES_PREFIX + "/samples.properties", SANDBOXES_PREFIX = "/jam4j/test/sandboxes", SANDBOXES_PROPERTIES = SANDBOXES_PREFIX + "/sandboxes.properties";
    }

    private String jamExec = null;

    @BeforeClass
    @Parameters("jam-exec")
    public void beforeClass(String jamExec) {
        this.jamExec = jamExec;
    }

    @DataProvider(name = "samples")
    public Object[][] samples() throws IOException {
        final String[] sampleNames = namesFromProperties(Resources.SAMPLES_PROPERTIES, "samples");
        return transpose(sampleNames);
    }

    @DataProvider(name = "sandboxes")
    public Object[][] sandboxes() throws IOException {
        final String[] sandboxNames = namesFromProperties(Resources.SANDBOXES_PROPERTIES, "sandboxes");
        return transpose(sandboxNames);
    }

    private static Object[][] transpose(Object[] objects) {
        final int count = objects.length;
        final Object[][] ans = new Object[count][1];
        for (int ix = 0; ix < count; ix++) ans[ix][0] = objects[ix];
        return ans;
    }

    private String[] namesFromProperties(String resource, String key) throws IOException {
        final Properties props = properties(resource);
        if (!props.containsKey(key)) return new String[0];
        final String value = props.getProperty(key);
        if (value == null || value.isEmpty()) return new String[0];
        return props.getProperty(key).split(" ");
    }

    private Properties properties(String resource) throws IOException {
        final Properties props = new Properties();
        final InputStream propInput = OutputTest.class.getResourceAsStream(resource);
        if (propInput == null) throw new RuntimeException("Can't find resource: " + resource);
        props.load(propInput);
        return props;
    }

    @Test(dataProvider = "samples", description = "Integration test comparing output to Jam's output")
    public void testSample(String sampleName) throws IOException {
        Reporter.log("Testing sample " + sampleName + " ...", true);
        final Sandbox sandbox = new Sandbox();
        final String sampleResource = Resources.SAMPLES_PREFIX + '/' + sampleName + ".jamfile";
        sandbox.add(sampleResource, "Jamfile");
        runSandbox(sandbox);
    }

    @Test(dataProvider = "sandboxes", description = "Integration test using a complete directory structure")
    public void testSandbox(String sandboxName) throws IOException {
        Reporter.log("Testing sandbox " + sandboxName + " ...", true);
        final Sandbox sandbox = new Sandbox();
        final String sandboxResourcePrefix = Resources.SANDBOXES_PREFIX + '/' + sandboxName;
        final String[] fileNames = namesFromProperties(sandboxResourcePrefix + "/sandbox.properties", "files");
        for (String fileName : fileNames) sandbox.add(sandboxResourcePrefix + '/' + fileName, fileName);
        runSandbox(sandbox);
    }

    private void runSandbox(final Sandbox sandbox) throws IOException {
        byte[] expectedOut, expectedErr, actualOut, actualErr;
        {
            final byte[][] expected = sandbox.runJam(new File(jamExec));
            expectedOut = expected[0];
            expectedErr = expected[1];
        }
        logOutput("Expected", expectedOut, expectedErr);
        {
            final byte[][] actual = sandbox.runJam4J();
            actualOut = actual[0];
            actualErr = actual[1];
        }
        logOutput("Actual", actualOut, actualErr);
        if (expectedErr.length == 0) assert actualErr.length == 0 : "Unexpected error output: " + new String(actualErr);
        assertEquals(actualOut, expectedOut, "Test output");
    }

    private static void logOutput(String label, byte[] actualOut, byte[] actualErr) {
        Reporter.log(label + ':', true);
        Reporter.log(new String(actualOut), true);
        Reporter.log("---", true);
        if (actualErr.length != 0) {
            Reporter.log(label + " errors:", true);
            Reporter.log(new String(actualErr), true);
            Reporter.log("---", true);
        }
    }
}
