package com.intel.gpe.client2.expert.workfloweditor.variables;

import com.intel.gpe.client2.SelectionClient;
import com.intel.gpe.clients.api.workflow.Action;
import com.intel.gpe.clients.api.workflow.AtomicJob;
import com.intel.gpe.clients.api.workflow.GPEWorkflowJob;
import com.intel.gpe.gridbeans.IGridBeanParameterValue;
import com.intel.util.xml.Namespaces;

/**
 * Base interface for AtomicGridBeanModel.TSS parameter values
 * 
 * @version $Id: TSSValue.java,v 1.4 2006/11/24 14:35:39 vashorin Exp $
 * @author Valery Shorin
 */
public interface TSSValue extends IGridBeanParameterValue, SelectionClient {

    /**
     * @param atomicJob AtomicJob to submit
     * @param workflowJob GPEWorkflowJob instance
     * @return action for setup atomic job and submit it   
     */
    public Action setupAndSubmitJob(AtomicJob atomicJob, GPEWorkflowJob workflowJob, Namespaces namespaces);
}
