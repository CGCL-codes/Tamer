package net.sourceforge.hourglass.reports.timecard;

import java.util.Date;
import net.sourceforge.hourglass.HourglassTestCase;
import net.sourceforge.hourglass.framework.DateUtilities;
import net.sourceforge.hourglass.framework.Project;
import net.sourceforge.hourglass.framework.ProjectGroup;

/**
 * @author Michael K. Grant <mike@acm.jhu.edu>
 */
public class TimecardTreeTableModelTests extends HourglassTestCase {

    private TimecardTreeTableModel m_model;

    private ProjectGroup m_sampleDataHier;

    public TimecardTreeTableModelTests(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        Date dateStart = createDate("2002-12-01T00:00:00.000", "-06:00");
        Date dateEnd = createDate("2002-12-03T00:00:00.000", "-06:00");
        m_sampleDataHier = getSampleDataHier();
        m_model = new TimecardTreeTableModel(m_sampleDataHier, dateStart, dateEnd, DateUtilities.DECIMAL_HOUR_FORMATTER);
    }

    public void testSummaryRow() {
        assertEquals(2, m_model.getChildCount(TimecardTreeTableModel.TREE_ROOT));
        assertEquals("Project 1", ((Project) m_model.getChild(TimecardTreeTableModel.TREE_ROOT, 0)).getName());
        assertEquals(m_sampleDataHier.getRootProject(), m_model.getChild(TimecardTreeTableModel.TREE_ROOT, 1));
    }
}
