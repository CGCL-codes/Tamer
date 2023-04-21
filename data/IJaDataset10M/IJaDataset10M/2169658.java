package bsh.org.objectweb.asm.tree.analysis;

import java.util.Set;
import bsh.org.objectweb.asm.tree.AbstractInsnNode;

/**
 * A {@link Value} that is represented by its type in a two types type system.
 * This type system distinguishes the ONEWORD and TWOWORDS types.
 * 
 * @author Eric Bruneton
 */
public class SourceValue implements Value {

    /**
     * The size of this value.
     */
    public final int size;

    /**
     * The instructions that can produce this value. For example, for the Java
     * code below, the instructions that can produce the value of <tt>i</tt>
     * at line 5 are the txo ISTORE instructions at line 1 and 3:
     * 
     * <pre>
     * 1: i = 0;
     * 2: if (...) {
     * 3:   i = 1;
     * 4: }
     * 5: return i;
     * </pre>
     * 
     * This field is a set of {@link AbstractInsnNode} objects.
     */
    public final Set insns;

    public SourceValue(final int size) {
        this(size, SmallSet.EMPTY_SET);
    }

    public SourceValue(final int size, final AbstractInsnNode insn) {
        this.size = size;
        this.insns = new SmallSet(insn, null);
    }

    public SourceValue(final int size, final Set insns) {
        this.size = size;
        this.insns = insns;
    }

    public int getSize() {
        return size;
    }

    public boolean equals(final Object value) {
        if (!(value instanceof SourceValue)) {
            return false;
        }
        SourceValue v = (SourceValue) value;
        return size == v.size && insns.equals(v.insns);
    }

    public int hashCode() {
        return insns.hashCode();
    }
}
