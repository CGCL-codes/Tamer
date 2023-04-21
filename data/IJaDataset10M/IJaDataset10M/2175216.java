package gov.nasa.jpf.network.alphabet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class AlphabetClient {

    public static final String MSG = "0123456789:;<=>?@ABCDEFFGHI";

    private static byte[] getByte(String a) {
        int n = a.length();
        byte[] m = new byte[n];
        for (int i = 0; i < n; i++) {
            m[i] = (byte) a.charAt(i);
        }
        return m;
    }

    private static byte[] ip(String s) {
        StringTokenizer st = new StringTokenizer(s, ".");
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) Integer.parseInt(st.nextToken());
        }
        return b;
    }

    public static void main(String[] args) throws IOException {
        InetAddress addr;
        Socket s[];
        Thread[] p, c;
        int num_threads, num_messages;
        int msg_start = 0;
        if (args.length > 2) addr = InetAddress.getByAddress(ip(args[2])); else addr = InetAddress.getLocalHost();
        num_threads = Integer.parseInt(args[0]);
        num_messages = Integer.parseInt(args[1]);
        p = new Thread[num_threads];
        c = new Thread[num_threads];
        s = new Socket[num_threads];
        for (int i = 0; i < num_threads; i++) {
            s[i] = new Socket(addr, AlphabetServer.SERVER_PORT);
            p[i] = new Producer(s[i].getOutputStream(), getByte(MSG.substring(msg_start, msg_start + num_messages)));
            c[i] = new Consumer(s[i].getInputStream(), num_messages);
            msg_start += num_messages;
        }
        for (int i = 0; i < num_threads; i++) {
            p[i].start();
            c[i].start();
        }
    }
}
