package jsr223;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalScript {

    public static void main(String[] args) throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("Clojure");
        engine.eval("(def foo \"World!\") (println \"Hello, \" foo)");
        System.out.printf("foo = %s\n", engine.get("user/foo"));
        engine.put("user/foo", "planet");
        engine.eval("(println \"Hello, \" foo)");
        engine.eval("(ns myns) (def foo \"xxx\")");
        engine.put("myns/foo", "planet");
        engine.eval("(println \"Hello, \" myns/foo)");
        System.out.println(engine.getBindings(ScriptContext.ENGINE_SCOPE).keySet());
        engine.eval("(def bar 747)");
        int bar = (Integer) engine.get("user/bar");
        System.out.printf("bar from Clojure: %d\n", bar);
    }
}
