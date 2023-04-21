package sketch.generator;

/**
 * This inteface is designed for composite generators, that is,
 * a generator having linked-sub generators
 * */
public interface IIterationLink {

    /**
	 * To see if there is more node (potentially from its children
	 * generators) to iterate
	 * */
    boolean hasNext();

    /**
	 * Move the iterator to the next node (potentially from its
	 * children generators)
	 * */
    void next();

    /**
	 * Reset the current node (and nodes from its children generator)
	 * */
    void reset();
}
