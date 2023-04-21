package servlets;

import fields.DelegacjeTab;
import db.Connectdb;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import fields.Osoba;
import fields.OsobaDb;
import logic.parametry;

/**
 * Servlet implementation class for Servlet: Logowanie
 * 
 */
public class Logowanie extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    Osoba osoba = new Osoba();

    OsobaDb osobaDb = new OsobaDb();

    Connectdb cdb = new Connectdb();

    DelegacjeTab delegacjeTab = new DelegacjeTab();

    parametry par = new parametry();

    public Logowanie() {
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
        HttpSession session = request.getSession();
        par.setPath(getServletContext().getRealPath("/"));
        par.LoadConfig(par, par.getPath());
        cdb.czyZalogowany();
        cdb.logowanie(osoba, par.getPath(), par);
        par.setDefaultForm(delegacjeTab);
        session.setAttribute("parametry", par);
        session.setAttribute("osoba", osoba);
        session.setAttribute("osobaDb", osobaDb);
        session.setAttribute("delegacjeTab", delegacjeTab);
        session.setAttribute("parametry2", par.pobierzWaluty());
        session.setAttribute("Lista_km", par.LoadConfig3(par.getPath(), par));
        if (osoba.getZalogowany().equals("true") == true) {
            RequestDispatcher dis = request.getRequestDispatcher("/menu.jsp");
            dis.forward(request, response);
        } else {
            RequestDispatcher dis = request.getRequestDispatcher("powrot.jsp");
            dis.forward(request, response);
        }
    }
}
