package org.gbif.ipt.struts2;

import org.gbif.ipt.config.Constants;
import java.util.Map;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * An Interceptor to set the current resource in a users session.
 */
public class ResourceSessionInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = -184757845342974320L;

    public ResourceSessionInterceptor() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String requestedResource = RequireManagerInterceptor.getResourceParam(invocation);
        if (requestedResource != null) {
            Map<String, Object> session = invocation.getInvocationContext().getSession();
            session.put(Constants.SESSION_RESOURCE, requestedResource);
        }
        return invocation.invoke();
    }
}
