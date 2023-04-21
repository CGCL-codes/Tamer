package lebah.upload;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jspsmart.upload.SmartUpload;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SmartUpload2 extends SmartUpload {

    public final void initialize2(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        m_application = context;
        m_request = request;
        m_response = response;
    }
}
