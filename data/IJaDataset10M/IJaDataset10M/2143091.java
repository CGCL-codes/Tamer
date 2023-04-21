package Test.Connection;

import hcmus.am.utils.ConfigurationManager;
import junit.framework.TestCase;

public class Test_ConfigurationManager extends TestCase {

    public void testGetPropertiesValue() {
        String s = ConfigurationManager.getConnectionURL();
        assert (s.length() > 0);
        System.out.println(s);
    }
}
