package com.javaeye.common.web;

import java.util.ArrayList;
import java.util.List;
import org.apache.struts2.ServletActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.javaeye.common.dto.Module;
import com.javaeye.common.dto.User;
import com.javaeye.common.service.IWelcomeService;
import com.javaeye.common.util.PermissionsHelper;
import com.opensymphony.xwork2.ActionSupport;

public class WelcomeAction extends ActionSupport {

    protected static Log log = LogFactory.getLog(WelcomeAction.class);

    private int[] queryData;

    private IWelcomeService service;

    private List<MenuModule> menuModules = new ArrayList<MenuModule>();

    /**
	 * 
	 */
    private static final long serialVersionUID = -7024923899191853934L;

    /**
	 * 查询欢迎画面的提示数据
	 * @return
	 * @throws Exception
	 */
    public String queryWelcomeData() throws Exception {
        queryData = service.getWelcomeData();
        return SUCCESS;
    }

    public String queryLeftData() throws Exception {
        menuModules.clear();
        User user = (User) ServletActionContext.getRequest().getSession().getAttribute(User.LOGIN);
        List<Module> rootMenuModules = PermissionsHelper.getUserMenuModuleList(user, 0);
        for (Module module : rootMenuModules) {
            int pid = module.getId();
            List<Module> childModules = PermissionsHelper.getUserMenuModuleList(user, pid);
            menuModules.add(new MenuModule(module, childModules));
        }
        return SUCCESS;
    }

    public int[] getQueryData() {
        return queryData;
    }

    public void setQueryData(int[] queryData) {
        this.queryData = queryData;
    }

    public void setService(IWelcomeService service) {
        this.service = service;
    }

    public List<MenuModule> getMenuModules() {
        return menuModules;
    }

    public void setMenuModules(List<MenuModule> menuModules) {
        this.menuModules = menuModules;
    }
}
