package com.dyuproject.openid;

import junit.framework.TestCase;
import com.dyuproject.util.http.SimpleHttpConnector;

/**
 * @author David Yu
 * @created Sep 24, 2008
 */
public class DefaultDiscoveryTest extends TestCase {

    public void testDiscovery() throws Exception {
        OpenIdContext context = new OpenIdContext(new DefaultDiscovery(), new DiffieHellmanAssociation(), SimpleHttpConnector.getDefault());
        Identifier identifier = Identifier.getIdentifier("http://techmusicbox.blogspot.com", null, context);
        OpenIdUser user = context.getDiscovery().discover(identifier, context);
        assertTrue(user != null && user.getOpenIdServer() != null);
        System.err.println(user.getOpenIdServer());
        System.err.println(user.getClaimedId());
    }
}
