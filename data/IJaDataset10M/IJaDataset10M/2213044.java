package com.google.gwt.core.ext.js;

import com.google.gwt.dev.js.JavaScriptParserImpl;
import java.net.URL;
import java.util.List;

/**
 * The interface by which all JavaScript is parsed. This includes both external
 * inlined scripts and JSNI blocks. A parser can be configured by setting the
 * System property <code>com.google.gwt.core.ext.js.JavaScriptParser</code> to
 * the name of a concrete class to instantiate. If this is not set, a default
 * implementation will be used.
 * 
 * Implementing classes should be effectively stateless.
 */
public abstract class JavaScriptParser {

    private static final String DEFAULT_PARSER = JavaScriptParserImpl.class.getName();

    public static JavaScriptParser createInstance() {
        return createInstance(Thread.currentThread().getContextClassLoader());
    }

    public static JavaScriptParser createInstance(ClassLoader cl) {
        String className = System.getProperty(JavaScriptParser.class.getName(), DEFAULT_PARSER);
        Throwable caught;
        try {
            Class<?> clazz = Class.forName(className, true, cl);
            Class<? extends JavaScriptParser> parserClass = clazz.asSubclass(JavaScriptParser.class);
            return parserClass.newInstance();
        } catch (ClassNotFoundException e) {
            caught = e;
        } catch (InstantiationException e) {
            caught = e;
        } catch (IllegalAccessException e) {
            caught = e;
        }
        throw new RuntimeException("Unable to construct a JavaScriptParser", caught);
    }

    /**
   * @param context the parse context
   * @param externalJs the list of external JS files to parse
   * @return a unified, non-permutation specific JS AST
   */
    public abstract JavaScriptAst initialParse(JavaScriptParseContext context, List<URL> externalJs);
}
