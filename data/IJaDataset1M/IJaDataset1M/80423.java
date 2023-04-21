package plumber;

import java.util.HashSet;
import org.junit.Test;
import plumber.component.ClasspathComponentManager;
import plumber.component.ComponentManager;
import plumber.flow.DefaultFlowLocator;
import plumber.flow.FlowManager;
import plumber.model.Flow;

/**
 * test setting up and locating Flows
 * @author mgarber
 *
 */
public class FlowTest {

    @SuppressWarnings("serial")
    @Test
    public void testSetup() {
        final Flow flow = TestUtils.makeFlow();
        FlowManager fm = new FlowManager();
        DefaultFlowLocator dfl = new DefaultFlowLocator();
        dfl.setFlows(new HashSet<Flow>() {

            {
                add(flow);
            }
        });
        fm.setFlowLocator(dfl);
        fm.setup();
    }

    @Test(expected = RuntimeException.class)
    public void testWrongComponent() {
        ComponentManager cm = new ClasspathComponentManager();
        cm.find("dsaggds", "dafdg");
    }
}
