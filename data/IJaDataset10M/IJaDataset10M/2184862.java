package org.opcda2out.composite;

import javax.script.Bindings;
import javax.script.CompiledScript;
import org.opcda2out.scripting.handlers.ScriptingEngineHandler;

/**
 * Defines a composite item
 * 
 * @author Joao Leal
 */
public interface Composite {

    /**
     * Provides the composite item name
     *
     * @return The composite item name
     */
    public String getName();

    /**
     * Provides the script text used by this composite item
     * 
     * @return The script text
     */
    public String getScript();

    /**
     * Provides the compiled script for this composite item, if it is available
     *
     * @return The compiled script (possibly <code>null</code>)
     */
    public CompiledScript getCompiledScript();

    /**
     * Provides the bindings to used by for the script evaluation
     * 
     * @return The script bindings
     */
    public Bindings getBindings();

    /**
     * Provides the scripting engine handler
     * 
     * @return The scripting engine handler
     */
    public ScriptingEngineHandler getScriptingEngineHandler();
}
