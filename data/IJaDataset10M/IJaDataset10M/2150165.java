package src.projects.VariationDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import src.lib.CommandLine;
import src.lib.CurrentVersion;
import src.lib.IterableIterator;
import src.lib.Error_handling.CommandLineProcessingException;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.SNP;
import src.projects.VariationDatabase.Iterators.Generic_SNP_Iterator;
import src.projects.VariationDatabase.libs.CommentFile;
import src.projects.VariationDatabase.libs.PSQL_SNP_insert;

public class importSNPs {

    private static String input_file;

    private static String output_path;

    private static String input_species;

    private static String input_chr;

    private static String conf_file;

    private static String psql_file;

    private static String db_name;

    private static String aligner;

    private static String aligner_ver;

    private static String snp_caller;

    private static String snp_caller_ver;

    private static String name;

    private static String format;

    private static String platform;

    private static int min_aln_quality;

    private static int min_base_quality;

    private static boolean cancer;

    private static String contact;

    private static String protocol;

    private static int readlength;

    private static boolean cell_line;

    private static String reference;

    private static String seq_type;

    private static String keyword_file;

    private static final String ST_KEYWORDFILE = "keyword_file";

    private importSNPs() {
    }

    private static Log_Buffer LB = null;

    /**
	 * Processing command line arguments for program.
	 * 
	 * @param Variables
	 *            Command line arguments: input path, output path, Species,
	 *            Chromosome(s), min snp percent, min snp observed.
	 */
    private static void parse_input(HashMap<String, String> Variables) {
        if (Variables == null) {
            usage();
        }
        assert (Variables != null);
        if (Variables.containsKey("help")) {
            usage();
        }
        if (Variables.containsKey("name")) {
            CommandLine.test_parameter_count(LB, "name", Variables.get("name"), 1);
            name = Variables.get("name");
        } else {
            LB.notice("file names must be supplied with -name flag");
            usage();
        }
        if (Variables.containsKey("output")) {
            CommandLine.test_parameter_count(LB, "output", Variables.get("output"), 1);
            output_path = Variables.get("output");
            if (!output_path.endsWith(System.getProperty("file.separator"))) {
                output_path = output_path.concat(System.getProperty("file.separator"));
            }
            LB.notice("Log File: " + output_path + name + ".log");
            LB.addLogFile(output_path + name + ".log");
        } else {
            LB.error("An output directory must be supplied with the -output flag");
            usage();
        }
        LB.notice(" * Output file name  : " + output_path);
        LB.notice(" * Name              : " + name);
        if (Variables.containsKey("format")) {
            CommandLine.test_parameter_count_min(LB, "format", Variables.get("format"), 1);
            format = Variables.get("format");
            LB.notice(" * Input format      : " + format);
        } else {
            LB.error("snp file format must be supplied with -format flag");
            usage();
        }
        if (Variables.containsKey("aligner")) {
            CommandLine.test_parameter_count_min(LB, "aligner", Variables.get("aligner"), 1);
            aligner = Variables.get("aligner");
            LB.notice(" * Input aligner     : " + aligner);
        } else {
            LB.error("aligner must be supplied with -aligner flag");
            usage();
        }
        if (Variables.containsKey("aligner_ver")) {
            CommandLine.test_parameter_count_min(LB, "aligner_ver", Variables.get("aligner_ver"), 1);
            aligner_ver = Variables.get("aligner_ver");
            LB.notice(" * aligner version   : " + aligner_ver);
        } else {
            LB.error("aligner version must be supplied with -aligner_ver flag");
            usage();
        }
        if (Variables.containsKey("snp_caller")) {
            CommandLine.test_parameter_count_min(LB, "snp_caller", Variables.get("snp_caller"), 1);
            snp_caller = Variables.get("snp_caller");
            LB.notice(" * SNP caller        : " + snp_caller);
        } else {
            LB.error("snp caller must be supplied with -snp_caller flag");
            usage();
        }
        if (Variables.containsKey("snp_caller_ver")) {
            CommandLine.test_parameter_count_min(LB, "snp_caller_ver", Variables.get("snp_caller_ver"), 1);
            snp_caller_ver = Variables.get("snp_caller_ver");
            LB.notice(" * SNP caller ver.   : " + snp_caller_ver);
        } else {
            LB.error("snp caller version must be supplied with -snp_caller_ver flag");
            usage();
        }
        if (Variables.containsKey("conf")) {
            CommandLine.test_parameter_count(LB, "conf", Variables.get("conf"), 1);
            conf_file = Variables.get("conf");
            LB.notice(" * Config file       : " + conf_file);
        } else {
            LB.error("Must specify config file with the -conf flag");
            usage();
        }
        if (Variables.containsKey("psql")) {
            CommandLine.test_parameter_count(LB, "psql", Variables.get("psql"), 1);
            psql_file = Variables.get("psql");
            LB.notice(" * PSQL config file  : " + psql_file);
        } else {
            LB.error("Must specify psql config file with the -psql flag");
            usage();
        }
        if (Variables.containsKey("db_name")) {
            CommandLine.test_parameter_count(LB, "db_name", Variables.get("db_name"), 1);
            db_name = Variables.get("db_name");
            LB.notice(" * PSQL Database name: " + db_name);
        } else {
            LB.error("Must specify psql database name with the -db_name flag");
            usage();
        }
        if (Variables.containsKey("maq_aln_quality")) {
            if (!aligner.equals("maq")) {
                LB.notice("-maq_aln_quality only used for maq aligner");
                usage();
            }
            CommandLine.test_parameter_count(LB, "maq_aln_quality", Variables.get("maq_aln_quality"), 1);
            min_aln_quality = Integer.valueOf(Variables.get("maq_aln_quality"));
            LB.notice(" * Maq min aln qual. : " + min_aln_quality);
        } else {
            min_aln_quality = 0;
        }
        if (Variables.containsKey("maq_base_quality")) {
            if (!aligner.equals("maq")) {
                LB.notice("-maq_base_quality only used for maq aligner");
                usage();
            }
            CommandLine.test_parameter_count(LB, "maq_base_quality", Variables.get("maq_base_quality"), 1);
            min_base_quality = Integer.valueOf(Variables.get("maq_base_quality"));
            LB.notice(" * Maq min base qual.: " + min_base_quality);
        } else {
            min_base_quality = 0;
        }
        if (Variables.containsKey("chr")) {
            CommandLine.test_parameter_count_min(LB, "chr", Variables.get("chr"), 1);
            input_chr = Variables.get("chr");
            LB.notice(" * Chromosome in use : " + input_chr);
        } else {
            LB.error("chomosome must be supplied with -chr flag");
            usage();
        }
        if (Variables.containsKey("species")) {
            CommandLine.test_parameter_count(LB, "species", Variables.get("species"), 1);
            input_species = Variables.get("species");
            LB.notice(" * Input Species     : " + input_species);
        } else {
            LB.error("input species must be supplied with -species flag");
            usage();
        }
        if (Variables.containsKey("cancer")) {
            CommandLine.test_parameter_count(LB, "cancer", Variables.get("cancer"), 1);
            String c = Variables.get("cancer");
            if (c.equalsIgnoreCase("true")) {
                cancer = true;
            } else if (c.equalsIgnoreCase("false")) {
                cancer = false;
            } else {
                LB.error("Unrecognized parameter for -cancer flag: must be set to true or false");
                usage();
            }
        } else {
            LB.error("The -cancer flag must be used to set the cancer variable, use '-cancer true' or '-cancer false'");
            usage();
        }
        LB.notice(" * Set is Cancer     : " + cancer);
        if (Variables.containsKey("contact")) {
            CommandLine.test_parameter_count(LB, "contact", Variables.get("contact"), 1);
            contact = Variables.get("contact");
            LB.notice(" * Contact           : " + contact);
        } else {
            contact = "";
            LB.notice(" * Contact           : none");
        }
        if (Variables.containsKey("protocol")) {
            CommandLine.test_parameter_count(LB, "protocol", Variables.get("protocol"), 1);
            protocol = Variables.get("protocol");
            LB.notice(" * Protocol used     : " + protocol);
        } else {
            protocol = "";
            LB.notice(" * Protocol used     : none");
        }
        if (Variables.containsKey("platform")) {
            CommandLine.test_parameter_count(LB, "platform", Variables.get("platform"), 1);
            platform = Variables.get("platform");
            LB.notice(" * Platform          : " + platform);
        } else {
            LB.error("Must specify platform used with -platform flag: Illumina, SOLID, 454");
            usage();
        }
        if (Variables.containsKey("readlength")) {
            CommandLine.test_parameter_count(LB, "readlength", Variables.get("readlength"), 1);
            readlength = Integer.valueOf(Variables.get("readlength"));
            LB.notice(" * Read length       : " + readlength);
        } else {
            readlength = 0;
            LB.notice(" * Read length       : none");
        }
        if (Variables.containsKey("reference")) {
            CommandLine.test_parameter_count(LB, "reference", Variables.get("reference"), 1);
            reference = Variables.get("reference");
            LB.notice(" * Reference name    : " + reference);
        } else {
            reference = "";
            LB.notice(" * Reference name    : none");
        }
        if (Variables.containsKey("cell_line")) {
            CommandLine.test_parameter_count(LB, "cell_line", Variables.get("cancer"), 1);
            String c = Variables.get("cell_line");
            if (c.equalsIgnoreCase("true")) {
                cell_line = true;
            } else if (c.equalsIgnoreCase("false")) {
                cell_line = false;
            } else {
                LB.error("Unrecognized parameter for -cell_line flag: must be set to true or false");
                usage();
            }
        } else {
            LB.error("The -cell_line must be used to set the cell_line variable, use '-cell_line true' or '-cell_line false'");
            usage();
        }
        LB.notice(" * Set as Cell Line  : " + cell_line);
        if (Variables.containsKey("seq_type")) {
            CommandLine.test_parameter_count(LB, "seq_type", Variables.get("seq_type"), 1);
            seq_type = Variables.get("seq_type");
            LB.notice(" * Sequencing type   : " + seq_type);
        } else {
            seq_type = "";
            LB.notice(" * Sequencing type   : none");
        }
        if (Variables.containsKey("input_file")) {
            CommandLine.test_parameter_count(LB, "input_file", Variables.get("input_file"), 1);
            input_file = Variables.get("input_file");
            LB.notice(" * Input file        : " + input_file);
            if (Variables.containsKey("input")) {
                LB.warning("Ignoring -input flag.  Redundant with -input_file flag.");
            }
        } else {
            LB.error("An input file must be supplied with the -input_file flag");
            usage();
        }
        if (Variables.containsKey(ST_KEYWORDFILE)) {
            CommandLine.test_parameter_count(LB, ST_KEYWORDFILE, Variables.get(ST_KEYWORDFILE), 1);
            keyword_file = Variables.get(ST_KEYWORDFILE);
            LB.notice(" * Keyword File      : " + keyword_file);
        } else {
            keyword_file = "";
            LB.notice(" * Keyword File      : none");
        }
        Variables.remove("input_file");
        Variables.remove("format");
        Variables.remove("output");
        Variables.remove("species");
        Variables.remove("chr");
        Variables.remove("conf");
        Variables.remove("psql");
        Variables.remove("db_name");
        Variables.remove("name");
        Variables.remove("aligner");
        Variables.remove("aligner_ver");
        Variables.remove("snp_caller");
        Variables.remove("snp_caller_ver");
        Variables.remove("maq_base_quality");
        Variables.remove("maq_aln_quality");
        Variables.remove("cancer");
        Variables.remove("contact");
        Variables.remove("protocol");
        Variables.remove("readlength");
        Variables.remove("cell_line");
        Variables.remove("cell_type");
        Variables.remove("reference");
        Variables.remove("platform");
        Variables.remove("seq_type");
        Variables.remove(ST_KEYWORDFILE);
        Iterator<String> keys = Variables.keySet().iterator();
        if (keys.hasNext()) {
            LB.error("Could not process the following flags:");
            for (String k : new IterableIterator<String>(keys)) {
                LB.error("  * " + k);
            }
            LB.die();
        }
    }

