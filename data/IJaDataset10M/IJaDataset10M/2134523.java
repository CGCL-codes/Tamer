package ao.chess.v1.ai.heavy;

import ao.chess.v1.ai.AlexoChess;
import ao.chess.v1.ai.FullOutcome;
import ao.chess.v1.ai.RandomBot;
import ao.chess.v1.ai.Reward;
import ao.chess.v1.model.Board;
import java.util.LinkedList;

/**
 *
 */
public class HeavyNode {

    private int visits;

    private Reward rewardSum;

    private int act;

    private Board state;

    private HeavyNode child;

    private HeavyNode sibling;

    private HeavyNode(int move, Board onState) {
        act = move;
        state = onState.prototype();
        if (act != 0) {
            state.makeMove(act);
        }
        visits = 0;
        rewardSum = new Reward();
    }

    public HeavyNode(Board state) {
        this(0, state);
    }

    public int size() {
        int size = 0;
        for (HeavyNode nextChild = child; nextChild != null; nextChild = nextChild.sibling) {
            size += nextChild.size();
        }
        return size + 1;
    }

    public int optimize() {
        HeavyNode best = optimalChild();
        return best.act;
    }

    private HeavyNode optimalChild() {
        HeavyNode optimal = null;
        double optimalReward = Long.MIN_VALUE;
        for (HeavyNode nextChild = child; nextChild != null; nextChild = nextChild.sibling) {
            double reward = nextChild.averageReward();
            if (reward > optimalReward) {
                optimal = nextChild;
                optimalReward = reward;
            }
        }
        return (optimal == null) ? this : optimal;
    }

    private double averageReward() {
        return rewardSum.averagedOver(visits + 1);
    }

    private boolean unvisited() {
        return visits == 0;
    }

    public void strategize() {
        LinkedList<HeavyNode> path = new LinkedList<HeavyNode>();
        path.add(this);
        while (!path.getLast().unvisited()) {
            HeavyNode selectedChild = path.getLast().descendByUCB1(visits);
            if (selectedChild == null) break;
            path.add(selectedChild);
        }
        propagateValue(path, path.getLast().monteCarloValue());
    }

    private void propagateValue(LinkedList<HeavyNode> path, Reward reward) {
        Reward maxiMax = reward.compliment();
        for (int i = path.size() - 1; i >= 0; i--) {
            HeavyNode step = path.get(i);
            step.rewardSum = step.rewardSum.plus(maxiMax);
            step.visits++;
            maxiMax = maxiMax.compliment();
        }
    }

    private HeavyNode descendByUCB1(int playsSoFar) {
        double greatestUtc = Long.MIN_VALUE;
        HeavyNode greatestChild = null;
        for (HeavyNode nextChild = child; nextChild != null; nextChild = nextChild.sibling) {
            double utcValue;
            if (nextChild.unvisited()) {
                utcValue = Integer.MAX_VALUE + 1000 * Math.random();
            } else {
                utcValue = nextChild.averageReward() + Math.sqrt(Math.log(visits) / (5 * nextChild.visits));
            }
            if (utcValue > greatestUtc) {
                greatestUtc = utcValue;
                greatestChild = nextChild;
            }
        }
        return greatestChild;
    }

    private Reward monteCarloValue() {
        int legalMoves[] = new int[256];
        FullOutcome out;
        Board rollout = state.prototype();
        do {
            int moveCount = rollout.generateMoves(false, legalMoves, 0);
            if (child == null) {
                HeavyNode prevChild = null;
                for (int i = 0; i < moveCount; i++) {
                    int move = legalMoves[i];
                    if (move == 0) continue;
                    if (child == null) {
                        child = new HeavyNode(move, rollout);
                        prevChild = child;
                    } else {
                        prevChild.sibling = new HeavyNode(move, rollout);
                        prevChild = prevChild.sibling;
                    }
                }
            }
            rollout.makeMove(RandomBot.random(legalMoves, moveCount));
        } while ((out = AlexoChess.outcome(rollout, null, 0)) != FullOutcome.UNDECIDED);
        return out.isDraw() ? new Reward(0.5) : out.scores(state.toMove == Board.WHITE_TO_MOVE) ? new Reward(1.0) : new Reward(0);
    }
}
