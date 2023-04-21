package junit.util;

import java.io.*;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import dbaccess.util2.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

@RunWith(Suite.class)
@SuiteClasses({ DateTimeTest.class, LatLonTest.class, DBPropertiesTest.class, LoadPropertiesTest.class, DataFileTest.class })
public class UtilTests {

    public static Test suite() {
        return new JUnit4TestAdapter(UtilTests.class);
    }
}
