package com.dcivision.dms.web;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;
import com.dcivision.dms.bean.DmsDocument;
import com.dcivision.dms.core.DocumentOperationManager;
import com.dcivision.dms.dao.DmsDocumentDAObject;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractListAction;
import com.dcivision.framework.web.AbstractSearchForm;

/**
 * <p>Class Name:       ListDmsPublicDocumentAction.java    </p>
 * <p>Description:      The list action class for ListUserRecord.jsp</p>
 * @author              Zoe Shum
 * @company             DCIVision Limited
 * @creation date       01/08/2003
 * @version             $Revision: 1.9.2.1 $
 */
public class ListDmsEmptyDocumentAction extends AbstractListAction {

    public static final String REVISION = "$Revision: 1.9.2.1 $";

    /**
   *  Constructor - Creates a new instance of ListDmsPublicDocumentAction and define the default listName.
   */
    public ListDmsEmptyDocumentAction() {
        super();
        this.setListName("dmsEmptyDocumentList");
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
        return (SystemFunctionConstant.DMS_EMPTY_FILE);
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
        if (!Utility.isEmpty(request.getSession().getAttribute(org.apache.struts.Globals.MESSAGE_KEY))) {
            this.addMessage(request, (String) request.getSession().getAttribute(org.apache.struts.Globals.MESSAGE_KEY));
            request.getSession().removeAttribute(org.apache.struts.Globals.MESSAGE_KEY);
        }
        if (!Utility.isEmpty(request.getSession().getAttribute(org.apache.struts.Globals.ERROR_KEY))) {
            this.addError(request, (String) request.getSession().getAttribute(org.apache.struts.Globals.ERROR_KEY));
            request.getSession().removeAttribute(org.apache.struts.Globals.ERROR_KEY);
        }
        List resultList = new ArrayList();
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        ListDmsEmptyDocumentForm docSearchForm = (ListDmsEmptyDocumentForm) form;
        try {
            DmsDocumentDAObject documentDAO = new DmsDocumentDAObject(sessionContainer, conn);
            resultList = documentDAO.getEmptyDocumentList(form);
            request.setAttribute(this.m_sListName, resultList);
            sessionContainer = null;
            documentDAO = null;
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
        Connection conn = this.getConnection(request);
        try {
            DocumentOperationManager docOpManager = new DocumentOperationManager(this.getSessionContainer(request), conn);
            DmsDocumentDAObject dmsDocDAO = new DmsDocumentDAObject(this.getSessionContainer(request), conn);
            if (!Utility.isEmpty(idAry)) {
                for (int i = 0; i < idAry.length; i++) {
                    DmsDocument dmsDoc = (DmsDocument) dmsDocDAO.getObjectByID(new Integer(idAry[i]));
                    docOpManager.deleteDocument(dmsDoc);
                }
            }
            docOpManager.release();
            dmsDocDAO = null;
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            conn = null;
        }
    }
}
