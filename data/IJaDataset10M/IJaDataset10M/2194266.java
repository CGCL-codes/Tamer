package org.hswgt.teachingbox.usecases.lwlr.poleswingup;

import java.util.List;
import org.hswgt.teachingbox.datastructures.TransitionProbability;
import org.hswgt.teachingbox.env.Action;
import org.hswgt.teachingbox.env.Environment;
import org.hswgt.teachingbox.env.State;
import org.hswgt.teachingbox.env.viz.PoleSwingUp2dWindow;
import org.hswgt.teachingbox.pbd.lwlr.LWLR_TransitionFunction;
import cern.jet.random.Uniform;

/**
 * This example shows, how the LWLR_TransitionFunction (which again is a 
 * LWLR_VectorPrediction) can be used to build a Teachingbox compatible
 * Environment, using Locally Weighted Linear Regression for simulation. 
 * 
 * @author Richard Cubek
 * 
 */
public class LWLR_PoleSwingUpModel implements Environment {

    private static final long serialVersionUID = -2236854640701443258L;

    protected State state;

    protected double punish = 0;

    protected boolean terminal = false;

    protected LWLR_TransitionFunction tf = new LWLR_TransitionFunction();

    /**
	 * Constructor.
	 * @throws Exception 
	 */
    public LWLR_PoleSwingUpModel() throws Exception {
        tf.addDatasetFile(CollectTrainingData.DATA_FILE_SIN);
        tf.addDatasetFile(CollectTrainingData.DATA_FILE_COS);
        tf.addDatasetFile(CollectTrainingData.DATA_FILE_OMEGA);
        tf.setQueryVectorMapper(new QueryVectorMapper());
        tf.setOutgoingVectorMapper(new PredictionOutputVectorMapper());
    }

    public double doAction(Action a) {
        List<TransitionProbability> list = tf.getTransitionProbabilities(state, a);
        state = list.get(0).getNextState();
        return 0;
    }

    public State getState() {
        return state;
    }

    @Override
    public void init(State s) {
        state = s;
        terminal = false;
        punish = 0;
    }

    @Override
    public void initRandom() {
        init(new State(new double[] { Uniform.staticNextDoubleFromTo(-Math.PI, Math.PI), 0 }));
    }

    public boolean isTerminalState() {
        if (terminal == true && punish <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Test the model simply by playing with it (subjective test).
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        LWLR_PoleSwingUpModel env = new LWLR_PoleSwingUpModel();
        env.init(new State(new double[] { Math.PI, 0 }));
        PoleSwingUp2dWindow window = new PoleSwingUp2dWindow("pole-swing-up");
        State state = env.getState();
        Action action;
        System.out.println("use arrow keys to apply forces");
        while (true) {
            window.setAngle(state.get(0));
            action = window.getAction();
            env.doAction(action);
            state = env.getState();
        }
    }
}
