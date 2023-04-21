package simplephoto.util;

import java.sql.SQLException;
import java.util.Map;
import simplephoto.exception.InvalidCredentialsException;
import simplephoto.exception.InvalidLoginException;
import simplephoto.exception.NotInOurDBException;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthenticationUserInterceptor implements Interceptor {

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        String tempLogin;
        String tempCredentials;
        String credentials;
        String login;
        Map session = actionInvocation.getInvocationContext().getSession();
        User user = (User) session.get("User");
        tempLogin = (String) session.get("TempLogin");
        tempCredentials = (String) session.get("TempCredentials");
        System.out.println("USER IS NULL");
        Map map = actionInvocation.getInvocationContext().getParameters();
        if (map.get("credentials") == null || map.get("login") == null) {
            if (tempLogin.compareTo("") == 0 || tempCredentials.compareTo("") == 0) {
                return "authenticationexception";
            } else {
                credentials = tempCredentials;
                login = tempLogin;
            }
        } else {
            credentials = ((String[]) map.get("credentials"))[0];
            login = ((String[]) map.get("login"))[0];
            session.put("TempLogin", login);
            session.put("TempCredentials", credentials);
        }
        try {
            if (nullOrEmpty(credentials) || nullOrEmpty(login)) throw new InvalidLoginException("Missing login or password");
            user = LoginServer.validateLogin(login, credentials);
        } catch (NotInOurDBException e) {
            return "notinourdbexception";
        } catch (Exception e) {
            LogServer.logException("AuthenticationUserInterceptor", e);
            return "authenticationexception";
        }
        session.put("User", user);
        System.out.println("hi\n");
        if (actionInvocation.getAction() instanceof ActiveUserAware) ((ActiveUserAware) actionInvocation.getAction()).setActiveUser(user);
        return actionInvocation.invoke();
    }

    public boolean nullOrEmpty(String str) {
        if (str == null || str.length() == 0) return true;
        return false;
    }
}
