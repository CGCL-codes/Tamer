package mbis.web.struts.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import mbis.web.struts.action.MyActionSupport;
import org.apache.struts2.StrutsStatics;

/**
 * User: thooomas
 * Date: 30.12.2008
 * Time: 18:33:05
 */
public class ActionNameInterceptor extends AbstractInterceptor implements StrutsStatics {

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        if (actionInvocation.getProxy().getAction() instanceof MyActionSupport) {
            MyActionSupport action = (MyActionSupport) actionInvocation.getProxy().getAction();
            action.setActionName(actionInvocation.getProxy().getActionName());
        }
        return actionInvocation.invoke();
    }
}
