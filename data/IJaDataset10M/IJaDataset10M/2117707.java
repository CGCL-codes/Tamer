package cn.myapps.core.macro.runner;

import org.mozilla.javascript.Scriptable;

/**
 * Interface to provide a scope object for script evalutaion to the debugger.
 */
public interface ScopeProvider {

    /**
     * Returns the scope object to be used for script evaluation.
     */
    Scriptable getScope();
}

;
