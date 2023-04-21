package org.jrcaf.flow.activity;

import java.util.Map;
import org.jrcaf.flow.action.IActionCaller;
import org.jrcaf.flow.action.TargetMapping;

/**
 * Interface for activities.
 */
public interface IActivity extends IActionCaller {

    /** The model name of the name of the selection. */
    public static final String SELECTION_DISPLAY_NAME = "SELECTION_DISPLAY_NAME";

    /** The model name of the image of the selection */
    public static final String SELECTION_IMAGE_NAME = "SELECTION_IMAGE_NAME";

    /**
    * Forwards to the next action defined by the action flow and selected by 
    * the passed TargetMapping.
    * Maps the passed results to the input parameters of the action to call and 
    * passes them to the that action. 
    * @param aResults The result parameters.
    * @param aMapping The TargetMapping.
    * @param aActionCaller The caller of the action.
    */
    public void nextAction(Map<String, Object> aResults, TargetMapping aMapping, IActionCaller aActionCaller);

    /**
    * Finishes the action by forwarding to the next action defined by the action 
    * flow and selected by the passed TargetMapping.
    * Maps the passed results to the input parameters of the action to call and 
    * passes them to the that action. 
    * @param aResults The result.
    * @param aMapping The TargetMapping.
    * @param aCallingAction The calling action.
    */
    public void finishAction(Map<String, Object> aResults, TargetMapping aMapping, IActionCaller aCallingAction);

    /**
    * Calls a action defined by the action flow and selected by the passed TargetMapping.
    * Maps the passed results to the input parameters of the action to call and 
    * passes them to the that action. 
    * @param aResults The result parameters.
    * @param aMapping The TargetMapping.
    * @param aTarget The name of the target called defined by the caller.
    * @param aCallingAction The calling action.
    */
    public void callAction(Map<String, Object> aResults, TargetMapping aMapping, String aTarget, IActionCaller aCallingAction);

    /**
    * Calls an action defined by the action flow and selected by the passed TargetMapping.
    * Waits until the action returns.
    * Maps the passed results to the input parameters of the action to call and 
    * passes them to the that action. 
    * @param aResults The result parameters.
    * @param aMapping The TargetMapping.
    * @param aTarget The name of the target called defined by the caller.
    * @param aCallingAction The calling action.
    * @return Returns the results of the called action.
    */
    public Map<String, Object> callActionAndWait(Map<String, Object> aResults, TargetMapping aMapping, String aTarget, IActionCaller aCallingAction);

    /**
    * Forks an action defined by the action flow and selected by the passed TargetMapping.
    * Maps the passed results to the input parameters of the action to call and 
    * passes them to the that action. 
    * @param aResults The result parameters.
    * @param aMapping The TargetMapping.
    * @param aCallingAction The calling action.
    */
    public void forkAction(Map<String, Object> aResults, TargetMapping aMapping, IActionCaller aCallingAction);
}
