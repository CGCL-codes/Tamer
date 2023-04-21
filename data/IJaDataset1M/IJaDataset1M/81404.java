package org.elascript.interpreter;

import java.util.*;
import org.elascript.*;
import org.elascript.symbol.*;

public class MemorySpace {

    private Map<String, Object> members = new HashMap<String, Object>();

    private Scope scope;

    private MemorySpace parent;

    private Interpreter interpreter;

    public MemorySpace(Scope scope, MemorySpace parent, Interpreter interpreter) {
        CodeContract.requiresArgumentNotNull(scope, "scope");
        CodeContract.requiresArgumentNotNull(interpreter, "interpreter");
        this.scope = scope;
        this.parent = parent;
        this.interpreter = interpreter;
        for (Symbol sym : scope.getSymbols()) if (sym instanceof VariableSymbol) members.put(sym.getName(), null);
    }

    public MemorySpace(Scope scope, Interpreter interpreter) {
        this(scope, null, interpreter);
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public Object get(VariableSymbol sym) {
        CodeContract.requiresArgumentNotNull(sym, "sym");
        if (sym.getScope() == scope) return members.get(sym.getName());
        return parent.get(sym);
    }

    public void put(VariableSymbol sym, Object value) {
        CodeContract.requiresArgumentNotNull(sym, "sym");
        if (sym.getScope() == scope) members.put(sym.getName(), value); else parent.put(sym, value);
    }

    void put(String name, Object value) {
        members.put(name, value);
    }

    public MemorySpace getParent() {
        return parent;
    }

    public Scope getScope() {
        return scope;
    }
}
