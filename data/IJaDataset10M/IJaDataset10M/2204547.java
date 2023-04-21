package net.sourceforge.jinit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A simplistic template engine. Basically replaces any 
 * <code>${var}</code> references with actual values. All references that 
 * are supposed to be substituted are added to a context object. If a 
 * reference is found and it is not in the
 * context, it is simply ignored, which makes this engine ideal to use for
 * Apache Ant build scripts where some references should be resolved but not
 * all.<P>
 * The basic working is as follows: Suppose you have a file with:<P>
 * <CODE>&nbsp;&nbsp;&nbsp; @(#) ${filename} ${date} ${time}</CODE><P>
 * And another with :<P>
 * <CODE>
 * &nbsp;&nbsp;&nbsp; filename=Test.java<BR>
 * &nbsp;&nbsp;&nbsp; date=2004-10-31
 * </CODE><P>
 * And you run the template engine on the files, the first file will have the
 * following:<P>
 * <CODE>&nbsp;&nbsp;&nbsp; @(#) Test.java 2004-10-31 ${time}</CODE>
 * @author <A HREF="mailto:trevor@nephila.co.za">Trevor Miller</A>
 * @version 0.2
 * @since 0.1
 */
public class TemplateEngine {

    /**
     * Class has static interface and should not be instanced
     * so we make the constructor private!
     */
    private TemplateEngine() {
    }

    /**
     * Merges the data in the context with the file given by resolving
     * all references contained in the context and substituting their
     * values in the file.
     * @param context The context containing reference values
     * @param file The file to merge with
     * @throws Exception If an error occurs
     */
    public static void merge(Hashtable context, File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        byte[] buffer = new byte[fin.available()];
        fin.read(buffer);
        String source = new String(buffer);
        Enumeration keys = context.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            source = processKey(key, (String) context.get(key), source);
        }
        FileOutputStream fout = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(fout);
        pw.println(source);
        pw.close();
    }

    /**
     * Merges the data in the context with the source given by resolving
     * all references contained in the context and substituting their
     * values in the source string.
     * @param context The context containing reference values
     * @param source The source string
     * @return The merged string
     */
    public static String merge(Hashtable context, String source) {
        Enumeration keys = context.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            source = processKey(key, (String) context.get(key), source);
        }
        return source;
    }

    /**
     * process a specific key (reference) in the context. Method searches
     * the source for all occurrences of the key and then replaces it with
     * the matching value from the context.
     * @param key
     * @param value
     * @param source
     * @return
     */
    private static String processKey(String key, String value, String source) {
        int index = 0;
        int start = 0;
        int end = 0;
        String symbol = "${" + key + "}";
        index = source.indexOf(symbol, end);
        while (index >= 0) {
            start = index;
            end = start + symbol.length();
            String first = source.substring(0, start);
            String last = source.substring(end);
            source = first + value + last;
            index = source.indexOf(symbol, end);
        }
        return source;
    }
}
