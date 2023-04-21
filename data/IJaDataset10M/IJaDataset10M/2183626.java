package net.sf.opendf.cal.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import net.sf.opendf.cal.parser.Lexer;
import net.sf.opendf.cal.parser.Parser;
import net.sf.opendf.util.logging.Logging;
import net.sf.opendf.util.source.MultiErrorException;
import net.sf.opendf.util.xml.Util;
import net.sf.opendf.util.exception.ReportingExceptionHandler;
import org.w3c.dom.Document;

/**
 * 
 * @author jornj
 */
public class Cal2CalML {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            printUsage();
            return;
        }
        boolean verbose = true;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-q")) {
                verbose = false;
                continue;
            }
            try {
                if (verbose) Logging.user().info("Compiling '" + args[i] + "'...");
                compileSource(args[i]);
                if (verbose) Logging.user().info("done.");
            } catch (Exception e) {
                (new ReportingExceptionHandler()).process(e);
            }
        }
    }

    public static File compileSource(String fileName) throws MultiErrorException, FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(fileName);
        Lexer calLexer = new Lexer(inputStream);
        Parser calParser = new Parser(calLexer);
        Document doc;
        doc = calParser.parseActor(fileName);
        String result = "";
        result = Util.createXML(doc);
        File outputFile = new File(fileName + "ml");
        OutputStream os = new FileOutputStream(outputFile);
        PrintWriter pw = new PrintWriter(os);
        pw.print(result);
        pw.close();
        return outputFile;
    }

    private static void printUsage() {
        System.out.println("Cal2CalML <source> ...");
    }
}
