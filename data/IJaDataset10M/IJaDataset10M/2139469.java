package fitlibrary.traverse.workflow.caller;

import fitlibrary.closure.ICalledMethodTarget;
import fitlibrary.global.PlugBoard;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Row;
import fitlibrary.traverse.Evaluator;
import fitlibrary.traverse.workflow.AbstractDoCaller;
import fitlibrary.typed.TypedObject;
import fitlibraryGeneric.typed.GenericTypedObject;

public class PostFixSpecialCaller extends AbstractDoCaller {

    private String methodName;

    private ICalledMethodTarget specialMethod;

    public PostFixSpecialCaller(Row row, Evaluator evaluator, boolean sequencing) {
        String firstCell = row.text(0, evaluator);
        if (row.size() == 4 && "=".equals(row.text(2, evaluator)) && ("set".equals(firstCell) || "setSymbolNamed".equals(evaluator.getRuntimeContext().extendedCamel(firstCell)))) return;
        if (row.size() >= 2) {
            methodName = row.text(row.size() - 2, evaluator);
            specialMethod = PlugBoard.lookupTarget.findPostfixSpecialMethod(evaluator, methodName);
            if (specialMethod != null) findMethodForInnerAction(row, evaluator, sequencing);
        }
    }

    private void findMethodForInnerAction(Row row, Evaluator evaluator, boolean sequencing) {
        try {
            PlugBoard.lookupTarget.findMethodByArity(row, 0, row.size() - 2, !sequencing, evaluator);
        } catch (Exception e) {
            setProblem(e);
        }
    }

    @Override
    public boolean isValid() {
        return specialMethod != null && !isProblem();
    }

    @Override
    public TypedObject run(Row row, TestResults testResults) throws Exception {
        return new GenericTypedObject(specialMethod.invoke(new Object[] { testResults, row }));
    }

    @Override
    public String ambiguityErrorMessage() {
        return methodName + "(TestResults,Row)";
    }
}
