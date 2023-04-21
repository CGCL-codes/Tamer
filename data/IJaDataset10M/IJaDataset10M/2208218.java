package x10me.opt.ir;

import x10me.opt.ir.operand.*;
import x10me.types.Type;

/**
 * The Add instruction class.
 *
 * The header comment for {@link Instruction} contains
 * an explanation of the role of these interior class in 
 * opt compiler's IR.
 */
public abstract class Add extends Binary {

    Add(Operand r1, Operand r2, Operand r3) {
        super(r1, r2, r3);
    }

    /**
   * Returns an add instruction of the given data type.
   *
   * @param type desired type to add
   * @param r1 destination of the add
   * @param r2 the first operand of the add
   * @param r3 the second operand of the add
   * @return an add instruction of the given type
   */
    public static Add create(Type type, Operand r1, Operand r2, Operand r3) {
        if (type.isLongType()) return new LongAdd(r1, r2, r3);
        if (type.isFloatType()) return new FloatAdd(r1, r2, r3);
        if (type.isDoubleType()) return new DoubleAdd(r1, r2, r3);
        if (type.isReferenceType()) return new RefAdd(r1, r2, r3);
        return new IntAdd(r1, r2, r3);
    }
}
