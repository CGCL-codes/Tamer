package servlets;

import fields.OsobaDb;
import fields.Osoba;
import db.Connectdb;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logic.parametry;

/**
 * Servlet implementation class for Servlet:
 * 
 */
public class ZmianaH extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    Osoba osoba = new Osoba();

    OsobaDb osobaDb = new OsobaDb();

    Connectdb connectdb = new Connectdb();

    public ZmianaH() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        osoba.setImie(request.getParameter("imie"));
        osoba.setNazwisko(request.getParameter("nazwisko"));
        osoba.setPasswd(request.getParameter("passwd"));
        osoba.setUser_id(request.getParameter("user_id"));
        osoba.setAdm(request.getParameter("adm"));
        HttpSession session = request.getSession();
        session.setAttribute("osoba", osoba);
        session.setAttribute("osobaDb", osobaDb);
        parametry par = new parametry();
        par.setPath(getServletContext().getRealPath("/"));
        par.LoadConfig(par, par.getPath());
        connectdb.logowanie(osoba, par.getPath(), par);
        if (osoba.getZalogowany().equals("true") == true) {
            RequestDispatcher dis = request.getRequestDispatcher("/zmianaH.jsp");
            dis.forward(request, response);
        } else {
            RequestDispatcher dis = request.getRequestDispatcher("powrot.jsp");
            dis.forward(request, response);
        }
    }
}
