package org.xmlcml.cml.converters.antlr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

/**
 *
 * @author pm286
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        InputStream instream = System.in;
        if (args.length > 0) {
            String filename = args[0];
            File file = new File(filename);
            System.out.println("F " + file.getAbsolutePath());
            instream = new FileInputStream(file);
        }
        processInput(instream);
    }

    private static void processInput(InputStream instream) {
        try {
            ANTLRInputStream input = new ANTLRInputStream(instream);
            TestLexer lexer = new TestLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TestParser parser = new TestParser(tokens);
            parser.tables();
        } catch (Exception e) {
            throw new RuntimeException("read parse fail", e);
        }
    }
}
