package ao.general;

import ao.general.impl.GameStateTree;
import ao.util.data.tuple.TwoTuple;

/**
 * Date: 7/9/11
 * Time: 4:39 PM
 */
public interface NashEquilibriumStrategySolver {

    void runSimulation(TwoTuple<int[], int[]> bothPlayerJointBucketSequenceSample, GameStateTree stateTree, StrategyProfile strategy);
}
