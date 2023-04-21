package com.googlecode.jerato.library.logic;

import com.googlecode.jerato.core.FunctionParameters;
import com.googlecode.jerato.core.FunctionTransfer;
import com.googlecode.jerato.core.logic.LogicFunction;
import com.googlecode.jerato.core.logic.LogicParameters;
import com.googlecode.jerato.core.logic.LogicTransfer;

public abstract class LogicFunctionImpl implements LogicFunction {

    public void call(FunctionTransfer trans, FunctionParameters input, FunctionParameters output) {
        if (!(trans instanceof LogicTransfer) || !(input instanceof LogicParameters) || !(output instanceof LogicParameters)) {
            trans = new LogicTransferImpl(trans);
            input = new LogicParametersImpl(input);
            output = new LogicParametersImpl(output);
        }
        execute((LogicTransfer) trans, (LogicParameters) input, (LogicParameters) output);
        ((LogicParameters) output).flushParent();
        ((LogicTransfer) trans).flushParent();
    }
}
