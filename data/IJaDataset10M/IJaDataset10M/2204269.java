package src.projects.VariationDatabase.ReportParsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import src.lib.CommandLine;
import src.lib.ParseInput;
import src.lib.Error_handling.CommandLineProcessingException;
import src.lib.ioInterfaces.FileIn;
import src.lib.ioInterfaces.FileOut;
import src.lib.ioInterfaces.Log_Buffer;

/**
 * This class indel lists.
 * @author afejes
 * @version $Revision: 3313 $
 */
public class processAssemblyIndel {

    private processAssemblyIndel() {
    }

    private static String file_in;

    private static String file_out;

    private static Log_Buffer LB;

    private static final String[] cancer_ids = { "HS1623", "HS1634", "HS1744", "HS1745", "HS1804", "HS1805", "HS1849", "HS2116" };

    private static final String[] normal_ids = { "HS1850", "HS2004", "HS2005", "HS2006" };

    private static ArrayList<String> req_param = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add(ParseInput.PARAM_IN_FILE[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_OUT_FILE[ParseInput.FIELD_NAME]);
        }
    };

    private static ArrayList<String> opt_param = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
        }
    };

    private static void parse_input(HashMap<String, String> Variables) {
        ParseInput pi = new ParseInput(LB, req_param, opt_param);
        pi.parse_input(Variables);
        file_in = pi.get(ParseInput.PARAM_IN_FILE[ParseInput.FIELD_NAME]);
        file_out = pi.get(ParseInput.PARAM_OUT_FILE[ParseInput.FIELD_NAME]);
    }

    /**
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
        assert (Variables != null);
        FileIn fi = new FileIn(LB, file_in, "\t", false);
        String[] line = null;
        int insertions = 0;
        int deletions = 0;
        HashMap<String, ArrayList<String>> indels = new HashMap<String, ArrayList<String>>();
        while ((line = fi.get_next()) != null) {
            String lib = line[0];
            String type = line[1];
            String[] details = line[2].split(":");
            int obs = Integer.valueOf(line[3]);
            if (details.length < 2) {
                continue;
            }
            String gene = details[0];
            String var = details[3];
            String hash_key = gene + "\t" + var;
            if (type.equals("snv")) {
                continue;
            }
            if (type.equals("snv")) {
                continue;
            }
            boolean ignore_read = false;
            if (obs < 10) {
                for (String l : cancer_ids) {
                    if (l.equals(lib)) {
                        ignore_read = true;
                    }
                }
            }
            if (ignore_read) {
                continue;
            }
            if (type.equals("ins")) {
                insertions += 1;
            }
            if (type.equals("del")) {
                deletions += 1;
            }
            ArrayList<String> t = null;
            if (indels.containsKey(hash_key)) {
                t = indels.get(hash_key);
            } else {
                t = new ArrayList<String>();
            }
            if (!t.contains(lib)) {
                t.add(lib);
            }
            indels.put(hash_key, t);
        }
        String[] keyset = new String[indels.size()];
        keyset = indels.keySet().toArray(keyset);
        Arrays.sort(keyset);
        HashMap<String, char[]> byGene = new HashMap<String, char[]>();
        for (String s : keyset) {
            int j = 0;
            if (s.startsWith("PDX")) {
                j += 1;
            }
            LB.debug("j = " + j);
            ArrayList<String> lib_arr = indels.get(s);
            boolean report = true;
            for (String n_lib : normal_ids) {
                if (lib_arr.contains(n_lib)) {
                    report = false;
                }
            }
            if (lib_arr.size() <= 1) {
                report = false;
            }
            if (report) {
                String gene = s.split("\t")[0];
                char[] graph = null;
                if (byGene.containsKey(gene)) {
                    graph = byGene.get(gene);
                } else {
                    graph = new char[cancer_ids.length];
                    for (int i = 0; i < cancer_ids.length; i++) {
                        graph[i] = ' ';
                    }
                }
                for (int i = 0; i < cancer_ids.length; i++) {
                    if (lib_arr.contains(cancer_ids[i])) {
                        graph[i] = '▄';
                    }
                }
                byGene.put(gene, graph);
            }
        }
        HashMap<String, String> xlat_libs = new HashMap<String, String>();
        xlat_libs.put("HCC1187", "HS1745");
        xlat_libs.put("HCC1395", "HS1804");
        xlat_libs.put("UAC-893", "HS2116");
        xlat_libs.put("HCC1500", "HS1805");
        xlat_libs.put("HCC202", "HS1744");
        xlat_libs.put("HCC2218", "HS1849");
        xlat_libs.put("HCC38", "HS1623");
        xlat_libs.put("HCC70", "HS1634");
        FileIn snvs = new FileIn(LB, "/projects/afejes/DC/expt_rpt_2011_03_22/summary.genes", "\t", true);
        line = snvs.get_next();
        int[] order = new int[line.length];
        for (int i = 0; i < line.length; i++) {
            for (int j = 0; j < cancer_ids.length; j++) {
                if (xlat_libs.get(line[i]).equals(cancer_ids[j])) {
                    order[i] = j;
                }
            }
        }
        while ((line = snvs.get_next()) != null) {
            String gene = line[0];
            char[] t = null;
            if (byGene.containsKey(gene)) {
                t = byGene.get(gene);
            } else {
                t = new char[cancer_ids.length];
                for (int i = 0; i < cancer_ids.length; i++) {
                    t[i] = ' ';
                }
            }
            for (int i = 1; i < line.length; i++) {
                if (1 == Integer.valueOf(line[i])) {
                    char c = t[order[i - 1]];
                    if (c == '▄') {
                        t[order[i - 1]] = '█';
                    } else {
                        t[order[i - 1]] = '▀';
                    }
                }
            }
            byGene.put(gene, t);
        }
        keyset = new String[byGene.size()];
        keyset = byGene.keySet().toArray(keyset);
        Arrays.sort(keyset);
        FileOut fo = new FileOut(LB, file_out, false);
        for (String g : keyset) {
            char[] s = byGene.get(g);
            if (numch(s) > 1) {
                fo.writeln(numch(s) + "\t" + charArrayToString(s) + "\t" + g);
            }
        }
        fo.close();
        LB.debug("insertions = " + insertions);
        LB.debug("deletions  = " + deletions);
        fi.close();
        LB.close();
    }

    private static int numch(char[] c) {
        int r = 0;
        for (char i : c) {
            if (i != ' ') {
                r += 1;
            }
        }
        return r;
    }

    private static String charArrayToString(char[] c) {
        String r = "";
        for (char i : c) {
            r += i;
        }
        return r;
    }
}
