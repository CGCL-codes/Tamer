package org.perfectjpattern.jee.integration.dao;

import junit.framework.*;
import org.easymock.*;
import org.perfectjpattern.jee.api.integration.dao.*;

/**
 * Test suite for the {@link ManagedTransactionStrategy}
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Feb 12, 2009 9:43:51 PM $
 */
public class TestManagedTransactionStrategy extends TestCase {

    public void testGetTransaction() {
        ITransaction myMockTransaction = EasyMock.createNiceMock(ITransaction.class);
        ISession mySession = EasyMock.createNiceMock(ISession.class);
        ISessionStrategy myMockSessionStrategy = EasyMock.createNiceMock(ISessionStrategy.class);
        EasyMock.expect(myMockSessionStrategy.getSession()).andReturn(mySession);
        EasyMock.expect(mySession.getTransaction()).andReturn(myMockTransaction);
        EasyMock.replay(myMockSessionStrategy, mySession);
        ManagedTransactionStrategy myTransactionStrategy = new ManagedTransactionStrategy();
        myTransactionStrategy.setSessionStrategy(myMockSessionStrategy);
        ITransaction myTransaction = myTransactionStrategy.getTransaction();
        assertNotNull("Transaction is expected not to be null", myTransaction);
    }
}
