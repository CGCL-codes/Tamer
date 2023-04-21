package servlets;

import db.ConnectDeleg;
import db.Connectdb;
import fields.DelegacjeTab;
import fields.OsobaDb;
import fields.Osoba;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logic.parametry;

/**
 *
 * @author pguza
 */
public class BlokowanieBazy extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    Osoba osoba = new Osoba();

    OsobaDb osobaDb = new OsobaDb();

    DelegacjeTab delegacjeTab = new DelegacjeTab();

    parametry par = new parametry();

    Connectdb con = new Connectdb();

    public BlokowanieBazy() {
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
        par.setPath(getServletContext().getRealPath("/"));
        par.LoadConfig(par, par.getPath());
        con.OdczytParametrowBazy(par.getPath(), par);
        System.out.println("Parametr 1 odczytany: " + par.getUrl());
        System.out.println("Parametr 2 odczytany: " + par.getUser());
        System.out.println("Parametr 3 odczytany: " + par.getPassword());
        System.out.println("Parametr 4 odczytany: " + par.getLock());
        delegacjeTab.setDbJdbcServer(par.getUrl());
        delegacjeTab.setDbLogin(par.getUser());
        delegacjeTab.setDbPassword(par.getPassword());
        System.out.println("Parametr 1 (delegacjeTAB)" + delegacjeTab.getDbJdbcServer());
        System.out.println("Parametr 2 (delegacjeTAB)" + delegacjeTab.getDbLogin());
        System.out.println("Parametr 3 (delegacjeTAB)" + delegacjeTab.getDbPassword());
        delegacjeTab.setDbJdbcServer(request.getParameter("dbJdbcServer"));
        delegacjeTab.setDbLogin(request.getParameter("dbLogin"));
        delegacjeTab.setDbPassword(request.getParameter("dbPassword"));
        HttpSession session = request.getSession();
        session.setAttribute("osoba", osoba);
        session.setAttribute("osobaDb", osobaDb);
        session.setAttribute("delegacjeTab", delegacjeTab);
        session.setAttribute("parametry", par);
        if (request.getParameter("blokuj").equals("ON")) {
            par.ustawlockParam("true");
            par.LoadConfig(par, par.getPath());
            RequestDispatcher dis = request.getRequestDispatcher("/bazaZablokowana.jsp");
            dis.forward(request, response);
        } else {
            par.ustawlockParam("false");
            par.LoadConfig(par, par.getPath());
            RequestDispatcher dis = request.getRequestDispatcher("/ustawionoDbparam.jsp");
            dis.forward(request, response);
        }
    }
}
