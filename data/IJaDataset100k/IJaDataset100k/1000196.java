package org.mitre.ovalutils.splitter.xccdf;

import java.util.*;
import org.mitre.ovalutils.splitter.common.*;

/**
 *
 * @author BWORRELL
 */
public class RuleExtractor extends AbsExtractor {

    private static final int EXIT_SUCCESS = 0, EXIT_FAILURE = 1;

    private static String output = "rule_extractor_results.xml", input = "", id = "", xsl = "";

    public RuleExtractor(String inputPath, String xslPath, String idToExtract, String outputPath) {
        super(inputPath, xslPath, idToExtract);
        this.setOutputPath(outputPath);
    }

    public RuleExtractor() {
        super(null, null, null);
    }

    public void setOutputPath(String path) {
        RuleExtractor.output = path;
    }

    @Override
    public String getOutputPath() {
        return RuleExtractor.output;
    }

    @Override
    public Map<String, Object> getXslParameters() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("desiredRuleId", super.getId());
        return params;
    }

    /**
   * Prints out a usage statement to the user
   */
    private static void usage() {
        System.out.print("XCCDF Rule Extractor: Extracts a rule from XCCDF document\n" + "Usage: > [options] \n\n" + "Options:\n" + "\t-i XCCDF Document to import\n" + "\t-o output file (optional)\n" + "\t-r id to extract\n" + "\t-x xsl file\n" + "\t-h usage\n\n");
    }

    /**
   * Parses the command line switches
   * @param args
   * @throws java.lang.Exception
   */
    private static void processCommandLine(String[] args) {
        String arg = null, val = null;
        boolean hasInput = false, hasId = false, hasXsl = false;
        for (int i = 0; i < args.length; i++) {
            arg = args[i];
            val = "";
            if (args.length > i + 1) val = args[++i];
            if (arg.compareToIgnoreCase("-h") == 0) {
                RuleExtractor.usage();
                System.exit(RuleExtractor.EXIT_SUCCESS);
            } else if (arg.compareToIgnoreCase("-i") == 0) {
                RuleExtractor.input = val;
                hasInput = true;
            } else if (arg.compareToIgnoreCase("-o") == 0) {
                RuleExtractor.output = val;
            } else if (arg.compareToIgnoreCase("-r") == 0) {
                RuleExtractor.id = val;
                hasId = true;
            } else if (arg.compareToIgnoreCase("-x") == 0) {
                RuleExtractor.xsl = val;
                hasXsl = true;
            } else {
                RuleExtractor.usage();
                System.exit(RuleExtractor.EXIT_FAILURE);
            }
        }
        if (hasInput == false || hasId == false || hasXsl == false) {
            RuleExtractor.usage();
            System.exit(RuleExtractor.EXIT_FAILURE);
        }
    }

    public static void main(String[] args) {
        RuleExtractor.processCommandLine(args);
        RuleExtractor extractor = new RuleExtractor(RuleExtractor.input, RuleExtractor.xsl, RuleExtractor.id, RuleExtractor.output);
        try {
            extractor.doExtract();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
