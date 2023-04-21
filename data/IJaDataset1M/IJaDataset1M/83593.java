package org.acplt.oncrpc;

/**
 * A collection of protocol constants used by the ONC/RPC package. Each
 * constant defines one of the possible transport protocols, which can be
 * used for communication between ONC/RPC clients and servers.
 *
 * @version $Revision: 1.1 $ $Date: 2003/08/13 12:03:41 $ $State: Exp $ $Locker:  $
 * @author Harald Albrecht
 */
public interface OncRpcProtocols {

    /**
     * Use the UDP protocol of the IP (Internet Protocol) suite as the
     * network communication protocol for doing remote procedure calls.
     * This is the same as the IPPROTO_UDP definition from the famous
     * BSD socket API.
     */
    public static final int ONCRPC_UDP = 17;

    /**
     * Use the TCP protocol of the IP (Internet Protocol) suite as the
     * network communication protocol for doing remote procedure calls.
     * This is the same as the IPPROTO_TCP definition from the famous
     * BSD socket API.
     */
    public static final int ONCRPC_TCP = 6;

    /**
     * Use the HTTP application protocol for tunneling ONC remote procedure
     * calls. This is definetely not similiar to any definition in the
     * famous BSD socket API.
     */
    public static final int ONCRPC_HTTP = -42;
}
