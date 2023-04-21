package abbot.tester;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import junit.extensions.abbot.*;

/** Test ImageComparator. */
public class ImageComparatorTest extends ComponentTestFixture {

    private ComponentTester tester;

    protected void setUp() {
        tester = new ComponentTester();
    }

    public void testImageCompare() throws Throwable {
        if (Robot.getEventMode() == Robot.EM_ROBOT) {
            File gif = new File("test/abbot/tester/image.gif");
            ImageIcon icon = new ImageIcon(gif.toURL());
            JLabel label = new JLabel(icon);
            showFrame(label);
            tester.actionFocus(label);
            BufferedImage img = tester.capture(label);
            ImageComparator ic = new ImageComparator();
            File jpg = new File("test/abbot/tester/image.jpg");
            assertTrue("Images should not differ", ic.compare(img, jpg) == 0);
        }
    }

    /** Create a new test case with the given name. */
    public ImageComparatorTest(String name) {
        super(name);
    }

    /** Return the default test suite. */
    public static void main(String[] args) {
        TestHelper.runTests(args, ImageComparatorTest.class);
    }
}
