package com.appspot.ajnweb.tools;

import java.util.HashMap;
import java.util.Map;
import com.google.apphosting.api.ApiProxy;

/**
 * @author shin1ogawa
 */
public class LocalEnvironment implements ApiProxy.Environment {

    LocalEnvironment() {
    }

    public String getAppId() {
        return "ajn-web";
    }

    public String getVersionId() {
        return "makesynccall";
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
        return "servicetest@gmail.com";
    }

    public boolean isAdmin() {
        return true;
    }

    public Map<String, Object> getAttributes() {
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }
}
