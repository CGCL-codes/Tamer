package jaga.pj.circuits.fpgaft;

import jaga.pj.circuits.SimulatorCircuit;
import jaga.pj.circuits.CircuitsLib;
import jaga.pj.circuits.SimulatorLogicElement;
import java.util.Vector;

/** Fault Model which iterates over the units used by this circuit.
 *
 * @author  mmg20
 */
public class SingleUsedFaultModel implements SingleFaultModel {

    protected SimulatorCircuit circuit;

    protected SingleFullFaultModel currentFaultModel;

    protected int nrOutsToCheck = -1;

    protected int reservedOutputs = 0;

    protected int faultTypes = FTLib.DEFAULT_FAULT_TYPES;

    public SingleUsedFaultModel(int faultTypes, SimulatorCircuit circuit, int nrOutsToCheck) {
        this(circuit, nrOutsToCheck);
        this.faultTypes = faultTypes;
    }

    public SingleUsedFaultModel(SimulatorCircuit circuit, int nrOutsToCheck, int resQ) {
        this(circuit, nrOutsToCheck);
        this.reservedOutputs = resQ;
    }

    /** Creates a new instance of SingleUsedFaultModel */
    public SingleUsedFaultModel(SimulatorCircuit circuit, int nrOutsToCheck) {
        this.circuit = circuit;
        this.nrOutsToCheck = nrOutsToCheck;
    }

    public SingleUsedFaultModel(int faultTypes, SimulatorCircuit circuit) {
        this(circuit);
        this.faultTypes = faultTypes;
    }

    /** Creates a new instance of SingleUsedFaultModel */
    public SingleUsedFaultModel(SimulatorCircuit circuit) {
        this.circuit = circuit;
    }

    public boolean hasMoreElements() {
        return currentFaultModel.hasMoreElements();
    }

    public Object nextElement() {
        return currentFaultModel.nextElement();
    }

    /** Resets the sequence of faults
     */
    public void reset() {
        SimulatorLogicElement[] outs = circuit.getInOutEls()[1];
        int outsToCheckNow;
        if (nrOutsToCheck < 0) {
            outsToCheckNow = outs.length;
        } else {
            outsToCheckNow = nrOutsToCheck;
        }
        Vector used = CircuitsLib.addConnectedGates(outs, outsToCheckNow);
        int nrUsed = used.size();
        for (int rql = 0; rql < reservedOutputs; rql++) {
            if (!used.contains(outs[nrOutsToCheck + rql])) {
                nrUsed++;
            }
        }
        currentFaultModel = new SingleFullFaultModel(0, nrUsed, faultTypes);
    }

    public String toString() {
        String rv = "Single Used Fault Model with:";
        rv += "\n    Outputs to Check: " + this.nrOutsToCheck;
        rv += "\n    Unchecked Outputs: " + this.reservedOutputs;
        rv += "\n    Fault Types: " + this.faultTypes;
        return rv;
    }
}
