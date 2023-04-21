package net.sf.aft.test;

import java.io.PipedWriter;
import javax.servlet.http.HttpServletResponse;
import net.sf.aft.util.Queue;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * Starts listening for an incoming HTTP request.
 *
 * <p>If no port is specified, the default port is the first port
 * declared using the <code>servletContainer</code> element. The local
 * path to which the listening occurs is specified using the
 * <code>path</code> attribute.
 *
 * <p>The execution of the Ant script will stop until a request
 * is received at the given local path. When the request is received,
 * the <code>listener</code> element start the matching process using
 * the matcher set described by the enclosed <code>match</code>
 * elements.
 *
 * <p>You can specify a timeout for which a request is expected. If
 * there's no request received, or if the request is received, but no
 * matchers succeed, the <code>listener</code> element fails.
 *
 * @author <a href="mailto:ovidiu@cup.hp.com">Ovidiu Predescu</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2001/12/31 16:23:45 $
 * @since September 26, 2001
 */
public class Listener extends ActionTask {

    HttpClient httpClient;

    int port = 0;

    String path;

    int timeout = 0;

    HttpMessage request;

    HttpServletResponse servletResponse;

    PipedWriter responseWriter;

    int debug = 0;

    public Listener() {
    }

    public Listener(int port, String path) {
        this.port = port;
        this.path = path;
    }

    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof Listener)) return false;
        Listener other = (Listener) another;
        return port == other.port && path.equals(other.path);
    }

    public int hashCode() {
        return port + (path == null ? 0 : path.hashCode());
    }

    public String toString() {
        return "[Listener: port " + port + ", path " + path + "]";
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setHttpClient(HttpClient client) {
        httpClient = client;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public void setHttpMessageRequest(HttpMessage request) {
        this.request = request;
    }

    public void setHttpServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
        objectModel.put("HttpServletResponse", servletResponse);
    }

    public void setResponseWriter(PipedWriter writer) {
        this.responseWriter = writer;
        objectModel.put("ResponseWriter", writer);
    }

    public void execute() {
        result = false;
        if (port == 0) port = ServletContainer.getServletContainer().getDefaultPort();
        ServletContainer container = ServletContainer.getServletContainer();
        Queue requestsQueue = container.getRequestsQueue();
        container.registerListener(this);
        requestsQueue.get(this, timeout);
        if (debug > 0) println("Received message " + request);
        if (request == null) return;
        matcherSet.setMatchOn(request);
        super.execute();
        try {
            if (objectModel.get("SentResponse") == null) {
                servletResponse.setContentType("text/html");
            }
            responseWriter.close();
        } catch (Exception ex) {
            println("Cannot write the response to the client: " + ex);
        }
    }
}
