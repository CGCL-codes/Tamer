package org.rakiura.bool;

/**
 * Represents connection to the output of a gate.
 * 
 * <br><br>
 * Connection.java<br>
 * Created: 13/02/2004 10:17:14<br>
 * 
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version $version$ $Revision: 1.3 $
 */
public class Connection {

    private Gate inputGate;

    private int inputIndex;

    private Gate outputGate;

    private int outputIndex;

    /**
	 * Initializes not connected connection.
	 */
    public Connection() {
    }

    /**
	 * Initializes this connection with a given input/output gates.
	 * @param inputGate 
	 * @param inputIndex 
	 * @param outputGate 
	 * @param outputIndex 
	 */
    public Connection(Gate inputGate, int inputIndex, Gate outputGate, int outputIndex) {
        this.inputGate = inputGate;
        this.inputIndex = inputIndex;
        this.outputGate = outputGate;
        this.outputIndex = outputIndex;
    }

    /** Returns the input gate of this connection.
	 * @return input gate of this connection. */
    public Gate getInputGate() {
        return this.inputGate;
    }

    /** Sets the input gate of this connection.
	 * @param gate input gate 
	 **/
    public void setInputGate(Gate gate) {
        this.inputGate = gate;
    }

    public int getInputIndex() {
        return this.inputIndex;
    }

    public void setInputIndex(int index) {
        this.inputIndex = index;
    }

    /** Returns the output gate of this connection.
	 * @return output gate of this connection. */
    public Gate getOutputGate() {
        return this.outputGate;
    }

    /** Sets the output gate of this connection.
	 * @param gate output gate.*/
    public void setOutputGate(Gate gate) {
        this.outputGate = gate;
    }

    public int getOutputIndex() {
        return this.outputIndex;
    }

    public void setOutputIndex(int index) {
        this.outputIndex = index;
    }

    public int getValue() {
        this.inputGate.evaluate();
        return this.inputGate.getValue(this.inputIndex);
    }

    public String toString() {
        return this.inputGate + "[" + this.inputIndex + "]" + "->" + this.outputGate + "[" + this.outputIndex + "]";
    }

    public String toDotString() {
        final StringBuffer b = new StringBuffer();
        b.append(this.inputGate.toString()).append(":").append("c").append(this.inputIndex);
        b.append(" -> ");
        b.append(this.outputGate.toString()).append(":").append("c").append(this.outputIndex);
        b.append(";");
        return b.toString();
    }
}
