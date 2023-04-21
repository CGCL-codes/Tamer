package com.google.gwt.dev.jjs.impl;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JProgram;

/**
 * Tests {@link PostOptimizationCompoundAssignmentNormalizer}.
 */
public class PostOptimizationCompoundAssignmentNormalizerTest extends OptimizerTestBase {

    public void testIntegralFloatCoercion() throws Exception {
        optimize("void", "long x=2L; float d=3; x += d;").into("long x=2L; float d=3; x = (long)((float)x + d);");
        optimize("void", "long x=2L; long d=3L; x += d;").into("long x=2L; long d=3L; x = x + d;");
        optimize("void", "int x=2; int d=3; x += d;").into("int x=2; int d=3; x += d;");
        optimize("void", "int x=2; short d=3; x += d;").into("int x=2; short d=3; x += d;");
        optimize("void", "int x=2; short d=3; d += x;").into("int x=2; short d=3; d = (short)(d + x);");
        optimize("void", "int x=2; long d=3L; x += d;").into("int x=2; long d=3L; x = (int)((long)x + d);");
        optimize("void", "int x=2; float d=3.0f; x += d;").into("int x=2; float d=3.0f; x = (int)(x + d);");
        optimize("void", "int x=2; double d=3.0; x += d;").into("int x=2; double d=3.0; x = (int)(x + d);");
        optimize("void", "float x=2; double d=3.0; x += d;").into("float x=2; double d=3.0; x += d;");
    }

    protected boolean optimizeMethod(JProgram program, JMethod method) {
        PostOptimizationCompoundAssignmentNormalizer.exec(program);
        LongCastNormalizer.exec(program);
        return true;
    }
}
