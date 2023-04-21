package org.paradise.dms.web.action.login;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.paradise.dms.pojo.SystemUser;
import org.paradise.dms.pojo.SystemUserGroup;
import org.paradise.dms.pojo.SystemUserGroupRole;
import org.paradise.dms.services.AuthenticateService;
import org.paradise.dms.web.action.DMSBaseAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 
 * Description:系统用户登陆Action
 * 
 * 
 * Copyright (c) paraDise sTudio(DT). All Rights Reserved.
 * 
 * @version 1.0 Mar 4, 2009 10:58:52 PM 李双江（paradise.lsj@gmail.com）created
 */
@Service
@Scope("prototype")
public class SystemUserLoginAction extends DMSBaseAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 954881224978804676L;

    private static Logger log = Logger.getLogger(SystemUserLoginAction.class);

    public String userName;

    public String password;

    public String roleType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public AuthenticateService getAuthenticateService() {
        return authenticateService;
    }

    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public AuthenticateService authenticateService;

    /**
	 * 
	 * Description:登录验证 流程：
	 * 
	 * 1. 用：username/usernickname +password去验证同时同时取用户的systemuserind,判断该用户是否可用
	 * 
	 * 
	 * 2取systemgroupid
	 * 
	 * 3.去systemusergroup验证groupid是否可用
	 * 
	 * 4.通过groupid去取grouppermissionvalue
	 * 
	 * 5.记住用户组用户名,昵称
	 * 
	 * 6.返回成功
	 * 
	 * 
	 * @Version1.0 Mar 4, 2009 11:00:13 PM 李双江（paradise.lsj@gmail.com）创建
	 * @return
	 */
    @SuppressWarnings("static-access")
    public String login() {
        List<SystemUser> list = new ArrayList<SystemUser>();
        list = authenticateService.getSysUserByNamePwd(this.getUserName(), this.getPassword());
        this.addActionError("用户名或密码不正确，请重试！");
        if (list != null && list.size() > 0) {
            String systemuserind = list.get(0).getSystemuserind();
            String systemusername = list.get(0).getSystemusername();
            String systemuserid = list.get(0).getSystemuserid();
            if ("1".equals(systemuserind)) {
                String usergroupid = list.get(0).getSystemusergroupid();
                String systemusernickname = list.get(0).getSystemusernickname();
                List<SystemUserGroup> suglst = authenticateService.isSystemUserGroupUsable(usergroupid);
                if (suglst != null && suglst.size() > 0) {
                    String sugind = suglst.get(0).getSystemusergroupind();
                    String systemusergroupname = suglst.get(0).getSystemusergroupname();
                    String systemusergroupid = suglst.get(0).getSystemusergroupid();
                    String systemusergroupvalue = suglst.get(0).getSystemusergroupvalue() + "";
                    log.info("DMS_info:欢迎您" + systemusername + " 登录.." + "您属于：" + systemusergroupname);
                    if ("1".equals(sugind)) {
                        SystemUserGroupRole supv = authenticateService.getSystemUserPermissionValue(usergroupid).get(0);
                        String permissionValue = supv.getSystemuserpermissionvalue();
                        HttpSession session = ServletActionContext.getRequest().getSession();
                        JSONObject upv = new JSONObject().fromObject(permissionValue);
                        session.setAttribute("userpermissionvalue", upv);
                        session.setAttribute("pv", permissionValue);
                        session.setAttribute("systemusername", systemusername);
                        session.setAttribute("systemuserid", systemuserid);
                        session.setAttribute("systemusergroupvalue", systemusergroupvalue);
                        session.setAttribute("systemusernickname", systemusernickname);
                        session.setAttribute("systemusergroupname", systemusergroupname);
                        session.setAttribute("systemusergroupid", systemusergroupid);
                        return SUCCESS;
                    } else {
                        this.addActionError(systemusername + "所在的用户组:" + systemusergroupname + "无权限登录了。");
                        log.warn("DMS_warn:" + systemusername + "所在的用户组:" + systemusergroupname + "无权限登录了。");
                        return INPUT;
                    }
                } else {
                    this.addActionError(systemusername + "怎么不属于一个组呢？");
                    log.warn("DMS_warn:" + systemusername + "怎么不属于一个组呢？");
                    return INPUT;
                }
            } else {
                this.addActionError(systemusername + "处于不可用状态。");
                log.warn("DMS_warn:" + systemusername + "处于不可用状态。");
                return INPUT;
            }
        }
        return INPUT;
    }

    public String logout() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        log.info("DMS_info:" + session.getAttribute("systemusergroupname") + " 用户：" + session.getAttribute("systemusername") + "退出成功！");
        session.invalidate();
        return SUCCESS;
    }
}
