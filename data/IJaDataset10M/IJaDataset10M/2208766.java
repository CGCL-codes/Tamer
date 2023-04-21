package org.jcvi.elvira.viral.nextgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jcvi.common.command.CommandLineOptionBuilder;
import org.jcvi.common.command.CommandLineUtils;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.assembly.ace.AceContig;
import org.jcvi.common.core.assembly.ace.AceContigDataStore;
import org.jcvi.common.core.assembly.ace.IndexedAceFileDataStore;
import org.jcvi.common.core.assembly.ace.consed.ConsedNavigationWriter;
import org.jcvi.common.core.assembly.ace.consed.ConsensusNavigationElement;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.io.IOUtil;

public class DetectIndelsInAceContigWorker {

    static final int MIN_INDEL_LENGTH = 4;

    static final float MIN_VARIANT_PERCENT = .05F;

    /**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws DataStoreException 
	 */
    public static void main(String[] args) throws FileNotFoundException, IOException, DataStoreException {
        Options options = new Options();
        options.addOption(new CommandLineOptionBuilder("a", "path to ace file (required)").longName("ace").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("c", "contig id to find errors for (required)").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("o", "path to output file (if not used output printed to STDOUT)").longName("out").build());
        options.addOption(new CommandLineOptionBuilder("nav", "path to optional consed navigation file to see abacus errors easier in consed").build());
        options.addOption(new CommandLineOptionBuilder("min_var_length", "length of the minimum variant GAPPED read length to consider.  This has no bearing " + "on the final consensus length since an assembly could have gaps at those consensus positions.  (default = " + MIN_INDEL_LENGTH + ")").build());
        options.addOption(new CommandLineOptionBuilder("min_var_percent", String.format("minimum percent coverage of variants vs consensus called in the slice to be considered." + " (default = %.2f)", MIN_VARIANT_PERCENT)).build());
        options.addOption(CommandLineUtils.createHelpOption());
        if (CommandLineUtils.helpRequested(args)) {
            printHelp(options);
            System.exit(0);
        }
        try {
            CommandLine commandLine = CommandLineUtils.parseCommandLine(options, args);
            String contigId = commandLine.getOptionValue("c");
            final PrintWriter out = getOutputWriter(commandLine);
            final ConsedNavigationWriter consedNavWriter;
            File aceFile = new File(commandLine.getOptionValue("a"));
            if (commandLine.hasOption("nav")) {
                consedNavWriter = ConsedNavigationWriter.createPartial(new FileOutputStream(commandLine.getOptionValue("nav")));
            } else {
                consedNavWriter = null;
            }
            final IndelDetector2 indelErrorFinder = new IndelDetector2(.05F, 4);
            try {
                AceContigDataStore datastore = IndexedAceFileDataStore.create(aceFile);
                Iterator<String> contigIds = datastore.idIterator();
                while (contigIds.hasNext()) {
                    String id = contigIds.next();
                    if (contigId.equals(id)) {
                        try {
                            findErrorsIn(indelErrorFinder, datastore.get(contigId), out, consedNavWriter);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
            } finally {
                IOUtil.closeAndIgnoreErrors(out, consedNavWriter);
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp(options);
            System.exit(1);
        }
    }

    private static void findErrorsIn(IndelDetector2 indelDetector, AceContig contig, PrintWriter out, ConsedNavigationWriter consedNavigationWriter) throws IOException {
        String contigId = contig.getId();
        out.println(contig.getId());
        List<Range> errorRanges = indelDetector.findIndelErrors(contig);
        out.println("found " + errorRanges.size() + " abacus errors");
        for (Range errorRange : errorRanges) {
            Range residueBasedRange = errorRange;
            if (consedNavigationWriter != null) {
                consedNavigationWriter.writeNavigationElement(new ConsensusNavigationElement(contigId, errorRange, "CA abacus error"));
            }
            out.printf("abacus error range : %s%n", residueBasedRange);
        }
    }

    private static PrintWriter getOutputWriter(CommandLine commandLine) throws FileNotFoundException {
        if (commandLine.hasOption("o")) {
            return new PrintWriter(new File(commandLine.getOptionValue("o")));
        }
        return new PrintWriter(System.out) {

            @Override
            public void close() {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("findIndelErrors -a <ace file>", "Parse an ace file and write out ungapped consensus coordinates of abacus assembly errors", options, "Created by Danny Katzel");
    }
}
