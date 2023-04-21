package com.laoer.bbscs.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.laoer.bbscs.comm.*;

public class RequestBasePathInterceptor extends AbstractInterceptor {

    /**
	 *
	 */
    private static final long serialVersionUID = 369409256340402236L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        ActionContext ac = invocation.getInvocationContext();
        Object action = invocation.getAction();
        if (action instanceof RequestBasePathAware) {
            HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
            StringBuffer sb = new StringBuffer();
            sb.append(BBSCSUtil.getWebRealPath(request));
            sb.append(request.getContextPath());
            sb.append("/");
            ((RequestBasePathAware) action).setBasePath(sb.toString());
        }
        return invocation.invoke();
    }
}
