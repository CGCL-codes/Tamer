package org.jikesrvm.compilers.opt.ir.operand;

import org.jikesrvm.classloader.TypeReference;
import org.vmmagic.unboxed.Offset;

/**
 * Represents a constant string operand.
 *
 * @see Operand
 */
public final class StringConstantOperand extends ObjectConstantOperand {

    /**
   * Construct a new string constant operand
   *
   * @param v the string constant
   * @param i JTOC offset of the string constant
   */
    public StringConstantOperand(String v, Offset i) {
        super(v, i);
    }

    /**
   * Return a new operand that is semantically equivalent to <code>this</code>.
   *
   * @return a copy of <code>this</code>
   */
    public Operand copy() {
        return new StringConstantOperand((String) value, offset);
    }

    /**
   * Return the {@link TypeReference} of the value represented by the operand.
   *
   * @return TypeReference.JavaLangString
   */
    public TypeReference getType() {
        return TypeReference.JavaLangString;
    }

    /**
   * Returns the string representation of this operand.
   *
   * @return a string representation of this operand.
   */
    public String toString() {
        return "string \"" + value + "\"";
    }
}
