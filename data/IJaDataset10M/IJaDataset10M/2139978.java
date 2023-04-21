package test.org.nakedobjects.utility.configuration;

import org.nakedobjects.utility.configuration.PropertiesConfiguration;
import java.util.Properties;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

public class PropertiesConfigurationTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PropertiesConfigurationTest.class);
    }

    private PropertiesConfiguration configuration;

    public PropertiesConfigurationTest(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.OFF);
        configuration = new PropertiesConfiguration();
        Properties p = new Properties();
        p.put("nakedobjects.bool", "on");
        p.put("nakedobjects.str", "string");
        configuration.add(p);
        Properties p1 = new Properties();
        p1.put("nakedobjects.int", "1");
        p1.put("nakedobjects.str", "replacement");
        configuration.add(p1);
    }

    public void testDuplicatedPropertyName() {
        assertEquals("replacement", configuration.getString("nakedobjects.str"));
    }

    public void testUniqueEntries() {
        assertEquals(1, configuration.getInteger("nakedobjects.int"));
        assertEquals(true, configuration.getBoolean("nakedobjects.bool"));
    }
}
