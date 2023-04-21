package org.rip.ftp.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.net.ssl.SSLSocket;
import org.apache.commons.io.IOUtils;
import org.rip.ftp.DataConnection;
import org.rip.ftp.FtpResponse;
import org.rip.ftp.ProtectionLevel;
import org.rip.ssl.SSLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientDataConnection extends DataConnection {

    private static Logger LOG = LoggerFactory.getLogger(ClientDataConnection.class);

    public Socket socket;

    public File tmpFile;

    byte[] data;

    private FtpSession ftpClient;

    public ClientDataConnection(FtpSession client) {
        ftpClient = client;
    }

    @Override
    public boolean open(String server, String port) {
        LOG.trace("cdc.open()");
        try {
            socket = new Socket(server, Integer.parseInt(port));
            if (ftpClient.getProtectionLevel() == ProtectionLevel.Private) {
                SSLSocket sslSocket = SSLUtils.createClientSocket(ftpClient.getSslContext(), socket, server, Integer.parseInt(port));
                socket = sslSocket;
            }
            return true;
        } catch (Exception e) {
            LOG.error("open", e);
        }
        return false;
    }

    public void close() {
        LOG.trace("cdc.close()");
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                LOG.error("Error closing socket", e);
            }
        }
        if (tmpFile != null) {
            tmpFile = null;
        }
    }

    public void complete() {
    }

    public void read() {
        SocketReader sr = new SocketReader(this);
        Thread t = new Thread(sr);
        t.start();
    }

    private class SocketReader implements Runnable {

        DataConnection conn;

        public SocketReader(DataConnection c) {
            conn = c;
        }

        @Override
        public void run() {
            long timeout = 10 * 1000L;
            long start = (new Date()).getTime();
            try {
                InputStream is = socket.getInputStream();
                boolean available = false;
                while (!available && !socket.isClosed()) {
                    try {
                        if (is.available() != 0) {
                            available = true;
                        } else {
                            Thread.sleep(100);
                        }
                    } catch (Exception e) {
                        LOG.error("Error checking socket", e);
                    }
                    long curr = (new Date()).getTime();
                    if ((curr - start) >= timeout) {
                        break;
                    }
                }
                if (socket.isClosed()) {
                } else {
                    tmpFile = File.createTempFile("ftp", "dat", new File("./tmp"));
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
                    IOUtils.copy(is, bos);
                    bos.flush();
                    bos.close();
                }
                String msg = FtpResponse.ReadComplete.asString() + ClientCommand.SP + "Read Complete" + ClientCommand.CRLF;
                List<String> list = new ArrayList<String>();
                list.add(msg);
                ClientResponse response = new ClientResponse(list);
                ftpClient.notifyListeners(response);
            } catch (Exception e) {
                LOG.error("Error reading server response", e);
            }
        }
    }

    private class ByteArraySocketReader implements Runnable {

        DataConnection conn;

        public ByteArraySocketReader(DataConnection c) {
            conn = c;
        }

        @Override
        public void run() {
            long timeout = 10 * 1000L;
            long start = (new Date()).getTime();
            try {
                InputStream is = socket.getInputStream();
                boolean available = false;
                while (!available && !socket.isClosed()) {
                    try {
                        if (is.available() != 0) {
                            available = true;
                        } else {
                            Thread.sleep(100);
                        }
                    } catch (Exception e) {
                        LOG.error("Error checking socket", e);
                    }
                    long curr = (new Date()).getTime();
                    if ((curr - start) >= timeout) {
                        break;
                    }
                }
                if (socket.isClosed()) {
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(is, baos);
                    baos.flush();
                    baos.close();
                    data = baos.toByteArray();
                }
                String msg = FtpResponse.ReadComplete.asString() + ClientCommand.SP + "Read Complete" + ClientCommand.CRLF;
                List<String> list = new ArrayList<String>();
                list.add(msg);
                ClientResponse response = new ClientResponse(list);
                ftpClient.notifyListeners(response);
            } catch (Exception e) {
                LOG.error("Error reading server response", e);
            }
        }
    }

    public InputStream getDataAsStream() {
        try {
            return new FileInputStream(tmpFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getData() {
        if (tmpFile != null) {
            try {
                return IOUtils.toByteArray(getDataAsStream());
            } catch (IOException e) {
                LOG.error("Error reading input stream", e);
            }
        }
        return null;
    }

    public String getDataAsString() {
        return new String(getData());
    }

    public void sendFile(String file) {
        SocketWriter sw = new SocketWriter(this, file);
        Thread t = new Thread(sw);
        t.start();
    }

    private class SocketWriter implements Runnable {

        String fileName;

        DataConnection conn;

        public SocketWriter(DataConnection c, String file) {
            conn = c;
            fileName = file;
        }

        @Override
        public void run() {
            try {
                LOG.info("restart:{}", ftpClient.getRestart());
                FileInputStream fis = new FileInputStream(new File(fileName));
                if (ftpClient.getRestart() != 0) {
                    fis.skip(ftpClient.getRestart());
                }
                OutputStream os = socket.getOutputStream();
                byte[] block = new byte[1024];
                for (int read = fis.read(block); read != -1; read = fis.read(block)) {
                    os.write(block, 0, read);
                }
                List<String> list = new ArrayList<String>();
                String msg = FtpResponse.FileSent.asString() + ClientCommand.SP + "File Sent" + ClientCommand.CRLF;
                list.add(msg);
                ClientResponse response = new ClientResponse(list);
                ftpClient.notifyListeners(response);
            } catch (Exception e) {
                LOG.error("Error sending file to server", e);
            }
        }
    }
}
