package logview;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logview.mbeans.LogFile;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public class LiveServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final transient java.util.logging.Logger log = java.util.logging.Logger.getLogger(LiveServlet.class.getName());

    private int defaultLineCount = 25;

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String uri = req.getRequestURI();
        final String key = req.getRequestURI().substring(uri.lastIndexOf('/') + 1, uri.lastIndexOf('.'));
        int lines = defaultLineCount;
        final String linesParam = req.getParameter("lines");
        if (!StringUtils.isEmpty(linesParam)) {
            try {
                lines = Integer.valueOf(linesParam).intValue();
            } catch (Exception unused) {
            }
        }
        log.fine("id: " + key);
        final MBeanServer server = InitServlet.getMBeanServer();
        try {
            ObjectName on = ObjectName.getInstance(InitServlet.DOMAIN + ":id=" + key + ",type=" + LogFile.class.getName());
            Set mbeans = server.queryMBeans(on, null);
            if (mbeans != null && mbeans.size() == 1) {
                ObjectInstance oi = (ObjectInstance) mbeans.iterator().next();
                String file = (String) server.getAttribute(oi.getObjectName(), "Filename");
                sendLog(resp.getWriter(), new FileReader(file), lines);
            } else {
                throw new NoSuchElementException("MBean " + on + " not found");
            }
        } catch (Exception e) {
            throw new ServletException(e.toString());
        }
    }

    private void sendLog(final PrintWriter w, final Reader reader, final int lines) throws IOException {
        Validate.isTrue(lines > 0, "lines must be positive greated than zero");
        final LineNumberReader br = new LineNumberReader(reader);
        final List<String> queue = new ArrayList<String>();
        String l = null;
        while ((l = br.readLine()) != null) {
            if (queue.size() > lines) {
                queue.remove(0);
            }
            queue.add(br.getLineNumber() + ": " + l);
        }
        log.fine(l);
        w.write("<pre><tt>");
        for (String string : queue) {
            w.write(StringEscapeUtils.escapeHtml(string));
            w.write("\n");
        }
        w.flush();
        w.write("</tt></pre>");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(ServletConfig config) throws ServletException {
        log.fine(LiveServlet.class.getName() + " initialized");
    }

    @Override
    public void destroy() {
        super.destroy();
        log.fine(LiveServlet.class.getName() + " destroyed");
    }
}
