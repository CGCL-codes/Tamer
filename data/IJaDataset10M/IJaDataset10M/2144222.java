package com.sun.ebxml.registry.conformance;

import junit.framework.*;
import com.sun.ebxml.registry.interfaces.soap.*;
import javax.xml.soap.*;
import java.io.*;

/**
 * Set of Preliminary OASIS/ebXML Registry Test Plan and Test Requirements
 *
 * PHASE I: Test setup and initialisation
 *
 * Use following properties to configure parameters:
 *
 * jaxr.sample.dir = directory of NIST test files
 * jaxr.sample.result = directory where result files will be stored
 *
*
 * @author Michal Zaremba
 */
public class SetConformanceTests extends TestCase {

    private static String url = null;

    private SOAPSender sender;

    private SOAPMessage reply;

    private TestSupport supportMethods;

    /**
     * Constructs a SetNISTTest with the specified name
     *
     * @param name Test case name
     */
    public SetConformanceTests(String name) {
        super(name);
        supportMethods = new TestSupport();
        supportMethods.deleteRaport("raport.txt");
        try {
            sender = supportMethods.connectRegistry(url);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Tests failed during Set up phase");
        }
    }

    /**
     * Sets up the text fixture.
     *
     * Called before every test case method.
     */
    protected void tearDown() throws Exception {
        System.gc();
    }

    /** 
     * TEST I
     *
     * 2.3 REGISTRATION AUTHORITY 
     *
     * Creation of a Registration Authority for the Registry
     *
     * Submision one XML Organisation element and exactly one XML User
     *
     */
    public void testSubmitRA() {
        try {
            supportMethods.printMessage("SUBMIT REGISTRATION AUTHORITY");
            supportMethods.submitFileToRegistry(sender, "SubmitRA.xml");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception has been thrown in 2.3 durign Registration Authority");
        }
    }

    /**
     * TEST II
     *
     * 2.4 SUBMITTING ORGANISATIONS
     * 
     * Create two submitting organisations with the names:
     *
     * SubmitOrgFrequent and SubmitOrgParent
     * 
     */
    public void testSubmitOrg() {
        try {
            supportMethods.printMessage("SUBMIT ORGANISATION - SubmitOrgFrequent");
            supportMethods.submitFileToRegistry(sender, "SubmitSOF.xml");
            supportMethods.printMessage("SUBMIT ORGANISATION - SubmitOrgParent");
            supportMethods.submitFileToRegistry(sender, "SubmitSOP.xml");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception has been thrown in 2.4 during Submitting Organisations");
        }
    }

    /**
     * TEST III
     *
     * 2.5 CHILD ORGANISATION
     * 
     * Create a new organisation that reference an existing organisation as its parent
     *
     * Determine 128 bit UUID for SubmitOrgParent and after SubmitSOC
     * 
     */
    public void testChildOrganisation() {
        try {
            supportMethods.printMessage("DETERMINE UUID FROM SubmitOrgParent");
            reply = supportMethods.submitFileToRegistry(sender, "FindSOP128bitUUID.xml");
            String UUID = supportMethods.getAttributeValue(reply, "ObjectRef", "id");
            supportMethods.setUUIDInFile(UUID, "SOP128bitUUID", "SubmitSOC.xml");
            supportMethods.printMessage("SUBMIT CHILD ORGANISATION - SubmitOrgChild");
            supportMethods.submitFileToRegistry(sender, "SubmitSOC_new.xml");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException in testChildOrganisation Test");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception has been thrown in 2.5 during submitting Child organisation");
        }
    }

    /**
     * TEST IV
     *
     * 2.6 RESPONSIBLE ORGANISATIONS
     * 
     * Submit ResponsibleOrg
     * 
     */
    public void testSubmitResponsibleOrg() {
        try {
            supportMethods.printMessage("SUBMIT RESPONSIBLE ORGANISATION - ResponsibleOrg");
            supportMethods.submitFileToRegistry(sender, "SubmitRORG.xml");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception has been thrown in 2.6 during Submitting Responsible Organisation");
        }
    }

    /**
     * TEST V
     *
     * 2.7 ADDITIONAL USERS
     * 
     *
     * Determine 128 bit UUID for SubmitOrgFrequent and after SubmitSOC2nd
     * 
     */
    public void testAdditionalUsers() {
        try {
            supportMethods.printMessage("DETERMINE UUID FROM SubmitOrgFrequent");
            reply = supportMethods.submitFileToRegistry(sender, "FindSOF128bitUUID.xml");
            String UUID = supportMethods.getAttributeValue(reply, "ObjectRef", "id");
            supportMethods.setUUIDInFile(UUID, "SOF128bitUUID", "SubmitSOF2nd.xml");
            supportMethods.printMessage("CREATE A SECOND USER FOR SubmitOrgFrequent");
            supportMethods.submitFileToRegistry(sender, "SubmitSOF2nd_new.xml");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("IOException in testAdditionalUser Test");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception has been thrown in 2.7 during submitting additional users");
        }
    }

    /**
     * Add tests to the suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new SetConformanceTests("testSubmitRA"));
        suite.addTest(new SetConformanceTests("testSubmitOrg"));
        suite.addTest(new SetConformanceTests("testChildOrganisation"));
        suite.addTest(new SetConformanceTests("testSubmitResponsibleOrg"));
        suite.addTest(new SetConformanceTests("testAdditionalUsers"));
        return suite;
    }

    public void runMyTests() {
        TestSuite suite = new TestSuite();
        suite.addTest(suite());
        TestResult result = new TestResult();
        suite.run(result);
        supportMethods.processResults("Test Setup and initialisation", result);
    }

    public static void main(String[] args) {
        SetConformanceTests test = new SetConformanceTests("SetConformanceTests");
        url = TestSupport.getURL(args);
        test.runMyTests();
    }
}
