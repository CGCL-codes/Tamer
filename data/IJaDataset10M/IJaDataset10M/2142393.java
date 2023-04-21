package com.dcivision.dms.web;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.dms.DmsErrorConstant;
import com.dcivision.dms.bean.DmsDocument;
import com.dcivision.dms.bean.DmsRoot;
import com.dcivision.dms.bean.DmsValidation;
import com.dcivision.dms.bean.DmsVersion;
import com.dcivision.dms.core.DocumentOperationManager;
import com.dcivision.dms.core.DocumentRetrievalManager;
import com.dcivision.dms.core.DocumentValidateManager;
import com.dcivision.dms.core.RootRetrievalManager;
import com.dcivision.dms.dao.DmsDocumentDAObject;
import com.dcivision.framework.ApplicationContainer;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.PermissionManager;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractListAction;
import com.dcivision.framework.web.AbstractSearchForm;

/**
 * <p>Class Name:       ListDmsPublicDocumentAction.java    </p>
 * <p>Description:      The list action class for ListUserRecord.jsp</p>
 * @author              Zoe Shum
 * @company             DCIVision Limited
 * @creation date       01/08/2003
 * @version             $Revision: 1.23.2.2 $
 */
public class ListDmsCompoundDocumentAction extends AbstractListAction {

    public static final String REVISION = "$Revision: 1.23.2.2 $";

    /**
   *  Constructor - Creates a new instance of ListDmsPublicDocumentAction and define the default listName.
   */
    public ListDmsCompoundDocumentAction() {
        super();
        this.setListName("dmsCompoundDocumentList");
    }

    /**
   * getMajorDAOClassName
   *
   * @return  The class name of the major DAObject will be used in this action.
   */
    public String getMajorDAOClassName() {
        return ("com.dcivision.dms.dao.DmsDocumentDAObject");
    }

    /**
   * getFunctionCode
   *
   * @return  The corresponding system function code of action.
   */
    public String getFunctionCode() {
        return (SystemFunctionConstant.DMS_COMPOUND_DOCUMENT);
    }

