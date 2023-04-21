package org.jikesrvm.opt.ir;

import org.jikesrvm.classloader.VM_TypeReference;
import org.vmmagic.unboxed.Offset;

/**
 * Represents a constant class operand.
 *
 * @see OPT_Operand
 * @author Ian Rogers
 */
public final class OPT_ClassConstantOperand extends OPT_ObjectConstantOperand {

    /**
   * Construct a new class constant operand
   *
   * @param v the class constant
   * @param i JTOC offset of the class constant
   */
    public OPT_ClassConstantOperand(Class<?> v, Offset i) {
        super(v, i);
    }

    /**
   * Return a new operand that is semantically equivalent to <code>this</code>.
   * 
   * @return a copy of <code>this</code>
   */
    public OPT_Operand copy() {
        return new OPT_ClassConstantOperand((Class<?>) value, offset);
    }

    /**
   * Return the {@link VM_TypeReference} of the value represented by the operand.
   * 
   * @return VM_TypeReference.JavaLangClass
   */
    public VM_TypeReference getType() {
        return VM_TypeReference.JavaLangClass;
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
