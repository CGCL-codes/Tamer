package com.sun.script.javascript;

import javax.script.*;
import sun.org.mozilla.javascript.internal.*;

/**
 * Represents compiled JavaScript code.
 *
 * @author Mike Grogan
 * @since 1.6
 */
final class RhinoCompiledScript extends CompiledScript {

    private RhinoScriptEngine engine;

    private Script script;

    RhinoCompiledScript(RhinoScriptEngine engine, Script script) {
        this.engine = engine;
        this.script = script;
    }

    public Object eval(ScriptContext context) throws ScriptException {
        Object result = null;
        Context cx = RhinoScriptEngine.enterContext();
        try {
            Scriptable scope = engine.getRuntimeScope(context);
            Object ret = script.exec(cx, scope);
            result = engine.unwrapReturnValue(ret);
        } catch (RhinoException re) {
            int line = (line = re.lineNumber()) == 0 ? -1 : line;
            String msg;
            if (re instanceof JavaScriptException) {
                msg = String.valueOf(((JavaScriptException) re).getValue());
            } else {
                msg = re.toString();
            }
            ScriptException se = new ScriptException(msg, re.sourceName(), line);
            se.initCause(re);
            throw se;
        } finally {
            Context.exit();
        }
        return result;
    }

    public ScriptEngine getEngine() {
        return engine;
    }
}
