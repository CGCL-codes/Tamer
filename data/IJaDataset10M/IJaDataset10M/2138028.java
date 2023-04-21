package org.ucl.xpath.function;

import org.ucl.xpath.*;
import org.ucl.xpath.types.*;
import java.util.*;

/**
 * Class for the Greater than function.
 */
public class FsGt extends Function {

    /**
	 * Constructor for FsGt.
	 */
    public FsGt() {
        super(new QName("gt"), 2);
    }

    /**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
    public ResultSequence evaluate(Collection args) throws DynamicError {
        assert args.size() == arity();
        return fs_gt_value(args);
    }

    /**
	 * Operation on the values of the arguments.
	 * @param args input arguments.
	 * @throws DynamicError Dynamic error.
	 * @return Result of the operation.
	 */
    public static ResultSequence fs_gt_value(Collection args) throws DynamicError {
        return FsEq.do_cmp_value_op(args, CmpGt.class, "gt");
    }

    /**
	 * General operation on the arguments.
	 * @param args input arguments.
	 * @throws DynamicError Dynamic error.
	 * @return Result of the operation.
	 */
    public static ResultSequence fs_gt_general(Collection args) throws DynamicError {
        return FsEq.do_cmp_general_op(args, FsGt.class, "fs_gt_value");
    }
}
