package uk.ac.bath.machine.tristate;

import uk.ac.bath.base.Value;
import java.io.*;

public class Not extends UnaryOperator {

    public void eval(Value a, Value b) {
        b.set(-a.val);
    }

    public int getStateSize() {
        return 2;
    }

    public int nIn() {
        return 1;
    }

    public int nOut() {
        return 1;
    }

    public Object clone() {
        return this;
    }

    public void toString(StringWriter w) {
        w.write("Not ");
    }
}
