package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * RunScript4: Execute scripts in an environment that includes the example
 * Counter class.
 * 
 * @author Norris Boyd
 */
public class RunScript {

    private static Script js;

    public static String getTestJS(String path) throws IOException {
        InputStream resourceAsStream = null;
        resourceAsStream = RunScript.class.getClassLoader().getResourceAsStream(path);
        InputStreamReader osw = new InputStreamReader(resourceAsStream);
        BufferedReader r = new BufferedReader(osw);
        StringBuilder b = new StringBuilder();
        String s = r.readLine();
        while (s != null) {
            s = r.readLine();
            if (s == null) break;
            b.append(s);
            b.append("\n");
        }
        return b.toString();
    }

    public static final Script getScript() throws IOException {
        if (js == null) {
            Context cx = Context.enter();
            try {
                Scriptable scope = cx.initStandardObjects();
                cx.evaluateString(scope, getTestJS("test/a.js"), "aurora", 1, null);
                Object[] propertyIds = ScriptableObject.getPropertyIds(scope);
                Object[] ids = scope.getIds();
                for (Object i : ids) {
                    System.out.println(i.toString());
                }
                Object object = scope.get("openAssignPage", scope);
                if (object instanceof NativeFunction) {
                    ((NativeFunction) object).getIds();
                    ((NativeFunction) object).getAllIds();
                    ((NativeFunction) object).getClassName();
                    Scriptable prototype = ((NativeFunction) object).getPrototype();
                    prototype.getIds();
                    ((NativeFunction) object).get("arguments", (Scriptable) object);
                    ((NativeFunction) object).get("prototype", (Scriptable) object);
                    ((NativeFunction) object).get("name", (Scriptable) object);
                    ((NativeFunction) object).get("arity", (Scriptable) object);
                    ((NativeFunction) object).get("length", (Scriptable) object);
                }
                if (object instanceof Script) {
                }
                NativeFunction sss = (NativeFunction) object;
                Scriptable prototype = sss.getPrototype();
                ;
                for (Object i : prototype.getIds()) {
                    System.out.println(i.toString());
                }
                String decompileScript = cx.decompileScript((Script) object, 1);
                System.out.println(decompileScript);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Context.exit();
            }
        }
        return js;
    }

    public static void mm() {
        String s = " function  a(para){para.toString();} function b(){}";
        Context cx = Context.enter();
        try {
            cx.setLanguageVersion(Context.VERSION_1_2);
            Scriptable scope = cx.initStandardObjects();
            Object result = cx.evaluateString(scope, s, "MySource", 1, null);
            System.out.println(result.getClass());
            Object[] propertyIds = ScriptableObject.getPropertyIds(scope);
            for (Object o : propertyIds) {
                System.out.println(o);
            }
            Object object = scope.get("a", scope);
            System.out.println(object.getClass().getInterfaces());
            System.out.println(object instanceof Scriptable);
            System.out.println(object instanceof Script);
        } finally {
            Context.exit();
        }
    }

    public static void main(String args[]) {
        try {
            getScript();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
