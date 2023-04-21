package ces.research.oa.document.application.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.BaseException;
import ces.arch.form.BaseForm;
import ces.arch.form.EditForm;
import ces.coral.dbo.DBHandle;
import ces.platform.system.dbaccess.User;
import ces.research.oa.document.application.form.ApplicationForm;
import ces.research.oa.document.util.DateUtil;
import ces.research.oa.document.util.OAConstants;
import ces.research.oa.entity.ApplicationPojo;

/**
 * 
 * <p>
 * Title:��������
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class DocSaveAction extends ces.arch.action.SaveAction {

    public DocSaveAction() {
    }

    private Log log = LogFactory.getLog(this.getClass());

    protected void doSave(ActionMapping mapping, EditForm editForm, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) {
        Object bean = editForm.getBean();
        getBo().insertOrUpdate(bean);
        try {
            Long id = (Long) PropertyUtils.getSimpleProperty(bean, "id");
            request.setAttribute("id", id);
            log.debug("set id into request : " + editForm.getId());
        } catch (Exception e) {
            throw new BaseException("pojo����������������Ϊ��id ,����Ϊ��long", e);
        }
    }

    protected ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws Exception {
        User user = (User) request.getSession().getAttribute(OAConstants.ATTRIBUTE_USER);
        if (user == null) {
            return mapping.findForward(OAConstants.FORWARD_LOGIN);
        }
        String userName = user.getUserName();
        try {
            int userId = user.getUserID();
            ApplicationForm bargainform = (ApplicationForm) form;
            if (bargainform.getId().longValue() == 0) {
                ((ApplicationPojo) bargainform.getBean()).setRegisterUserId(userId);
                ((ApplicationPojo) bargainform.getBean()).setRegisterUserName(userName);
                ((ApplicationPojo) bargainform.getBean()).setRegisterUserDeptId(user.getOrgOfUser().getOrganizeID());
                ((ApplicationPojo) bargainform.getBean()).setRegisterUserDeptName(user.getOrgOfUser().getOrganizeName());
            }
            this.doSave(mapping, (EditForm) form, request, response, errors);
            DBHandle dbo = new DBHandle("oa");
            String sql = "";
            if (request.getParameter("reject") != null && !request.getParameter("reject").equals("")) {
                ((ApplicationPojo) bargainform.getBean()).setActivityStatus(Long.parseLong(request.getParameter("reject")));
            }
            if (((ApplicationPojo) bargainform.getBean()).getActivityStatus() > 0) {
                sql = "insert into t_oa_application_update " + "(id, register_user_id, register_user_name, register_user_dept_id, register_user_dept_name, register_time, activity_status) " + "values (messageid.nextval, " + userId + ", '" + userName + "', " + user.getOrgOfUser().getOrganizeID() + ", '" + user.getOrgOfUser().getOrganizeName() + "', sysdate, " + ((ApplicationPojo) bargainform.getBean()).getActivityStatus() + " )";
                dbo.execute(sql);
            }
            return mapping.findForward("applicationform");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex);
            request.setAttribute("Exception", ex);
            return mapping.findForward("fail");
        }
    }
}
