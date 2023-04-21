package com.hack23.cia.model.api.sweden.content;

import org.junit.Assert;
import org.junit.Test;
import com.hack23.cia.model.api.sweden.configuration.CommitteeData;
import com.hack23.cia.model.api.sweden.configuration.CommitteeReportData;
import com.hack23.cia.testfoundation.TestInstance;

/**
 * The Class CommitteeTest.
 */
public class CommitteeTest extends AbstractSwedenModelApiUnitTest {

    /**
	 * Test to write.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @Test
    public void testToWrite() throws Exception {
        new TestInstance() {

            private final CommitteeData committeeData = getSwedenModelFactory().createCommitteeData("name", "shortcode");

            private CommitteeReportData committeeReport;

            @Override
            protected void assertPostcondition() {
                Assert.assertNotNull(committeeData);
                Assert.assertEquals(committeeReport, committeeData.getCommitteeReportsData().iterator().next());
            }

            @Override
            protected void run() {
                committeeData.addCommitteeReportData(committeeReport);
            }

            @Override
            protected void setupExpectations() {
                committeeReport = getSwedenModelFactory().createCommitteeReportData("name", "shortCode", getSwedenModelFactory().createParliamentYearData());
            }
        }.executeTestPhases();
    }
}
