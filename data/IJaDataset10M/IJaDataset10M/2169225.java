package net.sourceforge.pmd.symboltable;

import java.util.*;

public class SymbolTable {

    private Stack scopes = new Stack();

    public SymbolTable() {
        scopes.add(new LocalScope());
    }

    public Scope peek() {
        return (Scope) scopes.peek();
    }

    public int depth() {
        return scopes.size();
    }

    public void push(Scope scope) {
        scope.setParent(peek());
        scopes.add(scope);
    }

    public void pop() {
        scopes.pop();
    }
}
