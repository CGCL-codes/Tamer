package bank;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.PageBean;

public class Medewerker extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public void init() {
        System.out.println("Klant.init()");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Klant.doGet()");
        UberClass uberclass = new UberClass(request, response, UberClass.MEDEWERKER);
        beans.CommonBean cb = new beans.CommonBean();
        cb.put("type", "medewerker");
        request.setAttribute("t", cb);
        Principal p = request.getUserPrincipal();
        System.out.println("Medewerker.principal.getName() = " + p.getName());
        PageBean pb = new PageBean("medewerker/AccountBeheer.jsp");
        request.setAttribute("page", pb);
        uberclass.startOutput();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Klant.doPost()");
    }
}
