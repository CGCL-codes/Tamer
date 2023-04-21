package jreceiver.client.mgr.struts;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import jreceiver.client.common.RoleAuthBean;
import jreceiver.client.common.struts.JRecEditAction;
import jreceiver.client.common.struts.JRecEditForm;
import jreceiver.common.JRecException;
import jreceiver.common.rec.security.User;
import jreceiver.common.rec.security.UserRec;
import jreceiver.common.rec.security.Role;
import jreceiver.common.rpc.Users;
import jreceiver.common.rpc.RpcFactory;
import jreceiver.common.rpc.RpcException;
import jreceiver.common.rpc.Roles;

/**
 * Handle incoming requests on "users.do" to update a single record in Users table
 * <P>
 * This is a wrapper around the business logic. Its purpose is to
 * translate the HttpServletRequest to the business logic, which
 * if significant, should be processed in a separate class.
 * <p>
 *
 * @author Reed Esau
 * @version $Revision: 1.6 $ $Date: 2002/09/24 20:51:36 $
 */
public final class UserEditAction extends JRecEditAction {

    /**
    * load any special stuff in to the form, usually init'd from param list
    */
    public void onLoad(User user, JRecEditForm edit_form, HttpServletRequest req, ActionErrors errors) throws JRecException {
        UserEditForm form = (UserEditForm) edit_form;
        try {
            Users user_rpc = RpcFactory.newUsers(user);
            String user_id = req.getParameter("userId");
            if (user_id != null) {
                User userx = (User) user_rpc.getRec(user_id, null);
                form.setUserId(user_id);
                form.setFullName(userx.getFullName());
                form.setPassword(userx.getPassword());
                form.setRoleId(userx.getRoleId());
                form.setIsNewUser(false);
            } else {
                form.setUserId("");
                form.setFullName("");
                form.setRoleId("users");
                form.setIsNewUser(true);
            }
            HttpSession session = req.getSession();
            RoleAuthBean bean = (RoleAuthBean) session.getAttribute(RoleAuthBean.ROLE_AUTH_KEY);
            Vector roles;
            Roles role_rpc = RpcFactory.newRoles(user);
            if (bean.isAuthorized(Roles.HANDLER_NAME, Roles.GET_RECS)) {
                roles = role_rpc.getRecs(null, null, 0, Role.NO_LIMIT);
            } else throw new JRecException("unexpected lack of role authorization");
            form.setRoles(roles);
        } catch (RpcException e) {
            throw new JRecException("rpc-problem saving user", e);
        }
    }

    /**
     * save the form field data to persistent storage
     */
    public void onSave(User user, JRecEditForm edit_form, HttpSession session, ActionErrors errors) throws JRecException {
        UserEditForm form = (UserEditForm) edit_form;
        try {
            Users user_rpc = RpcFactory.newUsers(user);
            User updated_user = new UserRec(form.getUserId(), form.getPassword(), form.getFullName(), form.getRoleId());
            log.debug("onSave: updated_user=" + updated_user + " auth_user=" + user);
            user_rpc.storeRec(updated_user);
            if (updated_user.getId().equalsIgnoreCase(user.getId())) {
                log.debug("onSave: detected user modified his own entry");
                session.removeAttribute(User.USER_KEY);
            }
        } catch (RpcException e) {
            throw new JRecException("rpc-problem saving user", e);
        }
    }

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(UserEditAction.class);
}
