package edu.utah.seq.data;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import edu.utah.seq.parsers.BarParser;
import edu.utah.seq.useq.data.Region;
import util.gen.*;

/**Filters PointData for those that intersect a list of regions, e.g. RepeatMasker regions.*/
public class FilterPointData {

    private File[] pointDataDirectories;

    private File[] filteredPointDataDirectories;

    private HashMap<String, PointData[]>[] pointData;

    private File regionsFile;

    private HashMap<String, Region[]> regions;

    private double maximumFractionOverlap = 0.5;

    private int shiftPositions = 0;

    private boolean saveIntersectingData = false;

    private double[] startingNumObs;

    private double[] endingNumObs;

    private boolean justNumbersNoSave = false;

    private boolean verbose = true;

    /**Just calculates number of observations after filtering.  Doesn't write out filtered data.*/
    public FilterPointData(File regionsFile, File[] pointDataDirectories) {
        this.pointDataDirectories = pointDataDirectories;
        this.regionsFile = regionsFile;
        verbose = false;
        regions = Region.parseStartStops(regionsFile, 0, 0, 0);
        if (regions == null) Misc.printExit("\nProblem parsing your regions file! Check format looking for tab delimited (chr, start, stop, ...) from " + regionsFile);
        pointData = PointData.fetchStrandedPointData(pointDataDirectories);
        if (pointData == null) Misc.printExit("\nProblem loading your PointData. Do all of the directories contain PointData?\n");
        startingNumObs = new double[pointDataDirectories.length];
        endingNumObs = new double[pointDataDirectories.length];
        for (int i = 0; i < pointDataDirectories.length; i++) {
            startingNumObs[i] = PointData.totalObservationsMultiPointData(pointData[i]);
        }
        HashSet<String> chroms = new HashSet<String>();
        chroms.addAll(regions.keySet());
        for (int i = 0; i < pointData.length; i++) {
            chroms.addAll(pointData[i].keySet());
        }
        Iterator<String> it = chroms.iterator();
        while (it.hasNext()) {
            String chrom = it.next();
            if (regions.containsKey(chrom)) {
                filterAndSave(chrom);
            } else if (saveIntersectingData == false) {
                copy(chrom);
            }
        }
    }

    public FilterPointData(String[] args) {
        processArgs(args);
        System.out.println("Parsing regions...");
        regions = Region.parseStartStops(regionsFile, 0, 0, 0);
        if (regions == null) Misc.printExit("\nProblem parsing your regions file! Check format looking for tab delimited (chr, start, stop, ...) from " + regionsFile);
        System.out.println("Loading PointData...");
        pointData = PointData.fetchStrandedPointData(pointDataDirectories);
        if (pointData == null) Misc.printExit("\nProblem loading your PointData. Do all of the directories contain PointData?\n");
        if (justNumbersNoSave == false) filteredPointDataDirectories = new File[pointDataDirectories.length];
        startingNumObs = new double[pointDataDirectories.length];
        endingNumObs = new double[pointDataDirectories.length];
        String regionsFileName = Misc.removeExtension(regionsFile.getName());
        for (int i = 0; i < pointDataDirectories.length; i++) {
            if (justNumbersNoSave == false) {
                filteredPointDataDirectories[i] = new File(pointDataDirectories[i].getParentFile(), pointDataDirectories[i].getName() + "_" + regionsFileName + "_Filt" + shiftPositions + "bp");
                filteredPointDataDirectories[i].mkdir();
            }
            startingNumObs[i] = PointData.totalObservationsMultiPointData(pointData[i]);
        }
        HashSet<String> chroms = new HashSet<String>();
        chroms.addAll(regions.keySet());
        for (int i = 0; i < pointData.length; i++) {
            chroms.addAll(pointData[i].keySet());
        }
        Iterator<String> it = chroms.iterator();
        System.out.println("Filtering...");
        while (it.hasNext()) {
            String chrom = it.next();
            if (regions.containsKey(chrom)) {
                filterAndSave(chrom);
            } else if (saveIntersectingData == false) {
                copy(chrom);
            }
        }
        System.out.println("\nStats:");
        for (int i = 0; i < pointDataDirectories.length; i++) {
            double diff = startingNumObs[i] - endingNumObs[i];
            System.out.println((int) startingNumObs[i] + "\tStarting");
            System.out.println((int) endingNumObs[i] + "\tEnding");
            System.out.println((int) diff + "\tDifference");
        }
    }

