package com.javaeedev.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractRssController extends AbstractBaseController {

    protected static final int MAX_RSS_ITEMS = 20;

    private static final String ATTR_RSS = "attr_rss";

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<rss version=\"2.0\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\" xmlns:wfw=\"http://wellformedweb.org/CommentAPI/\" xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\">\n";

    private static final String CDATA_START = "<![CDATA[ ";

    private static final String CDATA_END = " ]]>";

    protected abstract RssData getRssData(HttpServletRequest request, HttpServletResponse response) throws Exception;

    @Override
    protected void afterComplete(HttpServletRequest request, HttpServletResponse response) {
        RssData data = (RssData) request.getAttribute(ATTR_RSS);
        List<RssItem> items = data.getItems();
        response.setContentType("text/xml;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(XML_HEADER);
            writer.write("<channel><title>");
            writer.write(data.getTitle());
            writer.write("</title><link>");
            writer.write(data.getLink());
            writer.write("</link><description>");
            writer.write(CDATA_START);
            writer.write(data.getDescription());
            writer.write(CDATA_END);
            writer.write("</description><language>zh-cn</language><lastBuildDate>");
            writer.write(data.getDate());
            writer.write("</lastBuildDate><pubDate>");
            writer.write(data.getDate());
            writer.write("</pubDate><generator>JavaEEdev RSS Version 1.0</generator>");
            for (RssItem rss : items) {
                writer.write("<item><title>");
                writer.write(CDATA_START);
                writer.write(rss.getTitle());
                writer.write(CDATA_END);
                writer.write("</title><link>");
                writer.write(rss.getLink());
                writer.write("</link><guid>");
                writer.write(rss.getGuid());
                writer.write("</guid><comments>");
                writer.write(rss.getLink());
                writer.write("#comments");
                writer.write("</comments><dc:creator>");
                writer.write(rss.getAuthor());
                writer.write("</dc:creator><pubDate>");
                writer.write(rss.getDate());
                writer.write("</pubDate><description>");
                writer.write(CDATA_START);
                writer.write(rss.getDescription());
                writer.write(CDATA_END);
                writer.write("</description></item>");
            }
            writer.write("</channel></rss>");
            writer.flush();
        } catch (IOException e) {
            log.error("IOException", e);
        }
    }

    @Override
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute(ATTR_RSS, getRssData(request, response));
        return null;
    }
}
