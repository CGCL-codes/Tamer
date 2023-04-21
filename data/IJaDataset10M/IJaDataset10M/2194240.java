package edu.nus.iss.ejava.team4.interceptors;

import java.util.Map;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import edu.nus.iss.ejava.team4.interfaces.UserAware;
import edu.nus.iss.ejava.team4.model.SessionStore;
import edu.nus.iss.ejava.team4.util.Constants;

public class AuthenticationInterceptor implements Interceptor {

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
        SessionStore user = (SessionStore) session.get(Constants.SESSION_USER);
        if (user == null) {
            return Action.LOGIN;
        } else {
            Action action = (Action) actionInvocation.getAction();
            if (action instanceof UserAware) {
                ((UserAware) action).setUser(user);
            }
            return actionInvocation.invoke();
        }
    }
}
