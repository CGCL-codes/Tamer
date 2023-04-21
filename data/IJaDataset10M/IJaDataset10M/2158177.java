package de.pannenleiter.db.expr;

import java.util.*;

public class VariableReference extends Expression {

    private String fullname;

    /**
   * Constructor
   * @param name the variable name (as a Name object)
   */
    public VariableReference(String name) throws Exception {
        this.fullname = name;
    }

    public void dump(String indent) {
        System.out.println(indent + "VariableReference " + fullname);
    }

    ReturnInfo format(SQLContext ctx, ReturnInfo up, boolean isExpr) throws Exception {
        return new ReturnInfo(fullname, ReturnInfo.VARIABLE);
    }

    public String toString() {
        return "$" + fullname;
    }
}
