package net.bionicmessage.groupdav;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A HTTP Input Stream that supports chunked-transfer-encoding
 * and hides HTTP headers from the reader.
 * @author matt
 */
public class HTTPInputStream extends BufferedInputStream {

    protected Socket sock;

    protected HashMap headers;

    protected List<HTTPCookie> setCookies;

    protected boolean chunked = false;

    protected boolean headersRead = false;

    protected int headerLinesRead = 0;

    protected int returnedStatus = 0;

    protected String returnedReason = "";

    protected boolean outOfChunks = false;

    public HTTPInputStream(Socket parent) throws IOException {
        super(parent.getInputStream());
        sock = parent;
        headers = new HashMap(10);
        setCookies = new ArrayList<HTTPCookie>(10);
    }

    @Override
    public synchronized int read() throws IOException {
        byte[] d = new byte[1];
        this.read(d, 0, 1);
        return (int) d[0];
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int bytesAck = 0;
        String str = null;
        if (!headersRead) {
            while ((str = readLineASCII()) != null) {
                if (headerLinesRead == 0) {
                    returnedStatus = Integer.parseInt(str.substring(10, 12));
                    returnedReason = str.substring(13);
                    headerLinesRead++;
                    bytesAck = bytesAck + str.length();
                } else if (str.contains(":")) {
                    String[] kv = str.split(":");
                    String name = kv[0].toLowerCase().trim();
                    if (name.contains("set-cookie")) {
                        HTTPCookie cookie = new HTTPCookie(kv[1].trim());
                        setCookies.add(cookie);
                    } else {
                        headers.put(name, kv[1].trim());
                    }
                    headerLinesRead++;
                    bytesAck = bytesAck + str.length();
                } else {
                    headersRead = true;
                    break;
                }
            }
            this.reset();
        }
        if ("chunked".equals(headers.get("transfer-encoding"))) {
            chunked = true;
            String chunkheader = readLineASCII();
            chunkheader = chunkheader.trim();
            int size = Integer.parseInt(chunkheader, 16) + 2;
            if (size != 0 && (size - 2) != 0) {
                byte[] chunk = new byte[size];
                do {
                } while (size > super.available());
                int read = super.read(chunk, off, size);
                System.arraycopy(chunk, 0, b, off, size - 2);
                return (read - 2);
            }
            outOfChunks = true;
            return -1;
        }
        return super.read(b, off, len);
    }

    protected String readLineASCII() throws IOException {
        int b;
        int read = 0;
        byte[] buf = new byte[65535];
        while ((b = super.read()) != -1) {
            read++;
            buf[read] = (byte) b;
            if (b == '\r') {
                this.mark(1);
                b = super.read();
                if (b == '\n') {
                    this.mark(1);
                    break;
                } else {
                    this.reset();
                }
            } else if (b == '\n') {
                break;
            }
        }
        return new String(buf, 0, read);
    }

    public HashMap getHeaders() {
        return headers;
    }

    public List<HTTPCookie> getCookies() {
        return setCookies;
    }
}
