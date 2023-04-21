package servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.DataBaseKeyWord;

public class Manage_Keywords extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operation = request.getParameter("btn");
        if (operation.equals("Delete")) {
            deleteKeywords(request, response);
        } else if (operation.equals("Edit")) {
            modifyKeywords(request, response);
        }
    }

    private void modifyKeywords(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = recupIndice(request);
        request.getSession().setAttribute("idEditKeyword", id);
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

    private void deleteKeywords(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] listeIds = recupIndices(request);
        new DataBaseKeyWord().suppression(listeIds);
        reinitialiserKeywords();
        response.sendRedirect("index.jsp");
    }

    private String[] recupIndices(HttpServletRequest request) {
        ArrayList ids = new ArrayList();
        int nb = new DataBaseKeyWord().getLastId();
        for (int i = 0; i < nb; ++i) {
            String res = request.getParameter("box" + i);
            if (res != null && !res.equals("")) {
                ids.add(String.valueOf(i));
            }
        }
        String realIDs = "";
        for (int i = 0; i < ids.size(); ++i) {
            realIDs += " " + ids.get(i);
            System.out.println("(" + ids.get(i) + ")");
        }
        return (realIDs.trim()).split(" ");
    }

    private void reinitialiserKeywords() {
        getServletContext().setAttribute("keywords", null);
    }
}
