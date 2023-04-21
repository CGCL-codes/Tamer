package com.google.appengine.demos.shardedcounter.v3;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Renders an HTML page showing current count of the sharded counter.
 *
 * Through the form, the user can increment the counter or add new shards.
 *
 */
public class CounterPage extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        displayPageStart(resp);
        ShardedCounter counter = getOrCreateCounter(resp);
        displayCounts(counter, resp);
        displayInputFormAndClose(resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        displayPageStart(resp);
        ShardedCounter counter = getOrCreateCounter(resp);
        String query = req.getQueryString();
        if (query != null && query.equals("addShard=true")) {
            displayCount(counter, resp);
            displayNumberOfShards(counter.addShard(), resp);
        } else {
            counter.increment();
            displayCount(counter, resp);
            displayNumberOfShards(counter.getNumShards(), resp);
        }
        displayInputFormAndClose(resp);
    }

    private void displayPageStart(HttpServletResponse resp) throws IOException {
        resp.getWriter().println("<html>");
        resp.getWriter().println("  <body>");
    }

    /**
   * Creates the sharded counter if it does not yet exist.
   */
    private ShardedCounter getOrCreateCounter(HttpServletResponse resp) throws IOException {
        CounterFactory factory = new CounterFactory();
        ShardedCounter counter = factory.getCounter("test'\"counter");
        if (counter == null) {
            counter = factory.createCounter("test'\"counter");
            counter.addShard();
            resp.getWriter().println("<p>No counter named 'test'\"counter', so we created one.</p>");
        }
        return counter;
    }

    private void displayCount(ShardedCounter counter, HttpServletResponse resp) throws IOException {
        resp.getWriter().println("  <p>Current count: " + counter.getCount() + "</p>");
    }

    private void displayCounts(ShardedCounter counter, HttpServletResponse resp) throws IOException {
        displayCount(counter, resp);
        displayNumberOfShards(counter.getNumShards(), resp);
    }

    private void displayNumberOfShards(int shards, HttpServletResponse resp) throws IOException {
        resp.getWriter().print("<p>Counter has " + shards);
        if (shards == 1) {
            resp.getWriter().println(" shard</p>");
        } else {
            resp.getWriter().println(" shards</p>");
        }
    }

    private void displayInputFormAndClose(HttpServletResponse resp) throws IOException {
        resp.getWriter().println("<form action=\"/v3\" method=\"post\">");
        resp.getWriter().println("  <div><input type=\"submit\" value=\"+1\" /></div>");
        resp.getWriter().println("</form>");
        resp.getWriter().println("<form action=\"/v3?addShard=true\" method=\"post\">");
        resp.getWriter().println("  <div><input type=\"submit\" value=\"Add Shard\" /></div>");
        resp.getWriter().println("</form>");
        resp.getWriter().println("  </body>");
        resp.getWriter().println("</html>");
    }
}
