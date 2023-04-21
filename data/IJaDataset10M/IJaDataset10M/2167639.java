package org.magicbox.servlet;

import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.magicbox.service.FeedService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * Servlet che fornisce i feed rss e atom
 * 
 * @author Massimiliano Dess� (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class FeedServlet extends HttpServlet {

    public void init() {
        defaultFeedType = getServletConfig().getInitParameter(DEFAULT_FEED_TYPE);
        WebApplicationContext springCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        feedService = (FeedService) springCtx.getBean(getServletConfig().getInitParameter(FEED_SERVICE));
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            SyndFeed feed = null;
            try {
                feed = feedService.getFeed(defaultFeedType);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.setContentType(MIME_TYPE);
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, res.getWriter());
            lastModified = System.currentTimeMillis();
        } catch (FeedException ex) {
            String msg = COULD_NOT_GENERATE_FEED_ERROR;
            log(msg, ex);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
        }
    }

    public long getLastModified(HttpServletRequest req) {
        return lastModified;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    private FeedService feedService;

    private String defaultFeedType;

    private long lastModified = 0;

    private static final String DEFAULT_FEED_TYPE = "default.feed.type";

    private static final String FEED_SERVICE = "feedService";

    private static final String COULD_NOT_GENERATE_FEED_ERROR = "Could not generate feed";

    private static final long serialVersionUID = -7235549547971068042L;

    private static final String MIME_TYPE = "application/xml; charset=UTF-8";
}
