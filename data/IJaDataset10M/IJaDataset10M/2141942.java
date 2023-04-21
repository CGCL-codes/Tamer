package com.arsenal.rtcomm.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Date;
import com.arsenal.log.Log;
import com.arsenal.message.IMessage;

public class ArsenalHTTPClient extends ArsenalClient implements ArsenalCommClient {

    protected int type = ArsenalClient.HTTP;

    private String key = null;

    public ArsenalHTTPClient(String hostname, int port, int httpport, int type) {
        super(hostname, port, httpport, type);
    }

    public boolean connect() {
        Log.debug(this, "Attempting to connect to: " + hostname + ":" + httpport + " type:" + protocol[type]);
        boolean connectStatus = true;
        try {
            Log.debug(this, "Try to get the socket");
            socket = new Socket(hostname, httpport);
            this.key = String.valueOf((new Date()).getTime());
            Log.debug(this, "got the socket, now try to assign the streams");
            this.localAddress = String.valueOf(socket.getLocalAddress());
            this.localPort = socket.getLocalPort();
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
            Log.debug(this, "assign input/output streams");
            String request = "GET /tunnel/connect=" + key + " HTTP/1.0\r\n\r\n";
            this.out.write(request.getBytes());
            this.out.flush();
            this.isConnected = true;
            Log.debug(this, "succcessfully connected, now try to assign object input stream");
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            Log.debug(this, "Successfully got objectoutput stream");
            this.ois = new ObjectInputStream(socket.getInputStream());
            Log.debug(this, "Successfully got objectinput stream");
            Log.debug(this, "Successfully setep input/output streams to server");
        } catch (Exception e) {
            Log.error(this, e.getMessage(), e.fillInStackTrace());
            connectStatus = false;
        }
        return connectStatus;
    }

    public boolean disconnect() {
        boolean successfulDisconnect = true;
        if (isConnected) {
            try {
                Socket s;
                s = new Socket(this.hostname, this.httpport);
                OutputStream os = s.getOutputStream();
                InputStream is = s.getInputStream();
                StringTokenizer st = new StringTokenizer(getInetAddress(), "/");
                String request = "GET /tunnel/disconnect/message=" + st.nextToken() + ":" + getLocalPort() + " HTTP/1.1\r\n\r\n";
                os.write(request.getBytes());
                os.flush();
                int c;
                while ((c = is.read()) != -1) {
                    int j = c;
                }
                os.close();
                is.close();
                s.close();
                os = null;
                is = null;
                s = null;
                this.in.close();
                this.out.close();
                this.socket.close();
                this.in = null;
                this.out = null;
                this.socket = null;
                this.oos = null;
                this.ois = null;
                this.isConnected = false;
            } catch (Exception e) {
                Log.error(this, e.getMessage(), e.fillInStackTrace());
            }
        } else successfulDisconnect = false;
        return successfulDisconnect;
    }

    public void send(IMessage message) {
        message.setFromIPAddress(getInetAddress());
        message.setFromIPPort(getLocalPort());
        message.setKey(this.key);
        Log.debug(this, "send(): " + getInetAddress() + "|" + getLocalPort() + "|" + this.key);
        Log.debug(this, "" + message);
        try {
            Socket s;
            s = new Socket(this.hostname, this.httpport);
            OutputStream os = s.getOutputStream();
            InputStream is = s.getInputStream();
            String request = "GET /tunnel/input/message=xxx HTTP/1.1\r\n\r\n";
            os.write(request.getBytes());
            os.flush();
            Log.debug(this, "sent headers - opening tunnel");
            ObjectOutputStream oos = new ObjectOutputStream(os);
            Log.debug(this, "assigning object output stream");
            oos.writeObject(message);
            Log.debug(this, "writing message");
            oos.flush();
            Log.debug(this, "message sent");
            int c;
            while ((c = is.read()) != -1) {
                int j = c;
            }
            os.close();
            is.close();
            s.close();
            os = null;
            is = null;
            s = null;
        } catch (Exception e) {
            Log.error(this, e.getMessage(), e.fillInStackTrace());
        }
    }

    public boolean reconnect() {
        boolean reconnectStatus = false;
        if (connect()) {
            reconnectStatus = true;
        }
        return reconnectStatus;
    }
}
