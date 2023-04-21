package gem;

import gem.parser.HGNCParser;
import gem.parser.HPRDParser;
import gem.parser.TabDelimitedFileParser;
import gem.util.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Ozgun Babur
 */
public class GRARAnalyzer {

    public static final double THR = 0.05;

    public static final DecimalFormat fmt = Constants.fmt;

    public static void main(String[] args) throws Throwable {
        printGeneChangeInExp();
        if (true) return;
        String dir = "resource/expdata/AR-GR/";
        boolean[][] pos = StageAnalyzer.getPos(dir);
        List<Triplet> trips = readTrips(dir);
        Gene gene = getGene(trips, "NCOA1");
        System.out.println("gene.id = " + gene.id);
        double[] means = getMeans(gene, pos);
        int[] vector = getChanges(gene, pos);
        System.out.println(str(means));
        System.out.println(str(vector));
        printChangeLikeliness(trips, pos[1], pos[2]);
    }

    public static List<Triplet> readTrips(String dir) throws Throwable {
        List<Triplet> trips = Triplet.readTrips("result/Result_fdr0.05_var10.0_AR_expo_andr.xls");
        for (Triplet t : trips) {
            t.backFromURLToIDs();
        }
        Triplet.removeNonModulation(trips);
        trips = Triplet.selectModulator(trips, "GAPDH");
        System.out.println("trips.size() = " + trips.size());
        CrossPlatformMapper.associateAndClean(trips, dir + "data.txt");
        System.out.println("trips.size() = " + trips.size());
        return trips;
    }

    public static double[] getMeans(Gene gene, boolean[][] pos) {
        double[] means = new double[pos.length];
        for (int i = 0; i < means.length; i++) {
            means[i] = CellTypeMatcher.getMeanValue(gene, pos[i]);
        }
        return means;
    }

    public static int[] getChangesTwo(Gene gene, boolean[][] pos) {
        int[] change = new int[2];
        double[] mean = getMeans(gene, pos);
        double pvF = CellTypeMatcher.getChangePvalBetweenTissues(gene, pos[1], pos[0]);
        double pvM = CellTypeMatcher.getChangePvalBetweenTissues(gene, pos[1], pos[2]);
        change[0] = pvF > THR ? 0 : mean[0] > mean[1] ? 1 : -1;
        change[1] = pvM > THR ? 0 : mean[2] > mean[1] ? 1 : -1;
        return change;
    }

    public static int[] getChanges(Gene gene, boolean[][] pos) {
        int[] change = new int[(int) Combination.calc(pos.length, 2)];
        double[] mean = getMeans(gene, pos);
        int k = 0;
        for (int i = 0; i < pos.length - 1; i++) {
            for (int j = i + 1; j < pos.length; j++) {
                double pv = CellTypeMatcher.getChangePvalBetweenTissues(gene, pos[i], pos[j]);
                change[k++] = pv > THR ? 0 : mean[j] > mean[i] ? 1 : -1;
            }
        }
        return change;
    }

    public static void printChangeLikeliness(List<Triplet> trips, boolean[] pos1, boolean[] pos2) {
        int[][] cnt = new int[6][2];
        int[] signcnt = new int[2];
        for (Triplet t : trips) {
            double pv = CellTypeMatcher.getChangePvalBetweenTissues(t.T, pos1, pos2);
            if (pv > THR) continue;
            int ch = (int) Math.signum(CellTypeMatcher.getMeanChange(t.T, pos1, pos2));
            signcnt[ch > 0 ? 1 : 0]++;
            for (int i = 0; i < cnt.length; i++) {
                int coef = getRelatedCoefficientSign(t, i);
                if (coef == 0) continue;
                if (coef * ch > 0) cnt[i][1]++; else cnt[i][0]++;
                if (i == 4 && coef * ch < 0) System.out.println(t.categ);
            }
        }
        System.out.println("tar signs = " + signcnt[0] + " " + signcnt[1]);
        for (int i = 0; i < cnt.length; i++) {
            System.out.println(i + "\t" + cnt[i][0] + "\t" + cnt[i][1] + "\t" + Binomial.getPval(cnt[i][0], cnt[i][1]));
        }
    }

