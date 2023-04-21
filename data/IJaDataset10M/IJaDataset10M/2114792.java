package net.sf.insim4j.client.impl;

import net.sf.insim4j.client.ClientType;
import net.sf.insim4j.client.InSimClient;
import net.sf.insim4j.client.InSimClientFactory;

/**
 * Factory for clients.
 *
 * @author Jiří Sotona
 *
 */
public class DefaultClientFactory implements InSimClientFactory {

    private static InSimClientFactory INSTANCE = null;

    /**
	 * Factory method.
	 *
	 * @return singleton instance of factory
	 */
    public static final InSimClientFactory getInstance() {
        if (DefaultClientFactory.INSTANCE == null) {
            DefaultClientFactory.INSTANCE = new DefaultClientFactory();
        }
        return DefaultClientFactory.INSTANCE;
    }

    @Override
    public InSimClient createClient(final ClientType clientType) {
        InSimClient client = null;
        switch(clientType) {
            case TCP:
                client = new TCPClient();
                break;
            case UDP:
                client = new UDPClient();
                break;
            default:
                assert false : clientType + " not supported by factory";
                break;
        }
        return client;
    }
}
