package com.volantis.mcs.eclipse.ab.editors.devices.odom;

import com.volantis.mcs.eclipse.common.odom.ODOMElementTestCase;
import junitx.util.PrivateAccessor;
import java.util.List;

/**
 * Test case for DeviceODOMElement
 */
public class DeviceODOMElementTestCase extends ODOMElementTestCase {

    /**
     * Test that submitRestorableName creates a StandardElementHandler
     * and that it does this only if the name has not already been submitted.
     */
    public void testSubmitRestorableName() throws Exception {
        DeviceODOMElementFactory factory = new DeviceODOMElementFactory();
        DeviceODOMElement policies = (DeviceODOMElement) factory.element("policies", com.volantis.mcs.eclipse.common.odom.MCSNamespace.DEVICE);
        policies.submitRestorableName("pixelsX");
        List restorables = (List) PrivateAccessor.getField(policies, "standardElementHandlerList");
        assertEquals("Expected the number of restorables to be 1.", 1, restorables.size());
        policies.submitRestorableName("pixelsX");
        assertEquals("Expected the number of restorables to still be 1.", 1, restorables.size());
        DeviceODOMElement policy = (DeviceODOMElement) factory.element("policy", com.volantis.mcs.eclipse.common.odom.MCSNamespace.DEVICE);
        policy.setAttribute("name", "pixelsX");
        policies.addContent(policy);
        StandardElementHandler seh = (StandardElementHandler) PrivateAccessor.getField(policy, "standardElementHandler");
        assertNotNull("There should be a StandardElementHandler on policy.", seh);
    }
}
