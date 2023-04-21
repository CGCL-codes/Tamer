package com.google.gwt.dev.js;

import com.google.gwt.dev.jjs.SourceOrigin;
import com.google.gwt.dev.js.ast.JsProgram;
import com.google.gwt.dev.js.ast.JsStatement;
import com.google.gwt.dev.js.ast.JsVisitor;
import com.google.gwt.dev.util.DefaultTextOutput;
import com.google.gwt.dev.util.TextOutput;
import junit.framework.TestCase;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A utility base type for writing tests for JS optimizers.
 */
public abstract class OptimizerTestBase extends TestCase {

    /**
   * Optimize a JS program.
   * 
   * @param js the source program
   * @param toExec a list of classes that implement
   *          <code>static void exec(JsProgram)</code>
   * @return optimized JS
   */
    protected String optimize(String js, Class<?>... toExec) throws Exception {
        JsProgram program = new JsProgram();
        List<JsStatement> expected = JsParser.parse(SourceOrigin.UNKNOWN, program.getScope(), new StringReader(js));
        program.getGlobalBlock().getStatements().addAll(expected);
        for (Class<?> clazz : toExec) {
            Method m = clazz.getMethod("exec", JsProgram.class);
            m.invoke(null, program);
        }
        TextOutput text = new DefaultTextOutput(true);
        JsVisitor generator = new JsSourceGenerationVisitor(text);
        generator.accept(program);
        return text.toString();
    }
}
