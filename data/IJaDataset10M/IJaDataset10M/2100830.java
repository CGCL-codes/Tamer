package com.dcivision.dms.web;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.dms.DmsErrorConstant;
import com.dcivision.dms.DmsOperationConstant;
import com.dcivision.dms.bean.DmsDocument;
import com.dcivision.dms.bean.DmsVersion;
import com.dcivision.dms.bean.MtmDocumentRelationship;
import com.dcivision.dms.core.DocumentOperationManager;
import com.dcivision.dms.core.DocumentRetrievalManager;
import com.dcivision.dms.core.DocumentValidateManager;
import com.dcivision.dms.dao.DmsDocumentDAObject;
import com.dcivision.dms.dao.DmsRootDAObject;
import com.dcivision.dms.dao.MtmDocumentRelationshipDAObject;
import com.dcivision.framework.ApplicationContainer;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractListAction;
import com.dcivision.framework.web.AbstractSearchForm;

/**
 * <p>Class Name:       ListDmsRecycleBinAction.java    </p>
 * <p>Description:      The list action class for ListUserRecord.jsp</p>
 * @author              Zoe Shum
 * @company             DCIVision Limited
 * @creation date       01/08/2003
 * @version             $Revision: 1.27.2.4 $
 */
public class ListDmsRecycleBinAction extends AbstractListAction {

    public static final String REVISION = "$Revision: 1.27.2.4 $";

    protected String MESSAGE_RECORDS_RESTORE = "common.message.records_restore";

    protected String MESSAGE_RECORDS_RESTORE_1 = "common.message.records_restore_1";

    protected String MESSAGE_RECORDS_RESTORE_2 = "common.message.records_restore_2";

    protected String MESSAGE_RECORDS_RESTORE_3 = "common.message.records_restore_3";

    protected String MESSAGE_RECORDS_HARD_DELETE = "common.message.records_deleted";

    protected String ERROR_RECORD_DUPLICATE = "errors.dms.restore_for_duplicate_record";

    protected String ERROR_RESOTRE_NOT_SUCCESS = "errors.dms.record_cannot_be_restore";

    protected String ERROR_RECORD_PARENT_DUPLICATE = "errors.dms.restore_for_parent_duplicate_record";

    protected String ERROR_RECORD_ANCESTOR_DUPLICATE = "errors.dms.restore_for_ancestor_duplicate_record";

    protected String ERROR_CAN_NOT_FIND_TARGET_DOCUMENT = "errors.dms.can_not_find_target_document";

    protected int errorRestoreNum = 0;

    protected int successRestoreDocumentNum = 0;

    protected int successRestoreFolderNum = 0;

    /**
	 * Constructor - Creates a new instance of ListDmsPublicDocumentAction and
	 * define the default listName.
	 */
    public ListDmsRecycleBinAction() {
        super();
        this.setListName("dmsRecycleBinList");
    }

    /**
	 * getMajorDAOClassName
	 * 
	 * @return The class name of the major DAObject will be used in this action.
	 */
    public String getMajorDAOClassName() {
        return ("com.dcivision.dms.dao.DmsDocumentDAObject");
    }

