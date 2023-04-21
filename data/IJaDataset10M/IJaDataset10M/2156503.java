package net.sf.wfnm.web.taglib;

import net.sf.wfnm.AttributeContainer;
import net.sf.wfnm.Config;
import net.sf.wfnm.NavigationManager;
import net.sf.wfnm.NavigationManagerFactory;
import net.sf.wfnm.Page;
import net.sf.wfnm.Webflow;
import net.sf.wfnm.web.HttpSessionWrapper;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * A tag that display the status of the framework.
 *
 * @author <a href="mailto:malbari@users.sourceforge.net">Maurizio Albari</a>
 * @version 1.0.6
 */
public class StatusTag extends TagSupport {

    /** 
     * The default webflows background colors
     */
    private static String[] DEFAULT_COLORS = new String[] { "#CFFAFA", "#FAFA9A", "#FACF9A", "#DAFADA" };

    /** 
     * The map that bind a webflow name with a background color
     */
    private Map webflow2color = new HashMap();

    /** 
     * The page color
     */
    private String pageColor;

    /** 
     * The height of the status table
     */
    private int height;

    /** 
     * The width of the status table
     */
    private int width;

    /**
     * Set the height of the status table
     *
     * @param height The height to set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the page color
     *
     * @param pageColor The pageColor to set.
     */
    public void setPageColor(String pageColor) {
        this.pageColor = pageColor;
    }

    /**
     * Sets the colors of webflows background
     *
     * @param colors The colors to set in the form. For example 'A=#CFFAFA; B=#FAFA9A; C=#FACF9A; D=#DAFADA'
     */
    public void setWebflowColors(String colors) {
        StringTokenizer strTok = new StringTokenizer(colors, " ,;");
        while (strTok.hasMoreTokens()) {
            String token = strTok.nextToken();
            int pos = token.indexOf('=');
            if (pos > 0) {
                String webflow = token.substring(0, pos);
                String color = token.substring(pos + 1);
                webflow2color.put(webflow, color);
            }
        }
    }

    /**
     * Set the width of the status table
     *
     * @param width The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Implements the start of the tag
     *
     * @return always SKIP_BODY in order to skip the body
     *
     * @throws JspException if something fails
     */
    public int doStartTag() throws JspException {
        StringBuffer buffer1 = new StringBuffer();
        buffer1.append("<table width='");
        buffer1.append(String.valueOf(width));
        buffer1.append("' bgcolor='#C0C0C0' ");
        buffer1.append("cellSpacing='0' cellPadding='0' border='1' bordercolor='#000000'>\n");
        if (!Config.getInstance().isEnabled()) {
            buffer1.append("<tr><td bordercolor='#C0C0C0' align='center'>\n");
            buffer1.append("Framework disabled\n");
        } else {
            AttributeContainer ac = (AttributeContainer) HttpSessionWrapper.wrapItIfNecessary(pageContext.getSession());
            NavigationManager manager = NavigationManagerFactory.getInstance(ac);
            Set sessionSet = new HashSet();
            for (Enumeration e = pageContext.getSession().getAttributeNames(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                sessionSet.add(key);
            }
            buffer1.append("<tr valign='bottom'><td bordercolor='#C0C0C0' align='center'>\n");
            buffer1.append("<table width='100%' cellSpacing='1' cellPadding='1' bgcolor='#C0C0C0'>\n");
            StringBuffer buffer2 = new StringBuffer();
            int count = 1;
            Stack webflowStack = manager.getWebflowStack();
            for (int i = webflowStack.size() - 1; i >= 0; i--) {
                count++;
                Webflow webflow = (Webflow) webflowStack.elementAt(i);
                String wfBgColor = getColor(webflow.getName(), i);
                buffer2.append("<tr valign='bottom'><td>\n");
                buffer2.append("<table bgcolor='");
                buffer2.append(wfBgColor);
                buffer2.append("' width='100%' cellSpacing='0' cellPadding='0' border='1' bordercolor='#C0C0C0'>\n");
                buffer2.append("<tr><td align='center' bordercolor='#000000'>\n");
                buffer2.append("<table width='100%' cellSpacing='2' cellPadding='2' border='1' bordercolor='");
                buffer2.append(wfBgColor);
                buffer2.append("' >\n");
                Stack pageStack = webflow.getPageStack();
                for (int j = pageStack.size() - 1; j >= 0; j--) {
                    count++;
                    Page page = (Page) pageStack.elementAt(j);
                    buffer2.append("<tr><td align='center' bgcolor='");
                    buffer2.append(pageColor);
                    buffer2.append("' bordercolor='#000000' >\n");
                    buffer2.append("Page <b>");
                    buffer2.append(page.getUrl());
                    buffer2.append("</b> owns: ");
                    buffer2.append(page.getOwnedObjectSet().toString());
                    buffer2.append("\n");
                    buffer2.append("<tr><td>\n");
                    sessionSet.removeAll(page.getOwnedObjectSet());
                }
                buffer2.append("</table>\n");
                buffer2.append("Webflow <b>");
                buffer2.append(webflow.getName());
                buffer2.append("</b> owns: ");
                buffer2.append(webflow.getOwnedObjectSet().toString());
                buffer2.append("\n");
                buffer2.append("</tr></td>\n");
                buffer2.append("</table>\n");
                buffer2.append("</tr></td>\n");
                sessionSet.removeAll(webflow.getOwnedObjectSet());
            }
            int emptyHeight = this.height - (count * 33);
            if (emptyHeight > 0) {
                buffer1.append("<tr valign='bottom'><td>\n");
                buffer1.append("<table width='100%' height='");
                buffer1.append(emptyHeight);
                buffer1.append("'>\n");
                buffer1.append("<tr><td>&nbsp;</tr></td>\n");
                buffer1.append("</table>\n");
                buffer1.append("</tr></td>\n");
            }
            buffer1.append(buffer2);
            buffer1.append("</table>\n");
            buffer1.append("</tr></td>\n");
            buffer1.append("<tr valign='bottom'><td align='center' bordercolor='#C0C0C0'>\n");
            buffer1.append("<b>Session</b> owns: ");
            buffer1.append(sessionSet.toString());
            buffer1.append("\n");
        }
        buffer1.append("</tr></td>\n");
        buffer1.append("</table>\n");
        try {
            pageContext.getOut().print(buffer1.toString());
        } catch (IOException ioe) {
            throw new JspException("Unable to print out the status", ioe);
        }
        return SKIP_BODY;
    }

    /**
     * Return the background color of a webflow
     *
     * @param webflowName the webflow name
     * @param index the inxex of the webflow
     *
     * @return the background color of the webflow
     */
    private String getColor(String webflowName, int index) {
        String color = (String) webflow2color.get(webflowName);
        if (color == null) {
            color = DEFAULT_COLORS[index % DEFAULT_COLORS.length];
        }
        return color;
    }
}
