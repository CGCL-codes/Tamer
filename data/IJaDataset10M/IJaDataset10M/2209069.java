package web;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

public class JavascriptEval {

    public static Object evalJS(String script) throws Exception {
        final ScriptEngineManager factory = new ScriptEngineManager();
        final ScriptEngine engine = factory.getEngineByName("JavaScript");
        return engine.eval(script);
    }

    public static NativeObject parseJSON(String json) throws Exception {
        return (NativeObject) evalJS("eval('(" + json + ")')");
    }

    public static Object getJSONproperty(String jsonText, String property) throws Exception {
        return parseJSON(jsonText).get(property, null);
    }

    public static Object[] getJSONarray(String jsonText, String array) throws Exception {
        final NativeArray obj = (NativeArray) parseJSON(jsonText).get(array, null);
        final Object[] arr = new Object[(int) obj.getLength()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = obj.get(i, null);
        }
        return arr;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getJSONproperty("{'msg':'OK'}", "msg"));
        System.out.println(getJSONarray("{'msg':['O','K']}", "msg")[0]);
    }
}
