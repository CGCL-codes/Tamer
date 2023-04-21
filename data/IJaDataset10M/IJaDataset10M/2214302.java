package com.cell.net.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HttpPost implements Runnable {

    String host;

    int port;

    String path;

    String params;

    HttpPostListener listener;

    public HttpPost(String host, int port, String path, String params) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.params = params;
    }

    public HttpPost(String host, int port, String path, String params, HttpPostListener listener) {
        this(host, port, path, params);
        this.listener = listener;
        new Thread(this).run();
    }

    public String post() {
        String ret = "";
        try {
            InetAddress addr = InetAddress.getByName(host);
            Socket socket = new Socket(addr, port);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            wr.write("POST " + path + " HTTP/1.0\r\n");
            wr.write("Host: <a target=\"_blank\" href=\"" + host + "\">" + host + "</a>\n");
            wr.write("Content-Length: " + params.length() + "\r\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            wr.write("\r\n");
            wr.write(params);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            String line;
            while ((line = rd.readLine()) != null) {
                ret += line + "\n";
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.response(this, e.getMessage());
            }
            return e.getMessage();
        }
        if (listener != null) {
            listener.response(this, ret);
        }
        return ret;
    }

    public void run() {
        post();
    }

    public static interface HttpPostListener {

        public void response(HttpPost post, String msg);
    }
}
