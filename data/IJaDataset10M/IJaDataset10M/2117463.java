package org.norecess.nolatte.environments;

import java.util.HashMap;
import java.util.Map;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IIdentifier;

/**
 * Implements a variable environment for a no-latte interpreter. Variables must
 * be defined in the no-latter source before they can be used or re-set; this is
 * handled by {@link #add(String,String)}. Variables are retreived with
 * {@link #get(String)}. Variable are reset with a new value through
 * {@link #set(String,String)}.
 * 
 * <p>
 * The {@link InvalidVariableException}class is used as a general "you did
 * something wrong with a variable" problem. It applies in three situations: (1)
 * a variable is redeclared in the same scope; (2) an undeclared variable is
 * accessed; and (3) an undeclared variable's value is re-set.
 */
public class Environment implements IEnvironment {

    private static final long serialVersionUID = 5056512435184797836L;

    private final Map<IIdentifier, Datum> myCurrentScope;

    private final IEnvironment myPreviousEnvironment;

    protected Environment() {
        this(new NullEnvironment());
    }

    public Environment(IEnvironment previous) {
        if (previous == this) {
            throw new IllegalArgumentException("Creating circular environment");
        }
        myCurrentScope = new HashMap<IIdentifier, Datum>();
        myPreviousEnvironment = previous;
    }

    public Environment(Map<IIdentifier, Datum> currentScope, IEnvironment previous) {
        myCurrentScope = currentScope;
        myPreviousEnvironment = previous;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null) && getClass().equals(o.getClass()) && equals((Environment) o);
    }

    private boolean equals(Environment other) {
        return myCurrentScope.equals(other.myCurrentScope) && myPreviousEnvironment.equals(other.myPreviousEnvironment);
    }

    @Override
    public int hashCode() {
        return myCurrentScope.hashCode();
    }

    public void define(IIdentifier variable, Datum value) {
        myPreviousEnvironment.define(variable, value);
    }

    public void add(IIdentifier variable, Datum value) throws InvalidVariableException {
        if (myCurrentScope.get(variable) != null) {
            throw new InvalidVariableException("Redefining variable " + variable, variable);
        }
        myCurrentScope.put(variable, value);
    }

    public Datum get(IIdentifier variable) throws InvalidVariableException {
        if (myCurrentScope.get(variable) != null) {
            return myCurrentScope.get(variable);
        } else {
            return myPreviousEnvironment.get(variable);
        }
    }

    public Datum set(IIdentifier variable, Datum value) throws InvalidVariableException {
        if (myCurrentScope.get(variable) != null) {
            return myCurrentScope.put(variable, value);
        } else {
            return myPreviousEnvironment.set(variable, value);
        }
    }

    @Override
    public String toString() {
        return myCurrentScope + " " + myPreviousEnvironment;
    }
}
