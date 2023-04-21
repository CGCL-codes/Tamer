package org.authorsite.mailarchive.view.web.taglibs;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * Taglib to assemble a breadcrumb trail in the default MailArchive web application.
 *  
 * @author jejking
 * @version $Revision: 1.3 $
 */
public class BreadcrumbTag extends NavigationTag {

    protected void doTagImplementation() throws JspException, IOException {
        if (uriComponents.length == 0 || uriComponents[0].equals("index.jspx")) {
            out.print("<span class=\"breadcrumb\">");
            out.print(navBundle.getString("archive"));
            out.print("</span>");
        } else {
            out.print("<a class=\"breadcrumb\" href=\"" + contextPath + "/index.jspx\" >");
            out.print(navBundle.getString("archive"));
            out.print("</a>");
            int cutOff = 0;
            if (uriComponents[uriComponents.length - 1].startsWith("index") || uriComponents[uriComponents.length - 1].endsWith("/")) {
                cutOff = 2;
            } else {
                cutOff = 1;
            }
            for (int i = 1; i < uriComponents.length - cutOff; i++) {
                out.print(" &gt; ");
                out.print("<a class=\"breadcrumb\" href=\"" + contextPath + "/");
                for (int j = 1; j <= i; j++) {
                    out.print(uriComponents[j] + "/");
                }
                out.println("index.jspx\">");
                out.println(navBundle.getString(uriComponents[i]));
                out.println("</a>");
            }
        }
        if (req.getAttribute("lastBreadcrumb") != null) {
            out.println(" &gt; ");
            out.println("<span class=\"breadcrumb\">");
            out.println(navBundle.getString((String) req.getAttribute("lastBreadcrumb")));
            out.println("</span>");
        }
    }
}
