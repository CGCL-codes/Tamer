package cc.mallet.fst;

import java.util.logging.Level;
import java.util.logging.Logger;
import cc.mallet.fst.SumLatticeDefault.LatticeNode;
import cc.mallet.fst.Transducer.State;
import cc.mallet.fst.Transducer.TransitionIterator;
import cc.mallet.types.DenseVector;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.LabelVector;
import cc.mallet.types.MatrixOps;
import cc.mallet.types.Sequence;
import cc.mallet.util.MalletLogger;

public class SumLatticeConstrained extends SumLatticeDefault {

    private static Logger logger = MalletLogger.getLogger(SumLatticeConstrained.class.getName());

    public SumLatticeConstrained(Transducer t, Sequence input, Sequence output, Segment requiredSegment, Sequence constrainedSequence) {
        this(t, input, output, (Transducer.Incrementor) null, null, makeConstraints(t, input, output, requiredSegment, constrainedSequence));
    }

    private static int[] makeConstraints(Transducer t, Sequence inputSequence, Sequence outputSequence, Segment requiredSegment, Sequence constrainedSequence) {
        if (constrainedSequence.size() != inputSequence.size()) throw new IllegalArgumentException("constrainedSequence.size [" + constrainedSequence.size() + "] != inputSequence.size [" + inputSequence.size() + "]");
        int[] constraints = new int[constrainedSequence.size() + 1];
        for (int c = 0; c < constraints.length; c++) constraints[c] = 0;
        for (int i = requiredSegment.getStart(); i <= requiredSegment.getEnd(); i++) {
            int si = t.stateIndexOfString((String) constrainedSequence.get(i));
            if (si == -1) logger.warning("Could not find state " + constrainedSequence.get(i) + ". Check that state labels match startTages and inTags, and that all labels are seen in training data.");
            constraints[i + 1] = si + 1;
        }
        if (requiredSegment.getEnd() + 2 < constraints.length) {
            String endTag = requiredSegment.getInTag().toString();
            int statei = t.stateIndexOfString(endTag);
            if (statei == -1) throw new IllegalArgumentException("Could not find state " + endTag + ". Check that state labels match startTags and InTags.");
            constraints[requiredSegment.getEnd() + 2] = -(statei + 1);
        }
        logger.fine("Segment:\n" + requiredSegment.sequenceToString() + "\nconstrainedSequence:\n" + constrainedSequence + "\nConstraints:\n");
        for (int i = 0; i < constraints.length; i++) {
            logger.fine(constraints[i] + "\t");
        }
        logger.fine("");
        return constraints;
    }

