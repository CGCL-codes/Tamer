package jaga.experiment;

import jaga.SampleData;
import jaga.experiment.Experiment;

/** Multiple Output Experiments allow many experiments sharing inputs to be bundled into the same one.
 * This is useful for example to combine many experiments, each deciding the fitness of different outputs
 * of a circuit to be combined into one.
 *
 * @author  Michael Garvie
 * @version 
 */
public class MultiOutputExperiment implements Experiment {

    protected Experiment[] exps;

    protected int mainExp = 0;

    public MultiOutputExperiment() {
    }

    /** Creates new MultiOutputExperiment */
    public MultiOutputExperiment(Experiment[] exps) {
        this.exps = exps;
    }

    /** Creates new MultiOutputExperiment */
    public MultiOutputExperiment(Experiment[] exps, int main) {
        this(exps);
        mainExp = main;
    }

    /** returns a fitness associated to a given input/output pair for
     * this experiment.  The fitness is a double and is in adjusted
     * fitness format.  From 0 to 1, 1 being perfectly fit.
     * @param in array of inputs
     * @param out array of outputs
     */
    public double getFitness(SampleData[] in, SampleData[] out) {
        double rv = 0;
        int thisOutBase = 0;
        for (int ol = 0; ol < exps.length; ol++) {
            int thisOutLen = exps[ol].getNumOfOutputs();
            rv += exps[ol].getFitness(in, jaga.ESLib.getLines(out, thisOutBase, thisOutLen));
            thisOutBase += thisOutLen;
        }
        return rv / exps.length;
    }

    /** generates an array of inputs suitable for this experiment
     * using default input sample separation.
     */
    public SampleData[] generateInput() {
        return exps[mainExp].generateInput();
    }

    /** generates an array of inputs suitable for this experiment.
     * @param inputSampleSeparation relative frequency of input to output samples.  If this is n, then n outputs will be sampled for every change in inputs.
     */
    public SampleData[] generateInput(int inputSampleSeparation) {
        return exps[mainExp].generateInput(inputSampleSeparation);
    }

    public int getNumOfInputs() {
        return exps[mainExp].getNumOfInputs();
    }

    public int getNumOfOutputs() {
        int rv = 0;
        for (int ol = 0; ol < exps.length; ol++) {
            rv += exps[ol].getNumOfOutputs();
        }
        return rv;
    }

    public String toString() {
        String rv = "Multiple Output Experiment with: ";
        for (int el = 0; el < exps.length; el++) {
            rv += "\n  Exp " + el + ":" + exps[el];
        }
        return rv;
    }
}
