package org.ucl.xpath.function;

import org.ucl.xpath.*;
import org.ucl.xpath.types.*;
import java.util.*;

/**
 * Class for Modular function.
 */
public class FsMod extends Function {

    /**
	 * Constructor for FsMod.
	 */
    public FsMod() {
        super(new QName("mod"), 2);
    }

    /**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
    public ResultSequence evaluate(Collection args) throws DynamicError {
        assert args.size() == arity();
        return fs_mod(args);
    }

    /**
         * General operation on the arguments.
         * @param args input arguments.
         * @throws DynamicError Dynamic error.
         * @return Result of the operation.
         */
    public static ResultSequence fs_mod(Collection args) throws DynamicError {
        return FsPlus.do_math_op(args, MathMod.class, "mod");
    }
}
