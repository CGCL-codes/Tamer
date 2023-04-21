package com.googlecode.mvnmigrate;

import com.googlecode.mvnmigrate.AbstractCommandMojo;

/**
 * @version $Id: BootstrapCommandMojoTest.java 132 2010-07-10 17:23:54Z marco.speranza79 $
 */
public class BootstrapCommandMojoTest extends AbstractMigrateTestCase {

    public void testBootstrapGoal() throws Exception {
        AbstractCommandMojo mojo = (AbstractCommandMojo) lookupMojo("bootstrap", testPom);
        assertNotNull(mojo);
        mojo.execute();
    }
}