    private static void usage() {
        LB.notice("This program requires the following parameters:");
        LB.notice(" -input_file    | <String> | provide the full path to the aligned file.");
        LB.notice(" -output        | <String> | provide a valid path for the output.");
        LB.notice(" -species       | <String> | Provide a Species handled in the conf file");
        LB.notice(" -chr           | <String> | Indicate which chromosome to run, or \"A\" for all.");
        LB.notice(" -format        | <String> | The format of the Snp file to read.");
        LB.notice(" -aligner       | <String> | Name of the aligner that provided the reads.");
        LB.notice(" -aligner_ver   | <String> | Version of the aligner that provided the reads .");
        LB.notice(" -snp_caller    | <String> | Name of the SNP caller that provided the reads (defines file format).");
        LB.notice(" -snp_caller_ver| <String> | Version of the SNP caller that provided the reads .");
        LB.notice(" -name          | <String> | Provides an identifier at the start of each file name.");
        LB.notice(" -cancer        | <boolean>| Is this cancer or not (true or false required)");
        LB.notice(" -platform      | <String> | Illumina, 454 or SOLiD required");
        LB.notice(" -conf          | <String> | The location of the configuration file to use.");
        LB.notice(" -psql          | <String> | The location of the psql configuration file to use.");
        LB.notice(" -db_name       | <String> | name of the psql database in the psql config file.");
        LB.notice(" -" + ST_KEYWORDFILE + "  | <String> | provide the full path to a file containing keywords, ");
        LB.die();
    }

