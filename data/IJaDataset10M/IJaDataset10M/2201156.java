package be.lassi.lanbox.cuesteps;

import java.util.Iterator;
import java.util.List;
import be.lassi.lanbox.cuesteps.CueStep;

public class AllCueStepTests {

    private CueStepTests cueStepTests = new CueStepTests();

    public AllCueStepTests() {
        add(ClearLayerTestCase.createTests());
        add(CommentTestCase.createTests());
        add(CueReferenceSceneTestCase.createTests());
        add(CueSceneTestCase.createTests());
        add(GoLayerCueListTestCase.createTests());
        add(GoNextTestCase.createTests());
        add(GoPreviousTestCase.createTests());
        add(GotoCueStepTestCase.createTests());
        add(LoopToTestCase.createTests());
        add(ResetLayerTestCase.createTests());
        add(ResumeLayerTestCase.createTests());
        add(SetLayerAttributesTestCase.createTests());
        add(SetLayerChaseTestCase.createTests());
        add(SetLayerMixModeTestCase.createTests());
        add(StartLayerTestCase.createTests());
        add(StopLayerTestCase.createTests());
        add(SuspendLayerTestCase.createTests());
        add(WaitLayerTestCase.createTests());
    }

    public CueStepTests getTests() {
        return cueStepTests;
    }

    public CueStep[] getCueSteps() {
        List<CueStepTest> tests = cueStepTests.getTests();
        CueStep[] cueSteps = new CueStep[tests.size()];
        for (int i = 0; i < cueSteps.length; i++) {
            cueSteps[i] = tests.get(i).getCueStep();
        }
        return cueSteps;
    }

    private void add(final CueStepTests tests) {
        Iterator<CueStepTest> i = tests.getTests().iterator();
        while (i.hasNext()) {
            cueStepTests.add(i.next());
        }
    }
}
