package cc.mallet.classify.tui;

import java.util.logging.*;
import java.io.*;
import cc.mallet.classify.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;
import cc.mallet.util.*;

/**
 * Diagnostic facilities for a vector file.
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */
public class Classifier2Info {

    private static Logger logger = MalletLogger.getLogger(Classifier2Info.class.getName());

    static CommandOption.File classifierFile = new CommandOption.File(Classifier2Info.class, "classifier", "FILE", true, new File("-"), "Read the saved classifier from this file.", null);

    public static void main(String[] args) throws FileNotFoundException, IOException {
        CommandOption.setSummary(Classifier2Info.class, "A tool for printing information about saved classifiers.");
        CommandOption.process(Classifier2Info.class, args);
        if (args.length == 0) {
            CommandOption.getList(Classifier2Info.class).printUsage(false);
            System.exit(-1);
        }
        Classifier classifier;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(classifierFile.value));
            classifier = (Classifier) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Couldn't read classifier " + classifierFile.value);
        }
        classifier.print();
    }
}
