package com.llq.studentinfo;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.llq.studentinfo.BasicGradingStrategy;
import com.llq.studentinfo.GradingStrategy;
import com.llq.studentinfo.HonorsGradingStrategy;
import com.llq.studentinfo.Student.Grade;

/**
 * �ȼ�������Ե�Test����
 * @author hp
 *
 */
public class GradingStrategyTest {

    @Test
    public void BasicGradingStrategyTest() {
        GradingStrategy strategy = new BasicGradingStrategy();
        assertEquals(4, strategy.getGradePointsFor(Grade.A));
        assertEquals(3, strategy.getGradePointsFor(Grade.B));
        assertEquals(2, strategy.getGradePointsFor(Grade.C));
        assertEquals(1, strategy.getGradePointsFor(Grade.D));
        assertEquals(0, strategy.getGradePointsFor(Grade.F));
    }

    @Test
    public void HonorsGradingStrategyTest() {
        GradingStrategy strategy = new HonorsGradingStrategy();
        assertEquals(5, strategy.getGradePointsFor(Grade.A));
        assertEquals(4, strategy.getGradePointsFor(Grade.B));
        assertEquals(3, strategy.getGradePointsFor(Grade.C));
        assertEquals(2, strategy.getGradePointsFor(Grade.D));
        assertEquals(0, strategy.getGradePointsFor(Grade.F));
    }
}
