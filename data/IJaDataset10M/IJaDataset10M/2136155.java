package org.opennms.web.response;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opennms.web.MissingParameterException;
import org.opennms.web.Util;

/**
 * A servlet that creates an XML dump of network performance data
 * using the <a href="http://www.rrdtool.org/">RRDTool</a>.
 *
 * <p>This servlet executes an <em>rrdtool dump</em> command
 * in another process, piping its XML output to the 
 * <code>ServletOutputStream</code>. </p>
 *
 * @author <A HREF="mailto:larry@opennms.org">Lawrence Karnowski</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 */
public class RRDDumpServlet extends HttpServlet {

    /** 
     * The working directory where we find the RRD files.
     */
    protected String workDir;

    /** 
     * The dump command (minus the actual RRD filename).
     */
    protected String commandPrefix;

    /**
     * Initializes this servlet by reading the rrdtool-graph properties file.
     */
    public void init() throws ServletException {
        ServletConfig config = this.getServletConfig();
        this.workDir = config.getInitParameter("rrd-directory");
        this.commandPrefix = config.getInitParameter("rrd-dump-command");
        if (this.workDir == null || this.commandPrefix == null) {
            throw new ServletException("Did not get all required init params. rrd-directory and rrd-dump-command are both required.  Please check your web.xml.");
        }
    }

    /**
     * Checks the parameters passed to this servlet, and if all are okay, executes
     * the RRDTool command in another process and pipes its XML output to the 
     * <code>ServletOutputStream</code> back to the requesting web browser.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rrd = request.getParameter("rrd");
        if (rrd == null) {
            throw new MissingParameterException("rrd");
        }
        String command = this.commandPrefix + " " + rrd;
        this.log(command);
        String[] commandArray = Util.createCommandArray(command, '@');
        Process process = Runtime.getRuntime().exec(commandArray, null, new File(this.workDir));
        PrintWriter out = response.getWriter();
        BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        if (err.ready()) {
            StringWriter tempErr = new StringWriter();
            Util.streamToStream(err, tempErr);
            String errorMessage = tempErr.toString();
            this.log("Read from stderr: " + errorMessage);
            response.setContentType("text/plain");
            Util.streamToStream(new StringReader(errorMessage), out);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            response.setContentType("text/xml");
            Util.streamToStream(in, out);
        }
        out.close();
    }
}