    public void copy(String chrom) {
        if (verbose) System.out.println("\t" + chrom);
        for (int i = 0; i < pointData.length; i++) {
            if (pointData[i].containsKey(chrom)) {
                PointData[] pd = pointData[i].get(chrom);
                for (int x = 0; x < pd.length; x++) {
                    endingNumObs[i] += pd[x].getInfo().getNumberObservations();
                    if (filteredPointDataDirectories == null) continue;
                    File original = pd[x].getBarParser().getBarFile();
                    if (shiftPositions == 0) {
                        System.out.println("\t\tNothing to filter copying " + pointDataDirectories[i].getName() + File.separator + original.getName());
                        IO.copy(original, new File(filteredPointDataDirectories[i], original.getName()));
                    } else {
                        System.out.println("\t\tNothing to filter shifting positions and saving " + pointDataDirectories[i].getName() + File.separator + original.getName());
                        HashMap<String, String> notes = pd[x].getInfo().getNotes();
                        if (notes == null) {
                            notes = new HashMap<String, String>();
                            pd[x].getInfo().setNotes(notes);
                            if (saveIntersectingData) notes.put("FilteredFor", regionsFile.getName()); else notes.put("FilteredAgainst", regionsFile.getName());
                        }
                        if (shiftPositions != 0) {
                            String strand = pd[x].getInfo().getStrand();
                            int[] pos = pd[x].getPositions();
                            if (strand.equals("+")) for (int z = 0; z < pos.length; z++) pos[z] += shiftPositions; else if (strand.equals("-")) for (int z = 0; z < pos.length; z++) {
                                pos[z] -= shiftPositions;
                                if (pos[z] < 0) pos[z] = 0;
                            } else System.err.println("\nWARNING: attempting to shift the bp positions but no strand information found, skipping!\n");
                            notes.put(BarParser.BP_3_PRIME_SHIFT, shiftPositions + "");
                        }
                        if (pd[x].getPositions().length != 0) pd[x].writePointData(filteredPointDataDirectories[i]);
                        pd[x].nullPositionScoreArrays();
                    }
                }
            }
        }
    }

