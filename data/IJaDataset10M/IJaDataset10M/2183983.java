package cn.ac.ntarl.umt.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.action.AbstractAction;
import cn.ac.ntarl.umt.actions.user.DBCreateUser;
import cn.ac.ntarl.umt.database.AlreadyExist;
import cn.ac.ntarl.umt.database.Database;
import cn.ac.ntarl.umt.form.CreateUserForm;
import cn.ac.ntarl.umt.security.Sessions;
import cn.ac.ntarl.umt.tree.DBGroupTree;
import cn.ac.ntarl.umt.tree.GroupTree;
import cn.ac.ntarl.umt.user.User;
import cn.ac.ntarl.umt.utils.MessageBean;

/** 
 * MyEclipse Struts
 * Creation date: 06-25-2007
 * 
 * XDoclet definition:
 * @struts.action path="/createUser" name="createUserForm" input="/createUser.jsp" scope="request" validate="true"
 * @struts.action-forward name="success" path="/CreateUserSuccess.jsp"
 * @struts.action-forward name="failure" path="/user/createUser.jsp"
 */
public class CreateUserAction extends AbstractAction {

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
        CreateUserForm createUserForm = (CreateUserForm) form;
        User u = createUserForm.createUser();
        ActionErrors errors = new ActionErrors();
        try {
            Database.perform(new DBCreateUser(u));
            log.info("user " + u.getUsername() + " is created successful");
            String returnstr = "�û�" + u.getUsername() + "�����ɹ�!";
            GroupTree tree = DBGroupTree.getGroupTree();
            tree.newUserNode(u);
            return MessageBean.findMessageForward(request, mapping, returnstr);
        } catch (AlreadyExist e) {
            log.warn("user " + u.getUsername() + " exists and cannot be created successful!");
            errors.add("username", new ActionError("errors.userexist"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } catch (CLBException e) {
            log.error("create user " + u.getUsername() + " fail with reason:" + e.getMessage());
            return MessageBean.findExceptionForward(request, mapping, e);
        }
    }

    private static Logger log;

    static {
        log = Logger.getLogger(CreateUserAction.class);
    }
}
