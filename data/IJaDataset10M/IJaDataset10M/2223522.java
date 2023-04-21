package src.projects.VariationDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import src.lib.CommandLine;
import src.lib.CurrentVersion;
import src.lib.Ensembl;
import src.lib.ParseInput;
import src.lib.Time;
import src.lib.Error_handling.CommandLineProcessingException;
import src.lib.ioInterfaces.FileOut;
import src.lib.ioInterfaces.HTMLOut;
import src.lib.ioInterfaces.Log_Buffer;
import src.projects.VariationDatabase.libs.IndelsInRegion;
import src.projects.VariationDatabase.libs.JunctionSnps;
import src.projects.VariationDatabase.libs.PSQLutils;
import src.projects.VariationDatabase.libs.SNPsInRegion;
import src.projects.VariationDatabase.objects.EnsemblMaps;

public class getJunctionSNPs {

    private getJunctionSNPs() {
    }

    private static String psql_file;

    private static String db_name;

    private static boolean sampledb;

    private static String conf_file;

    private static String out_path;

    private static String library;

    private static int min_obs;

    private static int min_cov;

    private static float min_percent;

    private static float percent_diff;

    private static float max_n_ratio;

    private static Log_Buffer LB = null;

    private static ArrayList<String> req_param = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add(ParseInput.PARAM_PSQL[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_DB_NAME[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_CONF[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_OUT_PATH[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_LIB[ParseInput.FIELD_NAME]);
        }
    };

    private static ArrayList<String> opt_param = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add(ParseInput.PARAM_MINCOV[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_MINOBS[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_MINPERCENT[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_PERCENT_DIFF[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_MAX_NORM_RAT[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_SAMPLEDB[ParseInput.FIELD_NAME]);
        }
    };

    private static void parse_input(HashMap<String, String> Variables) {
        ParseInput pi = new ParseInput(LB, req_param, opt_param);
        pi.parse_input(Variables);
        psql_file = pi.get(ParseInput.PARAM_PSQL[ParseInput.FIELD_NAME]);
        db_name = pi.get(ParseInput.PARAM_DB_NAME[ParseInput.FIELD_NAME]);
        conf_file = pi.get(ParseInput.PARAM_CONF[ParseInput.FIELD_NAME]);
        out_path = pi.get(ParseInput.PARAM_OUT_PATH[ParseInput.FIELD_NAME]);
        min_obs = Integer.valueOf(pi.get(ParseInput.PARAM_MINOBS[ParseInput.FIELD_NAME]));
        min_cov = Integer.valueOf(pi.get(ParseInput.PARAM_MINCOV[ParseInput.FIELD_NAME]));
        min_percent = Float.valueOf(pi.get(ParseInput.PARAM_MINPERCENT[ParseInput.FIELD_NAME]));
        percent_diff = Float.valueOf(pi.get(ParseInput.PARAM_PERCENT_DIFF[ParseInput.FIELD_NAME]));
        max_n_ratio = Float.valueOf(pi.get(ParseInput.PARAM_MAX_NORM_RAT[ParseInput.FIELD_NAME]));
        library = pi.get(ParseInput.PARAM_LIB[ParseInput.FIELD_NAME]);
        sampledb = pi.get_flag(ParseInput.PARAM_SAMPLEDB[ParseInput.FIELD_NAME]);
    }

    public static void main(String[] args) {
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        Thread th = new Thread(LB);
        th.start();
        new CurrentVersion(LB);
        LB.Version("Experimental Record Generator", "$Revision: 3505 $");
        HashMap<String, String> Variables = null;
        try {
            Variables = CommandLine.process_CLI(args);
        } catch (CommandLineProcessingException CLPE) {
            LB.error(CLPE.getMessage());
            LB.die();
        }
        parse_input(Variables);
        assert (Variables != null);
        if (!out_path.endsWith(System.getProperty("file.separator"))) {
            out_path = out_path.concat(System.getProperty("file.separator")).trim();
        }
        Ensembl ens = Ensembl.init(LB, "human", conf_file, "A");
        PSQLutils psqlu = new PSQLutils(LB, psql_file, db_name);
        int[] library_id = new int[1];
        library_id[0] = psqlu.get_library_id(library);
        psqlu.close();
        EnsemblMaps map = new EnsemblMaps(LB, ens);
        long progStart = System.currentTimeMillis();
        for (int current_chromosome = 0; current_chromosome < ens.get_number_of_chromosomes(); current_chromosome++) {
            psqlu = new PSQLutils(LB, psql_file, db_name);
            SNPsInRegion sir = new SNPsInRegion(LB, psqlu, ens, psql_file, sampledb);
            IndelsInRegion.setPSQLutils(psqlu);
            long start = System.currentTimeMillis();
            String chr_name = ens.get_chr_filename(current_chromosome);
            LB.notice("* Starting Processing on chromosome " + chr_name);
            LB.debug("\t" + Time.TimeTaken(start) + "\t\t\ttotal: " + Time.TimeTaken(progStart));
            LB.debug(" * Processing junction SNPs");
            start = System.currentTimeMillis();
            FileOut junction_full = new FileOut(LB, out_path + chr_name + ".junctions.rpt", false);
            FileOut junction_summary = new FileOut(LB, out_path + chr_name + ".junctions.rpt.summary", false);
            HTMLOut junction_html = new HTMLOut(LB, out_path + chr_name + ".junctions.html");
            junction_html.head_standard();
            junction_html.newtable();
            JunctionSnps.getJunctionSnps2(LB, library_id, sir, chr_name, min_obs, min_cov, min_percent, map, junction_full, junction_summary, junction_html, false, max_n_ratio, percent_diff);
            junction_html.endtable();
            junction_html.close();
            junction_full.close();
            junction_summary.close();
            LB.debug("\t" + Time.TimeTaken(start) + "\t\t\ttotal: " + Time.TimeTaken(progStart));
            sir.close();
            IndelsInRegion.close();
            psqlu.close();
        }
        map.close();
        ens.destroy();
        LB.close();
    }
}
