package edu.usc.epigenome.uecgatk.benWalkers;

import java.util.*;
import java.util.regex.Pattern;
import net.sf.samtools.*;
import org.broadinstitute.sting.gatk.filters.MappingQualityReadFilter;
import org.broadinstitute.sting.gatk.io.StingSAMFileWriter;
import org.broadinstitute.sting.gatk.refdata.ReadMetaDataTracker;
import org.broadinstitute.sting.gatk.walkers.*;
import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.utils.text.TextFormattingUtils;
import org.broadinstitute.sting.commandline.*;

@Requires({ DataSource.READS })
public class BsmapPairedEndFixerWalker extends ReadWalker<SAMRecord, SAMFileWriter> {

    public static final String PROGRAM_RECORD_NAME = "UECGATK Fix BSmap paired end bitmap bits";

    protected static final String END1_SUFFIX = String.format("%c1", '/');

    protected static final String END2_SUFFIX = String.format("%c2", '/');

    @Argument(fullName = "output_bam", shortName = "outputBam", doc = "Please use --out instead", required = false)
    @Deprecated
    protected String outbam;

    @Output(doc = "The output BAM file", required = true)
    private StingSAMFileWriter OUTPUT_BAM = null;

    @Hidden
    @Argument(fullName = "no_pg_tag", shortName = "noPG", required = false, doc = "Don't output the usual PG tag in the recalibrated bam file header. FOR DEBUGGING PURPOSES ONLY. This option is required in order to pass integration tests.")
    private boolean NO_PG_TAG = false;

    @Hidden
    @Argument(fullName = "fail_with_no_eof_marker", shortName = "requireEOF", required = false, doc = "If no EOF marker is present in the covariates file, exit the program with an exception.")
    private boolean REQUIRE_EOF = false;

    private static final Pattern COMMENT_PATTERN = Pattern.compile("^#.*");

    protected static final String EOF_MARKER = "EOF";

    /**
     * Read in the recalibration table input file.
     * Parse the list of covariate classes used during CovariateCounterWalker.
     * Parse the CSV data and populate the hashmap.
     */
    public void initialize() {
        OUTPUT_BAM.setPresorted(false);
        final SAMFileHeader header = getToolkit().getSAMFileHeader().clone();
        if (!NO_PG_TAG) {
            final SAMProgramRecord programRecord = new SAMProgramRecord(PROGRAM_RECORD_NAME);
            final ResourceBundle headerInfo = TextFormattingUtils.loadResourceBundle("StingText");
            try {
                final String version = headerInfo.getString("org.broadinstitute.sting.gatk.version");
                programRecord.setProgramVersion(version);
            } catch (MissingResourceException e) {
            }
            StringBuffer sb = new StringBuffer();
            sb.append(getToolkit().createApproximateCommandLineArgumentString(getToolkit(), this));
            programRecord.setCommandLine(sb.toString());
            List<SAMProgramRecord> oldRecords = header.getProgramRecords();
            List<SAMProgramRecord> newRecords = new ArrayList<SAMProgramRecord>(oldRecords.size() + 1);
            for (SAMProgramRecord record : oldRecords) {
                if (!record.getId().startsWith(PROGRAM_RECORD_NAME)) newRecords.add(record);
            }
            newRecords.add(programRecord);
            header.setProgramRecords(newRecords);
            OUTPUT_BAM.writeHeader(header);
        }
    }

    /**
     * For each base in the read calculate a new recalibrated quality score and replace the quality scores in the read
     * @param refBases References bases over the length of the read
     * @param read The read to be recalibrated
     * @return The read with quality scores replaced
     */
    public SAMRecord map(ReferenceContext refBases, SAMRecord read, ReadMetaDataTracker metaDataTracker) {
        if (!read.getReadPairedFlag()) {
            System.err.printf("BsmapPairedEndFixer doesn't do anything for single end reads\n");
        } else {
            boolean trueSecondOfPair = getSecondOfPairFromReadname(read);
            read.setFirstOfPairFlag((trueSecondOfPair) ? false : true);
            read.setSecondOfPairFlag((trueSecondOfPair) ? true : false);
        }
        return read;
    }

    /**
     * Start the reduce with a handle to the output bam file
     * @return A FileWriter pointing to a new bam file
     */
    public SAMFileWriter reduceInit() {
        return OUTPUT_BAM;
    }

    /**
     * Output each read to disk
     * @param read The read to output
     * @param output The FileWriter to write the read to
     * @return The FileWriter
     */
    public SAMFileWriter reduce(SAMRecord read, SAMFileWriter output) {
        if (output != null) {
            output.addAlignment(read);
        }
        return output;
    }

    /**
     * Do nothing
     * @param output The SAMFileWriter that outputs the bam file
     */
    public void onTraversalDone(SAMFileWriter output) {
    }

    protected static boolean getSecondOfPairFromReadname(SAMRecord read) {
        boolean secondOfPair = false;
        String readName = read.getReadName();
        if (readName.endsWith(END1_SUFFIX)) {
            secondOfPair = false;
        } else if (readName.endsWith(END2_SUFFIX)) {
            secondOfPair = true;
        } else {
            System.err.println("Got a read that doesn't end with /1 or /2: " + readName + ".  Can't tell which end it is.");
            System.exit(1);
        }
        return secondOfPair;
    }
}
