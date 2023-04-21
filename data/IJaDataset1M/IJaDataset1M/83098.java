package org.xaware.salesforce.bizcomp;

import org.custommonkey.xmlunit.XMLUnit;
import org.xaware.testing.functoids.TestingFunctoids;
import org.xaware.testing.util.BaseBdpTestCase;

/**
 * @author tferguson
 * 
 * This test case is for testing the Salesforce Biz Comp.  It runs actual biz docs calling the
 * salesforce bizcomp to run the tests.  The tests will not do anything if the following
 * properties are not setup in your buildoptions.properties.
 *      <li>salesforce.user</li>
 *      <li>salesforce.password</li>
 *      <li>salesforce.timeout</li>
 * 
 */
public class SalesforceNoWhereFragmentsTestCase extends BaseBdpTestCase {

    private boolean propertiesSet;

    /**
     * @param p_name
     */
    public SalesforceNoWhereFragmentsTestCase(String name) {
        super(name);
        try {
            TestingFunctoids.getProperty("salesforce.user");
            TestingFunctoids.getProperty("salesforce.pwd");
            TestingFunctoids.getProperty("salesforce.timeout");
        } catch (IllegalArgumentException e) {
            e.printStackTrace(System.out);
            System.out.println("The above stack trace indicates that you don't have salesforce.user, salesforce.pwd, or salesforce.timeout specified in your buildoptions.properties");
            propertiesSet = false;
            return;
        }
        XMLUnit.setIgnoreWhitespace(true);
        propertiesSet = true;
    }

    @Override
    protected String getDataFolder() {
        return "data/org/xaware/salesforce/bizcomp/";
    }

    /**
     * This test clears any records that would interfere with the below tests
     *
     */
    public void testClearTestData() {
        if (!propertiesSet) {
            return;
        }
        testHelper.setTestMethodName("testClearTestData");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        this.setInputXmlFileName("sf_insert_input.xml");
        setBizDocFileName("sf_cleanup.xbd");
        setExpectedOutputFileName("sf_cleanup_expected.xml");
        evaluateBizDoc();
        this.setInputXmlFileName("sf_insert_input.xml");
        setBizDocFileName("sf_select.xbd");
        setExpectedOutputFileName("sf_select_cleared_expected.xml");
        evaluateBizDoc();
    }

    /**
     * This test Inserts a Lead record and then selects it to make sure it is really there
     */
    public void testBasicInsert() {
        if (!propertiesSet) {
            return;
        }
        testHelper.setTestMethodName("testBasicInsert");
        setUseXmlUnit(true);
        this.setSimilarAllowed(true);
        setOnlyCheckStructure(true);
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        this.setInputXmlFileName("sf_insert_input.xml");
        setBizDocFileName("sf_insert.xbd");
        setExpectedOutputFileName("sf_insert_expected.xml");
        evaluateBizDoc();
        setUseXmlUnit(false);
        setOnlyCheckStructure(false);
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        this.setInputXmlFileName("sf_insert_input.xml");
        setBizDocFileName("sf_select.xbd");
        setExpectedOutputFileName("sf_select_expected.xml");
        evaluateBizDoc();
    }

    /**
     * This test updates the description field of the record inserted earlier
     * without specifying the id
     */
    public void testBasicUpdateNonId() {
        if (!propertiesSet) {
            return;
        }
        testHelper.setTestMethodName("testBasicUpdateNonId");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        this.setInputXmlFileName("sf_insert_input.xml");
        setBizDocFileName("sf_update_nonid_nofrag.xbd");
        setExpectedOutputFileName("sf_update_nonid_nofrag_expected.xml");
        evaluateBizDoc();
    }

    /**
     * This test updates the description field of the record inserted earlier
     * without specifying the id
     */
    public void testUpsertNonIdWithWhereClause() {
    }

    public void testDeleteNonId() {
        if (!propertiesSet) {
            return;
        }
        testHelper.setTestMethodName("testDeleteNonId");
        setUseXmlUnit(true);
        this.setSimilarAllowed(true);
        setOnlyCheckStructure(true);
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        this.setInputXmlFileName("sf_insert_batched_input.xml");
        setBizDocFileName("sf_insert_batch.xbd");
        setExpectedOutputFileName("sf_insert_deletenonid_expected.xml");
        evaluateBizDoc();
        setUseXmlUnit(true);
        this.setSimilarAllowed(true);
        setOnlyCheckStructure(true);
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        this.setInputXmlFileName("sf_insert_input.xml");
        setBizDocFileName("sf_delete_nonid_nofrag.xbd");
        setExpectedOutputFileName("sf_delete_nonid_nofrag_expected.xml");
        evaluateBizDoc();
        setUseXmlUnit(false);
        this.setSimilarAllowed(false);
        setOnlyCheckStructure(false);
    }
}
