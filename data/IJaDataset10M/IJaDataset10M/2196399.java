package com.javaeye.lonlysky.lforum.web.admin.rapidset;

import org.apache.struts2.config.ParentPackage;
import com.javaeye.lonlysky.lforum.AdminBaseAction;

/**
 * 
 * @author 寂寞地铁
 *
 */
@ParentPackage("default")
public class ShortcutAction extends AdminBaseAction {

    private static final long serialVersionUID = -5077765505173654657L;

    private String filenamelist = "";

    @Override
    public String execute() throws Exception {
        loadTemplateInfo();
        return SUCCESS;
    }

    /**
	 * 加载模板路径信息
	 */
    public void loadTemplateInfo() {
    }

    public String getFilenamelist() {
        return filenamelist;
    }
}
