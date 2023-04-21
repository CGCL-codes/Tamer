package com.centraview.sync;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class Login extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        SyncFacade syncFacade = new SyncFacade();
        syncFacade.setDataSource(dataSource);
        try {
            boolean loginResult = syncFacade.doLogin(form, request, response);
            if (loginResult != false) {
            } else {
                writer.print("FAIL");
            }
        } catch (Exception e) {
            writer.print("FAIL");
        }
        return (null);
    }
}
