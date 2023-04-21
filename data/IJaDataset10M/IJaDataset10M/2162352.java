package src.projects.MutationAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import src.lib.CommandLine;
import src.lib.CurrentVersion;
import src.lib.Ensembl;
import src.lib.ParseInput;
import src.lib.Error_handling.CommandLineProcessingException;
import src.lib.ioInterfaces.FileIn;
import src.lib.ioInterfaces.FileOut;
import src.lib.ioInterfaces.HTMLOut;
import src.lib.ioInterfaces.Log_Buffer;
import src.lib.objects.Triplet;
import src.lib.objects.Tuple;
import src.projects.MutationAnalysis.Utils.AlignmentUtils;
import src.projects.MutationAnalysis.Utils.AnnotationDButils;
import src.projects.MutationAnalysis.Utils.VariationUtils;
import src.projects.MutationAnalysis.objects.AnnotationSummary;
import src.projects.MutationAnalysis.objects.BioMartAnnotRecord;
import src.projects.MutationAnalysis.objects.Constants;
import src.projects.Utilities.Utils;
import src.projects.VariationDatabase.libs.PSQLutils;
import src.projects.VariationDatabase.libs.VariationDButils;
import src.projects.VariationDatabase.objects.EnsemblMaps;

/**
 * 
 * @author alirezak
 * @version 1.0
 * 
 * This annotate gene functions for the genes in a variation cluster report.
 */
public class ClusterReports {

    private static final boolean DEBUG = true;

    private ClusterReports() {
    }

    private static String outPath;

    private static String inputPath;

    private static String psqlFile;

    private static String confFile;

    private static Log_Buffer LB;

    private static PSQLutils psqlu;

    private static PSQLutils psqluAnnDB;

    private static double maxPvalue;

    private static HashMap<String, BioMartAnnotRecord> bioMartAnnotations = null;

    private static String ucscDataPath;

    private static String biomartFile;

    private static String ucscURL;

    private static String targetCancer;

    private static String variationDatabase;

    private static String annotationDatabase;

    static final Comparator<Triplet<Double, ArrayList<String>, ArrayList<String>>> ORDER_ROWS = new Comparator<Triplet<Double, ArrayList<String>, ArrayList<String>>>() {

        public int compare(Triplet<Double, ArrayList<String>, ArrayList<String>> e1, Triplet<Double, ArrayList<String>, ArrayList<String>> e2) {
            int r = 0;
            if (e1.get_first() - e2.get_first() < 0) {
                r = -1;
            } else if (e1.get_first() - e2.get_first() > 0) {
                r = 1;
            }
            return r;
        }
    };

    static final Comparator<Tuple<Integer, HashSet<String>>> ORDER_VARIATIONS = new Comparator<Tuple<Integer, HashSet<String>>>() {

        public int compare(Tuple<Integer, HashSet<String>> e1, Tuple<Integer, HashSet<String>> e2) {
            return e1.get_first() - e2.get_first();
        }
    };

