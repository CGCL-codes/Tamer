package balmysundaycandy.core.environment;

import com.google.apphosting.api.ApiProxy.Environment;

public class DevlopmentEnvironment implements Environment {

    public String getAppId() {
        return "myApplicationId";
    }

    public String getVersionId() {
        return "unittest";
    }

    public String getRequestNamespace() {
        return "";
    }

    public String getAuthDomain() {
        return "gmail.com";
    }

    public boolean isLoggedIn() {
        return true;
    }

    public String getEmail() {
        return "unittest@gmail.com";
    }

    public boolean isAdmin() {
        return true;
    }

    public java.util.Map<String, Object> getAttributes() {
        java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
        map.put("com.google.appengine.server_url_key", "dummy-for-taskqueue");
        return map;
    }
}
