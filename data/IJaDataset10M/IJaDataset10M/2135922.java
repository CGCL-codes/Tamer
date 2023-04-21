package org.eugenes.lusearch.handlers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import org.eugenes.lusearch.search.*;
import org.eugenes.lusearch.search.lucene.*;
import org.eugenes.lusearch.servlets.ArgosParameters;
import org.eugenes.lusearch.settings.ArgosDefaults;
import org.eugenes.lusearch.output.ArgosOutputXml;
import org.eugenes.lusearch.util.ArgosUtils;
import java.io.IOException;
import java.io.File;
import java.util.Properties;

/**
 * TODO: we need to implement some tests to confirm that
 * certain parameters are defined and throw an exception 
 * if they aren't defined.
 * 
 * This handler handles a search request.  It is responsible for 
 * collecting the data supplied by the search fields, creating
 * a query, and searching the requested search system(s) (currently
 * only lucene is implemented).  Once the search has been performed,
 * an ArgosSearchContainer object is created to hold relevant objects
 * and data.  This object and several other items are added to the
 * session.  The appropriate JSP page to generate a search result
 * page is returned.
 * 
 * This class implements the RequestHandler interface, which is 
 * necessary for the ArgosControllerServlet to dole out the work
 * to the appropriate handler automatically based on the requested 
 * html page.
 * 
 * @author Paul Poole
 *
 */
public class ArgosSearchHandler implements RequestHandler {

    public static String[] SORT_FIELDS = ArgosDefaults.INDEX_FIELDS;

    public static String DEFAULT_SEARCH_FIELD = ArgosDefaults.DEFAULT_SEARCH_FIELD;

    public static String OUTPUT_FORMAT = ArgosDefaults.DEFAULT_OUTPUT_FORMAT;

    public static String SEARCH_XSL_HTML = "searchReport.xsl";

    public static String SEARCH_XSL_XML = "searchReportXML.xsl";

    private static int numberResultsPerPage = (int) ArgosDefaults.NUM_DOCS_MEMORY / 2;

    private static int numberResultsMemory = ArgosDefaults.NUM_DOCS_MEMORY;

    private static int maxBatchResults;

    private static String[] sortFields;

    private static String[] displayFields;

    private static String defaultSearchField;

    private static String outputFormat;

    private static String searchXslHtml;

    private static String searchXslXml;

    private static Properties defaultProps;

    static {
        defineDefaults();
    }

    /**
	 * Private method that defines all the static variables.  This
	 * is called once, when the class is instantiated.
	 */
    private static final void defineDefaults() {
        sortFields = ArgosUtils.getPropertyArray(ArgosParameters.getProperty("sortFields"));
        if (sortFields == null) sortFields = SORT_FIELDS;
        displayFields = ArgosUtils.getPropertyArray(ArgosParameters.getProperty("displayFields"));
        if (defaultProps == null) defaultProps = ArgosParameters.getProperties();
        defaultSearchField = ArgosParameters.getProperty("defaultSearchField", DEFAULT_SEARCH_FIELD);
        outputFormat = ArgosParameters.getProperty("format", OUTPUT_FORMAT);
        searchXslHtml = ArgosParameters.getProperty("searchXslHtml", SEARCH_XSL_HTML);
        searchXslXml = ArgosParameters.getProperty("searchXslPrettyXml", SEARCH_XSL_XML);
        String nrpp = ArgosParameters.getProperty("numberResultsPerPage");
        if (nrpp != null) {
            numberResultsPerPage = Integer.parseInt(nrpp);
            ArgosSearchInterface.setNumSearchResultsPerPage(numberResultsPerPage);
        }
        String maxBR = ArgosParameters.getProperty("maxBatchDownload");
        if (maxBR != null) {
            maxBatchResults = Integer.parseInt(maxBR);
            ArgosSearchInterface.setMaxBatchDownloads(maxBatchResults);
        }
        String nResultsMem = ArgosParameters.getProperty("numResultsMemory");
        if (nResultsMem != null) numberResultsMemory = Integer.parseInt(nResultsMem);
    }

