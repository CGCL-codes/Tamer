package ecom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ecom.beans.ClientBean;
import ecom.beans.CurrencyType;
import ecom.beans.EcomCustomerRemote;
import ecom.beans.OutputType;
import java.util.Properties;
import javax.transaction.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Servlet implementation class test
 */
public class Deconnexion extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static InitialContext ic;

    private static EcomCustomerRemote customer;

    private static OutputType outputType;

    private static CurrencyType currency;

    private static Locale locale;

    private static ResourceBundle messageBundle;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Deconnexion() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("identifiant");
        session.removeAttribute("client");
        session.removeAttribute("affichemodel");
        session.removeAttribute("message");
        session.removeAttribute("Caddie");
        session.removeAttribute("langue");
        session.removeAttribute("marquechoisi");
        session.removeAttribute("magasinchoisi");
        session.removeAttribute("prixminichoisi");
        session.removeAttribute("prixmaxichoisi");
        session.removeAttribute("listeMarque");
        session.removeAttribute("listeMagasin");
        this.getServletContext().getRequestDispatcher("/index.jsp").include(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("identifiant");
        session.removeAttribute("client");
        session.removeAttribute("affichemodel");
        session.removeAttribute("message");
        session.removeAttribute("Caddie");
        session.removeAttribute("langue");
        session.removeAttribute("marquechoisi");
        session.removeAttribute("magasinchoisi");
        session.removeAttribute("prixminichoisi");
        session.removeAttribute("prixmaxichoisi");
        session.removeAttribute("listeMarque");
        session.removeAttribute("listeMagasin");
        this.getServletContext().getRequestDispatcher("/index.jsp").include(request, response);
    }
}
