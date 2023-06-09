package com.meidusa.amoeba.sqljep.function;

import java.sql.Timestamp;
import java.util.Calendar;
import static java.util.Calendar.*;
import com.meidusa.amoeba.sqljep.function.PostfixCommand;
import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;

public class WeekOfYear extends PostfixCommand {

    public final int getNumberOfParameters() {
        return 1;
    }

    public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
        node.childrenAccept(runtime.ev, null);
        Comparable<?> param = runtime.stack.pop();
        return new Comparable<?>[] { param };
    }

    public static Integer weekOfYear(Comparable<?> param, Calendar cal) throws ParseException {
        if (param == null) {
            return null;
        }
        if (param instanceof Timestamp || param instanceof java.sql.Date) {
            java.util.Date ts = (java.util.Date) param;
            cal.setTimeInMillis(ts.getTime());
            return new Integer(cal.get(WEEK_OF_YEAR));
        }
        throw new ParseException(WRONG_TYPE + " weekofyear(" + param.getClass() + ")");
    }

    public Comparable<?> getResult(Comparable<?>... comparables) throws ParseException {
        return weekOfYear(comparables[0], JepRuntime.getCalendar());
    }
}
