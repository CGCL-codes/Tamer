package net.sf.dropboxmq.workflow.adapters.http;

import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dropboxmq.workflow.adapters.ejb.WorkflowLocal;
import net.sf.dropboxmq.workflow.data.EventPackage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created: 19 Mar 2011
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision$, $Date$
 */
public class WorkflowServlet extends HttpServlet {

    private static final long serialVersionUID = 7085583864535407007L;

    private static final Log log = LogFactory.getLog(WorkflowServlet.class);

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final EventPackage eventPackage = HTTPAdapter.convertRequest(req);
        final WorkflowLocal ejb = lookupEJB();
        final EventPackage responseEvent = ejb.onEvent(HTTPAdapter.HTTP, eventPackage.getContent(), eventPackage.getEventProperties());
        if (responseEvent == null) {
            throw new RuntimeException("Response event was not generated");
        }
        final PrintWriter writer = resp.getWriter();
        writer.println(responseEvent.getContent());
    }

    private WorkflowLocal lookupEJB() {
        Context context = null;
        try {
            context = new InitialContext();
            return (WorkflowLocal) context.lookup("dropboxmq_esb/WorkflowEJB/local");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException e) {
                    log.warn("Ignoring NamingException while calling Context.close(), " + e.getMessage());
                }
            }
        }
    }
}
