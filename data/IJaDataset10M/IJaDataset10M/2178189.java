package base.userfunction;

import base.CProject;
import base.module.CModule;

public class CReturn extends CModule {

    @Override
    public void Init() {
        int[] Array = { 0, 1 };
        AssertParam(Array);
    }

    @Override
    public void Execute() {
        if (CProject.CurrentFunctionCall == null) Error("Оператор \"Возврат\" вне блока функции");
        if (Param.length == 1) CProject.CurrentFunctionCall.ReturnedValue = Param[0].GetValue().Clone();
        CProject.ExitStatus = CProject.EExitStatus.ExitFunction;
    }
}