    /** Create a lattice that constrains its transitions such that the
	 * <position,label> pairs in "constraints" are adhered
	 * to. constraints is an array where each entry is the index of
	 * the required label at that position. An entry of 0 means there
	 * are no constraints on that <position, label>. Positive values
	 * mean the path must pass through that state. Negative values
	 * mean the path must _not_ pass through that state. NOTE -
	 * constraints.length must be equal to output.size() + 1. A
	 * lattice has one extra position for the initial
	 * state. Generally, this should be unconstrained, since it does
	 * not produce an observation.
	 */
    public SumLatticeConstrained(Transducer trans, Sequence input, Sequence output, Transducer.Incrementor incrementor, LabelAlphabet outputAlphabet, int[] constraints) {
        if (false && logger.isLoggable(Level.FINE)) {
            logger.fine("Starting Lattice");
            logger.fine("Input: ");
            for (int ip = 0; ip < input.size(); ip++) logger.fine(" " + input.get(ip));
            logger.fine("\nOutput: ");
            if (output == null) logger.fine("null"); else for (int op = 0; op < output.size(); op++) logger.fine(" " + output.get(op));
            logger.fine("\n");
        }
        this.t = trans;
        this.input = input;
        this.output = output;
        latticeLength = input.size() + 1;
        int numStates = t.numStates();
        nodes = new LatticeNode[latticeLength][numStates];
        gammas = new double[latticeLength][numStates];
        double outputCounts[][] = null;
        if (outputAlphabet != null) outputCounts = new double[latticeLength][outputAlphabet.size()];
        for (int i = 0; i < numStates; i++) {
            for (int ip = 0; ip < latticeLength; ip++) gammas[ip][i] = Transducer.IMPOSSIBLE_WEIGHT;
        }
        logger.fine("Starting Constrained Foward pass");
        boolean atLeastOneInitialState = false;
        for (int i = 0; i < numStates; i++) {
            double initialWeight = t.getState(i).getInitialWeight();
            if (initialWeight > Transducer.IMPOSSIBLE_WEIGHT) {
                getLatticeNode(0, i).alpha = initialWeight;
                atLeastOneInitialState = true;
            }
        }
        if (atLeastOneInitialState == false) logger.warning("There are no starting states!");
        for (int ip = 0; ip < latticeLength - 1; ip++) for (int i = 0; i < numStates; i++) {
            logger.fine("ip=" + ip + ", i=" + i);
            if (constraints[ip] > 0) {
                if (constraints[ip] - 1 != i) {
                    logger.fine("Current state does not match positive constraint. position=" + ip + ", constraint=" + (constraints[ip] - 1) + ", currState=" + i);
                    continue;
                }
            } else if (constraints[ip] < 0) {
                if (constraints[ip] + 1 == -i) {
                    logger.fine("Current state does not match negative constraint. position=" + ip + ", constraint=" + (constraints[ip] + 1) + ", currState=" + i);
                    continue;
                }
            }
            if (nodes[ip][i] == null || nodes[ip][i].alpha == Transducer.IMPOSSIBLE_WEIGHT) {
                if (nodes[ip][i] == null) logger.fine("nodes[ip][i] is NULL"); else if (nodes[ip][i].alpha == Transducer.IMPOSSIBLE_WEIGHT) logger.fine("nodes[ip][i].alpha is -Inf");
                logger.fine("-INFINITE weight or NULL...skipping");
                continue;
            }
            State s = t.getState(i);
            TransitionIterator iter = s.transitionIterator(input, ip, output, ip);
            if (logger.isLoggable(Level.FINE)) logger.fine(" Starting Forward transition iteration from state " + s.getName() + " on input " + input.get(ip).toString() + " and output " + (output == null ? "(null)" : output.get(ip).toString()));
            while (iter.hasNext()) {
                State destination = iter.nextState();
                boolean legalTransition = true;
                if (ip + 1 < constraints.length && constraints[ip + 1] > 0 && ((constraints[ip + 1] - 1) != destination.getIndex())) {
                    logger.fine("Destination state does not match positive constraint. Assigning -infinite weight. position=" + (ip + 1) + ", constraint=" + (constraints[ip + 1] - 1) + ", source =" + i + ", destination=" + destination.getIndex());
                    legalTransition = false;
                } else if (((ip + 1) < constraints.length) && constraints[ip + 1] < 0 && (-(constraints[ip + 1] + 1) == destination.getIndex())) {
                    logger.fine("Destination state does not match negative constraint. Assigning -infinite weight. position=" + (ip + 1) + ", constraint=" + (constraints[ip + 1] + 1) + ", destination=" + destination.getIndex());
                    legalTransition = false;
                }
                if (logger.isLoggable(Level.FINE)) logger.fine("Forward Lattice[inputPos=" + ip + "][source=" + s.getName() + "][dest=" + destination.getName() + "]");
                LatticeNode destinationNode = getLatticeNode(ip + 1, destination.getIndex());
                destinationNode.output = iter.getOutput();
                double transitionWeight = iter.getWeight();
                if (legalTransition) {
                    logger.fine("transitionWeight=" + transitionWeight + " nodes[" + ip + "][" + i + "].alpha=" + nodes[ip][i].alpha + " destinationNode.alpha=" + destinationNode.alpha);
                    destinationNode.alpha = Transducer.sumLogProb(destinationNode.alpha, nodes[ip][i].alpha + transitionWeight);
                    logger.fine("Set alpha of latticeNode at ip = " + (ip + 1) + " stateIndex = " + destination.getIndex() + ", destinationNode.alpha = " + destinationNode.alpha);
                } else {
                    logger.fine("Illegal transition from state " + i + " to state " + destination.getIndex() + ". Setting alpha to -Inf");
                }
            }
        }
        totalWeight = Transducer.IMPOSSIBLE_WEIGHT;
        for (int i = 0; i < numStates; i++) if (nodes[latticeLength - 1][i] != null) {
            if (constraints[latticeLength - 1] > 0 && i != constraints[latticeLength - 1] - 1) continue;
            if (constraints[latticeLength - 1] < 0 && -i == constraints[latticeLength - 1] + 1) continue;
            logger.fine("Summing final lattice weight. state=" + i + ", alpha=" + nodes[latticeLength - 1][i].alpha + ", final weight = " + t.getState(i).getFinalWeight());
            totalWeight = Transducer.sumLogProb(totalWeight, (nodes[latticeLength - 1][i].alpha + t.getState(i).getFinalWeight()));
        }
        if (totalWeight == Transducer.IMPOSSIBLE_WEIGHT) return;
        for (int i = 0; i < numStates; i++) if (nodes[latticeLength - 1][i] != null) {
            State s = t.getState(i);
            nodes[latticeLength - 1][i].beta = s.getFinalWeight();
            gammas[latticeLength - 1][i] = nodes[latticeLength - 1][i].alpha + nodes[latticeLength - 1][i].beta - totalWeight;
            if (incrementor != null) {
                double p = Math.exp(gammas[latticeLength - 1][i]);
                assert (p >= 0.0 && p <= 1.0 && !Double.isNaN(p)) : "p=" + p + " gamma=" + gammas[latticeLength - 1][i];
                incrementor.incrementFinalState(s, p);
            }
        }
        for (int ip = latticeLength - 2; ip >= 0; ip--) {
            for (int i = 0; i < numStates; i++) {
                if (nodes[ip][i] == null || nodes[ip][i].alpha == Transducer.IMPOSSIBLE_WEIGHT) continue;
                State s = t.getState(i);
                TransitionIterator iter = s.transitionIterator(input, ip, output, ip);
                while (iter.hasNext()) {
                    State destination = iter.nextState();
                    if (logger.isLoggable(Level.FINE)) logger.fine("Backward Lattice[inputPos=" + ip + "][source=" + s.getName() + "][dest=" + destination.getName() + "]");
                    int j = destination.getIndex();
                    LatticeNode destinationNode = nodes[ip + 1][j];
                    if (destinationNode != null) {
                        double transitionWeight = iter.getWeight();
                        assert (!Double.isNaN(transitionWeight));
                        double oldBeta = nodes[ip][i].beta;
                        assert (!Double.isNaN(nodes[ip][i].beta));
                        nodes[ip][i].beta = Transducer.sumLogProb(nodes[ip][i].beta, destinationNode.beta + transitionWeight);
                        assert (!Double.isNaN(nodes[ip][i].beta)) : "dest.beta=" + destinationNode.beta + " trans=" + transitionWeight + " sum=" + (destinationNode.beta + transitionWeight) + " oldBeta=" + oldBeta;
                        assert (!Double.isNaN(nodes[ip][i].alpha));
                        assert (!Double.isNaN(transitionWeight));
                        assert (!Double.isNaN(nodes[ip + 1][j].beta));
                        assert (!Double.isNaN(totalWeight));
                        if (incrementor != null || outputAlphabet != null) {
                            double xi = nodes[ip][i].alpha + transitionWeight + nodes[ip + 1][j].beta - totalWeight;
                            double p = Math.exp(xi);
                            assert (p > Transducer.IMPOSSIBLE_WEIGHT && !Double.isNaN(p)) : "xis[" + ip + "][" + i + "][" + j + "]=" + xi;
                            if (incrementor != null) incrementor.incrementTransition(iter, p);
                            if (outputAlphabet != null) {
                                int outputIndex = outputAlphabet.lookupIndex(iter.getOutput(), false);
                                assert (outputIndex >= 0);
                                outputCounts[ip][outputIndex] += p;
                            }
                        }
                    }
                }
                gammas[ip][i] = nodes[ip][i].alpha + nodes[ip][i].beta - totalWeight;
            }
        }
        if (incrementor != null) for (int i = 0; i < numStates; i++) {
            double p = Math.exp(gammas[0][i]);
            assert (p > Transducer.IMPOSSIBLE_WEIGHT && !Double.isNaN(p));
            incrementor.incrementInitialState(t.getState(i), p);
        }
        if (outputAlphabet != null) {
            labelings = new LabelVector[latticeLength];
            for (int ip = latticeLength - 2; ip >= 0; ip--) {
                assert (Math.abs(1.0 - MatrixOps.sum(outputCounts[ip])) < 0.000001);
                ;
                labelings[ip] = new LabelVector(outputAlphabet, outputCounts[ip]);
            }
        }
    }
}
