package org.zkoss.spring.js.ajax;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.js.ajax.AjaxHandler;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 * Handle ZK Event trigger Flow transition.
 * 
 * @author henrichen
 * @since 1.1
 */
public class ZkAjaxHandler implements AjaxHandler {

    public static final String POPUP = "zkoss.spring.webflow.POPUP";

    public static final String POPUP_EVENT = "onZkSpringWebFlow_POPUP_EVENT";

    public boolean isAjaxRequest(HttpServletRequest request, HttpServletResponse response) {
        return Executions.getCurrent() != null;
    }

    public void sendAjaxRedirect(String targetUrl, HttpServletRequest request, HttpServletResponse response, boolean popup) throws IOException {
        final String contextPath = request.getContextPath();
        if (targetUrl.startsWith(contextPath)) {
            targetUrl = targetUrl.substring(contextPath.length());
        }
        if (popup) {
            doPopup(targetUrl);
        } else {
            Executions.getCurrent().sendRedirect(targetUrl);
        }
    }

    private void doPopup(String targetUrl) {
        final Execution exec = Executions.getCurrent();
        final Event event = (Event) exec.getAttribute("actionEvent");
        final Component popTarget = event.getTarget();
        exec.setAttribute(ZkAjaxHandler.POPUP, popTarget);
        Events.sendEvent(new Event(POPUP_EVENT, popTarget, targetUrl));
    }
}
