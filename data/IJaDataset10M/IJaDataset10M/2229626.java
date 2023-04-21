package ogdl.util;

import ogdl.reference.*;
import java.io.*;

/**
 * Support class for processing a list of files or stdin.
 *
 * @author didge
 */
public class OgdlFileProcessorSupport {

    public OgdlFileProcessorSupport() {
    }

    /**
     * Called for each file.
     * @param reader
     * @param fileName the name of file or <tt>"&lt;stdin&gt;"</tt>
     * @throws Exception
     */
    public void processFile(Reader reader, String fileName) throws Exception {
    }

    /**
     * Process the list of files.
     *
     * @param args an array of file names (if null, then <tt>"&lt;stdin&gt;"</tt> is assumed
     */
    public void processFiles(String[] args) {
        if (args == null) {
            try {
                InputStreamReader reader = new InputStreamReader(System.in);
                processFile(reader, "<stdin>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < args.length; ++i) {
                try {
                    FileReader reader = new FileReader(args[i]);
                    processFile(reader, args[i]);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
    }
}
