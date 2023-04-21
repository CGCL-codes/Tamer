package ao.chess.v2.engine.endgame.common;

import ao.chess.v2.engine.endgame.common.index.MinPerfectHash;
import ao.chess.v2.state.Representation;
import ao.chess.v2.state.State;
import ao.util.pass.Traverser;
import java.util.Iterator;

/**
 * User: aostrovsky
 * Date: 15-Oct-2009
 * Time: 3:58:41 PM
 */
public class StateMap implements Traverser<State> {

    private final MinPerfectHash indexer;

    private final byte[][] byIndex;

    public StateMap(MinPerfectHash minHash) {
        indexer = minHash;
        byIndex = new byte[minHash.size()][];
    }

    @Override
    public void traverse(State state) {
        long staticHash = state.staticHashCode();
        State existing = get(staticHash);
        if (existing == null) {
            put(staticHash, state);
        } else if (!existing.equals(state)) {
            System.out.println("StateMap COLLISION FOUND!!!");
            System.out.println(existing);
            System.out.println("vs");
            System.out.println(state);
        }
    }

    private void put(long staticHash, State state) {
        byIndex[indexer.index(staticHash)] = Representation.packStream(state);
    }

    private State get(long staticHash) {
        return getIndexed(indexer.index(staticHash));
    }

    private State getIndexed(int index) {
        byte[] packed = byIndex[index];
        return packed == null ? null : Representation.unpackStream(packed);
    }

    public void traverse(Traverser<State> visitor) {
        for (byte[] packedState : byIndex) {
            State state = Representation.unpackStream(packedState);
            visitor.traverse(state);
        }
    }

    public boolean containsState(long staticHash) {
        return byIndex[indexer.index(staticHash)] != null;
    }

    public State stateOf(long staticHash) {
        return get(staticHash);
    }

    public Iterable<State> states(final int[] indexes) {
        return new Iterable<State>() {

            @Override
            public Iterator<State> iterator() {
                return new Iterator<State>() {

                    private int nextIndex = 0;

                    @Override
                    public boolean hasNext() {
                        return nextIndex < indexes.length;
                    }

                    @Override
                    public State next() {
                        return getIndexed(indexes[nextIndex++]);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public Iterable<State> states() {
        return new Iterable<State>() {

            @Override
            public Iterator<State> iterator() {
                return new Iterator<State>() {

                    private int nextIndex = 0;

                    @Override
                    public boolean hasNext() {
                        return nextIndex < byIndex.length;
                    }

                    @Override
                    public State next() {
                        return Representation.unpackStream(byIndex[nextIndex++]);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public int size() {
        return byIndex.length;
    }
}
