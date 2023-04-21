package gleam.executive.workflow.action.harvard;

import gleam.executive.service.DocServiceManager;
import gleam.executive.workflow.util.WorkflowUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.exe.ExecutionContext;
import gleam.executive.workflow.util.JPDLConstants;
import gleam.executive.workflow.sm.JbpmHandlerProxy;

public class DeleteAnnotationActionHandler extends JbpmHandlerProxy {

    private static final long serialVersionUID = 1L;

    private DocServiceManager docServiceManager;

    /**
	 *  defined globally in process definition tells if process is in test or
	 *  production mode. If ommitted, the default mode is 'production'
	 */
    private String inVarMode;

    private String inVarPerformer;

    private String inVarDocumentId;

    private String inVarAnnotationSetName;

    private String inVarAnnotatorsPerDocument;

    protected final Log log = LogFactory.getLog(getClass());

    public void execute(ExecutionContext context) throws Exception {
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@");
        log.debug("DeleteAnnotationActionHandler START");
        String mode = (String) context.getVariable(getInVarMode());
        log.debug("@@@@@@@ mode " + mode);
        String performer = (String) context.getContextInstance().getVariable(getInVarPerformer(), context.getToken());
        log.debug("&&&&&&&&&&&&& performer " + performer);
        String documentId = (String) context.getContextInstance().getVariable(getInVarDocumentId(), context.getToken());
        log.debug("&&&&&&&&&&&&& documentId " + documentId);
        String annotationSetName = (String) context.getContextInstance().getVariable(getInVarAnnotationSetName(), context.getToken());
        log.debug("&&&&&&&&&&&&& annotationSetName " + annotationSetName);
        String currentDocumentAnnotationCSVString = (String) context.getVariable(JPDLConstants.ANNOTATED_BY_PREFIX + documentId);
        String updatedDocumentAnnotationCSVString = WorkflowUtil.markDocumentAsNotAnnotated(performer, currentDocumentAnnotationCSVString);
        log.debug("Set variable " + JPDLConstants.ANNOTATED_BY_PREFIX + documentId + " to: " + (String) context.getVariable(JPDLConstants.ANNOTATED_BY_PREFIX + documentId) + " to value " + updatedDocumentAnnotationCSVString);
        context.setVariable(JPDLConstants.ANNOTATED_BY_PREFIX + documentId, updatedDocumentAnnotationCSVString);
        String currentDocumentCancelationCSVString = (String) context.getVariable(JPDLConstants.CANCELED_BY_PREFIX + documentId);
        String updatedDocumentCancelationCSVString = WorkflowUtil.markDocumentAsCanceled(performer, currentDocumentCancelationCSVString);
        log.debug("Set variable " + JPDLConstants.CANCELED_BY_PREFIX + documentId + " to value " + updatedDocumentCancelationCSVString);
        context.setVariable(JPDLConstants.CANCELED_BY_PREFIX + documentId, updatedDocumentCancelationCSVString);
        if (annotationSetName == null) {
            annotationSetName = performer;
        }
        if (!JPDLConstants.TEST_MODE.equals(mode)) {
            boolean flag = docServiceManager.deleteAnnotationSet(documentId, annotationSetName);
            log.debug("is AS deleted? " + flag);
        } else {
            log.debug("Process is in TEST mode, so do not call docservice! ");
        }
        log.debug("DeleteAnnotationActionHandler END");
        context.leaveNode();
        log.debug("@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public String getInVarPerformer() {
        return inVarPerformer;
    }

    public void setInVarPerformer(String inVarPerformer) {
        this.inVarPerformer = inVarPerformer;
    }

    public DocServiceManager getDocServiceManager() {
        return docServiceManager;
    }

    public void setDocServiceManager(DocServiceManager docServiceManager) {
        this.docServiceManager = docServiceManager;
    }

    public String getInVarDocumentId() {
        return inVarDocumentId;
    }

    public void setInVarDocumentId(String inVarDocumentId) {
        this.inVarDocumentId = inVarDocumentId;
    }

    public String getInVarAnnotatorsPerDocument() {
        return inVarAnnotatorsPerDocument;
    }

    public void setInVarAnnotatorsPerDocument(String inVarAnnotatorsPerDocument) {
        this.inVarAnnotatorsPerDocument = inVarAnnotatorsPerDocument;
    }

    public String getInVarAnnotationSetName() {
        return inVarAnnotationSetName;
    }

    public void setInVarAnnotationSetName(String inVarAnnotationSetName) {
        this.inVarAnnotationSetName = inVarAnnotationSetName;
    }

    public String getInVarMode() {
        return inVarMode;
    }

    public void setInVarMode(String inVarMode) {
        this.inVarMode = inVarMode;
    }
}
