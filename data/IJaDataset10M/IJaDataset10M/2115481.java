package org.soybeanMilk.web.exe.th;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soybeanMilk.web.WebObjectSource;
import org.soybeanMilk.web.exe.WebAction;
import org.soybeanMilk.web.exe.WebAction.Target;

/**
 * "forward"类型目标处理器。
 * @author earthAngry@gmail.com
 * @date 2011-4-19
 *
 */
public class ForwardTargetHandler extends AbstractTargetHandler {

    private static Log log = LogFactory.getLog(ForwardTargetHandler.class);

    public ForwardTargetHandler() {
        super();
    }

    public void handleTarget(WebAction webAction, WebObjectSource webObjectSource) throws ServletException, IOException {
        String url = getActualTargetUrl(webAction, webObjectSource);
        if (url == null) throw new NullPointerException("the url must not be null in '" + Target.FORWARD + "' type target");
        HttpServletRequest request = webObjectSource.getRequest();
        HttpServletResponse response = webObjectSource.getResponse();
        if (isJspIncludeRequest(request)) {
            request.getRequestDispatcher(url).include(request, response);
            if (log.isDebugEnabled()) log.debug("include '" + url + "' for request");
        } else {
            request.getRequestDispatcher(url).forward(request, response);
            if (log.isDebugEnabled()) log.debug("forward '" + url + "' for request");
        }
    }
}
