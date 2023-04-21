package org.omg.DfResourceAccessDecision;

/**
 * Interface definition: AccessDecision.
 * 
 * @author OpenORB Compiler
 */
public interface AccessDecisionOperations {

    /**
     * Operation access_allowed
     */
    public boolean access_allowed(org.omg.DfResourceAccessDecision.ResourceName resource_name, String operation, org.omg.Security.SecAttribute[] attribute_list) throws org.omg.DfResourceAccessDecision.RadInternalError;

    /**
     * Operation multiple_access_allowed
     */
    public boolean[] multiple_access_allowed(org.omg.DfResourceAccessDecision.AccessDefinition[] access_requests, org.omg.Security.SecAttribute[] attribute_list) throws org.omg.DfResourceAccessDecision.RadInternalError;
}
