package ces.research.oa.document.incept.action;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.LoadAction;
import ces.arch.bo.BusinessException;
import ces.arch.form.BaseForm;
import ces.arch.form.EditForm;
import ces.arch.query.ListQury;
import ces.platform.system.dbaccess.Organize;
import ces.platform.system.dbaccess.User;
import ces.platform.system.facade.OrgUser;
import ces.research.oa.document.postil.bo.PostilBo;
import ces.research.oa.document.incept.form.InceptForm;
import ces.research.oa.document.util.OAConstants;
import ces.research.oa.entity.InceptPojo;
import ces.workflow.wapi.ClientAPI;
import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.WFFilter;
import ces.workflow.wapi.WFQuery;
import ces.workflow.wapi.Workitem;
import ces.workflow.wapi.status.WFStatus;

/**
 * 
 * <p>
 * Title: ������
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
public class DocLoadAction extends LoadAction {

    public DocLoadAction() {
    }

    private Log log = LogFactory.getLog(this.getClass());

    protected ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws Exception {
        User user = (User) request.getSession().getAttribute(OAConstants.ATTRIBUTE_USER);
        String userIdTemp = request.getParameter("userIdTemp") == null ? "" : request.getParameter("userIdTemp");
        if (user == null && userIdTemp != null && !"".equals(userIdTemp)) {
            user = new User(Integer.parseInt(userIdTemp));
        }
        request.setAttribute("userid", userIdTemp);
        if (user == null && userIdTemp == null) {
            return mapping.findForward(OAConstants.FORWARD_LOGIN);
        }
        if ("true".equals(request.getParameter("forview"))) {
            request.setAttribute("forview", "true");
        }
        if ("false".equals(request.getParameter("forview"))) {
            request.setAttribute("forview", "false");
        }
        try {
            String sign = request.getParameter("sign") == null ? "" : request.getParameter("sign");
            String userName = user.getUserName();
            Date dt = new Date();
            java.sql.Date date = new java.sql.Date(dt.getTime());
            InceptForm inceptform = (InceptForm) form;
            ClientAPI client = Coflow.getClientAPI(String.valueOf(user.getUserID()));
            Workitem workitem = null;
            if (request.getParameter("workitemId") != null && request.getParameter("autoCheckOut") != null && request.getParameter("autoCheckOut").equals("autoCheckOut")) {
                client.checkoutWorkitem(Long.parseLong(request.getParameter("workitemId")));
            }
            String myWorkItemId = request.getParameter("workitemId");
            if (myWorkItemId != null && !myWorkItemId.equals("")) inceptform.setWorkitemId(myWorkItemId);
            long processInstanceId;
            if (!"".equals(inceptform.getWorkitemId()) && inceptform.getWorkitemId() != null) {
                long workitemId = Long.parseLong(inceptform.getWorkitemId());
                workitem = client.getWorkitem(workitemId);
                processInstanceId = workitem.getProcessInstanceId();
                inceptform.setActivity(workitem.getActivityId());
            } else {
                processInstanceId = Long.parseLong(request.getParameter("processInstanceId"));
            }
            ListQury filter = new ListQury();
            filter.setQuery("from InceptPojo a where a.processInstanceId=" + processInstanceId);
            filter.setCursor(0);
            filter.setNumPerPage(1);
            filter.setTotalNum(1);
            List list = getBo().find(filter);
            if (list.size() == 0) {
                request.setAttribute("Exception", new Exception("�͸ù�������ص�ҵ����ݲ����ڣ����¼������ʵ��id:" + processInstanceId + "�������Ա��ϵ��"));
                return mapping.findForward("fail");
            }
            inceptform.setId(new Long(((InceptPojo) list.get(0)).getId()));
            inceptform.setBean(list.get(0));
            inceptform.setPostilList(new PostilBo().QueryPostil(((InceptPojo) inceptform.getBean()).getId(), "INCEPT", "drift"));
            ListQury lqury = new ListQury();
            lqury.setCursor(0);
            lqury.setNumPerPage(10);
            lqury.setTotalNum(10);
            lqury.setQuery("from PostilPojo where BUSINESS_ID= " + ((InceptPojo) inceptform.getBean()).getId() + " and MODULE_NAME='" + "INCEPT" + "' and POSTIL_TYPE in( '" + "deptdo" + "','" + "ldps" + "') order by postil_date asc, to_number(col1) asc");
            List ls = getBo().find(lqury);
            inceptform.setLeaderPostilList(ls);
            request.setAttribute("processInstanceId", String.valueOf(processInstanceId));
            if ("printoff".equals(request.getParameter("sign"))) {
                request.setAttribute("text", "text");
                String type = request.getParameter("printtype");
                if (type == null) {
                    return mapping.findForward("PRINTOFF");
                } else {
                    if (type.equals("0")) {
                        return mapping.findForward("PRINT16K");
                    } else {
                        return mapping.findForward("PRINTOFF");
                    }
                }
            }
            if ("dbview".equals(request.getParameter("sign"))) {
                request.setAttribute("dbview", "true");
                if (0 != processInstanceId) {
                    return mapping.findForward("workviewform");
                } else {
                    return mapping.findForward("viewDoc");
                }
            }
            if ("viewed".equals(request.getParameter("sign"))) {
                request.setAttribute("dbview", "false");
                if (0 != processInstanceId) {
                    return mapping.findForward("workviewform");
                } else {
                    return mapping.findForward("viewDoc");
                }
            }
            if (sign.equals("sign")) {
                if (workitem.getStatus() == WFStatus.WORKITEM_INITIALIZED.getValue()) {
                    client.checkoutWorkitem(workitem.getId());
                }
            }
            if ("checkOut".equals(request.getParameter("doAction"))) {
                if (workitem.getStatus() == WFStatus.WORKITEM_INITIALIZED.getValue()) {
                    client.checkoutWorkitem(workitem.getId());
                }
            } else if (workitem == null || workitem.getStatus() == WFStatus.WORKITEM_INITIALIZED.getValue()) {
                if (workitem != null) {
                    inceptform.setActivity(workitem.getActivityId());
                    if ("FINISH".equals(workitem.getActivityId()) || "ARCHIVE".equals(workitem.getActivityId()) || "ARCHIVE_FINISH".equals(workitem.getActivityId())) {
                        return mapping.findForward("finish");
                    }
                }
                if (0 != processInstanceId) {
                    return mapping.findForward("workviewform");
                } else {
                    return mapping.findForward("viewDoc");
                }
            } else if (workitem.getStatus() != WFStatus.WORKITEM_RUNNING.getValue()) {
                throw new Exception("�������Ѿ��������������ˢ���б�");
            } else if (workitem.getStatus() == WFStatus.WORKITEM_RUNNING.getValue() && "activity230".equals(workitem.getActivityId())) {
                WFQuery query = new WFQuery(WFQuery.ACTIVITY_ID, WFQuery.EQUALS, "activity227");
                query = new WFQuery(new WFQuery(WFQuery.PROCESS_INSTANCE_ID, WFQuery.EQUALS, workitem.getProcessInstanceId()), WFQuery.AND, query);
                query = new WFQuery(new WFQuery(WFQuery.STATUS, WFQuery.EQUALS, WFStatus.WORKITEM_COMPLETED.getValue()), WFQuery.AND, query);
                Workitem[] list2 = client.listWorkitems(new WFFilter(query));
                for (int i = 0; i < list2.length; i++) {
                    Workitem w = (Workitem) list2[i];
                    String uid = w.getOwnerId();
                    User u = new OrgUser().getUser(Integer.parseInt(uid));
                    Vector v = u.getParentOrg();
                    String oname = "";
                    if (v.size() > 0) {
                        Organize o = (Organize) v.get(0);
                        oname = o.getOrganizeName();
                    }
                }
            }
            inceptform.setActivity(workitem.getActivityId());
            if (0 != processInstanceId) {
                return mapping.findForward("workform");
            } else {
                return mapping.findForward("edit");
            }
        } catch (Exception ex) {
            log.error(ex);
            request.setAttribute("Exception", ex);
            return mapping.findForward("fail");
        }
    }

    protected void doLoad(ActionMapping mapping, EditForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws BusinessException {
        Long id = form.getId();
        Long theId = (Long) request.getAttribute("id");
        if (theId != null) {
            id = theId;
        }
        log.debug("form.getId : " + id);
        if (id.longValue() > 0) {
            Object bean = getBo().get(form.getBean().getClass(), id);
            form.setBean(bean);
            form.setId(id);
        }
    }
}
