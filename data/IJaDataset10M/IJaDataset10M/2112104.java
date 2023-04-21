package net.sf.jctools.ant;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import org.apache.tools.ant.types.DataType;

public class AID extends DataType {

    public static byte[] parseAID(String aid) throws IllegalArgumentException {
        if (aid.length() == 0) {
            throw new IllegalArgumentException("AID is empty");
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int off = 0;
        while (off < aid.length()) {
            if (!aid.startsWith("0x", off)) {
                throw new IllegalArgumentException("Invalid hexa prefix at offset " + off + ": expecting '0x' but is '" + aid.substring(off, Math.min(off + 2, aid.length())) + "'");
            }
            if (aid.length() - off < 4) {
                throw new IllegalArgumentException("Invalid value at offset " + (off + 2) + ": a number must have two digits");
            }
            try {
                os.write(Integer.parseInt(aid.substring(off + 2, off + 4), 16));
            } catch (NumberFormatException e) {
                IllegalArgumentException ex = new IllegalArgumentException(e.getMessage());
                ex.initCause(e);
                throw ex;
            }
            if (aid.length() - off - 4 > 0) {
                if (aid.charAt(off + 4) != ':') {
                    throw new IllegalArgumentException("Invalid hex separator at offset " + off + ": expecting ':' but is ' " + aid.charAt(off + 4) + "'");
                } else if (aid.length() - off - 4 == 1) {
                    throw new IllegalArgumentException("AID cannot end with ':'");
                }
                off++;
            }
            off += 4;
        }
        return os.toByteArray();
    }

    private byte[] aid;

    public AID(String aid) {
        this.aid = parseAID(aid);
    }

    public byte[] getBytes() {
        return aid;
    }

    /**
   * @see Object#equals(java.lang.Object)
   */
    public boolean equals(Object obj) {
        if (!(obj instanceof AID)) {
            return false;
        }
        return Arrays.equals(this.getBytes(), ((AID) obj).getBytes());
    }

    /**
   * @see Object#toString()
   */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < aid.length; i++) {
            sb.append("0x");
            sb.append(Integer.toString((int) aid[i] & 0xFF, 16).toUpperCase());
            if (i + 1 < aid.length) {
                sb.append(':');
            }
        }
        return sb.toString();
    }
}
