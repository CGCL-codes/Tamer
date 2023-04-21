package org.nightlabs.ssltest.server;

import java.io.Serializable;

/**
 * @author marco schulze - marco at nightlabs dot de
 */
public interface Command extends Serializable {

    Serializable execute();
}
