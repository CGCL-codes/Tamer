package com.jaeksoft.searchlib.web;

import java.net.URI;
import java.net.URISyntaxException;
import com.jaeksoft.searchlib.Client;
import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.user.Role;
import com.jaeksoft.searchlib.user.User;

public class ActionServlet extends AbstractServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -369063857059673597L;

    @Override
    protected void doRequest(ServletTransaction transaction) throws ServletException {
        try {
            Client client = transaction.getClient();
            String action = transaction.getParameterString("action");
            User user = transaction.getLoggedUser();
            if (user != null && !user.hasRole(client.getIndexName(), Role.INDEX_UPDATE)) throw new SearchLibException("Not permitted");
            if ("optimize".equalsIgnoreCase(action)) client.optimize(); else if ("swap".equalsIgnoreCase(action)) {
                long version = transaction.getParameterLong("version", 0);
                boolean deleteOld = transaction.getParameterBoolean("deleteOld", false);
                client.getIndex().swap(version, deleteOld);
            } else if ("reload".equalsIgnoreCase(action)) {
                client.reload();
            } else if ("online".equalsIgnoreCase(action)) client.getIndex().setOnline(true); else if ("offline".equalsIgnoreCase(action)) client.getIndex().setOnline(false);
            transaction.addXmlResponse("Status", "OK");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static void optimize(URI uri, String indexName) throws SearchLibException, URISyntaxException {
        call(buildUri(uri, "/action", indexName, "action=optimize"));
    }

    public static void reload(URI uri) throws SearchLibException, URISyntaxException {
        call(buildUri(uri, "/action", null, "action=reload"));
    }

    public static void swap(URI uri, String indexName, long version, boolean deleteOld) throws SearchLibException, URISyntaxException {
        StringBuffer query = new StringBuffer("action=swap");
        query.append("&version=");
        query.append(version);
        if (deleteOld) query.append("&deleteOld");
        call(buildUri(uri, "/action", indexName, query.toString()));
    }

    public static void online(URI uri, String indexName) throws SearchLibException, URISyntaxException {
        call(buildUri(uri, "/action", indexName, "action=online"));
    }

    public static void offline(URI uri, String indexName) throws SearchLibException, URISyntaxException {
        call(buildUri(uri, "/action", indexName, "action=offline"));
    }
}
