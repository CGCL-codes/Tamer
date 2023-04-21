package fitlibrary.specify.constraint;

import fitlibrary.traverse.DomainAdapter;

public class SetUpAndTearDownCalled implements DomainAdapter {

    private boolean isSetUp = false;

    public void setUp() {
        isSetUp = true;
    }

    public void tearDown() {
        throw new RuntimeException("tear down");
    }

    public boolean aB(int a, int b) {
        return isSetUp && a < b;
    }

    @Override
    public Object getSystemUnderTest() {
        return null;
    }
}
