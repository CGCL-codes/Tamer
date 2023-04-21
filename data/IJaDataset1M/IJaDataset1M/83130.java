package edu.idp.shared.compress;

/**
 * @author Kristopher T Babic
 */
public final class Fin {

    private static char[] pcTable;

    private static String input;

    private static String output;

    private static int curLoc = 0;

    private static int len;

    private static int index(char p1, char p2) {
        return (int) (p1 << 7) ^ p2;
    }

    private static void memset(char[] memloc, int val, int len) {
        for (int i = 0; i < len; i++) memloc[i] = (char) val;
    }

    private static void setInput(String in) {
        input = in;
        curLoc = 0;
        len = in.length();
        output = null;
    }

    private static String getOutput() {
        return output;
    }

    private static char getNext() {
        return input.charAt(curLoc++);
    }

    private static boolean isEnd() {
        return (curLoc >= len);
    }

    private static void write(char c) {
        if (output == null) output = new String();
        output += c;
    }

    public static String compress(String in) {
        char p1 = 0, p2 = 0;
        char[] buf = new char[8];
        char ctr = 0;
        int bctr = 0;
        int mask = 0;
        setInput(in);
        pcTable = new char[32768];
        memset(pcTable, 32, 32768);
        while (!isEnd()) {
            char c = getNext();
            if (pcTable[index(p1, p2)] == c) mask = mask ^ (1 << ctr); else {
                pcTable[index(p1, p2)] = c;
                buf[bctr++] = c;
            }
            if (++ctr == 8) {
                write((char) mask);
                for (int i = 0; i < bctr; i++) write(buf[i]);
                ctr = 0;
                bctr = 0;
                mask = 0;
            }
            p1 = p2;
            p2 = c;
        }
        if (ctr > 0) write((char) mask);
        for (int i = 0; i < bctr; i++) write(buf[i]);
        return getOutput();
    }

    public static String decompress(String in) {
        int ci, co;
        char p1 = 0, p2 = 0;
        char mask = 0;
        setInput(in);
        memset(pcTable, 32, 32768);
        while (!isEnd()) {
            ci = (int) getNext();
            mask = (char) ci;
            for (int ctr = 0; ctr < 8; ctr++) {
                if ((mask & (1 << ctr)) != 0) co = (int) pcTable[index(p1, p2)]; else {
                    if (!isEnd()) {
                        co = (int) getNext();
                        pcTable[index(p1, p2)] = (char) co;
                    } else return getOutput();
                }
                write((char) co);
                p1 = (char) p2;
                p2 = (char) co;
            }
        }
        return getOutput();
    }
}
