package bexee.core;

import bexee.model.activity.Activity;
import bexee.model.activity.Assign;
import bexee.model.activity.Compensate;
import bexee.model.activity.Empty;
import bexee.model.activity.Flow;
import bexee.model.activity.Invoke;
import bexee.model.activity.Receive;
import bexee.model.activity.Reply;
import bexee.model.activity.Sequence;
import bexee.model.activity.Switch;
import bexee.model.elements.Copy;
import bexee.model.elements.Correlation;
import bexee.model.elements.CorrelationPattern;
import bexee.model.elements.Link;
import bexee.model.elements.PartnerLink;
import bexee.model.elements.PartnerLinks;
import bexee.model.elements.Variable;
import bexee.model.elements.Variables;
import bexee.model.process.Process;

/**
 * The <code>ProcessController</code> is the core of the engine and contains
 * the execution logic for every BPEL activity.
 * <p>
 * The <code>ProcessController</code> needs to be thread-safe as several
 * threads may use it simultaneously.
 * <p>
 * The current version of bexee doesn't support all possible BPEL activities.
 * This is the reason, why there is a
 * <code>process(ProcessInstance, Activity)</code> method, it's a substitute
 * for all unimplemented activities. When implementing an Activity, the
 * implementor has to add a method
 * <code>process(ProcessInstance, NewlyImplActivity)</code> to this interface
 * and implement it in all implementing classes.
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:10 $
 * @author Patric Fornasier
 * @author Pawel Kowalski
 */
public interface ProcessController {

    /**
     * Execute the process using the given <code>ProcessInstance</code> and
     * the <code>BexeeMessage</code>.
     * 
     * @param instance
     *            the <code>ProcessInstance</code>
     * @param message
     *            the <code>BexeeMessage</code>
     */
    public void processMessage(ProcessInstance instance, BexeeMessage message);

    /**
     * Execute the root <code>Process</code> element of the BPEL process.
     * 
     * @param process
     *            root BPEL element
     * @param instance
     *            the process instance
     * @throws Exception
     */
    public void process(Process process, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Receive</code> activity.
     * 
     * @param receive
     * @param instance
     * @throws Exception
     */
    public void process(Receive receive, ProcessInstance instance) throws Exception;

    /**
     * Execute an <code>Invoke</code> BPEL activity.
     * 
     * @param invoke
     * @param instance
     * @throws Exception
     */
    public void process(Invoke invoke, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Reply</code> activity.
     * 
     * @param reply
     * @param instance
     * @throws Exception
     */
    public void process(Reply reply, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Variable</code> BPEL element.
     * 
     * @param variable
     * @param instance
     * @throws Exception
     */
    public void process(Variable variable, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Sequence</code> structured activity.
     * 
     * @param sequence
     * @param instance
     * @throws Exception
     */
    public void process(Sequence sequence, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Switch</code> structured activity.
     * 
     * @param bpelSwitch
     * @param instance
     * @throws Exception
     */
    public void process(Switch bpelSwitch, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Link</code> BPEL element.
     * 
     * @param link
     * @param instance
     * @throws Exception
     */
    public void process(Link link, ProcessInstance instance) throws Exception;

    /**
     * Execute <code>PartnerLinks</code> BPEL element.
     * 
     * @param partnerLinks
     * @param instance
     * @throws Exception
     */
    public void process(PartnerLinks partnerLinks, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>PartnerLink</code> BPEL element.
     * 
     * @param partnerLink
     * @param instance
     * @throws Exception
     */
    public void process(PartnerLink partnerLink, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Compensate</code> BPEL element.
     * 
     * @param compenstate
     * @param instance
     * @throws Exception
     */
    public void process(Compensate compenstate, ProcessInstance instance) throws Exception;

    /**
     * This is the process method for an Activity and is kept here as long as
     * there exist unimplemented activities.
     * 
     * @param assign
     * @param instance
     * @throws Exception
     */
    public void process(Assign assign, ProcessInstance instance) throws Exception;

    /**
     * Execute an <code>Activity</code> activity.
     * 
     * @param activity
     * @param instance
     * @throws Exception
     */
    public void process(Activity activity, ProcessInstance instance) throws Exception;

    /**
     * Execute an <code>Empty</code> activity.
     * 
     * @param activity
     * @param instance
     * @throws Exception
     */
    public void process(Empty empty, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Flow</code> structured activity.
     * 
     * @param flow
     * @param instance
     * @throws Exception
     */
    public void process(Flow flow, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Variables</code> BPEL element.
     * 
     * @param variables
     * @param instance
     * @throws Exception
     */
    public void process(Variables variables, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Correlation</code> BPEL element.
     * 
     * @param correlation
     * @param instance
     * @throws Exception
     */
    public void process(Correlation correlation, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>CorrelationPattern</code> BPEL element.
     * 
     * @param correlationPattern
     * @param instance
     * @throws Exception
     */
    public void process(CorrelationPattern correlationPattern, ProcessInstance instance) throws Exception;

    /**
     * Execute a <code>Copy</code> BPEL element.
     * 
     * @param copy
     * @param instance
     */
    public void process(Copy copy, ProcessInstance instance) throws Exception;
}
