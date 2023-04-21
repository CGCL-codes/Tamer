package com.googlecode.psiprobe.controllers.jsp;

import com.googlecode.psiprobe.Utils;
import com.googlecode.psiprobe.controllers.ContextHandlerController;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

public class DownloadServletController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jspName = ServletRequestUtils.getStringParameter(request, "source", null);
        if (jspName != null) {
            String servletName = getContainerWrapper().getTomcatContainer().getServletFileNameForJsp(context, jspName);
            if (servletName != null) {
                File servletFile = new File(servletName);
                if (servletFile.exists()) {
                    Utils.sendFile(request, response, servletFile);
                }
            }
        }
        return null;
    }
}
