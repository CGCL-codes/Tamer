package fitlibrary.traverse.function;

import fitlibrary.closure.ICalledMethodTarget;
import fitlibrary.exception.IgnoredException;
import fitlibrary.exception.method.BooleanMethodException;
import fitlibrary.exception.table.RowWrongWidthException;
import fitlibrary.global.PlugBoard;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Row;
import fitlibrary.table.Table;

public class ConstraintTraverse extends FunctionTraverse {

    private ICalledMethodTarget target;

    private boolean expected = true;

    protected boolean methodOK = false;

    protected int argCount = -1;

    protected boolean boundOK = false;

    public ConstraintTraverse() {
    }

    public ConstraintTraverse(Object sut, boolean expected) {
        super(sut);
        setExpected(expected);
    }

    public ConstraintTraverse(Object sut) {
        super(sut);
    }

    @Override
    public Object interpretAfterFirstRow(Table table, TestResults testResults) {
        bindFirstRowToTarget(table.at(1), testResults);
        for (int i = 2; i < table.size(); i++) processRow(table.at(i), testResults);
        return null;
    }

    public void bindFirstRowToTarget(Row row, TestResults testResults) {
        argCount = row.size();
        String argNames = "";
        for (int i = 0; i < argCount; i++) argNames += row.text(i, this) + " ";
        try {
            target = PlugBoard.lookupTarget.findTheMethodMapped(argNames, argCount, this);
            if (!target.returnsBoolean()) throw new BooleanMethodException(extendedCamel(argNames));
            target.setRepeatAndExceptionString(repeatString, exceptionString);
            methodOK = true;
        } catch (IgnoredException e) {
        } catch (Exception e) {
            if (expected) row.error(testResults, e); else row.pass(testResults);
        }
    }

    public void processRow(Row row, TestResults testResults) {
        if (!methodOK) {
            row.ignore(testResults);
            return;
        }
        if (row.size() != argCount) {
            row.error(testResults, new RowWrongWidthException(argCount));
            return;
        }
        try {
            boolean result = ((Boolean) target.invoke(row, testResults, true)).booleanValue();
            if (result == expected) row.pass(testResults); else row.fail(testResults);
        } catch (IgnoredException e) {
        } catch (Exception e) {
            if (expected) row.error(testResults, e); else row.pass(testResults);
        }
    }

    public void setExpected(boolean expected) {
        this.expected = expected;
    }
}
