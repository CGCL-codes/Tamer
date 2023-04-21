package com.trikelane.turtlescript;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;
import com.hp.hpl.jena.util.FileUtils;

public class TurtleSpecSection2_3Test {

    private static Logger logger = Logger.getLogger(TurtleSpecSection2_3Test.class);

    public static Class getSandboxClassLoader() {
        return SandboxClassLoader.class;
    }

    /**
	 * rhino.js by default (and I haven't figured out a way to override it) loads all of your Java classes
	 * into the JavaScript context.  This is causing the org.antlr Java objects to conflict with the org.antlr
	 * JavaScript objects.  That said, the custom classloader isn't working yet.
	 * 
	 * TODO: make the custom classloader work (at not loading all the Java classes).
	 */
    @BeforeClass
    public static void messWithClassLoader() {
        ContextFactory.initGlobal(new ContextFactory() {

            @Override
            protected Context makeContext() {
                Context cx = super.makeContext();
                cx.setWrapFactory(new WrapFactory() {

                    @Override
                    public Object wrap(Context cx, Scriptable scope, Object obj, Class staticType) {
                        return super.wrap(cx, scope, obj, staticType);
                    }

                    @Override
                    public Scriptable wrapNewObject(Context cx, Scriptable scope, Object obj) {
                        return super.wrapNewObject(cx, scope, obj);
                    }

                    @Override
                    public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class staticType) {
                        return new SandboxNativeJavaObject(scope, javaObject, staticType);
                    }
                });
                return cx;
            }
        });
        ContextFactory factory = ContextFactory.getGlobal();
        factory.initApplicationClassLoader(new SandboxClassLoader(TurtleSpecSection2_2Test.class.getClassLoader()));
    }

    private String helper(String ttlString, String testString) {
        StringBuilder script = new StringBuilder();
        List<String> javascriptResources = Arrays.asList("/html/lib/antlr3-all-min.js", "/html/lib/antlr3-cli-min.js", "/TTLLexer.js", "/TTLParser.js", "/html/lib/treeTTL.js", "/html/lib/emitter.js");
        try {
            Context cx = ContextFactory.getGlobal().enterContext();
            Scriptable scope = cx.initStandardObjects();
            Object wrappedOut = Context.javaToJS(System.out, scope);
            ScriptableObject.putProperty(scope, "out", wrappedOut);
            Object result = null;
            for (String resource : javascriptResources) {
                try {
                    result = cx.evaluateString(scope, FileUtils.readWholeFileAsUTF8(this.getClass().getResourceAsStream(resource)).replaceAll("org", "xorg"), resource, 1, (Object) null);
                } catch (NullPointerException e) {
                    System.out.println("Failed to load " + resource);
                }
            }
            result = cx.evaluateString(scope, "parseTtlToWalker('" + ttlString.replaceAll("org", "xorg") + "','file:///localhost/RhinoTest.java');", "file:///ttlString", 1, (Object) null);
            result = cx.evaluateString(scope, testString.replaceAll("org", "xorg"), "file:///testString", 1, (Object) null);
            return Context.toString(result).trim().replaceAll("xorg", "org");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void turtleSpecSection2_3_a() {
        String ttlString = "@prefix ex : <http://example.org/ns#> .\\n" + "ex:a ex:b ex:c,\\n" + "ex:d .\\n";
        String testString = "var triples = \n" + "	findTriples(null,'http://example.org/ns#b',null);\n" + "var triple = triples[1];\n" + "if (triple) {\n" + "    var name = triple[2];\n" + "    print(name);\n" + "}\n" + "output;";
        String result = helper(ttlString, testString);
        logger.debug(result);
        Assert.assertEquals("http://example.org/ns#d", result);
    }

    @Test
    public void turtleSpecSection2_3_b() {
        String ttlString = "@base <http://example.org/ns#> .\\n" + ":a :b :c ,\\n" + ":d .\\n";
        String testString = "var triples = \n" + "	findTriples(null,'http://example.org/ns#b',null);\n" + "var triple = triples[1];\n" + "if (triple) {\n" + "    var name = triple[2];\n" + "    print(name);\n" + "}\n" + "output;";
        String result = helper(ttlString, testString);
        logger.debug(result);
        Assert.assertEquals("http://example.org/ns#d", result);
    }

    @Test
    public void turtleSpecSection2_3_c() {
        String ttlString = "@base <http://example.org/ns#> .\\n" + ":a :b :c;\\n" + ":d :e.\\n";
        String testString = "var triples = \n" + "	findTriples(null,'http://example.org/ns#d',null);\n" + "var triple = triples[0];\n" + "if (triple) {\n" + "    var name = triple[2];\n" + "    print(name);\n" + "}\n" + "output;";
        String result = helper(ttlString, testString);
        logger.debug(result);
        Assert.assertEquals("http://example.org/ns#e", result);
    }
}