    /**
	 * Main function for processing Transcriptome data.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        Thread th = new Thread(LB);
        th.start();
        HashMap<String, String> Variables = null;
        try {
            Variables = CommandLine.process_CLI(args);
        } catch (CommandLineProcessingException CLPE) {
            LB.error(CLPE.getMessage());
            LB.die();
        }
        parse_input(Variables);
        new CurrentVersion(LB);
        LB.Version("SNP_Parser", "$Revision: 3481 $");
        String keywords = "";
        if (!"".equals(keyword_file)) {
            CommentFile cf = new CommentFile(LB, keyword_file);
            keywords = cf.readfile();
            cf.close();
        }
        PSQL_SNP_insert psqlsi = new PSQL_SNP_insert(LB, psql_file, db_name);
        Generic_SNP_Iterator gsi = new Generic_SNP_Iterator(LB, format, input_file);
        ArrayList<SNP> VSNP = new ArrayList<SNP>();
        int count = 0;
        int batch = 0;
        while (gsi.hasNext()) {
            SNP s = gsi.next();
            VSNP.add(s);
            count += 1;
            if (count % 50000 == 0) {
                psqlsi.insert_SNPs(VSNP, min_aln_quality, min_base_quality, aligner, aligner_ver, snp_caller, snp_caller_ver, name, reference, readlength, 0, cancer, input_file, contact, protocol, cell_line, platform, keywords);
                VSNP.clear();
                batch += 1;
                LB.notice("Batch " + batch + " of snps inserted.");
            }
        }
        LB.notice("Inserting remaining (" + VSNP.size() + ") SNPs to DB");
        psqlsi.insert_SNPs(VSNP, min_aln_quality, min_base_quality, aligner, aligner_ver, snp_caller, snp_caller_ver, name, reference, readlength, 0, cancer, input_file, contact, protocol, cell_line, platform, keywords);
        psqlsi.close();
        LB.close();
    }
}
