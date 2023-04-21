package edu.indiana.extreme.xbaya.workflow;

import edu.indiana.extreme.xbaya.component.ComponentException;
import edu.indiana.extreme.xbaya.event.EventProducer;
import edu.indiana.extreme.xbaya.gpel.GPELLinksFilter;
import edu.indiana.extreme.xbaya.gpel.script.BPELScript;
import edu.indiana.extreme.xbaya.gpel.script.BPELScriptType;
import edu.indiana.extreme.xbaya.graph.GraphException;
import edu.indiana.extreme.xbaya.security.SecurityUtil;
import edu.indiana.extreme.xbaya.security.UserX509Credential;
import edu.indiana.extreme.xbaya.wf.Workflow;
import edu.indiana.extreme.xbaya.XBayaEngine;
import org.gpel.client.GcInstance;
import org.gpel.client.GcSearchList;
import org.gpel.client.GpelClient;
import org.gpel.client.security.GpelUserX509Credential;
import xsul5.MLogger;
import xsul5.wsdl.WsdlDefinitions;
import java.net.URI;
import java.util.Map;

/**
 * @author : Eran Chinthaka ( eran.chinthaka@gmail.com )
 */
public abstract class WorkflowClient extends EventProducer {

    /**
     * Either workflow template or workflow instance
     */
    public enum WorkflowType {

        /**
         * Workflow template
         */
        TEMPLATE, /**
         * Workflow instance
         */
        INSTANCE
    }

    protected static final String PROCESS_WSDL_TYTLE = "process.wsdl";

    protected static final String PROCESS_GPEL_TITLE = "process.gpel";

    protected static final String PNG_MIME_TYPE = "image/png";

    protected static final String GRAPH_MIME_TYPE = "application/x-xbaya+xml";

    protected static final MLogger logger = MLogger.getLogger();

    protected URI engineURL;

    protected GpelClient client;

    protected GPELLinksFilter linksFilter = new GPELLinksFilter();

    protected GpelUserX509Credential gpelUserX509Credential;

    String xregistryURL;

    /**
     * @param engineURL
     * @throws WorkflowEngineException
     */
    public void setEngineURL(URI engineURL) throws WorkflowEngineException {
        this.engineURL = engineURL;
        connect();
    }

    /**
     * @return The URL of the GPEL Engine.
     */
    public URI getEngineURL() {
        return this.engineURL;
    }

    /**
     * Generates a BPEL process.
     *
     * @param workflow
     * @throws edu.indiana.extreme.xbaya.graph.GraphException
     *
     */
    public static void createScript(Workflow workflow) throws GraphException {
        BPELScript script = new BPELScript(workflow);
        script.create(BPELScriptType.GPEL);
        workflow.setGpelProcess(script.getGpelProcess());
        workflow.setWorkflowWSDL(script.getWorkflowWSDL().getWsdlDefinitions());
    }

    /**
     * Deploys a workflow to the GPEL Engine.
     *
     * @param workflow
     * @param redeploy
     * @return The workflow template ID.
     * @throws GraphException
     * @throws WorkflowEngineException
     */
    public URI createScriptAndDeploy(Workflow workflow, boolean redeploy) throws GraphException, WorkflowEngineException {
        logger.entering(new Object[] { workflow });
        createScript(workflow);
        return deploy(workflow, redeploy);
    }

    /**
     * Loads a workflow with s specified workflow template ID.
     *
     * @param templateID The workflow template ID.
     * @return The workflow loaded
     * @throws GraphException
     * @throws WorkflowEngineException
     * @throws edu.indiana.extreme.xbaya.component.ComponentException
     *
     */
    public Workflow load(URI templateID) throws GraphException, WorkflowEngineException, ComponentException {
        return load(templateID, WorkflowType.TEMPLATE);
    }

    /**
     * Returns the List of GcSearchResult.
     * <p/>
     * This method returns the first 100 matches.
     *
     * @return The List of GcSearchResult.
     * @throws WorkflowEngineException
     */
    public GcSearchList list() throws WorkflowEngineException {
        return list(100, WorkflowType.TEMPLATE);
    }

    /**
     * @param workflow
     * @param dscURL
     * @return The instance of workflow
     * @throws WorkflowEngineException
     * @throws ComponentException
     * @throws GraphException
     */
    public GcInstance instantiate(Workflow workflow, URI dscURL) throws WorkflowEngineException, ComponentException, GraphException {
        return instantiate(workflow, dscURL, null);
    }

    /**
     * @return True if the connection is secure; false otherwise.
     */
    public boolean isSecure() {
        return true;
    }

    /**
     * Checks if the client is connected to the BPEL engine.
     *
     * @return true if the client is connected to the BPEL engine; false
     *         otherwise.
     */
    protected synchronized boolean isConnected() {
        return (this.client != null);
    }

    /**
     * @param userX509Credential
     * @throws WorkflowEngineException 
     */
    public abstract void setUserX509Credential(UserX509Credential userX509Credential) throws WorkflowEngineException;

    /**
     * @param workflow
     * @param redeploy
     * @return The workflow template ID.
     * @throws WorkflowEngineException
     */
    public abstract URI deploy(Workflow workflow, boolean redeploy) throws WorkflowEngineException;

    /**
     * @param id
     * @param workflowType
     * @return The workflow loaded
     * @throws GraphException
     * @throws WorkflowEngineException
     * @throws ComponentException
     */
    public abstract Workflow load(URI id, WorkflowType workflowType) throws GraphException, WorkflowEngineException, ComponentException;

    /**
     * Returns the List of GcSearchResult.
     *
     * @param maxNum The maximum number of results
     * @param type
     * @return The List of GcSearchResult.
     * @throws WorkflowEngineException
     */
    @SuppressWarnings("boxing")
    public abstract GcSearchList list(int maxNum, WorkflowType type) throws WorkflowEngineException;

    /**
     * @param workflow The workflow to instantiate.
     * @param dscURL   The URL of the DSC.
     * @param name     The name that becomes a part of the workflow instance name.
     * @return The instance of workflow
     * @throws WorkflowEngineException
     * @throws ComponentException
     * @throws GraphException
     */
    public abstract GcInstance instantiate(Workflow workflow, URI dscURL, String name) throws WorkflowEngineException, ComponentException, GraphException;

    /**
     * Instantiate a specified workflow.
     * <p/>
     * The workflow must have been dployed.
     *
     * @param workflow
     * @param wsdlMap  Map<partnerLinkName, CWSDL>
     * @return The workflow instance.
     * @throws WorkflowEngineException
     * @Deprecated This one doesn't support hierarchical workflows. Use
     * instantiate(workflow, dscURL) instead.
     */
    @Deprecated
    public abstract GcInstance instantiate(Workflow workflow, Map<String, WsdlDefinitions> wsdlMap) throws WorkflowEngineException;

    /**
     * Starts the workflow instance.
     * <p/>
     * The AWSDLs in workflow must have been modified to CWSDLs.
     *
     * @param instance
     * @return The WSDL of the workflow.
     * @throws WorkflowEngineException
     */
    public abstract WsdlDefinitions start(final GcInstance instance) throws WorkflowEngineException;

    public abstract void connect() throws WorkflowEngineException;

    public abstract void setXRegistryUrl(URI xRegistryURL);

    public abstract void setXBayaEngine(XBayaEngine xBayaEngine);
}
