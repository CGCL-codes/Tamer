package org.jdamico.jhu.web.servlets;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vikulin.utils.Constants;

public class Authenticator extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getRemoteHost() + " ip client authenticated");
        File destinationDirectory = new File(Constants.conf.getFileDirectory());
        if (!destinationDirectory.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "destination directory " + Constants.conf.getFileDirectory() + " was not found");
            resp.getOutputStream().close();
            return;
        }
        if (!destinationDirectory.canWrite()) {
            resp.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "destination directory " + Constants.conf.getFileDirectory() + " is not writable");
            resp.getOutputStream().close();
            return;
        }
        resp.getOutputStream().close();
    }
}