    /**
   *  Method execute() - generaic function of result list handling, including:
   *                     1. Delete record in List
   *                     2. Get List Data
   *
   *  @param      mapping               ActionMapping from struts
   *  @param      form                  ActionForm from struts
   *  @param      request               HttpServletReuqest
   *  @param      respond               HttpServletRespond
   *  @return     ActionForward         Return the action forward object for struts
   *  @throws     ServletException      Throws ServletException
   */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (!Utility.isEmpty(request.getSession().getAttribute(org.apache.struts.Globals.MESSAGE_KEY))) {
            this.addMessage(request, (String) request.getSession().getAttribute(org.apache.struts.Globals.MESSAGE_KEY));
            request.getSession().removeAttribute(org.apache.struts.Globals.MESSAGE_KEY);
        }
        if (!Utility.isEmpty(request.getSession().getAttribute(org.apache.struts.Globals.ERROR_KEY))) {
            this.addError(request, (String) request.getSession().getAttribute(org.apache.struts.Globals.ERROR_KEY));
            request.getSession().removeAttribute(org.apache.struts.Globals.ERROR_KEY);
        }
        AbstractSearchForm searchForm = findExpectForm4ListAction(mapping, (AbstractSearchForm) form, request);
        log.info(this.getClass().getName() + ", OP Mode:" + searchForm.getOpMode() + ", Nav Mode:" + searchForm.getNavMode());
        ActionForward forward = this.retrieveFunctionCode(request, response, mapping);
        if (forward != null) {
            return forward;
        }
        if (GlobalConstant.OP_MODE_LIST_DELETE.equals(searchForm.getOpMode())) {
            try {
                this.deleteListData2(mapping, searchForm, request, response);
                this.commit(request);
                searchForm.setBasicSelectedID(null);
                searchForm.setOpMode(null);
                this.getListData(mapping, searchForm, request, response);
            } catch (ApplicationException e) {
                this.rollback(request);
                this.handleApplicationException(request, e);
                request.setAttribute(this.m_sListName, new ArrayList());
                try {
                    this.getListData(mapping, searchForm, request, response);
                    searchForm.setOpMode("");
                } catch (ApplicationException e1) {
                    this.handleApplicationException(request, e1);
                    request.setAttribute(this.m_sListName, new ArrayList());
                }
            }
        } else {
            try {
                this.getListData(mapping, searchForm, request, response);
            } catch (ApplicationException e) {
                this.handleApplicationException(request, e);
                request.setAttribute(this.m_sListName, new ArrayList());
            }
        }
        List resultList = (List) request.getAttribute(this.m_sListName);
        while ((resultList.size() == 0) && (TextUtility.parseInteger(searchForm.getCurStartRowNo()) > TextUtility.parseInteger(searchForm.getPageOffset()))) {
            searchForm.setCurStartRowNo("1");
            this.getListData(mapping, searchForm, request, response);
        }
        return mapping.findForward(GlobalConstant.NAV_MODE_LIST);
    }

    /**
   * getListData
   *
   * Override the parent's function. Purpose in create the default personal folder when non-exists,
   * and load the dmsDocument list.
   *
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws ApplicationException
   */
    public void getListData(ActionMapping mapping, AbstractSearchForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        List resultList = new ArrayList();
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = null;
        String curStartRowNo = form.getCurStartRowNo();
        if (Utility.isEmpty(curStartRowNo)) {
            curStartRowNo = request.getParameter("curStartRowNo");
            form.setCurStartRowNo(curStartRowNo);
        }
        try {
            conn = this.getConnection(request);
            DmsDocumentDAObject documentDAO = new DmsDocumentDAObject(sessionContainer, conn);
            RootRetrievalManager rootManager = new RootRetrievalManager(sessionContainer, conn);
            PermissionManager permissionManager = sessionContainer.getPermissionManager();
            DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            int startOffset = TextUtility.parseInteger(form.getCurStartRowNo());
            int pageSize = TextUtility.parseInteger(form.getPageOffset());
            resultList = documentDAO.getCompoundDocumentList(form);
            List newResulstList = new ArrayList();
            List list = new ArrayList();
            Map dmsRootMap = new HashMap();
            for (int i = 0; i < resultList.size(); i++) {
                DmsDocument dmsDocument = (DmsDocument) resultList.get(i);
                Integer rootID = dmsDocument.getRootID();
                DmsRoot dmsRoot = null;
                if (dmsRootMap.containsKey(rootID)) {
                    dmsRoot = (DmsRoot) dmsRootMap.get(rootID);
                } else {
                    dmsRoot = rootManager.getRootObject(dmsDocument.getRootID());
                    if (!Utility.isEmpty(dmsRoot)) {
                        dmsRootMap.put(rootID, dmsRoot);
                    }
                }
                if (!Utility.isEmpty(dmsRoot)) {
                    String permission = permissionManager.getPermission(conn, GlobalConstant.OBJECT_TYPE_DOCUMENT, dmsDocument.getID(), dmsRoot.getRootType(), dmsRoot.getOwnerID());
                    if (!Utility.isEmpty(permission) && permission.indexOf("R") != -1) {
                        newResulstList.add(dmsDocument);
                    }
                }
            }
            if (!Utility.isEmpty(newResulstList)) {
                if (newResulstList.size() - startOffset >= pageSize) {
                    for (int i = startOffset - 1; i < startOffset - 1 + pageSize; i++) {
                        DmsDocument dmsDocument = (DmsDocument) newResulstList.get(i);
                        dmsDocument.setRowNum(i + 1);
                        dmsDocument.setRecordCount(newResulstList.size());
                        dmsDocument.setHasRelationship(docRetrievalManager.hasRelationship(dmsDocument.getID(), ""));
                        list.add(dmsDocument);
                    }
                } else {
                    for (int i = startOffset - 1; i < newResulstList.size(); i++) {
                        DmsDocument dmsDocument = (DmsDocument) newResulstList.get(i);
                        dmsDocument.setRowNum(i + 1);
                        dmsDocument.setRecordCount(newResulstList.size());
                        dmsDocument.setHasRelationship(docRetrievalManager.hasRelationship(dmsDocument.getID(), ""));
                        list.add(dmsDocument);
                    }
                }
                request.setAttribute(this.getListName(), list);
            } else {
                request.setAttribute(this.getListName(), new ArrayList());
            }
            documentDAO = null;
        } catch (ApplicationException appEx) {
            this.rollback(request);
            throw appEx;
        } catch (Exception e) {
            log.error(e, e);
            this.rollback(request);
            throw new ApplicationException(ErrorConstant.COMMON_FATAL_ERROR, e);
        } finally {
            sessionContainer = null;
            conn = null;
        }
    }

    /**
   *  Method deleteListData() - delete the item(s) from listing page
   *
   *  @param      mapping               ActionMapping from struts
   *  @param      form                  ActionForm from struts
   *  @param      request               HttpServletReuqest
   *  @param      respond               HttpServletRespond
   *  @return     void
   *  @throws     ApplicationException  Throws ApplicationException the deleteion failed
   */
    public void deleteListData(ActionMapping mapping, AbstractSearchForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String[] idAry = form.getBasicSelectedID();
        Connection conn = null;
        SessionContainer sessionContainer = null;
        DocumentOperationManager docOpManager = null;
        DocumentRetrievalManager docRetrievalManager = null;
        try {
            conn = this.getConnection(request);
            sessionContainer = this.getSessionContainer(request);
            docOpManager = new DocumentOperationManager(sessionContainer, conn);
            docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            PermissionManager permissionManager = sessionContainer.getPermissionManager();
            if (!Utility.isEmpty(idAry)) {
                for (int i = 0; i < idAry.length; i++) {
                    DmsDocument document = docRetrievalManager.getDocument(TextUtility.parseIntegerObj(idAry[i]));
                    RootRetrievalManager rootManager = new RootRetrievalManager(sessionContainer, conn);
                    DmsRoot root = rootManager.getRootObject(document.getRootID());
                    boolean havePermission = false;
                    boolean isCheckout = false;
                    if (DmsRoot.PERSONAL_ROOT.equals(root.getRootType())) {
                        if (sessionContainer.getUserRecordID().equals(document.getOwnerID())) {
                            havePermission = true;
                        } else {
                            havePermission = false;
                        }
                    } else {
                        String permission = permissionManager.getPermission(conn, GlobalConstant.OBJECT_TYPE_DOCUMENT, document.getID());
                        if (!Utility.isEmpty(permission)) {
                            havePermission = true;
                        }
                        if (DmsVersion.EXCLUSIVE_LOCK.equals(document.getItemStatus())) {
                            isCheckout = true;
                        }
                    }
                    if (havePermission == false) {
                        throw new ApplicationException(DmsErrorConstant.NO_PERMISSION_DELETE_DOC);
                    }
                    if (isCheckout == true) {
                        throw new ApplicationException(ErrorConstant.DMS_MESSAGE_CHECKOUT_NOT_DELETE);
                    }
                    if (document != null) {
                        docOpManager.deleteFolder(document);
                    }
                }
            }
        } catch (ApplicationException appEx) {
            throw appEx;
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            docOpManager.release();
            docRetrievalManager.release();
            sessionContainer = null;
            conn = null;
        }
    }

    public void deleteListData2(ActionMapping mapping, AbstractSearchForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        DocumentValidateManager docValidateManager = new DocumentValidateManager(sessionContainer, conn);
        DocumentOperationManager docOperationManager = new DocumentOperationManager(sessionContainer, conn);
        DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
        DmsDocumentDAObject dmsDocumentDAO = new DmsDocumentDAObject(sessionContainer, conn);
        ApplicationContainer container = (ApplicationContainer) request.getSession().getServletContext().getAttribute(GlobalConstant.APPLICATION_CONTAINER_KEY);
        String[] selectedID = form.getBasicSelectedID();
        boolean isIgnoreDeleteShortcut = ((ListDmsDocumentForm) form).isIgnoreDeleteShortcut();
        for (int n = 0; n < selectedID.length; n++) {
            Integer srcDocumentId = TextUtility.parseIntegerObj(selectedID[n]);
            DmsDocument sourceDocument = (DmsDocument) dmsDocumentDAO.getObjectByID(srcDocumentId);
            DmsValidation validation = docValidateManager.validateDeleteAction(sourceDocument, isIgnoreDeleteShortcut);
            String cacheKey = "D" + sessionContainer.getSessionID() + "" + System.currentTimeMillis();
            try {
                List deleteDocumentIds = validation.getLstIdAccept();
                boolean success = container.checkAndLockDocumentOperationID(cacheKey, deleteDocumentIds, "ACDIMU");
                if (!success) {
                    this.addError(request, "errors.dms.fail_to_delete", docRetrievalManager.getLocationPath(srcDocumentId));
                    this.addError(request, "errors.dms.cannot_edit_now");
                } else {
                    docOperationManager.deleteDocument(sourceDocument, validation);
                    List lstIdHasRight = validation.getLstIdAccept();
                    List lstIdMisRight = validation.getLstIdReject();
                    List lstIdBeLocked = validation.getLstIdlocked();
                    List lstIdBeDeleted = validation.getLstIdDeleted();
                    List lstIdBeArchived = validation.getLstIdArchived();
                    List lstIdRelationship = validation.getLstIdHaveRelationship();
                    String locationPath = docRetrievalManager.getLocationPath(srcDocumentId);
                    if (validation.isSuccess()) {
                        this.addMessage(request, "dms.message.document_deleted_success", locationPath, String.valueOf(lstIdHasRight.size()));
                    } else {
                        this.addError(request, "errors.dms.fail_to_delete", locationPath);
                        for (int i = 0; i < lstIdMisRight.size(); i++) {
                            this.addError(request, "errors.dms.no_permission", docRetrievalManager.getLocationPath((Integer) lstIdMisRight.get(i)));
                        }
                        for (int i = 0; i < lstIdBeLocked.size(); i++) {
                            this.addError(request, "errors.dms.been_checkout", docRetrievalManager.getLocationPath((Integer) lstIdBeLocked.get(i)));
                        }
                        for (int i = 0; i < lstIdBeDeleted.size(); i++) {
                            this.addError(request, "errors.dms.been_deleted", docRetrievalManager.getLocationPath((Integer) lstIdBeDeleted.get(i)));
                        }
                        for (int i = 0; i < lstIdBeArchived.size(); i++) {
                            this.addError(request, "errors.dms.been_archived", docRetrievalManager.getLocationPath((Integer) lstIdBeArchived.get(i)));
                        }
                        for (int i = 0; i < lstIdRelationship.size(); i++) {
                            this.addError(request, "errors.dms.has_relationship", docRetrievalManager.getLocationPath((Integer) lstIdRelationship.get(i)));
                        }
                        if (!Utility.isEmpty(lstIdHasRight)) {
                            this.addError(request, "dms.message.document_been_deleted", String.valueOf(lstIdHasRight.size()));
                        }
                    }
                    List clipboardList = (List) request.getSession().getAttribute("DMS_CLIPBOARD");
                    if (clipboardList != null && !clipboardList.isEmpty()) {
                        for (int i = 0; i < clipboardList.size(); i++) {
                            if (deleteDocumentIds.contains(clipboardList.get(i))) {
                                clipboardList.remove(i--);
                            }
                        }
                    }
                }
            } catch (ApplicationException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ApplicationException(ex);
            } finally {
                container.unlockDmsDocumentOperationID(cacheKey);
            }
        }
        conn = null;
        sessionContainer = null;
        container = null;
        docValidateManager.release();
        docOperationManager.release();
        docRetrievalManager.release();
    }
}
