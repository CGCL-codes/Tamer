package com.dotmarketing.servlets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dotmarketing.beans.UserProxy;
import com.dotmarketing.cms.factories.PublicUserFactory;
import com.dotmarketing.factories.ClickstreamFactory;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.factories.UserProxyFactory;
import com.dotmarketing.portlets.campaigns.factories.RecipientFactory;
import com.dotmarketing.portlets.campaigns.model.Recipient;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.User;

public class CampaignReportingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    String UPDATE_OPENED = "UPDATE recipient set opened = now() where recipient_id = ?";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("image/gif");
            FileInputStream fis = new FileInputStream(Config.CONTEXT.getRealPath("/portal/images/shim.gif"));
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                out.write(buf, 0, i);
            }
            fis.close();
            out.close();
        } catch (FileNotFoundException e) {
            Logger.warn(this, e.toString(), e);
        }
        Recipient r = RecipientFactory.getRecipient(request.getParameter("r"));
        if (r.getInode() > 0) {
            r.setOpened(new java.util.Date());
            r.setLastResult(200);
            r.setLastMessage("Opened Email");
            InodeFactory.saveInode(r);
            User user = PublicUserFactory.getUserByEmail(r.getEmail());
            UserProxy sub = UserProxyFactory.getUserProxy(user);
            if (sub.getInode() > 0) {
                sub.setLastResult(200);
                sub.setLastMessage("Opened Email");
                UserProxyFactory.saveUserProxy(sub);
            }
            ClickstreamFactory.setClickStreamUser(user.getUserId(), request);
        }
        return;
    }
}
