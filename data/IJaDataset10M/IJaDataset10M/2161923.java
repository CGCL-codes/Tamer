package fate.test;

import fate.flow.container.Container;
import fate.flow.node.impl.SetValue;
import fate.flow.nodes.Node;
import fate.util.ParameterNames;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jeroen van Bergen
 */
public class SetValueTest {

    private Node node;

    private Map<String, String> parameters = new HashMap<String, String>();

    private TestNodeCreator nodeCreator = new TestNodeCreator();

    private String attributeName = "attribute_name";

    private String testValue = "whatever";

    @Test
    public void testSettingAttribute() throws Exception {
        parameters.put(ParameterNames.value.name(), testValue);
        parameters.put(ParameterNames.storeResultInAttribute.name(), attributeName);
        node = nodeCreator.getNode("test SetValue", SetValue.class.getCanonicalName(), parameters);
        Container container = new Container("some content");
        node.process(container);
        assertTrue(container.getAttribute(attributeName).equals(testValue));
    }
}
