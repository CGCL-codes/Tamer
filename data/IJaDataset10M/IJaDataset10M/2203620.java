package org.snipsnap.render.filter.links;

import org.radeox.util.Encoder;
import org.radeox.util.i18n.ResourceManager;
import org.radeox.util.logging.Logger;
import org.snipsnap.snip.Links;
import snipsnap.api.snip.SnipLink;
import org.snipsnap.util.URLEncoderDecoder;
import java.io.IOException;
import java.io.Writer;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.net.URL;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Renders backlinks
 *
 * @author Stephan J. Schmidt
 * @version $Id: BackLinks.java 1819 2005-04-06 17:56:22Z stephan $
 */
public class BackLinks {

    private static UrlFormatter formatter = new CutLengthFormatter();

    private static InetAddress googleHost;

    static {
        try {
            googleHost = InetAddress.getByName("www.google.com");
        } catch (UnknownHostException e) {
            Logger.warn("unable to resolve google.com: ", e);
        }
    }

    public static void appendTo(Writer writer, Links backLinks, int count) {
        Iterator iterator = backLinks.iterator();
        try {
            if (iterator.hasNext()) {
                writer.write("<span class=\"caption\">");
                writer.write(ResourceManager.getString("i18n.messages", "backlinks.title"));
                writer.write("</span>\n");
                writer.write("<ul class=\"list\">\n");
                while (iterator.hasNext() && --count >= 0) {
                    String url = (String) iterator.next();
                    writer.write("<li>");
                    writer.write("<span class=\"count\">");
                    writer.write("" + backLinks.getIntCount(url));
                    writer.write("</span>");
                    writer.write(" <span class=\"content\"><a href=\"");
                    writer.write(Encoder.escape(url));
                    writer.write("\">");
                    renderView(writer, url);
                    writer.write("</a></span></li>\n");
                }
                writer.write("</ul>");
            }
        } catch (IOException e) {
            Logger.warn("unable write to writer", e);
        }
    }

    private static void renderView(Writer writer, String url) throws IOException {
        URL urlInfo = new URL(url);
        String info = null;
        try {
            if (googleHost != null && googleHost.equals(InetAddress.getByName(urlInfo.getHost()))) {
                info = getQuery(urlInfo.getQuery(), "q");
                if (info != null) {
                    info = urlInfo.getHost() + ": " + info;
                }
            }
        } catch (UnknownHostException e) {
        }
        if (null == info) {
            info = url;
        }
        writer.write(Encoder.toEntity(info.charAt(0)));
        writer.write(Encoder.escape(SnipLink.cutLength(info.substring(1), 90)));
    }

    private static String getQuery(String query, String id) {
        if (null != query) {
            String vars[] = query.split("&");
            for (int v = 0; v < vars.length; v++) {
                if (vars[v].startsWith(id + "=")) {
                    try {
                        String value = URLEncoderDecoder.decode(vars[v].substring(id.length() + 1), "UTF-8");
                        return value.replace('+', ' ');
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