    public static void setLibrary(String libname, HttpSession session, ServletContext scontext) {
        try {
            String libprops = ArgosParameters.getProperty("CONF_ROOT", "conf");
            libprops = scontext.getRealPath(libprops);
            File lp = new File(libprops, libname + ".properties");
            if (lp.exists()) {
                ArgosParameters.setParameters(lp.toString(), defaultProps);
                if (session != null) session.setAttribute("ArgosParameters", ArgosParameters.getProperties());
                defineDefaults();
            }
        } catch (Exception ex) {
        }
    }

    /**
	 * Implements the handleRequest method of the RequestHandler
	 * interface.  Handles the search request by parsing the 
	 * request parameters, creating a query and calling the 
	 * search engine(s).  This method returns the jsp page that 
	 * displays search results.
	 * @param request  Servlet request object.
	 * @param response Servlet response object.
	 * @return The jsp page the generates the results page.
	 * @throws ServletException
	 * @throws IOException
	 */
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ArgosParameters.getProperty("reportURL") == null) ArgosParameters.setProperty("reportURL", getReportServer(request));
        try {
            String libname = request.getParameter("library");
            if (libname == null) libname = ArgosParameters.getProperty("LIB_NAME");
            if (libname == null) throw new ServletException("Need a library to search .");
            HttpSession session = request.getSession();
            if (!libname.equals(session.getAttribute("currentlib"))) {
                setLibrary(libname, session, session.getServletContext());
                session.setAttribute("currentlib", libname);
            }
            LuceneParameters lp = getLuceneParameters(libname);
            ArgosSearchContainer searchContainer = doSearches(request, lp);
            session.setAttribute("searchContainer", searchContainer);
            ArgosOutputXml output = new ArgosOutputXml();
            session.setAttribute("argosOutput", output);
            session.setAttribute("format", outputFormat);
            session.setAttribute("searchXslHtml", searchXslHtml);
            session.setAttribute("searchXslPrettyXml", searchXslXml);
            if (displayFields != null) session.setAttribute("displayFields", displayFields);
        } catch (Exception ex) {
            ArgosExceptionHandler.handleException(request, response, ex);
        }
        return ArgosParameters.getProperty("searchResultsJsp");
    }

    /**
	 * Place all implemented searches here..
	 * As of yet, it has not been decided how the results for
	 *  multiple implementations will be handled.
	 *  For now... since we have only one implementation, return
	 *  the one.  
	 * TODO: An idea for the future -- Create a new obj to hold all 
	 *  results ... it should have merge functions and individual
	 *  list functions.
	 * @param request Servlet request object.
	 * @param lp      The LuceneParameters object used for searching 
	 * 				  via the lucene search engine.
	 * @return  	  ArgosSearchContainer which will be placed in the
	 * 				  session object for access by the JSPs.
	 * @throws Exception
	 */
    private ArgosSearchContainer doSearches(HttpServletRequest request, LuceneParameters lp) throws Exception {
        ArgosQueryComponents components = getArgosQueryComponents(request);
        ArgosSearchContainer container = doLuceneSearch(lp, components);
        return container;
    }

    /**
	 * Performs a lucene search.
	 * @param lp  		 The LuceneParameters object.
	 * @param components ArgosQueryComponents which contain all the 
	 * 					 query components that will be used to create
	 * 					 a search query.
	 * @return			 ArgosSearchContainer containing the search
	 * 					 results and other relevant info/objects.
	 * @throws Exception
	 */
    private ArgosSearchContainer doLuceneSearch(LuceneParameters lp, ArgosQueryComponents components) throws Exception {
        ArgosSearch as = ArgosSearchInterface.getArgosLuceneSearch(lp, defaultSearchField, sortFields, numberResultsMemory, components);
        ArgosSearchContainer container = ArgosSearchInterface.performSearch(as);
        container.setTotalNumDocuments(lp.getTotalDocs());
        return container;
    }

    /**
	 * Creates or gets a LuceneParameters object for the requested 
	 * index directory.
	 * @param libname     The library name 
	 * 					   INDEX_ROOT / libname = location of the indexed data.
	 * @return			   LuceneParameters object containing the necessary
	 * 					   parameters/objects to use the lucene search engine.
	 * @throws IOException
	 * @throws Exception
	 */
    private LuceneParameters getLuceneParameters(String libname) throws IOException, Exception {
        LuceneParameters lp = (LuceneParameters) ArgosParameters.getObject(libname);
        if (lp == null) {
            lp = new LuceneParameters(libname);
            ArgosParameters.putObject(libname, lp);
        }
        return lp;
    }

    /**
	 * Gets an ArgosQueryComponents object, which is generated from
	 * the components of the search page (e.g. query field, wildcard, 
	 * etc.)
	 * @param request    Servlet request object.
	 * @return         	 ArgosQueryComponents used to create search query.
	 * @throws Exception
	 */
    private ArgosQueryComponents getArgosQueryComponents(HttpServletRequest request) throws Exception {
        String[] fields = request.getParameterValues("field");
        boolean wildCard = ArgosUtils.boolOf(request.getParameter("wild"));
        String query = request.getParameter("query");
        boolean noPredicted = ArgosUtils.boolOf(request.getParameter("noPredicted"));
        ArgosQueryComponents components = new ArgosQueryComponents();
        components.addQueryComponent(ArgosSearchInterface.getArgosQueryComponent(fields, query, wildCard, "+"));
        if (noPredicted) {
            components.addQueryComponent(ArgosSearchInterface.getArgosQueryComponent("CLA", "Predicted", "-"));
        }
        String[] orgs = request.getParameterValues("organism");
        if (orgs != null) components.addQueryComponent(ArgosSearchInterface.getArgosQueryComponent(new String[] { "ORG" }, orgs, "+"));
        return components;
    }

    /**
	 * Creates the gene report server name.  Since the gene reports 
	 * are not generated by java (they are generate by perl), the 
	 * work is done by a different port (at least) and in some cases 
	 * a different server all together (usually for testing servers 
	 * that run the tomcat server only). This method returns the 
	 * server name with protocol and port number.
	 * 
	 * TODO: surely there is a better way... don't have time
	 *       to figure out what that is right now...
	 * @param req  	Servlet request object.
	 * @return   	String containing the fully qualified report 
	 * 				server name.
	 */
    private String getReportServer(HttpServletRequest req) {
        StringBuffer sb = new StringBuffer();
        sb.append(doGetReportServerName(req));
        String port = doGetReportServerPort(req);
        if (!port.equals("80")) {
            sb.append(":");
            sb.append(port);
        }
        return sb.toString();
    }

    /**
	 * Gets the report server name.  This checks
	 * to see if a report server was defined in the properties file -
	 * if not, then the current server name is used.
	 * @param req  	Servlet request object.
	 * @return 		String containing the report server name.
	 */
    private final String doGetReportServerName(HttpServletRequest req) {
        String rServer = ArgosParameters.getProperty("reportServer");
        if (rServer == null) return "http://" + req.getServerName();
        return rServer;
    }

    /**
	 * Gets the report server port.  This checks to see if a report 
	 * server port was defined in the properties file - if not, then 
	 * the current server port is used.  As long as we continue to 
	 * use the old perl system to generate reports, this will probably 
	 * always use a different port than the current one.
	 * @param req  Servlet request object.
	 * @return     String containing the report server port.
	 */
    private final String doGetReportServerPort(HttpServletRequest req) {
        String rPort = ArgosParameters.getProperty("reportPort");
        if (rPort == null) return Integer.toString(req.getServerPort());
        return rPort;
    }
}
