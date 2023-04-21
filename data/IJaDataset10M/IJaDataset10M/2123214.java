package org.gudy.azureus2.plugins.ddb;

import java.net.InetSocketAddress;

/**
 * @author parg
 *
 */
public interface DistributedDatabaseContact {

    public String getName();

    public InetSocketAddress getAddress();

    public boolean isAlive(long timeout);

    public void isAlive(long timeout, DistributedDatabaseListener listener);

    public boolean isOrHasBeenLocal();

    /**
		 * Tries to open a NAT tunnel to the contact. Should only be used if direct contact fails
		 * @return
		 */
    public boolean openTunnel();

    public void write(DistributedDatabaseTransferType type, DistributedDatabaseKey key, DistributedDatabaseValue data) throws DistributedDatabaseException;

    public DistributedDatabaseValue read(DistributedDatabaseProgressListener listener, DistributedDatabaseTransferType type, DistributedDatabaseKey key, long timeout) throws DistributedDatabaseException;
}
