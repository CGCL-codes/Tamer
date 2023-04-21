package org.learnaholic.application.model;

public class TestResultEvent {

    private final TestResultLog source;

    public TestResultEvent(TestResultLog source) {
        this.source = source;
    }

    /**
	 * @return the source
	 */
    public TestResultLog getSource() {
        return source;
    }
}
