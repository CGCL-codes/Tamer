package ch.dvbern.lib.jampp.servlet;

import ch.dvbern.lib.jampp.multipart.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet 2.3 filter that replaces the standard HttpServletRequest with MulipartServletRequest if the content type
 * of the request is multipart/form-data.
 *
 *@author   $Author: dmilic $
 *@version  $Revision: 1.3 $
 */
public class MultipartFilter implements Filter {

    private static final String CONTEXT_TEMPORARY = "javax.servlet.context.tempdir";

    private static final String UPLOAD_DIRECTORY = "upload.directory";

    private static final String REQUEST_LENGTH_LIMIT = "request.limit";

    private File tmpDir;

    private int requestLengthLimit;

    /**
     * Initializes the filter, it determines the temporary directory for the file uploads and makes
     * sure that the temporary directory is writeable. It also determines the maximal request length (if one is defined).
     * <p> acceptet parameter are:
     * <table border=5>
     *  <tr><td><b>upload.directory</b></td><td>directory for temporary files, if this parameter is not set, the temporary directory for the context is used.</td></tr>
     *  <tr><td><b>request.limit</b></td><td>limit (in bytes) for a input - used to stop DOS attacks default == -1 with means no limit</td></tr>
     * </table>
     *@param cnf                   Fileter configuration.
     *@exception ServletException  Thrown if the temporary directory could not be found or is not writeable.
     */
    public void init(FilterConfig cnf) throws ServletException {
        String dir = cnf.getInitParameter(UPLOAD_DIRECTORY);
        if (dir != null) {
            tmpDir = new File(dir);
        } else {
            tmpDir = (File) cnf.getServletContext().getAttribute(CONTEXT_TEMPORARY);
        }
        if (tmpDir == null) throw new ServletException("could not find directory for file upload");
        if (!tmpDir.isDirectory()) throw new ServletException(tmpDir.getAbsolutePath() + " is not a directory");
        if (!tmpDir.canWrite()) throw new ServletException(tmpDir.getAbsolutePath() + " is not writable");
        requestLengthLimit = -1;
        String rl = cnf.getInitParameter(REQUEST_LENGTH_LIMIT);
        if (rl != null) {
            try {
                requestLengthLimit = Integer.parseInt(rl);
            } catch (NumberFormatException nfe) {
                throw new ServletException("parameter '" + REQUEST_LENGTH_LIMIT + "' is not a number");
            }
        }
    }

    /**
     * In this method determines the filter a content-type of the request and if the content-type is multipart/form-data wrapps
     * the HttpServletRequest in MultpartServletRequest. It also uses LimitedLineInput wrapper if the input length is limited
     * by init parameter.
     *
     *@param request               Servlet request
     *@param response              Servlet response
     *@param chain                 Servlet chain
     *@exception IOException       
     *@exception ServletException  
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String type = req.getHeader("Content-Type");
        if (type != null && type.startsWith("multipart/form-data")) {
            LineInput li = new ServletLineInput(req.getInputStream());
            if (requestLengthLimit >= 0) li = new LimitedLineInput(li, requestLengthLimit);
            MultipartParser up = new MultipartParser(li, MultipartParser.extractBoundary(type), tmpDir);
            List l = up.parse();
            req = new MultipartServletRequest(req, l);
        }
        chain.doFilter(req, response);
    }

    public void destroy() {
    }
}
