package edu.utah.seq.analysis;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import edu.utah.seq.data.*;
import edu.utah.seq.parsers.*;
import edu.utah.seq.useq.data.Region;
import util.gen.*;

/**
 *	Collapses an array of window into and array of enriched regions based on scores and a max gap.
 */
public class EnrichedRegionMaker {

    private int maxGap = -999;

    private int scoreIndexForBW = 0;

    private File[] swiFiles;

    private File regionsFile;

    private int workingSetNumberERs;

    private int[] setNumberERs;

    private File[] treatmentPointDirs;

    private File[] controlPointDirs;

    private boolean invertScores = false;

    private int[] multiScoreIndexes;

    private float[] multiScoreThresholds;

    private int bpBuffer = 0;

    private File fullPathToR = new File("/usr/bin/R");

    private boolean filterWindowsNotERs = true;

    private boolean verbose = true;

    private HashMap<String, Region[]> filterRegions;

    private int subWindowSize = 25;

    SmoothingWindowInfo[] windowInfo;

    private EnrichedRegion[] enrichedRegions;

    private ArrayList<EnrichedRegion> enrichedRegionsAL;

    private EnrichedRegion enrichedRegion;

    private Info info;

    private SmoothingWindow[] windows;

    private SmoothingWindow testWindow;

    private int numberOfWindows;

    private int totalNumberWindows;

    private int windowIndex;

    private int numWindowsInInt;

    private String[] scoreLabels;

    private boolean saveErs = false;

    private String saveName;

    private int readLength;

    private File saveDirectory;

    private File tempDirectory;

    public EnrichedRegionMaker(int maxGap) {
        this.maxGap = maxGap;
        enrichedRegionsAL = new ArrayList(1000);
    }

    public EnrichedRegionMaker(int maxGap, SmoothingWindowInfo[] windowInfo) {
        this.maxGap = maxGap;
        this.windowInfo = windowInfo;
        enrichedRegionsAL = new ArrayList(1000);
    }

    /**For integration with BisSeq.*/
    public EnrichedRegionMaker(int maxGap, int[] multiScoreIndexes, float[] multiScoreThresholds, int scoreIndexForBW) {
        verbose = false;
        this.maxGap = maxGap;
        this.multiScoreIndexes = multiScoreIndexes;
        this.multiScoreThresholds = multiScoreThresholds;
        this.scoreIndexForBW = scoreIndexForBW;
        enrichedRegionsAL = new ArrayList(1000);
    }

    /**For integration with RNASeq and ChIPSeq apps.
	 * Note, if calling multiple times with same windowInfo, be aware that the object will be modified by filtering against regions.*/
    public EnrichedRegionMaker(SmoothingWindowInfo[] windowInfo, File swiFile, int maxGap, File[] treatmentPointDirs, File[] controlPointDirs, boolean invertScores, File regionsFile, int bpBuffer, File fullPathToR, int[] multiScoreIndexes, float[] multiScoreThresholds, boolean verbose) {
        this.windowInfo = windowInfo;
        this.maxGap = maxGap;
        this.treatmentPointDirs = treatmentPointDirs;
        this.controlPointDirs = controlPointDirs;
        this.invertScores = invertScores;
        this.regionsFile = regionsFile;
        this.bpBuffer = bpBuffer;
        this.fullPathToR = fullPathToR;
        this.multiScoreIndexes = multiScoreIndexes;
        this.multiScoreThresholds = multiScoreThresholds;
        this.verbose = verbose;
        tempDirectory = new File(swiFile.getParentFile(), "TempDirDelMe" + Passwords.createRandowWord(5));
        tempDirectory.mkdir();
        runERs(swiFile);
        if (invertScores) invertScores();
        IO.deleteDirectory(tempDirectory);
    }

    public EnrichedRegionMaker(String[] args) {
        processArgs(args);
        for (int i = 0; i < swiFiles.length; i++) {
            System.out.println("\nProcessing " + swiFiles[i] + "...");
            windowInfo = (SmoothingWindowInfo[]) IO.fetchObject(swiFiles[i]);
            runERs(swiFiles[i]);
        }
        IO.deleteDirectory(tempDirectory);
        System.out.println("\nDone!\n");
    }

    public void runERs(File swiFile) {
        if (regionsFile != null) {
            filterRegions = Region.parseStartStops(regionsFile, bpBuffer, -1 * bpBuffer, 0);
            System.out.println("\tRemoving intersecting windows, +/- " + bpBuffer + " bp buffer");
            if (filterWindowsNotERs) filterWindows();
        }
        String windowSize = windowInfo[0].getInfo().getNotes().get(BarParser.WINDOW_SIZE);
        if (windowSize == null) Misc.printExit("\nCannot parse window size from " + windowInfo[0].getInfo().getNotes());
        if (maxGap == -999) maxGap = Integer.parseInt(windowSize);
        if (verbose) System.out.println("\t" + maxGap + "\tMax gap ");
        if (invertScores) {
            if (verbose) System.out.println("Multiplying scores by -1");
            invertScores();
        }
        String scoreDescriptions = windowInfo[0].getInfo().getNotes().get(BarParser.DESCRIPTION_TAG);
        if (scoreDescriptions != null) scoreLabels = scoreDescriptions.split(","); else scoreDescriptions = null;
        readLength = windowInfo[0].getInfo().getReadLength();
        if (setNumberERs != null) {
            for (int x = 0; x < setNumberERs.length; x++) {
                workingSetNumberERs = setNumberERs[x];
                makeSetNumberEnrichedRegions();
                if (regionsFile != null && filterWindowsNotERs == false) filterEnrichedRegions();
                if (enrichedRegions != null && enrichedRegions.length != 0) {
                    pickPeaks();
                    Arrays.sort(enrichedRegions, new ComparatorEnrichedRegionScore(scoreIndexForBW));
                    if (invertScores) invertRegionScores();
                    writeEnrichedRegions(swiFile);
                }
            }
        } else {
            try {
                int numScores = windowInfo[0].getSm()[0].getScores().length;
                if (workingSetNumberERs == 0 && Num.findHighestInt(multiScoreIndexes) >= numScores) Misc.printExit("\nError: max score index is greater than the number of scores!\n");
            } catch (Exception e) {
            }
            System.out.println("\tThresholds:");
            for (int x = 0; x < multiScoreThresholds.length; x++) {
                System.out.println("\t" + multiScoreThresholds[x] + "\t" + scoreLabels[multiScoreIndexes[x]] + " score threshold");
            }
            makeEnrichedRegions();
            if (regionsFile != null && filterWindowsNotERs == false && enrichedRegions.length != 0) filterEnrichedRegions();
            int numERsMade = 0;
            if (enrichedRegions != null) numERsMade = enrichedRegions.length;
            if (invertScores) System.out.println("\t" + numERsMade + "\tReduced Regions found"); else System.out.println("\t" + numERsMade + "\tEnriched Regions found");
            if (enrichedRegions != null && enrichedRegions.length != 0) {
                pickPeaks();
                Arrays.sort(enrichedRegions, new ComparatorEnrichedRegionScore(scoreIndexForBW));
                if (invertScores) invertRegionScores();
                if (writeEnrichedRegions(swiFile) == false) return;
            }
        }
        if (verbose) System.out.println("\n" + totalNumberWindows + "\tTotal number of windows");
    }

