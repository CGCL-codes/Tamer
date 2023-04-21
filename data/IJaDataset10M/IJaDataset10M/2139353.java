package elliott803.view;

import elliott803.hardware.PaperTapeStation;

/**
 * Unit test for the paper tape station view
 * 
 * @author Baldwin
 */
public class TestPtsView extends BaseViewTest {

    protected void setupTest() {
        ptsView = new PtsView(new PaperTapeStation(testComputer));
        testView.add(ptsView);
    }

    private PtsView ptsView;

    public void testRandom() throws Exception {
        while (true) {
            Thread.sleep(50);
        }
    }
}
