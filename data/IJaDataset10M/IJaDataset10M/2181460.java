package net.jsrb.rtl;

/**
 * C runtime library for socket.h
 */
public class socket {

    /**
	 * wrapper class for struct socketadd_in
	 */
    public static class sockaddr {

        public int sin_family;

        public int sin_port;

        public String sin_addr;

        /**
		 * This field is only used for accept() method,
		 * it is ignored by bind() method.
		 */
        public int sockfd;

        public String toString() {
            return "sockaddr[sin_port=" + sin_port + ",sin_addr=" + sin_addr + ",sockfd=" + sockfd + "]";
        }
    }

    public static int socketConnect(String host, int port) throws RtlException {
        int sockfd = socket.socket(RtlConstants.PF_INET, RtlConstants.SOCK_STREAM, 0);
        socket.sockaddr addr = new socket.sockaddr();
        addr.sin_family = RtlConstants.AF_INET;
        addr.sin_port = port;
        addr.sin_addr = host;
        try {
            socket.connect(sockfd, addr);
        } catch (RtlException e) {
            try {
                unistd.close(sockfd);
            } catch (Exception e2) {
            }
            throw e;
        }
        return sockfd;
    }

    public static native int socket(int domain, int type, int protocol) throws RtlException;

    public static native void bind(int sockfd, sockaddr addr) throws RtlException;

    public static native void listen(int sockfd, int backlog) throws RtlException;

    public static native sockaddr accept(int sockfd) throws RtlException;

    public static native void connect(int sockfd, sockaddr addr) throws RtlException;

    public static native int send(int sockfd, long bufaddr, int bufsize, int flags) throws RtlException;

    /**
     * Send packet
     * 
     * @param sockfd socket file descriptor
     * @param buf native buffer,from position to limit.
     * @param flags send flags
     * 
     * @return n bytes sent
     * 
     * @throws RtlException
     */
    public static int send(int sockfd, nbuf buf, int flags) throws RtlException {
        long bufaddr = 0;
        int bufsize = 0;
        bufaddr = buf.addr() + buf.position();
        bufsize = buf.limit() - buf.position();
        return send(sockfd, bufaddr, bufsize, flags);
    }

    public static native int recv(int sockfd, long bufaddr, int bufsize, int flags) throws RtlException;

    /**
     * Receieve data from socket to native buffer
     * 
     * @param sockfd socket file descriptor
     * @param buf native buffer, data from position to limit.
     * @param flags flags
     * 
     * @return receieved data , in bytes
     * 
     * @throws RtlException
     */
    public static int recv(int sockfd, nbuf buf, int flags) throws RtlException {
        long bufaddr = 0;
        int bufsize = 0;
        bufaddr = buf.addr() + buf.position();
        bufsize = buf.limit() - buf.position();
        return recv(sockfd, bufaddr, bufsize, flags);
    }

    public static native void shutdown(int sockfd, int how) throws RtlException;

    public static native void setsockopt(int sockfd, int level, int optname, int joptval) throws RtlException;

    public static native int getsockopt(int sockfd, int level, int optname) throws RtlException;

    private static native void ninit();

    static {
        ninit();
    }
}
