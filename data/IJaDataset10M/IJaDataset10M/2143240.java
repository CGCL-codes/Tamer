package ch.comtools.jsch.jce;

import java.security.MessageDigest;
import ch.comtools.jsch.HASH;

public class MD5 implements HASH {

    MessageDigest md;

    public int getBlockSize() {
        return 16;
    }

    public void init() throws Exception {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void update(byte[] foo, int start, int len) throws Exception {
        md.update(foo, start, len);
    }

    public byte[] digest() throws Exception {
        return md.digest();
    }
}
