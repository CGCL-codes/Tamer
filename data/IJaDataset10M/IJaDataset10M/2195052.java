package fedora.server.errors.servletExceptionExtensions;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p><b>Title:</b> Exception500.java</p>
 * <p><b>Description:</b> Thrown to reach 500-Internal Server Error error page.  
 * Can be used when forwarding can't, e.g., after some http output has already been written.</p>
 *
 * @author wdn5e@virginia.edu
 * @version $Id: InternalError500Exception.java 5162 2006-10-25 00:49:06Z eddie $
 */
public class InternalError500Exception extends RootException {

    private static final long serialVersionUID = 1L;

    public InternalError500Exception(HttpServletRequest request, String action, String detail, String[] details) {
        super(request, action, detail, details);
    }

    public InternalError500Exception(String message, HttpServletRequest request, String action, String detail, String[] details) {
        super(message, request, action, detail, details);
    }

    public InternalError500Exception(String message, Throwable cause, HttpServletRequest request, String action, String detail, String[] details) {
        super(message, cause, request, action, detail, details);
    }
}
