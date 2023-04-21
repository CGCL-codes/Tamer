package jcollector.suites;

public abstract class AllCollectedTestsConfiguredWithSystemProperty extends AbstractAllCollectedTests {

    protected String getDefaultPath() {
        return System.getProperty("root.path");
    }
}
