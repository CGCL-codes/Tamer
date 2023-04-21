package de.cologneintelligence.fitgoodies.runners;

import java.io.File;
import fit.Counts;

/**
 * This runner processes a single file. No additional report is generated.
 *
 * @author jwierum
 * @version $Id: FileRunner.java 46 2011-09-04 14:59:16Z jochen_wierum $
 */
public final class FileRunner {

    private FileRunner() {
    }

    /**
	 * Entry point.
	 * Takes 2 or 3 arguments, either the input file and the output file,
	 * or the input file, the output file and the encoding. If the encoding
	 * is omitted, utf-8 is used.
	 *
	 * @param args program parameters
	 */
    public static void main(final String[] args) {
        if (args.length < 2) {
            final String error = "Usage:\n" + "fitgoodies.runners.FileRunner inputfile outputfile [encoding]";
            System.err.println(error);
            throw new RuntimeException(error);
        }
        try {
            String encoding = "utf-8";
            if (args.length > 2) {
                encoding = args[2];
            }
            Runner runner = new FitFileRunner();
            args[0] = args[0].replace('/', File.separatorChar).replace('\\', File.separatorChar);
            args[1] = args[1].replace('/', File.separatorChar).replace('\\', File.separatorChar);
            runner.setEncoding(encoding);
            Counts result = runner.run(args[0], args[1]);
            System.out.println(result);
            if (result != null && (result.exceptions > 0 || result.wrong > 0)) {
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
