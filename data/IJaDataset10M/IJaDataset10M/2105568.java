package myservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import common.UploadUtils;

public class MyTestServlet2 extends HttpServlet {

    private static final long serialVersionUID = 5809173377033837420L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UploadUtils.upload2(request, Long.MAX_VALUE);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
