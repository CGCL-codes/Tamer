package net.community.apps.apache.maven.pom2cpsync.resources;

import net.community.apps.common.resources.BaseAnchor;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 14, 2008 11:16:13 AM
 */
public class ResourcesAnchor extends BaseAnchor {

    private ResourcesAnchor() {
        super();
    }

    private static ResourcesAnchor _instance;

    public static synchronized ResourcesAnchor getInstance() {
        if (null == _instance) _instance = new ResourcesAnchor();
        return _instance;
    }
}
