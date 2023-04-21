package be.kuleuven.cs.samgi.rest;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import be.kuleuven.cs.samgi.Entrypoint;
import be.kuleuven.cs.samgi.EntrypointE;
import be.kuleuven.cs.samgi.ParameterList;
import be.kuleuven.cs.samgi.soap.RequestHandler;

/**
 * Servlet implementation class GetOutput
 */
public class GetOutput extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public GetOutput() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration en = request.getParameterNames();
        try {
            String[] res = request.getParameterValues("entrypoint");
            for (String s : res) {
                RequestHandler handler = RequestHandler.getInstance(s);
                EntrypointE ep = new EntrypointE();
                Entrypoint e = new Entrypoint();
                e.setEntrypoint(s);
                ep.setEntrypoint(e);
                ParameterList pl = handler.GetOutput(ep);
                for (String ins : pl.getParameter()) {
                    response.getWriter().println(ins);
                }
            }
        } catch (Exception e) {
        }
        response.setStatus(200);
    }
}