    /**
	 * getFunctionCode
	 * 
	 * @return The corresponding system function code of action.
	 */
    public String getFunctionCode() {
        return (SystemFunctionConstant.DMS_RECYCLE_BIN);
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ListDmsDocumentForm docSearchForm = (ListDmsDocumentForm) form;
        log.info(this.getClass().getName() + ", OP Mode:" + docSearchForm.getOpMode() + ", Nav Mode:" + docSearchForm.getNavMode());
        ActionForward forward = this.retrieveFunctionCode(request, response, mapping);
        if (forward != null) {
            return forward;
        }
        if (Utility.isEmpty(docSearchForm.getRootIDAry())) {
            String rootIDAry = request.getParameter("rootIDAry");
            docSearchForm.setRootIDAry(rootIDAry);
        }
        String pageOffset = SystemParameterFactory.getSystemParameter(SystemParameterConstant.PAGE_OFF_SET);
        if (((DmsOperationConstant.RESTORE_DOCUMENT).equals(docSearchForm.getOpMode())) || ((DmsOperationConstant.HARD_DELETE_DOCUMENT).equals(docSearchForm.getOpMode()))) {
            try {
                if ((DmsOperationConstant.RESTORE_DOCUMENT).equals(docSearchForm.getOpMode())) {
                    this.regeditDuplicateNameCache(request);
                    this.restoreListData(mapping, docSearchForm, request, response);
                    this.commit(request);
                    if (successRestoreDocumentNum > 0) {
                        if (successRestoreFolderNum > 0) {
                            addMessage(request, MESSAGE_RECORDS_RESTORE_1, "" + successRestoreFolderNum, "" + successRestoreDocumentNum);
                        } else {
                            addMessage(request, MESSAGE_RECORDS_RESTORE_2, "" + successRestoreDocumentNum);
                        }
                    } else if (successRestoreFolderNum > 0) {
                        addMessage(request, MESSAGE_RECORDS_RESTORE_3, "" + successRestoreFolderNum);
                    }
                    if (errorRestoreNum > 0) {
                        addError(request, ERROR_RESOTRE_NOT_SUCCESS, "" + errorRestoreNum);
                    }
                } else if ((DmsOperationConstant.HARD_DELETE_DOCUMENT).equals(docSearchForm.getOpMode())) {
                    this.hardDeleteListData(mapping, docSearchForm, request, response);
                    this.commit(request);
                    addMessage(request, MESSAGE_RECORDS_HARD_DELETE, "" + docSearchForm.getBasicSelectedID().length);
                }
                docSearchForm.setBasicSelectedID(null);
                docSearchForm.setOpMode(null);
                this.getListData(mapping, docSearchForm, request, response);
                List resultList = (List) request.getAttribute(this.m_sListName);
                if ((Utility.isEmpty(resultList)) && (TextUtility.parseInteger(docSearchForm.getCurStartRowNo()) > 10)) {
                    int prvRowNo = TextUtility.parseInteger(docSearchForm.getCurStartRowNo()) - TextUtility.parseInteger(pageOffset);
                    docSearchForm.setCurStartRowNo(TextUtility.formatInteger(prvRowNo));
                    this.getListData(mapping, docSearchForm, request, response);
                }
            } catch (ApplicationException e) {
                this.rollback(request);
                this.handleApplicationException(request, e);
                request.setAttribute(this.m_sListName, new ArrayList());
            } finally {
                this.unlockDuplicateNameCache(request);
            }
            return mapping.findForward(GlobalConstant.NAV_MODE_LIST);
        } else if ("EMPTY_RECYCLE_BIN".equals(docSearchForm.getOpMode())) {
            try {
                emptyRecycleBin(mapping, docSearchForm, request, response);
                this.commit(request);
            } catch (ApplicationException e) {
                this.rollback(request);
                this.handleApplicationException(request, e);
            } catch (Exception e) {
            }
        }
        return super.execute(mapping, form, request, response);
    }

    /**
	 * getListData
	 * 
	 * Override the parent's function. Purpose in create the default personal
	 * folder when non-exists, and load the dmsDocument list.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws ApplicationException
	 */
    public void getListData(ActionMapping mapping, AbstractSearchForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        ListDmsPublicDocumentForm docSearchForm = (ListDmsPublicDocumentForm) form;
        DmsDocumentDAObject docDAO = new DmsDocumentDAObject(sessionContainer, conn);
        List resultList = new ArrayList();
        boolean accessPublicFolder = SystemParameterFactory.getSystemParameterBoolean(SystemParameterConstant.DMS_RECYCLE_BIN_ACCESS_PUBLIC_FOLDER);
        try {
            if (Utility.isEmpty(docSearchForm.getRootIDAry())) {
                if (accessPublicFolder) {
                    resultList = docDAO.getDeletedDocumentList(docSearchForm);
                } else {
                    resultList = docDAO.getDeletedPersonalDocumentList(docSearchForm);
                }
            } else {
                resultList = docDAO.getDeletedDocumentList(docSearchForm);
            }
            request.setAttribute(this.m_sListName, resultList);
            sessionContainer = null;
            docDAO = null;
        } catch (ApplicationException appEx) {
            this.rollback(request);
            throw appEx;
        } catch (Exception e) {
            log.error(e, e);
            this.rollback(request);
            throw new ApplicationException(ErrorConstant.COMMON_FATAL_ERROR, e);
        } finally {
            conn = null;
        }
    }

