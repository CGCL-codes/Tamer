package org.asoft.sapiente.protocol;

/**
 * A generic interface responsible for managing the lifecycle of protocols.
 * 
 * If performs operations as saving protocols, loading protocols from some
 * permanent storage, and creating new protocols.
 * 
 * @author Alex
 * 
 */
public interface ProtocolFactory {

    /**
	 * Loads a protocol from persistent storage.
	 * 
	 * @param id
	 *            The saved protocol id.
	 * @return The protocol. Null if none found.
	 */
    public Protocol loadProtocol(String id);

    /**
	 * Creates a new protocol for usage within the framework.
	 * 
	 * @return The newly created protocol
	 */
    public Protocol createProtocol();
}
