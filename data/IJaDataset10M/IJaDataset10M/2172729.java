package org.wdl.stockManagement.action.baseinfo;

import java.util.Map;
import org.wdl.stockManagement.service.baseinfo.IUserService;
import org.wdl.stockManagement.vo.UserInfo;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;

@SuppressWarnings("serial")
public class CustomerAction extends ActionSupport {

    protected UserInfo user;

    protected IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String execute() throws Exception {
        return ERROR;
    }
}
