package org.openejb.alt.connectors.minerva;

import org.opentools.minerva.connector.BaseConnectionManager;

/**
 * ConnectionManagerFactory implementation for the Minerva No Transaction
 * Connection Manager.  This provides non-shared access, and handles
 * only connectors that do not use transactions.  This factory will always
 * return the same ConnectionManager instance, but it configures it for the
 * factories you pass to createConnectionManager, so you still have to call
 * that once for every ManagedConnectionFactory.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.2 $
 */
public class NoTransCMFactory extends BaseCMFactory {

    protected BaseConnectionManager createConnectionManager() {
        return new MinervaNoTransCM();
    }
}
