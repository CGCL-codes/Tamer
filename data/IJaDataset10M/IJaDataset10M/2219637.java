package org.jpc.emulator.memory.codeblock.fastcompiler;

import java.lang.reflect.*;

/**
 * Immutable wrapper for objects which can be referenced in the constant pool.
 * @author Chris Dennis
 */
class ConstantPoolSymbol {

    private final Object poolEntity;

    /**
     * Constructs a new <code>ConstantPoolSymbol</code> wrapping
     * </code>o</code>.  This will throw <code>IllegalArgumentException</code>
     * if the supplied object cannot be referenced in a constant pool.  Valid
     * classes are:
     * <ul>
     * <li><code>Class</code></li>
     * <li><code>Method</code></li>
     * <li><code>Field</code></li>
     * <li><code>String</code></li>
     * <li><code>Integer</code></li>
     * <li><code>Long</code></li>
     * <li><code>Float</code></li>
     * <li><code>Double</code></li>
     * </ul>
     * @param o object being wrapped.  
     * @throws IllegalArgumentException if o is not a valid constant pool object
     */
    ConstantPoolSymbol(Object o) {
        Class cls = o.getClass();
        boolean ok = Class.class.equals(cls) || Method.class.equals(cls) || Field.class.equals(cls) || String.class.equals(cls) || Integer.class.equals(cls) || Long.class.equals(cls) || Float.class.equals(cls) || Double.class.equals(cls);
        if (!ok) throw new IllegalArgumentException();
        poolEntity = o;
    }

    /**
     * Returns the object being wrapped by this symbol.
     * @return object being wrapped
     */
    Object poolEntity() {
        return poolEntity;
    }

    public String toString() {
        return "ConstantPoolSymbol[" + poolEntity + "]";
    }
}
