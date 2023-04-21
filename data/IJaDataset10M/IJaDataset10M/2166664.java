package net.sf.jstester;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import net.sf.jstester.util.Assert;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides assertions on javascript code.<br>
 * Uses the JavaScript ScriptEngine provided by JDK6.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class JDK6JsTester {

    private static String asserts;

    private static final String EOL = System.getProperty("line.separator");

    private static String lintToolkit;

    private static final Log log = LogFactory.getLog(JsTester.class);

    static {
        lintToolkit = loadScript("net/sf/jstester/lintToolkit.js");
        asserts = loadScript("net/sf/jstester/asserts.js");
    }

    /**
    * Loads a script available in the classpath.
    */
    public static String loadScript(String scriptName) {
        return loadScript(scriptName, "UTF-8");
    }

    /**
    * Loads a script available in the classpath.
    */
    public static String loadScript(String scriptName, String encoding) {
        if (encoding == null || encoding.trim().length() == 0) encoding = "UTF-8";
        StringBuilder script = new StringBuilder();
        BufferedReader in = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            in = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(scriptName), encoding));
            String line = null;
            while ((line = in.readLine()) != null) {
                script.append(line + EOL);
            }
        } catch (Exception e) {
            log.fatal("could not load script '" + scriptName + "'");
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        return script.toString();
    }

    private ScriptContext context;

    private Bindings globalScope;

    private ScriptEngine scriptEngine;

    /**
    * Asserts if two arrays are equal.
    */
    public void assertArrayEquals(String expr1, String expr2) {
        assertArrayEquals(null, expr1, expr2);
    }

    /**
    * Asserts if two arrays are equal.
    */
    public void assertArrayEquals(String msg, String expr1, String expr2) {
        assertBinaryPredicate(msg, "assertArrayEquals", expr1, expr2);
    }

    /**
    * Asserts that a binary predicate is true.<br>
    * The predicate can not be null.
    */
    public void assertBinaryPredicate(String predicate, String expr1, String expr2) {
        assertBinaryPredicate(null, predicate, expr1, expr2);
    }

    /**
    * Asserts that a binary predicate is true.<br>
    * The predicate can not be null.
    */
    public void assertBinaryPredicate(String msg, String predicate, String expr1, String expr2) {
        Assert.assertNotNull("'predicate' is null", predicate);
        Boolean bool = (Boolean) eval(predicate + "(" + expr1 + "," + expr2 + ")");
        if (bool == null) {
            Assert.fail(predicate);
        }
        if (msg != null) {
            Assert.assertTrue(msg, bool.booleanValue());
        } else {
            Assert.assertTrue(bool.booleanValue());
        }
    }

    /**
    * Asserts that two expressions are equal.<br>
    * If the expressions are arrays, calls assertArrayEquals.<br>
    * If the expressions are objects, calls assertObjectEquals.<br>
    */
    public void assertEquals(String expr1, String expr2) {
        assertEquals(null, expr1, expr2);
    }

    /**
    * Asserts that two expressions are equal.<br>
    * If the expressions are arrays, calls assertArrayEquals.<br>
    * If the expressions are objects, calls assertObjectEquals.<br>
    */
    public void assertEquals(String msg, String expr1, String expr2) {
        assertBinaryPredicate(msg, "assertEquals", expr1, expr2);
    }

    public void assertIsAlien(String expr) {
        assertIsAlien(null, expr);
    }

    public void assertIsAlien(String msg, String expr) {
        assertUnaryPredicate(msg, "isAlien", expr);
    }

    /**
    * Asserts that the expression is an array.
    */
    public void assertIsArray(String expr) {
        assertIsArray(null, expr);
    }

    /**
    * Asserts that the expression is an array.
    */
    public void assertIsArray(String msg, String expr) {
        assertUnaryPredicate(msg, "isArray", expr);
    }

    /**
    * Asserts that the expression is a boolean.
    */
    public void assertIsBoolean(String expr) {
        assertIsBoolean(null, expr);
    }

    /**
    * Asserts that the expression is a boolean.
    */
    public void assertIsBoolean(String msg, String expr) {
        assertUnaryPredicate(msg, "isBoolean", expr);
    }

    /**
    * Asserts that the expression an empty object.
    */
    public void assertIsEmpty(String expr) {
        assertIsEmpty(null, expr);
    }

    /**
    * Asserts that the expression an empty object.
    */
    public void assertIsEmpty(String msg, String expr) {
        assertUnaryPredicate(msg, "isEmpty", expr);
    }

    /**
    * Asserts that the expression is a function.
    */
    public void assertIsFunction(String expr) {
        assertIsFunction(null, expr);
    }

    /**
    * Asserts that the expression is a function.
    */
    public void assertIsFunction(String msg, String expr) {
        assertUnaryPredicate(msg, "isFunction", expr);
    }

    /**
    * Asserts that the expression is a number.
    */
    public void assertIsNumber(String expr) {
        assertIsNumber(null, expr);
    }

    /**
    * Asserts that the expression is a number.
    */
    public void assertIsNumber(String msg, String expr) {
        assertUnaryPredicate(msg, "isNumber", expr);
    }

    /**
    * Asserts that the expression is an object.
    */
    public void assertIsObject(String expr) {
        assertIsObject(null, expr);
    }

    /**
    * Asserts that the expression is an object.
    */
    public void assertIsObject(String msg, String expr) {
        assertUnaryPredicate(msg, "isObject", expr);
    }

    /**
    * Asserts that the expression is a string.
    */
    public void assertIsString(String expr) {
        assertIsString(null, expr);
    }

    /**
    * Asserts that the expression is a string.
    */
    public void assertIsString(String msg, String expr) {
        assertUnaryPredicate(msg, "isString", expr);
    }

    /**
    * Asserts that the expression is undefined.
    */
    public void assertIsUndefined(String expr) {
        assertIsUndefined(null, expr);
    }

    /**
    * Asserts that the expression is undefined.
    */
    public void assertIsUndefined(String msg, String expr) {
        assertUnaryPredicate(msg, "isUndefined", expr);
    }

    /**
    * Asserts that the expression is not null.
    */
    public void assertNotNull(String expr) {
        assertNotNull(null, expr);
    }

    /**
    * Asserts that the expression is not null.
    */
    public void assertNotNull(String msg, String expr) {
        assertUnaryPredicate(msg, "assertNotNull", expr);
    }

    /**
    * Asserts that the expression is null.
    */
    public void assertNull(String expr) {
        assertNull(null, expr);
    }

    /**
    * Asserts that the expression is null.
    */
    public void assertNull(String msg, String expr) {
        assertUnaryPredicate(msg, "assertNull", expr);
    }

    /**
    * Asserts that the two expressions are objects and equal.<br>
    * If any property is an array, calls assertArrayEquals.<br>
    * If any property is an object, calls assertObjectEquals.<br>
    */
    public void assertObjectEquals(String expr1, String expr2) {
        assertObjectEquals(null, expr1, expr2);
    }

    /**
    * Asserts that the two expressions are objects and equal.<br>
    * If any property is an array, calls assertArrayEquals.<br>
    * If any property is an object, calls assertObjectEquals.<br>
    */
    public void assertObjectEquals(String msg, String expr1, String expr2) {
        assertBinaryPredicate(msg, "assertObjectEquals", expr1, expr2);
    }

    /**
    * Asserts that the expression is true.
    */
    public void assertTrue(String expr) {
        assertTrue(null, expr);
    }

    /**
    * Asserts that the expression is true.
    */
    public void assertTrue(String msg, String expr) {
        assertUnaryPredicate(msg, "assertTrue", expr);
    }

    /**
    * Asserts that a unary predicate is true.<br>
    * The predicate can not be null.
    */
    public void assertUnaryPredicate(String predicate, String expr) {
        assertUnaryPredicate(null, predicate, expr);
    }

    /**
    * Asserts that a unary predicate is true.<br>
    * The predicate can not be null.
    */
    public void assertUnaryPredicate(String msg, String predicate, String expr) {
        Assert.assertNotNull("'predicate' is null", predicate);
        Boolean bool = (Boolean) eval(predicate + "(" + expr + ")");
        if (bool == null) {
            Assert.fail(predicate);
        }
        if (msg != null) {
            Assert.assertTrue(msg, bool.booleanValue());
        } else {
            Assert.assertTrue(bool.booleanValue());
        }
    }

    /**
    * Executes an arbitrary expression.<br>
    * It fails if the expression throws a JsAssertException.<br>
    * It fails if the expression throws a RhinoException.<br>
    */
    public Object eval(String expr) {
        assertTesterIsInitialized();
        Object value = null;
        try {
            value = scriptEngine.eval(expr);
        } catch (ScriptException se) {
            Object jsAssertException = scriptEngine.get("currentException");
            if (jsAssertException != null) {
                try {
                    String message = (String) MethodUtils.invokeMethod(jsAssertException, "get", new Object[] { "message", jsAssertException });
                    if (message != null) {
                        Assert.fail(message);
                    }
                } catch (Exception e) {
                    Assert.fail(se.getMessage());
                }
            } else {
                Assert.fail(se.getMessage());
            }
        }
        return value;
    }

    /**
    * Get the current context.
    */
    public ScriptContext getContext() {
        return context;
    }

    /**
    * Get the global scope.
    */
    public Bindings getGlobalScope() {
        return globalScope;
    }

    /**
    * Initializes the context, global scope, and assertion scripts.<br>
    * Must be called before any call to eval() is issued.
    */
    public void onSetUp() {
        ScriptEngineManager m = new ScriptEngineManager();
        scriptEngine = m.getEngineByName("javascript");
        context = scriptEngine.getContext();
        globalScope = scriptEngine.getBindings(ScriptContext.GLOBAL_SCOPE);
        eval(lintToolkit);
        eval(asserts);
    }

    /**
    * Destroys the current context.
    */
    public void onTearDown() {
    }

    private void assertTesterIsInitialized() {
        Assert.assertNotNull("context is null. Don't forget to call onSetUp()", context);
        Assert.assertNotNull("globalScope is null. Don't forget to call onSetUp()", globalScope);
    }
}
