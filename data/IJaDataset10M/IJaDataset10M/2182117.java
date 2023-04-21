package panda.query.util;

import panda.server.Panda;

public class BufferInfo {

    public static int bestChunkSize(int fileSize) {
        int avail = Panda.getBufferManager().available();
        if (avail <= 1) return 1;
        int k = fileSize;
        double i = 1.0;
        while (k > avail) {
            i++;
            k = (int) Math.ceil(fileSize / i);
        }
        return k;
    }
}
