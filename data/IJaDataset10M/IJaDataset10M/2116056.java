package edu.byu.ece.bitwidth.ptolemy.parser.sysgen;

public class SimulinkSignal {

    public static final String SIGNED = "Signed  (2's comp)";

    public static final String UNSIGNED = "Unsigned";

    public static final String BOOLEAN = "Boolean";

    /** The type of signal the SimulinkSignal is. It can be SIGNED, UNSIGNED, and BOOLEAN */
    private String signalType = null;

    /** The bit width of the signal. It is -1 when it is not valid or has not been set */
    private int bitWidth = -1;

    /** The binary point of the signal. It is -1 when it is not valid or has not been set */
    private int binaryPoint = -1;

    public SimulinkSignal() {
    }

    /**
	 * Creates a new SimulinkSignal object with the parameters passed in.
	 * 
	 * @param signalType - the type of the signal
	 * @param bitWidth - the bit width of the signal
	 * @param binaryPoint - the binary point of the signal
	 */
    public SimulinkSignal(String signalType, int bitWidth, int binaryPoint) {
        setSignal(signalType, bitWidth, binaryPoint);
    }

    /**
	 * Sets the SimulinkSignal based on the ports parameters.
	 * 
	 * @param port - The port that will set the SimulinkSignals parameters
	 */
    public void setSignal(SimulinkPort port) {
        setSignal(port.getSignal());
    }

    /**
	 * Sets the SimulinkSignal based on the ports parameters found in the parameter
	 * list. If the block has a boolean output it will set the bit width to 1 and
	 * binary point to 0 rather than taking the values from the parameter list.
	 * 
	 * @param block - The block that will set the SimulinkSignals parameters
	 */
    public void setSignal(SimulinkBlock block) {
        if (block.getValue(SimulinkModel.ARITH_TYPE).equalsIgnoreCase("boolean")) {
            setSignal(BOOLEAN, 1, 0);
        } else {
            try {
                setSignal(block.getValue("arith_type"), Integer.parseInt(block.getValue("n_bits")), Integer.parseInt(block.getValue("bin_pt")));
            } catch (NumberFormatException e) {
                System.out.println("Problem with " + block.getName() + ". Cannont parse bit width or binary point into Integer");
            }
        }
    }

    /**
	 * Sets the SimulinkSignal based on the signals parameters.
	 * 
	 * @param signal - The signal that will set the SimulinkSignals parameters
	 */
    public void setSignal(SimulinkSignal signal) {
        setSignal(signal.getSignalType(), signal.getBitWidth(), signal.getBinaryPoint());
    }

    /**
	 * Sets the SimulinkSignal based on the parameters.
	 * 
	 * @param signalType - The type of the signal
	 * @param bitWidth - The bit width of the signal
	 * @param binaryPoint - The binary point of the signal
	 */
    public void setSignal(String signalType, int bitWidth, int binaryPoint) {
        setSignalType(signalType);
        setBitWidth(bitWidth);
        setBinaryPoint(binaryPoint);
    }

    /**
	 * Gets the type of the signal
	 * 
	 * @return the type of the signal
	 */
    public String getSignalType() {
        return signalType;
    }

    /**
	 * Sets the type of the signal. It checks to make sure a valid signalType was
	 * passed in. If it is not a valid signalType it will display an error.
	 * 
	 * @param signalType - the type of signal that the SimulinkSignal will be set
	 */
    public void setSignalType(String signalType) {
        if (signalType == null) {
            this.signalType = null;
        } else if (signalType.toLowerCase().startsWith("signed")) {
            this.signalType = SIGNED;
        } else if (signalType.toLowerCase().startsWith("un")) {
            this.signalType = UNSIGNED;
        } else if (signalType.toLowerCase().startsWith("bool")) {
            this.signalType = BOOLEAN;
        } else {
            MessageGenerator.briefError("Error: Unrecognized arithmetic type: " + signalType);
        }
    }

    /**
	 * Gets the bit width of the signal.
	 * 
	 * @return the bit width of the signal
	 */
    public int getBitWidth() {
        return bitWidth;
    }

    /**
	 * Sets the bit width of the signal.
	 * 
	 * @param bitWidth - the bit width that the signal will be set to
	 */
    public void setBitWidth(int bitWidth) {
        this.bitWidth = bitWidth;
    }

    /**
	 * Sets the binary point of the signal.
	 * 
	 * @return the binary point of the signal
	 */
    public int getBinaryPoint() {
        return binaryPoint;
    }

    /**
	 * Sets the binary point of the signal.
	 * 
	 * @param binaryPoint - the binary point that the signal will be set to.
	 */
    public void setBinaryPoint(int binaryPoint) {
        this.binaryPoint = binaryPoint;
    }

    /** 
	 * Checks to see if all the parameters (signalType, bitWidth, binaryPoint)
	 * have been set.
	 * 
	 * @return returns true if all the parameters have been set (they are valid numbers),
	 * 			otherwise, it returns false
	 */
    public boolean isDone() {
        if (signalType == null || bitWidth == -1 || binaryPoint == -1) {
            return false;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SimulinkSignal other = (SimulinkSignal) obj;
        if (signalType == null) {
            if (other.signalType != null) return false;
        } else if (!signalType.equals(other.signalType)) {
            return false;
        }
        if (bitWidth == -1) {
            if (other.bitWidth != -1) return false;
        } else if (bitWidth != other.bitWidth) {
            return false;
        }
        if (binaryPoint == -1) {
            if (other.binaryPoint != -1) return false;
        } else if (binaryPoint != other.binaryPoint) {
            return false;
        }
        return true;
    }

    public String toString() {
        String text = "";
        text += "\nSignal Type: " + signalType;
        text += "\nBit Width: " + bitWidth;
        text += "\nBinary Point: " + binaryPoint;
        return text;
    }
}