    private static ArrayList<String> req_param = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add(ParseInput.PARAM_PSQL[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_CONF[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_OUT_PATH[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_IN_PATH[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_UCSC_PATH[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_UCSC_URL[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_CANCER_TYPE[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_ANNOT_DB_NAME[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_DB_NAME[ParseInput.FIELD_NAME]);
        }
    };

    private static ArrayList<String> opt_param = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add(ParseInput.PARAM_MAX_PVALUE[ParseInput.FIELD_NAME]);
            add(ParseInput.PARAM_BIOMART_FILE[ParseInput.FIELD_NAME]);
        }
    };

    private static void parse_input(HashMap<String, String> Variables) {
        ParseInput pi = new ParseInput(LB, req_param, opt_param);
        pi.parse_input(Variables);
        psqlFile = pi.get(ParseInput.PARAM_PSQL[ParseInput.FIELD_NAME]);
        confFile = pi.get(ParseInput.PARAM_CONF[ParseInput.FIELD_NAME]);
        outPath = pi.get(ParseInput.PARAM_OUT_PATH[ParseInput.FIELD_NAME]);
        inputPath = pi.get(ParseInput.PARAM_IN_PATH[ParseInput.FIELD_NAME]);
        pi.get(ParseInput.PARAM_VARIATION_PATH[ParseInput.FIELD_NAME]);
        ucscDataPath = pi.get(ParseInput.PARAM_UCSC_PATH[ParseInput.FIELD_NAME]);
        ucscURL = pi.get(ParseInput.PARAM_UCSC_URL[ParseInput.FIELD_NAME]);
        biomartFile = pi.get(ParseInput.PARAM_BIOMART_FILE[ParseInput.FIELD_NAME]);
        maxPvalue = Double.valueOf(pi.get(ParseInput.PARAM_MAX_PVALUE[ParseInput.FIELD_NAME]));
        targetCancer = pi.get(ParseInput.PARAM_CANCER_TYPE[ParseInput.FIELD_NAME]);
        annotationDatabase = pi.get(ParseInput.PARAM_ANNOT_DB_NAME[ParseInput.FIELD_NAME]);
        variationDatabase = pi.get(ParseInput.PARAM_DB_NAME[ParseInput.FIELD_NAME]);
    }

    public static final void htmlHeader(HTMLOut html) {
        ArrayList<String> rowData = new ArrayList<String>();
        ArrayList<String> rowProp = new ArrayList<String>();
        rowData.add("Rank");
        rowProp.add("class=\"header\" width=\"50\"");
        rowData.add("Region_ID");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("Gene_Name");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("P_Value");
        rowProp.add("class=\"header\" width=\"150\"");
        rowData.add("Chromosome");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("Start");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("End");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("RefSeq Summary");
        rowProp.add("class=\"header\" width=\"500\"");
        rowData.add("Aceview Functions");
        rowProp.add("class=\"header\"  width=\"500\"");
        for (String s : BioMartAnnotRecord.header().split("\t")) {
            if (s.toLowerCase().contains("description") || s.toLowerCase().contains("definition")) {
                rowData.add(s);
                rowProp.add("class=\"header\"  width=\"300\"");
            } else {
                rowData.add(s);
                rowProp.add("class=\"header\"  width=\"100\"");
            }
        }
        rowData.add("#Variations");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("#Mutated samples");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("#Verified variations");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("#Samples with verified variations");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("#Verified SOMATIC variations");
        rowProp.add("class=\"header\" width=\"100\"");
        rowData.add("#Samples with SOMATIC variations");
        rowProp.add("class=\"header\" width=\"100\"");
        html.tableRow(rowData, rowProp);
    }

    private static void generateHTMLReport(String input, String output) {
        String[] line = null;
        HTMLOut html = new HTMLOut(LB, output + Constants.HTML_FILE_EXTENTION);
        HTMLOut htmlUnsorted = new HTMLOut(LB, output + ".unsorted.html");
        html.head_fixed_width();
        html.newtable();
        htmlHeader(html);
        htmlUnsorted.head_fixed_width();
        htmlUnsorted.newtable();
        htmlHeader(htmlUnsorted);
        ArrayList<Triplet<Double, ArrayList<String>, ArrayList<String>>> allRows = new ArrayList<Triplet<Double, ArrayList<String>, ArrayList<String>>>();
        int c = 0;
        int n = Utils.getFilesInDir(input).length;
        for (String f : Utils.getFilesInDir(input)) {
            if (!Utils.fileExists(input + "/" + f + "/" + f + ".completed")) {
                LB.warning("the completed file does not exists in " + f);
                continue;
            }
            int annotId = Integer.valueOf(f.split("-")[f.split("-").length - 1]);
            AnnotationSummary annotSummary = AnnotationDButils.getAnnotationSummaryById(annotId);
            if (annotSummary == null) {
                LB.warning("annotation with id " + annotId + " was not found in the database");
                continue;
            }
            if (!Utils.fileExists(input + "/" + f + "/" + f + Constants.SUMMARY_FILE_EXTENTION)) {
                LB.warning("the summary file does not exists in " + f);
                continue;
            }
            FileIn summary = new FileIn(LB, input + "/" + f + "/" + f + Constants.SUMMARY_FILE_EXTENTION, "\t", false);
            line = summary.get_next();
            if (line == null) {
                LB.warning("the summary file is empty in " + f);
                summary.close();
                continue;
            }
            int totalVariations = Integer.valueOf(line[0].split(":")[1]);
            int verifiedVariations = Integer.valueOf(line[1].split(":")[1]);
            int samplesWithVariations = Integer.valueOf(line[2].split(":")[1]);
            HashSet<String> libsWithMutations = new HashSet<String>();
            HashSet<String> libsWithSomaticMutations = new HashSet<String>();
            int somaticMutations = 0;
            while ((line = summary.get_next()) != null) {
                if (line.length < 12) {
                    continue;
                }
                boolean isSomatic = false;
                for (String l : line[11].split(",")) {
                    String[] ll = l.split(":");
                    if (ll.length < 2) {
                        LB.error("format error in " + input + "/" + f + "/" + f + Constants.SUMMARY_FILE_EXTENTION);
                        LB.error("expects library_id:variation_type");
                        LB.die();
                    }
                    libsWithMutations.add(ll[0]);
                    if (ll[1].contentEquals("SOMATIC")) {
                        isSomatic = true;
                        libsWithSomaticMutations.add(ll[0]);
                    }
                }
                if (isSomatic) {
                    somaticMutations++;
                }
            }
            double p = (annotSummary.getEnd() - annotSummary.getStart() + 1) / (Constants.GENOME_LENGTH);
            double pv = VariationUtils.logBinomialBasedProb(p, Constants.NUM_SUMATIC_SNVS, verifiedVariations);
            pv = Math.pow(10, pv);
            Triplet<Double, ArrayList<String>, ArrayList<String>> tableRow = getGeneFunction(annotSummary, f, pv);
            if (tableRow == null) {
                continue;
            }
            String url = "file://" + input + "/" + f + "/" + f + Constants.VARIATION_FILE_EXTENTION;
            tableRow.get_second().add(String.valueOf("<a href=" + url + ">" + totalVariations + "</a>"));
            tableRow.get_third().add("");
            tableRow.get_second().add(String.valueOf(samplesWithVariations));
            tableRow.get_third().add("");
            url = "file://" + input + "/" + f + "/" + f + Constants.SUMMARY_FILE_EXTENTION;
            tableRow.get_second().add(String.valueOf("<a href=" + url + ">" + verifiedVariations + "</a>"));
            tableRow.get_third().add("");
            tableRow.get_second().add(String.valueOf(libsWithMutations.size()));
            tableRow.get_third().add("");
            tableRow.get_second().add(String.valueOf(somaticMutations));
            tableRow.get_third().add("");
            tableRow.get_second().add(String.valueOf(libsWithSomaticMutations.size()));
            tableRow.get_third().add("");
            url = "file://" + input + "/" + f + "/" + f + Constants.HTML_FILE_EXTENTION;
            tableRow.get_second().add(0, "<a href=" + url + ">alignment</a>");
            tableRow.get_third().add(0, "");
            htmlUnsorted.tableRow(tableRow.get_second(), tableRow.get_third());
            htmlUnsorted.flush();
            allRows.add(tableRow);
            if (c > 5) {
            }
            if (++c % 100 == 0) {
                LB.debug(" *** processed " + c + "/" + n + " regions");
            }
        }
        htmlUnsorted.endtable();
        htmlUnsorted.close();
        Collections.sort(allRows, ORDER_ROWS);
        ArrayList<Double> pvalues = new ArrayList<Double>();
        for (Triplet<Double, ArrayList<String>, ArrayList<String>> t : allRows) {
            pvalues.add(t.get_first());
        }
        ArrayList<Double> qvalues = VariationUtils.benjaminiCorrection(pvalues);
        int i = 0;
        for (Triplet<Double, ArrayList<String>, ArrayList<String>> t : allRows) {
            if (qvalues.get(i) > maxPvalue) {
                break;
            }
            t.get_second().set(3, qvalues.get(i) + "");
            String rank = t.get_second().get(0);
            rank = rank.replace(">alignment</a>", ">" + (++i) + "</a>");
            t.get_second().set(0, rank);
            html.tableRow(t.get_second(), t.get_third());
            html.flush();
        }
        html.endtable();
        html.close();
    }

    /**
	 * Returns a table row containing the function and other annotations of the gene.
	 * If the pvalue of the gene is larger than the threshold it returns null.
	 * @param annotSummary
	 * @param regionPath
	 * @param pvalue
	 * @return
	 */
    private static Triplet<Double, ArrayList<String>, ArrayList<String>> getGeneFunction(AnnotationSummary annotSummary, String regionPath, double pvalue) {
        ArrayList<String> rowData = new ArrayList<String>();
        ArrayList<String> rowProp = new ArrayList<String>();
        if (pvalue > maxPvalue) {
            return null;
        }
        if (annotSummary == null) {
            return null;
        }
        String chromosome = annotSummary.getChr();
        String regionName = annotSummary.getDisplayId();
        int start = annotSummary.getStart();
        int end = annotSummary.getEnd();
        String trackFileName = createUCSCTracks(annotSummary, inputPath, regionPath, targetCancer, ucscDataPath);
        String geneEnsemblId = "";
        String geneName = "";
        if (!DEBUG) {
            Tuple<String, String> t = EnsemblMaps.getEnsmeblIDInRegion(regionName, chromosome, start, end);
            if (t != null) {
                geneEnsemblId = t.get_second();
                geneName = getGeneNameFromBioMart(geneEnsemblId);
            }
        }
        rowData.add(regionName);
        rowProp.add("");
        rowData.add(geneName);
        rowProp.add("");
        rowData.add(String.valueOf(pvalue));
        rowProp.add("");
        String url = "\"http://genome.ucsc.edu/cgi-bin/hgTracks?db=hg18&position=chr" + chromosome + "%3A" + start + "-" + end + "&hgt.customText=" + ucscURL + "/" + trackFileName + "\"";
        if (Utils.fileExists(ucscDataPath + "/" + trackFileName)) {
            url = "\"http://genome.ucsc.edu/cgi-bin/hgTracks?db=hg18&position=chr" + chromosome + "%3A" + start + "-" + end + "&hgt.customText=" + ucscURL + "/" + trackFileName + "\"";
        }
        rowData.add("<a href=" + url + ">" + chromosome + "</a>");
        rowProp.add("");
        rowData.add(String.valueOf(start));
        rowProp.add("");
        rowData.add(String.valueOf(end));
        rowProp.add("");
        Tuple<ArrayList<String>, ArrayList<String>> aceViewData = null;
        if (!DEBUG) {
            aceViewData = getGeneAnnotationFromAceView(geneName);
        }
        if (aceViewData != null) {
            rowData.addAll(aceViewData.get_first());
            rowProp.addAll(aceViewData.get_second());
        } else {
            for (int i = 0; i < Constants.ACEVIEW_COLUMNS; i++) {
                rowData.add("");
                rowProp.add("");
            }
        }
        Tuple<ArrayList<String>, ArrayList<String>> geneAnnot = null;
        if (!DEBUG) {
            geneAnnot = getGeneAnnotationFromBioMart(geneEnsemblId);
        }
        if (geneAnnot != null) {
            rowData.addAll(geneAnnot.get_first());
            rowProp.addAll(geneAnnot.get_second());
        } else {
            for (int i = 0; i < Constants.BIOMART_COLUMNS; i++) {
                rowData.add("");
                rowProp.add("");
            }
        }
        return new Triplet<Double, ArrayList<String>, ArrayList<String>>(pvalue, rowData, rowProp);
    }

    /**
	 * returns the contents of the AceView page for gene geneId.
	 * @param geneId
	 * @return
	 */
    private static ArrayList<String> getAceViewPageForGene(String geneId) {
        String url = "http://www.ncbi.nlm.nih.gov/IEB/Research/Acembly/av.cgi?db=human&c=gene&a=fiche&q=" + geneId;
        BufferedReader reader = null;
        try {
            LB.debug("retreiveing " + url);
            reader = Utils.readURL(url);
        } catch (Exception e1) {
            return null;
        }
        String line = "";
        ArrayList<String> out = new ArrayList<String>();
        try {
            while ((line = reader.readLine()) != null) {
                out.add(line);
            }
        } catch (IOException e) {
            return out;
        }
        return out;
    }

    private static Tuple<ArrayList<String>, ArrayList<String>> getGeneAnnotationFromAceView(String geneId) {
        if (geneId == null || geneId.length() == 0) {
            return null;
        }
        ArrayList<String> sb = getAceViewPageForGene(geneId);
        if (sb == null || sb.size() == 0) {
            return null;
        }
        String refSeqSummary = "";
        String functions = "";
        for (int i = 0; i < sb.size(); ) {
            if (sb.get(i).contains("tg_RefSeq_Summary")) {
                if (sb.get(i + 1).contains("<br") && sb.get(i + 3).contains("<br")) {
                    refSeqSummary = sb.get(i + 2);
                } else {
                    i++;
                    continue;
                }
            }
            if (sb.get(i).startsWith("<b>Function:</b>")) {
                String url = "\"http://www.ncbi.nlm.nih.gov/IEB/Research/Acembly/av.cgi?db=human&c=gene&a=fiche&q=" + geneId + "\"";
                functions = sb.get(i).substring(sb.get(i).indexOf("<b>Function:</b>")) + "<a href=" + url + ">:aceview </a>";
            }
            i++;
        }
        ArrayList<String> rowData = new ArrayList<String>();
        ArrayList<String> propData = new ArrayList<String>();
        rowData.add(refSeqSummary);
        propData.add("width=\"200\"");
        rowData.add(functions);
        propData.add("width=\"200\"");
        return new Tuple<ArrayList<String>, ArrayList<String>>(rowData, propData);
    }

    /**
	 * return HUGO gene name by gene ensemble ID
	 * @param geneId
	 * @return
	 */
    private static String getGeneNameFromBioMart(String geneId) {
        if (bioMartAnnotations == null) {
            importBioMartAnnotations(biomartFile);
        }
        if (bioMartAnnotations == null) {
            return null;
        }
        if (bioMartAnnotations.containsKey(geneId)) {
            return bioMartAnnotations.get(geneId).getGeneName();
        } else {
            return "";
        }
    }

    private static Tuple<ArrayList<String>, ArrayList<String>> getGeneAnnotationFromBioMart(String geneId) {
        if (bioMartAnnotations == null) {
            importBioMartAnnotations(biomartFile);
        }
        if (bioMartAnnotations == null) {
            return null;
        }
        ArrayList<String> rowData = new ArrayList<String>();
        ArrayList<String> propData = new ArrayList<String>();
        if (bioMartAnnotations.containsKey(geneId)) {
            for (String attr : bioMartAnnotations.get(geneId).getAttrArrayForTable()) {
                rowData.add(attr);
                propData.add("");
            }
        } else {
            for (int i = 0; i < BioMartAnnotRecord.NUM_ATTRIBUTES; i++) {
                rowData.add("");
                propData.add("");
            }
        }
        return new Tuple<ArrayList<String>, ArrayList<String>>(rowData, propData);
    }

    private static void importBioMartAnnotations(String input) {
        if (input == null) {
            LB.debug("no biomart file was specified");
            return;
        }
        LB.debug("Loading bioMart annotations from " + input);
        bioMartAnnotations = new HashMap<String, BioMartAnnotRecord>();
        FileIn in = new FileIn(LB, input, "", false);
        String[] line = null;
        int lineCount = 0;
        while ((line = in.get_next()) != null) {
            String[] ss = line[0].split("\t", -1);
            int start = -1;
            int end = -1;
            if (ss.length < 19) {
                continue;
            }
            try {
                start = Integer.parseInt(ss[4]);
                end = Integer.parseInt(ss[5]);
            } catch (Exception e) {
                continue;
            }
            String geneName = ss[0];
            lineCount++;
            if (lineCount % 100000 == 0) {
                LB.debug("processed " + lineCount + " lines");
            }
            BioMartAnnotRecord bmr = new BioMartAnnotRecord(ss[0], ss[2], ss[3], start, end, ss[6], ss[7], ss[9], ss[10], ss[11], ss[12], ss[13], ss[14], ss[15], ss[16], ss[17], ss[18]);
            bioMartAnnotations.put(geneName, bmr);
        }
        LB.notice("imported " + bioMartAnnotations.keySet().size() + " annotations from BioMart " + lineCount);
    }

    /**
	 * 
	 * @param annotSummary
	 * @param input
	 * @param regionName
	 * @param targetCancer
	 * @param ucscDataPath
	 * @return
	 */
    private static String createUCSCTracks(AnnotationSummary annotSummary, String input, String regionName, String targetCancer, String ucscDataPath) {
        String[] line = null;
        boolean first = true;
        StringBuffer trackData = new StringBuffer();
        trackData.append("browser position chr" + annotSummary.getChr() + ":" + annotSummary.getStart() + "-" + annotSummary.getEnd() + "\n");
        if (Utils.fileExists(input + "/" + regionName + "/" + regionName + Constants.SUMMARY_FILE_EXTENTION)) {
            FileIn in = new FileIn(LB, input + "/" + regionName + "/" + regionName + Constants.SUMMARY_FILE_EXTENTION, "\t", true);
            LB.debug("reading " + input + "/" + regionName + "/" + regionName + Constants.SUMMARY_FILE_EXTENTION);
            StringBuffer tmp = new StringBuffer();
            String header = "track type=bedGraph name=\"" + regionName + "_verified\" description=\"" + targetCancer + "_verified_variations" + "\" visibility=full color=250,0,0 altColor=200,0,0 priority=20 autoScale=off alwaysZero=on maxHeightPixels=100:100:50 viewLimits=0:10";
            tmp.append(header + "\n");
            boolean found = false;
            while ((line = in.get_next()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                if (line.length == 1) {
                    continue;
                }
                if (line.length < 11) {
                    LB.error("error in format " + Utils.stringArrayToString(line, "\t") + "\t" + line.length);
                    LB.die();
                }
                int start = Integer.valueOf(line[3]);
                int end = Integer.valueOf(line[4]);
                int cancerCount = line[10].split(",").length;
                tmp.append("chr" + line[1] + "\t" + start + "\t" + end + "\t" + cancerCount + "\n");
                found = true;
            }
            if (found) {
                trackData.append(tmp.toString());
            }
            in.close();
        }
        if (Utils.fileExists(input + "/" + regionName + "/" + regionName + Constants.VARIATION_FILE_EXTENTION)) {
            StringBuffer tmp = new StringBuffer();
            FileIn in = new FileIn(LB, input + "/" + regionName + "/" + regionName + Constants.VARIATION_FILE_EXTENTION, "\t", false);
            String header = "track type=bedGraph name=\"" + regionName + "_all\" description=\"" + targetCancer + "_all_variations" + "\" visibility=full color=0,0,0 altColor=0,0,200 priority=20 autoScale=off alwaysZero=on maxHeightPixels=100:100:50 viewLimits=0:10";
            tmp.append(header + "\n");
            boolean found = false;
            while ((line = in.get_next()) != null) {
                if (line.length < 5) {
                    LB.error("error in format " + Utils.stringArrayToString(line, "\t") + "\t" + line.length);
                    LB.die();
                }
                int start = Integer.valueOf(line[2]);
                int end = Integer.valueOf(line[2]);
                int cancerCount = 1;
                if (line.length == 7) {
                    cancerCount = line[6].split(",").length;
                }
                tmp.append("chr" + line[1] + "\t" + start + "\t" + end + "\t" + cancerCount + "\n");
                found = true;
            }
            if (found) {
                trackData.append(tmp.toString());
            }
            in.close();
        }
        String header = "track type=bedGraph name=\"TARGET_REGION\" description=\"" + regionName + "\" visibility=full color=0,0,0 altColor=0,200,200 priority=20 autoScale=off alwaysZero=on maxHeightPixels=100:100:50 viewLimits=0:10";
        trackData.append(header + "\n");
        trackData.append("chr" + annotSummary.getChr() + "\t" + annotSummary.getStart() + "\t" + annotSummary.getEnd() + "\t1\n");
        FileOut trackFile = new FileOut(LB, ucscDataPath + "/" + regionName + ".bed_graph", false);
        trackFile.writeln(trackData.toString());
        trackFile.close();
        return regionName + ".bed_graph";
    }

    public static void main(String[] args) {
        LB = Log_Buffer.getLogBufferInstance();
        LB.addPrintStream(System.out);
        Thread th = new Thread(LB);
        th.start();
        new CurrentVersion(LB);
        LB.Version("Finding Common Somatic Variations", "$Revision: 1.0 $");
        HashMap<String, String> Variables = null;
        try {
            Variables = CommandLine.process_CLI(args);
        } catch (CommandLineProcessingException CLPE) {
            LB.error(CLPE.getMessage());
            LB.die();
        }
        parse_input(Variables);
        assert (Variables != null);
        Ensembl.init(LB, "human", confFile, "A");
        Ensembl ens = new Ensembl(LB, "human");
        EnsemblMaps ensMap = new EnsemblMaps(LB, ens);
        psqlu = new PSQLutils(LB, psqlFile, variationDatabase);
        VariationDButils.init(LB, psqlu);
        psqluAnnDB = new PSQLutils(LB, psqlFile, annotationDatabase);
        AnnotationDButils.init(LB, psqluAnnDB);
        AlignmentUtils.init(LB, psqlu, psqluAnnDB);
        Utils.creatDirectory(outPath);
        generateHTMLReport(inputPath, outPath);
        psqlu.close();
        psqluAnnDB.close();
        ensMap.close();
        LB.close();
    }
}
