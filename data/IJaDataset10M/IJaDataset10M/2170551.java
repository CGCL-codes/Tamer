package org.expasy.jpl.tools.dig;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.expasy.jpl.commons.app.AbstractApplicationParameters;
import org.expasy.jpl.commons.base.io.DecimalFormatFactory;
import org.expasy.jpl.core.mol.chem.MassCalculator;
import org.expasy.jpl.core.mol.polymer.pept.cutter.CleavageSiteCutter;
import org.expasy.jpl.core.mol.polymer.pept.cutter.Digester;
import org.expasy.jpl.core.mol.polymer.pept.cutter.Peptidase;

/**
 * This object parses and stores command line parameters.
 * 
 * @author nikitin
 * 
 * @version 1.0
 */
public class Parameters extends AbstractApplicationParameters {

    private static final String __VERSION__ = "1.2";

    /** the default double precision */
    private static int DEFAULT_PRECISION = 6;

    /** the default number of missed cleavage */
    private static int DEFAULT_MISSED_CLEAVAGES = 0;

    /** the default peptide length filter */
    private static Integer DEFAULT_PEPT_LEN_FILTER = 5;

    /** the default protease */
    private static String DEFAULT_ENZYME = "Trypsin";

    private static MassCalculator DEFAULT_MASS_TYPE = MassCalculator.getMonoAccuracyInstance();

    private static List<Integer> DEFAULT_OUTPUT_FIELDS_IDX = Arrays.asList(1, 2, 3, 4, 5);

    private static String DEFAULT_FIELD_DELIMITER = "\t";

    static List<String> OUTPUT_FIELDS = Arrays.asList("Sequence", "MW", "pI", "Enz", "MC");

    /** the peptidases */
    private Set<Digester> digesters;

    /********************* the parameters **************************/
    private File fastaFile;

    private File outFile;

    /** the field delimiter for output */
    private String fieldDelimiter;

    /** the mass calculator */
    private MassCalculator massCalc;

    /** the print formatter for doubles */
    private NumberFormat formatter;

    /** the precision for doubles */
    private int precision;

    /** the fragment length filter for output */
    private int peptLenFilter;

    /** the output fields */
    private List<Integer> fields;

    /** the number of maximum missed-cleavages */
    private int missedCleavages;

    public Parameters(final Class<?> mainClass, final String[] params) {
        super(mainClass, __VERSION__, "<Fasta>", params);
    }

    public void init() {
    }

    public void addSettings(Properties settings) {
        settings.setProperty("precision", String.valueOf(DEFAULT_PRECISION));
        settings.setProperty("delimiter", DEFAULT_FIELD_DELIMITER);
        settings.setProperty("average", "false");
        settings.setProperty("enzymes", DEFAULT_ENZYME);
        settings.setProperty("fields", DEFAULT_OUTPUT_FIELDS_IDX.toString());
        settings.setProperty("max-missed-cleavage", String.valueOf(DEFAULT_MISSED_CLEAVAGES));
        settings.setProperty("length", String.valueOf(DEFAULT_PEPT_LEN_FILTER));
    }

