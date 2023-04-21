package org.opennms.jicmp.jna;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import com.sun.jna.LastErrorException;
import com.sun.jna.Native;

/**
 * UnixNativeSocketFactory
 *
 * @author brozow
 */
public class BSDV4NativeSocket extends NativeDatagramSocket {

    static {
        Native.register((String) null);
    }

    private int m_sock;

    public BSDV4NativeSocket(int family, int type, int protocol) throws Exception {
        m_sock = socket(family, type, protocol);
    }

    public native int socket(int domain, int type, int protocol) throws LastErrorException;

    public native int sendto(int socket, Buffer buffer, int buflen, int flags, bsd_sockaddr_in dest_addr, int dest_addr_len) throws LastErrorException;

    public native int recvfrom(int socket, Buffer buffer, int buflen, int flags, bsd_sockaddr_in in_addr, int[] in_addr_len) throws LastErrorException;

    public native int close(int socket) throws LastErrorException;

    private int getSock() {
        return m_sock;
    }

    @Override
    public int receive(NativeDatagramPacket p) {
        bsd_sockaddr_in in_addr = new bsd_sockaddr_in();
        int[] szRef = new int[] { in_addr.size() };
        ByteBuffer buf = p.getContent();
        int n = recvfrom(getSock(), buf, buf.capacity(), 0, in_addr, szRef);
        p.setLength(n);
        p.setAddress(in_addr.getAddress());
        p.setPort(in_addr.getPort());
        return n;
    }

    @Override
    public int send(NativeDatagramPacket p) {
        bsd_sockaddr_in destAddr = new bsd_sockaddr_in(p.getAddress(), p.getPort());
        ByteBuffer buf = p.getContent();
        return sendto(getSock(), buf, buf.remaining(), 0, destAddr, destAddr.size());
    }

    @Override
    public int close() {
        return close(getSock());
    }
}
