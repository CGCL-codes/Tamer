package ca.uwaterloo.crysp.otr.test;

public abstract class TestCase implements ITest {

    private TestHarness harness;

    protected abstract void runTest() throws TestException;

    protected abstract String getDesc();

    public TestCase(TestHarness h) {
        this.harness = h;
        h.notifyTestCaseCreated();
    }

    public String run() {
        try {
            this.runTest();
        } catch (Exception e) {
            return harness.notifyTestFailure(this.getDesc(), e);
        }
        return harness.notifyTestSuccess(this.getDesc());
    }
}
