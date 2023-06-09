package org.firebirdsql.jca;

import javax.resource.spi.ManagedConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.Statement;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Describe class <code>TestFBConnection</code> here.
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version 1.0
 */
public class TestFBConnection extends TestXABase {

    public TestFBConnection(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestFBConnection.class);
    }

    public void testCreateC() throws Exception {
        if (log != null) log.info("testCreateC");
        FBManagedConnectionFactory mcf = initMcf();
        assertTrue("Could not get FBManagedConnectionFactory", mcf != null);
        ManagedConnection mc = mcf.createManagedConnection(null, null);
        assertTrue("Could not get ManagedConnection", mc != null);
        Connection c = (Connection) mc.getConnection(null, null);
        assertTrue("Could not get Connection", c != null);
        mc.destroy();
    }

    public void testAssociateC() throws Exception {
        if (log != null) log.info("testAssociateC");
        FBManagedConnectionFactory mcf = initMcf();
        ManagedConnection mc1 = mcf.createManagedConnection(null, null);
        Connection c1 = (Connection) mc1.getConnection(null, null);
        ManagedConnection mc2 = mcf.createManagedConnection(null, null);
        Connection c2 = (Connection) mc2.getConnection(null, null);
        mc1.associateConnection(c2);
        mc2.associateConnection(c1);
        mc1.destroy();
        mc2.destroy();
    }

    public void testCreateStatement() throws Exception {
        if (log != null) log.info("testCreateStatement");
        FBManagedConnectionFactory mcf = initMcf();
        ManagedConnection mc = mcf.createManagedConnection(null, null);
        Connection c = (Connection) mc.getConnection(null, null);
        Statement s = c.createStatement();
        assertTrue("Could not create Statement", s != null);
        mc.destroy();
    }

    public void testUseStatement() throws Exception {
        if (log != null) log.info("testUseStatement");
        FBManagedConnectionFactory mcf = initMcf();
        ManagedConnection mc = mcf.createManagedConnection(null, null);
        Connection c = (Connection) mc.getConnection(null, null);
        Statement s = c.createStatement();
        XAResource xa = mc.getXAResource();
        Exception ex = null;
        Xid xid = new XidImpl();
        xa.start(xid, XAResource.TMNOFLAGS);
        try {
            s.execute("CREATE TABLE T1 ( C1 SMALLINT, C2 SMALLINT)");
        } catch (Exception e) {
            ex = e;
        }
        xa.end(xid, XAResource.TMSUCCESS);
        xa.commit(xid, true);
        xid = new XidImpl();
        xa.start(xid, XAResource.TMNOFLAGS);
        s.execute("DROP TABLE T1");
        s.close();
        xa.end(xid, XAResource.TMSUCCESS);
        xa.commit(xid, true);
        mc.destroy();
        if (ex != null) {
            throw ex;
        }
    }
}
