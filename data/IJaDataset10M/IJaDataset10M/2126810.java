package org.gimec.msnj;

import java.util.EventListener;

public interface ConnectionListener extends EventListener {

    /**
	 * A message has been received.
	 */
    void messageReceived(ConnectionEvent ev);

    /**
	 * A user's state has changed.
	 */
    void stateChange(ConnectionEvent ev);

    /**
	 * A user has added the current user to his/her forward list and needs to be
	 * put in either the allow list or the block list.
	 */
    void authorizeUser(ConnectionEvent ev);

    /**
	 * The Connection has been closed.
	 */
    void close(ConnectionEvent ev);
}
