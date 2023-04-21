package net.sf.dz3.device.sensor.impl;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.TestCase;
import net.sf.jukebox.datastream.signal.model.DataSample;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

/**
 * Shell sensor tests.
 * 
 * These tests, in their current form, are good enough to run on a Unix system, and, unfortunately,
 * not really portable across installations. Analyzing the source will give you an idea of what to do to
 * to make them work on your system - or feel free to make tests generic enough so they can be
 * reused without modification.
 * 
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2009
 */
public class ShellSensorTest extends TestCase {

    private final Logger logger = Logger.getLogger(getClass());

    /**
     * @return {@code false} if OS is not within a supported list (just Linux for now).
     */
    private boolean isOsSupported() {
        Set<String> supported = new TreeSet<String>();
        supported.add("Linux");
        String os = System.getProperty("os.name");
        if (supported.contains(os)) {
            return true;
        }
        logger.error("OS not supported: " + os);
        return false;
    }

    /**
     * Make sure the {@link ShellSensor} properly executes a shell command given.
     */
    public void testGood() throws IOException {
        NDC.push("testGood");
        try {
            if (!isOsSupported()) {
                return;
            }
            ShellSensor ss = new ShellSensor("address", 1000, "echo 5.5");
            DataSample<Double> sample = ss.getSensorSignal();
            logger.info("Sample: " + sample);
            assertFalse(sample.isError());
            assertEquals(5.5, sample.sample);
        } finally {
            NDC.pop();
        }
    }

    /**
     * Make sure the {@link ShellSensor} properly reports a problem with the shell command given.
     */
    public void testBad() throws IOException {
        NDC.push("testBad");
        try {
            if (!isOsSupported()) {
                return;
            }
            ShellSensor ss = new ShellSensor("address", 1000, "does.not.exist");
            DataSample<Double> sample = ss.getSensorSignal();
            logger.info("Sample: " + sample);
            assertTrue(sample.isError());
        } finally {
            NDC.pop();
        }
    }

    /**
     * Not a test, really.
     * 
     * Fiddle with arguments and see that pipes are not handled correctly.
     */
    public void testPipeRaw() throws IOException, InterruptedException {
        NDC.push("testPipeRaw");
        try {
            if (!isOsSupported()) {
                return;
            }
            int rc = -1;
            try {
                Process p = Runtime.getRuntime().exec(new String[] { "echo", "\"abcd\"|tr \"dcba\" \"0123\"" });
                rc = p.waitFor();
            } catch (Throwable t) {
                logger.error("Unexpected exception", t);
                fail("Unexpected exception");
            }
            assertEquals(0, rc);
        } finally {
            NDC.pop();
        }
    }

    /**
     * Make sure that {@link ShellSensor} implementation correctly handles commands with pipes in them.  
     */
    public void testPipe() throws IOException {
        NDC.push("testPipe");
        try {
            if (!isOsSupported()) {
                return;
            }
            ShellSensor ss = new ShellSensor("address", 1000, "echo \"abcd\"|tr \"dcba\" \"0123\"");
            DataSample<Double> sample = ss.getSensorSignal();
            logger.info("Sample: " + sample);
            assertFalse(sample.isError());
            assertEquals(3210.0, sample.sample);
        } catch (Throwable t) {
            logger.error("Unexpected exception", t);
            fail("Unexpected exception");
        } finally {
            NDC.pop();
        }
    }
}
