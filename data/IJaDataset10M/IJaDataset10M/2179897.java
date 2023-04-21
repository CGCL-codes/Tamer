package org.jamwiki.parser.alt;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.jamwiki.Environment;

/**
 * Controls generation of external links based on the linking.properties file at the bottom
 * of the classpath.
 *
 * The file controls how link syntax is hyperlinked
 * Syntax: prefix=mylink
 * mylink can include the keyword expansion ${url} to add the suffix of the link
 * if you want to have the external link open in a new frame, or in a different frame,
 * a key of the form "prefix.target" can be specified, e.g.:
 *
 * c2=http://c2.com/cgi/wiki?${url}
 * c2.target=#blank
 */
public class LinkExtender {

    private static final Logger logger = Logger.getLogger(LinkExtender.class);

    private static final String LINKING_PROPERTIES_FILE = "/linking.properties";

    public static final String URL_KEYWORD = "${url}";

    private static Properties linkingProperties;

    static {
        linkingProperties = Environment.loadProperties(LINKING_PROPERTIES_FILE);
    }

    /**
	 * Use the prefix to produce a hyperlink based on entries in the linking.properties file
	 *
	 * @param prefix the prefix
	 * @param url	the parameter as given by the user
	 * @param text
	 * @return
	 * @throws Exception
	 */
    public static String generateLink(String prefix, String url, String text) throws Exception {
        return generateLink(prefix, url, text, false);
    }

    /**
	 * Use the prefix to produce a hyperlink based on entries in the linking.properties file
	 *
	 * @param prefix the prefix
	 * @param url	the parameter as given by the user
	 * @param text
	 * @param onlyExternalLinks true if there are only external links converted, false otherwise.
	 * @return HTML representation of this link.
	 * @throws Exception
	 */
    public static String generateLink(String prefix, String url, String text, boolean onlyExternalLinks) throws Exception {
        logger.debug("generating link for prefix=" + prefix + ",url=" + url);
        String displayName = null;
        int displayNameDelimLocation = url.indexOf('|');
        if (displayNameDelimLocation >= 0) {
            displayName = url.substring(displayNameDelimLocation + 1);
            url = url.substring(0, displayNameDelimLocation);
        }
        String expansion = linkingProperties.getProperty(prefix);
        if (expansion == null) {
            logger.info("no expansion found for link extension: " + prefix);
            return "<span class=\"extendlinkproblem\">" + text + "</span>";
        }
        while (true) {
            int urlLocation = expansion.indexOf(URL_KEYWORD);
            if (urlLocation >= 0) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(expansion.substring(0, urlLocation));
                buffer.append(url);
                buffer.append(expansion.substring(urlLocation + URL_KEYWORD.length(), expansion.length()));
                expansion = buffer.toString();
            } else {
                break;
            }
        }
        String target = linkingProperties.getProperty(prefix + ".target");
        StringBuffer buffer = new StringBuffer();
        if (expansion.startsWith("http") || expansion.startsWith("ftp") || expansion.startsWith("mailto") || expansion.startsWith("news") || expansion.startsWith("telnet") || expansion.startsWith("file")) {
            buffer.append("<a class=\"externallink\" href=\"");
        } else {
            if (onlyExternalLinks) {
                if (displayName == null) {
                    String showPrefix = linkingProperties.getProperty(prefix + ".showprefix");
                    if (showPrefix != null && showPrefix.equals("false")) {
                        return url;
                    } else {
                        return text;
                    }
                } else {
                    return displayName;
                }
            }
            buffer.append("<a href=\"");
        }
        buffer.append(expansion);
        buffer.append("\" ");
        if (target != null) {
            buffer.append("target=\"");
            buffer.append(target);
            buffer.append("\"");
        }
        buffer.append(">");
        if (displayName == null) {
            String showPrefix = linkingProperties.getProperty(prefix + ".showprefix");
            if (showPrefix != null && showPrefix.equals("false")) {
                buffer.append(url);
            } else {
                buffer.append(text);
            }
        } else {
            buffer.append(displayName);
        }
        buffer.append("</a>");
        return buffer.toString();
    }

    /**
	 * Produce a link that doesn't use the prefix: scheme but can have a linking.properties entry
	 * hyperlinks.target=...
	 *
	 * @param destination
	 * @return
	 */
    public static String generateNonExpandedLink(String destination) throws IOException {
        StringBuffer buffer = new StringBuffer();
        if (destination.startsWith("http") || destination.startsWith("ftp") || destination.startsWith("mailto") || destination.startsWith("news") || destination.startsWith("telnet") || destination.startsWith("file")) {
            buffer.append("<a class=\"externallink\" href=\"");
        } else {
            buffer.append("<a href=\"");
        }
        buffer.append(destination);
        buffer.append("\" ");
        String target = linkingProperties.getProperty("hyperlinks.target");
        if (target != null) {
            buffer.append("target=\"");
            buffer.append(target);
            buffer.append("\"");
        }
        buffer.append(">");
        buffer.append(destination);
        buffer.append("</a>");
        return buffer.toString();
    }
}
