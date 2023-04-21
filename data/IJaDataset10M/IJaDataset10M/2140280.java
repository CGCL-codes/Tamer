package gci.tree;

import gci.temp.*;

public class TEMP extends Expression {

    public Temp temp;

    public TEMP(Temp t) {
        temp = t;
    }

    public ExpressionList kids() {
        return null;
    }

    public Expression build(ExpressionList kids) {
        return new TEMP(temp);
    }

    public String print() {
        return "TEMP " + temp.toString();
    }
}
