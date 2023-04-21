package ch.idsia.scenarios.oldscenarios;

import ch.idsia.agents.Agent;
import ch.idsia.agents.AgentsPool;
import ch.idsia.agents.controllers.TimingAgent;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.simulation.SimulationOptions;
import ch.idsia.tools.MarioAIOptions;
import ch.idsia.utils.statistics.StatisticalSummary;

public class Stats {

    static final int numberOfTrials = 100;

    public static void main(String[] args) {
        Agent controller = AgentsPool.loadAgent(args[0], false);
        final int startingSeed = Integer.parseInt(args[1]);
        doStats(controller, startingSeed);
    }

    public static void doStats(Agent agent, int startingSeed) {
        TimingAgent controller = new TimingAgent(agent);
        SimulationOptions options = new MarioAIOptions(new String[0]);
        options.setVisualization(false);
        options.setFPS(GlobalOptions.MaxFPS);
        System.out.println("Testing controller " + controller + " with starting seed " + startingSeed);
        double competitionScore = 0;
        competitionScore += testConfig(controller, options, startingSeed, 0, true);
        competitionScore += testConfig(controller, options, startingSeed, 0, false);
        competitionScore += testConfig(controller, options, startingSeed, 3, true);
        competitionScore += testConfig(controller, options, startingSeed, 3, false);
        competitionScore += testConfig(controller, options, startingSeed, 5, true);
        competitionScore += testConfig(controller, options, startingSeed, 5, false);
        competitionScore += testConfig(controller, options, startingSeed, 10, true);
        competitionScore += testConfig(controller, options, startingSeed, 10, false);
        System.out.println("Stats sum: " + competitionScore);
    }

    public static double testConfig(TimingAgent controller, SimulationOptions options, int seed, int level, boolean paused) {
        options.setLevelDifficulty(level);
        StatisticalSummary ss = test(controller, options, seed);
        System.out.printf("Level %d %s %.4f (%.4f) (min %.4f max %.4f) (avg time %.4f)\n", level, paused ? "paused" : "unpaused", ss.mean(), ss.sd(), ss.min(), ss.max(), controller.averageTimeTaken());
        return ss.mean();
    }

    public static StatisticalSummary test(Agent controller, SimulationOptions options, int seed) {
        StatisticalSummary ss = new StatisticalSummary();
        for (int i = 0; i < numberOfTrials; i++) {
            options.setLevelRandSeed(seed + i);
            controller.reset();
            options.setAgent(controller);
        }
        return ss;
    }
}
