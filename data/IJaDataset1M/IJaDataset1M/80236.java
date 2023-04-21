package gleam.executive.workflow.action.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import gleam.executive.model.Document;
import gleam.executive.service.DocServiceManager;
import gleam.executive.service.GateServiceManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import gleam.executive.workflow.util.JPDLConstants;
import gleam.executive.workflow.sm.JbpmHandlerProxy;

public class GateServiceCorpusActionHandler extends JbpmHandlerProxy {

    private static final long serialVersionUID = 1L;

    protected final Log log = LogFactory.getLog(getClass());

    private GateServiceManager gateServiceManager;

    private DocServiceManager docServiceManager;

    private String inVarCorpusId;

    private String inVarAsKey;

    private String inVarAsValue;

    private String inVarParameterKey;

    private String inVarParameterValue;

    /**
   * A message process variable is assigned the value of the message
   * member. The process variable is created if it doesn't exist yet.
   */
    public void execute(ExecutionContext context) throws Exception {
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@");
        log.debug("GateServiceCorpusActionHandler START");
        log.debug("&&&&&&&&&&&&& Testing GAS");
        long processInstanceId = context.getProcessInstance().getId();
        log.debug("current ProcessInstance " + processInstanceId);
        long tokenId = context.getToken().getId();
        log.debug("current tokenId " + tokenId);
        String corpusId = (String) context.getVariable(getInVarCorpusId());
        log.debug("&&&&&&&&&&&&& corpusId " + corpusId);
        List<Document> documents = docServiceManager.listDocuments(corpusId);
        log.debug("found documents " + documents.size());
        Iterator<Document> it = documents.iterator();
        String asKey = (String) context.getVariable(getInVarAsKey());
        log.debug("&&&&&&&&&&&&& asKey " + asKey);
        String asValue = (String) context.getVariable(getInVarAsValue());
        log.debug("&&&&&&&&&&&&& asValue " + asValue);
        Map<String, String> asMappings = null;
        if (asKey != null && asValue != null) {
            asMappings = new HashMap<String, String>();
            asMappings.put(asKey, asValue);
        }
        String parameterKey = (String) context.getVariable(getInVarParameterKey());
        log.debug("&&&&&&&&&&&&& parameterKey " + parameterKey);
        String parameterValue = (String) context.getVariable(getInVarParameterValue());
        log.debug("&&&&&&&&&&&&& parameterValue " + parameterValue);
        Map<String, String> parameterMappings = null;
        if (parameterKey != null && parameterValue != null) {
            parameterMappings = new HashMap<String, String>();
            parameterMappings.put(parameterKey, parameterValue);
        }
        final Token rootToken = context.getToken();
        final Node node = context.getNode();
        int j = 1;
        while (it.hasNext()) {
            Document doc = it.next();
            String docId = doc.getDocumentID();
            final Token newToken = new Token(rootToken, JPDLConstants.NEW_TOKEN_PREFIX + node.getId() + "." + j);
            log.debug("@@@@@@@ created new token " + newToken.getName());
            newToken.setTerminationImplicit(true);
            context.getJbpmContext().getSession().save(newToken);
            final ExecutionContext newExecutionContext = new ExecutionContext(newToken);
            log.debug("@@@@@@@@@@@ call GaS ");
            newExecutionContext.getContextInstance().createVariable(JPDLConstants.DOCUMENT_ID, docId, newToken);
            j++;
        }
        log.debug("GateServiceActionHandler END");
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public String decide(ExecutionContext executionContext) throws Exception {
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@");
        log.debug("GateServiceCorpusActionHandler START");
        String callbackError = (String) executionContext.getVariable(JPDLConstants.CALLBACK_ERROR_NAME);
        log.debug("callbackError " + callbackError);
        String result = "";
        if ("".equals(callbackError)) {
            result = JPDLConstants.TRANSITION_SUCCESS;
        } else {
            result = JPDLConstants.TRANSITION_FAILURE;
        }
        log.debug("GateServiceDecisionHandler END");
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@");
        return result;
    }

    public GateServiceManager getGateServiceManager() {
        return gateServiceManager;
    }

    public void setGateServiceManager(GateServiceManager gateServiceManager) {
        this.gateServiceManager = gateServiceManager;
    }

    public DocServiceManager getDocServiceManager() {
        return docServiceManager;
    }

    public void setDocServiceManager(DocServiceManager docServiceManager) {
        this.docServiceManager = docServiceManager;
    }

    public String getInVarAsKey() {
        return inVarAsKey;
    }

    public void setInVarAsKey(String inVarAsKey) {
        this.inVarAsKey = inVarAsKey;
    }

    public String getInVarAsValue() {
        return inVarAsValue;
    }

    public void setInVarAsValue(String inVarAsValue) {
        this.inVarAsValue = inVarAsValue;
    }

    public String getInVarCorpusId() {
        return inVarCorpusId;
    }

    public void setInVarCorpusId(String inVarCorpusId) {
        this.inVarCorpusId = inVarCorpusId;
    }

    public String getInVarParameterKey() {
        return inVarParameterKey;
    }

    public void setInVarParameterKey(String inVarParameterKey) {
        this.inVarParameterKey = inVarParameterKey;
    }

    public String getInVarParameterValue() {
        return inVarParameterValue;
    }

    public void setInVarParameterValue(String inVarParameterValue) {
        this.inVarParameterValue = inVarParameterValue;
    }
}
