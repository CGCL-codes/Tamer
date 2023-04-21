package jmathlib.toolbox.general;

import jmathlib.core.tokens.*;
import jmathlib.core.interpreter.*;
import jmathlib.core.functions.ExternalFunction;
import java.util.*;

/**An external function for getting the stored variables*/
public class whos extends ExternalFunction {

    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        Iterator iter = globals.getLocalVariables().getIterator();
        globals.getInterpreter().displayText("\nYour variables are:\n");
        globals.getInterpreter().displayText("\nName: \t size: \t type \n");
        if (getNArgIn(operands) == 1) {
            if ((operands[0] instanceof CharToken)) {
                String data = ((CharToken) operands[0]).getValue();
                if (data.equals("global")) iter = globals.getGlobalVariables().getIterator();
            }
        }
        while (iter.hasNext()) {
            Map.Entry next = ((Map.Entry) iter.next());
            Variable var = (Variable) next.getValue();
            OperandToken op = (OperandToken) var.getData();
            Boolean global = var.isGlobal();
            String name = var.getName();
            String line = "";
            line = name + "      \t";
            if (global) op = (OperandToken) globals.getGlobalVariables().getVariable(name).getData();
            if (op instanceof DataToken) {
                line += getSizeString(op);
                line += "  \t " + ((DataToken) op).getDataType() + " ";
            } else {
                line += "  \t unknown";
            }
            if (global) line += "(global)";
            globals.getInterpreter().displayText(line);
        }
        return null;
    }

    private String getSizeString(OperandToken tok) {
        int[] s = ((DataToken) tok).getSize();
        String line = "";
        if (s == null) return "";
        for (int i = 0; i < s.length; i++) {
            line += s[i];
            if (i < s.length - 1) line += "x";
        }
        return line;
    }
}
