package ca.ericslandry.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;
import ca.ericslandry.shared.domain.User;

public class GetUserResult implements Result {

    private static final long serialVersionUID = -2952156539618834426L;

    private User user;

    @SuppressWarnings("unused")
    private GetUserResult() {
    }

    public GetUserResult(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
