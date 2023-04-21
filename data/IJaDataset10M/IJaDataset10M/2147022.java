package jaga.pj.circuits;

import jaga.BitSet;
import java.util.HashSet;
import java.util.Random;

/** Modifies a circuit to be feed forward by finding loops and re-routing the recurrent connection
 * to a primary input.  If latchSafe is true then it allows loops which have a latch in them.
 *
 * @author  mmg20
 */
public class DynamicFeedForwardMapping implements CircuitMapping {

    protected static final int DEF_GENE_DEF_POS = 0;

    protected static final boolean DEF_LATCH_SAFE = false;

    protected boolean latchSafe;

    protected CircuitMapping inMap;

    protected int bitsPerVar;

    protected int geneSize;

    protected int[] inputDefPosWithinGene;

    protected int genesDefPos;

    public String toString() {
        String rv = "Dynamic Feed Forward Mapping with: ";
        rv += "\n    Bits Per Variable = " + bitsPerVar;
        rv += "\n    Gene Size = " + geneSize;
        rv += "\n    Input Definition Position Within Gene = " + jaga.ESLib.intArr2String(inputDefPosWithinGene);
        rv += "\n    Genes Definition Position Within Genotype = " + genesDefPos;
        rv += "\n    Loops with latches are safe = " + latchSafe;
        rv += "\n    Internal Mapping: " + inMap;
        return rv;
    }

    protected Random rnd = new Random();

    /** Creates a new instance of DynamicFeedForwardMapping */
    public DynamicFeedForwardMapping(CircuitMapping inMap, int bitsPerVar, int geneSize, int[] inputDefPosWithinGene) {
        this(inMap, bitsPerVar, geneSize, inputDefPosWithinGene, DEF_GENE_DEF_POS);
    }

    public DynamicFeedForwardMapping(CircuitMapping inMap, int bitsPerVar, int geneSize, int[] inputDefPosWithinGene, int genesDefPos) {
        this(inMap, bitsPerVar, geneSize, inputDefPosWithinGene, genesDefPos, DEF_LATCH_SAFE);
    }

    public DynamicFeedForwardMapping(CircuitMapping inMap, int bitsPerVar, int geneSize, int[] inputDefPosWithinGene, int genesDefPos, boolean latchSafe) {
        this.inMap = inMap;
        this.bitsPerVar = bitsPerVar;
        this.geneSize = geneSize;
        this.inputDefPosWithinGene = inputDefPosWithinGene;
        this.genesDefPos = genesDefPos;
        this.latchSafe = latchSafe;
    }

    public SimulatorLogicElement getElementFromAddress(SimulatorLogicElement[][] inoutels, int address) {
        return inMap.getElementFromAddress(inoutels, address);
    }

    /** Takes and individual and an array of arrays of logic elements where this
     * individual must be instantiated.
     * @param individual The individual to be mapped into a circuit.
     * @return Array consisting of three subarrays: The first will represent
     * the inputs to the circuit and is where the inputs will be fed in.  The second
     * represents the elements that are the outputs of the circuit and is where they
     * will be read out from.  The last is an array with all the elements of the
     * circuit.
     */
    public SimulatorLogicElement[][] map(BitSet individual) {
        SimulatorLogicElement[][] inoutels = inMap.map(individual);
        HashSet seenBefore = new HashSet();
        HashSet seenThisRound = new HashSet();
        for (int ql = 0; ql < inoutels[1].length; ql++) {
            removeLoops(inoutels[1][ql], seenBefore, seenThisRound, new HashSet(), inoutels, individual);
            seenBefore.addAll(seenThisRound);
            seenThisRound = new HashSet();
        }
        return inoutels;
    }

    protected void removeLoops(SimulatorLogicElement unit, HashSet seenBefore, HashSet seenThisRound, HashSet seenThisPath, SimulatorLogicElement[][] inoutels, BitSet individual) {
        seenThisRound.add(unit);
        seenThisPath.add(unit);
        if (latchSafe && unit instanceof Latch) {
            seenThisPath = new HashSet();
        }
        SimulatorLogicElement[] unitInputs = unit.getInputs();
        if (unitInputs != null) {
            for (int ul = 0; ul < unitInputs.length; ul++) {
                if (!seenBefore.contains(unitInputs[ul])) {
                    if (seenThisPath.contains(unitInputs[ul])) {
                        int randomInputIx = rnd.nextInt(inoutels[0].length);
                        unitInputs[ul] = inoutels[0][randomInputIx];
                        reRouteInput(individual, unit, ul, inoutels, randomInputIx);
                    } else {
                        if (!seenThisRound.contains(unitInputs[ul])) {
                            removeLoops(unitInputs[ul], seenBefore, seenThisRound, (HashSet) seenThisPath.clone(), inoutels, individual);
                        }
                    }
                }
            }
        }
    }

    protected void reRouteInput(BitSet individual, SimulatorLogicElement unit, int iix, SimulatorLogicElement[][] inoutels, int piix) {
        int unitIx = jaga.ESLib.indexOf(unit, inoutels[2]);
        int genePos = genesDefPos + geneSize * unitIx;
        int inputDefPos = genePos + inputDefPosWithinGene[iix];
        int totalAdd = 1 << bitsPerVar;
        int inputAddress = totalAdd - piix - 1;
        individual.intToBits(inputAddress, inputDefPos, bitsPerVar);
    }

    public void resetDelays(Object delayDef) {
        rnd = new Random(((Long) delayDef).longValue());
        inMap.resetDelays(delayDef);
    }
}
