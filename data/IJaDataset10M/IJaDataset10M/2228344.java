package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lebah.db.*;

public class ReloadConn implements IServlet {

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            PrintWriter out = response.getWriter();
            out.println("refresh connection properties...");
            ConnectionProperties.refreshInstance();
            Labels.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
