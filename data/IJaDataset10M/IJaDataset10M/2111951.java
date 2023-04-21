package fi.hip.gb.test;

import junit.framework.TestCase;
import fi.hip.gb.core.Config;

public class GBTestCase extends TestCase {

    public GBTestCase(String name) {
        super(name);
        String gbHome = "output/tests";
        if (System.getProperty("GB_HOME") != null) gbHome = System.getProperty("GB_HOME") + "/" + gbHome;
        Config.getInstance(gbHome);
    }
}
