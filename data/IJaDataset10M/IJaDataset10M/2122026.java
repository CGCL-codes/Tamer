package com.dcivision.dms.web;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.alert.bean.UpdateAlert;
import com.dcivision.alert.core.AdapterMaster;
import com.dcivision.dms.bean.DmsDocument;
import com.dcivision.dms.bean.DmsValidation;
import com.dcivision.dms.core.DocumentRetrievalManager;
import com.dcivision.dms.core.DocumentValidateManager;
import com.dcivision.dms.dao.DmsDocumentDAObject;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractMaintAction;
import com.dcivision.lucene.IndexManager;

/**
  MaintDmsFullTextViewerAction.java

  This class is the for Dms Full Text Viewer.

  @author      jerry zhou
  @company     DCIVision Limited
  @creation date   12/29/2004
  @version     $Revision: 1.16.2.3 $
*/
public class MaintDmsFullTextViewerAction extends AbstractMaintAction {

    public static final String REVISION = "$Revision: 1.16.2.3 $";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        MaintDmsFullTextViewerForm actionForm = (MaintDmsFullTextViewerForm) form;
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
        Integer documentID = TextUtility.parseIntegerObj(actionForm.getDocumentID());
        DmsDocument document = docRetrievalManager.getDocument(documentID);
        DocumentValidateManager docValidateManager = new DocumentValidateManager(sessionContainer, conn);
        StringBuffer message = new StringBuffer();
        DmsValidation validation = docValidateManager.validateOperateAction(document, false, "R");
        List lstIdMisRight = validation.getLstIdReject();
        List lstIdBeLocked = validation.getLstIdlocked();
        List lstIdBeDeleted = validation.getLstIdDeleted();
        List lstIdBeArchived = validation.getLstIdArchived();
        if (!lstIdMisRight.isEmpty() || !lstIdBeDeleted.isEmpty() || !lstIdBeArchived.isEmpty()) {
            for (int i = 0; i < lstIdMisRight.size(); i++) {
                message.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.no_permission", docRetrievalManager.getLocationPath((Integer) lstIdMisRight.get(i))) + "\n");
            }
            for (int i = 0; i < lstIdBeLocked.size(); i++) {
                message.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.been_checkout", docRetrievalManager.getLocationPath((Integer) lstIdBeLocked.get(i))) + "\n");
            }
            for (int i = 0; i < lstIdBeDeleted.size(); i++) {
                message.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.been_deleted", docRetrievalManager.getLocationPath((Integer) lstIdBeDeleted.get(i))) + "\n");
            }
            for (int i = 0; i < lstIdBeArchived.size(); i++) {
                message.append(MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), "errors.dms.been_archived", docRetrievalManager.getLocationPath((Integer) lstIdBeArchived.get(i))) + "\n");
            }
            request.setAttribute("setRefreshTag", "1");
            addMessage(request, message.toString());
            actionForm.setDocumentName("");
            request.getSession().setAttribute(org.apache.struts.Globals.ERROR_KEY, message.toString());
            return mapping.findForward(GlobalConstant.NAV_MODE_EDIT);
        }
        InputStream fullTextFileInputStream = null;
        IndexManager idxManager = null;
        try {
            idxManager = new IndexManager(sessionContainer, conn);
            String opMode = actionForm.getOpMode();
            DmsDocument dmsDocument = docRetrievalManager.getDocument(documentID);
            String content = null;
            actionForm.setHistLength(1);
            if (Utility.isEmpty(opMode) || GlobalConstant.OP_MODE_LIST_RETAIN.equals(opMode)) {
                content = idxManager.getContentString(documentID);
                if (content == null) {
                    content = "";
                }
                actionForm.setContent(content);
            }
            if (GlobalConstant.OP_MODE_UPDATE.equals(opMode)) {
                content = actionForm.getContent();
                fullTextFileInputStream = new java.io.ByteArrayInputStream(content.getBytes("UTF-8"));
                idxManager.updateDmsDocument(dmsDocument, fullTextFileInputStream, null);
                addMessage(request, MESSAGE_RECORD_UPDATE);
                DmsDocumentDAObject dmsDocumentDAO = new DmsDocumentDAObject(sessionContainer, conn);
                dmsDocumentDAO.updateObject(dmsDocument);
                AdapterMaster am = new AdapterMaster(sessionContainer, conn);
                am.call(UpdateAlert.DOCUMENT_TYPE, dmsDocument.getID(), UpdateAlert.UPDATE_ACTION, dmsDocument.getDocumentName(), null, null, null, dmsDocument.getID());
                am.release();
                conn.commit();
            }
        } catch (Exception ioe) {
            throw new ApplicationException(ErrorConstant.DMS_CONTENT_VIEWER_UPDATE_ERROR);
        } finally {
            idxManager.close();
        }
        return mapping.findForward(GlobalConstant.NAV_MODE_EDIT);
    }

    public String getFunctionCode() {
        return null;
    }

    public String getMajorDAOClassName() {
        return null;
    }
}
