package fr.aston.gestionconges.coordination;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fr.aston.gestionconges.metiers.DemandeConges;
import fr.aston.gestionconges.metiers.Employe;
import fr.aston.gestionconges.services.FactoryServices;
import fr.aston.gestionconges.services.IServiceDemandeConges;
import fr.aston.gestionconges.utilitaires.ConversionDate;

/**
 * Servlet implementation class ControleurAjax
 */
public class ControleurAjax extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleurAjax() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        executer(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        executer(request, response);
    }

    protected void executer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=ISO-8859-1");
        response.setHeader("Cache-Control", "no-cache");
        Employe employe = new Employe(2, "test", "1234", "Jack", "Jack", new Date(), "jack@aston.fr", "Manager", 1);
        IServiceDemandeConges serviceDemandeConges = FactoryServices.getServiceDemandeConges();
        List<DemandeConges> demandeconges = serviceDemandeConges.listerDemandesByUser(employe);
        out.println("<table border=1>");
        out.println("<tr>");
        out.println("<td>dateDebut</td>");
        out.println("<td>dateFin</td>");
        out.println("<td>etat</td>");
        out.println("<td>type</td>");
        out.println("<td>date emission</td>");
        out.println("<td>commentaire</td>");
        out.println("</tr>");
        for (DemandeConges demande : demandeconges) {
            out.print("<tr>");
            out.print("<td>");
            out.print(ConversionDate.convertirDateToString(demande.getDateDebut()));
            out.print("</td>");
            out.print("<td>");
            out.print(ConversionDate.convertirDateToString(demande.getDateFin()));
            out.print("</td>");
            out.print("<td>");
            out.print(demande.getEtat());
            out.print("</td>");
            out.print("<td>");
            out.print(demande.getTypeConge());
            out.print("</td>");
            out.print("<td>");
            out.print(ConversionDate.convertirDateToString(demande.getDateEmission()));
            out.print("</td>");
            out.print("<td>");
            out.print(demande.getCommentaire());
            out.print("</td>");
            out.print("<td>");
            out.print("</tr>");
        }
        out.println("</table>");
    }
}
