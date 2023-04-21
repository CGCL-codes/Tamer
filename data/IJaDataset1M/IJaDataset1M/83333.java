package com.dayspringtech.daylisp;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Environment.java
 *
 * @author Matthew Denson - ae6up at users.sourceforge.net
 */
public class Environment {

    private final HashMap<Symbol, Object> functions = new HashMap<Symbol, Object>();

    private final HashMap<Symbol, Object> variables = new HashMap<Symbol, Object>();

    private Environment parent = null;

    private Environment root = null;

    public Environment() {
    }

    public Environment(boolean init) {
        if (init) {
            DynamicVars.init();
            DefiningOperators.init();
            SpecialOperators.init();
            NumberFunctions.init();
            Primitives.init();
        }
    }

    public Environment(Environment e) {
        if (e == null) throw new IllegalArgumentException("Parent Environment must not be null.");
        parent = e;
        if (e.root != null) root = e.root; else root = e;
    }

    public Environment getRoot() {
        if (root == null) return this; else return root;
    }

    /**
     * lookup a symbol in variable namespace
     * @param symbol The variable to find value for
     * @return Object from the environment assigned to the Symbol
     * @throws java.util.NoSuchElementException When there is no value for the Symbol
     */
    public Object lookup(Symbol symbol) throws NoSuchElementException {
        if (variables.containsKey(symbol)) return variables.get(symbol); else {
            if (parent != null) return parent.lookup(symbol); else throw new NoSuchElementException();
        }
    }

    /**
     * change a binding in the variable namespace if it exists.
     * @param symbol The symbol representing the variable.
     * @param val The value to assign to the symbol.
     * @return  the symbol that was passed in.
     * @throws java.util.NoSuchElementException When there is no value for the Symbol
     */
    public Object set(Symbol symbol, Object val) throws NoSuchElementException {
        if (variables.containsKey(symbol)) {
            variables.put(symbol, val);
            return val;
        } else {
            if (parent != null) return parent.set(symbol, val); else throw new NoSuchElementException();
        }
    }

    /**
     * create new binding in the variable namespace of the current environment.
     * @param symbol The symbol representing the variable.
     * @param val The value to assign to the symbol.
     * @return the symbol that was passed in.
     */
    public Object define(Symbol symbol, Object val) {
        variables.put(symbol, val);
        return symbol;
    }

    /**
     * lookup a symbol in function namespace
     * @param symbol The symbol representing the function.
     * @return The function for the given symbol
     * @throws NoSuchElementException When there is no function for the Symbol
     */
    public Object lookupFunc(Symbol symbol) throws NoSuchElementException {
        if (functions.containsKey(symbol)) return functions.get(symbol); else {
            if (parent != null) return parent.lookupFunc(symbol); else if (symbol.getSymbolFunction() != null) return symbol.getSymbolFunction(); else throw new NoSuchElementException();
        }
    }

    /**
     * create new binding in then function namespace of the current environment.
     * @param symbol The symbol representing the function.
     * @param val The function to be assigned to the symbol in the function namespace
     * @return The symbol that was passed in
     */
    public Object defineFunc(Symbol symbol, Object val) {
        functions.put(symbol, val);
        return symbol;
    }

    public void close() {
    }
}