    public String getSpecificParamInfos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Input\n");
        sb.append("  read proteins from file    " + fastaFile + "\n");
        sb.append("  digested by protease       " + digesters + "\n");
        sb.append("Output\n");
        sb.append("  flush peptides in file     " + outFile + "\n");
        sb.append("  peptide length             >= " + peptLenFilter + "\n");
        sb.append("  peptide mass accuracy      " + massCalc.getAccuracy() + "\n");
        sb.append("  Peptide Fields\n");
        sb.append("    all                      " + OUTPUT_FIELDS + "\n");
        sb.append("    selected indices         " + fields + "\n\n");
        sb.append("  Number precision format    " + precision + "\n");
        return sb.toString();
    }

    public Options createAppOptions() {
        Options options = new Options();
        options.addOption("p", "precision", true, "define the number of fractional digits for output\n" + "by default: " + DEFAULT_PRECISION + ".");
        options.addOption("a", "average", false, "set the average mass accuracy for peptide mass calculation\n" + "by default: " + DEFAULT_MASS_TYPE.getAccuracy() + ".");
        options.addOption("o", "output", true, "set the output filename\n");
        options.addOption("l", "length", true, "define a filter over length of digested peptides\nby default: " + DEFAULT_PEPT_LEN_FILTER + ".");
        StringBuilder sb = new StringBuilder();
        for (Peptidase peptidase : Peptidase.getAllPooledPeptidases()) {
            sb.append(peptidase.getId());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        options.addOption("e", "enzymes", true, "define enzymes that digest proteins separately with:\n" + "enzyme name among '" + sb + "'.\nor custom motifs respecting the following grammar:\n" + "<pre-cut> <cut-token> <post-cut>\n" + "<cut-token> := '|'\n" + "<pre-cut> := (<AA> or <AA-class>)+\n" + "<post-cut> := (<AA> or <AA-class>)+\n" + "<AA> := [A-Z]\n" + "<AA-class> := '[' AA+ ']'\n" + "by default: " + DEFAULT_ENZYME + ".");
        sb = new StringBuilder();
        for (int i = 0; i < OUTPUT_FIELDS.size(); i++) {
            sb.append(i + 1);
            sb.append(":");
            sb.append(OUTPUT_FIELDS.get(i));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        options.addOption("d", "delimiter", true, "define the field delimiter to display" + "\nby default: \\t.");
        options.addOption("f", "fields", true, "define the fields to display (" + sb + ")\nby default: " + DEFAULT_OUTPUT_FIELDS_IDX + ".");
        options.addOption("m", "max-missed-cleavage", true, "define the number of maximum missed cleavages (for digestion)\n" + "by default: " + DEFAULT_MISSED_CLEAVAGES + ".");
        return options;
    }

    public void parseSpecificOptions(Options options, Properties settings, CommandLine line) throws ParseException {
        if (line.hasOption("a")) {
            settings.setProperty("average", "true");
        }
        if (line.hasOption("f")) {
            settings.setProperty("fields", line.getOptionValue("f"));
        }
        if (line.hasOption("d")) {
            settings.setProperty("delimiter", line.getOptionValue("d"));
        }
        if (line.hasOption("l")) {
            settings.setProperty("length", line.getOptionValue("l"));
        }
        if (line.hasOption("m")) {
            settings.setProperty("max-missed-cleavage", line.getOptionValue("m"));
        }
        if (line.hasOption("e")) {
            settings.setProperty("enzymes", line.getOptionValue("e"));
        }
        if (line.hasOption("p")) {
            settings.setProperty("precision", line.getOptionValue("p"));
        }
        if (line.getArgList().size() == 0) {
            throw new ParseException("missing fasta file name");
        } else if (line.getArgList().size() > 1) {
            throw new ParseException("too many arguments");
        } else {
            String filename = line.getArgs()[0];
            fastaFile = new File(filename);
            if (!fastaFile.exists()) {
                throw new ParseException(filename + " was not found");
            }
        }
        if (!settings.containsKey("output")) {
            if (line.hasOption("o")) {
                settings.setProperty("output", line.getOptionValue("o"));
            } else {
                settings.setProperty("output", fastaFile.getAbsolutePath() + ".tsv");
            }
        }
    }

    public void parseArguments(List<String> args) throws ParseException {
    }

    protected void setPropertyValues() throws ParseException {
        super.setPropertyValues();
        try {
            boolean isAverage = Boolean.parseBoolean(settings.getProperty("average"));
            if (isAverage) {
                massCalc = MassCalculator.getAvgAccuracyInstance();
            } else {
                massCalc = MassCalculator.getMonoAccuracyInstance();
            }
        } catch (NumberFormatException e) {
            new ParseException(e.getMessage());
        }
        try {
            missedCleavages = Integer.parseInt(settings.getProperty("max-missed-cleavage"));
        } catch (NumberFormatException e) {
            new ParseException(e.getMessage());
        }
        try {
            digesters = parseEnzymes(settings.getProperty("enzymes"), missedCleavages);
        } catch (java.text.ParseException e) {
            throw new ParseException(e.getMessage());
        }
        try {
            fields = parseFields(settings.getProperty("fields"));
        } catch (java.text.ParseException e) {
            throw new ParseException(e.getMessage());
        }
        try {
            peptLenFilter = Integer.parseInt(settings.getProperty("length"));
        } catch (NumberFormatException e) {
            new ParseException(e.getMessage());
        }
        try {
            precision = Integer.parseInt(settings.getProperty("precision"));
            formatter = DecimalFormatFactory.valueOf(precision);
        } catch (NumberFormatException e) {
            new ParseException(e.getMessage());
        }
        fieldDelimiter = settings.getProperty("delimiter");
        outFile = new File(settings.getProperty("output"));
    }

    /**
	 * Parse output fields from a given string.
	 * 
	 * @param param the string to get field index from.
	 * @return a list of fields.
	 * 
	 * @throws java.text.ParseException if not well formatted.
	 */
    private static List<Integer> parseFields(String param) throws java.text.ParseException {
        List<Integer> fields = new ArrayList<Integer>();
        param = param.replace("[", "").replace("]", "");
        String[] fs = param.split(",\\s*");
        for (String f : fs) {
            int field = Integer.parseInt(f);
            if (field < 1 || field > OUTPUT_FIELDS.size()) {
                throw new java.text.ParseException(field + ": bad field index", -1);
            }
            fields.add(field);
        }
        return fields;
    }

    /**
	 * Parse enzyme(s) from a given string.
	 * 
	 * @param param the string to get enzymes from.
	 * @return a list of enzymes.
	 * 
	 * @throws java.text.ParseException if not well formatted.
	 */
    public static Set<Digester> parseEnzymes(String line, int missedCleavageNum) throws java.text.ParseException {
        Set<Digester> s = new HashSet<Digester>();
        CleavageSiteCutter cutter = null;
        String[] enzymes = line.split(",\\s*");
        Digester digester;
        Peptidase peptidase;
        for (String enz : enzymes) {
            if (enz.indexOf('|') != -1) {
                cutter = new CleavageSiteCutter.Builder(enz).build();
                peptidase = Peptidase.valueOf(cutter);
            } else {
                peptidase = Peptidase.getInstance(enz);
            }
            if (peptidase == null) {
                throw new java.text.ParseException("enzyme " + enz + " is unknown", -1);
            }
            digester = Digester.newInstance(peptidase);
            if (missedCleavageNum > 0) {
                digester.setNumberOfMissedCleavage(missedCleavageNum);
            }
            s.add(digester);
        }
        return s;
    }

    /**
	 * @return the pepxml filename
	 */
    public final File getFastaFile() {
        return fastaFile;
    }

    /**
	 * @return the formatter
	 */
    public NumberFormat getFormatter() {
        return formatter;
    }

    /**
	 * @return the massCalc
	 */
    public final MassCalculator getMassCalc() {
        return massCalc;
    }

    /**
	 * @return the precision
	 */
    public final int getPrecision() {
        return precision;
    }

    /**
	 * @return the output file
	 */
    public final File getTSVFile() {
        return outFile;
    }

    /**
	 * @return the fieldDelimiter
	 */
    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    /**
	 * @return the fields
	 */
    public List<Integer> getFields() {
        return fields;
    }

    /**
	 * @return the digesters
	 */
    public Set<Digester> getDigesters() {
        return digesters;
    }

    /**
	 * @return the outFile
	 */
    public File getOutFile() {
        return outFile;
    }

    /**
	 * @return the peptLenFilter
	 */
    public int getPeptLenFilter() {
        return peptLenFilter;
    }
}
