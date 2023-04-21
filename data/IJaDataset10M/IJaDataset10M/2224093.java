package com.sheng.interceptor;

import java.util.Map;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class CheckuserexistInterceptor extends AbstractInterceptor {

    /**
	 * ���Ա��״̬��0��?���ϰ࣬1����Ѿ������ϰ�
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String forward = "";
        ActionContext ctx = invocation.getInvocationContext();
        Map<String, Object> session = ctx.getSession();
        try {
            int i = (Integer) session.get("existstate");
            if (i == 1) {
                forward = Action.NONE;
            } else {
                forward = invocation.invoke();
            }
        } catch (Exception e) {
            e.printStackTrace();
            forward = Action.NONE;
        }
        return forward;
    }
}
