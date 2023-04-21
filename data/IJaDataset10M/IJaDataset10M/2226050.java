package maze.commons.examples.auction.server.model.impl;

import java.util.HashSet;
import java.util.Set;
import maze.commons.examples.auction.common.basics.AuctionClient;
import maze.commons.shared.concurrent.ReadWriteLockBase;
import maze.commons.examples.auction.server.model.ClientRegistrar;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public class ClientRegistrarImpl extends ReadWriteLockBase implements ClientRegistrar {

    protected final Set<String> usernames = new HashSet<String>();

    protected final Set<AuctionClient> users = new HashSet<AuctionClient>();

    protected ClientRegistrarImpl() {
    }

    private static final ClientRegistrar INSTANCE = new ClientRegistrarImpl();

    public static ClientRegistrar getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean checkUsername(final String username) {
        assert username != null;
        readLock.lock();
        try {
            return !usernames.contains(username);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean regUser(final AuctionClient auctionClient) {
        assert auctionClient != null;
        writeLock.lock();
        try {
            if (usernames.contains(auctionClient.getUsername())) {
                return false;
            }
            usernames.add(auctionClient.getUsername());
            users.add(auctionClient);
            return true;
        } finally {
            writeLock.unlock();
        }
    }
}
