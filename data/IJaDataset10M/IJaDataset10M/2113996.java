package wfg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Vector;

public class Proxy extends Thread {

    private Socket client;

    private String[] action;

    private Vector<String[]> clientHeader;

    private boolean keepConnected = true;

    private InputStream clientRead;

    private InputStream serverRead;

    private BufferedOutputStream clientWrite;

    private BufferedOutputStream serverWrite;

    private PrintStream clientWriteText;

    private PrintStream serverWriteText;

    private String remoteHost;

    private String fullURL;

    private enum TransmissionType {

        NOT_DEFINED, CONTENT_LENGTH, CHUNKED, CLOSE_CONNECTION
    }

    ;

    Proxy(Socket _client) throws IOException {
        client = _client;
        clientRead = client.getInputStream();
        clientWrite = new BufferedOutputStream(client.getOutputStream());
        clientWriteText = new PrintStream(client.getOutputStream());
    }

    public void run() {
        while (keepConnected) {
            try {
                clientHeader = new Vector<String[]>();
                action = HeaderManipulator.GetHeader(clientHeader, clientRead, true);
            } catch (IOException e2) {
                keepConnected = false;
                continue;
            }
            System.out.println(action[0] + " " + action[1] + " " + action[2]);
            fullURL = action[1];
            int pos = action[1].indexOf('/');
            pos = action[1].indexOf('/', pos + 2);
            action[1] = action[1].substring(pos);
            String host = HeaderManipulator.GetValue(clientHeader, "Host");
            if (host.compareToIgnoreCase("wfg") == 0) {
                new SpecialPages(action, clientWrite, clientWriteText);
            } else {
                TransmitTheRemoteSite();
            }
        }
        try {
            client.close();
        } catch (IOException e) {
            return;
        }
    }

    private void TransmitTheRemoteSite() {
        String hostname;
        int port;
        Vector<String[]> serverHeader = new Vector<String[]>();
        String[] result;
        TransmissionType tt = TransmissionType.NOT_DEFINED;
        String token;
        int len;
        int contentLength;
        String host = HeaderManipulator.GetValue(clientHeader, "Host");
        if ((serverRead == null) || (host.compareTo(remoteHost) != 0)) {
            Socket web = new Socket();
            remoteHost = host;
            if (host.contains(":")) {
                hostname = host.substring(0, host.indexOf(':'));
                port = Integer.valueOf(host.substring(host.indexOf(':') + 1));
            } else {
                hostname = host;
                port = 80;
            }
            try {
                web.connect(new InetSocketAddress(hostname, port));
                serverRead = new BufferedInputStream(web.getInputStream());
                serverWrite = new BufferedOutputStream(web.getOutputStream());
                serverWriteText = new PrintStream(web.getOutputStream());
            } catch (IOException e) {
                keepConnected = false;
                return;
            }
        }
        FileOutputStream fos;
        try {
            File f = File.createTempFile("proxy", "");
            fos = new FileOutputStream(f);
            DB.AddRecord(f, fullURL);
            f.deleteOnExit();
        } catch (IOException e1) {
            keepConnected = false;
            return;
        }
        try {
            serverWriteText.println(action[0] + " " + action[1] + " " + action[2]);
            serverWriteText.println(HeaderManipulator.ListToString(clientHeader));
            if (action[0].compareTo("POST") == 0) {
                token = HeaderManipulator.GetValue(clientHeader, "Content-Length");
                if (token != null) {
                    contentLength = Integer.valueOf(token);
                    while (contentLength > 0) {
                        len = SendBridgedData(contentLength, null, clientRead, serverWrite);
                        contentLength -= len;
                    }
                    while (clientRead.available() > 0) {
                        len = SendBridgedData(clientRead.available(), null, clientRead, serverWrite);
                    }
                    serverWrite.flush();
                }
            }
            result = HeaderManipulator.GetHeader(serverHeader, serverRead, false);
            token = HeaderManipulator.GetValue(serverHeader, "Transfer-Encoding");
            if ((token != null) && (token.compareTo("chunked") == 0)) tt = TransmissionType.CHUNKED;
            token = HeaderManipulator.GetValue(serverHeader, "Connection");
            if ((token != null) && (token.compareTo("close") == 0)) tt = TransmissionType.CLOSE_CONNECTION;
            token = HeaderManipulator.GetValue(serverHeader, "Content-Length");
            if (token != null) tt = TransmissionType.CONTENT_LENGTH;
            clientWriteText.println(result[0]);
            clientWriteText.println(HeaderManipulator.ListToString(serverHeader));
            switch(tt) {
                case NOT_DEFINED:
                    System.out.println("WARNING: The transmission type is not defined. Will fallback to close connection");
                case CLOSE_CONNECTION:
                    keepConnected = false;
                    do {
                        len = SendBridgedData(1000, fos, serverRead, clientWrite);
                    } while (len != -1);
                    break;
                case CONTENT_LENGTH:
                    token = HeaderManipulator.GetValue(serverHeader, "Content-Length");
                    contentLength = Integer.valueOf(token);
                    while (contentLength > 0) {
                        len = SendBridgedData(contentLength, fos, serverRead, clientWrite);
                        contentLength -= len;
                    }
                    break;
                case CHUNKED:
                    while (true) {
                        Integer numberEmptyLine = 0;
                        String line = HeaderManipulator.GetNonNullLine(serverRead, numberEmptyLine);
                        int chunkLength = Integer.valueOf(hex2decimal(line));
                        for (int i = 0; i < numberEmptyLine; i++) clientWriteText.println();
                        clientWriteText.println(line);
                        if (chunkLength == 0) break;
                        while (chunkLength > 0) {
                            len = SendBridgedData(chunkLength, fos, serverRead, clientWrite);
                            chunkLength -= len;
                        }
                    }
                    break;
            }
            while (serverRead.available() > 0) {
                len = SendBridgedData(serverRead.available(), fos, serverRead, clientWrite);
            }
        } catch (IOException e) {
            keepConnected = false;
            return;
        }
        try {
            fos.close();
        } catch (IOException e) {
            return;
        }
    }

    private int SendBridgedData(int _maximumData, FileOutputStream _fos, InputStream _src, BufferedOutputStream _dst) throws IOException {
        int len;
        byte[] b = new byte[100];
        if (_maximumData < b.length) {
            len = _src.read(b, 0, _maximumData);
        } else {
            len = _src.read(b);
        }
        if (len > 0) {
            _dst.write(b, 0, len);
            _dst.flush();
            if (_fos != null) _fos.write(b, 0, len);
        }
        return len;
    }

    public static int hex2decimal(String _text) {
        String digits = "0123456789ABCDEF";
        _text = _text.toUpperCase();
        int val = 0;
        for (int i = 0; i < _text.length(); i++) {
            char c = _text.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }
}
