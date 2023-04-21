package wtanaka.praya.yahoo;

import java.io.IOException;
import java.io.Serializable;
import wtanaka.debug.Debug;

/**
 * This represents a pakcet as sent by or to the server
 *
 * Adapted from yahoolib.h by Nathan Neulinger (nneul@umr.edu)
 * http://www.umr.edu/~nneul/gtkyahoo/
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2002/07/08 07:58:43 $
 **/
public class YahooRawPacket implements Serializable {

    /** 7 chars and trailing null **/
    public byte[] version = new byte[8];

    /** length is 4 bytes , little endian **/
    public int len;

    public int service;

    public int connection_id;

    public int magic_id;

    public int unknown1;

    public int msgtype;

    byte[] nick1 = new byte[36];

    byte[] nick2 = new byte[36];

    byte[] content = new byte[1];

    public static StringBuffer buffer = new StringBuffer();

    public void writeTo(LittleEndianOutputStream out) throws IOException {
        out.write(version);
        out.writeInt(len);
        out.writeInt(service);
        out.writeInt(connection_id);
        out.writeInt(magic_id);
        out.writeInt(unknown1);
        out.writeInt(msgtype);
        out.write(nick1);
        out.write(nick2);
        out.write(content);
    }

    public void readFrom(LittleEndianInputStream in) throws IOException {
        String magicString = "YHOO";
        boolean foundMagic = false;
        StringBuffer buffer = new StringBuffer();
        buffer.append((char) in.read());
        buffer.append((char) in.read());
        buffer.append((char) in.read());
        buffer.append((char) in.read());
        Debug.println("bug411336", "Scanning for magic string..");
        Debug.println("bug411336", "Initial buffer is: \"" + buffer + "\"");
        while (!buffer.toString().equals(magicString)) {
            Debug.println("bug411336", "Current buffer is: \"" + buffer + "\"");
            int ch = in.read();
            if (ch < 0) throw new java.io.EOFException("EOF (ch is " + ch + ") while looking for magic");
            buffer.setCharAt(0, buffer.charAt(1));
            buffer.setCharAt(1, buffer.charAt(2));
            buffer.setCharAt(2, buffer.charAt(3));
            buffer.setCharAt(3, (char) ch);
        }
        in.read(this.version, magicString.length(), this.version.length - magicString.length());
        this.len = in.readInt();
        this.service = in.readInt();
        this.magic_id = in.readInt();
        this.connection_id = in.readInt();
        this.unknown1 = in.readInt();
        this.msgtype = in.readInt();
        in.read(this.nick1);
        in.read(this.nick2);
        if (this.len >= 0) {
            this.content = new byte[this.len];
            in.read(this.content);
        }
    }

    private String byteArrayToString(byte[] array) {
        StringBuffer toRet = new StringBuffer();
        for (int i = 0; (i < array.length); ++i) toRet.append(array[i] + ",");
        Debug.println("YahooRawPacket", toRet);
        toRet = new StringBuffer();
        for (int i = 0; (i < array.length) && (char) array[i] != '\0'; ++i) toRet.append((char) array[i]);
        return toRet.toString();
    }

    public String getNick1() {
        return byteArrayToString(nick1);
    }

    public String getNick2() {
        return byteArrayToString(nick2);
    }

    public String getContents() {
        return byteArrayToString(content);
    }

    /**
    * Debugging copy of this packet.
    **/
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("[YahooRawPacket ");
        s.append("Version:");
        s.append((char) version[0]);
        s.append((char) version[1]);
        s.append((char) version[2]);
        s.append((char) version[3]);
        s.append((char) version[4]);
        s.append((char) version[5]);
        s.append((char) version[6]);
        s.append((char) version[7]);
        s.append('\n');
        s.append("   len:" + len + "\n");
        s.append("   service:" + YahooLib.yahoo_get_service_string(service) + "\n");
        s.append("   connection_id:" + connection_id + "\n");
        s.append("   magic_id:" + magic_id + "\n");
        s.append("   unknown1:" + unknown1 + "\n");
        s.append("   msgtype:" + msgtype + "\n");
        s.append("   nick1:" + byteArrayToString(nick1) + "\n");
        s.append("   nick2:" + byteArrayToString(nick2) + "\n");
        s.append("   content:" + byteArrayToString(content) + "\n");
        s.append("]");
        return s.toString();
    }
}
