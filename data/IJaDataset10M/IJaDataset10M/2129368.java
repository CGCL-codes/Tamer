package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Sanjeev Singh
 */
@SuppressWarnings("serial")
public class SimplexServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String variableValue = request.getParameter("varibales");
            String constraintValue = request.getParameter("constraints");
            Float varValue = new Float(variableValue);
            Float conValue = new Float(constraintValue);
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Parameter Feeder Form</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Simplex Method for Linear Optimization" + "</h1>");
            out.println("<h3>Objective Function:</h3>");
            out.println("F(X):" + "");
            for (int i = 1; i <= varValue; i++) {
                if (i < varValue) {
                    out.println("<input type=\"text\" name=\"variable" + i + "\" size=\"5\">" + "X" + i + "+");
                } else {
                    out.println("<input type=\"text\" name=\"variable" + i + "\" size=\"5\">" + "X" + i);
                }
            }
            out.println("<select name=\"MaxMin\"> <option>Max</option> <option>Min</option></select>");
            out.println("<h3>Constraints:</h3>");
            for (int i = 1; i <= conValue; i++) {
                out.println("Constraint" + ":" + i);
                for (int j = 1; j <= varValue; j++) {
                    if (j < varValue) {
                        out.println("<input type=\"text\" name=\"constraint" + i + j + "\" size=\"5\">" + "X" + j + "+");
                    } else {
                        out.println("<input type=\"text\" name=\"constraint" + i + j + "\" size=\"5\">" + "X" + j);
                        out.println("<select name=\"ConstraintEquator\"><option><=</option><option>>=</option><option>=</option></select>");
                        out.println("<input type=\"text\" name=\"constant" + i + "\" size=\"5\">" + "<p></p>");
                    }
                }
            }
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
}
