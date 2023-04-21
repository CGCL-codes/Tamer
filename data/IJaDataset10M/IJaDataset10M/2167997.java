package org.chemvantage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

public class DataStoreCleaner extends HttpServlet {

    private static final long serialVersionUID = 137L;

    DAO dao = new DAO();

    Subject subject = dao.getSubject();

    public String getServletInfo() {
        return "PZone servlet presents user's detailed scores in the Practice Zone site.";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cleanSessions();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Done.");
    }

    private void cleanSessions() {
        final long now = new Date().getTime();
        final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        final Query query = new Query("_ah_SESSION");
        for (final Entity session : datastore.prepare(query).asIterable(FetchOptions.Builder.withLimit(1000))) {
            Long expires = (Long) session.getProperty("_expires");
            if (expires < now) {
                final Key key = session.getKey();
                datastore.delete(key);
            }
        }
    }
}
