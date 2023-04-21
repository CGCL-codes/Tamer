package cpmake;

import org.mozilla.javascript.*;
import java.io.*;

class RhinoInterpreter implements ScriptInterpreter {

    private Context m_context;

    private Scriptable m_scope;

    private int getLineNumber(String message) {
        return (-1);
    }

    public RhinoInterpreter() throws CPMakeException {
        m_context = Context.enter();
        m_scope = m_context.initStandardObjects();
        String script = "function print(a) { java.lang.System.out.println(a); }";
        try {
            m_context.evaluateString(m_scope, script, "PrintFunction", 1, null);
        } catch (RhinoException re) {
            throw new ScriptException(re.lineNumber(), re.sourceName(), re.getMessage());
        }
    }

    public void set(String var, Object value) throws CPMakeException {
        Object jsVar = Context.javaToJS(value, m_scope);
        ScriptableObject.putProperty(m_scope, var, jsVar);
    }

    public void call(String method, Object param1) throws CPMakeException {
        Object[] params = { param1 };
        call(method, params);
    }

    public void call(String method, Object param1, Object param2) throws CPMakeException {
        Object[] params = { param1, param2 };
        call(method, params);
    }

    private void call(String method, Object[] params) throws CPMakeException {
        Object scriptMethod = m_scope.get(method, m_scope);
        if (!(scriptMethod instanceof Function)) throw new MissingMethodException(method);
        try {
            ((Function) scriptMethod).call(m_context, m_scope, m_scope, params);
        } catch (RhinoException re) {
            throw new ScriptException(re.lineNumber(), re.sourceName(), re.getMessage());
        }
    }

    public void source(String file) throws CPMakeException, FileNotFoundException, IOException {
        try {
            m_context.evaluateReader(m_scope, new FileReader(file), file, 1, null);
        } catch (RhinoException re) {
            throw new ScriptException(re.lineNumber(), re.sourceName(), re.getMessage());
        }
    }

    public void eval(String statements) throws CPMakeException {
    }

    public void cleanup() {
        Context.exit();
    }
}
