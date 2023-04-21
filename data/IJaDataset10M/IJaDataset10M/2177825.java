package com.dyuproject.openid;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.dyuproject.util.ClassLoaderUtil;
import com.dyuproject.util.Delim;

/**
 * A cache for providers that have generic identifiers (not tied to a single user) 
 * E.g google and yahoo's openid_identifier parameters
 * 
 * @author David Yu
 * @created Aug 18, 2009
 */
public final class IdentifierSelectUserCache implements Discovery.UserCache {

    /**
     * The default resource location (classpath).
     */
    public static final String DEFAULT_RESOURCE_LOCATION = "identifier_select.properties";

    /**
     * Load the configured mappings from the properties file located at the  
     * given {@code resourceLoc}.
     */
    public static void load(String resourceLoc, Map<String, String> mappings) {
        URL resource = ClassLoaderUtil.getResource(resourceLoc, IdentifierSelectUserCache.class);
        if (resource == null) return;
        Properties props = new Properties();
        try {
            props.load(resource.openStream());
        } catch (IOException e) {
            return;
        }
        String[] providers = Delim.COMMA.split(props.getProperty("providers"));
        for (String p : providers) {
            String openIdServer = props.getProperty(p + ".openid_server");
            if (openIdServer != null) {
                for (int i = 0; ; i++) {
                    String id = props.getProperty(p + ".identifier." + i);
                    if (id == null) break;
                    mappings.put(id, openIdServer);
                }
            }
        }
    }

    private final Map<String, String> _mappings;

    public IdentifierSelectUserCache() {
        this(new HashMap<String, String>());
        load(DEFAULT_RESOURCE_LOCATION, _mappings);
    }

    public IdentifierSelectUserCache(String resourceLoc) {
        this(new HashMap<String, String>());
        load(resourceLoc, _mappings);
    }

    public IdentifierSelectUserCache(Map<String, String> mappings) {
        _mappings = mappings;
    }

    public OpenIdUser get(String key, boolean clone) {
        String openIdServer = _mappings.get(key);
        return openIdServer == null ? null : OpenIdUser.populate(key, YadisDiscovery.IDENTIFIER_SELECT, openIdServer);
    }

    public void put(String key, OpenIdUser user) {
    }
}
