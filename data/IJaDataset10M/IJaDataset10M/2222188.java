package pcode;

import jaguar.Jaguar;
import jaguar.JaguarPCode;
import jaguar.JaguarVM;
import java.io.File;
import java.util.ArrayList;

/**
 * @author peter
 *
 * <p>
 * <b>railroad:</b>
 * <pre>
 * ---- SET ----- variablename --- value ----------|
 * </pre>
 * Creates or updates the variable with the desired value.
 * <p>
 * The status register is set to the value for numeric values and to the 
 * linenumber (process counter register) of the next statement for non numerical values.
 * <p>
 * An earlier published variable stays published.
 * @see JaguarVM#macros(String)
 * @see JaguarVM#functions(String)
 * @see JaguarVM#setLabel(String, String)
 * @see JaguarPUBLISH
 * @see JaguarUNPUBLISH
 */
public class JaguarSET extends JaguarPCode {

    /**
	 * @param vm
	 * @param src
	 * @param line
	 * @param arg 
	 */
    public JaguarSET(JaguarVM vm, File src, String line, String arg) {
        super(vm, src, line, arg);
    }

    protected void complete(ArrayList can, int i) {
    }

    public int tokenType(int i) {
        if (i < 0) return JaguarPCode.OPCODE;
        if (!isToken(i)) return JaguarPCode.COMMENT;
        if (i == 0) return JaguarPCode.SYMBOL;
        return JaguarPCode.OPERAND;
    }

    protected void execute() {
        String name = lowerCaseToken(0);
        int value = vm.getPc();
        String text = vm.macros(getArgTail(1));
        if (text.matches("[-]{0,1}[0-9]+")) {
            value = Integer.parseInt(text, 10);
        }
        vm.setLabel(name, text);
        vm.setStatus(value);
        vm.logLine(Jaguar.LOGBLUE, Jaguar.LOGWHITE, Jaguar.LOGPROPORTIONAL, "Set " + name + " to '" + text + "'", null, null);
    }
}