    /**
	 * Method restoreListData() - delete the item(s) from listing page for
	 * restore action
	 * 
	 * @param mapping
	 *            ActionMapping from struts
	 * @param form
	 *            ActionForm from struts
	 * @param request
	 *            HttpServletReuqest
	 * @param respond
	 *            HttpServletRespond
	 * @return void
	 * @throws ApplicationException
	 *             Throws ApplicationException the deleteion failed
	 */
    public void restoreListData(ActionMapping mapping, AbstractSearchForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        try {
            DocumentOperationManager docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            DocumentValidateManager docValidateManager = new DocumentValidateManager(sessionContainer, conn);
            DmsDocumentDAObject dmsDocumentDAO = new DmsDocumentDAObject(sessionContainer, conn);
            MtmDocumentRelationship relationshipObj = new MtmDocumentRelationship();
            MtmDocumentRelationshipDAObject mtmDocumentRelationshipDAObject = new MtmDocumentRelationshipDAObject(sessionContainer, conn);
            List relationshipList = new ArrayList();
            List indirectRelationDocumentList = new ArrayList();
            MtmDocumentRelationshipDAObject dmsMtmDocumentRelationshipDAO = new MtmDocumentRelationshipDAObject(sessionContainer, conn);
            String[] prepSelectedID = form.getBasicSelectedID();
            List tempSelectedIDsList = new ArrayList();
            List preSelectedIDsList = new ArrayList();
            errorRestoreNum = 0;
            successRestoreDocumentNum = 0;
            successRestoreFolderNum = 0;
            for (int i = 0; i < prepSelectedID.length; i++) {
                if (!Utility.isEmpty(prepSelectedID[i])) {
                    if (!tempSelectedIDsList.contains(prepSelectedID[i])) {
                        preSelectedIDsList.add(prepSelectedID[i]);
                    }
                    DmsDocument document = docRetrievalManager.getDocument(TextUtility.parseIntegerObj(prepSelectedID[i]));
                    if (DmsDocument.FOLDER_TYPE.equals(document.getDocumentType()) || DmsDocument.COMPOUND_DOC_TYPE.equals(document.getDocumentType())) {
                        List temp = this.getAllTreeNodeID(dmsDocumentDAO, document.getID());
                        for (int j = 0; j < temp.size(); j++) {
                            if (!preSelectedIDsList.contains(temp.get(j))) {
                                preSelectedIDsList.add(temp.get(j));
                            }
                        }
                    }
                }
            }
            String[] sID = new String[preSelectedIDsList.size()];
            for (int i = 0; i < preSelectedIDsList.size(); i++) {
                sID[i] = (String) preSelectedIDsList.get(i);
            }
            for (int i = 0; i < sID.length; i++) {
                if (!Utility.isEmpty(sID[i])) {
                    if (!tempSelectedIDsList.contains(sID[i])) {
                        tempSelectedIDsList.add(sID[i]);
                    }
                    DmsDocument document = docRetrievalManager.getDocument(TextUtility.parseIntegerObj(sID[i]));
                    if (!Utility.isEmpty(document) && DmsDocument.DOCUMENT_LINK.equals(document.getDocumentType())) {
                        List tmpIndirectRelationDocumentList = mtmDocumentRelationshipDAObject.getInactiveListByRelationID(document.getID(), DmsDocument.DOCUMENT_LINK, GlobalConstant.RECORD_STATUS_INACTIVE);
                        if (!tmpIndirectRelationDocumentList.isEmpty()) {
                            MtmDocumentRelationship tmpMtmDocumentRelationship = (MtmDocumentRelationship) tmpIndirectRelationDocumentList.get(0);
                            DmsDocument targetDocument = docRetrievalManager.getDocument(tmpMtmDocumentRelationship.getDocumentID());
                            if (GlobalConstant.RECORD_STATUS_INACTIVE.equals(targetDocument.getRecordStatus())) {
                                if (!targetDocument.getUpdaterID().equals(sessionContainer.getUserRecordID())) {
                                    tempSelectedIDsList.remove(sID[i]);
                                    addError(request, ERROR_CAN_NOT_FIND_TARGET_DOCUMENT, document.getDocumentName());
                                    errorRestoreNum++;
                                } else {
                                    if (!tempSelectedIDsList.contains(tmpMtmDocumentRelationship.getDocumentID().toString())) {
                                        tempSelectedIDsList.add(tmpMtmDocumentRelationship.getDocumentID().toString());
                                    }
                                }
                            }
                        }
                    } else if (!Utility.isEmpty(document) && !DmsDocument.DOCUMENT_LINK.equals(document.getDocumentType()) && "Y".equals(request.getParameter("isRestoreAll"))) {
                        List tmpRelationshipList = mtmDocumentRelationshipDAObject.getInactiveListByID(document.getID(), DmsDocument.DOCUMENT_LINK, GlobalConstant.RECORD_STATUS_INACTIVE);
                        if (!tmpRelationshipList.isEmpty()) {
                            for (int j = 0; j < tmpRelationshipList.size(); j++) {
                                MtmDocumentRelationship tmpRelationship = (MtmDocumentRelationship) tmpRelationshipList.get(j);
                                if (DmsDocument.DOCUMENT_LINK.equals(tmpRelationship.getRelationshipType())) {
                                    if (!tempSelectedIDsList.contains(tmpRelationship.getRelatedDocumentID().toString())) {
                                        DmsDocument scDocument = (DmsDocument) dmsDocumentDAO.getDocumentByID(tmpRelationship.getRelatedDocumentID(), null);
                                        if (!Utility.isEmpty(scDocument) && scDocument.getUpdaterID().equals(sessionContainer.getUserRecordID())) {
                                            tempSelectedIDsList.add(tmpRelationship.getRelatedDocumentID().toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String[] selectedID = new String[tempSelectedIDsList.size()];
            for (int i = 0; i < tempSelectedIDsList.size(); i++) {
                selectedID[i] = (String) tempSelectedIDsList.get(i);
            }
            for (int i = 0; i < selectedID.length; i++) {
                if (!Utility.isEmpty(selectedID[i])) {
                    DmsDocument document = docRetrievalManager.getDocument(TextUtility.parseIntegerObj(selectedID[i]));
                    DmsDocument parentDocument = null;
                    parentDocument = (DmsDocument) dmsDocumentDAO.getInactiveByID(document.getParentID());
                    if (GlobalConstant.RECORD_STATUS_INACTIVE.equals(document.getRecordStatus())) {
                        if (docValidateManager.checkNameExistence(document)) {
                            addError(request, ERROR_RECORD_DUPLICATE, "" + document.getDocumentName());
                            errorRestoreNum++;
                        } else {
                            if (parentDocument != null && (docValidateManager.checkNameExistence(parentDocument))) {
                                String documentType = "";
                                if (DmsDocument.FOLDER_TYPE.equals(parentDocument.getDocumentType())) {
                                    documentType = "Parent Folder";
                                } else if (DmsDocument.COMPOUND_DOC_TYPE.equals(parentDocument.getDocumentType())) {
                                    documentType = "Parent Document";
                                }
                                addError(request, ERROR_RECORD_PARENT_DUPLICATE, document.getDocumentName(), documentType + " " + parentDocument.getDocumentName());
                                errorRestoreNum++;
                            } else if (parentDocument != null && (docValidateManager.checkAncestorNameExistence(parentDocument))) {
                                addError(request, ERROR_RECORD_ANCESTOR_DUPLICATE, document.getDocumentName());
                                errorRestoreNum++;
                            } else {
                                log.debug("No duplicate name exists");
                                docOperationManager.restoreDocument(document, true, "Y".equals(request.getParameter("isRestoreAll")));
                                if (DmsDocument.FOLDER_TYPE.equals(document.getDocumentType())) {
                                    successRestoreFolderNum++;
                                } else {
                                    successRestoreDocumentNum++;
                                }
                            }
                        }
                    }
                }
            }
            conn.commit();
            docOperationManager.release();
            docRetrievalManager.release();
            docValidateManager.release();
            sessionContainer = null;
        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(ErrorConstant.COMMON_FATAL_ERROR, e);
        } finally {
            conn = null;
        }
    }

    /**
	 * Method hardDeleteListData() - delete the item(s) from listing page for
	 * hard delete action
	 * 
	 * @param mapping
	 *            ActionMapping from struts
	 * @param form
	 *            ActionForm from struts
	 * @param request
	 *            HttpServletReuqest
	 * @param respond
	 *            HttpServletRespond
	 * @return void
	 * @throws ApplicationException
	 *             Throws ApplicationException the deleteion failed
	 */
    public void hardDeleteListData(ActionMapping mapping, AbstractSearchForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        try {
            DocumentOperationManager docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            DmsRootDAObject dmsRootDAO = new DmsRootDAObject(sessionContainer, conn);
            String[] selectedID = form.getBasicSelectedID();
            for (int i = 0; i < selectedID.length; i++) {
                if (!Utility.isEmpty(selectedID[i])) {
                    if (!docRetrievalManager.checkDocIsHardDeleted(TextUtility.parseIntegerObj(selectedID[i]))) {
                        DmsDocument document = docRetrievalManager.getDocument(TextUtility.parseIntegerObj(selectedID[i]));
                        Integer rootID = null;
                        if (document.getParentID().intValue() == 0) {
                            rootID = document.getRootID();
                        }
                        docOperationManager.hardDeleteDocument(document);
                        if (rootID != null) {
                            if (!docRetrievalManager.checkRootIsHardDeleted(rootID)) {
                                dmsRootDAO.hardDelete(rootID);
                            }
                        }
                    }
                }
            }
            docOperationManager.release();
            docRetrievalManager.release();
            sessionContainer = null;
        } catch (ApplicationException aex) {
            addError(request, DmsErrorConstant.PHYSICAL_FILE_NOT_FOUND);
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(ErrorConstant.COMMON_FATAL_ERROR, e);
        } finally {
            conn = null;
        }
    }

    /**
	 * Method emptyRecycleBin() - delete all the item(s) of the recycle bin
	 * 
	 * @param mapping
	 *            ActionMapping from struts
	 * @param form
	 *            ActionForm from struts
	 * @param request
	 *            HttpServletReuqest
	 * @param respond
	 *            HttpServletRespond
	 * @return void
	 * @throws ApplicationException
	 *             Throws ApplicationException the deleteion failed
	 */
    public void emptyRecycleBin(ActionMapping mapping, AbstractSearchForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        ListDmsPublicDocumentForm docSearchForm = (ListDmsPublicDocumentForm) form;
        DmsDocumentDAObject docDAO = new DmsDocumentDAObject(sessionContainer, conn);
        List resultList = new ArrayList();
        boolean accessPublicFolder = SystemParameterFactory.getSystemParameterBoolean(SystemParameterConstant.DMS_RECYCLE_BIN_ACCESS_PUBLIC_FOLDER);
        try {
            if (Utility.isEmpty(docSearchForm.getRootIDAry())) {
                if (accessPublicFolder) {
                    resultList = docDAO.getAllDeletedDocumentList();
                } else {
                    resultList = docDAO.getAllDeletedPersonalDocumentList();
                }
            } else {
                resultList = docDAO.getDeletedDocumentList(docSearchForm);
            }
            DocumentOperationManager docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
            DmsRootDAObject dmsRootDAO = new DmsRootDAObject(sessionContainer, conn);
            for (int i = 0; i < resultList.size(); i++) {
                DmsDocument document = (DmsDocument) resultList.get(i);
                if (!Utility.isEmpty(document)) {
                    if (!docRetrievalManager.checkDocIsHardDeleted(document.getID())) {
                        Integer rootID = null;
                        if (document.getParentID().intValue() == 0) {
                            rootID = document.getRootID();
                        }
                        docOperationManager.hardDeleteDocument(document);
                        if (rootID != null) {
                            if (!docRetrievalManager.checkRootIsHardDeleted(rootID)) {
                                dmsRootDAO.hardDelete(rootID);
                            }
                        }
                    }
                }
            }
            docOperationManager.release();
            docRetrievalManager.release();
            sessionContainer = null;
        } catch (ApplicationException aex) {
            addError(request, DmsErrorConstant.PHYSICAL_FILE_NOT_FOUND);
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(ErrorConstant.COMMON_FATAL_ERROR, e);
        } finally {
            conn = null;
        }
    }

    /**
   * resolve the problem duplicate name
   * EIP-438 06/12/29 LEE 
   * @param request
   */
    public void regeditDuplicateNameCache(HttpServletRequest request) {
        ApplicationContainer application = this.getSessionContainer(request).getAppContainer();
        if (application != null) {
            application.regeditDuplicateNameCache();
        }
    }

    /**
   * resolve the problem duplicate name
   * EIP-438 06/12/29 LEE 
   * @param request
   */
    public void unlockDuplicateNameCache(HttpServletRequest request) {
        ApplicationContainer application = this.getSessionContainer(request).getAppContainer();
        if (application != null) {
            application.unlockDuplicateNameCache();
        }
    }

    private List getAllTreeNodeID(DmsDocumentDAObject dmsDocumentDAO, Integer ParentId) throws Exception {
        List folderIdList = new ArrayList();
        folderIdList.add(ParentId.toString());
        String[] fieldNames = new String[] { "D.ID", "D.DOCUMENT_TYPE" };
        String[] fieldTypes = new String[] { "Integer", "String" };
        String[] itemStatus = new String[] { DmsVersion.ARCHIVED_STATUS };
        String[] recordStatus = new String[] { GlobalConstant.RECORD_STATUS_INACTIVE };
        String orderBy = "D.DOCUMENT_TYPE, D.ID";
        folderIdList.addAll(this.getChildrenIdRecursive(dmsDocumentDAO, fieldNames, fieldTypes, ParentId, itemStatus, recordStatus, orderBy));
        return folderIdList;
    }

    private List getChildrenIdRecursive(DmsDocumentDAObject dmsDocumentDAO, String[] fieldNames, String[] fieldTypes, Integer parentId, String[] itemStatus, String[] recordStatus, String orderBy) throws Exception {
        List lstChildDocumentId = new ArrayList();
        List listFieldObject = dmsDocumentDAO.getFieldListByParentId(fieldNames, fieldTypes, parentId, itemStatus, false, recordStatus, true, null, null, null, orderBy);
        for (int i = 0; i < listFieldObject.size(); i++) {
            Object[] fields = (Object[]) listFieldObject.get(i);
            Integer documentId = (Integer) fields[0];
            String documentType = (String) fields[1];
            lstChildDocumentId.add(documentId.toString());
            if (!DmsDocument.DOCUMENT_TYPE.equals(documentType)) {
                lstChildDocumentId.addAll(this.getChildrenIdRecursive(dmsDocumentDAO, fieldNames, fieldTypes, documentId, itemStatus, recordStatus, orderBy));
            }
        }
        return lstChildDocumentId;
    }
}
