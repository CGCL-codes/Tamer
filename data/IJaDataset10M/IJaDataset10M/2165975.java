package mips.model;

import java.util.HashMap;
import java.util.Map;
import mips.model.helper.AtomicCombLogicComp;
import mips.util.Signal;

public class Control_SCP extends AtomicCombLogicComp {

    private static Map<String, Object> table;

    static {
        table = new HashMap<String, Object>();
        Map<String, String> row;
        row = new HashMap<String, String>();
        row.put("RegDst", "1");
        row.put("ALUSrc", "0");
        row.put("MemToReg", "0");
        row.put("RegWrite", "1");
        row.put("MemRead", "0");
        row.put("MemWrite", "0");
        row.put("Branch", "0");
        row.put("Jump", "0");
        row.put("ALUOp", "10");
        table.put("000000", row);
        row = new HashMap<String, String>();
        row.put("RegDst", "0");
        row.put("ALUSrc", "1");
        row.put("MemToReg", "1");
        row.put("RegWrite", "1");
        row.put("MemRead", "1");
        row.put("MemWrite", "0");
        row.put("Branch", "0");
        row.put("Jump", "0");
        row.put("ALUOp", "00");
        table.put("100011", row);
        row = new HashMap<String, String>();
        row.put("RegDst", "x");
        row.put("ALUSrc", "1");
        row.put("MemToReg", "x");
        row.put("RegWrite", "0");
        row.put("MemRead", "0");
        row.put("MemWrite", "1");
        row.put("Branch", "0");
        row.put("Jump", "0");
        row.put("ALUOp", "00");
        table.put("101011", row);
        row = new HashMap<String, String>();
        row.put("RegDst", "x");
        row.put("ALUSrc", "0");
        row.put("MemToReg", "x");
        row.put("RegWrite", "0");
        row.put("MemRead", "0");
        row.put("MemWrite", "0");
        row.put("Branch", "1");
        row.put("Jump", "0");
        row.put("ALUOp", "01");
        table.put("000100", row);
        row = new HashMap<String, String>();
        row.put("RegDst", "0");
        row.put("ALUSrc", "0");
        row.put("MemToReg", "0");
        row.put("RegWrite", "0");
        row.put("MemRead", "0");
        row.put("MemWrite", "0");
        row.put("Branch", "0");
        row.put("Jump", "1");
        row.put("ALUOp", "00");
        table.put("000010", row);
    }

    protected static String getValue(String key, String subKey) {
        Map<String, String> row = (Map) table.get(key);
        String s = row.get(subKey);
        return s;
    }

    public Control_SCP() {
        this("Control");
    }

    public Control_SCP(String name) {
        String[] inports = { "In" };
        String[] outports = { "RegDst", "ALUOp", "ALUSrc", "Branch", "MemRead", "MemWrite", "RegWrite", "MemToReg", "Jump" };
        setValues(name, inports, outports, null);
        addTestInput("In", new Signal("000000"), 0);
        addTestInput("In", new Signal("100011"), 0);
        addTestInput("In", new Signal("101011"), 0);
        addTestInput("In", new Signal("000100"), 0);
        addTestInput("In", new Signal("000010"), 0);
    }

    public void initialize() {
        super.initialize();
        prop = ExperimentalFrame.getIntProperty("Control_SCP");
    }

    protected Map<String, String> compute() {
        String opcode = inportValMap.get("In");
        return compute(opcode);
    }

    protected static Map<String, String> compute(String opcode) {
        Map<String, String> results = new HashMap<String, String>();
        String field, val;
        String[] outports = { "RegDst", "ALUOp", "ALUSrc", "Branch", "MemRead", "MemWrite", "RegWrite", "MemToReg", "Jump" };
        for (int i = 0; i < outports.length; i++) {
            field = outports[i];
            val = getValue(opcode, field);
            results.put(field, val);
        }
        return results;
    }

    protected boolean gotInputs() {
        return inportValMap.containsKey("In");
    }
}
