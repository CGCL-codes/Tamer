package org.ucl.xpath.function;

import org.ucl.xpath.*;
import org.ucl.xpath.types.*;
import java.util.*;

/**
 * Returns xs:time(fn:current-dateTime()). This is axs:time (with timezone) that is current
 * at some time during the evaluation of a query or transformation in which fn:current-time()
 * is executed. This function is stable. The precise instant during the query or transformation
 * represented by the value of fn:current-time() is implementation dependent.
 */
public class FnCurrentTime extends Function {

    /**
	 * Constructor for FnCurrentTime.
	 */
    public FnCurrentTime() {
        super(new QName("current-time"), 0);
    }

    /**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
    public ResultSequence evaluate(Collection args) throws DynamicError {
        return current_time(args, dynamic_context());
    }

    /**
         * Current-Time operation.
         * @param args Result from the expressions evaluation.
	 * @param dc Result of dynamic context operation.
         * @throws DynamicError Dynamic error.
         * @return Result of fn:current-time operation.
         */
    public static ResultSequence current_time(Collection args, DynamicContext dc) throws DynamicError {
        assert args.size() == 0;
        AnyType res = new XSTime(new GregorianCalendar(), dc.tz());
        return ResultSequenceFactory.create_new(res);
    }
}
