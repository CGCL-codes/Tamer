package fido.servlets.questionword;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import fido.db.QuestionWordTable;
import fido.db.QuestionWord;
import fido.db.FidoDatabaseException;
import fido.servlets.FidoServlet;

public class Table extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String context = request.getContextPath();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Question Word Table</TITLE>");
        out.println("<LINK REL=stylesheet HREF=" + context + "/Borderless.css>");
        try {
            QuestionWordTable table = new QuestionWordTable();
            Collection list = table.list();
            out.println("<SCRIPT>");
            out.println("function remove(word)");
            out.println("{");
            out.println("	var bol = confirm(\"Are you sure you want to delete \" + word + \"?\")");
            out.println("	if (bol)");
            out.println("	{");
            out.println("		window.location=\"" + context + "/developer/questionWordDelete?word=\" + word");
            out.println("	}");
            out.println("}");
            FidoServlet.addStatusJavascript(out);
            out.println("</SCRIPT>");
            out.println("</HEAD>");
            out.println("<BODY>");
            String[][] path = { { "Home", context + "/index" }, { "Question Word Table", null } };
            FidoServlet.header(out, path, "questionword", context);
            FidoServlet.addAddLink(out, context, "/developer/questionWordParameters", "questionWordParameters", "Add a new Question Word", "width=300,height=200");
            out.println("<HR>");
            out.println("<TABLE BORDER=4>");
            out.println("<TH>Actions</TH><TH>Question Word</TH><TH>Type</TH>");
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                QuestionWord item = (QuestionWord) it.next();
                out.println("<TR><TD>");
                String params = "word=" + FidoServlet.htmlize(item.getWord()) + "&type=" + item.getType();
                FidoServlet.addEditLinkWindow(out, context, "/developer/questionWordParameters", params, "questionWordParameters", "Edit question word", "width=300,height=200");
                params = "'" + FidoServlet.htmlize(item.getWord()) + "'";
                FidoServlet.addRemoveLink(out, context, "remove", params, "Delete question word");
                out.println("</TD>");
                out.println("<TD ALIGN=CENTER>" + item.getWord() + "</TD>");
                out.println("<TD ALIGN=CENTER>" + item.getTypeDescription() + "</TD>");
                out.println("</TR>");
            }
            out.println("</TABLE>");
        } catch (FidoDatabaseException e) {
            out.println("</HEAD>");
            out.println("<BODY>");
            String[][] path = { { "Home", context + "/index" }, { "Question Word Table", null } };
            FidoServlet.header(out, path, "questionword", context);
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
