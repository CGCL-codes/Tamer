package servlets;

import fields.OsobaDb;
import fields.Osoba;
import logic.Liczby;
import fields.DelegacjeTab;
import db.Connectdb;
import db.ConnectDeleg;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import fields.View1;
import logic.parametry;

/**
 * Servlet implementation class for Servlet:
 * 
 */
public class Powrot extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    Osoba osoba = new Osoba();

    OsobaDb osobaDb = new OsobaDb();

    Connectdb connectdb = new Connectdb();

    DelegacjeTab delegacjeTab = new DelegacjeTab();

    Liczby liczby = new Liczby();

    View1 view1 = new View1();

    ConnectDeleg connectDeleg = new ConnectDeleg();

    parametry par = new parametry();

    public Powrot() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        osoba.setUser_id(request.getParameter("user_id"));
        osoba.setImie(request.getParameter("imie"));
        osoba.setNazwisko(request.getParameter("nazwisko"));
        osoba.setPasswd(request.getParameter("passwd"));
        osoba.setAdm(request.getParameter("adm"));
        delegacjeTab.setId(request.getParameter("id"));
        view1.setId(delegacjeTab.getId());
        HttpSession session = request.getSession();
        par.setPath(getServletContext().getRealPath("/"));
        par.LoadConfig(par, par.getPath());
        session.setAttribute("osoba", osoba);
        session.setAttribute("osobaDb", osobaDb);
        session.setAttribute("delegacjeTab", delegacjeTab);
        session.setAttribute("view1", view1);
        connectdb.logowanie(osoba, par.getPath(), par);
        if (osoba.getZalogowany().equals("true") == true) {
            RequestDispatcher dis = request.getRequestDispatcher("/menu.jsp");
            dis.forward(request, response);
        } else {
            RequestDispatcher dis = request.getRequestDispatcher("powrot.jsp");
            dis.forward(request, response);
        }
    }
}
