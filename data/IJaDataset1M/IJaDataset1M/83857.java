package net.sf.mpxj.sample;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.io.InputStream;

/**
 * This is a trivial class used to dump the contents of an MPP file
 * broken down into its internal file and directory structure, with the
 * content of each of the files written out as hex digits, and their
 * ASCII equivalents.
 */
public class MppDump {

    /**
    * Main method.
    *
    * @param args array of command line arguments
    */
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println("Usage: MppDump <input mpp file name> <output text file name>");
            } else {
                System.out.println("Dump started.");
                long start = System.currentTimeMillis();
                process(args[0], args[1]);
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("Dump completed in " + elapsed + "ms");
            }
        } catch (Exception ex) {
            System.out.println("Caught " + ex.toString());
        }
    }

    /**
    * This method opens the input and output files and kicks
    * off the processing.
    *
    * @param input Name of the input file
    * @param output Name of the output file
    * @throws Exception Thrown on file read errors
    */
    private static void process(String input, String output) throws Exception {
        FileInputStream is = new FileInputStream(input);
        PrintWriter pw = new PrintWriter(new FileWriter(output));
        POIFSFileSystem fs = new POIFSFileSystem(is);
        dumpTree(pw, fs.getRoot(), true, true, null);
        is.close();
        pw.flush();
        pw.close();
    }

    /**
    * This method recursively descends the directory structure, dumping
    * details of any files it finds to the output file.
    *
    * @param pw Output PrintWriter
    * @param dir DirectoryEntry to dump
    * @param showData flag indicating if data is dumped, or just structure
    * @param hex set to true if hex output is required
    * @param indent indent used if displaying structure only
    * @throws Exception Thrown on file read errors
    */
    @SuppressWarnings("unchecked")
    private static void dumpTree(PrintWriter pw, DirectoryEntry dir, boolean showData, boolean hex, String indent) throws Exception {
        long byteCount;
        for (Iterator<Entry> iter = dir.getEntries(); iter.hasNext(); ) {
            Entry entry = iter.next();
            if (entry instanceof DirectoryEntry) {
                String childIndent = indent;
                if (childIndent != null) {
                    childIndent += " ";
                }
                pw.println("start dir: " + entry.getName());
                dumpTree(pw, (DirectoryEntry) entry, showData, hex, childIndent);
                pw.println("end dir: " + entry.getName());
            } else if (entry instanceof DocumentEntry) {
                if (showData) {
                    pw.println("start doc: " + entry.getName());
                    if (hex == true) {
                        byteCount = hexdump(new DocumentInputStream((DocumentEntry) entry), pw);
                    } else {
                        byteCount = asciidump(new DocumentInputStream((DocumentEntry) entry), pw);
                    }
                    pw.println("end doc: " + entry.getName() + " (" + byteCount + " bytes read)");
                } else {
                    if (indent != null) {
                        pw.print(indent);
                    }
                    pw.println("doc: " + entry.getName());
                }
            } else {
                pw.println("found unknown: " + entry.getName());
            }
        }
    }

    /**
    * This method dumps the entire contents of a file to an output
    * print writer as hex and ASCII data.
    *
    * @param is Input Stream
    * @param pw Output PrintWriter
    * @return number of bytes read
    * @throws Exception Thrown on file read errors
    */
    private static long hexdump(InputStream is, PrintWriter pw) throws Exception {
        byte[] buffer = new byte[BUFFER_SIZE];
        long byteCount = 0;
        char c;
        int loop;
        int count;
        long address = 0;
        StringBuffer sb = new StringBuffer();
        while (true) {
            count = is.read(buffer);
            if (count == -1) {
                break;
            }
            byteCount += count;
            sb.setLength(0);
            for (loop = 0; loop < count; loop++) {
                sb.append(" ");
                sb.append(HEX_DIGITS[(buffer[loop] & 0xF0) >> 4]);
                sb.append(HEX_DIGITS[buffer[loop] & 0x0F]);
            }
            while (loop < BUFFER_SIZE) {
                sb.append("   ");
                ++loop;
            }
            sb.append("   ");
            for (loop = 0; loop < count; loop++) {
                c = (char) buffer[loop];
                if (c > 200 || c < 27) {
                    c = ' ';
                }
                sb.append(c);
            }
            pw.println(sb.toString());
            address += count;
        }
        return (byteCount);
    }

    /**
    * This method dumps the entire contents of a file to an output
    * print writer as ascii data.
    *
    * @param is Input Stream
    * @param pw Output PrintWriter
    * @return number of bytes read
    * @throws Exception Thrown on file read errors
    */
    private static long asciidump(InputStream is, PrintWriter pw) throws Exception {
        byte[] buffer = new byte[BUFFER_SIZE];
        long byteCount = 0;
        char c;
        int loop;
        int count;
        long address = 0;
        StringBuffer sb = new StringBuffer();
        while (true) {
            count = is.read(buffer);
            if (count == -1) {
                break;
            }
            byteCount += count;
            sb.setLength(0);
            for (loop = 0; loop < count; loop++) {
                c = (char) buffer[loop];
                if (c > 200 || c < 27) {
                    c = ' ';
                }
                sb.append(c);
            }
            pw.print(sb.toString());
            address += count;
        }
        return (byteCount);
    }

    /**
    * Buffer size for data output.
    */
    private static final int BUFFER_SIZE = 16;

    /**
    * Data used for conversion to hex.
    */
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
