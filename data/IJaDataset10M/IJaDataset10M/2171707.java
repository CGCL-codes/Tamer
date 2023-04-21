package jpicedt.graphic.model;

/**
 * An iterator over Element's control-point indices.
 */
public interface PointIndexIterator {

    /**
		 * @return true if the iterator has more elements
		 */
    boolean hasNext();

    /**
		 * @return the index of the next user-controlled point in the iteration
		 */
    int next();

    /**
		 * reset the state of this iterator
		 */
    void reset();
}
