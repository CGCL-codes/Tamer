package org.sempere.gwt.toolbox.remoting.client.rpc.service;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests class for ServiceLocator class
 * 
 * @author <a href="mailto:benjamin@sempere.org">Benjamin Sempere</a>
 */
public class ServiceLocatorTest {

    private ServiceLocator locator = ServiceLocator.getInstance();

    @Test
    public void getLocatorName() throws Exception {
        assertEquals(ServiceLocator.LOCATOR_NAME, this.locator.getLocatorName());
    }
}