    /**Attempts to pick the best peak within each enriched region. Switches treatment and control if inverting scores.*/
    public void pickPeaks() {
        if (enrichedRegions != null && controlPointDirs != null) {
            int halfPeakShift = 0;
            String halfPeakShiftString = windowInfo[0].getInfo().getNotes().get(BarParser.BP_3_PRIME_SHIFT);
            if (halfPeakShiftString == null) {
                System.out.println("\nCannot parse half peak shift. Skipping sub peak picking.");
                return;
            }
            halfPeakShift = Integer.parseInt(halfPeakShiftString);
            if (invertScores) new SubPeakPicker(enrichedRegions, controlPointDirs, treatmentPointDirs, subWindowSize, halfPeakShift, tempDirectory, fullPathToR); else new SubPeakPicker(enrichedRegions, treatmentPointDirs, controlPointDirs, subWindowSize, halfPeakShift, tempDirectory, fullPathToR);
        }
    }

    /**Writes out ER reports.*/
    public boolean writeEnrichedRegions(File swiFile) {
        if (enrichedRegions != null && enrichedRegions.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < multiScoreThresholds.length; x++) {
                String label = scoreLabels[multiScoreIndexes[x]];
                if (label.startsWith("Log2")) label = "Log2Ratio";
                sb.append(label);
                sb.append(multiScoreThresholds[x]);
                sb.append("_");
            }
            saveName = Misc.removeExtension(swiFile.getName()) + "_" + sb + enrichedRegions.length;
            String name = "EnrichedRegions_";
            if (invertScores) name = "ReducedRegions_";
            saveDirectory = new File(swiFile.getParentFile(), name + Misc.capitalizeFirstLetter(saveName));
            saveDirectory.mkdir();
            if (saveErs) {
                File ers = new File(saveDirectory, saveName + ".er");
                System.out.println("\tSaving serialized object EnrichedRegions[]...");
                IO.saveObject(ers, enrichedRegions);
            }
            if (verbose) System.out.println("\t\tSaving reports...");
            File report = new File(saveDirectory, saveName + ".xls");
            printEnrichedRegionReport(enrichedRegions, report, windowInfo[0].getInfo(), scoreLabels, invertScores);
            File egr = new File(saveDirectory, saveName + ".egr");
            printEGRFile(enrichedRegions, egr, windowInfo[0].getInfo().getVersionedGenome(), scoreLabels);
            File gff = new File(saveDirectory, saveName + ".gff");
            printGFFFile(enrichedRegions, gff, windowInfo[0].getInfo().getVersionedGenome(), scoreIndexForBW, scoreLabels);
            if (controlPointDirs != null) {
                File subDir = new File(saveDirectory, subWindowSize + "bpSubWinData");
                if (verbose) System.out.println("\t\tSaving sub window graph data...");
                printSubWindowGraphFiles(enrichedRegions, subDir, windowInfo[0].getInfo().getVersionedGenome());
            }
            return true;
        }
        return false;
    }

    public void printSubWindowGraphFiles(EnrichedRegion[] ers, File saveDir, String genomeVersion) {
        saveDir.mkdir();
        File egr = new File(saveDir, "bestSubWindows.egr");
        printEGRFileForBestSubWindow(ers, egr, genomeVersion);
        File gff = new File(saveDir, "bestSubWindows.gff");
        printGFFFileForBestSubWindow(ers, gff, genomeVersion);
        File barDir = new File(saveDir, "AllSubWindowStairStepBarFiles");
        barDir.mkdir();
        writeSubWindowBarFiles(barDir, genomeVersion);
    }

    /**Writes the sub windows for each enriched region, note also sorts enriched regions by position*/
    public void writeSubWindowBarFiles(File saveDir, String genomeVersion) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(BarParser.GRAPH_TYPE_TAG, BarParser.GRAPH_TYPE_STAIRSTEP);
        map.put(BarParser.GRAPH_TYPE_COLOR_TAG, "#FFFFFF");
        HeatMapMakerPosNeg hm = new HeatMapMakerPosNeg(0, 0, 0);
        String fileNames = Misc.stringArrayToString(IO.fetchFileNames(treatmentPointDirs), ",");
        if (controlPointDirs != null) fileNames = fileNames + " vs " + Misc.stringArrayToString(IO.fetchFileNames(controlPointDirs), ",");
        map.put(BarParser.SOURCE_TAG, fileNames);
        map.put(BarParser.WINDOW_SIZE, subWindowSize + "");
        map.put(BarParser.DESCRIPTION_TAG, "Enriched region sub window data");
        HashMap<String, ArrayList<EnrichedRegion>> ersHM = new HashMap<String, ArrayList<EnrichedRegion>>();
        ArrayList<EnrichedRegion> ersAL = null;
        String chr = "";
        for (int i = 0; i < enrichedRegions.length; i++) {
            String testChr = enrichedRegions[i].getChromosome();
            if (testChr.equals(chr) == false) {
                chr = testChr;
                if (ersHM.containsKey(chr)) ersAL = ersHM.get(chr); else {
                    ersAL = new ArrayList<EnrichedRegion>();
                    ersHM.put(chr, ersAL);
                }
            }
            ersAL.add(enrichedRegions[i]);
        }
        Iterator<String> it = ersHM.keySet().iterator();
        ComparatorEnrichedRegionPosition comp = new ComparatorEnrichedRegionPosition();
        ComparatorSmoothingWindowPosition smComp = new ComparatorSmoothingWindowPosition();
        while (it.hasNext()) {
            String chromosome = it.next();
            ArrayList<EnrichedRegion> ersALX = ersHM.get(chromosome);
            EnrichedRegion[] ers = new EnrichedRegion[ersALX.size()];
            ersALX.toArray(ers);
            Arrays.sort(ers, comp);
            ArrayList<SmoothingWindow> smAL = new ArrayList<SmoothingWindow>();
            for (int t = 0; t < ers.length; t++) {
                SmoothingWindow[] smIndi = ers[t].getSubWindows();
                for (int s = 0; s < smIndi.length; s++) {
                    if (invertScores) smIndi[s].getScores()[0] *= -1;
                    smAL.add(smIndi[s]);
                }
            }
            SmoothingWindow[] sm = new SmoothingWindow[smAL.size()];
            smAL.toArray(sm);
            Arrays.sort(sm, smComp);
            Info info = new Info("subWindowData", genomeVersion, chromosome, ".", readLength, map);
            PointData pd = hm.makeHeatMapPositionValues(sm);
            pd.setInfo(info);
            pd.writePointData(saveDir);
        }
    }

    /**Prints a simple gff file, the index is used to pick a score to place in the score column.*/
    public void printGFFFileForBestSubWindow(EnrichedRegion[] ers, File egr, String genomeVersion) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(egr));
            String name = Misc.removeExtension(egr.getName());
            out.println("#track text=" + name + " description=\"" + name + "\" useScore=1");
            out.println("# genome_version = " + genomeVersion);
            float[] trans = new float[ers.length];
            for (int i = 0; i < ers.length; i++) trans[i] = ers[i].getBestSubWindow().getScores()[0];
            Num.scale1To1000(trans);
            String nameLine = "\t" + name + "\tEnrichedRegionSubWindow\t";
            if (invertScores) nameLine = "\t" + name + "\tReducedRegionSubWindow\t";
            for (int i = 0; i < ers.length; i++) {
                out.print(ers[i].getChromosome());
                out.print(nameLine);
                out.print(ers[i].getBestSubWindow().getStart());
                out.print("\t");
                out.print(ers[i].getBestSubWindow().getStop());
                out.print("\t");
                SmoothingWindow bw = ers[i].getBestSubWindow();
                float[] scores = bw.getScores();
                out.print(trans[i]);
                out.print("\t.\t.\t");
                out.println("ER_" + i + "_Scores_" + Num.floatArrayToString(scores, ","));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Writes out an egr file for best sub windows, used by IGB for graph display.*/
    public static void printEGRFileForBestSubWindow(EnrichedRegion[] ers, File egr, String genomeVersion) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(egr));
            out.println("# genome_version = " + genomeVersion);
            out.println("# score 0 = NormDiff or Sum");
            float[] tScores = new float[ers.length];
            for (int i = 0; i < ers.length; i++) {
                tScores[i] = ers[i].getBestSubWindow().getScores()[0];
            }
            Num.scale1To1000(tScores);
            for (int i = 0; i < ers.length; i++) {
                out.print(ers[i].getChromosome());
                out.print("\t");
                out.print(ers[i].getBestSubWindow().getStart());
                out.print("\t");
                out.print(ers[i].getBestSubWindow().getStop());
                out.print("\t.\t");
                out.print(tScores[i]);
                out.println();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Writes out an egr file, used by IGB for graph display.*/
    public static void printEGRFile(EnrichedRegion[] ers, File egr, String genomeVersion, String[] scoreLabels) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(egr));
            out.println("# genome_version = " + genomeVersion);
            for (int i = 0; i < scoreLabels.length; i++) out.println("# score" + i + " = " + scoreLabels[i]);
            float[][] tScores = new float[scoreLabels.length][ers.length];
            for (int i = 0; i < ers.length; i++) {
                float[] scores = ers[i].getBestWindow().getScores();
                for (int j = 0; j < scores.length; j++) {
                    tScores[j][i] = scores[j];
                }
            }
            for (int i = 0; i < tScores.length; i++) Num.scale1To1000(tScores[i]);
            for (int i = 0; i < ers.length; i++) {
                out.print(ers[i].getChromosome());
                out.print("\t");
                out.print(ers[i].getStart());
                out.print("\t");
                out.print(ers[i].getStop());
                out.print("\t.\t");
                out.print(tScores[0][i]);
                for (int j = 1; j < tScores.length; j++) {
                    out.print("\t");
                    out.print(tScores[j][i]);
                }
                out.println();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Prints a simple gff file, the index is used to pick a score to place in the score column.*/
    public void printGFFFile(EnrichedRegion[] ers, File egr, String genomeVersion, int index, String[] scoreLabels) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(egr));
            String name = Misc.removeExtension(egr.getName());
            out.println("#track text=" + name + " description=\"" + name + "\" useScore=1");
            out.println("# genome_version = " + genomeVersion);
            for (int i = 0; i < scoreLabels.length; i++) out.println("# score" + i + " = " + scoreLabels[i]);
            float[] trans = new float[ers.length];
            for (int i = 0; i < ers.length; i++) trans[i] = ers[i].getBestWindow().getScores()[index];
            Num.scale1To1000(trans);
            String nameLine = "\t" + name + "\tEnrichedRegion\t";
            if (invertScores) nameLine = "\t" + name + "\tReducedRegion\t";
            for (int i = 0; i < ers.length; i++) {
                out.print(ers[i].getChromosome());
                out.print(nameLine);
                out.print(ers[i].getStart());
                out.print("\t");
                out.print(ers[i].getStop());
                out.print("\t");
                SmoothingWindow bw = ers[i].getBestWindow();
                float[] scores = bw.getScores();
                out.print(trans[i]);
                out.print("\t.\t.\t");
                out.println("ER_" + i + "_Scores_" + Num.floatArrayToString(scores, ","));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Writes out an excel compatible tab delimited spreadsheet with a hyperlink to IGB.*/
    public static void printEnrichedRegionReport(EnrichedRegion[] ers, File spreadSheetFile, Info info, String[] scoreDescriptions, boolean invertScores) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(spreadSheetFile));
            out.print("#Hyperlinks\tChr\tStart\tStop\t");
            if (ers[0].getBestSubWindow() == null) out.print("#Windows\tBW_Start\tBW_Stop\t"); else out.print("#Windows\t#T\t#Unique_T\t#C\t#Unique_C\tER_BinPVal\tER_Log2((#T+1)/(#C+1))\tBSW_Start\tBSW_Stop\tBSW_BinPVal\tBW_Start\tBW_Stop\t");
            if (scoreDescriptions != null) for (int i = 0; i < scoreDescriptions.length; i++) out.print("BW_" + scoreDescriptions[i] + "\t");
            String totalTreatment = info.getNotes().get("totalTreatmentObservations");
            String totalSum = "GenomeVersion=" + info.getVersionedGenome() + ", TotalTreatObs=" + totalTreatment;
            String totalControl = info.getNotes().get("totalControlObservations");
            if (totalControl != null) totalSum = totalSum + ", TotalCtrlObs=" + totalControl;
            out.println(totalSum);
            String url = "=HYPERLINK(\"http://localhost:7085/UnibrowControl?version=" + info.getVersionedGenome() + "&seqid=";
            for (int i = 0; i < ers.length; i++) {
                int winStart = ers[i].getStart() - 7500;
                if (winStart < 0) winStart = 0;
                int winEnd = ers[i].getStop() + 7500;
                out.print(url + ers[i].getChromosome() + "&start=" + winStart + "&end=" + winEnd + "\",\"" + (i + 1) + "\")\t");
                out.print(ers[i].getChromosome());
                out.print("\t");
                out.print(ers[i].getStart());
                out.print("\t");
                out.print(ers[i].getStop());
                out.print("\t");
                out.print(ers[i].getNumberOfWindows());
                out.print("\t");
                SmoothingWindow bsw = ers[i].getBestSubWindow();
                if (bsw != null) {
                    if (invertScores) {
                        int num = 0;
                        if (ers[i].getControlPoints() != null) num = ers[i].getControlPoints().length;
                        out.print(num);
                        out.print("\t");
                        out.print(ers[i].getNumberUniqueControlObservations());
                        out.print("\t");
                        num = 0;
                        if (ers[i].getTreatmentPoints() != null) num = ers[i].getTreatmentPoints().length;
                        out.print(num);
                        out.print("\t");
                        out.print(ers[i].getNumberUniqueTreatmentObservations());
                        out.print("\t");
                        out.print(ers[i].getBinomialPValue() * -1);
                        out.print("\t");
                        out.print(ers[i].getLog2Ratio() * -1);
                        out.print("\t");
                        out.print(bsw.getStart());
                        out.print("\t");
                        out.print(bsw.getStop());
                        out.print("\t");
                        out.print(bsw.getScores()[0]);
                        out.print("\t");
                    } else {
                        int num = 0;
                        if (ers[i].getTreatmentPoints() != null) num = ers[i].getTreatmentPoints().length;
                        out.print(num);
                        out.print("\t");
                        out.print(ers[i].getNumberUniqueTreatmentObservations());
                        out.print("\t");
                        num = 0;
                        if (ers[i].getControlPoints() != null) num = ers[i].getControlPoints().length;
                        out.print(num);
                        out.print("\t");
                        out.print(ers[i].getNumberUniqueControlObservations());
                        out.print("\t");
                        out.print(ers[i].getBinomialPValue());
                        out.print("\t");
                        out.print(ers[i].getLog2Ratio());
                        out.print("\t");
                        out.print(bsw.getStart());
                        out.print("\t");
                        out.print(bsw.getStop());
                        out.print("\t");
                        out.print(bsw.getScores()[0]);
                        out.print("\t");
                    }
                }
                SmoothingWindow bw = ers[i].getBestWindow();
                out.print(bw.getStart());
                out.print("\t");
                out.print(bw.getStop());
                out.print("\t");
                float[] scores = bw.getScores();
                out.print(scores[0]);
                for (int j = 1; j < scores.length; j++) {
                    out.print("\t");
                    out.print(scores[j]);
                }
                out.println();
            }
            out.close();
        } catch (Exception e) {
            System.out.println("\nError: problem printing spreadsheet report");
            e.printStackTrace();
        }
    }

    /**Will attempt to make the desired number of ERs*/
    public void makeSetNumberEnrichedRegions() {
        verbose = false;
        enrichedRegions = null;
        if (multiScoreIndexes != null) scoreIndexForBW = multiScoreIndexes[0];
        System.out.println("\tBootstrappping to find " + workingSetNumberERs + " ERs, score index " + scoreIndexForBW);
        multiScoreIndexes = new int[] { scoreIndexForBW };
        multiScoreThresholds = new float[] { 0 };
        float[] minMax = SmoothingWindowInfo.minMax(windowInfo, scoreIndexForBW);
        float start = minMax[0];
        float stop = minMax[1];
        float half = 0;
        ArrayList<Integer> numberERsAL = new ArrayList<Integer>();
        ArrayList<Float> thresholdsAL = new ArrayList<Float>();
        for (int i = 0; i < 50; i++) {
            if (enrichedRegions != null) {
                int numERs = enrichedRegions.length;
                if (numERs == workingSetNumberERs) {
                    System.out.println("\t\tFound desired number at a threshold of " + multiScoreThresholds[0]);
                    return;
                }
                if (numERs < workingSetNumberERs) stop = multiScoreThresholds[0]; else start = multiScoreThresholds[0];
            }
            half = (stop - start) / 2.0f;
            if (multiScoreThresholds[0] == (half + start)) break;
            multiScoreThresholds[0] = half + start;
            makeEnrichedRegions();
            thresholdsAL.add(new Float(multiScoreThresholds[0]));
            numberERsAL.add(new Integer(enrichedRegions.length));
        }
        float[] thresholds = Num.arrayListOfFloatToArray(thresholdsAL);
        int[] numberERs = Num.arrayListOfIntegerToInts(numberERsAL);
        int testNum = numberERs[numberERs.length - 1];
        int diff = Math.abs(testNum - workingSetNumberERs);
        for (int i = numberERs.length - 1; i >= 0; i--) {
            if (numberERs[i] != testNum) {
                if (diff > Math.abs(numberERs[i] - workingSetNumberERs)) {
                    multiScoreThresholds[0] = thresholds[i];
                    makeEnrichedRegions();
                }
                break;
            }
        }
        System.out.println("\t\tWarning, couldn't make desired number of ERs. Best set made contains " + enrichedRegions.length + " ERs at a threshold of " + multiScoreThresholds[0]);
        verbose = true;
    }

    /**Makes ERs given a window array.*/
    public void makeEnrichedRegions() {
        enrichedRegionsAL = new ArrayList(1000);
        totalNumberWindows = 0;
        for (int c = 0; c < windowInfo.length; c++) {
            info = windowInfo[c].getInfo();
            windows = windowInfo[c].getSm();
            numberOfWindows = windows.length;
            totalNumberWindows += numberOfWindows;
            int numWinMinusOne = numberOfWindows - 1;
            enrichedRegion = null;
            testWindow = null;
            windowIndex = 0;
            numWindowsInInt = 0;
            if (numberOfWindows > 0 && windows[0] != null) {
                SmoothingWindow firstWin = fetchGoodWindow();
                if (firstWin != null) {
                    enrichedRegion = new EnrichedRegion(firstWin, info.getChromosome());
                    numWindowsInInt = 1;
                    for (; windowIndex < numberOfWindows; windowIndex++) {
                        testWindow = windows[windowIndex];
                        if (passThresholds(testWindow)) {
                            int gap = testWindow.getStart() - enrichedRegion.getStop();
                            if (gap <= maxGap) {
                                enrichedRegion.setStop(testWindow.getStop());
                                numWindowsInInt++;
                                if (testWindow.getScores()[scoreIndexForBW] > enrichedRegion.getBestWindow().getScores()[scoreIndexForBW]) {
                                    enrichedRegion.setBestWindow(testWindow);
                                }
                            } else {
                                addIt();
                                numWindowsInInt = 1;
                                enrichedRegion = new EnrichedRegion(testWindow, info.getChromosome());
                            }
                        }
                        if (windowIndex == numWinMinusOne) addIt();
                    }
                }
            }
        }
        int numER = enrichedRegionsAL.size();
        if (numER > 0) {
            enrichedRegions = new EnrichedRegion[numER];
            enrichedRegionsAL.toArray(enrichedRegions);
        } else {
            enrichedRegions = null;
            if (verbose) Misc.printExit("\t\tNo Regions passed criteria?! Too strict? ");
        }
    }

    /**Makes ERs given a window array. Must be sorted but can have the same starts and full overlap.*/
    public void addEnrichedRegions(SmoothingWindow[] windows, String chromosome) {
        totalNumberWindows = 0;
        this.windows = windows;
        numberOfWindows = windows.length;
        int numWinMinusOne = numberOfWindows - 1;
        testWindow = null;
        windowIndex = 0;
        numWindowsInInt = 0;
        if (numberOfWindows > 0 && windows[0] != null) {
            SmoothingWindow firstWin = fetchGoodWindow();
            if (firstWin != null) {
                enrichedRegion = new EnrichedRegion(firstWin, chromosome);
                numWindowsInInt = 1;
                for (; windowIndex < numberOfWindows; windowIndex++) {
                    testWindow = windows[windowIndex];
                    if (passThresholds(testWindow)) {
                        if (overlap(testWindow, enrichedRegion)) {
                            if (testWindow.getStop() > enrichedRegion.getStop()) enrichedRegion.setStop(testWindow.getStop());
                            numWindowsInInt++;
                            if (testWindow.getScores()[scoreIndexForBW] > enrichedRegion.getBestWindow().getScores()[scoreIndexForBW]) {
                                enrichedRegion.setBestWindow(testWindow);
                            }
                        } else {
                            addIt();
                            numWindowsInInt = 1;
                            enrichedRegion = new EnrichedRegion(testWindow, chromosome);
                        }
                    }
                    if (windowIndex == numWinMinusOne) addIt();
                }
            }
        }
    }

    public static boolean sameSign(float a, float b) {
        if (a < 0 && b < 0) return true;
        if (a > 0 && b > 0) return true;
        if (a == 0 && b == 0) return true;
        return false;
    }

    /**Makes ERs given a window array that has already been thresholded for log2Ratio and pvalue. 
	 * Must be sorted but can have the same starts and full overlap.*/
    public void addEnrichedRegions(SmoothingWindow[] windows, String chromosome, int log2RatioIndex) {
        enrichedRegion = new EnrichedRegion(windows[0], chromosome);
        numWindowsInInt = 1;
        for (int i = 1; i < windows.length; i++) {
            testWindow = windows[i];
            boolean okSign = sameSign(testWindow.getScores()[log2RatioIndex], enrichedRegion.getBestWindow().getScores()[log2RatioIndex]);
            boolean okGap = overlap(testWindow, enrichedRegion);
            if (okSign == false || okGap == false) {
                enrichedRegion.setNumberOfWindows(numWindowsInInt);
                enrichedRegionsAL.add(enrichedRegion);
                enrichedRegion = new EnrichedRegion(windows[i], chromosome);
                numWindowsInInt = 1;
            } else {
                if (testWindow.getStop() > enrichedRegion.getStop()) enrichedRegion.setStop(testWindow.getStop());
                numWindowsInInt++;
                if (testWindow.getScores()[scoreIndexForBW] > enrichedRegion.getBestWindow().getScores()[scoreIndexForBW]) {
                    enrichedRegion.setBestWindow(testWindow);
                }
            }
        }
        enrichedRegion.setNumberOfWindows(numWindowsInInt);
        enrichedRegionsAL.add(enrichedRegion);
    }

    /**Makes ERs given a window array that has already been thresholded . 
	 * Must be sorted but can have the same starts and full overlap.*/
    public EnrichedRegion[] fetchEnrichedRegions(SmoothingWindow[] windows, String chromosome) {
        enrichedRegion = new EnrichedRegion(windows[0], chromosome);
        numWindowsInInt = 1;
        enrichedRegionsAL.clear();
        for (int i = 1; i < windows.length; i++) {
            testWindow = windows[i];
            boolean okGap = overlap(testWindow, enrichedRegion);
            if (okGap == false) {
                enrichedRegion.setNumberOfWindows(numWindowsInInt);
                enrichedRegionsAL.add(enrichedRegion);
                enrichedRegion = new EnrichedRegion(windows[i], chromosome);
                numWindowsInInt = 1;
            } else {
                if (testWindow.getStop() > enrichedRegion.getStop()) enrichedRegion.setStop(testWindow.getStop());
                numWindowsInInt++;
                if (testWindow.getScores()[scoreIndexForBW] > enrichedRegion.getBestWindow().getScores()[scoreIndexForBW]) {
                    enrichedRegion.setBestWindow(testWindow);
                }
            }
        }
        enrichedRegion.setNumberOfWindows(numWindowsInInt);
        enrichedRegionsAL.add(enrichedRegion);
        EnrichedRegion[] e = new EnrichedRegion[enrichedRegionsAL.size()];
        enrichedRegionsAL.toArray(e);
        return e;
    }

    public boolean overlap(SmoothingWindow win, EnrichedRegion er) {
        if (er.getStop() < win.getStart()) {
            int gap = win.getStart() - er.getStop();
            if (gap > maxGap) return false;
            return true;
        }
        if (win.getStop() < er.getStart()) {
            int gap = er.getStart() - win.getStop();
            if (gap > maxGap) return false;
            return true;
        }
        return true;
    }

    /**Looks to see if a given window passes multiple thresholds.*/
    public boolean passThresholds(SmoothingWindow win) {
        float[] scores = win.getScores();
        for (int i = 0; i < multiScoreIndexes.length; i++) {
            if (scores[multiScoreIndexes[i]] < multiScoreThresholds[i]) return false;
        }
        return true;
    }

    /**Returns the number of ERs that would have been made.
	 * If setStrippedWindows, will set the stripped SmoothingWindows in the SmoothingWindowInfo at each call
	 * this should be much faster with increasing stringent threshold calls.
	 * Assumes SmoothingWindows are sorted by position, smallest to largest*/
    public int countEnrichedRegions(boolean setStrippedWindows, int scoreIndex, float threshold) {
        int numberERs = 0;
        for (int c = 0; c < windowInfo.length; c++) {
            info = windowInfo[c].getInfo();
            int halfReadLength = info.getReadLength() / 2;
            windows = windowInfo[c].getSm();
            numberOfWindows = windows.length;
            ArrayList<SmoothingWindow> good = new ArrayList<SmoothingWindow>();
            for (int i = 0; i < numberOfWindows; i++) {
                if (windows[i].getScores()[scoreIndex] >= threshold) good.add(windows[i]);
            }
            windows = new SmoothingWindow[good.size()];
            good.toArray(windows);
            if (setStrippedWindows) windowInfo[c].setSm(windows);
            numberOfWindows = windows.length;
            if (numberOfWindows > 0) {
                enrichedRegion = new EnrichedRegion(windows[0]);
                numberERs++;
                for (int x = 1; x < numberOfWindows; x++) {
                    testWindow = windows[x];
                    int gap = testWindow.getStart() - enrichedRegion.getStop();
                    if (gap > maxGap) {
                        enrichedRegion = new EnrichedRegion(testWindow);
                        numberERs++;
                    } else {
                        enrichedRegion.setStop(testWindow.getStop() + halfReadLength);
                    }
                }
            }
        }
        return numberERs;
    }

    public void addIt() {
        enrichedRegion.setNumberOfWindows(numWindowsInInt);
        enrichedRegionsAL.add(enrichedRegion);
    }

    public SmoothingWindow fetchGoodWindow() {
        for (; windowIndex < numberOfWindows; windowIndex++) {
            if (passThresholds(windows[windowIndex])) {
                numWindowsInInt = 1;
                return windows[windowIndex++];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            printDocs();
            System.exit(0);
        }
        new EnrichedRegionMaker(args);
    }

    /**This method will process each argument and assign new varibles*/
    public void processArgs(String[] args) {
        Pattern pat = Pattern.compile("-[a-z]");
        String swiString = null;
        for (int i = 0; i < args.length; i++) {
            String lcArg = args[i].toLowerCase();
            Matcher mat = pat.matcher(lcArg);
            if (mat.matches()) {
                char test = args[i].charAt(1);
                try {
                    switch(test) {
                        case 'i':
                            multiScoreIndexes = Num.stringArrayToInts(args[++i], ",");
                            break;
                        case 'g':
                            maxGap = Integer.parseInt(args[i + 1]);
                            i++;
                            break;
                        case 'b':
                            bpBuffer = Integer.parseInt(args[i + 1]);
                            i++;
                            break;
                        case 'n':
                            setNumberERs = Num.parseInts(args[++i].split(","));
                            break;
                        case 'f':
                            swiString = args[++i];
                            break;
                        case 'r':
                            regionsFile = new File(args[i + 1]);
                            i++;
                            break;
                        case 's':
                            multiScoreThresholds = Num.stringArrayToFloat(args[++i], ",");
                            break;
                        case 'e':
                            filterWindowsNotERs = false;
                            break;
                        case 'm':
                            invertScores = true;
                            break;
                        case 'w':
                            subWindowSize = Integer.parseInt(args[i + 1]);
                            i++;
                            break;
                        case 't':
                            treatmentPointDirs = IO.extractFiles(args[++i]);
                            break;
                        case 'c':
                            controlPointDirs = IO.extractFiles(args[++i]);
                            break;
                        case 'p':
                            fullPathToR = new File(args[++i]);
                            break;
                        case 'h':
                            printDocs();
                            System.exit(0);
                        default:
                            Misc.printExit("\nProblem, unknown option! " + mat.group());
                    }
                } catch (Exception e) {
                    Misc.printExit("\nSorry, something doesn't look right with this parameter request: -" + test);
                }
            }
        }
        System.out.println("\nLaunching...");
        if (swiString != null) {
            File dir = new File(swiString);
            if (dir.isDirectory()) {
                swiFiles = IO.extractFiles(dir, ".swi.gz");
                if (swiFiles == null || swiFiles.length == 0) swiFiles = IO.extractFiles(dir, ".swi");
            } else swiFiles = new File[] { dir };
        }
        if (swiFiles == null || swiFiles.length == 0) {
            System.out.println("\nPlease enter a smoothed window xxx.swi results file or directory.\n");
            System.exit(0);
        }
        if (setNumberERs == null) {
            if (multiScoreIndexes == null) {
                System.out.println("Looking up indexes...");
                windowInfo = (SmoothingWindowInfo[]) IO.fetchObject(swiFiles[0]);
                String scoreDescriptions = windowInfo[0].getInfo().getNotes().get(BarParser.DESCRIPTION_TAG);
                scoreLabels = scoreDescriptions.split(",");
                System.out.println("\nPlease enter one or more of the score indexes from below:");
                for (int i = 0; i < scoreLabels.length; i++) {
                    System.out.println("\t" + i + "\t" + scoreLabels[i]);
                }
                Misc.printExit("");
            }
            if (multiScoreThresholds == null) Misc.printExit("\nPlease enter at leaste one minimum score and one score index.\n");
            if (setNumberERs == null && multiScoreThresholds.length != multiScoreIndexes.length) Misc.printExit("\nThe number of thresholds and indexes don't match!  These must be paired.\n");
        }
        if (treatmentPointDirs != null) {
            System.out.println(subWindowSize + "\tSub Window Size");
            if (controlPointDirs == null) Misc.printExit("\nPlease provide control data.\n");
            if (treatmentPointDirs.length == 1) {
                File[] otherDirs = IO.extractOnlyDirectories(treatmentPointDirs[0]);
                if (otherDirs != null && otherDirs.length > 0) treatmentPointDirs = otherDirs;
            }
            if (controlPointDirs.length == 1) {
                File[] otherDirs = IO.extractOnlyDirectories(controlPointDirs[0]);
                if (otherDirs != null && otherDirs.length > 0) controlPointDirs = otherDirs;
            }
            if (fullPathToR.canRead() == false) Misc.printErrAndExit("\nCannot find the R application? Rescanning ERs for the best subwindow requires completion of option -p.\n");
            tempDirectory = new File(swiFiles[0].getParentFile(), "TempDirDelMe" + Passwords.createRandowWord(5));
            tempDirectory.mkdir();
        }
        if (regionsFile != null && regionsFile.canRead() == false) Misc.printErrAndExit("\nCannot read/ find your regions file? " + regionsFile);
        if (filterWindowsNotERs == false && regionsFile == null) Misc.printErrAndExit("\nPlease enter a regions file to use in filtering windows.\n");
    }

    public static void printDocs() {
        System.out.println("\n" + "**************************************************************************************\n" + "**                           Enriched Region Maker: Jan 2010                        **\n" + "**************************************************************************************\n" + "ERM combines windows from ScanSeqs xxx.swi files into larger enriched or reduced\n" + "regions based on one or more scores. For each score index, you must provide a minimal\n" + "score. Adjacent windows that exceed the minimum score(s) are merged and the best\n" + "window scores applied to the region. If treatment and control PointData are provided,\n" + "the best 25bp peak within each region will be identified and each ER rescored. To\n" + "select for ERs with a 1% FDR and 2x enrichment above control, follow the example\n" + "assuming score indexes 1,2,4 correspond to QValFDR, EmpFDR, and \n" + "Log2Ratio. Note, if you are performing a static analysis comparing chIP vs chIP,\n" + "don't set thresholds on the EmpFDR, this was disabled and all of the values are zero.\n" + "To print descriptions of the score indexes, complete the command line and skip the \n" + "-i option. Lastly, FDRs and p-values are represented in USeq in a transformed state,\n" + "as -10Log10(FDR/p-val) where 13 = 5%, 20 = 1%, etc. To select for regions with an FDR of\n" + "less than 1% you would set a threshold of 20 for the QValFDR and, if running a static\n" + "analysis, the EmpFDR. \n\n" + "Options:\n" + "-f Full path file name for the serialized xxx.swi file from ScanSeqs, if a\n" + "      directory is specified, all xxx.swi files will be processed.\n" + "-s Minimal score(s) one for each score idex, comma delimited, no spaces.\n" + "-i Score index(s) one for each minimum score. \n" + "\nAdvanced Options:\n" + "-n Make a given number of ERs, one or more, comma delimited, no spaces. Uses score\n" + "      index 0.\n" + "-m Multiply scores by -1 to make reduced regions instead of enriched regions.\n" + "-r Remove windows that intersect a list of regions. Enter a full path tab delimited\n" + "      regions file text (chr start stop) Coordinates are assumed to be zero based and\n" + "      stop inclusive. Useful for excluding regions from ER generation.\n" + "-b BP buffer to subtract and add to start and stops of regions used in filtering\n" + "      intersecting windows, defaults to 0.\n" + "-e Exclude entire ERs that intersect the -r regions, defaults to removing windows.\n" + "      This is more exclusive and will not simply punch holes in ERs but throw out\n" + "      The entire ER.\n" + "-g Max gap, defaults to the size of the window used in ScanSeqs.\n" + "-t Provide treatment PointData directories, full path, comma delimited to ID the peak\n" + "       center in each ER. These should contain the same unshifted stranded chromosome\n" + "       specific xxx_-/+_.bar.zip files used in ScanSeqs.\n" + "-c Control PointData directories, ditto. \n" + "-p Full path to R, defaults to '/usr/bin/R', required for rescanning ERs.\n" + "-w Sub window size, defaults to 25bp.\n" + "\n" + "Example: java -Xmx500M -jar pathTo/USeq/Apps/EnrichedRegionMaker -f /solexa/zeste.swi\n" + "      -i 1,2,4 -s 20,20,1 -w 50\n\n" + "**************************************************************************************\n");
    }

    public void filterWindows() {
        for (int i = 0; i < windowInfo.length; i++) {
            String chr = windowInfo[i].getInfo().getChromosome();
            SmoothingWindow[] windows = windowInfo[i].getSm();
            if (filterRegions.containsKey(chr) == false) continue;
            Region[] regions = filterRegions.get(chr);
            ArrayList<SmoothingWindow> filteredWindows = new ArrayList<SmoothingWindow>();
            for (int j = 0; j < windows.length; j++) {
                int start = windows[j].getStart();
                int stop = windows[j].getStop();
                boolean intersects = false;
                for (int k = 0; k < regions.length; k++) {
                    if (regions[k].intersects(start, stop)) {
                        intersects = true;
                        break;
                    }
                }
                if (intersects == false) filteredWindows.add(windows[j]);
            }
            SmoothingWindow[] fwin = new SmoothingWindow[filteredWindows.size()];
            filteredWindows.toArray(fwin);
            windowInfo[i].setSm(fwin);
        }
    }

    /**Removes ERs that touch any filter region.*/
    public void filterEnrichedRegions() {
        if (enrichedRegions == null || enrichedRegions.length == 0) return;
        String currentChromosome = "";
        Region[] regions = null;
        ArrayList<EnrichedRegion> al = new ArrayList<EnrichedRegion>();
        for (int i = 0; i < enrichedRegions.length; i++) {
            String testChromosome = enrichedRegions[i].getChromosome();
            if (testChromosome.equals(currentChromosome) == false) {
                currentChromosome = testChromosome;
                regions = filterRegions.get(currentChromosome);
            }
            if (regions == null) al.add(enrichedRegions[i]); else {
                int start = enrichedRegions[i].getStart();
                int stop = enrichedRegions[i].getStop();
                boolean intersects = false;
                for (int k = 0; k < regions.length; k++) {
                    if (regions[k].intersects(start, stop)) {
                        intersects = true;
                        break;
                    }
                }
                if (intersects == false) al.add(enrichedRegions[i]);
            }
        }
        enrichedRegions = new EnrichedRegion[al.size()];
        al.toArray(enrichedRegions);
    }

    /**Multiplies normDiff, qvalFDR, and empFDR by -1*/
    public void invertScores() {
        for (int i = 0; i < windowInfo.length; i++) {
            String chr = windowInfo[i].getInfo().getChromosome();
            SmoothingWindow[] windows = windowInfo[i].getSm();
            for (int j = 0; j < windows.length; j++) {
                float[] scores = windows[j].getScores();
                if (scores.length == 2) {
                    scores[1] *= -1;
                } else {
                    scores[0] *= -1;
                    scores[1] *= -1;
                    scores[2] *= -1;
                    scores[4] *= -1;
                }
            }
        }
    }

    /**Multiplies normDiff, qvalFDR, and empFDR by -1*/
    public void invertRegionScores() {
        if (enrichedRegions == null) return;
        for (int i = 0; i < enrichedRegions.length; i++) {
            EnrichedRegion er = enrichedRegions[i];
            SmoothingWindow sm = er.getBestWindow();
            float[] scores = sm.getScores();
            if (scores.length == 2) {
                scores[1] *= -1;
            } else {
                scores[0] *= -1;
                scores[1] *= -1;
                scores[2] *= -1;
                scores[4] *= -1;
            }
            SmoothingWindow bsm = er.getBestSubWindow();
            if (bsm != null) bsm.getScores()[0] *= -1;
        }
    }

    public ArrayList<EnrichedRegion> getEnrichedRegionsAL() {
        return enrichedRegionsAL;
    }
}
