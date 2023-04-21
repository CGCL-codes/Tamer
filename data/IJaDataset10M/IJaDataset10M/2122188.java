package org.dita2indesign.cmdline;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dita2indesign.indesign.builders.InDesignFromDitaMapBuilder;
import org.dita2indesign.indesign.builders.Map2InDesignOptions;
import org.dita2indesign.indesign.inx.model.InDesignDocument;
import org.dita2indesign.indesign.inx.writers.InxWriter;

/**
 * Command-line utility for transforming DITA map files into InDesign
 * INX documents.
 */
public class Map2InDesign {

    /**
	 * 
	 */
    private static final String XSLT_OPTION_ONE_CHAR = "x";

    /**
	 * 
	 */
    private static final String IDTEMPLATE_OPTION_ONE_CHAR = "t";

    /**
	 * 
	 */
    private static final String STYLES_OPTION_ONE_CHAR = "s";

    /**
	 * 
	 */
    private static final String OUTPUT_OPTION_ONE_CHAR = "o";

    /**
	 * 
	 */
    private static final String INPUT_OPTION_ONE_CHAR = "i";

    private static Log log = LogFactory.getLog(Map2InDesign.class);

    /**
	 * 
	 */
    private static void printUsage() {
        String usage = "Usage:\n\n" + "\t" + Map2InDesign.class.getSimpleName() + " " + " {mapFile} " + " [{resultDir} | {resultInxFile}]" + "\n\n" + "Where:\n\n" + " mapFile: The DITA map file to be transformed.\n" + " resultInxFile: The directory to hold the resulting InDesign output. INX file will be named from the input map filename.\n" + " resultInxFile: The filename to use for the output InDesign INX file. By default, uses the map filename.\n" + "\n" + "NOTE: Existing result files are silently overwritten.";
        System.err.println(usage);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Options cmdlineOptions = configureOptions();
        CommandLineParser parser = new PosixParser();
        CommandLine line = null;
        try {
            line = parser.parse(cmdlineOptions, args);
        } catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Map2InDesign.class.getSimpleName(), cmdlineOptions);
            System.exit(-1);
        }
        String mapFilename = line.getOptionValue(INPUT_OPTION_ONE_CHAR);
        File mapFile = new File(mapFilename);
        checkExistsAndCanReadSystemExit(mapFile);
        File resultDir = null;
        File resultFile = null;
        String resultFilename = null;
        String namePart = mapFile.getName();
        namePart = namePart.substring(0, namePart.lastIndexOf("."));
        resultFilename = namePart + ".inx";
        if (line.hasOption(OUTPUT_OPTION_ONE_CHAR)) {
            File tempFile = new File(line.getOptionValue(OUTPUT_OPTION_ONE_CHAR));
            checkExistsAndCanReadSystemExit(tempFile);
            if (tempFile.isDirectory()) {
                resultDir = tempFile;
                if (!resultDir.canWrite()) {
                    System.err.println("Output directory \"" + resultDir.getAbsolutePath() + "\" is not writable");
                    System.exit(-1);
                }
                resultFile = new File(resultDir, resultFilename);
            } else {
                resultFile = tempFile;
            }
        } else {
            resultFile = new File(mapFile.getParentFile(), resultFilename);
        }
        checkExistsAndCanReadSystemExit(resultFile.getParentFile());
        Map2InDesign app = new Map2InDesign();
        Map2InDesignOptions options = new Map2InDesignOptions();
        options.setStyleCatalogUrl(line.getOptionValue(STYLES_OPTION_ONE_CHAR));
        try {
            options.setXsltUrl(line.getOptionValue(XSLT_OPTION_ONE_CHAR));
        } catch (Exception e) {
            System.err.println("Failed to set XSLT URL: " + e.getMessage());
            System.exit(-1);
        }
        options.setDebug(true);
        options.setResultFile(resultFile);
        try {
            options.setInDesignTemplate(new URL(IDTEMPLATE_OPTION_ONE_CHAR));
        } catch (MalformedURLException e) {
            System.err.println("Failed to set InDesign template URL: " + e.getMessage());
            System.exit(-1);
        }
        try {
            log.info("Generating InDesign document \"" + resultFile.getAbsolutePath() + "\"...");
            app.generateInDesign(mapFile, resultFile, options);
            System.out.println("InDesign generation complete, result is in \"" + resultFile.getAbsolutePath() + "\"");
        } catch (Exception e) {
            System.err.println("InDesign generation failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
	 * @return
	 */
    private static Options configureOptions() {
        Options options = new Options();
        Option opt = null;
        options.addOption(INPUT_OPTION_ONE_CHAR, "map", true, "(Input) The top-level map from which to generate an InDesign document");
        opt = options.getOption(INPUT_OPTION_ONE_CHAR);
        opt.setRequired(true);
        options.addOption(OUTPUT_OPTION_ONE_CHAR, "out", true, "(Output) The name of the output directory and, optionally, InDesign file to generate.");
        opt = options.getOption(OUTPUT_OPTION_ONE_CHAR);
        opt.setRequired(false);
        options.addOption(XSLT_OPTION_ONE_CHAR, "xslt", true, "The XSLT to apply to the map to generate InCopy articles from the referenced topics");
        opt = options.getOption(XSLT_OPTION_ONE_CHAR);
        opt.setRequired(true);
        options.addOption(STYLES_OPTION_ONE_CHAR, "styles", true, "The style catalog to be used by the InCopy article generation transform.");
        opt = options.getOption(STYLES_OPTION_ONE_CHAR);
        opt.setRequired(true);
        options.addOption(IDTEMPLATE_OPTION_ONE_CHAR, "idtemplate", true, "The InDesign template to use in generating the result InDesign document.");
        opt = options.getOption(IDTEMPLATE_OPTION_ONE_CHAR);
        opt.setRequired(true);
        return options;
    }

    /**
	 * @param mapFile
	 * @param resultInxFile
	 * @param options
	 * @throws Exception 
	 */
    private void generateInDesign(File mapFile, File resultInxFile, Map2InDesignOptions options) throws Exception {
        InDesignFromDitaMapBuilder builder = new InDesignFromDitaMapBuilder();
        InDesignDocument doc = builder.buildMapDocument(mapFile.toURL(), options);
        InxWriter writer = new InxWriter(resultInxFile);
        writer.write(doc);
    }

    private static void checkExistsAndCanReadSystemExit(File file) {
        try {
            checkExistsAndCanRead(file);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
	 * @param file
	 */
    private static void checkExistsAndCanRead(File file) {
        if (!file.exists()) {
            throw new RuntimeException("File " + file.getAbsolutePath() + " does not exist.");
        }
        if (!file.canRead()) {
            throw new RuntimeException("File " + file.getAbsolutePath() + " exists but cannot be read.");
        }
    }
}
