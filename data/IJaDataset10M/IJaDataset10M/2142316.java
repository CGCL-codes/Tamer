package fido.servlets.pronoun;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import fido.db.Pronoun;
import fido.db.PronounTable;
import fido.db.FidoDatabaseException;
import fido.servlets.FidoServlet;

public class Table extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String context = request.getContextPath();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Pronoun Table</TITLE>");
        out.println("<LINK REL=stylesheet HREF=" + context + "/Borderless.css>");
        try {
            PronounTable table = new PronounTable();
            Collection list = table.list();
            out.println("<SCRIPT>");
            out.println("function remove(name)");
            out.println("{");
            out.println("	var bol = confirm(\"Are you sure you want to delete \" + name + \"?\")");
            out.println("	if (bol)");
            out.println("	{");
            out.println("		window.location=\"" + context + "/developer/pronounDelete?name=\" + name");
            out.println("	}");
            out.println("}");
            FidoServlet.addStatusJavascript(out);
            out.println("</SCRIPT>");
            out.println("</HEAD>");
            out.println("<BODY>");
            String[][] path = { { "Home", context + "/index" }, { "Pronoun Table", null } };
            FidoServlet.header(out, path, "pronoun", context);
            FidoServlet.addAddLink(out, context, "/developer/pronounParameters", "pronounParameters", "Add a new Pronoun", "width=300,height=200");
            out.println("<HR>");
            out.println("<TABLE BORDER=4>");
            out.println("<TH>Actions</TH><TH>Pronoun</TH><TH>Person</TH><TH>Gender</TH><TH>Plural</TH>");
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                Pronoun item = (Pronoun) it.next();
                out.println("<TR><TD>");
                String params = "name=" + FidoServlet.htmlize(item.getPronoun()) + "&person=" + item.getPerson() + "&gender=" + item.getGender() + "&plural=" + item.isPlural();
                FidoServlet.addEditLinkWindow(out, context, "/developer/pronounParameters", params, "pronounParameters", "Edit pronoun", "width=300,height=200");
                params = "'" + FidoServlet.htmlize(item.getPronoun()) + "'";
                FidoServlet.addRemoveLink(out, context, "remove", params, "Delete pronoun");
                out.println("</TD>");
                out.println("<TD ALIGN=CENTER>" + item.getPronoun() + "</TD>");
                out.println("<TD ALIGN=CENTER>" + item.getPersonDescription() + "</TD>");
                out.println("<TD ALIGN=CENTER>" + item.getGenderDescription() + "</TD>");
                out.println("<TD ALIGN=CENTER>" + item.isPlural() + "</TD>");
                out.println("</TR>");
            }
            out.println("</TABLE>");
        } catch (FidoDatabaseException e) {
            out.println("</HEAD>");
            out.println("<BODY>");
            String[][] path = { { "Home", context + "/index" }, { "Pronoun Table", null } };
            FidoServlet.header(out, path, "pronoun", context);
            out.println("Database error:<BR>");
            out.println("<PRE>");
            e.printStackTrace(out);
            out.println("</PRE>");
        }
        FidoServlet.footer(out);
        out.println("</BODY>");
        out.println("</HTML>");
    }
}
