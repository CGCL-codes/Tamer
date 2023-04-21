package biz.evot.util.log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import biz.evot.osgi.MockBC;
import biz.evot.util.lang.NFile;
import biz.evot.util.lang.NInputStream;
import biz.evot.util.log.LogStream;
import biz.evot.util.log.Logger;
import junit.framework.TestCase;

public class LoggerTest extends TestCase {

    int count = 1;

    private PrintStream old;

    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("loging", "on");
        old = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        System.setOut(old);
    }

    public void testLogging() {
        Logger logger = new Logger();
        logger.addLogStream(new LogStream() {

            public void log(int level, String msg, Throwable th) {
                assertEquals(count, level);
                assertEquals("test" + count, msg);
                count++;
            }
        });
        logger.fatal("test1");
        logger.error("test2");
        logger.warn("test3");
        logger.info("test4");
        logger.debug("test5");
        logger.trace("test6");
        assertEquals(7, count);
        count = 1;
        Throwable th = new RuntimeException("Test");
        logger.fatal("test1", th);
        logger.error("test2", th);
        logger.warn("test3", th);
        logger.info("test4", th);
        logger.debug("test5", th);
        logger.trace("test6", th);
        assertEquals(7, count);
    }

    public void testThresholdLevel() {
        Logger logger = new Logger();
        logger.addLogStream(new LogStream() {

            public void log(int level, String msg, Throwable th) {
                count++;
            }
        });
        logger.setLevel(Logger.LEVEL_INFO);
        assertEquals(Logger.LEVEL_INFO, logger.getLevel());
        logger.fatal("test1");
        logger.error("test2");
        logger.warn("test3");
        logger.info("test4");
        logger.debug("test5");
        logger.trace("test6");
        assertEquals(5, count);
        count = 5;
        assertTrue(logger.isEnabledFatal());
        assertTrue(logger.isEnabledError());
        assertTrue(logger.isEnabledWarning());
        assertTrue(logger.isEnabledInfo());
        assertFalse(logger.isEnabledDebug());
        assertFalse(logger.isEnabledTrace());
    }

    public void testClassLoader() throws ClassNotFoundException {
        TestClassLoader cl = new TestClassLoader();
        Class clazz = cl.findClass("biz.evot.util.log.LoggerTest");
        assertEquals(cl, clazz.getClassLoader());
    }

    public void testLoggerBasedClassLoader() throws ClassNotFoundException {
        Class c1 = getClass();
        TestClassLoader cl = new TestClassLoader();
        Class c2 = cl.findClass("biz.evot.util.log.LoggerTest");
        Logger logger1 = Logger.getLogger(c1);
        Logger logger2 = Logger.getLogger(c2);
        assertNotSame(logger1, logger2);
        assertEquals(logger1, Logger.getLogger(Logger.class));
    }

    public void testOSGiBundleEvent() {
        Logger logger = Logger.getLogger(getClass());
        MockBC bc = new MockBC();
        logger.init(bc);
        assertTrue(logger.isEnabledOSGi());
        assertTrue(bc.getBundleListeners().size() > 0);
        BundleListener listener = (BundleListener) bc.getBundleListeners().get(0);
        listener.bundleChanged(new BundleEvent(BundleEvent.STOPPED, bc.getBundle()));
        assertFalse(logger.isEnabledOSGi());
    }

    class TestClassLoader extends ClassLoader {

        protected Class findClass(String name) throws ClassNotFoundException {
            byte[] data = null;
            try {
                InputStream in = getClass().getResourceAsStream("/biz/evot/util/log/LoggerTest.class");
                data = NInputStream.readBytes(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return defineClass(name, data, 0, data.length, null);
        }
    }
}
