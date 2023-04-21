package jaga.pj.circuits;

import jaga.BitSet;

/** A circuit mapping object defines the mapping from a genotype to the structure
 * of a circuit.  This can be used to customize faulty or non faulty circuits to
 * have different mappings like 'nearest neighbour', 'absolute position', and/or 
 * cater for variable length genotypes with adaptive mappings.
 * <p> Current convention is elements array is made of all elements to be updated
 * and last numInputs elements are the inputs which aren't updated..
 *
 * @author  Michael Garvie
 * @version 
 */
public interface CircuitMapping extends java.io.Serializable {

    public static int INPUTS = 0, OUTPUTS = 1, ELEMENTS = 2;

    /** Takes and individual and an array of arrays of logic elements where this
     * individual must be instantiated.
     * @param individual The individual to be mapped into a circuit.
     * @return Array consisting of three subarrays: The first will represent
     * the inputs to the circuit and is where the inputs will be fed in.  The second
     * represents the elements that are the outputs of the circuit and is where they
     * will be read out from.  The last is an array with all the elements of the
     * circuit.
    */
    public SimulatorLogicElement[][] map(BitSet individual);

    public SimulatorLogicElement getElementFromAddress(SimulatorLogicElement[][] inoutels, int address);

    public void resetDelays(Object delayDef);
}
