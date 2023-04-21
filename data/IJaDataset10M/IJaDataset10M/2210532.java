package nl.adaptivity.parser.eval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A default implementation for a symbol context.
 * 
 * @author Paul de Vrieze
 * @version 0.1 $Revision: 477 $
 */
public abstract class AbstractSymbolContext implements SymbolContext {

    private final SymbolContext aParentContext;

    /**
   * Create a new abstract symbol context.
   * 
   * @param pParentContext The parent context.
   */
    protected AbstractSymbolContext(final SymbolContext pParentContext) {
        aParentContext = pParentContext;
    }

    /** {@inheritDoc} */
    public SymbolContext getParentContext() {
        return aParentContext;
    }

    /** {@inheritDoc} */
    public boolean isVariableSymbol(final String pName) {
        return aParentContext != null && aParentContext.isVariableSymbol(pName);
    }

    /** {@inheritDoc} */
    public boolean isFunctionSymbol(final String pName) {
        return aParentContext != null && aParentContext.isFunctionSymbol(pName);
    }

    /** {@inheritDoc} */
    public boolean isFunctionSymbol(final String pName, final Type[] pParamTypes) {
        return aParentContext != null && aParentContext.isFunctionSymbol(pName, pParamTypes);
    }

    /** {@inheritDoc} */
    public boolean isConstantSymbol(final String pName) {
        return aParentContext != null && aParentContext.isConstantSymbol(pName);
    }

    /** {@inheritDoc} */
    public boolean isTypeSymbol(final String pName) {
        return aParentContext != null && aParentContext.isTypeSymbol(pName);
    }

    /** {@inheritDoc} */
    public boolean isSymbol(final String pName) {
        return isVariableSymbol(pName) || isFunctionSymbol(pName) || isConstantSymbol(pName) || isTypeSymbol(pName);
    }

    /** {@inheritDoc} */
    public Object getVariableSymbol(final String pName) throws HandlerException {
        if (aParentContext != null && aParentContext.isVariableSymbol(pName)) {
            return aParentContext.getVariableSymbol(pName);
        }
        throw new HandlerException("Unknown variable symbol \"" + pName + "\"");
    }

    /** {@inheritDoc} */
    public void setVariableSymbol(final String pName, final Object pValue) throws HandlerException {
        if (aParentContext != null && aParentContext.isVariableSymbol(pName)) {
            aParentContext.setVariableSymbol(pName, pValue);
            return;
        }
        throw new HandlerException("Unknown variable symbol \"" + pName + "\"");
    }

    /** {@inheritDoc} */
    public FunctionReference getFunctionSymbol(final String pName, final Type[] pParamTypes) throws HandlerException {
        if (aParentContext != null && aParentContext.isFunctionSymbol(pName)) {
            return aParentContext.getFunctionSymbol(pName, pParamTypes);
        }
        final List<FunctionReference> candidates = new ArrayList<FunctionReference>(getFunctionSymbols(pName));
        for (final Iterator<FunctionReference> it = candidates.iterator(); it.hasNext(); ) {
            final FunctionReference candidate = it.next();
            if (!parametersMatch(candidate, pParamTypes)) {
                it.remove();
            }
        }
        Collections.sort(candidates, getFunctionComparator(pParamTypes));
        if (candidates.size() > 0) {
            return candidates.get(0);
        }
        throw new HandlerException("No good candidate function \"" + pName + "\"");
    }

    /**
   * A comparing function that matches the best fitting function for the given
   * parameters.
   * 
   * @param pParamTypes The parameters to the function
   * @return a comparator.
   */
    public Comparator<FunctionReference> getFunctionComparator(final Type... pParamTypes) {
        return new Comparator<FunctionReference>() {

            public int compare(final FunctionReference pO1, final FunctionReference pO2) {
                return 0;
            }
        };
    }

    /**
   * If variable parameters must be used, this function should be overridden.
   * Instead it is also possible to override {@link #getFunctionSymbol}.
   * 
   * @param pFunc The function to check
   * @param pParamTypes Actual parameter types given
   * @return Whether the function matches the parameters
   */
    public boolean parametersMatch(final FunctionReference pFunc, final Type... pParamTypes) {
        final List<Type> paramTypes = pFunc.getParamTypes();
        if (paramTypes.size() != pParamTypes.length) {
            return false;
        }
        for (int i = 0; i < paramTypes.size(); i++) {
            if (!paramTypes.get(i).isAssignableFrom(pParamTypes[i])) {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    public Collection<FunctionReference> getFunctionSymbols(final String pName) throws HandlerException {
        if (aParentContext != null && aParentContext.isFunctionSymbol(pName)) {
            return aParentContext.getFunctionSymbols(pName);
        }
        throw new HandlerException("Unknown function symbol \"" + pName + "\"");
    }

    /** {@inheritDoc} */
    public Object getConstantSymbol(final String pName) throws HandlerException {
        if (aParentContext != null && aParentContext.isConstantSymbol(pName)) {
            return aParentContext.getConstantSymbol(pName);
        }
        throw new HandlerException("Unknown constant symbol \"" + pName + "\"");
    }

    /** {@inheritDoc} */
    public Type getTypeSymbol(final String pName) throws HandlerException {
        if (aParentContext != null && aParentContext.isTypeSymbol(pName)) {
            return aParentContext.getTypeSymbol(pName);
        }
        throw new HandlerException("Unknown type symbol \"" + pName + "\"");
    }

    /** {@inheritDoc} */
    public void defVariableSymbol(final Type pType, final String pName, final Object pValue) throws HandlerException {
        throw new HandlerException("Defining new variables not supported in this context");
    }

    /** {@inheritDoc} */
    public void defFunctionSymbol(final String pName, final FunctionReference pFunction) throws HandlerException {
        throw new HandlerException("Defining new functions not supported in this context");
    }
}
