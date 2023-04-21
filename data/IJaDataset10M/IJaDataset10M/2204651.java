package net.sourceforge.jsocks;

import java.net.*;
import java.io.*;

/**
  Datagram socket to interract through the firewall.<BR>
  Can be used same way as the normal DatagramSocket. One should
  be carefull though with the datagram sizes used, as additional data
  is present in both incomming and outgoing datagrams.
   <p>
   SOCKS5 protocol allows to send host address as either:
   <ul>
    <li> IPV4, normal 4 byte address. (10 bytes header size)
    <li> IPV6, version 6 ip address (not supported by Java as for now).
         22 bytes header size.
    <li> Host name,(7+length of the host name bytes header size).
   </ul>
  As with other Socks equivalents, direct addresses are handled
  transparently, that is data will be send directly when required 
  by the proxy settings.
  <p>
  <b>NOTE:</b><br>
  Unlike other SOCKS Sockets, it <b>does not</b> support proxy chaining,
  and will throw an exception if proxy has a chain proxy attached. The
  reason for that is not my laziness, but rather the restrictions of
  the SOCKSv5 protocol. Basicaly SOCKSv5 proxy server, needs to know from
  which host:port datagrams will be send for association, and returns address
  to which datagrams should be send by the client, but it does not
  inform client from which host:port it is going to send datagrams, in fact
  there is even no guarantee they will be send at all and from the same address
  each time.
 
 */
public class Socks5DatagramSocket extends DatagramSocket {

    InetAddress relayIP;

    int relayPort;

    Socks5Proxy proxy;

    private boolean server_mode = false;

    UDPEncapsulation encapsulation;

    /**
      Construct Datagram socket for communication over SOCKS5 proxy
      server. This constructor uses default proxy, the one set with
      Proxy.setDefaultProxy() method. If default proxy is not set or 
      it is set to version4 proxy, which does not support datagram
      forwarding, throws SocksException.

    */
    public Socks5DatagramSocket() throws SocksException, IOException {
        this(Proxy.defaultProxy, 0, null);
    }

    /**
      Construct Datagram socket for communication over SOCKS5 proxy
      server. And binds it to the specified local port.
      This constructor uses default proxy, the one set with
      Proxy.setDefaultProxy() method. If default proxy is not set or 
      it is set to version4 proxy, which does not support datagram
      forwarding, throws SocksException.
    */
    public Socks5DatagramSocket(int port) throws SocksException, IOException {
        this(Proxy.defaultProxy, port, null);
    }

    /**
      Construct Datagram socket for communication over SOCKS5 proxy
      server. And binds it to the specified local port and address.
      This constructor uses default proxy, the one set with
      Proxy.setDefaultProxy() method. If default proxy is not set or 
      it is set to version4 proxy, which does not support datagram
      forwarding, throws SocksException.
    */
    public Socks5DatagramSocket(int port, InetAddress ip) throws SocksException, IOException {
        this(Proxy.defaultProxy, port, ip);
    }

    /**
     Constructs datagram socket for communication over specified proxy.
     And binds it to the given local address and port. Address of null 
     and port of 0, signify any availabale port/address.
     Might throw SocksException, if:
     <ol>
      <li> Given version of proxy does not support UDP_ASSOCIATE.
      <li> Proxy can't be reached.
      <li> Authorization fails.
      <li> Proxy does not want to perform udp forwarding, for any reason.
     </ol>
     Might throw IOException if binding dtagram socket to given address/port
     fails.
     See java.net.DatagramSocket for more details.
    */
    public Socks5DatagramSocket(Proxy p, int port, InetAddress ip) throws SocksException, IOException {
        super(port, ip);
        if (p == null) throw new SocksException(Proxy.SOCKS_NO_PROXY);
        if (!(p instanceof Socks5Proxy)) throw new SocksException(-1, "Datagram Socket needs Proxy version 5");
        if (p.chainProxy != null) throw new SocksException(Proxy.SOCKS_JUST_ERROR, "Datagram Sockets do not support proxy chaining.");
        proxy = (Socks5Proxy) p.copy();
        ProxyMessage msg = proxy.udpAssociate(super.getLocalAddress(), super.getLocalPort());
        relayIP = msg.ip;
        if (relayIP.getHostAddress().equals("0.0.0.0")) relayIP = proxy.proxyIP;
        relayPort = msg.port;
        encapsulation = proxy.udp_encapsulation;
    }

    /**
     Used by UDPRelayServer.
    */
    Socks5DatagramSocket(boolean server_mode, UDPEncapsulation encapsulation, InetAddress relayIP, int relayPort) throws IOException {
        super();
        this.server_mode = server_mode;
        this.relayIP = relayIP;
        this.relayPort = relayPort;
        this.encapsulation = encapsulation;
        this.proxy = null;
    }

    /**
     Sends the Datagram either through the proxy or directly depending
     on current proxy settings and destination address. <BR>

     <B> NOTE: </B> DatagramPacket size should be at least 10 bytes less
                    than the systems limit.

     <P>
     See documentation on java.net.DatagramSocket
     for full details on how to use this method. 
     @param dp Datagram to send.
     @throws IOException If error happens with I/O.
    */
    public void send(DatagramPacket dp) throws IOException {
        if (!server_mode) {
            super.send(dp);
            return;
        }
        byte[] head = formHeader(dp.getAddress(), dp.getPort());
        byte[] buf = new byte[head.length + dp.getLength()];
        byte[] data = dp.getData();
        System.arraycopy(head, 0, buf, 0, head.length);
        System.arraycopy(data, 0, buf, head.length, dp.getLength());
        if (encapsulation != null) buf = encapsulation.udpEncapsulate(buf, true);
        super.send(new DatagramPacket(buf, buf.length, relayIP, relayPort));
    }

