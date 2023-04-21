package turing2011;

import java.io.InputStreamReader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MaquinaTurin {

    public static String eval(String mt, String prog) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        try {
            jsEngine.eval(new InputStreamReader(MaquinaTurin.class.getResourceAsStream("turing.js")));
            Invocable invocableEngine = (Invocable) jsEngine;
            String s = (String) invocableEngine.invokeFunction("turingMachine", mt, prog);
            return s;
        } catch (ScriptException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "<false,problem loading script engine>";
    }
}
