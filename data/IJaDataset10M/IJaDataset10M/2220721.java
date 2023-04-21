package org.jiplib.ip;

/**
 * This exception may be raised while converting an
 * <code>InetAddressRange</code> to a reverse string representation. If the
 * prefix is not a multiple of 8 for IPv4 networks and not a multiple of 4 for
 * IPv6 networks, conversion is not possible.
 * 
 * @author Can Bican
 * 
 */
public class InvalidNetworkForReverseException extends Exception {

    private static final long serialVersionUID = 4935063009179996011L;
}
