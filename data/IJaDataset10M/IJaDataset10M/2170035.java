package at.riemers.zero.base.user.controller.edituser;

import at.riemers.zero.base.model.ZeroUser;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tobias
 */
public class UserTransactionEvent {

    public HttpServletRequest request;

    public ZeroUser user;

    public ZeroUser getUser() {
        return user;
    }

    public void setUser(ZeroUser user) {
        this.user = user;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
