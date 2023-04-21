package a00720398;

import a00720398.beans.ConvertBean;
import a00720398.beans.RawData;
import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author thufir
 */
public class Lab08Servlet extends HttpServlet {

    private double output = 0;

    private String title;

    private ConvertBean convert;

    @Override
    public void init() {
        title = getServletContext().getInitParameter("title");
        convert = new ConvertBean();
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tempType = request.getParameter("tempType");
        String input = request.getParameter("input");
        processData(request, response, tempType, input);
    }

    private void processData(HttpServletRequest request, HttpServletResponse response, String type, String input) throws IOException, ServletException {
        double inputValue = 0.0;
        try {
            inputValue = Double.parseDouble(input);
        } catch (NumberFormatException nfe) {
            response.sendError(response.SC_BAD_REQUEST, "Invalid user input, \"" + input + "\". YOU BONE-HEAD!");
        }
        if (type.equals("celsius")) {
            output = convert.convertToCelsius(inputValue);
        } else {
            output = convert.convertToFahrenheit(inputValue);
        }
        request.setAttribute("input", input);
        request.setAttribute("type", type);
        request.setAttribute("output", output);
        gotoPage("/result.jsp", request, response);
    }

    private void gotoPage(String address, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    /** 
         * Handles the HTTP <code>GET</code> method.
         * @param request servlet request
         * @param response servlet response
         */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
