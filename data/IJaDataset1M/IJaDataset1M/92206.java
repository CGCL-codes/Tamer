package edu.byu.ece.bitwidth.ptolemy.strategies;

import java.io.PrintStream;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.byu.ece.bitwidth.ptolemy.BitwidthDirector;
import edu.byu.ece.bitwidth.ptolemy.actor.BitwidthActor;
import edu.byu.ece.bitwidth.ptolemy.actor.TokenCollector;
import edu.byu.ece.bitwidth.ptolemy.data.BitwidthToken;
import ptolemy.data.Token;
import ptolemy.kernel.util.IllegalActionException;

public abstract class Competition extends PrecisionStrategy {

    private final QuantizationErrorModel model;

    protected final BitwidthDirector director;

    private final FindUniformBitwidth uniform;

    private List<BitwidthActor> quantizers;

    private Stack<BitwidthActor> testList = new Stack<BitwidthActor>();

    private PriorityQueue<QuantizerCost> costs;

    protected BitwidthActor currentNode = null;

    private static enum State {

        FindUniformWidth, Competition, Finalize
    }

    ;

    private State competitionState = State.FindUniformWidth;

    public Competition(QuantizationErrorModel model, BitwidthDirector director) {
        this.model = model;
        this.director = director;
        uniform = new FindUniformBitwidth(director);
    }

    public final void initializeMinimumBitwidthSearch() throws IllegalActionException {
        testList.clear();
        testList.addAll(quantizers);
        for (BitwidthActor q : testList) {
            q.savePrecision();
        }
        currentNode = testList.pop();
        if (runUniformBWSearch()) {
            int current = uniform.getUniformValue();
            uniform.setUniformValue(setInitialWidth(current));
            setNewNode(current);
        }
    }

    public QuantizationErrorModel getErrorModel() {
        return model;
    }

    /**
	 * The competition doesn't have an appropriate token, The Error model does.
	 */
    @Override
    public BitwidthToken newInstance(Token t) throws IllegalActionException {
        return model.newInstance(t);
    }

    public void initialize(List<BitwidthActor> quantizerList, PrintStream report) throws IllegalActionException {
        this.quantizers = quantizerList;
        costs = new PriorityQueue<QuantizerCost>(quantizers.size());
        if (runUniformBWSearch()) {
            uniform.initialize(quantizerList, report);
            uniform.setInitialUniformValues();
            competitionState = State.FindUniformWidth;
        } else {
            competitionState = State.Competition;
            initializeMinimumBitwidthSearch();
        }
    }

    /**
	 * 
	 * @return True if we need to run another competition, false if there is nothing more to do.
	 * @throws IllegalActionException
	 */
    protected boolean competitionResults() throws IllegalActionException {
        int precision1;
        int precision2;
        QuantizerCost a;
        BitwidthActor q;
        if (costs.isEmpty()) {
            return false;
        }
        boolean done = true;
        do {
            if (costs.isEmpty()) {
                return false;
            }
            a = costs.poll();
            q = a.getQuantizer();
            precision1 = a.getPrecision();
            precision2 = q.getPrecision();
            done = ((precision1 > 0 && precision1 != precision2) && precision1 < 32);
        } while (!done);
        q.setPrecision(precision1);
        q.savePrecision();
        logger.debug("Choosing Node " + a);
        costs.clear();
        testList.clear();
        testList.addAll(quantizers);
        return true;
    }

    public String getModelName() {
        return model.getName();
    }

    private long millis = 0;

    @Override
    public boolean postfire(List<TokenCollector> outports) throws IllegalActionException {
        if (millis == 0) {
            millis = System.currentTimeMillis();
        }
        boolean done = false;
        boolean reset = false;
        boolean doneIteration = true;
        boolean stable = true;
        try {
            if (!outputsSettled(outports)) {
                return true;
            }
        } catch (IllegalActionException ex) {
            stable = false;
        }
        if (!stable && competitionState == State.FindUniformWidth) {
            int pre = uniform.getUniformValue();
            boolean foundU = uniform.findUniformBW(false);
            if (foundU && pre == uniform.getUniformValue()) throw new IllegalActionException("This system is unstable: Stopping");
            previousValues.clear();
            director.resetStateElements();
            resetIterations();
            if (foundU) {
                logger.info("Done finding Uniform Bitwidth: " + uniform.getUniformValue() + "\n");
                competitionState = State.Competition;
                initializeMinimumBitwidthSearch();
            }
            return true;
        }
        if (competitionState == State.FindUniformWidth) {
            if (!uniform.postfire(outports)) {
                competitionState = State.Competition;
                initializeMinimumBitwidthSearch();
            }
        } else if (competitionState == State.Competition) {
            if (findMinimumBitwidth(outports, !stable)) competitionState = State.Finalize;
        } else {
            done = true;
            millis = System.currentTimeMillis() - millis;
            competitionState = State.FindUniformWidth;
        }
        previousValues.clear();
        resetIterations();
        return !done;
    }

    public long getDuration() {
        return millis;
    }

    public boolean findMinimumBitwidth(List<TokenCollector> outputs, boolean autofail) throws IllegalActionException {
        boolean passed = !autofail;
        boolean moveOn = true;
        double systemError = 0.0;
        for (TokenCollector tc : outputs) {
            passed &= tc.metErrorConstraint();
            systemError += tc.getError().doubleValue();
        }
        if (passed) {
            moveOn = nodePassed(systemError);
        } else {
            nodeFailed(systemError);
        }
        int precision;
        if (moveOn) {
            do {
                if (testList.empty()) {
                    boolean done = !competitionResults();
                    for (BitwidthActor a : quantizers) {
                        a.loadPrecision();
                    }
                    if (done) {
                        return true;
                    }
                }
                currentNode = testList.pop();
                precision = currentNode.getPrecision();
                setNewNode(precision);
                director.resetStateElements();
            } while ((precision) == 0);
        }
        return false;
    }

    protected QuantizerCost addNodeCost(double systemCost) {
        QuantizerCost q = new QuantizerCost(currentNode, systemCost, currentNode.getPrecision());
        costs.add(q);
        return q;
    }

    private Log logger = LogFactory.getLog(this.getClass());

    /**
     * Whether or not the competition needs to find the UBW first
     */
    public abstract boolean runUniformBWSearch();

    /**
     * Given the current Uniform Bit-width, what to set all the nodes to.
     * @param currentWidth
     * @return
     */
    public abstract int setInitialWidth(int currentWidth);

    /**
	 * Returns true if ready to move on to next node, false to continue working with this node.
	 */
    protected abstract boolean nodePassed(double systemError) throws IllegalActionException;

    /**
     * What to do if a node's current width made the system fail the error constraints.
     * @throws IllegalActionException
     */
    protected abstract void nodeFailed(double systemError) throws IllegalActionException;

    /**
     * What to do if a node's current width made the system meet the error constraints.
     * @throws IllegalActionException
     */
    protected abstract void setNewNode(int currentPrecision) throws IllegalActionException;
}
