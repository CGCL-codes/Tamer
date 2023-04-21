package ftp;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import syslogd.SyslogdCommandFactory;
import common.action.Action;
import common.action.ActionForward;

public class FtpServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FTP Servlet 왔어요");
        ActionForward forward = null;
        Action action = null;
        try {
            FtpCommandFactory cmdFactory = FtpCommandFactory.getInstance();
            String command = request.getParameter("command");
            System.out.println("Command : " + command);
            if (command == null) {
                command = "loginForm";
            }
            action = cmdFactory.getAction(command);
            forward = action.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
        if (forward == null) {
            return;
        }
        if (forward.isRedirect()) {
            response.sendRedirect(forward.getPath());
        } else {
            RequestDispatcher rd = request.getRequestDispatcher(forward.getPath());
            rd.forward(request, response);
        }
    }
}
