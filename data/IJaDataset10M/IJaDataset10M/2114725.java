package org.ucl.xpath.function;

import org.ucl.xpath.*;
import org.ucl.xpath.types.*;
import java.util.*;

/**
 * Returns an xs:boolean indicating whether or not the value of $arg1 contains
 * (at the beginning, at the end, or anywhere within) at least one sequence of
 * collation units that provides a minimal match to the collation units in the
 * value of $arg2, according to the collation that is used.
 */
public class FnContains extends Function {

    private static Collection _expected_args = null;

    /**
	 * Constructor for FnContains.
	 */
    public FnContains() {
        super(new QName("contains"), 2);
    }

    /**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
    public ResultSequence evaluate(Collection args) throws DynamicError {
        return contains(args);
    }

    /**
         * Contains operation.
         * @param args Result from the expressions evaluation.
         * @throws DynamicError Dynamic error.
         * @return Result of fn:contains operation.
         */
    public static ResultSequence contains(Collection args) throws DynamicError {
        Collection cargs = Function.convert_arguments(args, expected_args());
        ResultSequence rs = ResultSequenceFactory.create_new();
        Iterator argiter = cargs.iterator();
        ResultSequence arg1 = (ResultSequence) argiter.next();
        String str1 = "";
        String str2 = "";
        if (!arg1.empty()) str1 = ((XSString) arg1.first()).value();
        ResultSequence arg2 = (ResultSequence) argiter.next();
        if (!arg2.empty()) str2 = ((XSString) arg2.first()).value();
        int str1len = str1.length();
        int str2len = str2.length();
        if (str1len == 0) {
            rs.add(new XSBoolean(false));
            return rs;
        }
        if (str2len == 0) {
            rs.add(new XSBoolean(true));
            return rs;
        }
        if (str1.indexOf(str2) == -1) rs.add(new XSBoolean(false)); else rs.add(new XSBoolean(true));
        return rs;
    }

    /**
         * Obtain a list of expected arguments.
         * @return Result of operation.
         */
    public static Collection expected_args() {
        if (_expected_args == null) {
            _expected_args = new ArrayList();
            SeqType arg = new SeqType(new XSString(), SeqType.OCC_QMARK);
            _expected_args.add(arg);
            _expected_args.add(arg);
        }
        return _expected_args;
    }
}
