package au.csiro.atnf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.csiro.atnf.cli.CommandLineOptionsComparator;
import au.csiro.atnf.cli.Command;
import au.csiro.atnf.util.AtnfIvoaException;
import au.csiro.atnf.util.Util;
import au.csiro.atnf.voresource.RpfitsConvert;

/**
 * Implements a command line interface for reading RPFITS files and writed the primary header to a file.
 * 
 * Copyright 2010, CSIRO Australia All rights reserved.
 * 
 * @author Vivek Pulukuri on 19/05/2010
 * @version $Revision: 6 $ $Date: 2011-05-08 20:17:14 -0400 (Sun, 08 May 2011) $
 */
public class RpfitsViewer implements Command {

    /**
     * Constant that defines the logger to be used.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RpfitsViewer.class);

    /**
     * The command name.
     */
    public static final String COMMAND_NAME = "viewrpfits";

    /**
     * Command options
     */
    private Options options;

    /**
     * Constructor
     */
    public RpfitsViewer() {
        this.options = createOptions();
    }

    /**
     * Creates the command line options.
     * 
     * @return the command line options.
     */
    @SuppressWarnings("static-access")
    @Override
    public Options createOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName("file").hasArg().withDescription(" 1: a rpfits file to be viewed.").isRequired(true).withLongOpt("inputFile").create("i"));
        options.addOption(OptionBuilder.withArgName("file").hasArg().withDescription(" 2: (optional)location of the header info to be written. If not specified, " + "input file location is selected with file name starting with header.").isRequired(false).withLongOpt("outputFile").create("o"));
        return options;
    }

    /**
     * Run the RpfitsViewer command.
     * 
     * @param args
     *            command line arguments.
     * @throws ParseException
     *             thrown if the command line arguments can not be parsed.
     * @throws IOException
     *             thrown if netCDF can to be written to or read from.
     */
    @Override
    public void execute(String[] args) throws ParseException, IOException {
        try {
            CommandLine parsedCommandLine = new GnuParser().parse(options, args);
            String inputFilenameArg = parsedCommandLine.hasOption("inputFile") ? parsedCommandLine.getOptionValue("inputFile") : "";
            String outputFilenameArg = parsedCommandLine.hasOption("outputFile") ? parsedCommandLine.getOptionValue("outputFile") : "";
            if (inputFilenameArg.equalsIgnoreCase(outputFilenameArg)) {
                throw new AtnfIvoaException("Output file can not overwrite input file.", new Throwable());
            }
            File inputFile = Util.getExistingFile(inputFilenameArg);
            outputFilenameArg = "".equalsIgnoreCase(outputFilenameArg) ? inputFile.getParent() + "\\header_" + inputFile.getName() + ".txt" : outputFilenameArg;
            String result = RpfitsConvert.fetchPrimaryHDUHeaderAsString(inputFile);
            Util.writeTextFile(new File(outputFilenameArg), result);
            System.out.println(result);
            LOG.info("Success");
            System.out.println("Success exported header to " + outputFilenameArg);
            System.exit(0);
        } catch (IllegalArgumentException iae) {
            LOG.error(iae.getMessage());
            System.out.println(iae.getMessage());
            System.out.println(getUsageString());
            System.exit(1);
        } catch (MissingOptionException moe) {
            LOG.error(moe.getMessage());
            System.out.println(moe.getMessage());
            System.out.println(getUsageString());
            System.exit(1);
        } catch (AtnfIvoaException aie) {
            LOG.error(aie.getMessage());
            System.out.println(aie.getMessage());
            System.out.println(getUsageString());
            System.exit(1);
        }
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * Returns the commands usage as a <code>String</code>
     * 
     * @param options
     *            the command line options.
     * @return the usage for this command.
     */
    @Override
    public String getUsageString() {
        String header = "RPfits header viewer";
        String footer = "\n Example: viewrpfits --inputFile 2001-12-12_1903.C787 [optional]--outputFile 2001-12-12_1903.txt \n" + "Will fetch the RPFITS file primary HDU heaser unit to  text file 2001-12-12_1903.C787.txt " + "If output file is not specified a deafult file with header_2001-12-12_1903.C787.txt is written  ";
        StringWriter sw = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new CommandLineOptionsComparator());
        formatter.printHelp(new PrintWriter(sw), PRINT_WIDTH, COMMAND_NAME, header, options, 0, 1, footer);
        return sw.toString();
    }

    /**
     * Performs validation of the command line options.
     * <p>
     * Performs the validation that can not be performed by simple "parsing" of the command line string.
     * 
     * @param command line args
     */
    @Override
    public String validCommand(String[] commandLine) {
        try {
            new GnuParser().parse(options, commandLine);
        } catch (ParseException ex) {
            return ex.getMessage();
        }
        return "";
    }
}
