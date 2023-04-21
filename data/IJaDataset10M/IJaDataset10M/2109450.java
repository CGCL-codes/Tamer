package org.skycastle.component.ports;

import junit.framework.TestCase;
import org.skycastle.core.old.port.Port;
import org.skycastle.util.uiidentity.UiIdentityImpl;

@SuppressWarnings({ "JavaDoc" })
public class PortTest extends TestCase {

    public void testPortHasId() throws Exception {
        final String id = "bar";
        final Port port = new FluidPort(new UiIdentityImpl(id));
        assertEquals(id, port.getIdentifier());
    }
}
