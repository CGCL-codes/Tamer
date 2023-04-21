package edu.kds.newNativeSimulator.logic;

import edu.kds.misc.BinaryNumber;
import edu.kds.newNativeSimulator.SeriousSimException;
import edu.kds.newNativeSimulator.SimException;

public class WrEnFlipFlopLogic extends CompLogic {

    private static final long serialVersionUID = -1852709300268656279L;

    private BinaryNumber[] currentlyOut;

    public void resetClockInternally() {
        int val = component.component.getParamValue("init").intValue();
        int size = component.component.getParamValue("Sz").intValue();
        currentlyOut = new BinaryNumber[component.component.getArraySize()];
        for (int v = 0; v < currentlyOut.length; v++) {
            currentlyOut[v] = new BinaryNumber(val, size);
        }
    }

    private BinaryNumber[] tmpIn;

    private BinaryNumber[] tmpWrEn;

    public void prepareClockInternally() {
        try {
            tmpIn = getValue("in");
            tmpWrEn = getValue("wrEnable");
        } catch (SimException exc) {
            throw new SeriousSimException("Kan ikke opdatere tilstanden af " + component.component.name + " da ind-v�rdien er udefineret");
        }
    }

    public void fireClockInternally() {
        for (int i = 0; i < component.component.getArraySize(); i++) {
            if (tmpWrEn[i].intValue() > 0) {
                currentlyOut[i] = tmpIn[i];
                tmpIn[i] = null;
            }
        }
    }

    protected String[] getOutConnectorNames() {
        return new String[] { "out" };
    }

    public BinaryNumber[] getOutConnectorValue(String connector) {
        if (connector.equalsIgnoreCase("out")) return currentlyOut;
        throw new Error("Error in signal propagation. Unknown connector: " + connector);
    }

    public long getStableOutTime(String connector, int newParam) {
        return 2;
    }

    /**
	 * FlipFlops is to latches consisting each of one and, one or and two nots 
	 */
    public long getTransistors() {
        int size = component.component.getParamValue("Sz").intValue();
        long transistors = size * (4 + 4 + 2 * 2 + 1);
        return transistors;
    }
}
