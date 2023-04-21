package src.projects.findFeatues;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import src.lib.CommandLine;
import src.lib.Ensembl;
import src.lib.IterableIterator;
import src.lib.Error_handling.CommandLineProcessingException;
import src.lib.analysisTools.Exon_Overlap;
import src.lib.ioInterfaces.GSC_Aberation_Iterator;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.Aberation;
import src.lib.objects.Aberation_Deletion;

public class FindFeatures {

    private static Log_Buffer LB;

    private static String conf_file;

    private static Ensembl Const;

    private static String input_chr;

    private static String input_species;

    private static String output_path;

    private static String input_file;

    private static String input_dir;

    private static String name;

    private static int ext_width;

    private static final String PARM_ALIGNER = "aligner";

    private FindFeatures() {
    }

    private static void usage() {
        LB.notice("This program requires six parameters:");
        LB.notice(" -input   | <String> | provide the full path to the eland files.");
        LB.notice(" -output  | <String> | provide a valid path for the output.");
        LB.notice(" -species | <String> | Provide a Species handled in the conf file");
        LB.notice(" -chr     | <String> | Indicate which chromosome to run, or \"A\" for all.");
        LB.notice(" -conf    | <String> | The location of the configuration file to use.");
        LB.notice(" -prepend | <String> | allows a string to be prepended to the chromosome name");
        LB.die();
    }

    private static void parse_input(HashMap<String, String> Variables) {
        if (Variables == null) {
            usage();
        }
        assert (Variables != null);
        if (Variables.containsKey("help")) {
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
        if (Variables.containsKey("chr")) {
            CommandLine.test_parameter_count_min(LB, "chr", Variables.get("chr"), 1);
            input_chr = Variables.get("chr");
            LB.notice(" * Chromosome in use : " + input_chr);
        } else {
            LB.error("chomosome must be supplied with -chr flag");
            usage();
        }
        if (Variables.containsKey("ext_width")) {
            CommandLine.test_parameter_count(LB, "ext_width", Variables.get("ext_width"), 1);
            ext_width = Integer.valueOf(Variables.get("ext_width"));
        } else {
            ext_width = 0;
        }
        LB.notice(" * Extension width   : " + Variables.get("ext_width"));
        if (Variables.containsKey("species")) {
            CommandLine.test_parameter_count(LB, "species", Variables.get("species"), 1);
            input_species = Variables.get("species");
            LB.notice(" * Input Species     : " + input_species);
        } else {
            LB.error("input species must be supplied with -species flag");
            usage();
        }
        if (Variables.containsKey("name")) {
            CommandLine.test_parameter_count(LB, "name", Variables.get("name"), 1);
            name = Variables.get("name");
            LB.notice(" * Input Species     : " + name);
        } else {
            LB.error("input species must be supplied with -input flag");
            usage();
        }
        if (Variables.containsKey("output")) {
            CommandLine.test_parameter_count(LB, "output", Variables.get("output"), 1);
            output_path = Variables.get("output");
            if (!output_path.endsWith(System.getProperty("file.separator"))) {
                output_path = output_path.concat(System.getProperty("file.separator"));
            }
            LB.notice(" * Output directory  : " + output_path);
        } else {
            LB.error("An output directory must be supplied with the -output flag");
            usage();
        }
        if (Variables.containsKey("input_file")) {
            CommandLine.test_parameter_count(LB, "input_file", Variables.get("input_file"), 1);
            input_file = Variables.get("input_file");
            LB.notice(" * Input file        : " + input_file);
            if (Variables.containsKey("input")) {
                LB.warning("Ignoring -input flag.  Redundant with -input_file flag.");
            }
        } else if (Variables.containsKey("input")) {
            CommandLine.test_parameter_count(LB, "input", Variables.get("input"), 1);
            input_dir = Variables.get("input");
            if (!input_dir.endsWith(System.getProperty("file.separator"))) {
                input_dir = input_dir.concat(System.getProperty("file.separator"));
            }
            LB.notice(" * Input directory   : " + input_dir);
        } else {
            LB.error("An input file must be supplied with the -input_file flag or a directory with the -input flag");
            usage();
        }
        Variables.remove(PARM_ALIGNER);
        Variables.remove("input_file");
        Variables.remove("input");
        Variables.remove("output");
        Variables.remove("species");
        Variables.remove("chr");
        Variables.remove("conf");
        Variables.remove("name");
        Variables.remove("ext_width");
        Iterator<String> keys = Variables.keySet().iterator();
        if (keys.hasNext()) {
            LB.error("Could not process the following flags:");
            for (String k : new IterableIterator<String>(keys)) {
                LB.error("  " + k);
            }
            LB.die();
        }
    }

    /**
	 * Main function for processing Aberation data
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        LB.addLogFile(CommandLine.get_output_path_bootstrap(args) + CommandLine.get_output_name_bootstrap(args) + ".log");
        Thread th = new Thread(LB);
        th.start();
        LB.notice("Log file: " + CommandLine.get_output_path_bootstrap(args) + CommandLine.get_output_name_bootstrap(args) + ".log");
        LB.Version("FindFeatures", "$Revision: 584 $");
        HashMap<String, String> Variables = null;
        try {
            Variables = CommandLine.process_CLI(args);
        } catch (CommandLineProcessingException CLPE) {
            LB.error(CLPE.getMessage());
            LB.die();
        }
        parse_input(Variables);
        Const = Ensembl.init(LB, input_species, conf_file, input_chr);
        for (int current_chromosome = 0; current_chromosome < Const.get_number_of_chromosomes(); current_chromosome++) {
            LB.notice("*** Begin Processing Chromosome " + Const.get_chromosome(current_chromosome));
            String file = null;
            if (input_file != null) {
                file = input_file;
            } else {
                file = input_dir + Const.get_chr_filename(current_chromosome);
            }
            String file2 = output_path + Const.get_chr_filename(current_chromosome) + ".exons.ovlp";
            GSC_Aberation_Iterator it = new GSC_Aberation_Iterator(LB, "aberations", file, true);
            ArrayList<Aberation_Deletion> dels = new ArrayList<Aberation_Deletion>();
            for (Object a : new IterableIterator<Object>(it)) {
                if (((Aberation) a).get_type() == 'D') {
                    dels.add((Aberation_Deletion) a);
                }
            }
            Aberation_Deletion[] deletions = new Aberation_Deletion[dels.size()];
            deletions = dels.toArray(deletions);
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2)));
            } catch (IOException io) {
                LB.error("Can't create file for output: " + file2);
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
            assert (bw != null);
            Exon_Overlap.process_exons(LB, deletions, Const, current_chromosome, ext_width, bw);
            try {
                bw.close();
            } catch (IOException io) {
                LB.warning("Can't close file - continuing.");
                LB.warning("Message thrown by Java environment (may be null):" + io.getMessage());
            }
        }
        Const.destroy();
        Const = null;
        LB.notice("Statistics:");
        LB.close();
    }
}
