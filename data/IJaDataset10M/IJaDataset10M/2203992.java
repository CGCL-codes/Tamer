package uk101.view;

import uk101.machine.TapeRecorder;

/**
 * Unit test for the Cassette player view
 *
 * @author Baldwin
 */
public class TestCassetteView extends BaseViewTest {

    protected void setupTest() {
        cView = new CassetteView(new TapeRecorder(testComputer.acia));
        testView.add(cView);
    }

    private CassetteView cView;

    public void testWait() throws Exception {
        while (true) {
            Thread.yield();
        }
    }
}
