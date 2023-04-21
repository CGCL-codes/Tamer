package com.xenonsoft.bridgetown.test.aop.transaction;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.xenonsoft.bridgetown.aop.IMethodInterceptor;
import com.xenonsoft.bridgetown.aop.transaction.IResourceController;
import com.xenonsoft.bridgetown.aop.transaction.ITransactionServiceManager;
import com.xenonsoft.bridgetown.aop.transaction.ITransactionSession;
import com.xenonsoft.bridgetown.resources.IConfigLoader;
import com.xenonsoft.bridgetown.resources.XMLConfigLoader;
import com.xenonsoft.bridgetown.soa.IServiceAssembler;
import com.xenonsoft.bridgetown.soa.impl.AOPServiceAssemblerImpl;

/**
 * A junit test for AOP assembly factory.
 * Tests the method interceptor.
 * 
 * @author peterp, 03-Oct-2004
 * @version $Id: TestAOPHibernateTransaction.java,v 1.1 2005/03/17 02:57:13 peter_pilgrim Exp $
 */
public class TestAOPHibernateTransaction extends TestCase {

    /** Configuration file */
    public static final String CONFIG_FILE1 = "aop-hibernate-transaction-101.xml";

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestAOPHibernateTransaction.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for TestAOPWeaver
     * @param name
     */
    public TestAOPHibernateTransaction(String name) {
        super(name);
    }

    /**
     * Test basic method interception with a AOP service assembler
     * implementation that instruments a bean with the debug 
     * interceptor.
     */
    public void testBasicMethodInterception() throws Exception {
        IServiceAssembler sai = new AOPServiceAssemblerImpl();
        IConfigLoader configLoader = new XMLConfigLoader();
        configLoader.setFile(new File(CONFIG_FILE1));
        sai.load(configLoader);
        sai.start();
        try {
            ITransactionServiceManager manager = (ITransactionServiceManager) sai.getName("testCenter", "myTransactionManager");
            ITransactionSession session = (ITransactionSession) sai.getName("testCenter", "myTransactionSession");
            IResourceController resource = (IResourceController) sai.getName("testCenter", "myResourceController");
            IMethodInterceptor interceptor = (IMethodInterceptor) sai.getName("testCenter", "myTransactionInterceptor");
            CreateTracksBusinessLogic dao = (CreateTracksBusinessLogic) sai.getName("testCenter", "createTracksBusinessLogic");
            dao.createTracks();
            System.out.println("See here! No SQL complicated catch statements");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw e;
        } finally {
            sai.dispose();
        }
    }
}
