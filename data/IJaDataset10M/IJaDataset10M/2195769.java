package org.t2framework.daisy.core.xa.impl;

import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import junit.framework.TestCase;
import org.t2framework.commons.transaction.xa.XidImpl;
import org.t2framework.daisy.core.mock.MockConnection;
import org.t2framework.daisy.core.wrapper.LogicalConnection;
import org.t2framework.daisy.core.wrapper.impl.LogicalConnectionImpl;
import org.t2framework.daisy.core.xa.XAResourceStatus;

/**
 * 
 * {@.en See JTA 1.1 spec 4.2.}
 * 
 * <br />
 * 
 * {@.ja }
 * 
 * @author shot
 * 
 */
public class XAResourceImplTest extends TestCase {

    public void test1_normalOnePhaseWalkthrough() throws Exception {
        MockConnection con = new MockConnection();
        XAConnectionImpl coordinator = new XAConnectionImpl(con);
        XAResourceImpl xaResource = new XAResourceImpl(coordinator);
        Xid xid = new XidImpl();
        xaResource.start(xid, XAResource.TMNOFLAGS);
        {
            assertTrue(coordinator.isInTransaction());
            assertTrue(xaResource.status == XAResourceStatus.ACTIVE);
            assertEquals(xid, xaResource.getCurrentId());
        }
        con.close();
        xaResource.end(xid, XAResource.TMSUCCESS);
        {
            assertTrue(xaResource.status == XAResourceStatus.SUCCESS);
        }
        xaResource.commit(xid, true);
        {
            assertFalse(coordinator.isInTransaction());
            assertTrue(con.isCommitted());
        }
    }

    public void test2_normalTwoPhaseWalkthrough() throws Exception {
        MockConnection con = new MockConnection();
        XAConnectionImpl coordinator = new XAConnectionImpl(con);
        XAConnectionImpl coordinator2 = new XAConnectionImpl(con);
        Xid xid = new XidImpl();
        Xid xid2 = new XidImpl(xid, 1);
        LogicalConnection con1 = new LogicalConnectionImpl(coordinator, xid, null);
        LogicalConnection con2 = new LogicalConnectionImpl(coordinator, xid2, null);
        XAResourceImpl xaResource = (XAResourceImpl) coordinator.getXAResource();
        XAResourceImpl xaResource2 = (XAResourceImpl) coordinator2.getXAResource();
        xaResource.start(xid, XAResource.TMNOFLAGS);
        {
            assertTrue(coordinator.isInTransaction());
            assertTrue(xaResource.status == XAResourceStatus.ACTIVE);
            assertEquals(xid, xaResource.getCurrentId());
        }
        xaResource2.start(xid2, XAResource.TMJOIN);
        {
            assertTrue(coordinator2.isInTransaction());
            assertTrue(xaResource2.status == XAResourceStatus.ACTIVE);
            assertEquals(xid2, xaResource2.getCurrentId());
        }
        xaResource.end(xid, XAResource.TMSUCCESS);
        {
            assertTrue(xaResource.status == XAResourceStatus.SUCCESS);
        }
        xaResource2.end(xid2, XAResource.TMSUCCESS);
        {
            assertTrue(xaResource2.status == XAResourceStatus.SUCCESS);
        }
        con1.close();
        con2.close();
        assertTrue(xaResource.prepare(xid) == XAResource.XA_OK);
        {
            assertTrue(xaResource.status == XAResourceStatus.PREPARED);
        }
        assertTrue(xaResource2.prepare(xid2) == XAResource.XA_OK);
        {
            assertTrue(xaResource2.status == XAResourceStatus.PREPARED);
        }
        xaResource.commit(xid, false);
        {
            assertFalse(coordinator.isInTransaction());
        }
        xaResource2.commit(xid2, false);
        {
            assertFalse(coordinator2.isInTransaction());
        }
        assertTrue(con.isCommitted());
        con.close();
    }
}
