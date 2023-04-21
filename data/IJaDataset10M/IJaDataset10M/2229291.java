package net.neurotech.jmarks;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Subservlets involving manipulation of bookmarks.
 */
public class MarkServlets {

    /**
	 * Handles the creation of a new bookmark in the current folder.
	 */
    public static boolean newMark(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
        Folder curfolder = (Folder) session.getValue("curfolder");
        if (action.equals("newmark")) {
            Theme theme = Theme.getDefaultTheme(request, response);
            PrintWriter out = response.getWriter();
            theme.setOutput(out);
            theme.openPage("New Bookmark");
            out.println("<center><form action='" + Utils.getServlet(request) + "' method=\"post\">");
            out.println("<input type=\"hidden\" name=\"action\" value=\"newmark2\"><table border=\"0\">");
            out.println("<tr><td><font face=\"Verdana\" size=\"-1\">Bookmark Name:</font></td>");
            out.println("<td><input type=\"text\" name=\"title\"></td>");
            out.println("</tr>");
            out.println("<tr><td><font face=\"Verdana\" size=\"-1\">Bookmark URL:</font></td>");
            out.println("<td><input type=\"text\" name=\"url\"></td></tr>");
            out.println("<tr><td colspan=\"2\"><center><input type=\"submit\" value=\"Create!\"></center></td>");
            out.println("</tr>");
            out.println("</table></form>");
            out.println("</center>");
            theme.closePage();
        } else if (action.equals("newmark2")) {
            URL url;
            try {
                url = new URL(request.getParameter("url"));
            } catch (MalformedURLException ue) {
                Theme theme = Theme.getDefaultTheme(request, response);
                PrintWriter out = response.getWriter();
                theme.setOutput(out);
                theme.errorPage("Bad URL", "The fully qualified URL you entered is invalid.");
                return false;
            }
            curfolder.addMark(new Mark(request.getParameter("title"), "", request.getParameter("url"), curfolder));
            return true;
        }
        return false;
    }

    /**
	 * This handles the user clicking on a bookmark name.  It sends
	 * a redirect to the browser and calls the visited() method
	 * of the Mark object.
	 */
    public static void redirect(HttpServletRequest request, HttpServletResponse response, Mark mark) throws ServletException, IOException {
        mark.visited();
        response.sendRedirect(mark.getURL().toString());
    }

    /**
	 * Responsible for editing a bookmark's properties.
	 */
    public static void editMark(HttpServletRequest request, HttpServletResponse response, Mark mark) throws ServletException, IOException {
        Theme theme = Theme.getDefaultTheme(request, response);
        PrintWriter out = response.getWriter();
        theme.setOutput(out);
        Date last = mark.getLastVisited().getTime();
        response.setContentType("text/html");
        if (request.getParameter("check") != null) Utils.checkUpdateTime(mark);
        theme.openPage(mark.getTitle());
        out.println("<center><font face='Verdana'>" + mark.getTitle() + "</font></center><br>");
        out.println("<center><table border='0' width='60%'>");
        out.println("<form action='" + Utils.getServlet(request) + "' method='post'>");
        out.println("<tr><td>" + theme.getSmall("Name:") + "</td><td>" + theme.getSmall("<input type=text width=50 value=\"" + mark.getTitle() + "\">") + "</td></tr>");
        out.println("<tr><td>" + theme.getSmall("URL:") + "</td><td>" + theme.getSmall("<input type=text width=50 value=\"" + mark.getURL().toString() + "\">") + "</td>");
        out.println("<tr><td>" + theme.getSmall("Visits:") + "</td><td>" + theme.getSmall(new Integer(mark.countVisits()).toString()) + "</td></tr>");
        out.println("<tr><td>" + theme.getSmall("Last Visited:") + "</td><td>" + theme.getSmall(last.toString()) + "</td></tr>");
        out.println("<tr><td>" + theme.getSmall("Folder:") + "</td><td>");
        Folder mine = mark.getFolder();
        theme.openSmallFont();
        out.print(mine.getTitle());
        theme.closeSmallFont();
        out.println("</td></tr>");
        String o;
        if (mark.getLastUpdated() == null) {
            o = "Unknown";
        } else {
            Date updated = mark.getLastUpdated().getTime();
            o = updated.toString();
        }
        out.println("<tr><td>" + theme.getSmall("Last Updated: [<a href='" + Utils.getServlet(request) + "?action=edit&mark=" + Theme.encodeURL(mark.getTitle()) + "&check=yes'>Check</a>]") + "</td><td>" + theme.getSmall(o) + "</td></tr>");
        out.println("</tr></table><br>");
        out.print("<table border='0'>");
        out.print("<tr><td valign='middle'><input type='submit' value='Save Changes'></form></td>");
        out.print("<td valign='middle'><form action='" + Utils.getServlet(request) + "' method='post'>");
        out.print("<input type='hidden' name='action' value='delete'>");
        out.print("<input type='hidden' name='mark' value='" + mark.getTitle() + "'>");
        out.print("<input type='hidden' name='folder' value='" + mark.getFolder().getName() + "'>");
        out.print("<input type='submit' value='Delete'>");
        out.print("</form></td>");
        out.print("<td valign='middle'><form action='" + Utils.getServlet(request) + "' method='get'>");
        out.print("<input type='submit' value='Cancel'>");
        out.print("</form></td>");
        out.println("</tr></table></center>");
        theme.closePage();
    }

    /**
 	 * Deletes a bookmark and returns whether or not it was sucessful.
 	 */
    public static boolean deleteMark(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String mark = request.getParameter("mark");
        String folder = request.getParameter("folder");
        HttpSession session = request.getSession(false);
        if ((mark == null) || (folder == null) || (session == null)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            Theme theme = Theme.getDefaultTheme(request, response);
            theme.setOutput(out);
            theme.errorPage("Bad Request", "I wasn't sure which bookmark to delete!");
            return false;
        }
        User user = (User) session.getValue("user");
        Folder root = user.getRoot();
        Folder target;
        if ((folder.equalsIgnoreCase("root")) || (folder.equalsIgnoreCase("my bookmarks"))) {
            target = root;
        } else {
            target = root.findFolder(folder);
        }
        if (target == null) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            Theme theme = Theme.getDefaultTheme(request, response);
            theme.setOutput(out);
            theme.errorPage("Bad Request", "Containing folder \"" + folder + "\"not found.");
            return false;
        }
        Mark delmark = target.findMark(mark);
        if (delmark == null) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            Theme theme = Theme.getDefaultTheme(request, response);
            theme.setOutput(out);
            theme.errorPage("Bad Request", "Target bookmark not found.");
            return false;
        }
        target.removeMark(delmark);
        return true;
    }

    /**
	 * Code that displays a bookmark on a web page.
	 */
    public static void displayMark(HttpServletRequest request, PrintWriter out, Mark mark) {
        String description = mark.getURL().toString();
        out.println("<font face=\"Verdana\"><a title='" + description + "' href=\"" + Utils.getServlet(request) + "?goto=" + Theme.encodeURL(mark.getTitle()) + "\">" + mark.getTitle() + "</a> [<a title='" + description + "' href=\"" + Utils.getServlet(request) + "?action=edit&mark=" + Theme.encodeURL(mark.getTitle()) + "\">Details</a>]</font><br>");
    }
}
