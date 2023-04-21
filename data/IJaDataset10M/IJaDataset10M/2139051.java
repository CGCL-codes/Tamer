package issuetracker;

import junit.framework.TestCase;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestPrivacy extends TestCase {

    private WicketTester tester;

    @Override
    public void setUp() {
        tester = new WicketTester(new WicketApplication());
    }

    public void testRenderMyPage() {
        tester.startPage(Privacy.class);
        tester.assertRenderedPage(Privacy.class);
    }
}
