package ch8.p2;

import org.junit.Assert;
import org.junit.Test;

public class QuizTester {

    @Test
    public void QuizTest() {
        DataSet ds = new DataSet(new QuizMeasurer());
        ds.add(new Quiz(98, "A+"));
        ds.add(new Quiz(20, "F"));
        ds.add(new Quiz(50, "C"));
        ds.add(new Quiz(75, "B"));
        ds.add(new Quiz(80, "B+"));
        Quiz qMax = (Quiz) ds.getMaximum();
        Quiz qMin = (Quiz) ds.getMinimum();
        System.out.printf("Highest score: %s %s\n", qMax.getGrade(), qMax.getScore());
        System.out.printf("Lowest score: %s %s\n", qMin.getGrade(), qMin.getScore());
        System.out.printf("Average score: %s", ds.getAverage());
        Assert.assertEquals(64.6, ds.getAverage(), 0);
    }
}
