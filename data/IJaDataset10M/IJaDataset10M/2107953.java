package gumbo.net.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NetUtils {

    private NetUtils() {
    }

    private static double MSEC_TO_SEC = 1.0 / 1000.0;

    private static double NSEC_TO_SEC = 1.0 / 1000000000.0;

    public static String fromMsec(long msec) {
        return String.format("%7.3f", msec * MSEC_TO_SEC);
    }

    public static String fromNsec(long nsec) {
        return String.format("%10.4f", nsec * NSEC_TO_SEC);
    }

    /**
	 * Utility for converting a host name and port number into a hostport
	 * string. Any whitespace will be trimmed from the host name.
	 * @param host Host name. None if null.
	 * @param port Port number. None if negative.
	 * @return New String of the form "[host][:port]". Possibly empty but never
	 * null.
	 */
    public static String toHostport(String host, int port) {
        String hostport = "";
        if (host != null) {
            hostport += host.trim();
        }
        if (port > 0) {
            hostport += ":" + port;
        }
        return hostport;
    }

    /**
	 * Utility for converting a host name and port number into a hostport
	 * string. Any whitespace will be trimmed from the host name and port
	 * string.
	 * @param host Host name. None if null.
	 * @param port Port number, possibly of the form "port" or ":port". None if
	 * null, empty, ill-formed, or negative value.
	 * @return New String of the form "[host][:port]". Possibly empty but never
	 * null.
	 */
    public static String toHostport(String host, String port) {
        int portNum = -1;
        if (port != null) {
            port = port.trim();
            int index = port.indexOf(':');
            if (index >= 0) {
                port = port.substring(index + 1);
            }
            try {
                portNum = Integer.parseInt(port);
            } catch (Exception ex) {
                portNum = -1;
            }
        }
        return toHostport(host, portNum);
    }

    /**
	 * Utility for translating a hostport string into the readable form "(none)"
	 * if it is null or empty.
	 * @param hostport Hostport string of the form "[host][:port]". Empty if
	 * null.
	 * @return New String with the original hostport or "(none)" if it was
	 * empty. Never null.
	 */
    public static String toHostport(String hostport) {
        if (hostport == null || hostport.length() == 0) {
            return "(none)";
        }
        return hostport;
    }

    /**
	 * Utility for obtaining the server hostport from a server socket.
	 * @param socket Server socket. Ignored if null.
	 * @return New String of the form "host:port". Empty if socket is null.
	 */
    public static String toServerHostport(ServerSocket sock) {
        if (sock == null) return new String("");
        String host = "";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
        }
        return new String(host + ":" + sock.getLocalPort());
    }

    /**
	 * Utility for obtaining the local hostport from a socket.
	 * @param socket Socket. Ignored if null.
	 * @return New String of the form "host:port". Empty if socket is null.
	 */
    public static String toLocalHostport(Socket sock) {
        if (sock == null) return new String("");
        return new String(sock.getLocalAddress().getHostAddress() + ":" + sock.getLocalPort());
    }

    /**
	 * Utility for obtaining the remote hostport from a socket.
	 * @param socket Socket. Ignored if null.
	 * @return New String of the form "host:port". Empty if socket is null.
	 */
    public static String toRemoteHostport(Socket sock) {
        if (sock == null) return new String("");
        return new String(sock.getInetAddress().getHostAddress() + ":" + sock.getPort());
    }

    /**
	 * Utility for getting the host name from a hostport string. Any whitespace
	 * will be trimmed from the hostport.
	 * @param hostport Hostport string of the form "[host][:port]". None if
	 * null.
	 * @return New String with the host name. Empty if hostport is null, empty,
	 * or ill-formed.
	 */
    public static String toHost(String hostport) {
        if (hostport == null) return new String("");
        hostport = hostport.trim();
        String host = "";
        int index = hostport.indexOf(':');
        if (index < 0) {
            host = hostport;
        } else {
            host = hostport.substring(0, index);
        }
        return host;
    }

    /**
	 * Utility for getting the port number from a hostport string. Any
	 * whitespace will be trimmed from the hostport.
	 * @param hostport Hostport string of the form "[host][:port]". None if
	 * null.
	 * @return Port number. -1 if hostport is null, empty, or ill-formed (i.e.
	 * contains no port number).
	 */
    public static int toPort(String hostport) {
        if (hostport == null) return -1;
        hostport = hostport.trim();
        int port = -1;
        int index = hostport.indexOf(':');
        if (index >= 0) {
            try {
                port = Integer.parseInt(hostport.substring(index + 1));
            } catch (Exception ex) {
                port = -1;
            }
        }
        return port;
    }

    /**
	 * Utility for generating a connection report of the form
	 * "<local_name> <local_hostport> to <remote_name> <remote_hostport>".
	 * @param local Local name (e.g. "client"). If null, defaults to "local".
	 * @param remote Remote name (e.g. "server"). If null, defaults to "remote".
	 * @param socket Socket. Null if none.
	 * @return New String with the report.
	 */
    public static String connectionReport(String local, String remote, Socket sock) {
        if (local == null) local = "local";
        if (remote == null) remote = "remote";
        return new String(local + " " + toHostport(toLocalHostport(sock)) + " to " + remote + " " + toHostport(toRemoteHostport(sock)));
    }

    /**
	 * Wraps a buffered reader around a socket.
	 * @param socket Open socket. Never null.
	 * @return Socket reader. never null.
	 */
    public static Reader socketReader(Socket socket) throws IOException {
        if (socket == null) throw new IllegalArgumentException("Socket is null.");
        Reader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader;
    }

    /**
	 * Wraps a buffered writer around a socket.
	 * @param socket Open socket. Never null.
	 * @return Socket writer. Never null.
	 */
    public static Writer socketWriter(Socket socket) throws IOException {
        if (socket == null) throw new IllegalArgumentException("Socket is null.");
        Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        return writer;
    }
}
