package org.edits.openlp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.edits.CommandExecutor;
import org.edits.Edits;
import org.edits.FileLoader;
import org.edits.annotation.TextAnnotator;
import org.edits.etaf.AnnotatedText;
import org.edits.etaf.EntailmentPair;
import org.edits.processor.EDITSIterator;
import org.edits.processor.EDITSListIterator;
import org.edits.processor.EntailmentPairSource;
import org.edits.processor.FileEPTarget;
import org.edits.processor.Target;

public class EditsOpenNLP extends TextAnnotator {

    public EditsOpenNLP(String tokenizer, String tagger, String parser) {
    }

    @Override
    public void run(List<AnnotatedText> list) throws Exception {
    }

    public static void main(String[] args) {
        try {
            mainEX(args);
        } catch (Exception e) {
            Logger logger = Logger.getLogger("edits.main");
            logger.debug(e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void mainEX(String[] args) throws Exception {
        new Edits();
        Options oxsx = CommandExecutor.defaultOptions();
        Options oxs = new Options();
        for (Object x : oxsx.getOptions()) {
            Option xx = (Option) x;
            if (xx.getLongOpt().equals("configuration")) continue;
            if (xx.getLongOpt().equals("model")) {
                xx.setDescription("OpenNLP model");
                xx.setRequired(true);
            }
            if (xx.getLongOpt().equals("output")) {
                xx.setDescription("Annotated dataset path");
                xx.setRequired(true);
            }
            oxs.addOption(xx);
        }
        if (args.length == 0) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            HelpFormatter formatter = new HelpFormatter();
            printWriter.println("Tree Tagger Edits Script");
            printWriter.println("EDITS - Edit Distance Textual Entailment Suite - " + Edits.VERSION);
            formatter.printUsage(printWriter, HelpFormatter.DEFAULT_WIDTH, "edits-textpro");
            formatter.printOptions(printWriter, 120, oxs, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD);
            System.out.println(result.toString());
            return;
        }
        CommandLine line = null;
        try {
            line = new BasicParser().parse(oxs, args);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            return;
        }
        Edits.setVerbose(line.hasOption("verbose"));
        String path = line.getOptionValue("model");
        TextAnnotator annotator = new EditsOpenNLP("", "", "");
        EDITSIterator<EntailmentPair> source = null;
        Target<EntailmentPair> target = new FileEPTarget(line.getOptionValue("output"), true);
        if (!line.hasOption("load_in_memory") || !line.getOptionValue("load_in_memory").equals("false")) source = new EDITSListIterator<EntailmentPair>(FileLoader.loadEntailmentCorpus(line.getArgs()[0]).getPair()); else source = new EntailmentPairSource(line.getArgs()[0]);
        annotator.annotate(source, target);
        target.close();
    }
}
