package fr.dauphine.bookstore.backoffice.author;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import fr.dauphine.bookstore.commun.MessagesUtil;
import fr.dauphine.bookstore.hibernate.HibernateUtil;
import fr.dauphine.bookstore.modele.Author;

/**
 * Suppression d'un auteur.
 */
public final class RemoveAuthorServlet extends HttpServlet {

    /**
	 * Serial version UID.
	 */
    private static final long serialVersionUID = 6691764298927858547L;

    /**
	 * Traite la methode GET.
	 * 
	 * @param request
	 *            Requete
	 * @param response
	 *            Reponse
	 * @throws ServletException
	 *             Erreur de servlet
	 * @throws IOException
	 *             Erreur d'entree/sortie
	 */
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Session hSession = HibernateUtil.getSessionFactory().getCurrentSession();
        Long id = Long.valueOf(request.getParameter("id"));
        Author a = (Author) hSession.get(Author.class, id);
        if (!a.getBooks().isEmpty()) {
            MessagesUtil.addMessage(request, MessagesUtil.ERROR, "L'auteur " + a + " a ecrit des livres encore en vente");
            request.getRequestDispatcher("authors.jsp").forward(request, response);
        } else {
            hSession.delete(a);
            response.sendRedirect("authors.jsp");
        }
    }
}
