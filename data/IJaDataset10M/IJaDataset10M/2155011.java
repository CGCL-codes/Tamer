package com.bugfree4j.app.security.admin.action;

import com.bugfree4j.app.security.admin.form.BugProjectFormBean;
import com.bugfree4j.app.security.admin.form.GroupFormBean;
import com.bugfree4j.dao.BugprojectDAO;
import com.bugfree4j.dao.DAOConstants;
import com.bugfree4j.dao.SysGroupDAO;
import com.bugfree4j.per.common.DAOException;
import com.bugfree4j.per.common.web.struts.BaseAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DOCUMENT ME!
 *
 * @author $Author: cvs $
 * @version $Revision: 1.1 $
  */
public class DelGroup extends BaseAction {

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GroupFormBean fm = (GroupFormBean) form;
        ActionErrors errors = new ActionErrors();
        try {
            SysGroupDAO groupDao = (SysGroupDAO) this.getBean(DAOConstants.SYSGROUP_DAO);
            groupDao.delete(fm.getGroupid());
        } catch (DAOException e) {
            e.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.check.invalid", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.check.invalid", e.getMessage()));
        }
        if (!errors.isEmpty()) {
            this.saveErrors(request, errors);
            return mapping.getInputForward();
        }
        return mapping.findForward("success");
    }
}
