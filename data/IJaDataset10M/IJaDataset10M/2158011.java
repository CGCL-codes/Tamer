package org.xteam.sled.semantic.exp;

import java.util.HashMap;
import java.util.Map;

public class VariableSubstitute extends ExpRewriter {

    private Map<String, Exp> vars;

    public VariableSubstitute(String name, Exp exp) {
        this.vars = new HashMap<String, Exp>();
        vars.put(name, exp);
    }

    public VariableSubstitute(Map<String, Exp> vars) {
        this.vars = vars;
    }

    public void visitVar(ExpVar expVar) {
        if (vars.containsKey(expVar.name())) {
            stack.push(vars.get(expVar.name()));
        } else {
            stack.push(expVar);
        }
    }

    public String toString() {
        return vars.toString();
    }
}
