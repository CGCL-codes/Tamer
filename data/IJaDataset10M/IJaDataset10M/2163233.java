package org.wportal.jspwiki.plugin;

import com.ecyrd.jspwiki.TextUtil;
import com.ecyrd.jspwiki.TranslatorReader;
import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.WikiEngine;
import com.ecyrd.jspwiki.WikiPage;
import com.ecyrd.jspwiki.attachment.Attachment;
import com.ecyrd.jspwiki.plugin.PluginException;
import com.ecyrd.jspwiki.plugin.WikiPlugin;
import com.ecyrd.jspwiki.plugin.PageNavPart;
import org.apache.log4j.Logger;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: SimonLei
 * Date: 2004-7-15
 * Time: 11:45:37
 * $Id: RecentChangesPlugin.java,v 1.2 2004/11/14 10:00:17 simon_lei Exp $

    JSPWiki - a JSP-based WikiWiki clone.

    Copyright (C) 2001 Janne Jalkanen (Janne.Jalkanen@iki.fi)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 2.1 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 *  Returns the Recent Changes.
 *
 *  Parameters: since=number of days,
 *              format=(compact|full)
 *
 *  @author Janne Jalkanen
 */
public class RecentChangesPlugin implements WikiPlugin {

    /** How many days we show by default. */
    private static final int DEFAULT_DAYS = 100 * 365;

    private static Logger log = Logger.getLogger(RecentChangesPlugin.class);

    private boolean isSameDay(Date a, Date b) {
        Calendar aa = Calendar.getInstance();
        aa.setTime(a);
        Calendar bb = Calendar.getInstance();
        bb.setTime(b);
        return (aa.get(Calendar.YEAR) == bb.get(Calendar.YEAR) && aa.get(Calendar.DAY_OF_YEAR) == bb.get(Calendar.DAY_OF_YEAR));
    }

    private int start = 0;

    private int perPage = 200;

    public String execute(WikiContext context, Map params) throws PluginException {
        int since = TextUtil.parseIntParameter((String) params.get("since"), DEFAULT_DAYS);
        start = TextUtil.parseIntParameter((String) context.getHttpParameter(PageNavPart.PARAM_START), 0);
        perPage = TextUtil.parseIntParameter((String) params.get(PageNavPart.PARAM_PER_PAGE), 200);
        int spacing = 4;
        boolean showAuthor = true;
        WikiEngine engine = context.getEngine();
        String format = (String) params.get("format");
        if ("compact".equals(format)) {
            spacing = 0;
            showAuthor = false;
        }
        Calendar sincedate = new GregorianCalendar();
        sincedate.add(Calendar.DAY_OF_MONTH, -since);
        log.debug("Calculating recent changes from " + sincedate.getTime());
        Collection changes = null;
        try {
            changes = engine.getPageManager().getAllPages(start, perPage, "lastModified desc");
        } catch (Exception e) {
            throw new PluginException("Can't get recentChanged page", e);
        }
        StringWriter out = new StringWriter();
        PageNavPart pageNav = new PageNavPart(context.getPage().getName());
        TranslatorReader linkProcessor = new TranslatorReader(context, new java.io.StringReader(""));
        if (changes != null) {
            Date olddate = new Date(0);
            SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat tfmt = new SimpleDateFormat("HH:mm:ss");
            out.write(pageNav.getNavParts(start, perPage) + "<br/>");
            out.write("<table border=\"0\" cellpadding=\"" + spacing + "\">\n");
            for (Iterator i = changes.iterator(); i.hasNext(); ) {
                WikiPage pageref = (WikiPage) i.next();
                Date lastmod = pageref.getLastModified();
                if (lastmod.before(sincedate.getTime())) {
                    break;
                }
                if (!isSameDay(lastmod, olddate)) {
                    out.write("<tr>\n");
                    out.write("  <td colspan=\"2\"><b>" + fmt.format(lastmod) + "</b></td>\n");
                    out.write("</tr>\n");
                    olddate = lastmod;
                }
                String pageurl = engine.encodeName(pageref.getName());
                String link = linkProcessor.makeLink(pageref);
                out.write("<tr>\n");
                out.write("<td width=\"30%\">" + link + "</td>\n");
                String text = engine.getText(pageref.getName());
                int firstEnter = text.indexOf("\n");
                String title;
                if (firstEnter == -1) firstEnter = Math.min(100, text.length());
                if (firstEnter > 100) {
                    title = engine.textToHTML(context, text.substring(0, 100) + "...");
                } else title = engine.textToHTML(context, text.substring(0, firstEnter));
                out.write("<td width=\"30%\">" + title + "</td>\n");
                if (pageref instanceof Attachment) {
                    out.write("<td>" + tfmt.format(lastmod) + "</td>");
                } else {
                    out.write("<td><a href=\"" + engine.getBaseURL() + "Diff.jsp?page=" + pageurl + "&amp;r1=-1\">" + tfmt.format(lastmod) + "</a></td>\n");
                }
                if (showAuthor) {
                    String author = pageref.getAuthor();
                    if (author != null) {
                        if (engine.pageExists(author)) {
                            author = linkProcessor.makeLink(TranslatorReader.READ, author, author);
                        }
                    } else {
                        author = "unknown";
                    }
                    out.write("<td>" + author + "</td>");
                }
                out.write("</tr>\n");
            }
            out.write("</table>\n");
        }
        out.write("<br/>" + pageNav.getNavParts(start, perPage));
        return out.toString();
    }
}
