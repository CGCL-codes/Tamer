package com.google.code.clickstream;

import java.io.PrintWriter;
import java.util.Map;
import javax.portlet.PortletURL;

/**
 * This class provides common utilities for extending clickstream.
 *
 * @author <a href="mailto:contact@chenwang.org">Chen Wang</a>
 */
public class ClickstreamExtensionUtils {

    private ClickstreamExtensionUtils() {
    }

    private static String detectShowbots(String value) {
        String showbots = "false";
        if ("true".equalsIgnoreCase(value)) {
            showbots = "true";
        } else if ("both".equalsIgnoreCase(value)) {
            showbots = "both";
        }
        return showbots;
    }

    private static String createUrl(String showbots, String sid, PortletURL pu) {
        StringBuffer sb = new StringBuffer();
        if (pu != null) {
            pu.setParameter("showbots", showbots);
            if (sid != null) {
                pu.setParameter("sid", sid);
            }
            sb.append(pu.toString());
        } else {
            sb.append("?").append("showbots=").append(showbots);
            if (sid != null) {
                sb.append("&").append("sid=").append(sid);
            }
        }
        return sb.toString();
    }

    /**
     * Prints the page header.
     * @param out output writer
     * @param title page title
     */
    private static void printHeader(PrintWriter out, String title) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" ");
        out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
        out.println("<head><title>");
        out.println(title);
        out.println("</title></head>");
        out.println("<body>");
        out.print("<h2>");
        out.print(title);
        out.println("</h2>");
    }

    public static void printClickstreamList(Map<String, Clickstream> clickstreams, PrintWriter out, boolean isFragment, String sb) {
        printClickstreamList(clickstreams, out, isFragment, sb, null);
    }

    /**
     * Print out the active clickstream list.
     *
     * @param request HttpServletRequest
     * @param out PrintWriter
     */
    public static void printClickstreamList(Map<String, Clickstream> clickstreams, PrintWriter out, boolean isFragment, String sb, PortletURL pu) {
        if (!isFragment) {
            printHeader(out, "Active Clickstreams");
        }
        out.println("<p>");
        String showbots = detectShowbots(sb);
        if ("true".equals(showbots)) {
            out.println("<a href=\"" + createUrl("false", null, pu) + "\">User Streams</a>");
            out.println(" | ");
            out.println("<strong>Bot Streams</strong>");
        } else if ("both".equalsIgnoreCase(showbots)) {
            out.println("<a href=\"" + createUrl("false", null, pu) + "\">User Streams</a>");
            out.println(" | ");
            out.println("<a href=\"" + createUrl("true", null, pu) + "\">Bot Streams</a>");
        } else {
            out.println("<strong>User Streams</strong>");
            out.println(" | ");
            out.println("<a href=\"" + createUrl("true", null, pu) + "\">Bot Streams</a>");
        }
        out.println(" | ");
        if (!"both".equalsIgnoreCase(showbots)) {
            out.println("<a href=\"" + createUrl("both", null, pu) + "\">Both</a>");
        } else {
            out.println("<strong>Both</strong>");
        }
        out.println("</p>");
        if (clickstreams.isEmpty()) {
            out.println("<p>No clickstreams in progress.</p>");
        } else {
            synchronized (clickstreams) {
                out.print("<p><ol>");
                for (Map.Entry<String, Clickstream> entry : clickstreams.entrySet()) {
                    String key = entry.getKey();
                    Clickstream stream = entry.getValue();
                    if (showbots.equals("false") && stream.isBot()) {
                        continue;
                    } else if (showbots.equals("true") && !stream.isBot()) {
                        continue;
                    }
                    String hostname = (stream.getHostname() != null && !"".equals(stream.getHostname()) ? stream.getHostname() : "Stream");
                    out.print("<li>");
                    out.print("<a href=\"" + createUrl(showbots, key, pu) + "\">");
                    out.write("<strong>");
                    out.print(hostname);
                    out.print("</strong>");
                    out.print("</a> ");
                    out.print("<small>[");
                    out.print(stream.getStream().size());
                    out.print(" reqs]</small>");
                    out.print("</li>");
                }
                out.print("</ol></p>");
            }
        }
    }

    public static void printClickstreamDetail(Map<String, Clickstream> clickstreams, PrintWriter out, String sid, boolean isFragment, String sb) {
        printClickstreamDetail(clickstreams, out, sid, isFragment, sb, null);
    }

    /**
     * Received the "sid" parameter, print out the stream detail.
     *
     * @param request HttpServletRequest
     * @param out PrintWriter
     * @param sid session id
     */
    public static void printClickstreamDetail(Map<String, Clickstream> clickstreams, PrintWriter out, String sid, boolean isFragment, String sb, PortletURL pu) {
        Clickstream stream = clickstreams.get(sid);
        out.println("<p><a href=\"" + createUrl(sb, null, pu) + "\">All streams</a></p>");
        if (stream == null) {
            if (!isFragment) {
                printHeader(out, "Clickstream for " + sid);
            }
            out.write("<p>Session for " + sid + " has expired.</p>");
            return;
        }
        if (!isFragment) {
            printHeader(out, "Clickstream for " + stream.getHostname());
        }
        out.println("<p><ul>");
        if (stream.getInitialReferrer() != null) {
            out.println("<li>");
            out.println("<strong>Initial Referrer</strong>: ");
            out.print("<a href=\"");
            out.print(stream.getInitialReferrer());
            out.print("\">");
            out.print(stream.getInitialReferrer());
            out.println("</a>");
            out.println("</li>");
        }
        out.println("<li>");
        out.println("<strong>Hostname</strong>: ");
        out.println(stream.getHostname());
        out.println("</li>");
        out.println("<li>");
        out.println("<strong>Session ID</strong>: ");
        out.println(sid);
        out.println("</li>");
        out.println("<li>");
        out.println("<strong>Bot</strong>: ");
        out.println(stream.isBot() ? "Yes" : "No");
        out.println("</li>");
        out.println("<li>");
        out.println("<strong>Stream Start</strong>: ");
        out.println(stream.getStart());
        out.println("</li>");
        out.println("<li>");
        out.println("<strong>Last Request</strong>: ");
        out.println(stream.getLastRequest());
        out.println("</li>");
        out.println("<li>");
        out.println("<strong>Session Length</strong>: ");
        long streamLength = stream.getLastRequest().getTime() - stream.getStart().getTime();
        if (streamLength > 3600000) {
            out.print((streamLength / 3600000) + " hours ");
        }
        if (streamLength > 60000) {
            out.print(((streamLength / 60000) % 60) + " minutes ");
        }
        if (streamLength > 1000) {
            out.print(((streamLength / 1000) % 60) + " seconds");
        }
        out.println("</li>");
        out.println("<li>");
        out.println("<strong># of Requests</strong>: ");
        out.println(stream.getStream().size());
        out.println("</li>");
        out.println("</ul></p>");
        out.println("<h3>Click stream:</h3>");
        synchronized (stream) {
            out.print("<p><ol>");
            for (ClickstreamRequest cr : stream.getStream()) {
                String click = cr.toString();
                out.write("<li>");
                out.write("<a href=\"http://");
                out.print(click);
                out.write("\">");
                out.print(click);
                out.write("</a>");
                out.write("</li>");
            }
            out.print("</ol></p>");
        }
    }
}
