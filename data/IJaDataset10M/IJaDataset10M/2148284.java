package werkzeugkasten.resource.synchronizer.server;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Servlet;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import werkzeugkasten.common.runtime.ExtensionAcceptor;
import werkzeugkasten.common.util.StringUtil;
import werkzeugkasten.resource.synchronizer.Activator;

/**
 * 
 * @author taichi
 * 
 */
public class JettyLauncher {

    public static final int DEFAULT_HTTP_PORT = 8386;

    protected File workDir;

    protected Server server = null;

    public JettyLauncher(File workDir) {
        this.workDir = workDir;
    }

    public void start() {
        if (this.server == null || this.server.isRunning() == false) {
            this.server = new Server(DEFAULT_HTTP_PORT);
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            server.setHandler(contexts);
            final Context root = new Context(contexts, "/", Context.NO_SESSIONS);
            final Set<String> urls = new HashSet<String>();
            ExtensionAcceptor.accept(Activator.PLUGIN_ID, "servlet", new ExtensionAcceptor.ExtensionVisitor() {

                public boolean visit(IConfigurationElement e) {
                    if ("servlet".equals(e.getName())) {
                        try {
                            Object o = e.createExecutableExtension("class");
                            String path = e.getAttribute("url-pattern");
                            if (o instanceof Servlet && StringUtil.isEmpty(path) == false && urls.contains(path) == false) {
                                urls.add(path);
                                ServletHolder holder = new ServletHolder((Servlet) o);
                                root.addServlet(holder, path);
                            }
                        } catch (CoreException ex) {
                            Activator.log(ex);
                        }
                    }
                    return true;
                }
            });
            try {
                server.start();
            } catch (Exception e) {
                Activator.log(e);
            }
        }
    }

    public boolean isRunning() {
        return this.server != null && this.server.isRunning();
    }

    public void stop() {
        if (isRunning()) {
            try {
                this.server.stop();
            } catch (Exception e) {
                Activator.log(e);
            }
        }
    }
}
