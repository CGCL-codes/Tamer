package org.SoccerTournament.Webapp;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage extends TestCase {

    private WicketTester tester;

    @Override
    public void setUp() {
        tester = new WicketTester(new WicketApplication());
    }

    public void testRenderMyPage() {
        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.assertLabel("message", "If you see this message wicket is properly configured and running");
    }
}
