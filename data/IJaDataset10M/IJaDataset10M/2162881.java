package lcd_api.widgets;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This is a test suite containing automatic JUnit tests for all of the 
 * Widgets as well as the main Factory added to the API. Executing this 
 * test will execute all of the sub tests in one shot.
 * 
 * @author Robert Derelanko
 */
public class WidgetTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Widget Tests");
        suite.addTestSuite(StringWidgetTest.class);
        suite.addTestSuite(HorizontalBarWidgetTest.class);
        suite.addTestSuite(TitleWidgetTest.class);
        suite.addTestSuite(ScrollerWidgetTest.class);
        suite.addTestSuite(IconWidgetTest.class);
        suite.addTestSuite(WidgetFactoryTest.class);
        return suite;
    }
}
