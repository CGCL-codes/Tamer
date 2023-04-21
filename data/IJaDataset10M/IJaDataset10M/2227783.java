package com.razie.pub.comms;

import java.net.Socket;
import java.net.URL;
import com.razie.pub.comms.LightAuth.AuthType;

/**
 * a communication channel serves two endpoints.
 * 
 * it can serve multiple streams between the two end-points. can have state and not reconnect. it
 * can reconnect automatically etc. The idea is that the client code doesn't care about the actual
 * communication channel technology and optimizations/implementation, i.e. SCTP vs TCP. $
 * 
 * this is also so virtual that it can connect two agents via a proxy. it can also accomodate multiple sessions in parallel.
 * 
 * @author razvanc99
 * 
 */
public class CommChannel {

    public ChannelEndPoint from, to;

    protected LightAuth.AuthType auth = LightAuth.AuthType.ANYBODY;

    public CommChannel(AuthType ia) {
        auth = ia;
    }

    protected LightAuth.AuthType getAuth() {
        return auth;
    }

    ;

    /**
     * this is a client requesting something
     */
    public static class SocketEndPoint extends ChannelEndPoint {

        public java.net.InetAddress address;

        private String ip;

        private String port;

        public SocketEndPoint(Socket sock) {
            super();
            this.address = sock.getInetAddress();
            this.ip = sock.getInetAddress().getHostAddress();
            this.port = String.valueOf(sock.getPort());
        }

        public String getIp() {
            return ip;
        }

        @Override
        public String getPort() {
            return port;
        }

        @Override
        public String toString() {
            return address.toString() + " IP=" + ip + " PORT=" + port;
        }
    }

    /**
     * simple URL based channel end=point. The channel is only interested in the server:port part
     */
    public static class URLChannelEndPoint extends ChannelEndPoint {

        URL url;

        String surl;

        public URLChannelEndPoint(String surl) {
            super();
            this.surl = surl;
        }

        public URLChannelEndPoint(URL url) {
            super();
            this.url = url;
        }

        @Override
        public String getIp() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getPort() {
            throw new UnsupportedOperationException();
        }
    }
}
