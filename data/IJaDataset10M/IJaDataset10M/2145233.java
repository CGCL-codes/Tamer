package wabawt;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;

public class Report extends Container {

    int width = 740;

    int height = 1060;

    int detailHeight;

    int leftmargin = 0;

    int rightmargin = 0;

    int topmargin = 0;

    int bottommargin = 0;

    Band header;

    Band detail;

    Band summary;

    Band footer;

    Color background = new Color(255, 255, 255);

    HttpServletRequest request;

    String query;

    Vector columns = new Vector();

    String driver;

    String url;

    int rownumber;

    Connection conn;

    Object cols[];

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setMargins(int left, int right, int top, int bottom) {
        this.leftmargin = left;
        this.rightmargin = right;
        this.topmargin = top;
        this.bottommargin = bottom;
    }

    public void setHeader(Band band) {
        header = band;
    }

    public void setDetail(Band band) {
        detail = band;
    }

    public void setSummary(Band band) {
        summary = band;
    }

    public void setFooter(Band band) {
        footer = band;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addColumn(DBColumn column) {
        columns.addElement(column);
    }

    private void loadDriver() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName(driver).newInstance();
    }

    private void connect() throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    public void generateXML(PrintWriter out) {
        out.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
        out.print("<report ");
        out.print("width=\"" + width + "\" ");
        out.print("height=\"" + height + "\" ");
        out.print("driver=\"" + xmldecode(driver) + "\" ");
        out.print("url=\"" + xmldecode(url) + "\" ");
        out.print("leftmargin=\"" + leftmargin + "\" ");
        out.print("rightmargin=\"" + rightmargin + "\" ");
        out.print("topmargin=\"" + topmargin + "\" ");
        out.print("bottommargin=\"" + bottommargin + "\" ");
        out.print("background=\"#" + Integer.toHexString(background.getRGB()).toUpperCase().substring(2) + "\" ");
        out.println(">");
        out.println("<query>");
        out.println(xmldecode(query.trim()));
        out.println("</query>");
        header.generateXML(out, "header");
        detail.generateXML(out, "detail");
        summary.generateXML(out, "summary");
        footer.generateXML(out, "footer");
        out.println("</report>");
    }

    public void generateHTML(PrintWriter out) {
        String act = request.getParameter("action");
        if (act == null || act.length() < 1) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>");
            out.println("</title>");
            out.println("</head>");
            out.println("<body scroll=no leftmargin=" + leftmargin + " rightmargin=" + rightmargin + " topmargin=" + topmargin + " bottommargin=" + bottommargin + ">");
            out.println("<table width=100% height=100% cellpadding=0 cellspacing=0>");
            out.println("<tr><td height=" + header.getHeight() + "><iframe src=\"" + request.getRequestURI() + "?class=" + request.getParameter("class") + "&action=header\" width=100% height=100% frameborder=0 scrolling=no></iframe></td></tr>");
            out.println("<tr><td height=100%><iframe src=\"" + request.getRequestURI() + "?class=" + request.getParameter("class") + "&action=detail\" width=100% height=100% frameborder=0></iframe></td></tr>");
            out.println("<tr><td height=" + footer.getHeight() + "><iframe src=\"" + request.getRequestURI() + "?class=" + request.getParameter("class") + "&action=footer\" width=100% height=100% frameborder=0 scrolling=no></iframe></td></tr>");
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } else generateContent(out, act);
    }

    public void generateContent(PrintWriter out, String action) {
        if (action.equals("header")) {
            header.generateHTML(out, this, 0);
        } else if (action.equals("footer")) {
            footer.generateHTML(out, this, 0);
        } else if (action.equals("detail")) {
            try {
                int count = columns.size();
                int offset = 0;
                rownumber = 0;
                loadDriver();
                connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                cols = new Object[count];
                while (rs.next()) {
                    rownumber++;
                    for (int i = 0; i < count; i++) {
                        DBColumn col = (DBColumn) columns.elementAt(i);
                        cols[i] = rs.getObject(col.name);
                    }
                    detail.generateHTML(out, this, offset);
                    offset += detail.getHeight();
                }
                rs.close();
                conn.close();
            } catch (InstantiationException e) {
                out.println("An InstantiationException occured during report generation:");
            } catch (IllegalAccessException e) {
                out.println("An IllegalAccessException occured during report generation:");
            } catch (ClassNotFoundException e) {
                out.println("An ClassNotFoundException occured during report generation:");
            } catch (SQLException e) {
                out.println("An SQLException occured during report generation:");
            }
        }
    }

    public Object getValue(String column) {
        int count = columns.size();
        for (int i = 0; i < count; i++) {
            DBColumn col = (DBColumn) columns.elementAt(i);
            if (col.name.equals(column)) {
                return cols[i];
            }
        }
        return null;
    }
}
