package org.openymsg.network.connection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openymsg.network.ServiceType;
import org.openymsg.network.connection.YMSGHeader;

/**
 * A YMSG9 packet has a 20 byte fixed format header. The first four bytes are
 * the magic code "YMSG". The next four contain the protocol version 0x09000000.
 * The next two are the size in bytes of the body. Following this a two byte
 * service id (which effectively determines the message/body type). Then a four
 * byte status code, usually zero on outgoing packets. Finally a four byte
 * session code, which should always parrot in outgoing packets the last
 * incoming packet (it can switch mid-session, apparently!)
 * 
 * The body is a set of ASCII strings terminated by the byte sequence 0xc080 .
 * Some of these strings are 'instructions' written as numbers, which appear to
 * dictate the type and/or meaning of the data which follows in the next
 * section. In a way they are like key/value pairs, except that not all keys
 * appear to require values. (Given the user- unfriendly nature of these codes,
 * and their apparent inconsistent usage from one message type to another,
 * little reverse engineered information exists as to their actual meaning.
 * Those implementing the YMSG9 protocol tend to just copy them verbatim.)
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 * @author S.E. Morris
 */
public class YMSGInputStream extends BufferedInputStream {

    private static final Logger log = Logger.getLogger(YMSGInputStream.class);

    public YMSGInputStream(InputStream in) {
        super(in);
    }

    public YMSGHeader createHeader() throws IOException {
        YMSGHeader p = new YMSGHeader();
        byte[] header = new byte[20];
        if (readBuffer(header) <= 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append((char) header[i]);
        }
        p.magic = sb.toString();
        p.version = u2i(header[5]);
        p.length = (u2i(header[8]) << 8) + (u2i(header[9]));
        p.service = ServiceType.getServiceType((u2i(header[10]) << 8) + (u2i(header[11])));
        p.status = (u2i(header[12]) << 24) + (u2i(header[13]) << 16) + (u2i(header[14]) << 8) + (u2i(header[15]));
        p.sessionId = (u2i(header[16]) << 24) + (u2i(header[17]) << 16) + (u2i(header[18]) << 8) + (u2i(header[19]));
        if (!p.magic.equals("YMSG")) {
            throw new IOException("Bad YMSG9 header");
        }
        return p;
    }

    protected String[] createBody(YMSGHeader header) throws IOException, UnsupportedEncodingException {
        List<String> v = new ArrayList<String>();
        byte[] body = new byte[header.length];
        if (readBuffer(body) < 0) return null;
        String charEncoding = System.getProperty("openymsg.network.charEncoding", "UTF-8");
        int start = 0;
        boolean keyPos = true;
        for (int i = 0; i < body.length - 1; i++) {
            if (u2i(body[i]) == 0xc0 && u2i(body[i + 1]) == 0x80) {
                String s = new String(body, start, i - start, charEncoding);
                if (keyPos) {
                    s = cleanse(s);
                    if (isKey(s)) v.add(s);
                } else v.add(s);
                keyPos = !keyPos;
                i++;
                start = i + 1;
            }
        }
        if ((v.size() % 2) != 0) {
            v.remove(v.size() - 1);
        }
        String[] messageBody = v.toArray(new String[v.size()]);
        return messageBody;
    }

    private int u2i(byte b) {
        return b & 0xff;
    }

    /**
	 * I've noticed on some chat rooms, particularly those with lots of users
	 * which require multiple packets to send the membership list, the second
	 * packet can contain garbled data, usually false elements beginning with a
	 * zero byte (\0). I've no idea if this is a bug in Yahoo's code, a
	 * deliberate attempt to make third party clients crash, or merely a bug in
	 * what *I've* written. This code is an attempt to trap and deal with this
	 * troublesome data. If anyone has any better ideas, get in touch!
	 */
    private String cleanse(String s) {
        while (s.length() > 0 && (s.charAt(0) == 0 || s.charAt(0) > 0x7f)) {
            s = s.substring(1);
        }
        return s;
    }

    private boolean isKey(String s) {
        for (int i = 0; i < s.length(); i++) if (!Character.isDigit(s.charAt(i))) return false;
        return (s.length() <= 5);
    }

    /**
	 * Reads an entire buffer of data, allowing for blocking. Returns bytes read
	 * (should be == to buffer size) or negative bytes read if 'EOF'
	 * encountered.
	 * 
	 * @param buff
	 *            Buffer to fill with data.
	 * @return Bytes read
	 */
    private int readBuffer(byte[] buff) throws IOException {
        int p = 0, r = 0;
        while (p < buff.length) {
            r = super.read(buff, p, buff.length - p);
            if (r < 0) return (p + 1) * -1;
            p += r;
        }
        return p;
    }
}
