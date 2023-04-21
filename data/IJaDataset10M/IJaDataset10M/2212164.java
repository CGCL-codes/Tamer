package net.sf.esims.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.exception.ExceptionHandler;

public class ErrorHandlerAction implements ExceptionHandler {

    public void handle(Throwable throwable, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("reason", throwable.toString());
        request.setAttribute("stackTrace", throwable.getStackTrace());
        request.getRequestDispatcher("/WEB-INF/pages/_systemerr.jsp").forward(request, response);
    }

    public void init(Configuration arg0) throws Exception {
    }
}
