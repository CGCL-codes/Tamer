package tony;

import javax.servlet.ServletRequest;

public class Navbar {

    public static String getNavbar(ServletRequest request) {
        StringBuilder navbarhtml = new StringBuilder("<ul>");
        String[] items = new String[] { "Free Gifts", "Play", "Friends", "Help", "Add Pieces of Eight", "Bookmark Pirates" };
        String par = request.getParameter("id");
        if (par == null || par.equals("undefined")) par = "1";
        for (int i = 0; i < items.length; i++) {
            if (Integer.toString(i).equals(par)) navbarhtml.append("<li class=\"current\">"); else navbarhtml.append("<li>");
            navbarhtml.append("<a href=\"FBPirates.jsp?id=" + i + "\">" + items[i] + "</a></li>");
        }
        navbarhtml.append("</ul>");
        return navbarhtml.toString();
    }

    public static String getSrc(ServletRequest request) {
        String par = request.getParameter("id");
        if (par == null || par.equals("undefined")) par = "1";
        return "page" + par + ".html";
    }
}
