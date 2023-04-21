package org.jrcaf.action;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jrcaf.activity.IActivity;
import org.jrcaf.internal.JRCAFPlugin;
import org.jrcaf.internal.action.ActionTypeDefinition;
import org.jrcaf.internal.action.PartDefinition;
import org.jrcaf.internal.activity.Activity;

/**
 *  Defines an action.
 */
public class ActionDefinition extends ActionTypeDefinition {

    private final Map<String, TargetMapping> result2TargetMapping;

    /**
    * Creates a new action definition.
    * @param aResult2TargetMapping 
    */
    public ActionDefinition(Map<String, ActionDefinition.TargetMapping> aResult2TargetMapping) {
        super();
        result2TargetMapping = aResult2TargetMapping;
    }

    /**
    * Executes the defined action and returns imediately.
    * @param aParameters The parameters the action is called with.
    * @param aActivity The enclosing activity.
    * @param aCalledBy The calling action. 
    */
    public void execute(Map<String, Object> aParameters, IActivity aActivity, IAction aCalledBy) {
        IAction action = actionFactory.createAction(aParameters, aActivity, this, aCalledBy);
        action.execute();
    }

    /**
    * Maps the passed results to the declared parameter names. And merges these 
    * parameters with the constant parameters (passed overwrites constant).
    * @param aResults The results to be mapped.
    * @param aParameterMapping The mapping to be used. 
    * @return The maped parameters merged with the declared parameters.
    */
    public Map<String, Object> mapAndMergeParameters(Map<String, Object> aResults, Map<String, String> aParameterMapping) {
        Map<String, Object> mergedParameters = new HashMap<String, Object>(parameters);
        for (Entry<String, Object> entry : aResults.entrySet()) {
            String parameterName = aParameterMapping.get(entry.getKey());
            if (parameterName != null) mergedParameters.put(parameterName, entry.getValue());
        }
        return mergedParameters;
    }

    /**
    *  Defines a mapping from result names to named target actions.
    */
    public static class TargetMapping {

        /** The type of the call of the target action.*/
        public final CallType callType;

        /** The name of the target action. */
        public final String targetName;

        /** The Maping from result name to parameter name. */
        public final Map<String, String> parameterMapping;

        /**
       *  Creates a new target mapping.
       * @param aCallType The String constant of the type of the call.
       * @param aTargetName The name of the target action.
       * @param aParameterMapping The Maping from result name to parameter name.
       * @throws ParseException Thrown if passed aCallType string is invalid
       */
        public TargetMapping(String aCallType, String aTargetName, Map<String, String> aParameterMapping) throws ParseException {
            this(CallType.parse(aCallType), aTargetName, aParameterMapping);
        }

        /**
       *  Creates a new target mapping.
       * @param aCallType The enum constant of the type of the call.
       * @param aTargetName The name of the target action.
       * @param aParameterMapping The Maping from result name to parameter name.
       */
        public TargetMapping(CallType aCallType, String aTargetName, Map<String, String> aParameterMapping) {
            super();
            callType = aCallType;
            targetName = aTargetName;
            parameterMapping = aParameterMapping;
        }
    }

    /**
    * @param aResultName The name of the result. 
    * @return Returns the target mapping based on the passed result name.
    */
    public TargetMapping getTargetMapping(String aResultName) {
        return result2TargetMapping.get(aResultName);
    }

    /**
    * Executes the defined action and waits for the result.
    * @param aParameters The parameters the action is called with.
    * @param aActivity The enclosing activity.
    * @param aCalledBy The calling action. 
    * @return Returns the result of the executed action.
    */
    public Map<String, Object> executeAndWait(Map<String, Object> aParameters, Activity aActivity, IAction aCalledBy) {
        IAction action = actionFactory.createAction(aParameters, aActivity, this, aCalledBy);
        return action.executeAndWait();
    }

    /**
    *  Fill in the declared parameters of the passed ActionTypeDefinition, if the
    *  parameter is missing here.
    * @param aActionType The ActionType definition to lookup for missing parameters.
    */
    public void fillParameters(ActionTypeDefinition aActionType) {
        if (actionFactory == null) actionFactory = aActionType.getActionFactory();
        if (viewValidationResultStrategie == null) viewValidationResultStrategie = aActionType.getViewValidationResultStrategie();
        if ((actionType == null) || (JRCAFPlugin.EMPTY_STRING.equals(actionType) == true)) actionType = aActionType.getActionType();
        getRules().addAll(aActionType.getRules());
        for (String key : aActionType.getParameters().keySet()) if (parameters.containsKey(key) == false) parameters.put(key, aActionType.getParameters().get(key));
        for (String key : aActionType.getImageParameters().keySet()) if (imageParameters.containsKey(key) == false) imageParameters.put(key, aActionType.getImageParameters().get(key));
        for (String key : aActionType.getConverterParameters().keySet()) if (converterParameters.containsKey(key) == false) converterParameters.put(key, aActionType.getConverterParameters().get(key));
        for (String key : aActionType.getPartDefinitions().keySet()) if (partDefinitions.containsKey(key) == false) partDefinitions.put(key, aActionType.getPartDefinitions().get(key)); else {
            List<PartDefinition> definitions = partDefinitions.get(key);
            definitions.addAll(aActionType.getPartDefinitions().get(key));
        }
    }
}
