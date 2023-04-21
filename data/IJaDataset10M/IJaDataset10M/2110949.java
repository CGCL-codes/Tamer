package com.smb.MMUtil.action.createORM;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.smb.MMUtil.action.base.ActionBase;
import com.smb.MMUtil.handler.IMySQLManagerJdbcUtilTools;
import com.smb.framework.web.action.ControllerAction;
import com.smb.framework.web.action.ModelAndPage;

public class CreateORMConfigSelectDBAction extends ActionBase implements ControllerAction {

    private static Log logger = LogFactory.getLog(CreateORMConfigSelectDBAction.class);

    public ModelAndPage handleModelAndPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String createORMID = request.getParameter("createORMID");
        String packageName = request.getParameter("packageName");
        if (packageName.equals("") || packageName.indexOf(".") == -1) {
            logger.info("\nClient Side Request RemoteAddr : [ " + request.getRemoteAddr() + " ]");
            request.setAttribute("warn", "请输入正确的Package名称");
            return new ModelAndPage("/WEB-INF/page/orm/createORMConfigList.jsp");
        } else {
            logger.info("\nClient Side Request RemoteAddr : [ " + request.getRemoteAddr() + " ]");
            IMySQLManagerJdbcUtilTools mmu = getMMU(session);
            request.setAttribute("showDataBases", mmu.showDataBases());
            request.setAttribute("createORMID", createORMID);
            request.setAttribute("packageName", packageName);
            request.setAttribute("warn", "");
        }
        return new ModelAndPage("/WEB-INF/page/orm/createORMConfigSelectDB.jsp");
    }
}
