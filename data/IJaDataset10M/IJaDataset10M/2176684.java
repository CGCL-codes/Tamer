package org.jikesrvm.compilers.opt.ir.operand;

import org.jikesrvm.classloader.TypeReference;
import org.vmmagic.unboxed.Offset;

/**
 * Represents a constant class operand.
 *
 * @see Operand
 */
public final class ClassConstantOperand extends ObjectConstantOperand {

    /**
   * Construct a new class constant operand
   *
   * @param v the class constant
   * @param i JTOC offset of the class constant
   */
    public ClassConstantOperand(Class<?> v, Offset i) {
        super(v, i);
    }

    /**
   * Return a new operand that is semantically equivalent to <code>this</code>.
   *
   * @return a copy of <code>this</code>
   */
    public Operand copy() {
        return new ClassConstantOperand((Class<?>) value, offset);
    }

    /**
   * Return the {@link TypeReference} of the value represented by the operand.
   *
   * @return TypeReference.JavaLangClass
   */
    public TypeReference getType() {
        return TypeReference.JavaLangClass;
    }

    /**
   * Returns the string representation of this operand.
   *
   * @return a string representation of this operand.
   */
    public String toString() {
        return "class \"" + value + "\"";
    }
}
