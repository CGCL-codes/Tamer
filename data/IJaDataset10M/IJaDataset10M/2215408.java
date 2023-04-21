package tests.api.org.punit.testclasses;

import org.punit.type.*;

public class Parameter1 implements Parameter {

    public int value;

    Parameter1(int i) {
        value = i;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
