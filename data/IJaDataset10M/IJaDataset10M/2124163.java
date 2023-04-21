package bgu.nlp.wikidump;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class LinkExtractor {

    static long charWritten = 0;

    static int fileWritten = 1;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            readWrite("F:/Movies/input-1.xml", "F:/Movies/output-1");
            readWrite("F:/Movies/hewiki.xml", "D:/Ziv/wiki/outWiki");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readWrite(final String inFile, final String outFile) throws IOException {
        InputStreamReader reader = getReader(inFile);
        OutputStreamWriter writer = getWriter(outFile + fileWritten);
        int c = 0;
        int count = 0;
        int linkSize = 0;
        while ((c = reader.read()) > 0) {
            if (c == '[' && count == 0) {
                writer.write("[");
            }
            if (c == '[') {
                count++;
            }
            if (count == 2) {
                writer.write(c);
                charWritten++;
                linkSize++;
            }
            if (c == ']' && count > 0) {
                count--;
            }
            if ((c == ']' && count == 0) || linkSize > 40) {
                writer.write("]\r\n");
                linkSize = 0;
                count = 0;
                if (charWritten > 1000000L) {
                    writer.close();
                    charWritten = 0;
                    fileWritten++;
                    writer = getWriter(outFile + fileWritten);
                }
            }
        }
        reader.close();
        writer.close();
    }

    private static OutputStreamWriter getWriter(final String outFile) throws FileNotFoundException, UnsupportedEncodingException {
        File out = new File(outFile + ".txt");
        FileOutputStream outS = new FileOutputStream(out);
        OutputStreamWriter writer = new OutputStreamWriter(outS, "UTF-8");
        return writer;
    }

    private static InputStreamReader getReader(final String inFile) throws FileNotFoundException, UnsupportedEncodingException {
        File in = new File(inFile);
        FileInputStream inS = new FileInputStream(in);
        InputStreamReader reader = new InputStreamReader(inS, "UTF-8");
        return reader;
    }
}
