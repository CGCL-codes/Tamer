package jvc.web.action.system;

import java.sql.ResultSet;
import jvc.util.AppUtils;
import jvc.util.db.MyDB;
import jvc.web.module.User;
import jvc.util.log.Logger;
import jvc.util.security.RSAUtils;
import jvc.web.action.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title :Զ�̵�½</p>
 * <p>Description:</p>
 * <p>Created on 2005-5-16</p>
 * <p>Company :YPL</p>
 *  @author : Ru_fj
 *  @version : 1.0
 */
public class RemoteLoginAction implements BaseAction {

    private static Logger logger = Logger.getLogger(RemoteLoginAction.class.getName());

    public String Excute(ActionContent input, ActionContent output, MyDB mydb) {
        try {
            HttpServletRequest request = (HttpServletRequest) input.getRequest();
            String key = AppUtils.getProperty("jvc.remote." + input.getParam("RemoteSystem") + ".key");
            String data = input.getParam("RemoteSessionData");
            String UserID = RSAUtils.deCode(key, data);
            if (UserID == null) return "!" + input.getParam("fault");
            mydb.check();
            mydb.prepareStatement("select * from jvc_user,jvc_group where GroupID=jvc_group.id and UserID=?");
            mydb.setString(1, UserID);
            ResultSet rs = mydb.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getString("UserID"), rs.getString("UserName"));
                output.setParam("UserID", user.getUserID());
                output.setParam("UserName", user.getUserName());
                user.setGroupID(rs.getString("groupid"));
                if (input.getApplicationMap() != null) {
                    Map onlineUser = (Map) input.getApplicationMap().get("onlineUser");
                    if (onlineUser == null) {
                        onlineUser = new Hashtable();
                        input.getApplicationMap().put("onlineUser", onlineUser);
                    }
                    onlineUser.put(request.getSession().getId(), user);
                }
                user.setLoginTime(new Date());
                user.setLastOperateTime(new Date());
                if (request != null) {
                    user.setIP(request.getRemoteAddr());
                    user.setID(request.getSession().getId());
                    logger.debug(user.getIP() + "��¼ϵͳ");
                    user.setLogin(true);
                    input.setUser(user);
                    Map priviliegemap = jvc.web.usermanager.PrivilegeUtils.getAllowCommonds(mydb, user);
                    input.setLogin(true);
                    input.setPrivilegeMap(priviliegemap);
                    if (user.getUserID().equalsIgnoreCase("administrator")) input.setAllowAll(true);
                    return input.getParam("success");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        output.setParam("UserID", input.getParam("UserID"));
        output.setParam("message", "�û�����������");
        return "!" + input.getParam("fault");
    }
}
