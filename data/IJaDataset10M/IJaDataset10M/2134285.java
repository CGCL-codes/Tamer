package processing.core;

import java.io.*;
import javax.microedition.io.*;

/** The <b>PClient</b> object is used to initiate network requests to 
 * web servers on the Internet.  It supports the two most common methods
 * of the Hypertext Transfer Protocol (HTTP), <b>GET</b> and <b>POST</b>, allowing it
 * to communicate with servers as if it were a desktop web browser.  Both
 * methods initiate a new network request and return a <b>PRequest</b> object used
 * to track the request and read the returned data.
 *
 * @category Net
 * @related PRequest
 * @author  Francis Li
 */
public class PClient {

    protected PMIDlet midlet;

    protected String server;

    protected int port;

    protected String authorization;

    public PClient(PMIDlet midlet, String server) {
        this(midlet, server, 80);
    }

    /**
     * @param midlet PMIDlet: typically use "this"
     * @param server String: name or IP address of server
     * @param port int: optional port number to read/write from on the server
     */
    public PClient(PMIDlet midlet, String server, int port) {
        this.midlet = midlet;
        this.server = server;
        this.port = port;
    }

    /** Sets up HTTP Basic authentication.  This is NOT secure!  HTTP Basic
     * simply concats the username and password together, then Base64 encodes
     * it.
     * 
     * @param username String: username to send
     * @param password String: password to send
     */
    public void setAuthorization(String username, String password) {
        String concat = username + ":" + password;
        authorization = "Basic " + encode(concat.getBytes());
    }

    /** Minimal URL encoding implementation. Replaces disallowed characters
     * with corresponding escape codes. This is a static method that can
     * be accessed anywhere without instantiating a PClient object.
     * 
     * @returns String: url encoded version of String
     * @hidden
     */
    public static String urlencode(String str) {
        StringBuilder encoded = new StringBuilder();
        char c;
        for (int i = 0, length = str.length(); i < length; i++) {
            c = str.charAt(i);
            if (Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c)) {
                encoded.append(c);
            } else if (c == ' ') {
                encoded.append('+');
            } else {
                encoded.append('%');
                if (c < 16) {
                    encoded.append('0');
                }
                encoded.append(Integer.toHexString(c));
            }
        }
        return encoded.toString();
    }

    /** The GET method is used to fetch data from a web server.  It is the method
     * used by web browsers most commonly used to fetch the contents of a URL.  It
     * can also be used to execute scripts that return data based on parameters
     * passed in the URL string.
     * 
     * @param file String: name of file to fetch or script to execute on the web server
     * @param params String[]: an array of String objects representing the names of parameters being passed to a script
     * @param values String[]: an array of String objects representing the values of parameters being passed to a script
     * @return PRequest
     */
    public PRequest GET(String file) {
        return request(file, null, null);
    }

    public PRequest GET(String file, String[] params, String[] values) {
        StringBuilder query = new StringBuilder();
        query.append(file);
        query.append("?");
        for (int i = 0, length = params.length; i < length; i++) {
            query.append(params[i]);
            query.append("=");
            query.append(urlencode(values[i]));
            if (i < (length - 1)) {
                query.append("&");
            }
        }
        return GET(query.toString());
    }

    /** The <b>POST</b> method is used to submit data to a web server to be processed.
     * This is the method most commonly used by web browsers to send data from
     * forms when the user clicks on a "Submit" button.  The <b>params</b> array
     * contains the names of the form fields.  The <b>values</b> array must be 
     * the same size as and contain the values corresponding, in order, to the 
     * names specified in the <b>params</b> array.  If you need to submit
     * binary data, create an array of Objects that can contain either String
     * values or byte array (byte[]) objects containing the binary data.
     *
     * @param file String: name of the web page/script that will process the data
     * @param params String[]: an array of String objects containing the names of form fields being submitted
     * @param values String[]: an array of String objects containing the values of form fields being submitted
     * @param data Object[]: an array of objects, each object either String or byte[], representing values or binary data being submitted 
     * @return PRequest
     */
    public PRequest POST(String file, String[] params, String[] values) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        for (int i = 0, length = params.length; i < length; i++) {
            if (i > 0) {
                ps.print("&");
            }
            ps.print(urlencode(params[i]));
            ps.print("=");
            ps.print(urlencode(values[i]));
        }
        return request(file, "application/x-www-form-urlencoded", baos.toByteArray());
    }

    public PRequest POST(String file, String[] params, Object[] data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            for (int i = 0, length = params.length; i < length; i++) {
                ps.print("--BOUNDARY_185629\r\n");
                ps.print("Content-Disposition: form-data; name=\"");
                ps.print(params[i]);
                if (data[i] instanceof String) {
                    ps.print("\"\r\n");
                    ps.print("\r\n");
                    ps.print((String) data[i]);
                    ps.print("\r\n");
                } else if (data[i] instanceof byte[]) {
                    byte[] buffer = (byte[]) data[i];
                    ps.print("\"; filename=\"");
                    ps.print(params[i]);
                    ps.print("\"\r\n");
                    ps.print("Content-Type: application/octet-stream\r\n");
                    ps.print("Content-Transfer-Encoding: binary\r\n\r\n");
                    ps.write(buffer);
                    ps.print("\r\n");
                }
            }
            ps.print("--BOUNDARY_185629--\r\n\r\n");
            return request(file, "multipart/form-data; boundary=BOUNDARY_185629", baos.toByteArray());
        } catch (IOException ioe) {
            throw new PException(ioe);
        }
    }

    protected PRequest request(String file, String contentType, byte[] bytes) {
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(server);
        if (port != 80) {
            url.append(":");
            url.append(port);
        }
        url.append(file);
        PRequest request = new PRequest(midlet, url.toString(), contentType, bytes, authorization);
        Thread t = new Thread(request);
        t.start();
        return request;
    }

    /** The following Base64 implementation:
     *
     * Copyright (c) 2002,2003, Stefan Haustein, Oberhausen, Rhld., Germany
     *
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or
     * sell copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     *
     * The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software.
     *
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
     * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
     * IN THE SOFTWARE. 
     * 
     * @hidden
     */
    public static String encode(byte[] data) {
        return encode(data, 0, data.length, null).toString();
    }

    static final char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    /** Encodes the part of the given byte array denoted by start and
     * len to the Base64 format.  The encoded data is appended to the
     * given StringBuilder. If no StringBuilder is given, a new one is
     * created automatically. The StringBuilder is the return value of
     * this method. This is a static method that can
     * be accessed anywhere without instantiating a PClient object.
     * 
     * @returns StringBuilder
     * @hidden
     */
    public static StringBuilder encode(byte[] data, int start, int len, StringBuilder buf) {
        if (buf == null) {
            buf = new StringBuilder(data.length * 3 / 2);
        }
        int end = len - 3;
        int i = start;
        int n = 0;
        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8) | (((int) data[i + 2]) & 0x0ff);
            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append(charTab[(d >> 6) & 63]);
            buf.append(charTab[d & 63]);
            i += 3;
            if (n++ >= 14) {
                n = 0;
                buf.append("\r\n");
            }
        }
        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);
            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append(charTab[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;
            buf.append(charTab[(d >> 18) & 63]);
            buf.append(charTab[(d >> 12) & 63]);
            buf.append("==");
        }
        return buf;
    }
}
