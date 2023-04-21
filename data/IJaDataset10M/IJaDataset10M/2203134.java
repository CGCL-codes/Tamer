package storage.multibuffer;

import server.starDB;

public class BufferNeeds {

    public static int bestFactor(int size) {
        int avail = starDB.bufferMgr().available();
        if (avail <= 1) {
            return 1;
        }
        int k = size;
        double i = 1d;
        while (k > avail) {
            i++;
            k = (int) Math.ceil(size / i);
        }
        return k;
    }

    public static int bestRoot(int size) {
        int avail = starDB.bufferMgr().available();
        if (avail <= 1) {
            return 1;
        }
        int k = Integer.MAX_VALUE;
        double i = 1d;
        while (k < avail) {
            i++;
            k = (int) Math.ceil(Math.pow(size, i / 1));
        }
        return k;
    }
}
