package org.hswgt.teachingbox.usecases.poleswingup;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.agent.Agent;
import org.hswgt.teachingbox.core.rl.env.RewardDelayedEnvironment;
import org.hswgt.teachingbox.core.rl.env.Environment;
import org.hswgt.teachingbox.core.rl.env.PoleSwingupEnvironment;
import org.hswgt.teachingbox.core.rl.etrace.ETraceType;
import org.hswgt.teachingbox.core.rl.experiment.EpisodicSaver;
import org.hswgt.teachingbox.core.rl.experiment.Experiment;
import org.hswgt.teachingbox.core.rl.learner.DelayedLearner;
import org.hswgt.teachingbox.core.rl.learner.GradientDescentQLearner;
import org.hswgt.teachingbox.core.rl.learner.Learner;
import org.hswgt.teachingbox.core.rl.network.Network;
import org.hswgt.teachingbox.core.rl.plot.Plotter;
import org.hswgt.teachingbox.core.rl.plot.RuntimePlotter;
import org.hswgt.teachingbox.core.rl.plot.ValueFunctionSurfacePlotter;
import org.hswgt.teachingbox.core.rl.policy.EpsilonGreedyPolicy;
import org.hswgt.teachingbox.core.rl.policy.GreedyPolicy;
import org.hswgt.teachingbox.core.rl.network.adaption.NoNodeNearby;
import org.hswgt.teachingbox.core.rl.network.adaption.TdErrorExceedsThreshold;
import org.hswgt.teachingbox.core.rl.network.rbf.RadialBasisFunction;
import org.hswgt.teachingbox.core.rl.network.rbf.adaption.RBFDistanceCalculator;
import org.hswgt.teachingbox.core.rl.plot.networkviz.NetworkVisualizer;
import org.hswgt.teachingbox.core.rl.plot.networkviz.RBF2DVisualizer;
import org.hswgt.teachingbox.core.rl.valuefunctions.QFeatureFunction;
import org.hswgt.teachingbox.core.rl.valuefunctions.ValueFunctionEQ;
import org.hswgt.teachingbox.core.rl.viz.CartPole2dWindow;
import org.hswgt.teachingbox.core.rl.viz.PoleSwingUp2dWindow;
import org.hswgt.teachingbox.core.rl.viz.PoleSwingUpVisualization;
import org.hswgt.teachingbox.core.rl.viz.Visualization.Mode;

/**
 * <pre>
 * Environment: Pole swing-up
 * Algorithm: Q-Learning
 * Approximation: Adaptive RBFNetwork
 * </pre>
 */
public class DelayedQLearningErrorExceedsThresholdNRBF {

    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.DEBUG);
        final double[] sigma = new double[] { (PoleSwingupEnvironment.MAX_POS - PoleSwingupEnvironment.MIN_POS) / 15, (PoleSwingupEnvironment.MAX_VEL - PoleSwingupEnvironment.MIN_VEL) / 15 };
        TdErrorExceedsThreshold adaptionRule = new TdErrorExceedsThreshold(new RadialBasisFunction(sigma, sigma), new RBFDistanceCalculator());
        Network net = new Network(adaptionRule);
        net.setIsNormalized(true);
        QFeatureFunction Q = new QFeatureFunction(net, PoleSwingupEnvironment.ACTION_SET);
        Environment env = new PoleSwingupEnvironment();
        EpsilonGreedyPolicy pi = new EpsilonGreedyPolicy(Q, PoleSwingupEnvironment.ACTION_SET, 0.00);
        Agent agent = new Agent(pi);
        final int MAX_EPISODES = 1000;
        final int MAX_STEPS = 1000;
        final double alpha = 0.1;
        final double gamma = 0.9;
        final double lambda = 0.9;
        GradientDescentQLearner learner = new GradientDescentQLearner(Q, net, PoleSwingupEnvironment.ACTION_SET);
        learner.setAlpha(alpha);
        learner.setGamma(gamma);
        learner.setLambda(lambda);
        learner.addObserver(adaptionRule);
        int delay = 15;
        Learner delayedLearner = new DelayedLearner(learner, delay);
        agent.addObserver(delayedLearner);
        RewardDelayedEnvironment delayedEnv = new RewardDelayedEnvironment(env, delay, agent, delayedLearner);
        Experiment experiment = new Experiment(agent, delayedEnv, MAX_EPISODES, MAX_STEPS);
        double[] xrange = { -Math.PI, 0.2, Math.PI };
        double[] yrange = { -2 * Math.PI, 0.2, 2 * Math.PI };
        Plotter Vplotter = new ValueFunctionSurfacePlotter(Q, xrange, yrange, "Pole swing-up task");
        Vplotter.getPlotter().getAxis("x").setLabel("theta");
        Vplotter.getPlotter().getAxis("y").setLabel("thetad");
        Vplotter.getPlotter().getAxis("z").setLabel("max_Q");
        Vplotter = new RuntimePlotter(Vplotter, RuntimePlotter.Mode.EPISODE, 30, net);
        experiment.addObserver((RuntimePlotter) Vplotter);
        PoleSwingUpVisualization viz = new PoleSwingUpVisualization(agent, delayedLearner, Mode.EPISODE, 100, MAX_STEPS);
        viz.setEnv(new RewardDelayedEnvironment(env, delay, agent, delayedLearner));
        experiment.addObserver(viz);
        experiment.addObserver(adaptionRule);
        experiment.run();
    }
}
