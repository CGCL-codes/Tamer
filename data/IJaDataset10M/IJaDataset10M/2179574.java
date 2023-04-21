package edu.asu.quadriga.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.asu.quadriga.cytoscope.AGraphCreator;
import edu.asu.quadriga.cytoscope.JSONHelper;
import edu.asu.quadriga.cytoscope.RelNodeGraphCreator;
import edu.asu.quadriga.cytoscope.SearchHelper;
import edu.asu.quadriga.cytoscope.single.CytoscopeGraphCreator;
import edu.asu.quadriga.impl.elements.Concept;
import edu.asu.quadriga.impl.elements.Term;
import edu.asu.quadriga.impl.events.AppellationEvent;
import edu.asu.quadriga.interfaces.elements.IConcept;
import edu.asu.quadriga.interfaces.elements.IElement;
import edu.asu.quadriga.interfaces.elements.IRelation;
import edu.asu.quadriga.interfaces.elements.ITerm;
import edu.asu.quadriga.interfaces.events.IAppellationEvent;
import edu.asu.quadriga.interfaces.events.ICreationEvent;
import edu.asu.quadriga.interfaces.events.IRelationEvent;
import edu.asu.quadriga.quadruple.ElementCache;
import edu.asu.quadriga.quadruple.QuadrupleManager;
import edu.asu.quadriga.search.SearchManager;

/**
 * Servlet implementation class SearchServelt
 */
public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(ServletParamter.LOGIN_IS_LOGGED_IN) == null || session.getAttribute(ServletParamter.LOGIN_IS_LOGGED_IN).equals(false)) {
            RequestDispatcher view = request.getRequestDispatcher("restricted.jsp");
            view.forward(request, response);
            return;
        }
        QuadrupleManager manager = new QuadrupleManager(getServletContext(), (ElementCache) request.getSession().getAttribute(ServletParamter.SESSION_ELEMENT_CACHE));
        String searchTerm = request.getParameter("searchTerm");
        String data = "";
        if (searchTerm != null && !searchTerm.isEmpty()) {
            CytoscopeGraphCreator graphCreator = new CytoscopeGraphCreator(manager);
            SearchManager search = new SearchManager(manager);
            List<IAppellationEvent> events = search.getSearchedAppellations(searchTerm);
            List<ICreationEvent> relations = new ArrayList<ICreationEvent>();
            for (IAppellationEvent event : events) {
                relations.addAll(search.getParentRelations(event));
            }
            data = graphCreator.getGraphData(relations, "");
        }
        request.setAttribute("data", data);
        RequestDispatcher view = request.getRequestDispatcher("quadruplesNetwork.jsp");
        view.forward(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
