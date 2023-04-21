package mips.model;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import mips.model.helper.AtomicCombLogicComp;
import mips.util.Signal;

public class SignExtend extends AtomicCombLogicComp {

    int bitLen = 32;

    private int extendSize = 16;

    public SignExtend() {
        this("SignExtend");
    }

    public SignExtend(String name) {
        String[] inports = { "In" };
        String[] outports = { "Out" };
        setValues(name, inports, outports, null);
        addTestInput("In", new Signal(Signal.numToBinStr(BigInteger.valueOf(2), 16)), 0);
        addTestInput("In", new Signal(Signal.numToBinStr(BigInteger.valueOf(-2), 16)), 0);
    }

    public void initialize() {
        super.initialize();
        prop = ExperimentalFrame.getIntProperty("SignExtend");
    }

    protected Map<String, String> compute() {
        String newVal = null;
        String inData = inportValMap.get("In");
        char[] head = new char[extendSize];
        char c = inData.charAt(0);
        for (int j = 0; j < extendSize; j++) {
            head[j] = c;
        }
        newVal = new String(head);
        newVal = newVal.concat(inData);
        Map<String, String> results = new HashMap<String, String>();
        results.put("Out", newVal);
        return results;
    }

    protected boolean gotInputs() {
        return inportValMap.containsKey("In");
    }
}
