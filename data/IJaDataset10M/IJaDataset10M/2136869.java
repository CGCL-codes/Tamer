package ao.chess.v2.engine.trans;

import ao.chess.v2.engine.mcts.MctsHeuristic;
import ao.chess.v2.engine.mcts.MctsNode;
import ao.chess.v2.engine.mcts.MctsRollout;
import ao.chess.v2.engine.mcts.MctsSelector;
import ao.chess.v2.engine.mcts.rollout.MctsRolloutImpl;
import ao.chess.v2.engine.mcts.heuristic.MctsFpuHeuristic;
import ao.chess.v2.engine.mcts.message.MctsAction;
import ao.chess.v2.engine.mcts.node.MctsNodeImpl;
import ao.chess.v2.engine.mcts.value.Ucb1TunedValue;
import ao.chess.v2.state.Move;
import ao.chess.v2.state.State;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

/**
 * User: aostrovsky
 * Date: 12-Oct-2009
 * Time: 10:56:16 AM
 */
public class TransNode {

    private static final MctsHeuristic HEURISTIC = new MctsFpuHeuristic();

    private static final MctsRollout MC_TRIAL = new MctsRolloutImpl(false);

    private static final MctsSelector<Ucb1TunedValue> SELECTOR = new Ucb1TunedValue.MeanSelector();

    private Ucb1TunedValue value;

    private long stateHash;

    private int[] acts;

    private TransNode[] kids;

    public TransNode(State state) {
        value = new Ucb1TunedValue();
        stateHash = state.longHashCode();
        acts = null;
        kids = null;
    }

    public boolean isUnvisited() {
        return acts == null;
    }

    public void runTrajectory(State fromProtoState, Long2ObjectMap<TransNode> transpositionTable) {
        State cursor = fromProtoState.prototype();
        ObjectLinkedOpenHashSet<TransNode> path = new ObjectLinkedOpenHashSet<TransNode>();
        TransNode lastInPath = this;
        path.add(lastInPath);
        while (!lastInPath.isUnvisited()) {
            TransNode selectedChild = lastInPath.descendByBandit(cursor, HEURISTIC, path, transpositionTable);
            if (selectedChild == null) break;
            lastInPath = selectedChild;
            path.add(selectedChild);
        }
        if (lastInPath.kids == null) {
            lastInPath.initiateKids(cursor);
        }
        backupMcValue(path, MC_TRIAL.monteCarloPlayout(cursor, HEURISTIC));
    }

    private TransNode descendByBandit(State cursor, MctsHeuristic heuristic, ObjectLinkedOpenHashSet<TransNode> path, Long2ObjectMap<TransNode> transTable) {
        if (kids.length == 0) return null;
        double greatestValue = Double.NEGATIVE_INFINITY;
        int greatestValueIndex = -1;
        for (int i = 0; i < kids.length; i++) {
            TransNode kid = kids[i];
            double banditValue;
            if (kid == null || kid.isUnvisited()) {
                banditValue = heuristic.firstPlayUrgency(acts[i]);
            } else if (path.contains(kid)) {
                continue;
            } else {
                banditValue = kid.value.confidenceBound(null, value);
            }
            if (banditValue > greatestValue) {
                greatestValue = banditValue;
                greatestValueIndex = i;
            }
        }
        if (greatestValueIndex == -1) return null;
        Move.apply(acts[greatestValueIndex], cursor);
        if (kids[greatestValueIndex] == null) {
            TransNode newNode = new TransNode(cursor);
            TransNode existing = transTable.get(newNode.stateHash);
            if (existing == null) {
                transTable.put(newNode.stateHash, newNode);
                kids[greatestValueIndex] = newNode;
            } else {
                kids[greatestValueIndex] = existing;
            }
        }
        return kids[greatestValueIndex];
    }

    @SuppressWarnings("unchecked")
    private void initiateKids(State fromState) {
        acts = fromState.legalMoves();
        if (acts == null) {
            kids = new TransNode[0];
        } else {
            kids = new TransNode[acts.length];
        }
    }

    private void backupMcValue(ObjectLinkedOpenHashSet<TransNode> path, double leafWinRate) {
        double reward = 1.0 - leafWinRate;
        for (ObjectBidirectionalIterator<TransNode> i = path.iterator(path.last()); i.hasPrevious(); ) {
            i.previous().value.update(reward);
            reward = 1.0 - reward;
        }
    }

    public TransAction bestMove() {
        if (kids == null || kids.length == 0) return null;
        int bestAct = -1;
        TransNode bestKid = null;
        for (int i = 0, kidsLength = kids.length; i < kidsLength; i++) {
            TransNode kid = kids[i];
            if (kid != null && (bestKid == null || SELECTOR.compare(bestKid.value, kid.value) < 0)) {
                bestKid = kid;
                bestAct = acts[i];
            }
        }
        return new TransAction(bestAct, bestKid);
    }

    public TransNode childMatching(int action) {
        if (acts == null) return null;
        for (int i = 0, actsLength = acts.length; i < actsLength; i++) {
            int act = acts[i];
            if (act == action) {
                return kids[i];
            }
        }
        return null;
    }

    public void addStates(LongCollection to) {
        if (to.contains(stateHash)) return;
        to.add(stateHash);
        if (kids == null) return;
        for (TransNode kid : kids) {
            if (kid == null) continue;
            kid.addStates(to);
        }
    }

    @Override
    public String toString() {
        return uniqueSize() + " | " + value.toString();
    }

    private int uniqueSize() {
        LongSet states = new LongOpenHashSet();
        addStates(states);
        return states.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransNode transNode = (TransNode) o;
        return stateHash == transNode.stateHash;
    }

    @Override
    public int hashCode() {
        return (int) (stateHash ^ (stateHash >>> 32));
    }
}
