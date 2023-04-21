package com.arsenal.rtcomm.server.http;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.SocketException;
import java.io.*;
import java.util.StringTokenizer;
import com.arsenal.log.Log;
import com.arsenal.rtcomm.server.ConnectionManager;

public class FileOut implements HttpProcessor {

    private InputStream in = null;

    private ConnectionManager connectionManager = null;

    private String relativeURL = null;

    private String filename = null;

    private StringTokenizer urlCheck = null;

    private String revisedMessage = null;

    private String session = null;

    public FileOut(ConnectionManager connectionManager, HttpInputStream in, String relativeURL) throws IOException {
        this.connectionManager = connectionManager;
        this.in = in;
        this.relativeURL = relativeURL;
        Log.debug(this, "Got message from FILE OUT: " + relativeURL);
        StringTokenizer st = new StringTokenizer(getMessageFromURL(relativeURL), "*");
        String newFilename = "";
        while (st.hasMoreTokens()) {
            newFilename = newFilename + st.nextToken();
            if (st.hasMoreTokens()) newFilename = newFilename + " ";
        }
        Log.debug(this, "recovered filename is: " + newFilename);
        this.filename = newFilename;
    }

    public boolean processRequest(HttpOutputStream out) throws IOException {
        File f = new File(this.connectionManager.getTempDir() + File.separator + this.filename);
        final int BUFFER_SIZE = 7900;
        try {
            OutputStream os = out.getSocket().getOutputStream();
            FileInputStream fin = null;
            DataOutputStream dout = null;
            fin = new FileInputStream(f);
            dout = new DataOutputStream(os);
            long fileSize = f.length();
            long length = 0;
            int bytes = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            dout.writeLong(fileSize);
            dout.flush();
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            for (length = fileSize; length > 0; ) {
                bytes = (int) (length > BUFFER_SIZE ? BUFFER_SIZE : length);
                bytes = fin.read(buffer, 0, bytes);
                if (bytes < 0) break;
                length -= bytes;
                dout.write(buffer, 0, bytes);
                dout.flush();
            }
            fin.close();
            fin = null;
            os.close();
            out.getSocket().close();
            Log.debug(this, "File: " + this.filename + " has been successfully sent to a remote client");
        } catch (Exception e) {
            Log.debug(this, e.getMessage(), e);
        }
        return true;
    }

    public String getMessageFromURL(String relURL) {
        int a = relURL.indexOf("=");
        if (a != -1) return relURL.substring(a + 1, relURL.length()); else return "No message";
    }
}
