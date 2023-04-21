package org.avis.subscription.ast.nodes;

import java.util.Collection;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.avis.subscription.ast.Node;
import static java.util.Collections.singleton;

/**
 * @author Matthew Phillips
 */
public class StrUnicodeDecompose extends Node {

    private static Method normalizeMethod;

    private static boolean java5;

    private static Object modeDecompose;

    private static Object modeDecomposeCompat;

    static {
        try {
            java5 = false;
            Class<?> java6Normalizer = Class.forName("java.text.Normalizer");
            Class<?> formClass = Class.forName("java.text.Normalizer$Form");
            normalizeMethod = java6Normalizer.getMethod("normalize", CharSequence.class, formClass);
            modeDecompose = formClass.getEnumConstants()[0];
            modeDecomposeCompat = formClass.getEnumConstants()[2];
        } catch (ClassNotFoundException ex) {
            java5 = true;
            try {
                Class<?> java5Normalizer = Class.forName("sun.text.Normalizer");
                Class<?> modeClass = Class.forName("sun.text.Normalizer$Mode");
                normalizeMethod = java5Normalizer.getMethod("normalize", String.class, modeClass, Integer.TYPE);
                modeDecompose = java5Normalizer.getField("DECOMP").get(null);
                modeDecomposeCompat = java5Normalizer.getField("DECOMP_COMPAT").get(null);
            } catch (Exception ex2) {
                throw new ExceptionInInitializerError(ex2);
            }
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static enum Mode {

        DECOMPOSE, DECOMPOSE_COMPAT
    }

    public final Node stringExpr;

    public final Mode mode;

    private final Object normMode;

    public StrUnicodeDecompose(Node stringExpr, Mode mode) {
        this.stringExpr = stringExpr;
        this.mode = mode;
        this.normMode = mode == Mode.DECOMPOSE ? modeDecompose : modeDecomposeCompat;
    }

    @Override
    public Class<?> evalType() {
        return String.class;
    }

    @Override
    public Object evaluate(Map<String, Object> attrs) {
        Object result = stringExpr.evaluate(attrs);
        if (!(result instanceof String)) return BOTTOM;
        try {
            if (java5) return normalizeMethod.invoke(null, result, normMode, 0); else return normalizeMethod.invoke(null, result, normMode);
        } catch (InvocationTargetException ex) {
            throw new Error(ex.getCause());
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    @Override
    public String expr() {
        return mode == Mode.DECOMPOSE ? "decompose" : "decompose-compat";
    }

    @Override
    public Node inlineConstants() {
        Object result = evaluate(EMPTY_NOTIFICATION);
        return result == null ? this : new Const(result);
    }

    @Override
    public String presentation() {
        return name();
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public Collection<Node> children() {
        return singleton(stringExpr);
    }
}
