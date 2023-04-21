package com.bradrydzewski.gwtgantt.connector;

import com.bradrydzewski.gwtgantt.geometry.Point;
import com.bradrydzewski.gwtgantt.geometry.Rectangle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Tests for {@link StartToStartCalculator}
 * @author Brad Rydzewski
 */
public class StartToStartCalculatorTest {

    /**
     * Instance of {@link  StartToStartCalculator}.
     */
    private StartToStartCalculator calculator = new StartToStartCalculator();

    /**
     * Tests calculating connector for 2 rectangles that both have the
     * same left coordinates. In terms of a gantt chart, this would
     * mean both tasks start at the same time.
     *
     * The connector should render coordinates that look like the
     * following plotted points:
     * <pre>
     *   x------o
     *   |
     *   x------>
     * </pre>
     */
    @Test
    public void testCalculate_SameLeftCoordinates() {
        Rectangle firstTask = new Rectangle(20, 10, 100, 20);
        Rectangle secondTask = new Rectangle(20, 30, 100, 20);
        Point[] points = calculator.calculate(firstTask, secondTask);
        assertTrue("Points are not null", points != null);
        assertTrue("Four points make up connection", points.length == 4);
        assertTrue("Point 1 is (20,20)", points[0].getX() == 20 && points[0].getY() == 20);
        assertTrue("Point 2 is (5,20) ", points[1].getX() == 5 && points[1].getY() == 20);
        assertTrue("Point 3 is (5,40) ", points[2].getX() == 5 && points[2].getY() == 40);
        assertTrue("Point 4 is (20,40)", points[3].getX() == 20 && points[3].getY() == 40);
    }
}
