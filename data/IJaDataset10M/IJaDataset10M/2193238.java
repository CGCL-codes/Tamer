package org.modulefusion.testrunner;

public class TestRegistration {

    private final Class<? extends Object> testClazz;

    public TestRegistration(Class<? extends Object> testClazz) {
        this.testClazz = testClazz;
    }

    public Class<? extends Object> getTestClazz() {
        return testClazz;
    }
}
