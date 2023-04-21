package com.izforge.izpack.util;

import com.izforge.izpack.installer.AutomatedInstallData;
import com.izforge.izpack.installer.IzPanel;
import java.util.Iterator;

/**
 * A helper class which creates a summary from all panels. This class calls all declared panels for
 * a summary To differ between caption and message, HTML is used to draw caption in bold and indent
 * messaged a little bit.
 *
 * @author Klaus Bartz
 */
public class SummaryProcessor {

    private static String HTML_HEADER;

    private static String HTML_FOOTER = "</body>\n</html>\n";

    private static String BODY_START = "<div class=\"body\">";

    private static String BODY_END = "</div>";

    private static String HEAD_START = "<h1>";

    private static String HEAD_END = "</h1>\n";

    static {
        StringBuffer sb = new StringBuffer(256);
        sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n").append("<html>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" + "<head>\n<STYLE TYPE=\"text/css\" media=screen,print>\n").append("h1{\n  font-size: 100%;\n  margin: 1em 0 0 0;\n  padding: 0;\n}\n").append("div.body {\n  font-size: 100%;\n  margin: 0mm 2mm 0  8mm;\n  padding: 0;\n}\n").append("</STYLE>\n</head>\n<body>\n");
        HTML_HEADER = sb.toString();
    }

    /**
     * Returns a HTML formated string which contains the summary of all panels. To get the summary,
     * the methods * {@link IzPanel#getSummaryCaption} and {@link IzPanel#getSummaryBody()} of all
     * panels are called.
     *
     * @param idata AutomatedInstallData which contains the panel references
     * @return a HTML formated string with the summary of all panels
     */
    public static String getSummary(AutomatedInstallData idata) {
        Iterator<IzPanel> iter = idata.panels.iterator();
        StringBuffer sb = new StringBuffer(2048);
        sb.append(HTML_HEADER);
        while (iter.hasNext()) {
            IzPanel panel = iter.next();
            String caption = panel.getSummaryCaption();
            String msg = panel.getSummaryBody();
            if (caption == null || msg == null) {
                continue;
            }
            sb.append(HEAD_START).append(caption).append(HEAD_END);
            sb.append(BODY_START).append(msg).append(BODY_END);
        }
        sb.append(HTML_FOOTER);
        return (sb.toString());
    }
}
