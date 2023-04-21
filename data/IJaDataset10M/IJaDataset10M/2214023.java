package uk.org.ogsadai.dqp.common;

import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.DataSinkResource;
import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.Server;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Evaluation node interface that defines how the DQP code functionality 
 * interacts with an evaluation node.  Evaluation nodes can be local (on 
 * the same OGSA-DAI instance as the DQP coordinator) or remote (on another
 * OGSA-DAI instance).
 * <p>
 * DQP can send workflows to both local and remote evaluation nodes.  The
 * workflows that DQP generates are neutral to the OGSA-DAI presentation layer
 * being used.  These presentation layer neutral workflows must be converted
 * to presentation layer specific workflows prior to execution. This interface
 * supports these transformations.
 * <p>
 * For local evaluation nodes the interface has a method called 
 * <code>transformWorkflow</code> that explicitly performs the transformation.
 * The core DQP code will call this method before executing the workflow on
 * the local OGSA-DAI engine.
 * <p>
 * For remote evaluation nodes core DQP code will call the <code>getDRER</code>
 * method to obtain proxy for the remote Data Request Execution Resource (DRER).
 * When code DQP code passes a workflow to this proxy the proxy is will 
 * automatically transform the presentation layer neutral workflow to a workflow
 * suitable for the specific presentation layer.
 * <p>
 * The presentation layer neutral workflows produced by the core DQP code will
 * all have the following properties:
 * <ul>
 *  <li>
 *   all activities within the pipeline will be instances of
 *   <code>uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity</code>.
 *  </li>
 *  <li>
 *   all activity instances with activity name 
 *   <code>uk.org.ogsadai.ObtainFromDataSource</code> will be instances of
 *   <code>uk.org.ogsadai.dqp.execute.workflow.DQPObtainFromDataSource</code>.
 *   This class provides a method called <code>getEvaluationNode</code> that
 *   give access to the evaluation node containing the data source resource
 *   from which data is to be obtained.  This method returns an instance of
 *   <code>EvaluationNode</code> that can be cast to a particular implementation
 *   of <code>EvaluationNode</code> to obtain presentation layer details such
 *   as the service URL for example.
 *  </li>
 *  <li>
 *   all activity instances with activity name 
 *   <code>uk.org.ogsadai.ObtainFromDataSource</code> will have the following
 *   inputs:
 *     <ul>
 *       <li><tt>mode</tt><li>
 *       <li><tt>numberOfBlocks</tt></li>
 *       <li><tt>resourceID</tt></li>
 *     </ul>
 *   and an output called <tt>data</tt> which are normal inputs and outputs
 *   to the <tt>ObtainFromDataSource</tt> activity.
 *  </li>
 * Note that activity does not have an input specifying the service URL or EPR.
 * The addition of this input and any additional security inputs (and possibly
 * other activities to generate them) is the tasks of the workflow transformer
 * associated with the evaluation node.
 *
 * TODO: check if still relevant
 *
 * @author The OGSA-DAI Project Team.
 */
public interface EvaluationNode {

    /**
     * Returns <code>true</code> if the evaluation node is local.
     * 
     * @return <code>true</code> if the evaluation node is local.
     */
    public boolean isLocal();

    /**
     * Gets the Data Request Execution Resource for this evaluation node.
     * <p>
     * The <tt>DataRequestExecutionResource</tt> instance returned will 
     * automatically transform DQP's presentation layer neutral workflows into 
     * workflows suitable for the target presentation layer prior to executing
     * them.  For details of DQP's presentation layer neutral workflows see the
     * top level documentation for this interface.
     * 
     * @param requestDetails 
     *            details relating to parent request
     * 
     * @return the client toolkit proxy used to contact the DRER.
     */
    public DataRequestExecutionResource getDRER(RequestDetails requestDetails);

    /**
     * Gets the specified Data Source Resource on this evaluation node.
     * 
     * @param resourceID
     *            data source resource ID
     * @param requestDetails 
     *            details relating to parent request
     *            
     * @return the client toolkit proxy data source resource used to contact
     *         the remote resource.
     */
    public DataSourceResource getDataSourceResource(ResourceID resourceID, RequestDetails requestDetails);

    /**
     * Gets the specified Data Sink Resource on this evaluation node.
     * 
     * @param resourceID
     *            data sink resource ID
     * @param requestDetails 
     *            details relating to parent request
     *            
     * @return the client toolkit proxy data sink resource used to contact
     *         the remote resource.
     */
    public DataSinkResource getDataSinkResource(ResourceID resourceID, RequestDetails requestDetails);

    /**
     * Returns an instance of a client toolkit <code>Server</code> class with
     * the server URL set.
     * 
     * @return server instance
     */
    public Server getServer();

    /**
     * Transforms DQP presentation layer neutral worfklow into a workflow 
     * suitable for the presentation layer being used.
     * <p>
     * This method will only be called for local evaluation nodes.
     * <p>
     * For details of DQP's presentation layer neutral workflows see the
     * top level documentation for this interface.
     * 
     * @param workflow         workflow to be transformed.  
     *    
     * @param requestDetails   details relating to parent request
     * 
     * @return workflow to execute.
     */
    public PipelineWorkflow transformWorkflow(PipelineWorkflow workflow, RequestDetails requestDetails);
}
