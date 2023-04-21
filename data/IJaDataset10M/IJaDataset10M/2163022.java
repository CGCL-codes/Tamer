package org.homemotion.macros.impl;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.homemotion.macros.MacroEngine;

public final class JavaScriptEngine implements MacroEngine {

    private ScriptEngineManager engineManager = new ScriptEngineManager();

    private ScriptEngine engine = engineManager.getEngineByName("JavaScript");

    public Object execute(String script) throws Exception {
        return engine.eval(script);
    }

    public String getDescription() {
        return "Standard embedded Rhino JavaScript Engine.";
    }

    public String getName() {
        return "JavaScript";
    }
}
