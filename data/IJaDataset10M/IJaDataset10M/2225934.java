package jung;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeIndexFunction;

public interface EdgeOffsetFunction<V, E> extends EdgeIndexFunction<V, E> {

    float getOffset(Graph<V, E> graph, E e);

    /**
	 * No Op class
	 * 
	 * @param <V>
	 *            vertex
	 * @param <E>
	 *            edge
	 */
    class NOOP<V, E> implements EdgeOffsetFunction<V, E> {

        @Override
        public float getOffset(final Graph<V, E> graph, final E e) {
            return 0.f;
        }

        @Override
        public int getIndex(final Graph<V, E> graph, final E e) {
            return 0;
        }

        @Override
        public void reset(final Graph<V, E> g, final E edge) {
        }

        @Override
        public void reset() {
        }
    }
}
