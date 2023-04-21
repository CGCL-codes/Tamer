package org.kablink.teaming.module.workflow.support;

import java.util.Map;
import java.util.HashMap;

/**
 * Abstact class to kick off customization in workflow processing
 * Only usefull for non-scheduled callouts, where workflow exection is active
 * @author Janet
 *
 */
public abstract class AbstractWorkflowCallout implements WorkflowCallout {

    protected WorkflowCallout helper;

    protected Map params;

    public void setHelper(WorkflowCallout helper) {
        this.helper = helper;
    }

    public Object getVariable(String name) {
        if (helper == null) {
            if (params == null) params = new HashMap();
            return params.get(name);
        }
        return helper.getVariable(name);
    }

    public Map getVariables() {
        if (helper == null) {
            if (params == null) params = new HashMap();
            return params;
        }
        return helper.getVariables();
    }

    public void setVariable(String name, Object value) {
        if (helper == null) {
            if (params == null) params = new HashMap();
            params.put(name, value);
            return;
        }
        helper.setVariable(name, value);
    }
}
