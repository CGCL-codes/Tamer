package fitlibrary.traverse.function;

import java.util.ArrayList;
import java.util.List;
import fitlibrary.closure.ICalledMethodTarget;
import fitlibrary.exception.table.ExtraCellsException;
import fitlibrary.exception.table.MissingCellsException;
import fitlibrary.global.PlugBoard;
import fitlibrary.parser.Parser;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.table.Table;

public class CombinationTraverse extends FunctionTraverse {

    private List<Object> topValues = new ArrayList<Object>();

    private boolean methodOK = false;

    private ICalledMethodTarget methodTarget = null;

    private Parser firstParser;

    public CombinationTraverse() {
    }

    public CombinationTraverse(Object sut) {
        super(sut);
    }

    @Override
    public Object interpretAfterFirstRow(Table table, TestResults testResults) {
        bindFirstRowToTarget(table.at(1), testResults);
        for (int i = 2; i < table.size(); i++) processRow(table.at(i), testResults);
        return null;
    }

    public void bindFirstRowToTarget(Row row, TestResults testResults) {
        Parser secondParser = null;
        try {
            methodTarget = PlugBoard.lookupTarget.findTheMethodMapped("combine", 2, this);
            Parser[] parameterParsers = methodTarget.getParameterParsers();
            firstParser = parameterParsers[0];
            secondParser = parameterParsers[1];
        } catch (Exception e) {
            row.error(testResults, e);
            return;
        }
        int rowLength = row.size();
        for (int i = 1; i < rowLength; i++) {
            Cell cell = row.at(i);
            try {
                topValues.add(secondParser.parseTyped(cell, testResults).getSubject());
            } catch (Exception e) {
                cell.error(testResults, e);
                return;
            }
        }
        methodOK = true;
    }

    public void processRow(Row row, TestResults testResults) {
        if (!methodOK) {
            row.ignore(testResults);
            return;
        }
        try {
            Object arg1 = firstParser.parseTyped(row.at(0), testResults).getSubject();
            if (row.size() - 1 < topValues.size()) throw new MissingCellsException("CombinationTraverse");
            if (row.size() - 1 > topValues.size()) throw new ExtraCellsException("CombinationTraverse");
            for (int i = 1; i < row.size(); i++) {
                Object result = methodTarget.invoke(new Object[] { arg1, topValues.get(i - 1) });
                methodTarget.checkResult(row.at(i), result, true, false, testResults);
            }
        } catch (Exception e) {
            row.error(testResults, e);
            return;
        }
    }
}
