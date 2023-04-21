package architecture.ee.security.authentication;

import architecture.ee.user.User;

public interface AuthenticationProvider {

    public abstract User getUser();

    public abstract AuthToken getAuthToken();

    public abstract boolean isSystemAdmin();
}
