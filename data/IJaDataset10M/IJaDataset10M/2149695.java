package ist.ac.simulador.assembler.p3;

import ist.ac.simulador.assembler.p3.P3Recognizer;
import ist.ac.simulador.assembler.p3.P3Lexer;
import ist.ac.simulador.assembler.IInstruction;
import java.io.*;
import java.awt.event.*;

/** Main Compiler class
 */
public class p3as implements ist.ac.simulador.assembler.IParser {

    /** Starts the compiler
     * @param args The file or files to compile
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                System.err.println("Parsing...");
                for (int i = 0; i < args.length; i++) {
                    doFile(new File(args[i]));
                    System.err.println("done.");
                }
            } else System.err.println("Usage: java p3as.Main " + "<directory or file name>");
        } catch (Exception e) {
            System.err.println("exception: " + e);
            e.printStackTrace(System.err);
        }
    }

    /** Compile the file. This method decides what action to take based on the type of
     * file we are looking at.
     * @param f The file to compile
     * @throws Exception Any IO exception.
     */
    public static void doFile(File f) throws Exception {
        if (f.isDirectory()) {
            String files[] = f.list();
            for (int i = 0; i < files.length; i++) doFile(new File(f, files[i]));
        } else if ((f.getName().length() > 4) && f.getName().substring(f.getName().length() - 4).equals(".as")) {
            System.err.print("   " + f.getAbsolutePath());
            FileInputStream in = new FileInputStream(f);
            System.err.println(" " + parseFile(f.getName(), in));
        }
    }

    /** Parses the file and generates the compiled code into
     * a file with name <CODE>f</CODE>.cod read from the input stream <CODE>s</CODE>.
     * @param f The files name
     * @param s The input stream to be read.
     * @return null if ok. A string with the error description in an error occurs.
     */
    public static String parseFile(String f, InputStream s) {
        return parseFile(f, s, null);
    }

    /** Parses the file <CODE>f</CODE>, reading from <CODE>s</CODE> and
     * writing into <CODE>o</CODE>.
     * @param f The file's name
     * @param s The input stream to read
     * @param o The output stream.
     * @return null if ok. A string with the error description in an error occurs.
     */
    public static String parseFile(String f, InputStream s, OutputStream o) {
        try {
            P3Lexer lexer = new P3Lexer(s);
            lexer.setFilename(f);
            P3Recognizer parser = new P3Recognizer(lexer);
            parser.setFilename(f);
            parser.compilationUnit();
            Object state = parser.getState();
            lexer = new P3Lexer(new FileInputStream(f));
            lexer.setFilename(f);
            parser = new P3Recognizer(lexer);
            parser.setFilename(f);
            if (o == null) parser.setState(state); else parser.setState(state, o);
            parser.compilationUnit();
            parser.closeOutput();
        } catch (Exception e) {
            return "parser exception: " + e;
        }
        return null;
    }

    public String getExt() {
        return ".as";
    }

    public String getExtExplain() {
        return "P3 files";
    }

    public IInstruction getInstruction() {
        return new Instruction();
    }

    public String parsingFile(String f, InputStream s) {
        return parseFile(f, s, null);
    }

    public String parsingFile(String f, InputStream s, OutputStream o) {
        return parseFile(f, s, o);
    }
}
