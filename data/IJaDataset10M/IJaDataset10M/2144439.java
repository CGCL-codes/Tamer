package org.xaware.ide.shared;

import java.util.Hashtable;
import javax.naming.Context;
import org.xaware.testing.util.BaseTestCase;

/**
 * Test getting tables using JBoss 4.0.3 SP1 connection pool for Sybase
 * 
 * In order to run this test case successfully, following requirements must be met: 1. jbossall-client.jar file from
 * <jboss>/client should in included in classpath 2. Create *ds.xml file to set up Sybase data source connection pool
 * using JNDI name "XASybaseDS" on JBoss, and copy it on <jboss>/server/default/deploy folder 3. Start JBoss
 * 
 * @author abhatt
 */
public class JBoss403Sybase extends BaseTestCase {

    private DataSourceConnectionHelper testHelper = null;

    public JBoss403Sybase(final String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        testHelper = new DataSourceConnectionHelper();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        testHelper.closeConnection();
    }

    public void testGettingTables() {
        final Hashtable ht = new Hashtable();
        ht.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        ht.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        testHelper.getTables(ht, "XASybaseDS");
    }
}
