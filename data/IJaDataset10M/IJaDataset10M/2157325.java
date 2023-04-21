package com.okrasz.elvis.elvisnet.taglib;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.sql.*;
import com.okrasz.elvis.db.sql.DbElpEndpoints;

/**
 *  Generated tag class.
 */
public class ListLibrariesTag extends TagSupport {

    public ListLibrariesTag() {
        super();
    }

    /**
     *  
     * Fill in this method to perform other operations from doStartTag().
     * 
     */
    public void otherDoStartTagOperations() {
        JspWriter out = pageContext.getOut();
        System.out.println("ListLibrariesTag: start");
        try {
            ResultSet rs = DbElpEndpoints.getInstance().getSearchEndpoints();
            out.println("<table cellpadding='0' cellspacing='0' class='Net'>");
            out.println("  <colgroup>");
            for (int i = 0; i < 10; i++) out.println("    <col>");
            out.println("  </colgroup>");
            out.println("  <tbody>");
            out.println("    <tr><th class='heading' colspan='10'>Biblioteki w sieci</th></tr>");
            out.println("  <tr class='header'>");
            out.print("    <td></td> <td>identyfikator</td> <td>Nazwa</td> <td>WWW</td> <td>ELP URL</td> <td>wersja</td> ");
            out.println(" <td>stan</td> <td>data aktualizacji</td> <td>niedostępny od</td> <td>komentarz</td>");
            out.println("  </tr>");
            try {
                while (rs != null && rs.next()) {
                    out.print(" <tr><td style='text-align: center;'>");
                    out.print("<a href=\"?action=remove&amp;connector=");
                    out.print(rs.getString("url"));
                    out.print("\" alt=\"usuń\"><img src=\"../image/remove.png\" border='0' style='margin-right: 4px;'></a>");
                    out.print("<a href=\"?action=recheck&amp;connector=");
                    out.print(rs.getString("url"));
                    out.print("\" alt=\"sprawdź ponownie\"><img src=\"../image/refresh.png\" border='0'></a>");
                    out.print("</td> <td>");
                    out.print(rs.getString("libraryID"));
                    out.print("</td> <td>");
                    out.print(rs.getString("name"));
                    out.print("</td> <td><a href=\"");
                    out.print(rs.getString("www"));
                    out.print("\">");
                    out.print(rs.getString("www"));
                    out.print("</a></td> <td>");
                    out.print(rs.getString("url"));
                    out.print("</td> <td  style='text-align: center;'>");
                    out.print(rs.getString("version"));
                    out.print("</td> <td style='text-align: center;'>");
                    switch(rs.getInt("state")) {
                        case com.okrasz.elvis.db.sql.DbElpEndpoints.STATE_OK:
                            out.print("OK");
                            break;
                        case com.okrasz.elvis.db.sql.DbElpEndpoints.STATE_UNAVAILABLE:
                            out.print("niedostępny");
                            break;
                        default:
                            out.print("stan nieznany");
                    }
                    out.print("</td> <td style='text-align: center;'>");
                    out.print(rs.getDate("lastUpdate"));
                    out.print(" ");
                    out.print(rs.getTime("lastUpdate"));
                    out.print("</td> <td style='text-align: center;'>");
                    if (rs.getDate("unavailableSince") != null) {
                        out.print(rs.getDate("unavailableSince"));
                        out.print(" ");
                        out.print(rs.getTime("unavailableSince"));
                    }
                    out.print("</td> <td>");
                    out.print(rs.getString("comment"));
                    out.println("</td></tr>");
                }
            } finally {
                out.println("<tr class='button'><td colspan='10'><form action='elvisnet.jsp' method='post'><input type='hidden' name='action' value='recheckAll'><input class='button' type='submit' value='sprawdź biblioteki w sieci'></form></td></tr>");
                out.println("</table>");
            }
        } catch (java.sql.SQLException e) {
            try {
                out.println("<div class='error'>Błąd w dostępie do bazy danych");
            } catch (java.io.IOException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  
     * Fill in this method to determine if the tag body should be evaluated
     * Called from doStartTag().
     * 
     */
    public boolean theBodyShouldBeEvaluated() {
        return true;
    }

    /**
     *  
     * Fill in this method to perform other operations from doEndTag().
     * 
     */
    public void otherDoEndTagOperations() {
    }

    /**
     *  
     * Fill in this method to determine if the rest of the JSP page
     * should be generated after this tag is finished.
     * Called from doEndTag().
     * 
     */
    public boolean shouldEvaluateRestOfPageAfterEndTag() {
        return true;
    }

    /** .
     *
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_INCLUDE if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     *
     */
    public int doStartTag() throws JspException, JspException {
        otherDoStartTagOperations();
        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    /** .
     *
     *
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     *
     */
    public int doEndTag() throws JspException, JspException {
        otherDoEndTagOperations();
        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
    }
}
