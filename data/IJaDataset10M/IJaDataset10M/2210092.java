package org.ucl.xpath.function;

import org.ucl.xpath.*;
import org.ucl.xpath.types.*;
import java.util.*;

/**
 * Returns an xs:integer representing the hours component in the canonical lexical
 * representation of the value of $arg. The result may be negative.
 * If $arg is the empty sequence, returns the empty sequence.
 */
public class FnHoursFromDuration extends Function {

    private static Collection _expected_args = null;

    /**
	 * Constructor for FnHoursFromDuration.
	 */
    public FnHoursFromDuration() {
        super(new QName("hours-from-duration"), 1);
    }

    /**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
    public ResultSequence evaluate(Collection args) throws DynamicError {
        return hours_from_duration(args);
    }

    /**
         * Hours-from-Duration operation.
         * @param args Result from the expressions evaluation.
         * @throws DynamicError Dynamic error.
         * @return Result of fn:hours-from-duration operation.
         */
    public static ResultSequence hours_from_duration(Collection args) throws DynamicError {
        Collection cargs = Function.convert_arguments(args, expected_args());
        ResultSequence arg1 = (ResultSequence) cargs.iterator().next();
        ResultSequence rs = ResultSequenceFactory.create_new();
        if (arg1.empty()) {
            return rs;
        }
        XDTDayTimeDuration dtd = (XDTDayTimeDuration) arg1.first();
        int res = dtd.hours();
        if (dtd.negative()) res *= -1;
        rs.add(new XSInteger(res));
        return rs;
    }

    /**
         * Obtain a list of expected arguments.
         * @return Result of operation.
         */
    public static Collection expected_args() {
        if (_expected_args == null) {
            _expected_args = new ArrayList();
            _expected_args.add(new SeqType(new XDTDayTimeDuration(), SeqType.OCC_QMARK));
        }
        return _expected_args;
    }
}
