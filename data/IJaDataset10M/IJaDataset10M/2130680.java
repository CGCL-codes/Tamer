package servlets;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import dbUtil.*;

/**
 *
 * @author Administrador
 */
public class ManejadorReserva extends HttpServlet {

    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
        } finally {
            out.close();
        }
    }

    public void mostrar_pagina(String url, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        RequestDispatcher disp = req.getRequestDispatcher(url);
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.setIntHeader("Expires", 0);
        disp.forward(req, resp);
    }

    public String seleccionar(DBConfig dc, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "";
    }

    public String agregar(DBConfig dc, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "";
    }

    public String modificar(DBConfig dc, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "";
    }

    public String eliminar(DBConfig dc, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "";
    }

    public DBConfig getDataBaseConfig() {
        ServletContext scon = getServletContext();
        DBConfig dc = new DBConfig();
        dc.setDbname(scon.getInitParameter("dbname"));
        dc.setServer(scon.getInitParameter("dbserver"));
        dc.setUser(scon.getInitParameter("dbuser"));
        dc.setPass(scon.getInitParameter("dbpass"));
        return dc;
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
