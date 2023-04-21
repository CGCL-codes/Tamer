package net.gencsoy.tesjeract;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

public class TiffTestMain {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) System.loadLibrary("tessdll");
        System.loadLibrary("tesjeract");
        if (args.length != 1) {
            System.err.println("Should give TIFF file as first argument");
            System.exit(1);
        }
        File tiff = new File(args[0]);
        if (!tiff.exists()) {
            System.err.println("No such file " + args[0]);
            System.exit(1);
        }
        MappedByteBuffer buf = new FileInputStream(tiff).getChannel().map(MapMode.READ_ONLY, 0, tiff.length());
        Tesjeract tess = new Tesjeract("eng");
        EANYCodeChar[] words = tess.recognizeAllWords(buf);
        for (EANYCodeChar c : words) {
            while (c.blanks-- > 0) System.out.print(" ");
            System.out.print((char) c.char_code);
        }
    }
}
