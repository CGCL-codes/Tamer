package org.subethamail.web.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.subethamail.common.NotFoundException;
import org.subethamail.core.lists.i.Archiver;
import org.subethamail.entity.i.PermissionException;

/**
 * This servlet will return an archived message in its raw rfc2822 format.
 * 
 * The format of the url has to be /id/[mode]/filename. If mode is omitted it will default to view.
 */
public class ArchiveServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String MODE_VIEW = "view";

    public static final String MODE_DOWNLOAD = "download";

    @Inject
    Archiver arch;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long msgId = null;
        String[] pathSplit = request.getPathInfo().split("/");
        String tmpId = (pathSplit.length > 1) ? pathSplit[1] : null;
        String mode = (pathSplit.length > 2) ? pathSplit[2] : null;
        msgId = Long.parseLong(tmpId);
        try {
            if (msgId == null) throw new NotFoundException("Message not found: invalid id!");
            if (mode != null && MODE_DOWNLOAD.equals(mode.toLowerCase())) {
                response.setHeader("Content-Disposition", "attachment");
                response.setContentType("message/rfc822");
            } else {
                response.setContentType("text/plain");
            }
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            arch.writeMessage(msgId, bos);
        } catch (PermissionException pex) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/error_permission.jsp");
            request.setAttribute("javax.servlet.error.exception", pex);
            dispatcher.forward(request, response);
        } catch (NotFoundException nfex) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/error_notfound.jsp");
            request.setAttribute("javax.servlet.error.exception", nfex);
            dispatcher.forward(request, response);
        }
    }
}
