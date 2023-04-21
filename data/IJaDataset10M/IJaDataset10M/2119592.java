package cn.com.ethos.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Example {

    static final byte[] CRLF = { '\r', '\n' };

    static String host = "192.168.0.224";

    static int port = 27888;

    public static void main(String args[]) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 1000);
        OutputStream os = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String query = "{query:'virtus',page:1,size:10}\r\n";
        os.write(query.getBytes("utf-8"));
        os.write(CRLF);
        os.flush();
        StringBuffer sb = new StringBuffer();
        for (; ; ) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) break;
            sb.append(line);
        }
        System.out.println(sb.toString());
    }
}
