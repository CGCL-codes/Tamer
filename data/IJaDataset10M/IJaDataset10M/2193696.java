package src.fileUtilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import src.lib.CommandLine;
import src.lib.Constants;
import src.lib.CurrentVersion;
import src.lib.IterableIterator;
import src.lib.Error_handling.CommandLineProcessingException;
import src.lib.Error_handling.UnexpectedResultException;
import src.lib.analysisTools.Exon_Junction_Map;
import src.lib.ioInterfaces.Bedwriter;
import src.lib.ioInterfaces.Generic_AlignRead_Iterator;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.AlignedRead;
import src.lib.objects.MAQRyanMap;
import src.lib.objects.SimpleAlignedRead;

/**
 * A program to convert reads to BED files.
 * @author
 * @version $Revision: 3247 $
 */
public class ConvertToBed {

    private static String output_path = null;

    private static String[] files = null;

    private static String filename = null;

    private static Log_Buffer LB;

    private static String aligner = null;

    private static boolean chr_print = true;

    private static boolean prepend = true;

    private static boolean header = true;

    private static int qualityfilter = 0;

    private static boolean junctions;

    private static String junction_map;

    private static int junctionsize;

    private static final int JUNCTION_HALF_WIDTH_36 = 35;

    private static final String JUNCTION_READ_NAME_36 = "all_junctions.36.version2";

    private static final int JUNCTION_HALF_WIDTH_42 = 41;

    private static final String JUNCTION_READ_NAME_42 = "all_junctions.42.version2";

    private static final int JUNCTION_HALF_WIDTH_50 = 49;

    private static final String JUNCTION_READ_NAME_50 = "all_junctions.50.version2";

    private static int JUNCTION_HALF_WIDTH;

    private static String JUNCTION_READ_NAME;

    private ConvertToBed() {
    }

    private static void usage() {
        StringBuffer sb = new StringBuffer();
        sb.append("To use this function:\n");
        sb.append("Flags are as follows:\n");
        sb.append(" -help            | Displays this text\n");
        sb.append(" -input           | Provide a list of file(s) to read and process.\n");
        sb.append(" -output          | Path where the output files should be deposited.\n");
        sb.append(" -name            | name of file(s).\n");
        sb.append(" -aligner         | name of the aligner.\n");
        sb.append(" -nochr           | drop the name of the chromosome/genome.\n");
        sb.append(" -noprepend       | do not prepend chr to the chromosome name.\n");
        sb.append(" -noheader        | do not include the bedfile header.\n");
        sb.append(" -qualityfilter   | the minimum quality to accept, when using maq files.\n");
        sb.append("Junction specific flags:\n");
        sb.append(" -junctionmap     | the file containing the map to the junction annotation.\n");
        sb.append(" -junctionsize    | the size of the junction map in use.  Expected values 36,42,50\n");
        if (LB != null) {
            LB.notice(sb.toString());
            LB.die();
        } else {
            System.out.println(sb.toString());
            System.exit(0);
        }
    }

    private static String get_output_path_bootstrap(String[] args) {
        for (int x = 0; x < args.length; x++) {
            if (args[x].equalsIgnoreCase("-output")) {
                if (x + 1 < args.length && !(args[x + 1].startsWith("-"))) {
                    if (!args[x + 1].endsWith(System.getProperty("file.separator"))) {
                        args[x + 1] = args[x + 1].concat(System.getProperty("file.separator"));
                    }
                    return args[x + 1].trim();
                } else {
                    usage();
                }
            }
        }
        System.out.println("No output flag provided: must use -output flag on command line. " + "Can't bootstrap error log");
        usage();
        return null;
    }

