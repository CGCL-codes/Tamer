package net.sf.jaybox.common;

import java.io.*;
import java.util.*;

public class StreamUtilities {

    public static long extractLong(byte[] data, int offset, int bytes) {
        long ret = 0;
        for (int i = 0; i < bytes; i++) ret |= (((long) data[i + offset]) & 0xFF) << (i * 8);
        return ret;
    }

    public static int extractInt(byte[] data, int offset, int bytes) {
        return (int) extractLong(data, offset, bytes);
    }
}
