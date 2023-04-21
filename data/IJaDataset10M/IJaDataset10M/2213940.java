package org.jcvi.fluvalidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.LevelRangeFilter;
import org.jcvi.common.core.seq.fastx.fasta.nt.DefaultNucleotideSequenceFastaRecord;
import org.jcvi.common.core.seq.fastx.fasta.nt.NucleotideSequenceFastaRecord;
import org.jcvi.common.log.StandardLoggingLayout;
import org.jcvi.glk.Extent;
import org.jcvi.glk.ExtentAttribute;
import org.jcvi.glk.ExtentAttributeType;
import org.jcvi.glk.ExtentType;
import org.jcvi.glk.elvira.ExtentTypeName;
import org.jcvi.glk.helpers.GLKHelper;
import org.jcvi.glk.helpers.HibernateGLKHelper;
import org.jcvi.glk.session.JDBCSessionExtension;
import org.jcvi.glk.session.SessionManager;
import org.jcvi.glk.session.SessionService;
import org.jcvi.glk.session.SybaseDatabaseConfig;
import org.jcvi.glk.session.UnpooledSessionExtension;

/**
 * 
 *
 * @author jsitz@jcvi.org
 */
public class ValidationFilter {

    private static final String DEFAULT_USER = "access";

    private static final String DEFAULT_PASS = ValidationFilter.DEFAULT_USER;

    private static final int BASE_SEGMENT_COUNT = 8;

