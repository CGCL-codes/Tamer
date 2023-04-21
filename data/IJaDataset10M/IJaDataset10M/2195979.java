package org.apache.xmlbeans.impl.jam.internal.elements;

import org.apache.xmlbeans.impl.jam.JClass;

/**
 * <p>Class implementation to represent the 'void' type.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class VoidClassImpl extends BuiltinClassImpl {

    private static final String SIMPLE_NAME = "void";

    public static boolean isVoid(String fd) {
        return fd.equals(SIMPLE_NAME);
    }

    public VoidClassImpl(ElementContext ctx) {
        super(ctx);
        super.reallySetSimpleName(SIMPLE_NAME);
    }

    public boolean isVoidType() {
        return true;
    }

    public boolean isAssignableFrom(JClass c) {
        return false;
    }
}