    private static int getRelatedCoefficientSign(Triplet t, int index) {
        int[] p = getPosForInd(index);
        int c1 = t.cnt[p[1]];
        int n1 = t.cnt[p[0]] + c1;
        int c2 = t.cnt[p[3]];
        int n2 = t.cnt[p[2]] + c2;
        double p1 = c1 / (double) n1;
        double p2 = c2 / (double) n2;
        double pval = Difference.calcPairwisePval(c1, n1, c2, n2);
        if (pval > THR) return 0;
        return (int) Math.signum(p2 - p1);
    }

    private static int[] getPosForInd(int index) {
        if (index == 0) return new int[] { 0, 4, 1, 5 };
        if (index == 1) return new int[] { 0, 4, 2, 6 };
        if (index == 2) return new int[] { 0, 4, 3, 7 };
        if (index == 3) return new int[] { 1, 5, 2, 6 };
        if (index == 4) return new int[] { 1, 5, 3, 7 };
        if (index == 5) return new int[] { 2, 6, 3, 7 };
        return null;
    }

    public static String str(int[] x) {
        String s = "";
        for (int i : x) {
            s += "\t" + i;
        }
        return s;
    }

    public static String str(double[] x) {
        String s = "";
        for (Double i : x) {
            s += "\t" + fmt.format(i);
        }
        return s;
    }

    static Gene getGene(List<Triplet> trips, String id) {
        for (Triplet t : trips) {
            if (t.M.id.equals(id)) return t.M;
            if (t.F.id.equals(id)) return t.F;
            if (t.T.id.equals(id)) return t.T;
        }
        return null;
    }

    private static void printAndrResponsiveTarChange() throws Throwable {
        TabDelimitedFileParser parser = new TabDelimitedFileParser("resource/factors/AR_andr_small.txt");
        Map<String, String> scores = parser.getOneToOneMap("Target", "Score");
        Set<String> ids = new HashSet<String>();
        for (String sym : scores.keySet()) ids.add(Triplet.getSymbolToGeneMap().get(sym));
        String dir = "resource/expdata/GSE11428/";
        Map<String, Gene> geneMap = ExpDataReader.readGenes(ids, dir, 0, 0);
        parser = new TabDelimitedFileParser("resource/factors/AR-select-small.txt");
        Set<String> select = parser.getColumnSet(0);
        boolean[][] pos = StageAnalyzer.getPos(dir);
        String[] stage = StageAnalyzer.getStageNames(dir);
        for (String s : stage) {
            System.out.print("\t" + s);
        }
        for (int i = 0; i < pos.length; i++) {
            System.out.print("\n" + stage[i]);
            for (int j = 0; j < pos.length; j++) {
                if (i == j) {
                    System.out.print("\t");
                    continue;
                }
                int same = 0;
                int oppo = 0;
                List<String> ss = new ArrayList<String>();
                List<String> so = new ArrayList<String>();
                int[] signcnt = new int[2];
                int cnt = 0;
                for (String id : geneMap.keySet()) {
                    String sym = Triplet.getGeneToSymbolMap().get(id);
                    if (sym == null) sym = id;
                    int response = scores.get(sym).startsWith("-") ? -1 : 1;
                    cnt++;
                    Gene gene = geneMap.get(sym);
                    if (gene == null) gene = geneMap.get(id);
                    double pv = CellTypeMatcher.getChangePvalBetweenTissues(gene, pos[i], pos[j]);
                    if (pv > 0.05) continue;
                    int ch = (int) Math.signum(CellTypeMatcher.getMeanChange(gene, pos[i], pos[j]));
                    signcnt[ch > 0 ? 1 : 0]++;
                    if (ch * response == 1) {
                        same++;
                        ss.add(sym);
                    } else {
                        oppo++;
                        so.add(sym);
                    }
                }
                System.out.print("\t" + same + " | " + oppo);
            }
        }
        System.out.println();
    }

