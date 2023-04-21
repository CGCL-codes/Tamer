package hr.chus.cchat.struts2.action.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Web GET or POST action used for testing.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
public class Test extends ActionSupport implements ServletRequestAware {

    private static final long serialVersionUID = 1L;

    private Log log = LogFactory.getLog(getClass());

    private HttpServletRequest request;

    private String test;

    @Override
    public String execute() throws Exception {
        log.info(request.getCharacterEncoding());
        log.info(test);
        return SUCCESS;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
}
