package net.sf.sail.webapp.service.file.impl;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

/**
 * @author Laurel Williams
 * 
 * @version $Id: AuthoringJNLPModifierTest.java 2120 2008-09-24 19:41:21Z hiroki $
 */
public class AuthoringJNLPModifierTest extends TestCase {

    private static final String inputJnlp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<jnlp codebase=\"" + "http://tels-develop.soe.berkeley.edu:8080/jnlp\" spec=\"1.0+\"> <information><title>Authoring" + "Everything Snapshot</title>	<vendor>TELS Center</vendor><homepage href=\"index.html\" /><description>" + "Preview Basic Pas</description></information><security><all-permissions /></security>" + "<resources> <j2se initial-heap-size=\"32m\" max-heap-size=\"128m\" version=\"1.5+\" />" + "<property name=\"maven.jnlp.version\" value=\"authoring-everything-snapshot-0.1.0-20070511.182952\" />" + "<jar href=\"org/concord/framework/framework.jar\" version=\"0.1.0-20070511.131402-50\" />" + "<jar href=\"com/webrenderer/webrenderer-win/webrenderer-win.jar\" version=\"3.0\" /> </resources>" + "<resources os=\"Linux\"> " + "<nativelib href=\"org/telscenter/java/dev/jdic-native/jdic-native-linux-nar.jar\" version=\"20060613\" /> " + "<jar href=\"org/telscenter/java/dev/jdic/jdic-linux.jar\" version=\"20060613\" />" + "</resources> <application-desc main-class=\"org.telscenter.proprietary.TelsProprietaryLauncher\" /> </jnlp>";

    private static final String curnitURL = "test.jar";

    private static final Long PROJECTID = new Long(1);

    private static final String outputJnlp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><jnlp codebase=\"" + "http://tels-develop.soe.berkeley.edu:8080/jnlp\" spec=\"1.0+\"> <information><title>Authoring" + "Everything Snapshot</title>	<vendor>TELS Center</vendor><homepage href=\"index.html\" /><description>" + "Preview Basic Pas</description></information><security><all-permissions /></security>" + "<resources> <j2se initial-heap-size=\"32m\" max-heap-size=\"128m\" version=\"1.5+\" />" + "<property name=\"maven.jnlp.version\" value=\"authoring-everything-snapshot-0.1.0-20070511.182952\" />" + "<jar href=\"org/concord/framework/framework.jar\" version=\"0.1.0-20070511.131402-50\" />" + "<jar href=\"com/webrenderer/webrenderer-win/webrenderer-win.jar\" version=\"3.0\" /> " + "<property name=\"jnlp.curnit_url.get\" value=\"test.jar\" />" + "<property name=\"jnlp.curnit_url.post\" value=\"\" />" + "<property name=\"jnlp.runmode\" value=\"web\" />" + "<property name=\"jnlp.portal_baseurl\" value=\"\" />" + "<property name=\"jnlp.project.id\" value=\"1\" />" + "</resources>" + "<resources os=\"Linux\"> <nativelib href=\"org/telscenter/java/dev/jdic-native/jdic-native-linux-nar.jar\"" + "version=\"20060613\" /> <jar href=\"org/telscenter/java/dev/jdic/jdic-linux.jar\" version=\"20060613\" />" + "</resources> <application-desc main-class=\"org.telscenter.proprietary.TelsProprietaryLauncher\" /> </jnlp>";

    private static final String outputJnlp2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><jnlp codebase=\"" + "http://tels-develop.soe.berkeley.edu:8080/jnlp\" spec=\"1.0+\"> <information><title>Authoring" + "Everything Snapshot</title>	<vendor>TELS Center</vendor><homepage href=\"index.html\" /><description>" + "Preview Basic Pas</description></information><security><all-permissions /></security>" + "<resources> <j2se initial-heap-size=\"32m\" max-heap-size=\"128m\" version=\"1.5+\" />" + "<property name=\"maven.jnlp.version\" value=\"authoring-everything-snapshot-0.1.0-20070511.182952\" />" + "<jar href=\"org/concord/framework/framework.jar\" version=\"0.1.0-20070511.131402-50\" />" + "<jar href=\"com/webrenderer/webrenderer-win/webrenderer-win.jar\" version=\"3.0\" /> " + "<property name=\"jnlp.curnit_url.get\" value=\"test.jar\" />" + "<property name=\"jnlp.curnit_url.post\" value=\"http://localhost:8080/webapp/author/project/postproject.html?projectId=3\" />" + "<property name=\"jnlp.runmode\" value=\"web\" />" + "<property name=\"jnlp.portal_baseurl\" value=\"http://localhost:8080/webapp\" />" + "<property name=\"jnlp.project.id\" value=\"1\" />" + "</resources>" + "<resources os=\"Linux\"> <nativelib href=\"org/telscenter/java/dev/jdic-native/jdic-native-linux-nar.jar\"" + "version=\"20060613\" /> <jar href=\"org/telscenter/java/dev/jdic/jdic-linux.jar\" version=\"20060613\" />" + "</resources> <application-desc main-class=\"org.telscenter.proprietary.TelsProprietaryLauncher\" /> </jnlp>";

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testmodifyJNLP() throws Exception {
        AuthoringJNLPModifier modifier = new AuthoringJNLPModifier();
        assertEquals(StringUtils.deleteWhitespace(outputJnlp), StringUtils.deleteWhitespace(modifier.modifyJnlp(inputJnlp, curnitURL, PROJECTID)));
    }

    public void testmodifyJNLP_2() throws Exception {
        AuthoringJNLPModifier modifier = new AuthoringJNLPModifier();
        assertEquals(StringUtils.deleteWhitespace(outputJnlp2), StringUtils.deleteWhitespace(modifier.modifyJnlp(inputJnlp, curnitURL, PROJECTID, "http://localhost:8080/webapp", "http://localhost:8080/webapp/author/project/postproject.html?projectId=3")));
    }
}