    private static void parse_input(HashMap<String, String> Variables) {
        if (Variables == null) {
            usage();
        }
        assert (Variables != null);
        if (Variables.containsKey("help")) {
            usage();
        }
        if (Variables.containsKey("output")) {
            CommandLine.test_parameter_count(LB, "output", Variables.get("output"), 1);
            output_path = Variables.get("output");
            LB.notice(" * Output path name  : " + output_path);
        } else {
            LB.error("output path must be supplied with -output flag");
            usage();
        }
        if (Variables.containsKey("name")) {
            CommandLine.test_parameter_count(LB, "name", Variables.get("name"), 1);
            filename = Variables.get("name");
            LB.notice(" * Output file name  : " + filename);
        } else {
            LB.error("output file must be supplied with -name flag");
            usage();
        }
        if (Variables.containsKey("input")) {
            files = Variables.get("input").split(",");
            if (files.length == 0) {
                LB.error("-input flag must be followed by a list of files to process");
                LB.die();
            }
        } else {
            LB.error("Input file(s) must be provided must be supplied with the -input flag");
            usage();
        }
        if (Variables.containsKey("aligner")) {
            CommandLine.test_parameter_count(LB, "aligner", Variables.get("aligner"), 1);
            aligner = Variables.get("aligner");
            LB.notice(" * aligner           : " + aligner);
        } else {
            LB.error("aligner must be supplied with -aligner flag");
            usage();
        }
        if (Variables.containsKey("junctionmap")) {
            if (!aligner.equalsIgnoreCase(Constants.FILE_TYPE_MAQ)) {
                LB.error("The -junctionmap parameter may only be used with -aligner maq");
                LB.die();
            }
            CommandLine.test_parameter_count(LB, "junctionmap", Variables.get("junctionmap"), 1);
            junction_map = Variables.get("junctionmap");
            junctions = true;
            LB.notice(" * Junction map file : " + junction_map);
        } else {
            junctions = false;
            LB.notice(" * Junctions         : Skipped");
        }
        if (Variables.containsKey("junctionsize")) {
            if (!junctions) {
                LB.error("the -junctionsize parameter may only be used if the -junctionmap parameter is used.");
                LB.die();
            }
            CommandLine.test_parameter_count(LB, "junctionsize", Variables.get("junctionsize"), 1);
            junctionsize = Integer.valueOf(Variables.get("junctionsize"));
            LB.notice(" * Map junction size : " + junctionsize);
            if (junctionsize == 36) {
                JUNCTION_HALF_WIDTH = JUNCTION_HALF_WIDTH_36;
                JUNCTION_READ_NAME = JUNCTION_READ_NAME_36;
            } else if (junctionsize == 42) {
                JUNCTION_HALF_WIDTH = JUNCTION_HALF_WIDTH_42;
                JUNCTION_READ_NAME = JUNCTION_READ_NAME_42;
            } else if (junctionsize == 50) {
                JUNCTION_HALF_WIDTH = JUNCTION_HALF_WIDTH_50;
                JUNCTION_READ_NAME = JUNCTION_READ_NAME_50;
            } else {
                LB.notice("Supported junction sizes are : 36, 42 and 50.  Specified junction size of " + junctionsize + " is not currently available.");
                LB.die();
            }
        } else {
            if (junctions) {
                LB.error("Must specify junction size with the -junctionsize flag, if junctions are in use");
                usage();
            }
        }
        if (Variables.containsKey("override_mapname")) {
            CommandLine.test_parameter_count(LB, "override_mapname", Variables.get("override_mapname"), 1);
            JUNCTION_READ_NAME = Variables.get("override_mapname");
        }
        if (Variables.containsKey("nochr")) {
            CommandLine.test_parameter_count(LB, "nochr", Variables.get("nochr"), 0);
            chr_print = false;
        }
        LB.notice(" * Chr names         : " + ((chr_print) ? "names will be printed" : "no names"));
        if (Variables.containsKey("noheader")) {
            CommandLine.test_parameter_count(LB, "noheader", Variables.get("noheader"), 0);
            header = false;
        }
        LB.notice(" * Bedfile header    : " + ((header) ? "header on" : "header off"));
        if (Variables.containsKey("noprepend")) {
            CommandLine.test_parameter_count(LB, "noprepend", Variables.get("noprepend"), 0);
            prepend = false;
        }
        LB.notice(" * Prepend chr       : " + ((prepend) ? "'chr' will be prepended to chr names" : "no prepend"));
        if (Variables.containsKey("qualityfilter")) {
            CommandLine.test_parameter_count(LB, "qualityfilter", Variables.get("qualityfilter"), 1);
            qualityfilter = Integer.valueOf(Variables.get("qualityfilter"));
        }
        LB.notice(" * Minimum quality   : " + qualityfilter);
        LB.notice("Files in use:");
        for (String f : files) {
            LB.notice("\t" + f);
        }
        Variables.remove("help");
        Variables.remove("input");
        Variables.remove("output");
        Variables.remove("name");
        Variables.remove("aligner");
        Variables.remove("header");
        Variables.remove("nochr");
        Variables.remove("noprepend");
        Variables.remove("junctionsize");
        Variables.remove("junctionmap");
        Variables.remove("override_mapname");
        Variables.remove("qualityfilter");
        Iterator<String> keys = Variables.keySet().iterator();
        if (keys.hasNext()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Could not process the following flags:");
            for (String k : new IterableIterator<String>(keys)) {
                sb.append("  " + k);
            }
            LB.error(sb.toString());
            LB.die();
        }
    }

