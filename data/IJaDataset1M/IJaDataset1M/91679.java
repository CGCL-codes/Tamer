package org.xaware.server.engine.instruction.bizcomps.sql.insert_update;

import org.xaware.testing.functoids.TestingFunctoids;
import org.xaware.testing.util.BaseBdpTestCase;

/**
 * @author Vasu Thadaka
 *
 */
public class SqlInsertUpdateBatchTestCase extends BaseBdpTestCase {

    /**
     * @param p_name
     */
    public SqlInsertUpdateBatchTestCase(String p_name) {
        super(p_name);
    }

    @Override
    protected String getDataFolder() {
        return "data/org/xaware/server/engine/instruction/bizcomps/sql/insert_update/batch/";
    }

    /**
     * This test should have an two rows with the same key.  Because they are in batch
     * two rows will be entered instead of the second one updating the first.  If you removed the
     * transaction or batch so they are not in the same batch, then an update would occur.
     * 
     * Following are the steps
     * Step1: delete all records.
     * Step2: insert_update records.
     * Step3: select all the records to match inserted records.
     *
     */
    public void testInsertUpdateBatch() {
        TestingFunctoids.setProperty("current.environment", "sqlserver");
        testHelper.setTestMethodName("testInsertUpdateBatch");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        setBizDocFileName("cleanCustomers.xbd");
        this.setInputXmlFileName("customerData.xml");
        setExpectedOutputFileName("cleanCustomers_expected.xml");
        evaluateBizDoc();
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        setBizDocFileName("FetchAllCustomers.xbd");
        setExpectedOutputFileName("FetchAllCustomers_clean_expected.xml");
        evaluateBizDoc();
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        setBizDocFileName("loadCustomerBatch.xbd");
        this.setInputXmlFileName("customerData.xml");
        setExpectedOutputFileName("loadCustomers_expected.xml");
        evaluateBizDoc();
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        setBizDocFileName("FetchAllCustomers.xbd");
        setExpectedOutputFileName("FetchAllCustomers_expected.xml");
        evaluateBizDoc();
    }

    /**
     * Tests where there are nulls in the column map values.
     * FIXME -- This needs to be changed to a different schema and table name and
     * those tables created for each developer and branch for build tests.
     */
    public void dtestInsertWithNullsInColumnMap() {
        TestingFunctoids.setProperty("current.environment", "oracle");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        setBizDocFileName("a01_001_cc_pos.xbd");
        this.setInputXmlFileName("a01_001_cc_pos_ix.xml");
        setExpectedOutputFileName("a01_001_cc_pos_expected.xml");
        evaluateBizDoc();
    }
}
