package nz.ac.massey.xmldad.webservice;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import nz.ac.massey.xmldad.bookcatalogue.Book;
import nz.ac.massey.xmldad.bookcatalogue.Catalogue;
import nz.ac.massey.xmldad.bookcatalogue.ObjectFactory;
import nz.ac.massey.xmldad.bookquery.BookSearch;
import nz.ac.massey.xmldad.bookquery.BookSubmission;
import nz.ac.massey.xmldad.bookquery.FeedbackSubmission;
import nz.ac.massey.xmldad.bookquery.Response;

/**
 * Handles requests as described in bookQuery.xsd.
 * @author Nathan C Jones
 * @version 0.2
 */
public class CatalogueServlet extends HttpServlet {

    private static final long serialVersionUID = 5401359136721441331L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JAXBContext context = JAXBContext.newInstance("nz.ac.massey.xmldad.bookquery");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object requestObject = unmarshaller.unmarshal(request.getInputStream());
            Object responseObject = null;
            Marshaller marshaller = context.createMarshaller();
            if (requestObject instanceof BookSubmission) responseObject = submitBook((BookSubmission) requestObject); else if (requestObject instanceof FeedbackSubmission) responseObject = submitFeedback((FeedbackSubmission) requestObject); else if (requestObject instanceof BookSearch) {
                responseObject = searchBook((BookSearch) requestObject);
                marshaller = JAXBContext.newInstance("nz.ac.massey.xmldad.bookcatalogue").createMarshaller();
            } else {
                response.getOutputStream().println("unexpected object of type: " + requestObject.getClass());
                return;
            }
            marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type=\"text/xsl\" href=\"catalogue.xsl\"?>");
            marshaller.marshal(responseObject, response.getOutputStream());
            response.setContentType("application/xml");
        } catch (JAXBException e) {
            response.getOutputStream().println("error " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * @param bookSubmission
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
    private Response submitBook(BookSubmission bookSubmission) throws IOException, JAXBException {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();
        hibernateSession.beginTransaction();
        hibernateSession.saveOrUpdate(bookSubmission.getBook());
        hibernateSession.getTransaction().commit();
        Response response = new nz.ac.massey.xmldad.bookquery.ObjectFactory().createResponse();
        response.setStatus("ok");
        response.setMessage("Book successfully added to catalog");
        return response;
    }

    /**
	 * @param feedbackSubmission
	 * @return
	 * @throws JAXBException 
	 */
    private Response submitFeedback(FeedbackSubmission feedbackSubmission) throws JAXBException {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();
        hibernateSession.beginTransaction();
        Book book = (Book) hibernateSession.createCriteria(Book.class).add(Restrictions.eq("Isbn", feedbackSubmission.getIsbn())).uniqueResult();
        Response response = new nz.ac.massey.xmldad.bookquery.ObjectFactory().createResponse();
        if (book != null) {
            book.getFeedback().add(feedbackSubmission.getFeedback());
            hibernateSession.saveOrUpdate(book);
            hibernateSession.getTransaction().commit();
            response.setStatus("ok");
            response.setMessage("Feedback successfully added to book: " + feedbackSubmission.getIsbn());
        } else {
            response.setStatus("error");
            response.setMessage("No book with isbn: " + feedbackSubmission.getIsbn());
        }
        return response;
    }

    /**
	 * 
	 * @param search
	 * @param out
	 * @throws IOException 
	 * @throws JAXBException 
	 */
    private Catalogue searchBook(BookSearch search) throws IOException, JAXBException {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();
        Criterion searchCriterion = createSearchCriterion(search);
        ObjectFactory factory = new nz.ac.massey.xmldad.bookcatalogue.ObjectFactory();
        Catalogue catalogue = factory.createCatalogue();
        if (searchCriterion != null) {
            hibernateSession.beginTransaction();
            catalogue.getBook().addAll(hibernateSession.createCriteria(Book.class).add(searchCriterion).list());
            hibernateSession.getTransaction().commit();
        }
        return catalogue;
    }

    /**
	 * Create a hibernate search criterion based on a book search.
	 * The resulting search criterion will join each non-null and
	 * non-empty search criteria from the book search by logical disjunction.
	 * @param search the object describing the properties to search for.
	 * @return a new search criterion based on the book search or null
	 * if all of the search properties are null or empty strings.
	 */
    private Criterion createSearchCriterion(BookSearch search) {
        Criterion searchCriterion = null;
        if (search.getAuthor() != null && !search.getAuthor().equals("")) {
            searchCriterion = Restrictions.ilike("Author", search.getAuthor(), MatchMode.ANYWHERE);
        }
        if (search.getTitle() != null && !search.getTitle().equals("")) {
            Criterion titleCriterion = Restrictions.ilike("Title", search.getTitle(), MatchMode.ANYWHERE);
            if (searchCriterion == null) searchCriterion = titleCriterion; else searchCriterion = Restrictions.or(searchCriterion, titleCriterion);
        }
        if (search.getIsbn() != null && !search.getIsbn().equals("")) {
            Criterion isbnCriterion = Restrictions.eq("Isbn", search.getIsbn());
            if (searchCriterion == null) searchCriterion = isbnCriterion; else searchCriterion = Restrictions.or(searchCriterion, isbnCriterion);
        }
        if (search.getYear() != null) {
            Criterion yearCriterion = Restrictions.eq("Year", search.getYear());
            if (searchCriterion == null) searchCriterion = yearCriterion; else searchCriterion = Restrictions.or(searchCriterion, yearCriterion);
        }
        return searchCriterion;
    }
}
