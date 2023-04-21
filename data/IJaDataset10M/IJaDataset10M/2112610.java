package org.mmtk.harness.lang.pcode;

import org.mmtk.harness.Main;
import org.mmtk.harness.lang.Env;
import org.mmtk.harness.lang.ast.AST;
import org.mmtk.harness.lang.compiler.Register;
import org.mmtk.harness.lang.runtime.StackFrame;

public final class ExitOp extends UnaryOp {

    public ExitOp(AST source, Register status) {
        super(source, "exit", status);
    }

    @Override
    public void exec(Env env) {
        StackFrame frame = env.top();
        int rval = frame.get(operand).getIntValue();
        if (rval == 0) {
            Main.exitWithSuccess();
        }
        Main.exitWithFailure(rval);
    }
}
