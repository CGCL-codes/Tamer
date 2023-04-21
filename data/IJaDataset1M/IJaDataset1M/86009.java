package org.rakiura.evm;

/**
 * Executes the instruction from the stack. 
 * Instruction number is taken from the stack, and it is executed on the
 * current machine. If the instruction index is out of the current machine 
 * instruction range, it is automatically translated (normalized) into 
 * the appropriate range, based on the modulo operator and machine size. 
 *  
 * <br><br>
 * OpCall.java
 * Created: 2/12/2004 10:45:13 
 *
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version @version@ $Revision: 1.7 $
 * 
 */
public class OpCall extends Instruction {

    static final OpCall theInstance = new OpCall();

    private OpCall() {
    }

    public static OpCall getInstance() {
        return theInstance;
    }

    public void execute(ExecutionEngine e) throws SizeLimitExceededException {
        if (e.stack.size() == 0) {
            e.error();
            return;
        }
        int a1 = e.stack.pop();
        a1 = normalizeIndex(a1, e.getCM().size());
        e.getCM().execute(a1, e);
    }

    public String getMnemonic() {
        return "call";
    }
}
