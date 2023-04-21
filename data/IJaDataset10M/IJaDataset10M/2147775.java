package edu.kds.newNativeSimulator.logic;

import edu.kds.misc.BinaryNumber;
import edu.kds.newNativeSimulator.SeriousSimException;
import edu.kds.newNativeSimulator.SimException;

public class NorLogic extends CompLogic {

    private static final long serialVersionUID = -5571831889860910279L;

    protected String[] getOutConnectorNames() {
        return new String[] { "out" };
    }

    public BinaryNumber[] getOutConnectorValue(String connector) throws SimException {
        if (connector.equalsIgnoreCase("out")) {
            BinaryNumber[] ret = new BinaryNumber[component.component.getArraySize()];
            BinaryNumber[] inVals = getValue("in");
            for (int v = 0; v < inVals.length; v++) {
                BinaryNumber out = null;
                for (char bit : inVals[v].bits()) {
                    if (bit == '1') {
                        out = new BinaryNumber(0, 1);
                        break;
                    }
                }
                if (out == null) out = new BinaryNumber(1, 1);
                ret[v] = out;
            }
            return ret;
        }
        throw new SeriousSimException("Error in signal propagation. Unknown connector: " + connector);
    }

    public long getStableOutTime(String connector, int firstLast) throws SimException {
        if (connector.equalsIgnoreCase("out")) {
            long inTime = getStableInTime("in", firstLast);
            int size = component.component.getParamValue("Sz").intValue();
            return inTime + (long) (size / 8 + 1);
        }
        throw new Error("Error in signal propagation. Unknown connector: " + connector);
    }

    public long getTransistors() {
        int size = component.component.getParamValue("Sz").intValue();
        return size * 4;
    }
}
