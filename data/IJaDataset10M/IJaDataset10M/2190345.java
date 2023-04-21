package org.fernwood.jbasic.opcodes;

import org.fernwood.jbasic.JBasic;
import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.Tokenizer;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.statements.Statement;
import org.fernwood.jbasic.value.Value;

/**
 * @author cole
 * 
 */
public class OpEXEC extends AbstractOpcode {

    /**
	 * Pop top string from stack, and execute it as a statement.
	 * @throws JBasicException  if a stack underflow occurs or there is
	 * an execution error with the resulting statement.
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        String cmd = null;
        if (env.instruction.stringValid) cmd = env.instruction.stringOperand; else {
            cmd = env.pop().getString();
        }
        boolean fSandBox = false;
        if (env.instruction.integerValid && env.instruction.integerOperand >= 10) fSandBox = true;
        final Statement execStmt = new Statement(env.session, env.codeStream.debugger);
        execStmt.program = null;
        execStmt.byteCode = null;
        execStmt.statementObject = null;
        if (env.codeStream.statement != null) {
            execStmt.statementID = env.codeStream.statement.statementID;
            execStmt.fInDebugger = env.codeStream.statement.fInDebugger;
            execStmt.debugger = env.codeStream.statement.debugger;
        }
        Tokenizer originalText = new Tokenizer(cmd, JBasic.compoundStatementSeparator);
        final String verb = originalText.nextToken().toUpperCase();
        final Value aliasRecord = env.session.globals().reference("SYS$ALIASES");
        if (aliasRecord != null && aliasRecord.isType(Value.RECORD)) {
            final Value sub = aliasRecord.getElement(verb);
            if (sub != null) cmd = sub.getString() + " " + originalText.getBuffer();
        }
        execStmt.store(cmd);
        if (execStmt.status.equals(Status.NOCOMPILE)) execStmt.byteCode = null;
        boolean savedSandBox = false;
        if (fSandBox) {
            savedSandBox = env.session.globals().localReference("SYS$SANDBOX").getBoolean();
            env.session.enableSandbox(fSandBox);
        }
        Status status = null;
        status = execStmt.execute(env.localSymbols, false);
        if (fSandBox) env.session.enableSandbox(savedSandBox);
        if (env.instruction.integerValid && ((env.instruction.integerOperand % 10) == 1)) {
            env.push(env.localSymbols.reference("SYS$STATUS"));
            return;
        }
        if (!status.success()) throw new JBasicException(status);
        if (status.equals("*STEP")) throw new JBasicException(status);
        return;
    }
}
