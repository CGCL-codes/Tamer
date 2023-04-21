package test.jin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import jflux.data.ChronogramUtil;
import jflux.data.FluxDataSet;
import jflux.data.ProbabilityUtils;
import jflux.data.ProfileUtils;
import jflux.io.CopasIO;

public class Main {

    public static FluxDataSet prepareOne(File f) throws IOException {
        FluxDataSet prof = CopasIO.readAll(f);
        prof = ProfileUtils.filterOk(prof);
        ProfileUtils.resampleLengths(prof, 2000);
        ProfileUtils.align(prof, 0);
        ProfileUtils.alignBrightestLeft(prof, 0);
        return prof;
    }

    public static FluxDataSet getPooled(Collection<File> f) throws IOException {
        FluxDataSet d = null;
        boolean hasAny = false;
        for (File ff : f) {
            if (ff.getName().endsWith(".csv")) {
                hasAny = true;
                FluxDataSet prof = CopasIO.readAll(ff);
                if (d == null) d = prof; else ProfileUtils.poolInto(d, prof);
            }
        }
        if (hasAny) {
            d = ProfileUtils.filterOk(d);
            ProfileUtils.resampleLengths(d, 2000);
            ProfileUtils.align(d, 1);
            ProfileUtils.alignBrightestLeft(d, 1);
            return d;
        } else throw new IOException("No files to load");
    }

    public static Collection<File> getCSVrecursive(File f) throws IOException {
        if (f.getName().endsWith(".csv")) {
            return Collections.singletonList(f);
        } else if (f.isDirectory()) {
            LinkedList<File> fileList = new LinkedList<File>();
            for (File ff : f.listFiles()) fileList.addAll(getCSVrecursive(ff));
            return fileList;
        } else return Collections.emptyList();
    }

    public static void compare(File fa, File fb) throws IOException {
        FluxDataSet profA = prepareOne(fa);
        FluxDataSet profB = prepareOne(fb);
        double prob = ProbabilityUtils.calcProbAreaAGreaterThanB(1, profA, profB, 400, 800, 0, 2000);
        System.out.println(prob);
    }

    public static void main(String[] args) {
        try {
            File root = new File("/Volumes/TBU_main06/copasData/jin me process");
            String[] genes = new String[] { "pbs4", "rpn11", "rpt5" };
            for (String gene : genes) {
                System.out.println("----------------- " + gene);
                FluxDataSet f1 = getPooled(getCSVrecursive(new File(root, gene)));
                System.out.println("-----");
                FluxDataSet f2 = getPooled(getCSVrecursive(new File(root, gene + "-mut")));
                System.out.println("-----");
                int[][] chrono1 = ChronogramUtil.makeChrono(f1.events, 1);
                ChronogramUtil.saveChronoImage(new File("/Volumes/TBU_main06/copasData/jin me process/pics", gene + ".png"), chrono1);
                int[][] chrono2 = ChronogramUtil.makeChrono(f2.events, 1);
                ChronogramUtil.saveChronoImage(new File("/Volumes/TBU_main06/copasData/jin me process/pics", gene + "-mut.png"), chrono2);
                double prob = ProbabilityUtils.calcProbAreaAGreaterThanB(1, f1, f2, -10000, 10000, 0, 2000);
                double prob2 = ProbabilityUtils.calcProbAreaAGreaterThanB(1, f1, f2, -10000, 10000, 0, 2000 / 3);
                System.out.println(prob);
                System.out.println(prob2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
