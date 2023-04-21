package org.databene.dbsanity.report;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the {@link ReportUtil} class.<br/><br/>
 * Created: 27.10.2010 13:26:13
 * @since 1.0
 * @author Volker Bergmann
 */
public class ReportUtilTest {

    @Test
    public void testNormalizeUrl() {
        assertEquals("ABCDE1234.-_+", ReportUtil.normalizeUrl("ABCDE1234.-_+"));
        assertEquals("ABCDEx1234", ReportUtil.normalizeUrl("ABCD!?/\\E*1234"));
        assertEquals("alpha_beta", ReportUtil.normalizeUrl("alpha beta"));
        assertEquals("test_xyz", ReportUtil.normalizeUrl("test 'xyz'"));
    }
}
