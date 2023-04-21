package welo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import welo.model.jcr.CmsCss;
import welo.servlet.listener.AppContextListener;
import welo.utility.JR;
import welo.utility.jcr.JcrHelper;

/**
 * Serve css files that are stored in the database
 * @author James Yong
 */
public class CssServer extends HttpServlet {

    private static final String MIME_CSS = "text/css";

    private static final String EXTENSION_CSS = ".css";

    /**
     * We cache the script output for speed
     */
    protected final Map<String, String> scriptCache = new HashMap<String, String>();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String filepath = "";
        String contextPath = request.getContextPath();
        if (JR.isNNNE(contextPath)) {
            filepath = uri.substring(contextPath.length() + 1);
        } else {
            filepath = uri.substring(1);
        }
        if (filepath != null && filepath.endsWith(EXTENSION_CSS)) {
            System.out.println("filepath=" + filepath);
            try {
                doFile(response, filepath, MIME_CSS);
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    protected void doFile(HttpServletResponse resp, String path, String mimeType) throws Exception {
        resp.setContentType(mimeType);
        String output = null;
        synchronized (this.scriptCache) {
            output = this.scriptCache.get(path);
            if (output == null) {
                String filename = path;
                filename = filename.replaceAll("css/", "");
                System.out.println("filepath after replaced=" + filename);
                ObjectContentManager ocm = AppContextListener.getOCM();
                List list = (List) ocm.getObjects(JcrHelper.getOCMQuery(ocm, CmsCss.class, new String[] { "name=" + filename }));
                if (list.size() > 1) {
                    System.out.println("Warning!!!! More than 1 CmsCssSection found!");
                }
                CmsCss ccbean = (CmsCss) list.iterator().next();
                output = ccbean.getContent();
                this.scriptCache.put(path, output);
            }
        }
        PrintWriter out = resp.getWriter();
        out.println(output);
        out.flush();
    }
}
