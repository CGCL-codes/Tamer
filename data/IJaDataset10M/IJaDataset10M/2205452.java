package de.berlin.fu.inf.gameai.game.search.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.uncommons.maths.random.CellularAutomatonRNG;
import de.berlin.fu.inf.gameai.game.CopyableState;
import de.berlin.fu.inf.gameai.game.search.ContinuesSearch;
import de.berlin.fu.inf.gameai.game.search.mcts.node.Node;
import de.berlin.fu.inf.gameai.game.search.mcts.node.NodeValue;
import de.berlin.fu.inf.gameai.utils.CommonObjectFactory;
import de.berlin.fu.inf.gameai.utils.Pair;

/**
 * Implementation of MC Tree Search to sample a game, we select a path into a
 * leaf, expand the leaf, sample random actions and propagate the result back.
 * 
 * @author wabu
 * @param <S>
 * @param <T>
 * @param <R>
 */
public abstract class AbstractTreeSearch<N extends Node<N, V, S>, V extends NodeValue<S>, S extends CopyableState<S>> extends AbstractTree<N, V, S> implements ContinuesSearch<S> {

    private final Logger logger = Logger.getLogger(AbstractTreeSearch.class);

    protected final Random rnd = CommonObjectFactory.createRandom();

    public N selectPath(final N root, final List<N> path) {
        N node = root;
        for (N n = root.selectNode(); n != null; n = node.selectNode()) {
            path.add(n);
            node = n;
        }
        return node;
    }

    public void applyPath(final S state, final List<N> path) {
        for (final N n : path) {
            state.applyAction(n.getAction());
        }
    }

    public void expandLeaf(final S state, final List<N> path, final N node) {
        final int action = node.selectAction(state);
        state.applyAction(action);
        final N leaf = createNode(state, action, node);
        node.addNode(leaf);
        path.add(leaf);
    }

    public void propagateResult(final N root, final List<N> path, final S result) {
        for (int i = path.size() - 1; i >= 0; i--) {
            final N node = path.get(i);
            node.getValue().addResult(result);
            node.refresh();
        }
        root.getValue().addResult(result);
        root.refresh();
    }

    public void sample(final N root, final S state, final List<N> path) {
        final N node = selectPath(root, path);
        applyPath(state, path);
        expandLeaf(state, path, node);
        sample(state, rnd);
        propagateResult(root, path, state);
    }

    public void sample(final List<N> path) {
        final Pair<N, S> pair = getRootState();
        sample(pair.fst, pair.snd, path);
    }

    public void run() {
        logger.info("starting search ...");
        final List<N> list = new ArrayList<N>();
        while (!Thread.interrupted()) {
            sample(list);
            stats.updateStats(list);
            list.clear();
        }
    }
}
