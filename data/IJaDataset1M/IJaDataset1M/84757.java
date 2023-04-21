package fido.servlets.object;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import fido.db.AttributeTable;
import fido.db.Attribute;
import fido.db.FidoDatabaseException;

public class AttributeParameters extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String context = request.getContextPath();
        String id = request.getParameter("object");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Object Attribute Parameters</TITLE>");
        out.println("<LINK REL=stylesheet HREF=" + context + "/Border.css>");
        out.println("</HEAD>");
        out.println("<BODY>");
        try {
            AttributeTable table = new AttributeTable();
            Collection list = table.list();
            if (list.size() == 0) {
                out.println("<SCRIPT>");
                out.println("function remote(url)");
                out.println("{");
                out.println("window.opener.location=url");
                out.println("window.close()");
                out.println("}");
                out.println("</SCRIPT>");
                out.println("<BODY>");
                out.println("There are no Attributes available. These are required");
                out.println("to add a new Object Attribute.<P>");
                out.println("Press the <I>View</I> button to display the Attribute");
                out.println("table in the main window.<P>");
                out.println("<FORM>");
                out.println("<CENTER>");
                out.println("<INPUT TYPE=button VALUE=View onClick=\"remote('../attributeTable')\">");
                out.println("<INPUT TYPE=button VALUE=Close onClick=\"window.close()\">");
                out.println("<INPUT TYPE=button VALUE=Help onClick=\"window.open('" + context + "/help?topic=objectattrparam')\">");
                out.println("</CENTER>");
                out.println("</FORM>");
            } else {
                out.println("<FORM METHOD=post ACTION=" + context + "/developer/objectAttrSave>");
                out.println("<TABLE>");
                out.println("<INPUT TYPE=hidden NAME=id VALUE=\"" + id + "\">");
                out.println("<TR><TD>Attribute: </TD><TD>");
                out.println("<SELECT NAME=attribute>");
                for (Iterator it = list.iterator(); it.hasNext(); ) {
                    Attribute item = (Attribute) it.next();
                    out.println("<OPTION VALUE=" + item.getAttribute() + ">" + item.getAttribute() + "</OPTION>");
                }
                out.println("</SELECT>");
                out.println("</TD></TR>");
                out.println("</TABLE><P>");
                out.println("<CENTER>");
                out.println("<INPUT TYPE=submit VALUE=Ok>");
                out.println("<INPUT TYPE=button VALUE=Cancel onClick=\"window.close()\">");
                out.println("<INPUT TYPE=button VALUE=Help onClick=\"window.open('" + context + "/help?topic=objectattrparam')\">");
                out.println("</CENTER>");
                out.println("</FORM>");
            }
        } catch (FidoDatabaseException e) {
            out.println("Database error:<BR>");
            out.println("<PRE>");
            e.printStackTrace(out);
            out.println("</PRE>");
        }
        out.println("</BODY>");
        out.println("</HTML>");
    }
}