    public static void printGeneChangeInExp() throws Throwable {
        TabDelimitedFileParser p = new TabDelimitedFileParser("resource/NuclearReceptors.txt");
        Set<String> modNames = p.getColumnSet(0);
        Set<String> ids = new HashSet<String>();
        for (String name : modNames) {
            ids.add(Triplet.getSymbolToGeneMap().get(name));
        }
        String dir = "resource/expdata/GSE11428/";
        Map<String, Gene> geneMap = ExpDataReader.readGenes(ids, dir, 0, 0);
        boolean[][] pos = StageAnalyzer.getPos(dir);
        int i = 0;
        int j = 1;
        int k = 2;
        int l = 3;
        System.out.println("geneMap = " + geneMap.size());
        List<String> select = new ArrayList<String>();
        List<String> idList = new ArrayList<String>(geneMap.keySet());
        for (String id : HGNCParser.sortToSymb(idList)) {
            String sym = Triplet.getGeneToSymbolMap().get(id);
            if (sym == null) sym = id;
            Gene gene = geneMap.get(sym);
            if (gene == null) gene = geneMap.get(id);
            double pv = CellTypeMatcher.getChangePvalBetweenTissues(gene, pos[i], pos[j]);
            double before = (CellTypeMatcher.getMeanValue(gene, pos[i]));
            double after = (CellTypeMatcher.getMeanValue(gene, pos[j]));
            double ch = after - before;
            double fold = after / before;
            double pv2 = CellTypeMatcher.getChangePvalBetweenTissues(gene, pos[k], pos[l]);
            double before2 = (CellTypeMatcher.getMeanValue(gene, pos[k]));
            double after2 = (CellTypeMatcher.getMeanValue(gene, pos[l]));
            double ch2 = after2 - before2;
            double fold2 = after2 / before2;
            double thr = 1.5;
            if ((fold > thr || fold < 1 / thr) && pv < 0.05) {
                System.out.println(sym + "\t" + ch + "\t" + fold + "\t" + pv);
                System.out.println(sym + "\t" + ch2 + "\t" + fold2 + "\t" + pv2);
                select.add(sym + (ch > 0 ? "+" : "-"));
            }
        }
        System.out.println("cnt = " + select.size());
        for (String s : select) {
        }
    }

    public static void printGeneChangeDistr() throws Throwable {
        TabDelimitedFileParser p = new TabDelimitedFileParser("resource/NuclearReceptors.txt");
        Set<String> modNames = p.getColumnSet(0);
        Set<String> ids = new HashSet<String>();
        for (String name : modNames) {
            ids.add(Triplet.getSymbolToGeneMap().get(name));
        }
        String dir = "resource/expdata/philip/";
        Map<String, Gene> geneMap = ExpDataReader.readMouseHomologs(ids, dir, 0, 0);
        boolean[][] pos = StageAnalyzer.getPos(dir);
        int i = 3;
        int j = 4;
        for (String id : geneMap.keySet()) {
            String sym = Triplet.getGeneToSymbolMap().get(id);
            if (sym == null) sym = id;
            Gene gene = geneMap.get(sym);
            if (gene == null) gene = geneMap.get(id);
            double before = (CellTypeMatcher.getMeanValue(gene, pos[i]));
            Histogram h = new Histogram(0.1);
            for (int k = 0; k < pos[j].length; k++) {
                if (!pos[j][k]) continue;
                double fold = gene.value[k] / before;
                h.count(fold);
            }
            System.out.println("\n" + sym);
            h.print();
        }
    }

