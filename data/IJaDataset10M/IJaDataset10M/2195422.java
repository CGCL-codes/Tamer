package org.hswgt.teachingbox.usecases.simkorsel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.agent.Agent;
import org.hswgt.teachingbox.core.rl.env.SimKorselEnv;
import org.hswgt.teachingbox.core.rl.env.MountainCarEnv;
import org.hswgt.teachingbox.core.rl.experiment.EpisodicSaver;
import org.hswgt.teachingbox.core.rl.experiment.Experiment;
import org.hswgt.teachingbox.core.rl.learner.GradientDescentQLearner;
import org.hswgt.teachingbox.core.rl.network.Network;
import org.hswgt.teachingbox.core.rl.network.cmacs.TileCodingFactory;
import org.hswgt.teachingbox.core.rl.plot.Plotter;
import org.hswgt.teachingbox.core.rl.plot.PolicyPlotter3D;
import org.hswgt.teachingbox.core.rl.plot.QFunctionPlotter3D;
import org.hswgt.teachingbox.core.rl.plot.RuntimePlotter;
import org.hswgt.teachingbox.core.rl.plot.ValueFunctionSurfacePlotter;
import org.hswgt.teachingbox.core.rl.plot.RuntimePlotter.Mode;
import org.hswgt.teachingbox.core.rl.plot.networkviz.NetworkVisualizer;
import org.hswgt.teachingbox.core.rl.plot.networkviz.Tile2DVisualizer;
import org.hswgt.teachingbox.core.rl.policy.GreedyPolicy;
import org.hswgt.teachingbox.core.rl.tools.ObjectSerializer;
import org.hswgt.teachingbox.core.rl.valuefunctions.QFeatureFunction;
import org.hswgt.teachingbox.core.rl.valuefunctions.ValueFunctionEQ;

/**
 * <pre>
 * Environment: Korsel
 * Algorithm: Q-Learning
 * Approximation: Adaptive RBFNetwork
 * </pre>
 */
public class QLearningTileCoding {

    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.DEBUG);
        int nTilings = 5;
        SimKorselEnv env = new SimKorselEnv();
        double[][] config = new double[][] { { env.MIN_X, env.MAX_X, 10 }, { env.MIN_Y, env.MAX_Y, 10 }, { env.MIN_ANGLE, env.MAX_ANGLE, 10 } };
        Network net = new Network();
        net.add(TileCodingFactory.createTilings(config, nTilings));
        NetworkVisualizer netViz = new NetworkVisualizer(net);
        netViz.setRanges(config);
        netViz.setVizualizer(new Tile2DVisualizer(netViz.getPlotter()));
        netViz.plot();
        QFeatureFunction Q = new QFeatureFunction(net, SimKorselEnv.ACTION_SET);
        GreedyPolicy pi = new GreedyPolicy(Q, SimKorselEnv.ACTION_SET);
        Agent agent = new Agent(pi);
        final int MAX_EPISODES = 100;
        final int MAX_STEPS = 100;
        final double alpha = 0.4;
        final double gamma = 1;
        final double lambda = 0.9;
        Experiment experiment = new Experiment(agent, env, MAX_EPISODES, MAX_STEPS);
        GradientDescentQLearner learner = new GradientDescentQLearner(Q, net, SimKorselEnv.ACTION_SET);
        learner.setAlpha(alpha);
        learner.setGamma(gamma);
        learner.setLambda(lambda);
        agent.addObserver(learner);
        ValueFunctionEQ V = new ValueFunctionEQ(Q);
        V.costfunction = true;
        Plotter Vplotter = new ValueFunctionSurfacePlotter(V, "[0:1:1024]", "[0:1:768]", "MountainCar");
        Vplotter = new RuntimePlotter(Vplotter, RuntimePlotter.Mode.EPISODE, 10, net);
        experiment.addObserver((RuntimePlotter) Vplotter);
        experiment.run();
        ObjectSerializer.save("MC_Q_tilecoding.ser", Q);
    }
}
