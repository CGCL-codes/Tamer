package cn.mmbook.platform.action.security;

import static javacommon.util.extjs.Struts2JsonHelper.outString;
import java.io.IOException;
import javacommon.base.BaseStruts2Action;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.Preparable;
import cn.mmconpay.util.*;

/**
 * 权限处理 ACTIOM
 * @author qiongguo
 *
 */
public class SecurityAction extends BaseStruts2Action implements Preparable {

    public void prepare() throws Exception {
    }

    public Object getModel() {
        return null;
    }

    /**
	 * 暂不做权限处理。
	 * manager/security/Security/nemu.do
	 * @throws IOException
	 */
    public void nemu() throws IOException {
        SecurityNemu nemuServer = new SecurityNemu();
        String menus = nemuServer.menuStr;
        outString(menus);
    }
}
