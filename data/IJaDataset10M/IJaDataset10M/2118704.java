package org.hip.vif.menu.test;

import java.util.Locale;
import java.util.Vector;
import org.eclipse.core.runtime.IConfigurationElement;
import org.hip.vif.menu.ActorGroupState;
import org.hip.vif.menu.TaskSetConfiguration;
import junit.framework.TestCase;

/**
 * @author Luthiger
 * Created: 18.05.2008
 */
public class TaskSetConfigurationTest extends TestCase {

    private static final String NL = System.getProperty("line.separator");

    private static final String EXPECTED = "<li class=\"part\"><a href=\"javascript:submitRequest('.org.hip.vif.request.type2-1')\">!Test entry 2.1!</a></li>" + NL + "<li class=\"part\"><a href=\"javascript:submitRequest('.org.hip.vif.request.type2-2')\">!Test entry 2.2!</a></li>" + NL + "<li class=\"part\"><a href=\"javascript:submitRequest('.org.hip.vif.request.type2-3')\">!Test entry 2.3!</a></li>" + NL;

    private ConfigurationHouseKeeper configuration;

    public TaskSetConfigurationTest(String name) {
        super(name);
        configuration = new ConfigurationHouseKeeper();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRender() {
        ActorGroupState lState = new ActorGroupState();
        lState.actorPermissions = new Vector<String>();
        IConfigurationElement lTaskSet = configuration.createTaskSet();
        TaskSetConfiguration lConfiguration = new TaskSetConfiguration(lTaskSet, true, null, null);
        assertEquals("rendered task set in menu", EXPECTED, lConfiguration.render(Locale.ENGLISH, lState).toString());
    }
}
