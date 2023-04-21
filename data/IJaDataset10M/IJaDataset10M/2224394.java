package net.vicms.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Collection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.vicms.dto.Categories;
import net.vicms.dto.Sessions;
import net.vicms.dto.Users;
import net.vicms.service.DeleteService;
import net.vicms.service.InsertService;
import net.vicms.service.SelectService;
import net.vicms.service.UpdateService;
import net.vicms.util.Util;

/**
 *
 * @author kien
 */
public class CategoriesController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String message = "";

    private String forward = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("act");
        action = (action == null) ? "list" : action;
        if (action.equalsIgnoreCase("add")) {
            Categories category = new Categories();
            category.setId(Util.generateUUID());
            category.setTitle(request.getParameter("title"));
            category.setDescription(request.getParameter("description"));
            category.setPossition(Integer.parseInt(request.getParameter("possition")));
            category.setStatus(request.getParameter("status") == null ? false : true);
            category.setDateupdate(Date.valueOf(request.getParameter("dateupdate")));
            String sessionId = request.getParameter("sessionId");
            Sessions session = new Sessions();
            session.setId(sessionId);
            category.setSession(session);
            Users user = (Users) request.getSession().getAttribute("user");
            String userId = user.getId();
            user.setId(userId);
            category.setUser(user);
            InsertService is = new InsertService();
            Categories result = is.insertCategories(category);
            if (result == null) {
                message = "Add Categories Error!";
                request.setAttribute("categories", category);
                action = "addform";
            } else {
                message = "Add Categories Successful!";
                action = "list";
            }
            request.setAttribute("message", message);
        } else if (action.equalsIgnoreCase("edit")) {
            Categories category = new Categories();
            category.setId(request.getParameter("cat"));
            category.setTitle(request.getParameter("title"));
            category.setDescription(request.getParameter("description"));
            category.setPossition(Integer.parseInt(request.getParameter("possition")));
            category.setStatus(request.getParameter("status") == null ? false : true);
            category.setDateupdate(Date.valueOf(request.getParameter("dateupdate")));
            Sessions session = new Sessions();
            session.setId(request.getParameter("sessionId"));
            category.setSession(session);
            Users user = (Users) request.getSession().getAttribute("user");
            String userId = user.getId();
            user.setId(userId);
            category.setUser(user);
            UpdateService us = new UpdateService();
            Categories result = us.updateCategories(category);
            message = result == null ? "Update Category Error! " : "Update Category Successful!";
            action = "list";
            request.setAttribute("message", message);
        } else if (action.equalsIgnoreCase("del")) {
            String categoryId = request.getParameter("cat");
            DeleteService ds = new DeleteService();
            boolean result = ds.deleteCategoriesById(categoryId);
            message = result ? "Delete Category Successful!" : "Delete Category Error!";
            action = "list";
            request.setAttribute("message", message);
        } else if (action.equalsIgnoreCase("cancel")) {
            action = "list";
        }
        if (action.equalsIgnoreCase("list")) {
            String sessionId = request.getParameter("sessionId");
            sessionId = (sessionId == null) ? "all" : sessionId;
            SelectService ss = new SelectService();
            Collection<Categories> categories;
            if (sessionId.equals("all")) {
                categories = ss.selectCategories();
            } else {
                categories = ss.selectCategoriesBySessionId(sessionId);
            }
            Collection<Sessions> sessions = ss.selectSessions();
            request.setAttribute("sessionId", sessionId);
            request.setAttribute("categories", categories);
            request.setAttribute("sessions", sessions);
            forward = categories == null ? "" : "/view/listcategories.jsp";
        } else if (action.equalsIgnoreCase("addform")) {
            SelectService ss = new SelectService();
            Collection<Sessions> sessions = ss.selectSessions();
            request.setAttribute("sessions", sessions);
            forward = "/view/addcategories.jsp";
        } else if (action.equalsIgnoreCase("editform")) {
            String categoryId = request.getParameter("cat");
            SelectService ss = new SelectService();
            Categories category = ss.selectCategoriesById(categoryId);
            request.setAttribute("category", category);
            Collection<Sessions> sessions = ss.selectSessions();
            request.setAttribute("sessions", sessions);
            forward = (category == null) ? "" : "/view/editcategories.jsp";
        }
        request.setAttribute("forward", forward);
        request.setAttribute("from", "categories");
        RequestDispatcher rd = request.getRequestDispatcher("/view/adminpage.jsp");
        rd.forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
