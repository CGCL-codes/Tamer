package org.rapla.mobile.android.test.utility;

import org.rapla.facade.ClientFacade;
import org.rapla.mobile.android.RaplaMobileException;
import org.rapla.mobile.android.test.mock.MockClientFacade;
import org.rapla.mobile.android.test.mock.MockRaplaContext;
import org.rapla.mobile.android.utility.factory.ClientFacadeFactory;
import android.test.AndroidTestCase;

/**
 * ClientFacadeFactoryTest
 * 
 * Unit test class for org.rapla.mobile.android.utilities.factory.ClientFacadeFactory
 * 
 * @see org.rapla.mobile.android.utility.factory.utilities.factory.ClientFacadeFactory
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class ClientFacadeFactoryTest extends AndroidTestCase {

    public void testGetInstanceShouldAlwaysReturnTheSameInstance() {
        ClientFacadeFactory first = ClientFacadeFactory.getInstance();
        ClientFacadeFactory second = ClientFacadeFactory.getInstance();
        assertEquals(first, second);
    }

    public void testCreateInstanceShouldReturnFacadeFromRaplaContext() throws RaplaMobileException {
        MockRaplaContext context = new MockRaplaContext();
        MockClientFacade facade = new MockClientFacade();
        context.setClientFacade(facade);
        ClientFacade clientFacade = ClientFacadeFactory.getInstance().createInstance(context);
        assertEquals(facade, clientFacade);
    }
}
