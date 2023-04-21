package info.olteanu.utils.file;

import java.io.*;
import info.olteanu.utils.*;

public class BinaryDiff {

    public static void main(String[] args) throws Exception {
        FileInputStream f1 = new FileInputStream(args[0]);
        FileInputStream f2 = new FileInputStream(args[1]);
        long diffCount = binaryDiff(System.out, f1, f2);
        if (diffCount == 0) System.out.println("No differences"); else System.out.println("Differences: " + diffCount);
        f1.close();
        f2.close();
    }

    public static long binaryDiff(PrintStream out, InputStream is1, InputStream is2) throws IOException {
        BufferedInputStream r1 = new BufferedInputStream(is1);
        BufferedInputStream r2 = new BufferedInputStream(is2);
        long diffCount = 0;
        long index = 0;
        while (true) {
            int byte1 = r1.read();
            int byte2 = r2.read();
            if (byte1 == -1 || byte2 == -1) {
                if (byte1 != -1) out.println("First file longer");
                if (byte2 != -1) out.println("Second file longer");
                return diffCount;
            }
            if (byte1 != byte2) {
                diffCount++;
                out.println(StringTools.adjustLengthForNumber(Long.toHexString(index), 8) + ": " + StringTools.adjustLengthForNumber(Integer.toHexString(byte1), 2) + " " + StringTools.adjustLengthForNumber(Integer.toHexString(byte2), 2));
            }
            index++;
        }
    }
}
