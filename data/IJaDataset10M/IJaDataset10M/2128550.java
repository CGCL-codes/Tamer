package com.hand.action;

import java.util.Map;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 系统名：HCSMobileApp
 * 子系统名：退出应用程序
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 30, 2011
 */
public class ExitAppAction extends ActionSupport {

    @Override
    public String execute() throws Exception {
        ActionContext actionContext = ActionContext.getContext();
        Map<String, Object> session = actionContext.getSession();
        session.clear();
        return SUCCESS;
    }
}
