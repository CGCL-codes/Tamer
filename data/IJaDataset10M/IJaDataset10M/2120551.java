package gui.opener;

import gui.session.Reference;
import gui.session.Stato;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MultiOpen extends HttpServlet {

    static final long serialVersionUID = 8;

    Stato stato = null;

    /**
	 * Costruttore
	 */
    public MultiOpen() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("stato") != null) {
            stato = (Stato) request.getSession().getAttribute("stato");
        } else {
            stato = new Stato(getServletContext().getRealPath("/"), getServletContext().getRealPath("/") + "cache" + File.separator + request.getSession().getId(), request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/cache/" + request.getSession().getId());
        }
        String openFile = request.getParameter("file");
        String type = request.getParameter("type");
        if (type == null) type = "msword";
        PrintWriter out = response.getWriter();
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"it\" lang=\"it\">");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=iso-8859-1\">");
        out.println("<title>" + openFile + "</title>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
        out.println("<script type=\"text/javascript\">");
        out.println("function showDoc(type){");
        out.println("  iframe = document.getElementById(\"show\");");
        out.println("  switch(type){");
        out.println("    	case 0:  iframe.src=\"" + stato.ref.getCacheURL(openFile) + "\"; break;");
        out.println("    	case 10: iframe.src=\"" + stato.tafDebug.get(openFile) + "\"; break;");
        out.println("    	case 1:  iframe.src=\"OpenFile?file=" + openFile + "&type=preview\"; break;");
        out.println("    	case 2:  iframe.src=\"OpenFile?file=" + openFile + "&type=msword \"; break;");
        out.println("    	case 3:  iframe.src=\"OpenFile?file=" + openFile + "&type=pdf \"; break;");
        out.println("  }");
        out.println("}");
        out.println("</script>");
        out.println("</head>");
        out.println("<body>");
        out.println("<form>");
        out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\">");
        out.println(" <tr>");
        out.println("   <td valign=\"bottom\" align=\"left\">");
        if ((stato.debugMode) && (stato.tafDebug.containsKey(openFile))) out.println("	  <input type=\"button\" value=\"NMARKUP\" onclick=\"showDoc(10)\">");
        if (stato.debugMode) out.println("	  <input type=\"button\" value=\"XML\" onclick=\"showDoc(0)\">");
        out.println("	  <input type=\"button\" value=\"XHTML\" onclick=\"showDoc(1)\">");
        out.println("	  <input type=\"button\" value=\"DOC\" onclick=\"showDoc(2)\">");
        out.println("	  <input type=\"button\" value=\"PDF\" onclick=\"showDoc(3)\">");
        out.println("   </td>");
        out.println("   <td align=\"right\" valign=\"bottom\">");
        out.println(openFile);
        out.println("   </td>");
        out.println(" </tr>");
        out.println("</table>");
        out.println("</form>");
        if (type.equals("xml") && (stato.debugMode)) out.println("<iframe id=\"show\" src=\"" + stato.ref.getCacheURL(openFile) + "\" width=\"100%\" height=\"92%\"/>"); else if (type.equals("nmarkup") && (stato.debugMode) && (stato.tafDebug.containsKey(openFile))) out.println("<iframe id=\"show\" src=\"" + stato.tafDebug.get(openFile) + "\" width=\"100%\" height=\"92%\"/>"); else out.println("<iframe id=\"show\" src=\"OpenFile?file=" + openFile + "&type=" + type + "\" width=\"100%\" height=\"92%\"/>");
        out.println("</body>");
        out.println("</html>");
    }
}
