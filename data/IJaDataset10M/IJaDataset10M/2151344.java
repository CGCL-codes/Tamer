package com.jcorporate.expresso.core;

import com.jcorporate.expresso.services.test.DBTestSuite;
import com.jcorporate.expresso.services.test.TestSystemInitializer;
import java.util.Vector;

/**
 * DESCRIBE ME!
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public class ExpressoStressTestSuite extends DBTestSuite {

    public ExpressoStressTestSuite() {
    }

    public static void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() throws Exception {
        TestSystemInitializer.setUp();
        ExpressoStressTestSuite ts = new ExpressoStressTestSuite();
        return ts;
    }

    /**
     * Allows the test schema to be automatically be created and cleaned up as
     * well
     */
    protected void addSchema(Vector schemas) throws Exception {
        TestSystemInitializer.setUp();
        schemas.add(com.jcorporate.expresso.core.dbobj.tests.TestSchema.class.newInstance());
        super.addSchema(schemas);
    }
}
