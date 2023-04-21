package org.blobstreaming.www;

import org.blobstreaming.lib.str;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.lang.Integer;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;
import java.util.BitSet;

/**
 * In general a URL has the following form:
 *
 * [protocol] '://' [user] '@' [address] [file] '#' [reference]
 * 
 * [user] has the form [name] ':' [password]
 *
 * [address] has the form [host] ':' [port]
 *
 * [file] has the form '/' [path] '?' [searchArg] '&' [searchArg] ...
 *
 * [searchArg] has the form [name] '=' [value]
 *
 */
public class URL {

    private String protocol = null;

    private String user = null;

    private String password = null;

    private String host = null;

    private int port = 0;

    private String file = null;

    private String reference = null;

    public static String decode(String value, String charset) throws IOException {
        if (value == null) return null;
        byte octets[] = value.getBytes(charset);
        int length = octets.length;
        int oi = 0;
        for (int i = 0; i < length; oi++) {
            byte aByte = (byte) octets[i++];
            if (aByte == '%' && i + 2 <= length) {
                byte high = (byte) Character.digit((char) octets[i++], 16);
                byte low = (byte) Character.digit((char) octets[i++], 16);
                if (high == -1 || low == -1) {
                    throw new IOException("Incomplete URL encoding pattern");
                }
                aByte = (byte) ((high << 4) + low);
            }
            octets[oi] = (byte) aByte;
        }
        return new String(octets, 0, oi, charset);
    }

    public static String decode(String value) throws IOException {
        return decode(value, "utf-8");
    }

    public static String encode(String value, String dont_encode, String charset) throws UnsupportedEncodingException {
        String no_enc;
        BitSet allowed = new BitSet(256);
        byte[] octets;
        no_enc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.*";
        if (dont_encode != null) no_enc = no_enc + dont_encode;
        octets = no_enc.getBytes();
        for (int i = 0; i < octets.length; i++) allowed.set(octets[i]);
        octets = charset == null ? value.getBytes() : value.getBytes(charset);
        StringBuffer buf = new StringBuffer(octets.length);
        for (int i = 0; i < octets.length; i++) {
            char c = (char) octets[i];
            if (allowed.get(c)) {
                buf.append(c);
            } else {
                buf.append('%');
                byte b = octets[i];
                char hexadecimal = Character.forDigit((b >> 4) & 0xF, 16);
                buf.append(Character.toUpperCase(hexadecimal));
                hexadecimal = Character.forDigit(b & 0xF, 16);
                buf.append(Character.toUpperCase(hexadecimal));
            }
        }
        return buf.toString();
    }

    public static String encode(String value, String dont_encode) throws UnsupportedEncodingException {
        return (encode(value, dont_encode, null));
    }

    public static String encode(String value) throws UnsupportedEncodingException {
        return (encode(value, null, null));
    }

    public URL(String uri) {
        protocol = str.left(uri, "://", -1);
        uri = str.right(uri, "://", -1);
        host = str.left(uri, "/");
        if (host == uri) file = ""; else file = "/" + str.right(uri, "/");
        if (str.locate(host, "@") >= 0) {
            user = str.left(host, "@", -1);
            host = str.right(host, "@", -1);
            if (str.locate(user, ":") >= 0) {
                password = str.right(user, ":", -1);
                user = str.left(user, ":", -1);
            }
        }
        if (str.locate(host, ":") >= 0) {
            port = Integer.valueOf(str.right(host, ":"), 10).intValue();
            host = str.left(host, ":");
        }
        if (str.locate(file, "#") >= 0) {
            reference = str.right(file, "#");
            file = str.left(file, "#");
        }
    }

    public String getProtocol() {
        if (protocol == null) return ("http");
        return (protocol);
    }

    public String getUser() {
        return (user);
    }

    public String getPassword() {
        return (password);
    }

    /**
	 * Address is host:port, where host may
	 * be a host name or an IP address.
	 * If lookupHost is true, then this returns
	 * ip-address:port.
	 */
    public String getAddress(boolean lookupHost) {
        String a = host;
        if (a == null) a = "127.0.0.1"; else {
            if (lookupHost) a = new InetSocketAddress(a, port).getAddress().getHostAddress();
        }
        if (port != 0) return (a + ":" + port);
        return (a);
    }

    public String getAddress() {
        return getAddress(false);
    }

    public String getHost() {
        return (host);
    }

    public int getPort() {
        return (port);
    }

    public String getFile() {
        if (file == null) return ("/");
        return (file);
    }

    public String getReference() {
        return (reference);
    }

    /**
	 * Return the URI (Universal Resource Identifier),
	 * without the user name.
	 */
    public String getID(boolean lookupHost) {
        String id;
        id = getProtocol() + "://" + getAddress(lookupHost) + getFile();
        return (id);
    }

    public String getID() {
        return getID(false);
    }

    public void setProtocol(String p) {
        protocol = p;
    }

    public void setUser(String u) {
        user = u;
    }

    public void setPassword(String p) {
        password = p;
    }

    public void setAddress(String a) {
        if (str.locate(a, ":") >= 0) {
            host = str.left(a, ":");
            port = Integer.valueOf(str.right(a, ":"), 10).intValue();
        } else {
            host = a;
            port = 0;
        }
    }

    public void setFile(String f) {
        if (str.substr(f, 0, 1).equals("/")) file = f; else file = "/" + f;
    }

    public void setReference(String r) {
        reference = r;
    }

    public ResourceHandler getResource() throws IOException, InstantiationException, IllegalAccessException {
        return (ResourceHandler.getHandler(this));
    }

    public ContentHandler getContent() throws IOException, InstantiationException, IllegalAccessException {
        return (getResource().getContent());
    }

    public long getContentLength() throws IOException, InstantiationException, IllegalAccessException {
        ContentHandler h;
        h = getContent();
        if (h == null) return (0);
        return (h.getLength());
    }

    public String getContentStringData() throws IOException, InstantiationException, IllegalAccessException {
        ContentHandler h;
        h = getContent();
        if (h == null) return null;
        return (h.getStringData());
    }

    public String setContent(ContentHandler h) throws IOException, InstantiationException, InstantiationException, IllegalAccessException {
        ResourceHandler r = ResourceHandler.getHandler(this);
        r.setContent(h);
        return (r.getStatus());
    }

    public String setContentData(String type, InputStream data, long len, String encoding, String options) throws IOException, InstantiationException, IllegalAccessException {
        ResourceHandler r = ResourceHandler.getHandler(this);
        r.setContentData(type, data, len, encoding, options);
        return (r.getStatus());
    }
}