    /**
	 *
	 * @param args list of files to operate on.
	 */
    public static void main(String[] args) {
        final String path = get_output_path_bootstrap(args);
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        LB.addLogFile(path + "AlignReadsToBed.log");
        Thread th = new Thread(LB);
        th.start();
        new CurrentVersion(LB);
        LB.Version("ConvertToBed", "$Revision: 3247 $");
        LB.notice("Log file: " + path + "ConvertToBed.log");
        HashMap<String, String> Variables = null;
        try {
            Variables = CommandLine.process_CLI(args);
        } catch (CommandLineProcessingException CLPE) {
            LB.error(CLPE.getMessage());
            LB.die();
        }
        parse_input(Variables);
        HashMap<String, Bedwriter> ht = new HashMap<String, Bedwriter>();
        MAQRyanMap[] all_junctions = null;
        if (junctions) {
            LB.notice("Reading in Ryan's junction to genome mappings");
            all_junctions = Exon_Junction_Map.get_all_junctions_map(LB, junction_map);
        }
        for (String f : files) {
            Generic_AlignRead_Iterator it = new Generic_AlignRead_Iterator(LB, aligner, "sourcefile", f, qualityfilter, 128, null, 0, false);
            int R = 0;
            int G = 0;
            int B = 0;
            int cnt = 0;
            while (it.hasNext()) {
                cnt++;
                if (cnt % 1000000 == 0) {
                    LB.notice(cnt + " reads processed");
                }
                AlignedRead alnrd = null;
                try {
                    alnrd = it.next();
                } catch (NoSuchElementException e) {
                    break;
                }
                if (alnrd == null) {
                    continue;
                }
                if (alnrd.get_direction() == '+') {
                    R = Constants.COLOURS_ON;
                    B = 0;
                    G = 0;
                } else {
                    R = 0;
                    B = Constants.COLOURS_ON;
                    G = 0;
                }
                if (alnrd.get_chromosome().equals(JUNCTION_READ_NAME)) {
                    handle_junctions(ht, alnrd, all_junctions, R, G, B);
                } else {
                    printRead(ht, alnrd, R, G, B);
                }
            }
            it.close();
            System.gc();
        }
        Iterator<String> keys = ht.keySet().iterator();
        for (String k : new IterableIterator<String>(keys)) {
            Bedwriter bed = ht.get(k);
            bed.close();
        }
        LB.close();
    }

    private static void handle_junctions(HashMap<String, Bedwriter> ht, AlignedRead alnrd, MAQRyanMap[] all_junctions, int R, int G, int B) {
        Vector<SimpleAlignedRead> tmp = Exon_Junction_Map.TranslateJunction(LB, alnrd, all_junctions, JUNCTION_HALF_WIDTH);
        if (tmp != null) {
            AlignedRead x = null;
            for (SimpleAlignedRead t : tmp) {
                try {
                    x = t.toAlignedRead();
                } catch (UnexpectedResultException ure) {
                    LB.error("Aligned junction could not be converted back to alignedread object");
                    LB.error("Warning thrown: " + ure.getMessage());
                }
                printRead(ht, x, R, G, B);
            }
        } else {
            LB.notice("Skipped a junction - could not map it.");
            LB.notice("Sequence: " + alnrd.get_sequence());
        }
    }

    private static void printRead(HashMap<String, Bedwriter> ht, AlignedRead alnrd, int R, int G, int B) {
        int align_start = alnrd.get_alignStart();
        int align_end = alnrd.get_alignEnd();
        if (alnrd.get_direction() == '-') {
            align_start = align_end - alnrd.get_alignLength();
            if (align_start < 0 || align_end < 0) {
                return;
            }
        } else {
            align_start -= 1;
        }
        align_start -= 1;
        String chr = alnrd.get_chromosome();
        if (chr.contentEquals("MT")) {
            chr = "M";
        }
        String chrname = null;
        if (chr_print) {
            if (chr.startsWith("chr") || !prepend) {
                chrname = chr;
            } else {
                chrname = "chr" + chr;
            }
        } else {
            chrname = "";
        }
        Bedwriter bed = ht.get(chrname);
        if (bed == null) {
            bed = new Bedwriter(LB, output_path + chrname + "_" + filename + "_" + aligner + ".bed.gz");
            if (header) {
                bed.BedHeader(chrname + "_" + filename + " Sequences", "Illumina " + aligner + "-aligned reads - " + filename, 10, 4, true);
            }
            ht.put(chrname, bed);
        }
        bed.writelnExt(chrname, align_start, align_end, alnrd.get_sequence(), 0, alnrd.get_direction(), align_start, align_end, R, G, B, 0, "", "");
    }
}
