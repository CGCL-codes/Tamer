package de.grogra.xl.query;

/**
 * A <code>Producer</code> is used within right hand sides of XL rules
 * in order to construct
 * the replacement for the match of the left hand side. 
 * 
 * @author Ole Kniemeyer
 */
public interface Producer {

    /**
	 * Indicates the XL rule arrow <code>==&gt;</code>.
	 */
    int SIMPLE_ARROW = 0;

    /**
	 * Indicates the XL rule arrow <code>==&gt;&gt;</code>.
	 */
    int DOUBLE_ARROW = 1;

    /**
	 * Indicates the XL rule arrow <code>::&gt;</code>.
	 */
    int EXECUTION_ARROW = 2;

    /**
	 * This method is invoked by the XL run-time system in order to notify the
	 * producer about the beginning of a right-hand side (i.e., a match for the left-hand side
	 * has been found, and the right-hand side is executed).
	 * 
	 * @param arrow the type of rule arrow, one of {@link #SIMPLE_ARROW},
	 * {@link #DOUBLE_ARROW}, {@link #EXECUTION_ARROW}
	 * 
	 * @return <code>true</code> if the right-hand side shall be executed,
	 * <code>false</code> if its execution shall be skipped
	 */
    boolean producer$beginExecution(int arrow);

    /**
	 * This method is invoked by the XL run-time system in order to notify the
	 * producer about the end of the execution of a right-hand side.
	 * 
	 * @param executed return value of invocation of
	 * {@link #producer$beginExecution}
	 */
    void producer$endExecution(boolean executed);

    void producer$visitEdge(EdgePattern pattern);

    /**
	 * Returns the graph for which this producer constructs the
	 * right-hand side structur.
	 * 
	 * @return the graph on which this producer operates
	 */
    Graph producer$getGraph();
}
