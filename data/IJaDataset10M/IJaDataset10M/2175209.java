package org.xorm.test.relationships;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.logging.Logger;
import org.xorm.test.BaseTestSuite;

/**
 *  Test Suite for basic relationships functionality.
 *
 *@author     <a href="mailto:doug@dseifert.net">Doug Seifert</a>
 */
public class RelationshipsTestSuite extends BaseTestSuite {

    public RelationshipsTestSuite() {
        super("Relationships Test Suite");
    }

    /**
     *  Setup the test suite.
     *
     *@return    A new TestSuite
     */
    public static Test suite() {
        RelationshipsTestSuite lSuite = new RelationshipsTestSuite();
        lSuite.addTest(new TestSuite(RelationshipsTestCase.class));
        return lSuite.getTestSetup();
    }
}
