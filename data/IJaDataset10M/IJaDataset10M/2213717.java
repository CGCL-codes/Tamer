package corina.map;

import java.io.*;
import java.util.StringTokenizer;

public class Raw2Pack {

    private static class Segment {

        Segment(String line) {
        }

        void emit(OutputStream o) {
        }
    }

    public static void main(String args[]) throws IOException {
        final String filename = "earth.raw";
        int size[] = new int[] { 0, 0, 0, 0, 0, 0 };
        BufferedReader r = new BufferedReader(new FileReader(filename));
        String line;
        for (; ; ) {
            line = r.readLine();
            if (line == null) break;
            StringTokenizer tok = new StringTokenizer(line, "=, ");
            tok.nextToken();
            tok.nextToken();
            tok.nextToken();
            tok.nextToken();
            int x = Integer.parseInt(tok.nextToken());
            int y = Integer.parseInt(tok.nextToken());
            while (tok.hasMoreTokens()) {
                int x2 = Integer.parseInt(tok.nextToken());
                int y2 = Integer.parseInt(tok.nextToken());
                int dx = Math.abs(x2 - x);
                int dy = Math.abs(y2 - y);
                try {
                    int s1 = (dx == 0 ? 0 : (int) Math.ceil(Math.log(dx) / Math.log(7)));
                    size[s1]++;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    System.out.println("oops, dx=" + dx);
                }
                try {
                    int s2 = (dy == 0 ? 0 : (int) Math.ceil(Math.log(dy) / Math.log(7)));
                    size[s2]++;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    System.out.println("oops, dx=" + dx);
                }
                x = x2;
                y = y2;
            }
        }
        int total = 0;
        for (int i = 0; i < size.length; i++) {
            System.out.println("number of moves that fit in " + i + " bytes: " + size[i]);
            total += i * size[i];
        }
        System.out.println("total size for all: " + total + " B = " + (total / 1024) + " KB = " + (total / (1024 * 1024)) + " MB");
    }
}
