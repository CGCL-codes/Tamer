package com.j256.ormlite.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Test;
import com.j256.ormlite.logger.Log.Level;

public class LocalLogTest extends BaseLogTest {

    public LocalLogTest() {
        super(new LocalLog("CommonsLoggingLogTest"));
    }

    @Test
    public void testLevelProperty() {
        Log log = new LocalLog("foo");
        if (log.isLevelEnabled(Level.TRACE)) {
            return;
        }
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "TRACE");
        try {
            log = new LocalLog("foo");
            assertTrue(log.isLevelEnabled(Level.TRACE));
        } finally {
            System.clearProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLevelProperty() {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "not a valid level");
        try {
            new LocalLog("foo");
        } finally {
            System.clearProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY);
        }
    }

    @Test(timeout = 10000)
    public void testFileProperty() throws Exception {
        String logPath = "target/foo.txt";
        File logFile = new File(logPath);
        logFile.delete();
        LocalLog.openLogFile(logPath);
        try {
            LocalLog log = new LocalLog("foo");
            assertTrue(log.isLevelEnabled(Level.FATAL));
            String msg = "fpjwefpwejfpwfjwe";
            log.log(Level.FATAL, msg);
            log.flush();
            assertTrue(logFile.exists());
            while (logFile.length() < msg.length()) {
                Thread.sleep(100);
            }
        } finally {
            LocalLog.openLogFile(null);
            logFile.delete();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFileProperty() {
        LocalLog.openLogFile("not-a-proper-directory-name-we-hope/foo.txt");
    }

    @Test
    public void testNotEnabled() {
        String logPath = "target/foo.txt";
        File logFile = new File(logPath);
        logFile.delete();
        LocalLog.openLogFile(logPath);
        try {
            LocalLog log = new LocalLog("foo");
            if (log.isLevelEnabled(Level.TRACE)) {
                return;
            }
            String msg = "fpjwefpwejfpwfjwe";
            log.log(Level.TRACE, msg);
            log.flush();
            assertTrue(logFile.exists());
            assertEquals(0, logFile.length());
        } finally {
            LocalLog.openLogFile(null);
            logFile.delete();
        }
    }
}
