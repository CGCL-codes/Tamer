package gov.nasa.jpf;

/**
 * Interface for a Strategy class to encapsulate the state space search algorithm.
 * This is one of the two major JPF abstractions (the other one being the VM)
 */
public interface Search {

    public static final String DEPTH_CONSTRAINT = "DEPTH";

    public static final String QUEUE_CONSTRAINT = "QUEUE";

    public static final String SIZE_CONSTRAINT = "SIZE";

    /**
   * add a listener for state changes
   */
    void addListener(SearchListener listener);

    void addProperty(Property property);

    void removeProperty(Property property);

    /**
   * return the search results (property violations)
   */
    ErrorList getErrors();

    /**
   * do we have a next state (after advancing it)
   */
    boolean hasNextState();

    /**
   * has this state already been visited (only useful after advancing it)
   */
    boolean isNewState();

    boolean isEndState();

    /**
   * get current search depth
   */
    int getSearchDepth();

    /**
   * get the Transition that brought us into this state
   */
    Transition getTransition();

    /**
   * get a unique numeric id for this state
   */
    int getStateNumber();

    /**
   * answer the last hit search constraint code
   */
    String getSearchConstraint();

    /**
   * request a single backtrack step as the next transition
   */
    boolean requestBacktrack();

    /**
   * does the search strategy allow listeners to do explicit backtracking
   */
    boolean supportsBacktrack();

    /**
   * does this search strategy support state restoration
   */
    boolean supportsRestoreState();

    /**
   * start the search process - this is the main driver for the VirtualMachine
   */
    void search();

    /**
   * Get the VM that is used by the search
   * @return
   */
    VM getVM();
}
