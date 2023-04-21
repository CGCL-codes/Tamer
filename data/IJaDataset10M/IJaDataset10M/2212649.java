package tests.api.org.punit.testclasses;

import org.punit.type.Parameter;

public class IntParameter implements Parameter {

    public int value;

    public IntParameter(int i) {
        value = i;
    }

    public String toString() {
        return "param: " + value;
    }
}
