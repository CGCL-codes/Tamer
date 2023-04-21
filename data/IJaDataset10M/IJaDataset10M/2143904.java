package com.bonkey.config;

/**
 * Protocol for client server commands over sockets
 * 
 * @author marcel
 */
public class BonkeyProtocol {

    /**
	 * Signal that the service is ready
	 */
    public static final int SERVICE_READY = 1;

    /**
	 * Signal that the service is auto-running 
	 */
    public static final int SERVICE_AUTO_RUNNING = 2;

    /**
	 * Signal that service needs to be sent the config directory
	 */
    public static final int SERVICE_NEEDS_CONFIG = 3;
}