    public static void findCorrelationInTissue() throws Throwable {
        TabDelimitedFileParser p = new TabDelimitedFileParser("temp2");
        Set<String> syms = p.getColumnSet(0);
        String grSym = "ESR1";
        String arSym = "AR";
        syms.add(grSym);
        syms.add(arSym);
        p = new TabDelimitedFileParser("resource/factors/AR_andr_small.txt");
        Map<String, String> score = p.getOneToOneMap("Target", "Score");
        Set<String> ids = new HashSet<String>();
        for (String g : syms) ids.add(Triplet.getSymbolToGeneMap().get(g));
        String dir = "resource/expdata/expo";
        Map<String, Gene> map = ExpDataReader.readGenes(ids, dir, 0, 0);
        Set<Gene> tars = new HashSet<Gene>(map.values());
        Gene gr = map.get(Triplet.getSymbolToGeneMap().get(grSym));
        tars.remove(gr);
        Gene ar = map.get(Triplet.getSymbolToGeneMap().get(arSym));
        tars.remove(ar);
        boolean[][] pos = StageAnalyzer.getPos(dir + "/");
        String[] cols = FileUtil.getColumnsArray(dir + "/stages.txt");
        Set<String> s1 = new HashSet<String>();
        Set<String> s2 = new HashSet<String>();
        System.out.println("tar to test = " + tars.size());
        System.out.println("Type\tAgree\tDisagree\tAR\tGR");
        for (int j = 0; j < cols.length; j++) {
            System.out.print(cols[j]);
            double ar_mean = CellTypeMatcher.getMeanValue(ar, pos[j]);
            double gr_mean = CellTypeMatcher.getMeanValue(gr, pos[j]);
            int cp = 0, cn = 0;
            int agree = 0, confCnt = 0;
            int pos_agree = 0, neg_agree = 0, pos_conf = 0, neg_conf = 0;
            for (Gene tar : tars) {
                double corr = Pearson.calcCorrelation(gr.value, tar.value, pos[j]);
                int sign = score.get(tar.getSymbol()).startsWith("-") ? -1 : 1;
                if (Math.abs(corr) > 0.2) {
                    if (corr * sign > 0) {
                        agree++;
                        if (j == 1) s1.add(tar.getSymbol());
                    } else {
                        confCnt++;
                        if (j == 5) s2.add(tar.getSymbol());
                    }
                }
            }
            System.out.println("\t" + agree + "\t" + confCnt + "\t" + ar_mean + "\t" + gr_mean);
        }
        for (String s : s1) System.out.println(s);
        System.out.println("---");
        for (String s : s2) System.out.println(s);
        SetUtils.printVenn(s1, s2);
    }

    public static void showChangesInAbsenceAndPresenceOfAndrogen() throws Throwable {
        TabDelimitedFileParser p = new TabDelimitedFileParser("resource/factors/AR-select.txt");
        Set<String> syms = p.getColumnSet(0);
        p = new TabDelimitedFileParser("resource/factors/AR_andr.txt");
        Map<String, String> score = p.getOneToOneMap("Target", "Score");
        Set<String> ids = new HashSet<String>();
        for (String g : syms) ids.add(Triplet.getSymbolToGeneMap().get(g));
        String dir = "resource/expdata/Ling";
        Map<String, Gene> map = CrossPlatformMapper.fetchGenes(ids, dir + "/data.txt");
        Set<Gene> tars = new HashSet<Gene>(map.values());
        boolean[][] pos = StageAnalyzer.getPos(dir + "/");
        int j = 0;
        for (Gene tar : tars) {
            double pv1 = CellTypeMatcher.getChangePvalBetweenTissues(tar, pos[0], pos[2]);
            double ch1 = CellTypeMatcher.getMeanChange(tar, pos[0], pos[2]);
            double ch2 = CellTypeMatcher.getMeanChange(tar, pos[1], pos[3]);
            double x1 = CellTypeMatcher.getMeanValue(tar, pos[0]);
            double x2 = CellTypeMatcher.getMeanValue(tar, pos[2]);
            double rat = x2 / x1;
            {
                j++;
                System.out.print("\n" + tar.getSymbol());
                for (int i = 0; i < 4; i++) {
                    System.out.print("\t" + fmt.format(CellTypeMatcher.getMeanValue(tar, pos[i])));
                }
            }
        }
        System.out.println("\n\nj = " + j);
    }
}
