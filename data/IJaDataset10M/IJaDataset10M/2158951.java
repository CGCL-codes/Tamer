package controllers.web;

import business.BookListing;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.BooksZenBooks;
import util.RequestHelper;

/**
 * Handles the display of the user's control panel (UCP).
 *
 * @author Rick Varella
 * @version 12.17.2009
 */
public class UserControlsServlet extends HttpServlet {

    private String dbConfigResource;

    private String jspPath;

    private BooksZenBooks bzb;

    /**
     * Initializes the servlet and sets up required instance variables.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        dbConfigResource = getServletContext().getInitParameter("dbConfigResource");
        jspPath = getServletContext().getInitParameter("jspPath");
    }

    /**
     * Handles all incoming POST requests to the servlet.
     *
     * @param request The contents of the HTTP request.
     * @param response The contents of the HTTP response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        bzb = new BooksZenBooks("en", dbConfigResource);
        String forwardUrl;
        String pageTitle;
        String action = RequestHelper.getString("action", request);
        RequestDispatcher dispatcher;
        bzb.getLexicon().load("global");
        bzb.getLexicon().load("subject");
        bzb.getLexicon().load("ucp");
        if (bzb.getAuthenticatedUser(request) == null) {
            bzb.getLexicon().load("error");
            bzb.getLexicon().load("register");
            forwardUrl = jspPath + "401.jsp";
            pageTitle = bzb.getLexicon().get("unauthorized");
        } else if (action.equals("listings")) {
            bzb.getLexicon().load("book");
            bzb.getLexicon().load("listing");
            bzb.getLexicon().load("search");
            forwardUrl = jspPath + "myListings.jsp";
            pageTitle = bzb.getLexicon().get("myListings");
            request.setAttribute("listings", getUserListings(request));
        } else if (action.equals("profile")) {
            forwardUrl = "/editProfile";
            pageTitle = bzb.getLexicon().get("editProfile");
        } else {
            forwardUrl = jspPath + "myControls.jsp";
            pageTitle = bzb.getLexicon().get("myControls");
        }
        request.setAttribute("config", bzb.getConfig().getSettings());
        request.setAttribute("lexicon", bzb.getLexicon().getLexicons());
        request.setAttribute("language", bzb.getLexicon().getLanguage());
        request.setAttribute("subjects", bzb.getSubjects());
        request.setAttribute("pageTitle", pageTitle);
        dispatcher = getServletContext().getRequestDispatcher(forwardUrl);
        dispatcher.forward(request, response);
    }

    /**
     * Handles all incoming GET requests to the servlet.
     *
     * @param request The contents of the HTTP request.
     * @param response The contents of the HTTP response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private ArrayList<BookListing> getUserListings(HttpServletRequest request) {
        ArrayList<BookListing> listings = new ArrayList<BookListing>();
        BookListing listing;
        String[] fields = { "l.*", "b.*, u.*" };
        String where = "l.userId = " + bzb.getAuthenticatedUser(request).getUserId();
        String[] join = { "INNER JOIN bzb.book b ON l.isbn=b.isbn", "INNER JOIN bzb.user u ON l.userId=u.userId" };
        ResultSet result = bzb.getDriver().select("booklisting l", fields, where, join, null, null, null, 0, 0);
        try {
            while (result.next()) {
                listing = new BookListing();
                listing.init(bzb.getDriver());
                listing.populate(result);
                listings.add(listing);
            }
        } catch (SQLException e) {
        }
        return listings;
    }
}
