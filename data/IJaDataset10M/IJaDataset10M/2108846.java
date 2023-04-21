package alice.respect;

import alice.logictuple.*;

/**
 * Tuple Centre Interface
 */
public interface ITupleCentre {

    /**
     * Inserts a tuple in the tuple centre
     * @param t logic tuple
     * @throws InvalidLogicTupleException if the logic tuple is not correct
     */
    void out(LogicTuple t) throws InvalidLogicTupleException;

    /**
     * Removes a tuple from the tuple centre, matching the specified
     * template.
     * 
     * Current thread is blocked until a tuple matching the template
     * is found. The template and the tuple are unified.  
     *  
     * @param t template
     * @return tuple matching the request, unified with the template
     * @throws InvalidLogicTupleException if the logic tuple is not correct
     */
    LogicTuple in(LogicTuple t) throws InvalidLogicTupleException;

    /**
	 * Reads a tuple from the tuple centre, matching the specified
	 * template.
	 * 
	 * Current thread is blocked until a tuple matching the template
	 * is found. The template and the tuple are unified. The tuple 
	 * is not removed from the tuple centre. 
	 *  
	 * @param t template
	 * @return tuple matching the request, unified with the template
     * @throws InvalidLogicTupleException if the logic tuple is not correct
	 */
    LogicTuple rd(LogicTuple t) throws InvalidLogicTupleException;

    /**
	 * No-blocking in: removes a tuple eventually present inside the tuple centre, 
	 * matching the specified template. 
	 * 
	 * If the tuple is not found, a null value is returned   
	 * (the primitive does not block).
	 * 
	 * @param t template
	 * @return tuple matching the request, unified with the template
	 *         or null if the tuple is not found
	 * @throws InvalidLogicTupleException if the logic tuple is not correct
	 */
    LogicTuple inp(LogicTuple t) throws InvalidLogicTupleException;

    /**
	 * No-blocking rd: reads a tuple eventually present inside the tuple centre, 
	 * matching the specified template. 
	 * 
	 * If the tuple is not found, a null value is returned   
	 * (the primitive does not block).
	 * 
	 * @param t template
	 * @return tuple matching the request, unified with the template
	 *         or null if the tuple is not found
	 * @throws InvalidLogicTupleException if the logic tuple is not correct
	 */
    LogicTuple rdp(LogicTuple t) throws InvalidLogicTupleException;

    /**
     * Sets the behaviour of the tuple centre.
     * 
     * The ReSpecT logic language is used to specify the behaviour
     * specification.   
     * 
     * @param spec behaviour specificatio encoded in the ReSpecT language
     * @throws InvalidSpecificationException if a not valid specification is provided
     */
    void setSpec(String spec) throws InvalidSpecificationException;

    /**
     * Gets the behaviour specification of the tuple centre.
     * 
     * @return the behaviour specification encoded in the ReSpecT language.
     */
    String getSpec();

    /**
     * Gets the identifier of the tuple centre
     * 
     * @return the identifier
     */
    TupleCentreId getId();
}
