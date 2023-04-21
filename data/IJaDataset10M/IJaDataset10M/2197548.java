package tigerunit.framework;

import tigerunit.util.Log;
import java.lang.reflect.Method;

/**
 * A test case created from a method having the {@link tigerunit.annotation.Test}
 * annotation. An AnnotationTestCase relies on its enclosing {@link TestFixture}
 * to provide its setup and teardown methods.
 */
public class AnnotationTestCase extends AbstractTestCase {

    private Method test;

    private AnnotationTestFixture fixture;

    private String description;

    public AnnotationTestCase(String description, Method testMethod) {
        super(testMethod.getName());
        this.description = description;
        test = testMethod;
    }

    public String getClassName() {
        return this.fixture.getFixtureClass().getName();
    }

    public void setFixture(AnnotationTestFixture fixture) {
        this.fixture = fixture;
    }

    public AnnotationTestFixture getFixture() {
        return fixture;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(getName());
        if (fixture != null) {
            buf.append('(').append(fixture.getFixtureClass().getName()).append(')');
        }
        if (Log.isDebug()) {
            buf.append(": ");
            if (description != null) {
                buf.append(description);
            }
        }
        return buf.toString();
    }

    public void runSetUp() throws Exception {
        if (fixture != null) {
            if (TestSession.getInstance().isReload()) {
                fixture.executeFixtureSetupMethods();
            }
            fixture.executeSetupMethods();
        }
    }

    public void runTearDown() throws Exception {
        if (fixture != null) {
            fixture.executeTearDownMethods();
            if (TestSession.getInstance().isReload()) {
                fixture.executeFixtureTearDownMethods();
            }
        }
    }

    protected void runTest() throws Throwable {
        TestSession.getInstance().execute(test, fixture.getTarget());
    }
}
