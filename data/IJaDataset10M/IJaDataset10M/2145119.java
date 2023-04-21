package simpleorm.simpleweb.requestlet;

import simpleorm.simpleweb.core.WPage;
import simpleorm.simpleweb.core.WException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 /**
  * An instance of this class is created for each call of Servlet.doGet|doPost.
  * Contains utility routines needed to make pages without JSPs.
  */
public abstract class WRequestlet {

    HttpServletRequest request;

    HttpServletResponse response;

    HttpServlet servlet;

    public PrintWriter out;

    boolean isGet = false;

    public <R extends WRequestlet> R initRequestlet(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet, boolean get) {
        this.request = request;
        this.response = response;
        this.servlet = servlet;
        isGet = get;
        return (R) this;
    }

    public void doGetPost() {
        try {
            out = response.getWriter();
            onGetPost();
            out.close();
        } catch (RuntimeException re) {
            throw re;
        } catch (Throwable ex) {
            throw new WException(ex);
        }
    }

    public abstract void onGetPost() throws Throwable;

    /**
     * Outputs raw HTML, does NOT quote it.
     */
    public void outRaw(String html) {
        out.print(html);
    }

    /**
     * outEscaped(data, false)
     */
    public void outEscaped(String data) {
        outEscaped(data, false);
    }

    /**
     * Outputs data after escaping it so that '&lt;' becomes '&amp;lt;' etc.<p>
     * <p/>
     * Converts spaces to '&amp;nbsp;' iff nbsp<p>
     * <p/>
     * Amazing that I need to write this myself!<p>
     *
     * @see #outRaw
     */
    public void outEscaped(String data, boolean nbsp) {
        if (data == null) out.print(""); else {
            for (int dx = 0; dx < data.length(); dx++) {
                char ch = data.charAt(dx);
                switch(ch) {
                    case ' ':
                        out.print(nbsp ? "&nbsp;" : " ");
                        break;
                    case '<':
                        out.print("&lt;");
                        break;
                    case '>':
                        out.print("&gt;");
                        break;
                    case '\"':
                        out.print("&quot;");
                        break;
                    case '\'':
                        out.print("&#039;");
                        break;
                    case '\\':
                        out.print("&#092;");
                        break;
                    case '&':
                        out.print("&amp;");
                        break;
                    default:
                        out.print(ch);
                        break;
                }
            }
        }
    }

    /**
     * URL Encodes string in appropriate? encoding.
     * used for ?=value Get parameters on links
     */
    public void outURLEncoded(String data) throws Exception {
        out.print(URLEncoder.encode(data, "UTF-8"));
    }

    /**
     * Convert a nibble to a hex character
     * @param	nibble	the nibble to convert.
     */
    public static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    /** A table of hex digits */
    private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /** Mainly to include a JSP page after the main page processing. */
    public void includeRenderer(String url) throws Exception {
        servlet.getServletConfig().getServletContext().getRequestDispatcher(url).include(request, response);
    }
}
