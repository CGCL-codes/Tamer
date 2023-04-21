package org.nakedobjects.nof.util.memento;

import org.nakedobjects.nof.testsystem.TestPojo;

public class TestPojoSimple extends TestPojo {

    private String name;

    public TestPojoSimple() {
    }

    public TestPojoSimple(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
