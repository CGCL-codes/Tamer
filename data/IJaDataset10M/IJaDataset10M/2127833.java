package com.btmatthews.maven.plugins.ldap.test;

import junit.framework.TestCase;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Unit tests for the ldif-loader plugin goal.
 * 
 * @author <a href="mailto:brian.matthews@terranua.com">Brian Matthews</a>
 * @version 1.0
 */
public final class TestLDIFLoaderMojo extends AbstractLDAPMojoTest {

    /**
     * The name of the property that indicates if the plugin should continue
     * when an error is encountered.
     */
    private static final String CONTINUE_ON_ERROR_PROPERTY = "continueOnError";

    /**
     * The goal being tested.
     */
    private static final String GOAL = "ldif-load";

    /**
     * The default constructor.
     */
    public TestLDIFLoaderMojo() {
    }

    /**
     * Test the configuration for the ldif-load goal.
     * 
     * @throws Exception
     *             If something unexpected happens.
     */
    public void testLDIFLoader() throws Exception {
        final Mojo mojo = this.getMojo(TestLDIFLoaderMojo.GOAL);
        TestCase.assertNotNull(mojo);
        this.assertEquals(mojo, AbstractLDAPMojoTest.VERSION_PROPERTY, AbstractLDAPMojoTest.VERSION);
        this.assertEquals(mojo, AbstractLDAPMojoTest.HOST_PROPERTY, AbstractLDAPMojoTest.LOCALHOST);
        this.assertEquals(mojo, AbstractLDAPMojoTest.PORT_PROPERTY, AbstractLDAPMojoTest.PORT);
        this.assertEquals(mojo, AbstractLDAPMojoTest.AUTH_DN_PROPERTY, AbstractLDAPMojoTest.DN);
        this.assertEquals(mojo, AbstractLDAPMojoTest.PASSWD_PROPERTY, AbstractLDAPMojoTest.PASSWD);
        this.assertEquals(mojo, TestLDIFLoaderMojo.CONTINUE_ON_ERROR_PROPERTY, true);
        mojo.execute();
    }

    /**
     * Test the configuration for the ldif-load goal.
     * 
     * @throws Exception
     *             If something unexpected happens.
     */
    public void testLDIFLoaderWithoutContinueOnError() throws Exception {
        final Mojo mojo = this.getMojo(TestLDIFLoaderMojo.GOAL);
        TestCase.assertNotNull(mojo);
        this.assertEquals(mojo, AbstractLDAPMojoTest.VERSION_PROPERTY, AbstractLDAPMojoTest.VERSION);
        this.assertEquals(mojo, AbstractLDAPMojoTest.HOST_PROPERTY, AbstractLDAPMojoTest.LOCALHOST);
        this.assertEquals(mojo, AbstractLDAPMojoTest.PORT_PROPERTY, AbstractLDAPMojoTest.PORT);
        this.assertEquals(mojo, AbstractLDAPMojoTest.AUTH_DN_PROPERTY, AbstractLDAPMojoTest.DN);
        this.assertEquals(mojo, AbstractLDAPMojoTest.PASSWD_PROPERTY, AbstractLDAPMojoTest.PASSWD);
        this.assertEquals(mojo, TestLDIFLoaderMojo.CONTINUE_ON_ERROR_PROPERTY, false);
        try {
            mojo.execute();
            TestCase.fail(AbstractLDAPMojoTest.MOJO_EXCEPTION_EXPECTED);
        } catch (MojoExecutionException e) {
            TestCase.assertTrue(true);
        }
    }
}
