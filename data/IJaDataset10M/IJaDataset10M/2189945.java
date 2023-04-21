package org.parosproxy.paros.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

public class HttpUtil {

    public static String encodeURI(String uri) throws URISyntaxException {
        String tmp = null;
        tmp = uri.replaceAll(" ", "%20");
        tmp = tmp.replaceAll("<", "%3C");
        tmp = tmp.replaceAll(">", "%3E");
        tmp = tmp.replaceAll("'", "%27");
        tmp = tmp.replaceAll("\\x28", "%28");
        tmp = tmp.replaceAll("\\x29", "%29");
        tmp = tmp.replaceAll("\\x22", "%22");
        return tmp;
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public static void closeServerSocket(ServerSocket socket) {
        if (socket == null) return;
        try {
            socket.close();
        } catch (Exception e) {
        }
    }

    public static void closeSocket(Socket socket) {
        if (socket == null) return;
        try {
            socket.close();
        } catch (Exception e) {
        }
    }

    public static void closeInputStream(InputStream in) {
        if (in == null) return;
        try {
            in.close();
        } catch (Exception e) {
        }
    }

    public static void closeOutputStream(OutputStream out) {
        if (out == null) return;
        try {
            out.close();
        } catch (Exception e) {
        }
    }
}
