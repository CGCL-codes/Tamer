package org.simpleframework.news;

import java.io.PrintStream;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Article implements Component {

    @Attribute
    private String title;

    @Element
    private String link;

    @Element
    private String date;

    @Element(data = true)
    private String content;

    public Article() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void write(PrintStream out) {
        out.println("<br>");
        out.println("<br>");
        out.println("<table width='100%' cellspacing='0' cellpadding='5'>");
        out.println("<tr valign='top' align='left'>");
        out.println("<td>");
        out.println("<a href='" + link + "'>");
        out.println("<img src='/images/home_bullet.gif' border='0' alt='" + date + "'>");
        out.println("</a>");
        out.println("</td>");
        out.println("<td>");
        out.println("<p>");
        out.println("<a href='" + link + "'>");
        out.println("<b>" + title + "</b>");
        out.println("</a><br>");
        out.println(content.trim());
        out.println("</p>");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
    }
}
