package edu.mit.csail.pag.amock.subjects.fields;

import edu.mit.csail.pag.amock.tests.AmockUnitTestCase;
import java.awt.Rectangle;

public class RectangleSystem {

    public static void main(String[] args) {
        Rectangle r = new Rectangle(23, 34, 4, 7);
        RectangleHelper h = new RectangleHelper(r);
        h.returnXMaybeTweak(false);
        h.returnXMaybeTweak(false);
    }

    public static class ProcessorTests extends AmockUnitTestCase {

        public void testNothing() {
        }
    }
}
