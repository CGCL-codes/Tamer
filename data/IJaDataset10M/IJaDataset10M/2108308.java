package com.dcivision.workflow.web;

import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.dms.bean.DmsDocument;
import com.dcivision.dms.dao.DmsDocumentDAObject;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractListAction;
import com.dcivision.framework.web.AbstractSearchForm;

/**
 * <p>Class Name:       ListWorkflowDocTrackingAction.java    </p>
 * <p>Description:      The list action class for ListWorkflowDocTracking.jsp</p>
 * @author           Lun Au
 * @company          DCIVision Limited
 * @creation date    26/10/2004
 * @version          $Revision: 1.5 $
 */
public class ListWorkflowDocTrackingAction extends AbstractListAction {

    public static final String REVISION = "$Revision: 1.5 $";

    /** Creates a new instance of ListWorkflowDocTrackingAction */
    public ListWorkflowDocTrackingAction() {
        super();
        this.setListName("workflowDocTrackingList");
        this.setListFunctionName("getListDocTracking");
    }

    public String getFunctionCode() {
        return (SystemFunctionConstant.WORKFLOW_TRACK);
    }

    public String getMajorDAOClassName() {
        return ("com.dcivision.workflow.dao.WorkflowProgressDAObject");
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ListWorkflowDocTrackingForm listWorkflowDocTrackingForm = (ListWorkflowDocTrackingForm) form;
        ActionForward forward = mapping.findForward(GlobalConstant.NAV_MODE_LIST);
        request.setAttribute(this.m_sListName, new ArrayList());
        if (!SystemParameterFactory.getSystemParameterBoolean("dms.enable_paper_document")) {
            addError(request, ErrorConstant.COMMON_UNAUTHORIZED_ACCESS);
            return forward;
        }
        if (!Utility.isEmpty(listWorkflowDocTrackingForm.getDocReferenceNumber())) {
            DmsDocumentDAObject dmsDocumentDAO = new DmsDocumentDAObject(this.getSessionContainer(request), this.getConnection(request));
            DmsDocument dmsDoc = (DmsDocument) dmsDocumentDAO.getObjectByReferenceNo(GlobalConstant.RECORD_STATUS_ACTIVE, listWorkflowDocTrackingForm.getDocReferenceNumber().trim(), com.dcivision.dms.bean.DmsDocument.PAPER_DOC_TYPE);
            if (dmsDoc == null) {
                addError(request, ErrorConstant.DB_RECORD_NOT_FOUND_ERROR);
                return forward;
            } else {
                listWorkflowDocTrackingForm.setDocumentID(dmsDoc.getID().toString());
                if (!this.getSessionContainer(request).getPermissionManager().hasAccessRight(GlobalConstant.OBJECT_TYPE_DOCUMENT, Integer.valueOf(listWorkflowDocTrackingForm.getDocumentID()), "R")) {
                    addError(request, ErrorConstant.COMMON_UNAUTHORIZED_ACCESS);
                    return forward;
                }
            }
        }
        forward = super.execute(mapping, form, request, response);
        if (GlobalConstant.NAV_MODE_PREVIEW.equals(((AbstractSearchForm) form).getNavMode())) {
            return mapping.findForward(GlobalConstant.NAV_MODE_PREVIEW);
        }
        return forward;
    }
}
