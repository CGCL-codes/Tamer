package jarden.ws.testing;

import java.util.Collection;

public abstract class TestSuite {

    private String name;

    /**
     * 
     * @return just the tests from this suite <br> to get the tests from contained suites, call getTestSuites()
     *
     */
    public abstract Collection<Test> getTests();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
