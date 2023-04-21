package ao.general.impl.khun;

import ao.general.FiniteZeroSumExtensiveGame;
import ao.general.GameState;
import ao.general.NashEquilibriumStrategySolver;
import ao.general.StrategyProfile;
import ao.general.impl.CounterfactualRegretMinimizer;
import ao.general.impl.GameStateTree;
import ao.general.impl.StrategyProfileImpl;
import java.util.Arrays;

/**
 * Date: 7/10/11
 * Time: 11:27 PM
 */
public class KuhnSolverRunner {

    public static void main(String[] args) {
        FiniteZeroSumExtensiveGame game = new KuhnPoker();
        NashEquilibriumStrategySolver solver = new CounterfactualRegretMinimizer();
        GameStateTree stateTree = new GameStateTree(game.initialState());
        StrategyProfile strategyProfile = new StrategyProfileImpl();
        for (int i = 0; i < 10000; i++) {
            solver.runSimulation(game.bothPlayerJointBucketSequenceSample(), stateTree, strategyProfile);
        }
    }
}