    /**
     * @param args
     */
    public static void main(String[] args) {
        ValidationFilter.setupLogging();
        final Logger syslogger = Logger.getLogger("System");
        final String serverName = ValidationFilter.getResourceString("validationFilter.server", "Database Server", "SYBTIGR");
        final String dbName = ValidationFilter.getResourceString("validationFilter.db", "Database Name", null);
        final String username = ValidationFilter.getResourceString("validationFilter.user", "Username", ValidationFilter.DEFAULT_USER);
        final String password = ValidationFilter.getResourceString("validationFilter.pass", "Password", ValidationFilter.DEFAULT_PASS, false);
        final SessionService sessionService = SessionManager.getImplementation();
        sessionService.setDatabaseConfig(new SybaseDatabaseConfig(serverName, dbName));
        sessionService.setAuthentication(username, password);
        sessionService.useSQLEcho(false);
        sessionService.addExtension(new UnpooledSessionExtension());
        sessionService.addExtension(new JDBCSessionExtension());
        final HibernateGLKHelper glkHelper = new HibernateGLKHelper(sessionService.getSessionFactory().openSession());
        final String samplePath = ValidationFilter.getResourceString("validationFilter.sampleFile");
        final File sampleFile = new File(samplePath);
        if (!sampleFile.exists()) {
            System.err.println("The sample list file (" + samplePath + ") does not exist.");
            System.exit(1);
        }
        if (!sampleFile.canRead()) {
            System.err.println("The sample list file (" + samplePath + ") is not readable.");
            System.exit(1);
        }
        syslogger.info("Scanning list file...");
        try {
            final String outputPrefix = ValidationFilter.getResourceString("validationFilter.outputPrefix");
            final ValidationFilter validator = new ValidationFilter(glkHelper, outputPrefix);
            validator.setLogger(syslogger);
            final Scanner listReader = new Scanner(sampleFile);
            while (listReader.hasNextInt()) {
                final int refId = listReader.nextInt();
                validator.filterSample(refId);
            }
            validator.end();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String getResourceString(String id) {
        return System.getProperty(id);
    }

    private static String getResourceString(String id, String prompt, String defaultValue) {
        return ValidationFilter.getResourceString(id, prompt, defaultValue, true);
    }

    private static String getResourceString(String id, String prompt, String defaultValue, boolean echo) {
        String value = System.getProperty(id);
        if (value != null) return value;
        final String promptFormat = "%s [%s]: ";
        if (System.console() != null) {
            if (echo) {
                value = System.console().readLine(promptFormat, prompt, defaultValue).trim();
            } else {
                value = new String(System.console().readPassword(promptFormat, prompt, defaultValue)).trim();
            }
        } else {
            System.out.printf(promptFormat, prompt, defaultValue);
            try {
                value = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
            } catch (final IOException e) {
                return defaultValue;
            }
        }
        if (value.length() < 1) return defaultValue;
        return value;
    }

    /**
     * Obtains the root {@link Logger} from Log4J.  Global settings should be
     * applied to this Logger, rather than the local Logger so that the
     * settings are similarly applied to the Loggers from other subsystems
     * (Hibernate et al).
     *
     * @return The top-level {@link Logger}.
     */
    private static Logger getRootLogger() {
        return Logger.getRootLogger();
    }

    /**
     * Initializes the local logging system.  This accomplishes the same task
     * as Log4J's <code>BasicConfigurator</code> or <code>DOMConfigurator</code>
     * but does not use a configuration file.  Instead, the rules are applied
     * in code.
     */
    private static void setupLogging() {
        try {
            final Appender consoleLog = new ConsoleAppender(StandardLoggingLayout.global(), "System.out");
            final Appender fileLog = new FileAppender(StandardLoggingLayout.global(), "validationFilter.log");
            final LevelRangeFilter consoleFilter = new LevelRangeFilter();
            consoleFilter.setLevelMax(Level.FATAL);
            consoleFilter.setLevelMin(ValidationFilter.getMinimumLogLevel());
            consoleLog.addFilter(consoleFilter);
            fileLog.addFilter(consoleFilter);
            ValidationFilter.getRootLogger().setLevel(Level.DEBUG);
            ValidationFilter.getRootLogger().addAppender(fileLog);
            if (System.getProperty("validationFilter.consoleLog") != null) {
                ValidationFilter.getRootLogger().addAppender(consoleLog);
            }
            Logger.getLogger("org.hibernate").setLevel(Level.WARN);
            Logger.getLogger("com.opensymphony.oscache").setLevel(Level.WARN);
        } catch (final IOException e) {
            throw new RuntimeException("Error while initializing log file.", e);
        }
    }

    private static Level getMinimumLogLevel() {
        return Level.toLevel(System.getProperty("plateToStudy.logLevel"), Level.INFO);
    }

    private final ValidationEvaluator evaluator;

    private final GLKHelper glkHelper;

    private final PrintStream pass;

    private final PrintStream fail;

    private final PrintStream manifest;

    private final List<Integer> sampleCountBySegment;

    private int invalidSamples;

    private final ExtentType sampleType;

    private final ExtentAttributeType speciesCodeAttr;

    private Logger logger;

    /**
     * Creates a new <code>FluValidator</code>.
     */
    public ValidationFilter(GLKHelper glkHelper, String outputPrefix) throws FileNotFoundException {
        super();
        this.evaluator = new PartialSampleEvaluator();
        this.glkHelper = glkHelper;
        this.pass = new PrintStream(outputPrefix + ".passed.fasta");
        this.fail = new PrintStream(outputPrefix + ".failed.fasta");
        this.manifest = new PrintStream(outputPrefix + ".manifest");
        this.sampleCountBySegment = new ArrayList<Integer>(ValidationFilter.BASE_SEGMENT_COUNT);
        while (this.sampleCountBySegment.size() < ValidationFilter.BASE_SEGMENT_COUNT + 1) {
            this.sampleCountBySegment.add(new Integer(0));
        }
        this.invalidSamples = 0;
        this.sampleType = this.glkHelper.getExtentType(ExtentTypeName.SAMPLE);
        this.speciesCodeAttr = this.glkHelper.getExtentAttributeType("species_code");
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void end() {
        this.reportStats(this.manifest);
        this.pass.flush();
        this.fail.flush();
        this.manifest.flush();
        this.pass.close();
        this.fail.close();
        this.manifest.close();
    }

    public String getSpeciesCode(Extent sample) {
        final ExtentAttribute speciesCode = sample.getAttribute(this.speciesCodeAttr);
        if (speciesCode == null) {
            return "(No species code found)";
        }
        return speciesCode.getValue();
    }

    public void reportStats(PrintStream output) {
        output.println();
        output.println("Sampe Counts by Valid Segments:");
        output.printf("%16s : %8s\n", "Segments Valid", "Samples");
        for (int i = this.sampleCountBySegment.size() - 1; i >= 0; i--) {
            final Integer count = this.sampleCountBySegment.get(i);
            output.printf("%16d : %8d\n", i, count);
        }
        output.printf("%16s : %8d\n", "No Assemblies", this.invalidSamples);
    }

    public void filterSample(int sampleId) {
        final Map<String, NucleotideSequenceFastaRecord> printableRecords = new HashMap<String, NucleotideSequenceFastaRecord>();
        final Map<String, Integer> segmentCounts = new HashMap<String, Integer>();
        final Logger logger = Logger.getLogger(String.valueOf(sampleId));
        logger.info("Filtering sample " + sampleId);
        final ValidationRecordSet records = new ValidationRecordSet();
        records.loadSnapshot(this.glkHelper, sampleId);
        if (records.getRecordCount() < 1) {
            this.invalidSamples++;
            logger.warn("No contigs found for sample " + sampleId);
            this.manifest.printf("*** Sample %d: No Assemblies Found\n", sampleId);
            this.manifest.println();
            return;
        }
        final Extent sample = this.glkHelper.getExtent(this.sampleType, String.valueOf(sampleId));
        final String speciesCode = this.getSpeciesCode(sample);
        this.buildRecordLists(printableRecords, segmentCounts, records, sample, speciesCode);
        final NCBIValidationEngine validator = new NCBIValidationEngine(this.getLogger());
        try {
            int segmentsPass = 0;
            int segmentsFail = 0;
            final List<String> manifestPass = new ArrayList<String>();
            final List<String> manifestFail = new ArrayList<String>();
            final SampleValidationResult sampleResult = validator.validate(records);
            for (final SegmentValidationResult segmentResult : sampleResult) {
                for (final ValidationResult result : segmentResult) {
                    this.filterResult(printableRecords, manifestPass, manifestFail, result);
                }
                if (this.evaluator.evaluate(segmentResult).isAtLeast(ValidationState.PASSABLE)) {
                    segmentsPass++;
                } else {
                    segmentsFail++;
                }
            }
            this.recordSegmentStats(segmentsPass, segmentsFail);
            Collections.sort(manifestPass);
            Collections.sort(manifestFail);
            this.manifest.printf("*** Sample %s: %d of %d segments pass. (%d of %d assemblies)\n", sampleId, segmentsPass, sampleResult.getSegmentCount(), manifestPass.size(), manifestPass.size() + manifestFail.size());
            for (final String line : manifestPass) {
                this.manifest.println(line);
            }
            for (final String line : manifestFail) {
                this.manifest.println(line);
            }
        } catch (final ValidationTimeoutException e) {
            System.err.println("-- The remote service took too long to respond.");
            System.err.println("   Skipping sample " + sampleId);
            this.manifest.printf("*** Sample %s: No response from validation server.\n", sampleId);
        } catch (final ValidationCommunicationException e) {
            System.err.println("-- There is a problem communicating with the remote service.");
            System.err.println("   Skipping sample " + sampleId);
            System.err.println("   (Error: " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getLocalizedMessage() + ")");
            this.manifest.printf("*** Sample %s: Error communicating with service: %s (%s).\n", sampleId, e.getCause().getLocalizedMessage(), e.getCause().getClass().getSimpleName());
        } catch (final ValidationException e) {
            throw new RuntimeException("Error while validating records.", e);
        } finally {
            this.manifest.println();
        }
    }

    private void recordSegmentStats(int passedSegments, int failedSegments) {
        final int maxSegments = passedSegments + failedSegments;
        while (this.sampleCountBySegment.size() < maxSegments) {
            this.sampleCountBySegment.add(new Integer(0));
        }
        Integer sampleCount = this.sampleCountBySegment.get(passedSegments);
        sampleCount++;
        this.sampleCountBySegment.set(passedSegments, sampleCount);
    }

    /**
     * @param printableRecords
     * @param manifestPass
     * @param manifestFail
     * @param result
     */
    private void filterResult(final Map<String, NucleotideSequenceFastaRecord> printableRecords, final List<String> manifestPass, final List<String> manifestFail, final ValidationResult result) {
        ValidationState resultState = ValidationState.INVALID;
        final NucleotideSequenceFastaRecord printableRecord = printableRecords.get(result.getIdentifier());
        if (printableRecord == null) {
            this.getLogger().warn("Failed to find printable record for " + result.getIdentifier());
        }
        if (this.evaluator.evaluate(result).isAtLeast(ValidationState.PASSABLE)) {
            this.pass.print(printableRecord);
            resultState = ValidationState.VALID;
        } else {
            this.fail.print(printableRecord);
        }
        final String manifestLine = String.format("%s: %s [ %d bases ] : Assembly %s", resultState.toString(), printableRecord.getId(), printableRecord.getSequence().getLength(), result.getIdentifier());
        if (resultState.equals(ValidationState.VALID)) {
            manifestPass.add(manifestLine);
        } else {
            manifestFail.add(manifestLine);
        }
    }

    /**
     * @param printableRecords
     * @param segmentCounts
     * @param records
     * @param sample
     * @param speciesCode
     */
    private void buildRecordLists(final Map<String, NucleotideSequenceFastaRecord> printableRecords, final Map<String, Integer> segmentCounts, final ValidationRecordSet records, final Extent sample, final String speciesCode) {
        for (final NucleotideSequenceFastaRecord record : records) {
            final String id = record.getId();
            final String segment = record.getComment();
            final int nextSegmentIndex = (segmentCounts.containsKey(segment)) ? segmentCounts.get(segment).intValue() : 0;
            final String recordName = sample.getReference() + "_" + segment + "_" + nextSegmentIndex;
            final String recordComment = id + " " + speciesCode;
            final NucleotideSequenceFastaRecord printableRecord = new DefaultNucleotideSequenceFastaRecord(recordName, recordComment, record.getSequence());
            printableRecords.put(id, printableRecord);
            segmentCounts.put(segment, Integer.valueOf(nextSegmentIndex + 1));
        }
    }
}
