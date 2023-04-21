package edu.usc.epigenome.uecgatk;

import org.broadinstitute.sting.utils.BaseUtils;

public class BaseUtilsMore {

    public static byte[] toUpperCase(byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = toUpperCase(in[i]);
        }
        return out;
    }

    public static byte toUpperCase(byte b) {
        return (byte) Character.toUpperCase((char) b);
    }
}
