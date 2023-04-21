package xmlmanager.core;

import java.util.Properties;
import javax.xml.transform.stream.StreamResult;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * XQueryJob - an eclipse platform Job to run XQuery in background <br/>
 * this is a user job so it should show up in the progress monitor and the user will have access to cancel it
 *
 * @author jbialos
 */
public class XQueryJob extends Job {

    XQueryExpression expression;

    DynamicQueryContext context;

    StreamResult streamResult;

    Properties properties;

    /**
     * Set up an xquery to run as a scheduled background process<br/>
     *
     * @param expression
     *            - XQuery Expression. A saxon specific expression
     * @param context
     *            - query context. A saxon specific expression
     * @param streamResult
     *            - StreamResult where the output will be sent
     * @param properties
     *            - properties for the execution of the expression
     */
    protected XQueryJob(XQueryExpression expression, DynamicQueryContext context, StreamResult streamResult, Properties properties) {
        super("XQuery Job");
        this.expression = expression;
        this.context = context;
        this.streamResult = streamResult;
        this.properties = properties;
        setUser(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            System.out.println("About to run");
            expression.run(context, streamResult, properties);
            System.out.println("Done running");
        } catch (XPathException e) {
            e.printStackTrace();
            MessageConsole mc = QueryRunner.findConsole("XQuery");
            mc.clearConsole();
            MessageConsoleStream stream = mc.newMessageStream();
            stream.setColor(QueryRunner.INSTANCE.getRed());
            stream.print(e.getMessage());
        }
        return Status.OK_STATUS;
    }
}
