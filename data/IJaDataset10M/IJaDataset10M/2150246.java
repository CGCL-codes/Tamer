package ssmith.io;

import java.io.*;

/**
 *
 * @author  steve smith
 * @version
 */
public class TextFile {

    private String type;

    public static String READ = "read";

    public static String WRITE = "write";

    public static String APPEND = "append";

    private BufferedReader br;

    private BufferedWriter bw;

    private boolean EOF;

    private String filename;

    /** Creates new TextFile */
    public TextFile() {
    }

    /** Creates new TextFile */
    public void openFile(String Filename, String Type) throws FileNotFoundException, IOException {
        this.type = Type;
        this.filename = Filename;
        if (Type.equalsIgnoreCase(READ)) {
            br = new BufferedReader(new FileReader(Filename));
            EOF = false;
        } else if (Type.equalsIgnoreCase(WRITE)) {
            bw = new BufferedWriter(new FileWriter(Filename, false));
            EOF = true;
        } else if (Type.equalsIgnoreCase(APPEND)) {
            bw = new BufferedWriter(new FileWriter(Filename, true));
            EOF = true;
        } else {
            throw new IOException("TextFile(): Invalid Type (" + Type + ")");
        }
    }

    /**
	 * This returns a string, or nothing if EOF reached.
	 */
    public String readLine() throws IOException {
        String line;
        line = br.readLine();
        if (line != null) {
            EOF = false;
            if (br.markSupported() == true) {
                br.mark(1024);
                if (br.readLine() == null) {
                    EOF = true;
                }
                br.reset();
            }
        } else {
            EOF = true;
        }
        return line;
    }

    public boolean isEOF() {
        return EOF;
    }

    public void writeLine(String text) throws IOException {
        bw.write(text);
        bw.newLine();
    }

    public void write(String text) throws IOException {
        bw.write(text);
    }

    public void close() throws IOException {
        if (type.equalsIgnoreCase(READ)) {
            br.close();
        } else if (type.equalsIgnoreCase(WRITE)) {
            bw.close();
        } else if (type.equalsIgnoreCase(APPEND)) {
            bw.close();
        }
        EOF = true;
    }

    public String getFilename() {
        return filename;
    }

    public boolean delete() {
        return new File(filename).delete();
    }
}
