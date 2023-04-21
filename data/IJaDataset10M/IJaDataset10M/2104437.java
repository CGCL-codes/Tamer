package jmathlib.core.interpreter;

import jmathlib.core.tokens.OperandToken;

/**Stores a list of contexts. A context is a set of variables pluss some code.
   When a function is called a new context is added to the list. When the function is removed, the context is destroyed. */
public class ContextList {

    private Context localContext;

    private Context globalContext;

    /**Create an empty context to act as the global context
     * This constructor must only be called ONCE, because it also creates the
     * GLOBAL context
     */
    public ContextList() {
        globalContext = new Context();
        localContext = new Context();
    }

    /**
     * 
     * @param context
     */
    public void pushContext(Context context) {
        context.setParent(localContext);
        localContext = context;
    }

    /** Used to create a new context and put it on the stack when executing a function
     * @param _variables = the variable list of the function, null if it's a script file
     */
    public Context createContext(VariableList _variables) {
        if (_variables == null) {
            _variables = localContext.getVariables();
        }
        localContext = new Context(_variables, localContext);
        return localContext;
    }

    /**
     * return to the calling context of the function
     */
    public Context popContext() {
        Context context = localContext;
        localContext = localContext.getParent();
        return context;
    }

    /**
     * @return the variable list of the current context
     */
    public VariableList getLocalVariables() {
        return localContext.getVariables();
    }

    /**
     * @return the variable list of the global context
     */
    public VariableList getGlobalVariables() {
        return globalContext.getVariables();
    }

    /**
     * this method returns a variable from the workspace. It also checks
     * if the variable is local or global and returns the value from
     * the correct local or global workspace.
     * @param name
     * @return
     */
    public Variable getVariable(String name) {
        Variable var = localContext.getVariables().getVariable(name);
        if ((var != null) && (var.isGlobal())) {
            return globalContext.getVariables().getVariable(name);
        } else {
            return var;
        }
    }

    /**
     * create a variable in the local or global workspace
     * @param
     * @return
     */
    public Variable createVariable(String name) {
        Variable var = getVariable(name);
        if (getVariable(name) != null) return var;
        return getLocalVariables().createVariable(name);
    }

    /**
     * Set a variable in the local or global workspace. In case
     * the variable is not yet created, create it, too.
     * @param
     * @param
     */
    public void setVariable(String name, OperandToken value) {
        Variable var = getVariable(name);
        if (var == null) createVariable(name);
        getVariable(name).assign(value);
    }

    /**
     * @return true if the current context is the global one
     */
    public boolean isGlobalContext() {
        return (localContext.getParent() == null);
    }

    /**
     * 
     * @return
     */
    public Context getLocalContext() {
        return localContext;
    }
}
