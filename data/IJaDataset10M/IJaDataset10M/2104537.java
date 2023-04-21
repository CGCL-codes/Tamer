package org.p4pp.p3p.appel.ruleset;

import org.p4pp.util.uri.InvalidOrUnsupportedUriException;
import org.p4pp.p3p.document.ExpiryDate;
import org.p4pp.p3p.document.ExpiryInThePastException;
import org.p4pp.p3p.document.P3PDocumentNotConstructableException;
import org.p4pp.p3p.document.NoSuchPolicyException;
import org.p4pp.p3p.document.Policy;
import org.p4pp.p3p.document.PolicyCollection;
import junit.framework.*;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *A class
 *
 * @author  Nikolaj
 */
public class EvidenceTest extends TestCase {

    /** The directory containing the test files for this  test. */
    private static final File testDir = createTestDir();

    /**
     *Creates a new EvidenceTest instance.
     *
     * @param  name A parameter
     */
    public EvidenceTest(String name) {
        super(name);
    }

    /**
     * Returns a suite of all functions called "test...()"
     *
     * @return  a <code>Test</code> value
     */
    public static Test suite() {
        return new TestSuite(EvidenceTest.class);
    }

    /**
     * runs all functions called "test...()" as tests
     *
     * @param  args A parameter
     */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     *A method
     *
     * @return  A return value
     */
    static File createTestDir() {
        String sep = File.separator;
        URL testfilesDirURL = ClassLoader.getSystemResource("org" + sep + "p4pp" + sep + "p3p" + sep + "appel" + sep + "ruleset" + sep + "testfiles");
        return new File(testfilesDirURL.getFile());
    }

    /**
     * Tests creation and getters
     *
     * @exception  MalformedURLException The name says it all...
     * @exception  ExpiryInThePastException The name says it all...
     * @exception  ParseException The name says it all...
     * @exception  P3PDocumentNotConstructableException The name says it all...
     * @exception  InvalidOrUnsupportedUriException The name says it all...
     * @exception  NoSuchPolicyException The name says it all...
     */
    public void testCreationAndGetters() throws MalformedURLException, ExpiryInThePastException, ParseException, P3PDocumentNotConstructableException, InvalidOrUnsupportedUriException, NoSuchPolicyException {
        URL urlOfTheRequestedSource = new URL("http://www.server.org/a/path/file.html");
        File file = new File(testDir, "firstPolicy.p3p");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = dateFormat.parse("07.10.2010");
        URL url = new URL("HTTP://www.dummy.de/url.url");
        assertTrue("date is null!", date != null);
        PolicyCollection policies = new PolicyCollection(file, url);
        Policy policy = policies.getPolicyByName("One");
        Evidence evidence = new Evidence(urlOfTheRequestedSource, policy);
        assertEquals("URL given constructing Evidence instance is not equal to url returned from getter.", urlOfTheRequestedSource, evidence.getUriOfTheRequestedSource().asUrl());
        assertSame("Policy given constructing an Evidence instance is not the same to that one returned by getPolicy()", policy, evidence.getPolicy());
    }

    /** does nothing */
    protected void setUp() {
    }

    /** does nothing */
    protected void tearDown() {
    }
}
