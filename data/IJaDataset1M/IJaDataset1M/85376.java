package net.disy.legato.javascript.test;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

public class RhinoTest {

    @Test
    public void test() throws Exception {
        final Context context = Context.enter();
        final Scriptable scope = context.initStandardObjects();
        final String script = IOUtils.toString(getClass().getResourceAsStream("Factorial.js"));
        final Script compiledScript = context.compileString(script, "Factorial.js", 0, null);
        compiledScript.exec(context, scope);
        final Function function = (Function) scope.get("factorial", scope);
        @SuppressWarnings("unused") final Object result = function.call(context, scope, scope, new Object[] { 10 });
    }
}
