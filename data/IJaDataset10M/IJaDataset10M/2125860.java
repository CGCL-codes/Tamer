package unitTest.org.yaorma.properties;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.yaorma.properties.AppProps;

public class UnitTestProperties extends TestCase {

    public void testProps() throws Exception {
        ArrayList<String> keys = AppProps.getKeys();
        System.out.println("got " + keys.size() + " keys:");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = AppProps.get(key);
            System.out.println(key + ": " + value);
        }
    }
}
