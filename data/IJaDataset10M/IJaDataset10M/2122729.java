package net.sf.dhwt.initializer;

import junit.framework.TestCase;
import org.apache.commons.lang.*;
import java.beans.*;
import java.util.*;
import net.sf.dhwt.util.*;
import net.sf.dhwt.tracer.*;
import net.sf.dhwt.manager.*;

/**  DHWT ConfigInitializerTest Test Case */
public class ConfigInitializerTest extends BaseTestCase {

    /**
     *  Constructor for the DefaultCompositeTest object
     *
     * @param  fName
     */
    public ConfigInitializerTest(String fName) {
        super(fName);
    }

    /**
     *  A unit test for JUnit
     *
     * @exception  Exception
     */
    public void testDoInit() throws Exception {
        Properties params = new Properties();
        params.put(ConfigInitializer.PARAM_DIGESTER_XML_NAME, "jndi:/localhost/dhwt/WEB-INF/dhwt-digester-rules.xml");
        params.put(ConfigInitializer.PARAM_CONFIG_XML_NAME, "jndi:/localhost/dhwt/WEB-INF/dhwt-config.xml");
        params.put(ConfigInitializer.PARAM_DIGESTER_VALIDATING_NAME, "false");
        ConfigInitializer configInitializer = new ConfigInitializer();
        configInitializer.loadParameters(params);
        configInitializer.doInit();
        Iterator it = DataSourceManager.getManager().getNames().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            Debug.getDebug().debug(DataSourceManager.getManager().get(name));
        }
        it = CompositeManager.getManager().getNames().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            Debug.getDebug().debug(CompositeManager.getManager().get(name));
        }
        it = ComponentManager.getManager().getNames().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            Debug.getDebug().debug(ComponentManager.getManager().get(name));
        }
        it = ContainerManager.getManager().getNames().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            Debug.getDebug().debug(ContainerManager.getManager().get(name));
        }
    }
}
