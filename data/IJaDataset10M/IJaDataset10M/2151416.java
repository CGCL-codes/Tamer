package servlets;

import fields.DelegacjeTab;
import db.Connectdb;
import db.ConnectWydruk;
import db.ConnectDeleg;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logic.GenDel;
import logic.Liczby;
import fields.Osoba;
import fields.OsobaDb;
import logic.UpdateDelegacjeTab;
import fields.View1;
import logic.parametry;

/**
 * Servlet implementation class for Servlet:
 * 
 */
public class EdytujKm extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    Osoba osoba = new Osoba();

    OsobaDb osobaDb = new OsobaDb();

    Connectdb connectdb = new Connectdb();

    UpdateDelegacjeTab updateDelegacjeTab = new UpdateDelegacjeTab();

    ConnectWydruk connectWydruk = new ConnectWydruk();

    DelegacjeTab delegacjeTab = new DelegacjeTab();

    Liczby liczby = new Liczby();

    View1 view1 = new View1();

    ConnectDeleg connectDeleg = new ConnectDeleg();

    GenDel genDel = new GenDel();

    parametry par = new parametry();

    public EdytujKm() {
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
        delegacjeTab.setId_km(request.getParameter("id_km"));
        delegacjeTab.setNazwa_km(request.getParameter("nazwa_km"));
        delegacjeTab.setStawka_km(request.getParameter("stawka_km"));
        par.setPath(getServletContext().getRealPath("/"));
        par.LoadConfig(par, par.getPath());
        System.out.println(delegacjeTab.getId_km() + "\n" + delegacjeTab.getNazwa_km() + "\n" + delegacjeTab.getStawka_km());
        updateDelegacjeTab.updateKm(par.getPath(), par, delegacjeTab);
        HttpSession session = request.getSession();
        session.setAttribute("delegacjeTab", delegacjeTab);
        session.setAttribute("osoba", osoba);
        session.setAttribute("connectDeleg", connectDeleg);
        session.setAttribute("Lista_km", par.LoadConfig3(par.getPath(), par));
        RequestDispatcher dis = request.getRequestDispatcher("/km.jsp");
        dis.forward(request, response);
    }
}
