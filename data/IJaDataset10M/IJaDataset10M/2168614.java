package net.sourceforge.cruisecontrol.dashboard.utils.functors;

import java.io.File;
import junit.framework.TestCase;
import net.sourceforge.cruisecontrol.dashboard.utils.CCDateFormatter;
import org.joda.time.DateTime;

public class LastSucceedFilterTest extends TestCase {

    public void testShouldRejectTheFileIfNotSuccessful() {
        ReportableFilter filter = BuildSummariesFilters.lastSucceedFilter(new DateTime());
        filter.accept(new File(""), "log20060704155725.xml");
        assertNull(filter.report());
    }

    public void testShouldReturnLastSuccessfulFile() {
        String[] names = new String[] { "log20060704155725.xml", "log20060704155724Lbuild.503.xml", "log20060704155723Lbuild.503.xml" };
        DateTime time = CCDateFormatter.formatLogName("log20060704155725.xml");
        ReportableFilter filter = BuildSummariesFilters.lastSucceedFilter(time);
        for (int i = 0; i < names.length; i++) {
            filter.accept(new File(""), names[i]);
        }
        assertEquals("log20060704155724Lbuild.503.xml", filter.report().getName());
    }

    public void testShouldReturnNullIfLastSucceedFileCanNotBeFound() {
        String[] names = new String[] { "log20060704155723.xml", "log20060704155724Lbuild.503.xml", "log20060704155725.xml" };
        DateTime time = CCDateFormatter.formatLogName("log20060704155724Lbuild.503.xml");
        ReportableFilter filter = BuildSummariesFilters.lastSucceedFilter(time);
        for (int i = 0; i < names.length; i++) {
            filter.accept(new File(""), names[i]);
        }
        assertNull(filter.report());
    }
}
