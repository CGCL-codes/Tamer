package com.nitbcn.lib.view;

/**
 *
 * @author Raimon Bosch
 */
public class Footer {

    StringBuffer sb;

    /** Creates a new instance of Footer */
    public Footer() {
        sb = new StringBuffer();
    }

    public String get() {
        sb.append("<div id='footer'>\n");
        sb.append("\t<p><a href='http://www.nitbcn.com/topsearches.html' title='nitbcn.com Most Common Tags'>Most Common Tags</a></p>\n");
        sb.append("\t<p class='legal'>&copy;2009 All Rights Reserved.</p>\n");
        sb.append("</div>\n");
        return sb.toString();
    }
}
