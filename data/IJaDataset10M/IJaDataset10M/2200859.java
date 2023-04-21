package test;

import java.lang.*;
import java.util.Random;
import takatuka.debug.Gc;
import java.util.Vector;
import java.util.Enumeration;

public class OfflineGcTest {

    static final int MAX_LIMIT = 3;

    static Random r = new Random();

    static Vector freeCands = new Vector();

    static void doStuff(int group_id) {
        Object tmp;
        tmp = new byte[r.nextInt(4) + 1];
        Gc.addToGroup(tmp, group_id);
        if (r.nextInt(5) == 0) {
            freeCands.addElement(tmp);
        }
    }

    public static void main(String[] args) {
        int limit;
        limit = 10;
        for (int i = 0; i < limit; i++) {
            doStuff(1);
        }
        for (int i = 0; i < limit; i++) {
            doStuff(2);
        }
        for (int i = 0; i < limit; i++) {
            doStuff(3);
        }
        Gc.dumpHeap();
        Gc.dumpGroups();
    }
}
