package stuff;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
import java.util.zip.*;
import stuff.StringUtils;
import contrib.CBZip2InputStream;

/**
* @author vincent
*
*/
public class InBlockGMX {

    String fileName;

    static BufferedReader stream;

    StringTokenizer tok;

    static Pattern startPattern = Pattern.compile("\0133");

    static int doode = 0;

    public InBlockGMX() {
        this("");
    }

    public InBlockGMX(String fn) {
        open(fn);
    }

    /**
	 * @param fn
	 * @throws IOException
	 */
    public void open(final String fn) {
        fileName = fn;
        try {
            if (fn.equals("")) {
                System.err.print("InBlock: No filename supplied. ");
                System.err.print("Defaulting to stdin.\n");
                stream = new BufferedReader(new InputStreamReader(System.in));
            } else {
                if (fn.endsWith(".gz")) {
                    stream = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fn))));
                } else if (fn.endsWith(".bz2")) {
                    stream = new BufferedReader(new InputStreamReader(new CBZip2InputStream(new FileInputStream(fn))));
                } else stream = new BufferedReader(new FileReader(fn));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * close the file stream and reset the file name
	 * 
	 * @throws IOException
	 */
    public void close() {
        if (fileName.equals("")) {
        } else {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
	 * 
	 * @return all solute! blocks as an array list separated by "[ blockname ]"
	 */
    public static ArrayList getNextBlock() {
        ArrayList block = new ArrayList();
        String line = new String();
        int blockid = 0;
        try {
            while (((line = stream.readLine()) != null)) {
                if (line.trim().matches("\\[\\ atoms\\ \\]")) {
                    blockid = 1;
                    block.add(line);
                } else if (line.trim().matches("\\[\\ bonds\\ \\]")) {
                    blockid = 2;
                    block.add(line);
                } else if (line.trim().matches("\\[\\ pairs\\ \\]")) {
                    blockid = 3;
                    block.add(line);
                } else if (line.trim().matches("\\[\\ angles\\ \\]")) {
                    blockid = 4;
                    block.add(line);
                } else if (line.trim().matches("\\[\\ dihedrals\\ \\]")) {
                    blockid = 5;
                    block.add(line);
                } else if (blockid != 0) {
                    if (blockid == 1) block.add(line); else if (blockid == 2) block.add(line); else if (blockid == 3) block.add(line); else if (blockid == 4) block.add(line); else if (blockid == 5) block.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return block;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fin;
        ArrayList block = new ArrayList();
        block = getNextBlock();
    }
}
