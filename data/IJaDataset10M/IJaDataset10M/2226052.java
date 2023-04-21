package uia.alumni.search;

import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import uia.alumni.data.User;
import uia.alumni.form.*;
import uia.alumni.web.Page;
import static uia.alumni.form.FieldType.*;
import static uia.alumni.web.Constants.*;
import static uia.alumni.search.Constants.SEARCH;

/**
 *
 * This Servlet is a Demonstration of the Form Validation.
 * @author Simon Zimmermann.
 */
public class Search extends Page {

    public static final String SERVLET_NAME = "search";

    @PersistenceUnit(name = PERSISTENCE_UNIT)
    private EntityManagerFactory emf;

    public Search() {
        super(null);
    }

    /**
    * Write the body HTML for the Form test
    * @param out
    * @param request
    */
    public void writeBody(PrintWriter out, HttpServletRequest request) {
        out.println("<h1>Search</h1>");
        out.print("<form action=" + Search.SERVLET_NAME + ".do method=\"GET\">");
        out.print("<input type=\"text\" name=" + SEARCH + ">");
        out.println("<input type=\"" + SUBMIT + "\" value=\"Search\">");
        out.println("</form>");
        try {
            Form form = new Form(request);
            FormField text = form.newField(SEARCH.toString(), TEXT);
            writeFormErrors(out, request);
            if (form.isValid()) {
                out.println(tagP("You searched for: " + text.getFieldValue()));
            }
        } catch (Exception e) {
            out.println("<pre>");
            out.println(e.getMessage());
            out.println("</pre>");
        } finally {
        }
    }

    public static final long serialVersionUID = 1;
}
