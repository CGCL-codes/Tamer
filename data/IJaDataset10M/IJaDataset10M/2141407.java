package servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.DataBaseEvent;

public class Manage_Events extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operation = request.getParameter("btn");
        if (operation.equals("Delete")) {
            deleteEvents(request, response);
        } else if (operation.equals("Edit")) {
            modifyEvents(request, response);
        }
    }

    private void modifyEvents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = recupIndice(request);
        request.getSession().setAttribute("idEditEvent", id);
        response.sendRedirect("index.jsp");
    }

    private String recupIndice(HttpServletRequest request) {
        String id = null;
        String res = request.getParameter("radio");
        if (res != null) {
            id = res.substring(5);
        }
        return id;
    }

    private void deleteEvents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] listeIds = recupIndices(request);
        new DataBaseEvent().suppression(listeIds);
        reinitialiserEvents();
        response.sendRedirect("index.jsp");
    }

    private void reinitialiserEvents() {
        getServletContext().setAttribute("events", null);
        getServletContext().setAttribute("allEvents", null);
    }

    private String[] recupIndices(HttpServletRequest request) {
        ArrayList ids = new ArrayList();
        int nb = new DataBaseEvent().getLastId();
        for (int i = 0; i <= nb; ++i) {
            String res = request.getParameter("box" + i);
            if (res != null && !res.equals("")) {
                ids.add(String.valueOf(i));
            }
        }
        String realIDs = "";
        for (int i = 0; i < ids.size(); ++i) {
            realIDs += " " + ids.get(i);
        }
        return (realIDs.trim()).split(" ");
    }
}
