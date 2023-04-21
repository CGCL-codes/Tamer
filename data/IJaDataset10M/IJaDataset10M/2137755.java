package cn.ac.ntarl.umt.action.user;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.action.AbstractAction;
import cn.ac.ntarl.umt.actions.group.DBGetGroup;
import cn.ac.ntarl.umt.actions.user.DBDeleteGroupChilds;
import cn.ac.ntarl.umt.actions.user.DBListGroupChild;
import cn.ac.ntarl.umt.database.Database;
import cn.ac.ntarl.umt.form.DeleteGroupChildForm;
import cn.ac.ntarl.umt.security.Sessions;
import cn.ac.ntarl.umt.tree.DBGroupTree;
import cn.ac.ntarl.umt.tree.GroupTree;
import cn.ac.ntarl.umt.user.Group;
import cn.ac.ntarl.umt.user.GroupRelation;
import cn.ac.ntarl.umt.utils.MessageBean;

/** 
 * MyEclipse Struts
 * Creation date: 06-29-2007
 * 
 * XDoclet definition:
 * @struts.action path="/createGroup" name="createGroupForm" input="/user/inputGroup.jsp" scope="request" validate="true"
 */
public class DeleteGroupChildAction extends AbstractAction {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) {
        DeleteGroupChildForm deleteGroupRelationForm = (DeleteGroupChildForm) form;
        if (!(Sessions.IsCurrentPosition(request, 1, "�����Ա")) && !(Sessions.IsCurrentPosition(request, deleteGroupRelationForm.getGroupID(), "�����Ա"))) {
            return MessageBean.findMessageForward(request, mapping, "��ǰ�û�Ȩ�޲��㣬�޷���������������µ�¼��");
        }
        if (deleteGroupRelationForm.isInit()) {
            try {
                Group g = (Group) Database.perform(new DBGetGroup(deleteGroupRelationForm.getGroupID()));
                List childs = (List) Database.perform(new DBListGroupChild(deleteGroupRelationForm.getGroupID()));
                if (g != null) {
                    request.setAttribute("group", g);
                    request.setAttribute("oldgroupchild", childs);
                    log.info("delete child group init successful!");
                    return mapping.getInputForward();
                } else {
                    log.warn("delete child group init fail: can not find group or child group");
                    return MessageBean.findMessageForward(request, mapping, "�޷��ҵ���" + deleteGroupRelationForm.getGroupID());
                }
            } catch (CLBException e) {
                log.warn("delete child group init error:" + e.getMessage());
                return MessageBean.findExceptionForward(request, mapping, e);
            }
        } else {
            try {
                int groupid = deleteGroupRelationForm.getGroupID();
                String sid[] = request.getParameterValues("check2");
                ArrayList childids = null;
                ArrayList deleterelation = new ArrayList();
                if (sid != null && sid.length > 0) {
                    childids = new ArrayList();
                    for (int i = 0; i < sid.length; i++) {
                        try {
                            int cid = Integer.parseInt(sid[i].trim());
                            if (cid > 0) {
                                GroupRelation gr1 = new GroupRelation(String.valueOf(groupid), String.valueOf(cid));
                                deleterelation.add(gr1);
                                childids.add(cid);
                            } else {
                                return MessageBean.findMessageForward(request, mapping, "��������ID��Ϣʧ�ܣ�");
                            }
                        } catch (Exception e) {
                            return MessageBean.findMessageForward(request, mapping, "��������ID��Ϣʧ�ܣ�");
                        }
                    }
                } else return MessageBean.findMessageForward(request, mapping, "û��ѡ�а��飬�޷�ɾ�����ϵ");
                if (childids != null && childids.size() > 0) {
                    Database.perform(new DBDeleteGroupChilds(groupid, childids));
                    log.info("delete group child from group: " + groupid);
                }
                if (deleterelation != null && deleterelation.size() > 0) {
                    GroupTree tree = DBGroupTree.getGroupTree();
                    int[] groupids = new int[sid.length];
                    for (int i = 0; i < groupids.length; i++) {
                        groupids[i] = Integer.parseInt(sid[i].trim());
                    }
                    String parent = tree.getGroupName(groupid);
                    String[] children = tree.getGroupNames(groupids);
                    for (String child : children) {
                        tree.removeSubGroup(parent, child);
                    }
                    log.info("delete group child from memory");
                }
                return MessageBean.findMessageForward(request, mapping, "�ɹ�����" + deleteGroupRelationForm.getGroupID() + "��ɾ����飨���飩");
            } catch (CLBException e) {
                log.error("delete group child fail with reason:" + e.getMessage());
                return MessageBean.findExceptionForward(request, mapping, e);
            }
        }
    }

    private static Logger log;

    static {
        log = Logger.getLogger(DeleteGroupChildAction.class);
    }
}
