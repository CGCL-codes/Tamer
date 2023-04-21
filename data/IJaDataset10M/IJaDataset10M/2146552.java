package com.tensegrity.palojava;

/**
 * The <code>PaloServer</code> interface defines general methods to connect with
 * and login into a certain palo server.
 * 
 * @author ArndHouben
 * @version $Id: PaloServer.java,v 1.5 2008/08/18 10:26:20 PhilippBouillon Exp $
 */
public interface PaloServer {

    /** 
	 * constant for server type legacy
	 * @deprecated legacy server is not supported anymore!
	 */
    public static final int TYPE_LEGACY = 1;

    /** constant for server type http */
    public static final int TYPE_HTTP = 2;

    /** constant for server type XMLA */
    public static final int TYPE_XMLA = 3;

    /** constant for server type WSS */
    public static final int TYPE_WSS = 4;

    /**
	 * Returns the {@link ServerInfo} object to gather further information about 
	 * this palo server
	 * @return
	 */
    public ServerInfo getInfo();

    /**
	 * Connect to this palo server.
	 * @return {@link DbConnection} if connection was successful
	 */
    public DbConnection connect();

    /**
     * Disconnects from the palo server
     * @throws PaloException if an communication exception occurs
     */
    public void disconnect();

    /**
     * Tests if the palo server is still reachable
     * @throws PaloException if palo server is not reachable anymore 
     */
    public void ping() throws PaloException;
}
