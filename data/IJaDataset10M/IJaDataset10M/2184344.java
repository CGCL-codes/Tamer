package com.itmusings.stm;

/**
 * 
 * An implementation of a state transition machine. This would allow the caller to proceed from one state to the
 * other.  The implementation is designed to support two types of states:
 * <li> Auto state  - a state whose transitions can be computed automatically by calling a method in a class.
 * <li> Manual state - a state that requires some triggering event (ex: user input in a form, user responding to an email etc.) to
 * progress further. 
 * <p>The STM also supports the notion of an initial state  
 * <p>
 *  @author Raja Shankar Kolluru
 */
public interface STM {

    public static final String SUCCESS = "__SUCCESS__";

    public static final String FAILURE = "__FAILURE__";

    /**
	 * This method allows the user to specify no starting state and feed it to the STM. This would start with the default flow and the 
	 * initial state in it. It would then recursively process thru all
	 * "auto states" and finally returns the new state to which it transitioned. 
	 * <p>The new state is either an end state or a manual state (i.e. a state that requires a user triggered action to determine the transitioning event)
	 * <p>This proceed() method supports the following kinds of behavior:
	 * <li>If the state passed is null, then the initial state of the default flow is chosen.
	 * <li> 
	 * @param flowContext - the context for the flow.
	 * @return  the transitioned state. 
	 * @throws Exception
	 */
    public abstract State proceed(Object flowContext) throws Exception;

    /**
	 * This method allows the user to specify a starting state and feed it to the STM. This would recursively process thru all
	 * "auto states" and finally returns the new state to which it transitioned. 
	 * <p>The new state is either an end state or a manual state (i.e. a state that requires a user triggered action to determine the transitioning event)
	 * <p>This proceed() method supports the following kinds of behavior:
	 * <li>If the state passed is null, then the initial state of the default flow is chosen.
	 * <li> 
	 * @param startingState - the specified state in the specified flow
	 * @param flowContext - the context for the flow.
	 * @return  the transitioned state. 
	 * @throws Exception
	 */
    public abstract State proceed(State startingState, Object flowContext) throws Exception;

    /**
	 * This method allows the user to specify a starting state and feed it to the STM. This would recursively process thru all
	 * "action states" and finally returns the new state to which it transitioned. 
	 * <p>The new state is either an end state or a view state (i.e. a state that requires a user triggered action to determine the transitioning event)
	 * <p>This proceed() method uses the specified state in the specified flow.
	 * @param startingState - the state you start the flow in
	 * @param startingEventId - the event that needs to start the flow. This would have been most probably obtained by the user.
	 * @param flowContext - the context for the flow.
	 * @return  the transitioned state. 
	 * @throws Exception
	 */
    public abstract State proceed(State startingState, String startingEventId, Object flowContext) throws Exception;

    /**
	 * Initialization method. The flow needs to be read by a flow configurator which needs to be set into the STM.
	 * @param flowConfigurator
	 */
    public abstract void setFlowConfigurator(FlowConfigurator flowConfigurator);
}
