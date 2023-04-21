package org.hswgt.teachingbox.usecases.poleswingup;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.agent.Agent;
import org.hswgt.teachingbox.core.rl.env.PoleSwingupEnvironment;
import org.hswgt.teachingbox.core.rl.experiment.EpisodicSaver;
import org.hswgt.teachingbox.core.rl.experiment.Experiment;
import org.hswgt.teachingbox.core.rl.learner.GradientDescentQLearner;
import org.hswgt.teachingbox.core.rl.network.Network;
import org.hswgt.teachingbox.core.rl.network.adaption.NoNodeNearby;
import org.hswgt.teachingbox.core.rl.network.rbf.RadialBasisFunction;
import org.hswgt.teachingbox.core.rl.network.rbf.adaption.RBFDistanceCalculator;
import org.hswgt.teachingbox.core.rl.plot.Plotter;
import org.hswgt.teachingbox.core.rl.plot.RuntimePlotter;
import org.hswgt.teachingbox.core.rl.plot.ValueFunctionSurfacePlotter;
import org.hswgt.teachingbox.core.rl.policy.EpsilonGreedyPolicy;
import org.hswgt.teachingbox.core.rl.tools.ObjectSerializer;
import org.hswgt.teachingbox.core.rl.valuefunctions.QFeatureFunction;
import org.hswgt.teachingbox.core.rl.valuefunctions.ValueFunctionEQ;
import org.hswgt.teachingbox.core.rl.viz.PoleSwingUp2dWindow;
import org.hswgt.teachingbox.core.rl.viz.PoleSwingUpVisualization;
import org.hswgt.teachingbox.core.rl.viz.Visualization.Mode;

/**
 * <pre>
 * Environment: Pole swing-up
 * Function: Replay of a previously learned policy of the pole swing-up task 
 * 			 (Q-learning with adaptive NRBF approximation within 120 episodes).
 * 			 This use case can serve as a testbed for evaluating policy parameters.
 * Author: Michel Tokic 
 * </pre>
 */
public class Replay_ANRBF {

    public static void main(String[] args) throws Exception {
        QFeatureFunction Q = ObjectSerializer.load("data/pole-swingup/qlearning-anrbf.Q");
        PoleSwingupEnvironment env = new PoleSwingupEnvironment();
        EpsilonGreedyPolicy pi = new EpsilonGreedyPolicy(Q, PoleSwingupEnvironment.ACTION_SET, 0.00);
        Agent agent = new Agent(pi);
        final int MAX_EPISODES = 3;
        final int MAX_STEPS = 1000;
        Experiment experiment = new Experiment(agent, env, MAX_EPISODES, MAX_STEPS);
        double[] xrange = { -Math.PI, 0.1, Math.PI };
        double[] yrange = { -2 * Math.PI, 0.1, 2 * Math.PI };
        Plotter Vplotter = new ValueFunctionSurfacePlotter(Q, xrange, yrange, "Pole swing-up task");
        Vplotter.getPlotter().getAxis("x").setLabel("theta");
        Vplotter.getPlotter().getAxis("y").setLabel("thetad");
        Vplotter.getPlotter().getAxis("z").setLabel("max_Q");
        Vplotter = new RuntimePlotter(Vplotter, RuntimePlotter.Mode.EPISODE, 1);
        experiment.addObserver((RuntimePlotter) Vplotter);
        PoleSwingUp2dWindow window = new PoleSwingUp2dWindow("pole swing-up");
        experiment.addObserver(window);
        experiment.run();
    }
}
