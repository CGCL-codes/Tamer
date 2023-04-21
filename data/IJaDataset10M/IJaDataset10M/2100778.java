package si.cit.eprojekti.edocs.controller.documentWorkFlowStates;

import java.util.ArrayList;
import java.util.Iterator;
import si.cit.eprojekti.edocs.DocsSchema;
import si.cit.eprojekti.edocs.dbobj.Document;
import si.cit.eprojekti.edocs.util.ComponentSecurityManager;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import si.cit.eprojekti.edocs.controller.DocumentController;
import si.cit.eprojekti.edocs.controller.documentStates.ErrorState;
import javax.servlet.ServletRequest;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.i18n.Messages;
import org.apache.log4j.Priority;
import com.jcorporate.expresso.core.controller.ServletControllerRequest;

/**
 *	
 *	Approved Document State - external state for Document Work Flow Controller
 *
 * 	@author taks
 *	@version 1.0
 *
 */
public class ApprovedDocumentState extends State {

    private static final long serialVersionUID = -8043837085219426824L;

    private static org.apache.log4j.Category standardLog = org.apache.log4j.Category.getInstance("pvn.standard.edocs");

    private static org.apache.log4j.Category debugLog = org.apache.log4j.Category.getInstance("pvn.debug.edocs");

    private static org.apache.log4j.Category observerLog = org.apache.log4j.Category.getInstance("pvn.observer.edocs");

    /**
	 * 	Constructor
	 */
    public ApprovedDocumentState() {
        super();
    }

    /** 
	 * Constructor
	 * @param stateName
	 * @param descrip
	 */
    public ApprovedDocumentState(String stateName, String descrip) {
        super(stateName, descrip);
    }

    /** 
	 *  Run this state
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        response.setFormCache();
        String projectId = "";
        try {
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(DocumentController.class);
            errorTrans.setState(ErrorState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            short accessLevel = ComponentSecurityManager.checkSecurityForProject(request.getParameter("projectId"), request.getParameter("category"), request.getUid());
            if (accessLevel < ComponentSecurityManager.ACCESS_LEADER) throw ComponentSecurityManager.accessDeniedOccured(response);
            projectId = request.getParameter("projectId");
            String docId = request.getParameter("documentId");
            String catId = request.getParameter("categoryId");
            String uId = String.valueOf(request.getUid());
            int checkCategory = 0;
            if (docId != null) {
                Document documentID = new Document();
                documentID.setField("DocumentId", docId);
                documentID.retrieve();
                checkCategory = documentID.getFieldInt("CategoryId");
            }
            if (catId != null) checkCategory = Integer.parseInt(catId);
            Document document = new Document();
            if (docId != null) {
                document.setField("DocumentId", docId);
                document.retrieve();
                String author = document.getAuthorName(document.getFieldInt("AuthorId"));
                if (author.equals("Unknown")) author = Messages.getString(DocsSchema.class.getName(), request.getLocale(), "approvedDocumentStateAuthorUnknown");
                if (document.getField("DocumentApproved").equals("0")) {
                    document.setField("DocumentApproved", uId);
                    document.setField("UpdatedUid", uId);
                    document.updateAproved();
                    document.setLocale(request.getLocale());
                    Output docApprovedInfo = new Output();
                    docApprovedInfo.setName("DocumentApprovedInfo");
                    docApprovedInfo.setLabel("Document with ID=" + docId + " was approved!");
                    docApprovedInfo.setAttribute("Author", author);
                    docApprovedInfo.setContent(document.getField("DocumentTitle"));
                    response.add(docApprovedInfo);
                } else {
                    Output docApprovedFail = new Output();
                    docApprovedFail.setName("DocumentApprovedFail");
                    docApprovedFail.setLabel("Document with ID=" + docId + " is already approved!");
                    docApprovedFail.setAttribute("Author", author);
                    docApprovedFail.setContent(document.getField("DocumentTitle"));
                    response.add(docApprovedFail);
                }
            }
            if (catId != null) {
                document.setLocale(request.getLocale());
                document.setField("CategoryId", catId);
                document.setField("DocumentApproved", "0");
                int numberOfDissDocs = document.count();
                if (numberOfDissDocs == 0) {
                    Output catApprovedFail = new Output();
                    catApprovedFail.setName("CategoryApprovedFail");
                    catApprovedFail.setLabel("In category with ID=" + catId + " is no document to be approved!");
                    catApprovedFail.setContent(document.getValidValueDescrip("CategoryId"));
                    response.add(catApprovedFail);
                } else {
                    ArrayList allRecords = document.searchAndRetrieveList();
                    Iterator i = allRecords.iterator();
                    while (i.hasNext()) {
                        document = (Document) i.next();
                        document.setField("DocumentApproved", uId);
                        document.setField("UpdatedUid", uId);
                        document.update(true);
                    }
                    Output catApprovedInfo = new Output();
                    catApprovedInfo.setName("CategoryApprovedInfo");
                    catApprovedInfo.setLabel("In category with ID=" + catId + " has been approved " + numberOfDissDocs + " document(s)!");
                    catApprovedInfo.setContent(document.getValidValueDescrip("CategoryId"));
                    catApprovedInfo.setAttribute("numberOfApprovedDocs", String.valueOf(numberOfDissDocs));
                    response.add(catApprovedInfo);
                }
            }
            Transition listDisapproved = new Transition();
            listDisapproved.setName("GoToListDissapproved");
            listDisapproved.setLabel(Messages.getString(DocsSchema.class.getName(), request.getLocale(), "approvedDocumentStateBackToDissapprovedList"));
            listDisapproved.setControllerObject(this.getController().getClass());
            listDisapproved.setState("listDisapprovedDocumentState");
            listDisapproved.addParam("projectId", projectId);
            response.add(listDisapproved);
        } catch (Exception e) {
            if (e instanceof DBException) addError("errors.DBException"); else if (e.getMessage().equals("errors.accessDeniedOccured")) addError("errors.accessDeniedOccured"); else addError("errors.Exception");
            if (standardLog.isEnabledFor(Priority.WARN)) standardLog.warn(" :: Exception in \"" + this.getName() + "\" : " + e.toString());
            if (debugLog.isDebugEnabled()) debugLog.debug(" :: Exception in \"" + this.getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
            if (observerLog.isInfoEnabled()) {
                ServletRequest servletRq = ((ServletControllerRequest) request).getServletRequest();
                observerLog.info(" :: Location= " + this.getClass().getName() + " :: UID= " + request.getUid() + " :: User= " + request.getUser() + " :: IP= " + servletRq.getRemoteAddr());
            }
        }
    }
}
