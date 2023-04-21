package org.ikasan.framework.flow.invoker;

import java.util.List;
import org.ikasan.framework.flow.invoker.FlowInvocationContext;
import org.junit.Assert;
import org.junit.Test;

public class FlowInvocationContextTest {

    String componentName1 = "componentName1";

    String componentName2 = "componentName2";

    /**
	 * Test method for {@link org.ikasan.framework.flow.FlowInvocationContext#getLastComponentName()}.
	 */
    @Test
    public void testGetLastComponentName_willReturnNullWhenNoComponentsAdded() {
        FlowInvocationContext flowInvocationContext = new FlowInvocationContext();
        Assert.assertNull(flowInvocationContext.getLastComponentName());
    }

    /**
	 * Test method for {@link org.ikasan.framework.flow.FlowInvocationContext#getLastComponentName()}.
	 */
    @Test
    public void testGetLastComponentName_willReturnMostRecentlyAddedComponentName() {
        FlowInvocationContext flowInvocationContext = new FlowInvocationContext();
        Assert.assertNull(flowInvocationContext.getLastComponentName());
        flowInvocationContext.addInvokedComponentName(componentName1);
        Assert.assertEquals(componentName1, flowInvocationContext.getLastComponentName());
        flowInvocationContext.addInvokedComponentName(componentName2);
        Assert.assertEquals(componentName2, flowInvocationContext.getLastComponentName());
    }

    /**
	 * Test method for {@link org.ikasan.framework.flow.FlowInvocationContext#getInvokedComponents()}.
	 */
    @Test
    public void testGetInvokedComponents() {
        FlowInvocationContext flowInvocationContext = new FlowInvocationContext();
        flowInvocationContext.addInvokedComponentName(componentName1);
        flowInvocationContext.addInvokedComponentName(componentName2);
        List<String> invokedComponents = flowInvocationContext.getInvokedComponents();
        Assert.assertEquals(componentName1, invokedComponents.get(0));
        Assert.assertEquals(componentName2, invokedComponents.get(1));
        invokedComponents.add("componentName3");
        Assert.assertFalse(invokedComponents.equals(flowInvocationContext.getInvokedComponents()));
    }
}
