package cn.ac.ntarl.umt.action.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.action.AbstractAction;
import cn.ac.ntarl.umt.actions.group.DBGetGroup;
import cn.ac.ntarl.umt.actions.request.DBCreateReuqest;
import cn.ac.ntarl.umt.actions.request.DBGetRequest;
import cn.ac.ntarl.umt.actions.request.DBReadRequests;
import cn.ac.ntarl.umt.actions.request.DBSaveRequest;
import cn.ac.ntarl.umt.database.Database;
import cn.ac.ntarl.umt.form.SubGroupForm;
import cn.ac.ntarl.umt.request.DBRequest;
import cn.ac.ntarl.umt.security.Sessions;
import cn.ac.ntarl.umt.subgroup.SubGroupRequest;
import cn.ac.ntarl.umt.tree.DBGroupTree;
import cn.ac.ntarl.umt.user.Group;
import cn.ac.ntarl.umt.utils.MessageBean;

/**
 * MyEclipse Struts Creation date: 12-24-2007
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/request/subGroup" name="subGroupForm"
 *                input="/request/subGroup.jsp" scope="request" validate="true"
 */
public class SubGroupAction extends AbstractAction {

    @Override
    protected ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) {
        SubGroupForm sub = (SubGroupForm) form;
        try {
            if (sub.isLoading()) {
                return onLoad(mapping, form, request, response, sm);
            }
            if (sub.isAccept()) {
                return onAccept(mapping, form, request, response, sm);
            }
            if (sub.isDeny()) {
                return onDeny(mapping, form, request, response, sm);
            }
            if (sub.isCreateReqeust()) {
                return onCreate(mapping, form, request, response, sm);
            }
            if (sub.isDelete()) {
                return onDelete(mapping, form, request, response, sm);
            }
            return MessageBean.findMessageForward(request, mapping, "����Ĳ��������޷��ҵ�����Ĳ���������ϵ����Ա");
        } catch (CLBException e) {
            return MessageBean.findExceptionForward(request, mapping, e);
        }
    }

    private ActionForward onCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) throws CLBException {
        SubGroupForm sub = (SubGroupForm) form;
        Group subgroup = (Group) Database.perform(new DBGetGroup(sub.getSubGroupID()));
        Group superGroup = (Group) Database.perform(new DBGetGroup(sub.getGroupID()));
        if (!DBGroupTree.getGroupTree().checkCircle(superGroup.getName(), new String[] { subgroup.getName() })) {
            SubGroupRequest req = new SubGroupRequest(subgroup, superGroup);
            req.create();
            Database.perform(new DBCreateReuqest(req));
            return MessageBean.findMessageForward(request, mapping, "�������ύ");
        } else {
            return MessageBean.findMessageForward(request, mapping, "�޷�������룬������ĸ����ϵ���γ�ѭ��������ϵ");
        }
    }

    private ActionForward onDeny(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) throws CLBException {
        SubGroupForm sub = (SubGroupForm) form;
        DBRequest req = (DBRequest) Database.perform(new DBGetRequest(sub.getRequestId()));
        req.request.deny();
        Database.perform(new DBSaveRequest(req));
        return MessageBean.findMessageForward(request, mapping, "���������Ѿܾ�");
    }

    private ActionForward onDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) throws CLBException {
        SubGroupForm sub = (SubGroupForm) form;
        DBRequest req = (DBRequest) Database.perform(new DBGetRequest(sub.getRequestId()));
        req.request.delete();
        Database.perform(new DBSaveRequest(req));
        return MessageBean.findMessageForward(request, mapping, "���������ѱ�ɾ��");
    }

    private ActionForward onAccept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) throws CLBException {
        SubGroupForm sub = (SubGroupForm) form;
        DBRequest req = (DBRequest) Database.perform(new DBGetRequest(sub.getRequestId()));
        if (req != null) {
            req.request.accept();
            Database.perform(new DBSaveRequest(req));
            return MessageBean.findMessageForward(request, mapping, "�����ϵ�ѳɹ����");
        } else return MessageBean.findMessageForward(request, mapping, "������������޷��ҵ�ָ����������Ϣ");
    }

    private ActionForward onLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) throws CLBException {
        SubGroupForm sub = (SubGroupForm) form;
        if (sm.ExistCurrentPosition(sub.getGroupID(), "�����Ա")) {
            DBRequest[] requests;
            requests = (DBRequest[]) Database.perform(new DBReadRequests(sub.getGroupID(), SubGroupRequest.REQUEST_TYPE));
            request.setAttribute("Requests", requests);
            return mapping.getInputForward();
        } else {
            return MessageBean.findMessageForward(request, mapping, "���������Ĺ���Ա���޷���������й���");
        }
    }
}