    /**
     This method allows to send datagram packets with address type DOMAINNAME.
     SOCKS5 allows to specify host as names rather than ip addresses.Using
     this method one can send udp datagrams through the proxy, without having
     to know the ip address of the destination host.
     <p> 
     If proxy specified for that socket has an option resolveAddrLocally set
     to true host will be resolved, and the datagram will be send with address
     type IPV4, if resolve fails, UnknownHostException is thrown.
     @param dp Datagram to send, it should contain valid port and data
     @param host Host name to which datagram should be send.
     @throws IOException If error happens with I/O, or the host can't be 
     resolved when proxy settings say that hosts should be resolved locally.
     @see Socks5Proxy#resolveAddrLocally(boolean)
    */
    public void send(DatagramPacket dp, String host) throws IOException {
        dp.setAddress(InetAddress.getByName(host));
        super.send(dp);
    }

    /**
    * Receives udp packet. If packet have arrived from the proxy relay server,
    * it is processed and address and port of the packet are set to the
    * address and port of sending host.<BR>
    * If the packet arrived from anywhere else it is not changed.<br>
    * <B> NOTE: </B> DatagramPacket size should be at least 10 bytes bigger
    * than the largest packet you expect (this is for IPV4 addresses). 
    * For hostnames and IPV6 it is even more.
      @param dp Datagram in which all relevent information will be copied.
    */
    public void receive(DatagramPacket dp) throws IOException {
        super.receive(dp);
        if (server_mode) {
            int init_length = dp.getLength();
            int initTimeout = getSoTimeout();
            long startTime = System.currentTimeMillis();
            while (!relayIP.equals(dp.getAddress()) || relayPort != dp.getPort()) {
                dp.setLength(init_length);
                if (initTimeout != 0) {
                    int newTimeout = initTimeout - (int) (System.currentTimeMillis() - startTime);
                    if (newTimeout <= 0) throw new InterruptedIOException("In Socks5DatagramSocket->receive()");
                    setSoTimeout(newTimeout);
                }
                super.receive(dp);
            }
            if (initTimeout != 0) setSoTimeout(initTimeout);
        } else if (!relayIP.equals(dp.getAddress()) || relayPort != dp.getPort()) return;
        byte[] data;
        data = dp.getData();
        if (encapsulation != null) data = encapsulation.udpEncapsulate(data, false);
        int offset = 0;
        ByteArrayInputStream bIn = new ByteArrayInputStream(data, offset, dp.getLength());
        ProxyMessage msg = new Socks5Message(bIn);
        dp.setPort(msg.port);
        dp.setAddress(msg.getInetAddress());
        int data_length = bIn.available();
        System.arraycopy(data, offset + dp.getLength() - data_length, data, offset, data_length);
        dp.setLength(data_length);
    }

    /**
    * Returns port assigned by the proxy, to which datagrams are relayed.
    * It is not the same port to which other party should send datagrams.
      @return Port assigned by socks server to which datagrams are send
      for association.
    */
    public int getLocalPort() {
        if (server_mode) return super.getLocalPort();
        return relayPort;
    }

    /**
    * Address assigned by the proxy, to which datagrams are send for relay.
    * It is not necesseraly the same address, to which other party should send
    * datagrams.
      @return Address to which datagrams are send for association.
    */
    public InetAddress getLocalAddress() {
        if (server_mode) return super.getLocalAddress();
        return relayIP;
    }

    /**
    * Closes datagram socket, and proxy connection.
    */
    public void close() {
        if (!server_mode) proxy.endSession();
        super.close();
    }

    /**
     This method checks wether proxy still runs udp forwarding service
     for this socket.
     <p>
     This methods checks wether the primary connection to proxy server
     is active. If it is, chances are that proxy continues to forward
     datagrams being send from this socket. If it was closed, most likely
     datagrams are no longer being forwarded by the server.
     <p>
     Proxy might decide to stop forwarding datagrams, in which case it
     should close primary connection. This method allows to check, wether
     this have been done.
     <p>
     You can specify timeout for which we should be checking EOF condition
     on the primary connection. Timeout is in milliseconds. Specifying 0 as
     timeout implies infinity, in which case method will block, until 
     connection to proxy is closed or an error happens, and then return false.
     <p>
     One possible scenario is to call isProxyactive(0) in separate thread,
     and once it returned notify other threads about this event.

     @param timeout For how long this method should block, before returning.
     @return true if connection to proxy is active, false if eof or error
             condition have been encountered on the connection.
   */
    public boolean isProxyAlive(int timeout) {
        if (server_mode) return false;
        if (proxy != null) {
            try {
                proxy.proxySocket.setSoTimeout(timeout);
                int eof = proxy.in.read();
                if (eof < 0) return false; else return true;
            } catch (InterruptedIOException iioe) {
                return true;
            } catch (IOException ioe) {
                return false;
            }
        }
        return false;
    }

    private byte[] formHeader(InetAddress ip, int port) {
        Socks5Message request = new Socks5Message(0, ip, port);
        request.data[0] = 0;
        return request.data;
    }
}
