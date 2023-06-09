package ma.glasnost.orika.test.util;

import junit.framework.Assert;
import ma.glasnost.orika.metadata.NestedProperty;
import ma.glasnost.orika.property.PropertyResolver;
import org.junit.Test;

public class PropertiesTestCase {

    @Test
    public void testNestedProperty() {
        String np = "start.x";
        NestedProperty p = PropertyResolver.getInstance().getNestedProperty(Line.class, np);
        Assert.assertEquals(Integer.TYPE, p.getRawType());
    }

    public static class Point {

        private int x, y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public static class Line {

        private Point start;

        private Point end;

        public Point getStart() {
            return start;
        }

        public void setStart(Point start) {
            this.start = start;
        }

        public Point getEnd() {
            return end;
        }

        public void setEnd(Point end) {
            this.end = end;
        }
    }

    public static class LineDTO {

        private int x0, y0, x1, y1;

        public int getX0() {
            return x0;
        }

        public void setX0(int x0) {
            this.x0 = x0;
        }

        public int getY0() {
            return y0;
        }

        public void setY0(int y0) {
            this.y0 = y0;
        }

        public int getX1() {
            return x1;
        }

        public void setX1(int x1) {
            this.x1 = x1;
        }

        public int getY1() {
            return y1;
        }

        public void setY1(int y1) {
            this.y1 = y1;
        }
    }
}