    /**Removes PointData reads that overlap the regions.*/
    public void filterAndSave(String chrom) {
        Region[] startStops = regions.get(chrom);
        int lastBase = Region.findLastBase(startStops);
        boolean[] maskedBases = new boolean[lastBase + 1000];
        for (int i = 0; i < startStops.length; i++) {
            int stop = startStops[i].getStop();
            for (int j = startStops[i].getStart(); j < stop; j++) {
                maskedBases[j] = true;
            }
        }
        String note = "removed";
        if (saveIntersectingData) note = "parsed";
        for (int i = 0; i < pointData.length; i++) {
            if (pointData[i].containsKey(chrom)) {
                PointData[] pd = pointData[i].get(chrom);
                for (int x = 0; x < pd.length; x++) {
                    int numRemoved;
                    if (saveIntersectingData) numRemoved = pd[x].fetch(maskedBases, maximumFractionOverlap); else numRemoved = pd[x].filter(maskedBases, maximumFractionOverlap);
                    HashMap<String, String> notes = pd[x].getInfo().getNotes();
                    if (notes == null) {
                        notes = new HashMap<String, String>();
                        pd[x].getInfo().setNotes(notes);
                        if (saveIntersectingData) notes.put("FilteredFor", regionsFile.getName()); else notes.put("FilteredAgainst", regionsFile.getName());
                    }
                    if (shiftPositions != 0) {
                        String strand = pd[x].getInfo().getStrand();
                        int[] pos = pd[x].getPositions();
                        if (strand.equals("+")) for (int z = 0; z < pos.length; z++) pos[z] += shiftPositions; else if (strand.equals("-")) for (int z = 0; z < pos.length; z++) {
                            pos[z] -= shiftPositions;
                            if (pos[z] < 0) pos[z] = 0;
                        } else System.err.println("\nWARNING: attempting to shift the bp positions but no strand information found, skipping!\n");
                        notes.put(BarParser.BP_3_PRIME_SHIFT, shiftPositions + "");
                    }
                    int numPos = pd[x].getPositions().length;
                    if (numPos != 0) {
                        pd[x].getInfo().setNumberObservations(numPos);
                        endingNumObs[i] += pd[x].getInfo().getNumberObservations();
                        if (filteredPointDataDirectories == null) continue;
                        pd[x].writePointData(filteredPointDataDirectories[i]);
                        if (verbose) System.out.println("\t" + chrom);
                    }
                    pd[x].nullPositionScoreArrays();
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printDocs();
            System.exit(0);
        }
        new FilterPointData(args);
    }

    /**This method will process each argument and assign new varibles*/
    public void processArgs(String[] args) {
        Pattern pat = Pattern.compile("-[a-z]");
        System.out.println("\nArguments: " + Misc.stringArrayToString(args, " ") + "\n");
        for (int i = 0; i < args.length; i++) {
            String lcArg = args[i].toLowerCase();
            Matcher mat = pat.matcher(lcArg);
            if (mat.matches()) {
                char test = args[i].charAt(1);
                try {
                    switch(test) {
                        case 'p':
                            pointDataDirectories = IO.extractFiles(args[++i]);
                            break;
                        case 'r':
                            regionsFile = new File(args[++i]);
                            break;
                        case 'i':
                            saveIntersectingData = true;
                            break;
                        case 'n':
                            justNumbersNoSave = true;
                            break;
                        case 's':
                            shiftPositions = Integer.parseInt(args[++i]);
                            break;
                        case 'a':
                            maximumFractionOverlap = Double.parseDouble(args[++i]);
                            break;
                        case 'h':
                            printDocs();
                            System.exit(0);
                        default:
                            Misc.printExit("\nProblem, unknown option! " + mat.group());
                    }
                } catch (Exception e) {
                    Misc.printExit("\nSorry, something doesn't look right with this parameter: -" + test + "\n");
                }
            }
        }
        if (pointDataDirectories == null || pointDataDirectories[0].isDirectory() == false) Misc.printExit("\nError: cannot find your treatment Point Data directories(s)!\n");
        if (pointDataDirectories.length == 1) {
            File[] otherDirs = IO.extractOnlyDirectories(pointDataDirectories[0]);
            if (otherDirs != null && otherDirs.length > 0) pointDataDirectories = otherDirs;
        }
        if (regionsFile == null || regionsFile.canRead() == false) Misc.printExit("\nEnter a regions file to use in filtering intersecting PointData.\n");
    }

    public static void printDocs() {
        System.out.println("\n" + "**************************************************************************************\n" + "**                          Filter Point Data: March 2011                           **\n" + "**************************************************************************************\n" + "FPD drops or saves observations from PointData that intersect a list of regions\n" + "      (e.g. repeats, interrogated regions).\n\n" + "Options:\n" + "-p Point Data directories, full path, comma delimited. These should contain\n" + "      chromosome specific xxx.bar.zip files. \n" + "-r Full path file text for a tab delimited text file containing regions to use in\n" + "      filtering the intersecting data (chr start stop ..., interbase coordinates).\n" + "-i Select data that intersects the list of regions, defaults to selecting data that\n" + "      doesn't intersect.\n" + "-a Acceptible intersection, fraction, defaults to 0.5\n" + "-n Just calculate the number of observations after filtering, don't save any data.\n" + "\n" + "Example: java -Xmx1500M -jar pathTo/USeq/Apps/FilterPointData -p /data/PointData \n" + "      -r /repeats/hg18RepeatMasker.bed -a 0.75\n\n" + "**************************************************************************************\n");
    }

    public double[] getStartingNumObs() {
        return startingNumObs;
    }

    public double[] getEndingNumObs() {
        return endingNumObs;
    }
}
