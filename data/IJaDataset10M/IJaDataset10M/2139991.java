package com.netgents.script.scala;

import junit.framework.TestCase;
import javax.script.*;

public class JSR223ScalaTest extends TestCase {

    private ScriptEngine loadEngine() throws ScriptException {
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("Scala");
        assertNotNull("Could not load script engine", scriptEngine);
        return scriptEngine;
    }

    public void testLoadEngine() throws ScriptException {
        loadEngine();
    }

    public void testSimpleEval() throws ScriptException {
        loadEngine().eval("1");
    }

    public void testResultPassing() throws ScriptException {
        ScriptEngine engine = loadEngine();
        int result = (Integer) (engine.eval("scriptResult = 2"));
        assertEquals(result, 2);
    }

    public void testBindingWithType() throws ScriptException {
        ScriptEngine engine = loadEngine();
        Bindings bindings = engine.createBindings();
        bindings.put("a:int", 1);
        ScriptContext ctx = new SimpleScriptContext();
        ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        int result = (Integer) (engine.eval("scriptResult = a+1", ctx));
        assertEquals(result, 2);
    }

    public void testBindingWithoutType() throws ScriptException {
        ScriptEngine engine = loadEngine();
        Bindings bindings = engine.createBindings();
        bindings.put("a", 1);
        ScriptContext ctx = new SimpleScriptContext();
        ctx.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        int result = (Integer) (engine.eval("scriptResult = a+1", ctx));
        assertEquals(result, 2);
    }

    public void testVersionRetrieval() throws ScriptException {
        System.out.println(loadEngine().getFactory().getLanguageVersion());
    }
}
