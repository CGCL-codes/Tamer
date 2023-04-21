package org.red5.webapps.admin.client;

import org.red5.server.Client;
import org.red5.server.ClientRegistry;

/**
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Martijn van Beek (martijn.vanbeek@gmail.com)
 */
public class AuthClient extends Client {

    public AuthClient() {
        super();
    }

    public AuthClient(String id, ClientRegistry registry) {
        super(id, registry);
    }
}
