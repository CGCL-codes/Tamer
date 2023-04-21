package fido.servlets.propernoun;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import fido.db.ProperNounTable;
import fido.db.ProperNounNotFoundException;
import fido.db.FidoDatabaseException;
import fido.servlets.FidoServlet;

public class Parameters extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String context = request.getContextPath();
        String name = request.getParameter("name");
        String sense = request.getParameter("sense");
        String object = request.getParameter("object");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Proper Noun Parameters</TITLE>");
        out.println("<LINK REL=stylesheet HREF=" + context + "/Border.css>");
        try {
            out.println("<SCRIPT>");
            FidoServlet.addValidateIdentifier(out);
            FidoServlet.addValidateNumber(out);
            out.println("function validateForm()");
            out.println("{");
            out.println("	if (validateIdentifier('Proper Noun', document.form.name.value) == false)");
            out.println("	{");
            out.println("		document.form.name.focus()");
            out.println("		return false;");
            out.println("	}");
            out.println("	if (validateNumber('Object Id', document.form.object.value) == false)");
            out.println("	{");
            out.println("		document.form.object.focus()");
            out.println("		return false;");
            out.println("	}");
            out.println("	return true");
            out.println("}");
            out.println("function putFocus()");
            out.println("{");
            out.println("	document.form.name.focus();");
            out.println("}");
            out.println("</SCRIPT>");
            out.println("</HEAD>");
            out.println("<BODY onLoad=\"putFocus()\">");
            out.println("<FORM NAME=form METHOD=post ACTION=" + context + "/developer/properNounSave onSubmit=\"return validateForm()\">");
            if (name != null) {
                ProperNounTable table = new ProperNounTable();
                out.println("<INPUT TYPE=hidden NAME=hashcode VALUE=" + table.hashCode(name, sense) + ">");
            }
            out.println("<INPUT TYPE=hidden NAME=sense VALUE=" + sense + ">");
            out.println("<TABLE>");
            out.print("<TR><TD>Proper Noun: </TD><TD>");
            if (name == null) out.println("<INPUT TYPE=text NAME=name MAXLENGTH=20 SIZE=20></TD></TR>"); else {
                out.println("<INPUT TYPE=hidden NAME=name VALUE=\"" + name + "\">");
                out.println(name + "</TD></TR>");
            }
            out.print("<TR><TD>Object Id: </TD><TD><INPUT TYPE=text NAME=object MAXLENGTH=20 ");
            if (object != null) out.print("VALUE=\"" + object + "\" ");
            out.println("SIZE=20></TD></TR>");
            out.println("</TABLE><P>");
            out.println("<CENTER>");
            out.println("<INPUT TYPE=submit VALUE=Ok>");
            out.println("<INPUT TYPE=button VALUE=Cancel onClick=\"window.close()\">");
            out.println("<INPUT TYPE=button VALUE=Help onClick=\"window.open('" + context + "/help?topic=propernounparam')\">");
            out.println("</CENTER>");
            out.println("</FORM>");
        } catch (ProperNounNotFoundException e) {
            out.println("</HEAD>");
            out.println("<BODY>");
            out.println("The proper noun passed into the edit dialog does not exist");
            out.println("in the database: " + e.getMessage());
        } catch (FidoDatabaseException e) {
            out.println("</HEAD>");
            out.println("<BODY>");
            out.println("Database error:<BR>");
            out.println("<PRE>");
            e.printStackTrace(out);
            out.println("</PRE>");
        }
        out.println("</BODY>");
        out.println("</HTML>");
    }
}
