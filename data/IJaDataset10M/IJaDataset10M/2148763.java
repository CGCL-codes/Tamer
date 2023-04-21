package org.notify4b.util;

import static org.junit.Assert.*;
import java.io.File;
import junit.framework.TestCase;
import org.notify4b.*;

public class PropertiesConfiguratorTest extends TestCase {

    Notifier notifier;

    File configFile;

    PropertiesConfigurator pc;

    protected void setUp() {
        notifier = new DefaultNotifier("main");
        configFile = new File("notify4b.properties");
        pc = new PropertiesConfigurator(configFile);
    }

    public void testDoConfigure() {
        try {
            pc.doConfigure(notifier);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    protected void tearDown() {
        notifier = null;
        configFile = null;
        pc = null;
    }
}
