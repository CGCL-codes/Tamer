package org.jcvi.vics.compute.service.blast;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jcvi.vics.compute.api.EJBFactory;

/**
 * Created by IntelliJ IDEA.
 * User: tnabeel
 * Date: May 22, 2007
 * Time: 2:58:43 PM
 *
 */
public class BlastTestErrors extends BlastTestBase {

    public BlastTestErrors() {
        super();
    }

    public BlastTestErrors(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        setBlastDatasetName(GOS_CHESAPEAKE_BAY);
    }

    public void testBlastAllWarnings() throws Exception {
        setExpectedHits(4L);
        setExpectedMessageCount(7);
        setBlastInputFastaText(">62005829\nGTGATGGTGGTGGCGGTGGTGATGGTGATGGTGATGGTGATGGT\n>62004030\nTGATGATGATGATGATGATGATGATGATGATGATGAGGATGAGGATGATGATGATGATGA\n>62006130\nTGATGATGATTGATGATGATGATGATGATGATGCTGATGATGATGATGATGATGATGA\n>62009466\nCTGCAGTCAGCTTCAAAATCGCATCATTTCTTAGGAGTAAACGAAAAT\n>62009467\nAAAAAATGTTCAATCATGTTCAAAAATACTGAAATTTCAAAAAACATATTTATCGAAATAAAAAAAAAAAAAAAA\n>62001882\nCAGACAGACAGACAGACAGACCGACAGACAGACAGACAGACAGACAGACAGACAGACAGACAGACGAGAC\n>62001369\nGTGTGTCATTGTGTGTCATTGTGTGTCATTGTGTGTCATTGTGTGTGTATCATAATGTGTG\n>62004557\nATATATTGATATATTGATATATTGATATATTGATATATTGATATATTGATATATTGATAT");
        submitJobAndWaitForCompletion("TestBlast");
        validateHits();
        validateMessageCount();
    }

    public void testBlastWarningsAndSuccess() throws Exception {
        setExpectedHits(22L);
        setExpectedMessageCount(6);
        setBlastInputFastaText(">mydefine\nTTGGGGATCGTGCTGGGTGTCATTGTTGCTTTCGTGTCAGCGGTGGTTGCTGTTGGTCGCTTCCTGATTG\n>62004030\nTGATGATGATGATGATGATGATGATGATGATGATGAGGATGAGGATGATGATGATGATGA\n>62006130\nTGATGATGATTGATGATGATGATGATGATGATGCTGATGATGATGATGATGATGATGA\n>62009466\nCTGCAGTCAGCTTCAAAATCGCATCATTTCTTAGGAGTAAACGAAAAT\n>62009467\nAAAAAATGTTCAATCATGTTCAAAAATACTGAAATTTCAAAAAACATATTTATCGAAATAAAAAAAAAAAAAAAA\n>62001882\nCAGACAGACAGACAGACAGACCGACAGACAGACAGACAGACAGACAGACAGACAGACAGACAGACGAGAC\n>62001369\nGTGTGTCATTGTGTGTCATTGTGTGTCATTGTGTGTCATTGTGTGTGTATCATAATGTGTG\n>62004557\nATATATTGATATATTGATATATTGATATATTGATATATTGATATATTGATATATTGATAT");
        submitJobAndWaitForCompletion("BlastWithGridMerge");
        validateHits();
        validateMessageCount();
    }

    public void testBlastnNoHits() throws Exception {
        setExpectedHits(0L);
        setExpectedMessageCount(0);
        setBlastInputFastaText(">mydefine\n4444444444");
        submitJobAndWaitForCompletion("BlastWithGridMerge");
        validateHits();
        validateMessageCount();
    }

    public void testBlastError() throws Exception {
        try {
            submitJobAndWaitForCompletion("TestBlastError");
        } catch (Exception e) {
            verifyErrorCompletion();
            assertNull(EJBFactory.getRemoteComputeBean().getBlastHitCountByTaskId(getTaskId()));
            return;
        }
        fail("Should have caught exception");
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(BlastTestErrors.class);
        return suite;
    }
}
