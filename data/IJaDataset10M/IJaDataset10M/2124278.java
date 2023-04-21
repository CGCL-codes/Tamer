package org.apache.jsp.skins.psp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import agorum.mce.ngrc.*;
import java.util.Date;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.net.URLEncoder;
import java.net.URLDecoder;
import agorum.mce.ngrc.*;

public final class vkKeyboard3_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    /**
 * get an int setting from the settings 
 */
    int gsi(String attr, HttpServletRequest request) {
        return Integer.parseInt((String) request.getAttribute(attr));
    }

    /**
 * Format a double (seconds) to a normal time-display
 */
    String formatTime(double time) {
        DecimalFormat dcFormat = new DecimalFormat("00");
        int hour = (int) (time / 3600.0);
        int minute = (int) ((time - hour * (3600.0)) / 60.0);
        int seconds = (int) (time - hour * 3600.0 - minute * 60.0);
        String s = "";
        if (hour != 0) s += dcFormat.format(hour) + ":";
        s += dcFormat.format(minute) + ":";
        s += dcFormat.format(seconds);
        return s;
    }

    /**
 * cut away len from string
 */
    String cutString(String s, int len) {
        if (s.length() > len) {
            s = s.substring(0, len - 2) + "...";
        }
        return s;
    }

    /**
 * fix Colons
 */
    String fixHok(String s) {
        return s.replaceAll("\"", "\\\\\"");
    }

    /**
 * encode with ISO
 */
    String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "ISO-8859-1");
        } catch (Exception e) {
        }
        return null;
    }

    /**
 * get the duration for each title in an album
 */
    double getAlbumDuration(ArrayList list) {
        double durationSum = 0.0;
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof DetailListBean) {
                DetailListBean title = (DetailListBean) o;
                durationSum += title.getDuration();
            }
        }
        return durationSum;
    }

    /**
 * create the image map for a virtual keyboard
 */
    void createVKMap(String[][] cmds, int offsX, int offsY, int width, int height, String ident, JspWriter out) throws Exception {
        String s = "";
        s += "<map name=\"" + ident + "\">\n";
        int lx = cmds[0].length;
        int ly = cmds.length;
        double ox = (double) width / (double) lx;
        double oy = (double) height / (double) ly;
        for (int y = 0; y < ly; y++) {
            for (int x = 0; x < lx; x++) {
                int rx = (int) (offsX + x * (double) ox);
                int ry = (int) (offsY + y * (double) oy);
                if (!cmds[y][x].equals("000")) {
                    s += "<area shape=\"rect\" coords=\"" + (rx + "," + ry + "," + (rx + ox) + "," + (ry + oy)) + "\"" + " href=\"JavaScript:doSendKey('" + cmds[y][x] + "')\">\n";
                }
            }
        }
        s += "</map>\n";
        out.println(s);
    }

    /**
 * create the image map for the filter keyboard
 */
    void createFilterMap(String[][] cmds, int offsX, int offsY, int width, int height, String ident, JspWriter out) throws Exception {
        String s = "";
        s += "<map name=\"" + ident + "\">\n";
        int lx = cmds[0].length;
        int ly = cmds.length;
        double ox = (double) width / (double) lx;
        double oy = (double) height / (double) ly;
        for (int y = 0; y < ly; y++) {
            for (int x = 0; x < lx; x++) {
                int rx = (int) (offsX + x * (double) ox);
                int ry = (int) (offsY + y * (double) oy);
                if (!cmds[y][x].equals("000")) {
                    s += "<area shape=\"rect\" coords=\"" + (rx + "," + ry + "," + (rx + ox) + "," + (ry + oy)) + "\"" + " href=\"JavaScript:doMusicFilter('" + cmds[y][x] + "')\">\n";
                }
            }
        }
        s += "</map>\n";
        out.println(s);
    }

    private static java.util.List _jspx_dependants;

    static {
        _jspx_dependants = new java.util.ArrayList(4);
        _jspx_dependants.add("/skins/psp/../default/vkKeyboard3.jsp");
        _jspx_dependants.add("/skins/psp/../default/../../skin.jsp");
        _jspx_dependants.add("/settings_include.jsp");
        _jspx_dependants.add("/skins/psp/../default/../../ngrctools_include.jsp");
    }

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        JspFactory _jspxFactory = null;
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            _jspxFactory = JspFactory.getDefaultFactory();
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write('\r');
            out.write('\n');
            org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/skins/" + NgRCCommHelper.staticSkinName + "/settings.jsp", out, false);
            out.write("\r\n");
            out.write("\r\n");
            String skinName = NgRCCommHelper.staticSkinName;
            out.write('\r');
            out.write('\n');
            out.write('\r');
            out.write('\n');
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\t\r\n");
            String[][] rcCommands = new String[][] { { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l" }, { "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x" }, { "y", "z", "*", "#", " ", " ", " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "js:parent.hideKB()" } };
            out.write("\r\n");
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("\r\n");
            out.write("\t<head>\r\n");
            out.write("\t\t<link rel=\"stylesheet\" href=\"../");
            out.print(skinName);
            out.write("/styles.css.jsp\" type=\"text/css\"></link>\r\n");
            out.write("\t\t<script src=\"../../tools.js.jsp\" language=\"JavaScript\"></script>\r\n");
            out.write("\t</head>\r\n");
            out.write("\t\r\n");
            out.write("\t<body class=\"contentBody\" \r\n");
            out.write("\t\tmarginwidth=\"0\" marginheight=\"0\" topmargin=\"0\" leftmargin=\"0\">\r\n");
            out.write("\r\n");
            out.write("\t\t");
            out.write("\r\n");
            out.write("\t\t<div class=\"virtKeyboard2\">\r\n");
            out.write("\t\t\t<img border=\"0\" src=\"../");
            out.print(skinName);
            out.write("/images/keyb3.png\" usemap=\"#rcCommands\">\r\n");
            out.write("\t\t\t");
            createFilterMap(rcCommands, 3, 3, 370, 124, "rcCommands", out);
            out.write("\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t</body>\r\n");
            out.write("</html>\r\n");
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
}
