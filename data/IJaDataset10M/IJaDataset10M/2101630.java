package cz.langteacher.model;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import cz.langteacher.AbstractTestCase;
import cz.langteacher.exam.ExamDirection;
import cz.langteacher.manager.ExamStatisticManager;

public class ExamStatisticTest extends AbstractTestCase {

    private ExamStatisticManager stat = new ExamStatisticManager();

    @Before
    public void init() {
        super.init();
        stat.addData(d1, true, 4000, ExamDirection.BASE_TO_STUDIED);
        stat.addData(d5, false, 3456, ExamDirection.BY_DESCRIPTION);
        stat.addData(d1, false, 2315, ExamDirection.RANDOMLY);
        stat.addData(d2, true, 2345, ExamDirection.BASE_TO_STUDIED);
        stat.addData(d2, false, 5634, ExamDirection.STUDIED_TO_BASE);
        stat.addData(d4, true, 2345, ExamDirection.BY_DESCRIPTION);
        stat.addData(d3, true, 9765, ExamDirection.BASE_TO_STUDIED);
        stat.addData(d1, false, 3212, ExamDirection.STUDIED_TO_BASE);
        stat.addData(d1, true, 5678, ExamDirection.BY_DESCRIPTION);
        stat.addData(d5, false, 5678, ExamDirection.STUDIED_TO_BASE);
        stat.addData(d5, true, 1234, ExamDirection.RANDOMLY);
        stat.addData(d1, false, 7854, ExamDirection.BY_DESCRIPTION);
        stat.addData(d4, true, 5642, ExamDirection.BASE_TO_STUDIED);
        stat.addData(d4, false, 7843, ExamDirection.STUDIED_TO_BASE);
        stat.addData(d4, true, 6432, ExamDirection.STUDIED_TO_BASE);
        stat.addData(d1, false, 4642, ExamDirection.RANDOMLY);
        stat.addData(d3, false, 4789, ExamDirection.BASE_TO_STUDIED);
    }

    @Test
    public void getMostUnSuccessful() {
        Assert.assertEquals("", d1, stat.getMostUnsuccessful());
        Assert.assertEquals("", d4, stat.getMostSuccessful());
        System.out.println(stat.getPercentageFor(d1, true));
        System.out.println(stat.getPercentageFor(d4, true));
        System.out.println(stat.getPercentageSuccess());
    }
}
