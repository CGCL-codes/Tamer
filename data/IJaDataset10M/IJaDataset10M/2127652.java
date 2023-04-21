package alltests;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.junit.internal.runners.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.xml.sax.SAXException;
import com.ibm.realtime.flexotask.template.FlexotaskValidationException;

/**
 * Testcases for the Flexible Task Graphs System
 */
public class Testcases {

    @Test
    public void testCommunicating() throws FlexotaskValidationException, SAXException, IOException, ParserConfigurationException {
        communicating.Main.main(new String[0]);
    }

    @Test
    public void testHighfreqread() throws FlexotaskValidationException, InterruptedException, IOException {
        highfreqread.Main.main(new String[] { "500" });
    }

    @Test
    public void testLctes2007() throws FlexotaskValidationException, InterruptedException {
        lctes2007.Main.main(new String[0]);
    }

    @Test
    public void testModes() throws FlexotaskValidationException, InterruptedException {
        modes.Main.main(new String[0]);
    }

    @Test
    public void testTypes() throws SAXException, IOException, ParserConfigurationException, FlexotaskValidationException, InterruptedException {
        types.Main.main(new String[0]);
    }

    @Test
    public void testNativeIO() throws IOException, SAXException, ParserConfigurationException, FlexotaskValidationException {
        nativeio.Main.main(new String[0]);
    }

    @Test
    public void testBadScheduler() throws FlexotaskValidationException {
        badscheduler.Main.main(new String[0]);
    }

    /**
	 * Method to test in a non-eclipse environment (e.g. a realtime VM)
	 */
    public static void main(String[] args) {
        RunListener listener = new BatchRunListener();
        JUnitCore runner = new JUnitCore();
        runner.addListener(listener);
        runner.run(Testcases.class);
    }

    /**
	 * A RunListener for batch mode operation 
	 */
    private static class BatchRunListener extends TextListener {

        public void testFailure(Failure failure) {
            System.err.println("Test failed: " + failure.getTestHeader() + ".  More details later.");
        }

        public void testIgnored(Description description) {
            System.err.println("Test ignored: " + description);
        }

        public void testStarted(Description description) {
            System.err.println("Starting test: " + description);
        }
    }
}
