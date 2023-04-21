package metso.paradigma.portal.interceptor;

import metso.dal.ConnectionManagerHelper;
import metso.paradigma.portal.actions.BaseAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class HibernateCloseSessionInterceptor implements Interceptor {

    private static final long serialVersionUID = 2130127987215476271L;

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            return invocation.invoke();
        } catch (Exception e) {
            return BaseAction.EXCEPTION;
        } finally {
            ConnectionManagerHelper.getConnectionManager().releaseConnection();
        }
    }
}
