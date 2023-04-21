package balmysundaycandy.scalatool.client;

import com.google.apphosting.api.ApiProxy;

public abstract class DefaultEnvironment implements ApiProxy.Environment {

    @Override
    public String getAuthDomain() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getRequestNamespace() {
        return "";
    }

    @Override
    public String getVersionId() {
        return "1";
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
