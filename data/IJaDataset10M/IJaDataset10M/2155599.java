package opennlp.grok.preprocess.chunk;

import java.io.*;
import java.util.zip.*;
import opennlp.common.english.*;
import opennlp.maxent.*;
import opennlp.maxent.io.*;
import java.util.*;
import opennlp.common.util.*;

/**
 * A shallow parser for English
 * (based on POSTaggerME)
 *
 * @author      Joerg Tiedemann
 * @version     $Revision: 1.2 $, $Date: 2002/11/08 17:57:10 $
 */
public class EnglishChunkerME extends ChunkerME {

    private static final String modelFile = "data/EnglishChunker.bin.gz";

    /**
     * No-arg constructor which loads the English chunker model
     * transparently.
     */
    public EnglishChunkerME() {
        super(getModel(modelFile), new ChunkerContextGenerator(new BasicEnglishAffixes()));
        _useClosedClassTagsFilter = true;
        _closedClassTagsFilter = new EnglishClosedClassTags();
    }

    private static MaxentModel getModel(String name) {
        try {
            return new BinaryGISModelReader(new DataInputStream(new GZIPInputStream(new BufferedInputStream(EnglishChunkerME.class.getResourceAsStream(name))))).getModel();
        } catch (IOException E) {
            E.printStackTrace();
            return null;
        }
    }

    public static ArrayList convertInputLine(String s) {
        ArrayList tokens = new ArrayList();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return tokens;
    }

    /**
     * <p>Chunks a string passed in on the command line.
     *
     */
    public static void main(String[] args) throws IOException {
        if (args[0].equals("-test")) {
            System.out.println(new EnglishChunkerME().tag(args[1]));
        } else {
            TrainEval.run(args, new EnglishChunkerME());
        }
    }
}
