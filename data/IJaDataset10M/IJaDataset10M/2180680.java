package wabawt;

import java.io.*;

public abstract class Window extends Container {

    MenuBar menubar = null;

    StatusBar statusbar = null;

    String title = "";

    String onload = "";

    public void setMenuBar(MenuBar menubar) {
        this.menubar = menubar;
    }

    public MenuBar getMenuBar() {
        return menubar;
    }

    public void setStatusBar(StatusBar statusbar) {
        this.statusbar = statusbar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOnload(String onload) {
        this.onload = onload;
    }

    public void generateHTML(PrintWriter out) {
        String act = request.getParameter("action");
        String type = request.getParameter("type");
        if (act == null || act.length() < 1) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + title + "</title>");
            if (style.length() > 0) {
                out.println(style);
            }
            if (menubar != null) {
                menubar.generateCSS(out);
                menubar.generateScript(out);
            }
            if (statusbar != null) {
                statusbar.generateScript(out);
            }
            if (script.length() > 0) {
                out.println(script);
            }
            out.println("</head>");
            out.print("<body style=\"border:none\"");
            if (menubar != null) {
                if (onload.length() > 0) out.println(" onload=\"_menu_init();" + onload + "\" onclick=\"_menu_cancel()\""); else out.println(" onload=\"_menu_init()\" onclick=\"_menu_cancel()\"");
            } else if (onload.length() > 0) out.println(" onload=\"" + onload + "\"");
            out.println(" scroll=no bottommargin=0 leftmargin=0 rightmargin=0 topmargin=0 bgcolor=#" + Integer.toHexString(getBackground().getRGB()).toUpperCase().substring(2) + ">");
            out.println("<table width=100% height=100% cellpadding=0 cellspacing=0>");
            if (menubar != null) {
                out.println("<tr><td height=1>");
                menubar.generateHTML(out);
                out.println("</td></tr>");
            }
            out.println("<tr><td>");
            if (html.length() > 0) {
                out.println(html);
            }
            if (layoutMgr != null) {
                layoutMgr.generateHTML(out, component, ncomponents, "#" + Integer.toHexString(getBackground().getRGB()).toUpperCase().substring(2), getBorder().toString());
            } else {
                for (int i = 0; i < ncomponents; i++) {
                    component[i].generateHTML(out);
                }
            }
            if (html.length() > 0) out.println(html);
            if (statusbar != null) {
                out.println("</td></tr>");
                out.println("<tr><td height=1>");
                statusbar.generateHTML(out);
                out.println("</td></tr></table>");
            } else {
                out.println("</td></tr></table>");
            }
            out.println("</body>");
            out.println("</html>");
        } else {
            String id = act;
            int pos = act.indexOf(".");
            if (pos > 0) id = act.substring(0, pos);
            for (int i = 0; i < ncomponents; i++) {
                if (component[i].id.equals(id)) {
                    if (pos > 0) component[i].generateContent(out, act.substring(pos + 1, act.length()), type); else component[i].generateContent(out, type);
                }
            }
            if (html.length() > 0) out.println(html);
        }
    }

    public void generateXML(PrintWriter out) {
        String layout = "0";
        String sb = "false";
        if (layoutMgr instanceof BorderLayout) layout = "2"; else if (layoutMgr instanceof FlowLayout) layout = "1";
        if (statusbar != null) sb = "true";
        out.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
        out.println("<window x=\"" + left + "\" y=\"" + top + "\" width=\"" + width + "\" height=\"" + height + "\" bgcolor=\"#" + Integer.toHexString(getBackground().getRGB()).toUpperCase().substring(2) + "\" id=\"" + id + "\" caption=\"" + title + "\" layout=\"" + layout + "\" statusbar=\"" + sb + "\">");
        generateXMLContent(out);
        if (statusbar != null) statusbar.generateXML(out);
        out.println("</window>");
    }

    public void generateXMLContent(PrintWriter out) {
        if (script.length() > 0) {
            out.println(script);
        }
        if (menubar != null) menubar.generateXML(out);
        for (int i = 0; i < ncomponents; i++) {
            component[i].generateXML(out);
        }
    }
}
