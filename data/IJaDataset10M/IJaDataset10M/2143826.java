package org.ucl.xpath.function;

import org.ucl.xpath.*;
import org.ucl.xpath.types.*;
import java.util.*;

/**
 * Returns an xs:boolean indicating whether the argument node is "nilled".
 * If the argument is not an element node, returns the empty sequence.
 */
public class FnNilled extends Function {

    private static Collection _expected_args = null;

    /**
	 * Constructor for FnNilled.
	 */
    public FnNilled() {
        super(new QName("nilled"), 1);
    }

    /**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
    public ResultSequence evaluate(Collection args) throws DynamicError {
        return nilled(args);
    }

    /**
         * Nilled operation.
         * @param args Result from the expressions evaluation.
         * @throws DynamicError Dynamic error.
         * @return Result of fn:nilled operation.
         */
    public static ResultSequence nilled(Collection args) throws DynamicError {
        Collection cargs = Function.convert_arguments(args, expected_args());
        ResultSequence arg1 = (ResultSequence) cargs.iterator().next();
        NodeType nt = (NodeType) arg1.first();
        return nt.nilled();
    }

    /**
         * Obtain a list of expected arguments.
         * @return Result of operation.
         */
    public static Collection expected_args() {
        if (_expected_args == null) {
            _expected_args = new ArrayList();
            _expected_args.add(new SeqType(SeqType.OCC_NONE));
        }
        return _expected_args;
    }
}
