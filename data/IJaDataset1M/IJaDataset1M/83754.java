package com.liferay.portal.events;

import com.liferay.portal.struts.Action;
import com.liferay.portal.struts.ActionException;
import com.liferay.portal.util.WebKeys;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <a href="ClearRenderParametersAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ClearRenderParametersAction extends Action {

    public void run(HttpServletRequest req, HttpServletResponse res) throws ActionException {
        HttpSession ses = req.getSession();
        Map renderParametersPool = (Map) ses.getAttribute(WebKeys.PORTLET_RENDER_PARAMETERS);
        if (renderParametersPool != null) {
            renderParametersPool.clear();
        }
    }
}
