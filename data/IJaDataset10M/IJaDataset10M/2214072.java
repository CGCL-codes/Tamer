package com.googlecode.jchav.data;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test of the PageData implementation
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class PageDataImplTest {

    /**
     * For v4 junit tests to run through ant we currently need the adapter.
     * @return suite of tests.
     */
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(PageDataImplTest.class);
    }

    /**
     * Ensure that the first page is always the summary page.
     *
     */
    @Test
    public void testSummaryIsTheFirstPage() {
        BuildIdImpl id = new BuildIdImpl("beta", 1);
        PageDataImpl data = new PageDataImpl();
        data.addMeasurement("Z Page", "Z Page", new MeasurementImpl(id, 1L, 2L, 3L));
        data.addMeasurement(PageData.SUMMARY_PAGE_ID, PageData.SUMMARY_PAGE_ID, new MeasurementImpl(id, 1L, 2L, 3L));
        data.addMeasurement("A Page", "A Page", new MeasurementImpl(id, 1L, 2L, 3L));
        for (String page : data.getPageIds()) {
            assertEquals(PageData.SUMMARY_PAGE_ID, page);
            break;
        }
    }
}
