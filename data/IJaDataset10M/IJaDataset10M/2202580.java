package org.ais.convert.hir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import org.ais.convert.*;
import org.ais.convert.similarity.Similarity;
import org.ais.convert.similarity.SimilarityContext;

/**
 * 2-files processor, calculating HIRs.
 */
public class UPSometer extends EachPairProcessor {

    private int MIN_THREDHOLD = Constants.DEFAULT_SNIPS;

    private float CM_THRESHOLD = Constants.DEFAULT_CM;

    private boolean calculateSimilarity;

    private static ArrayList chromoList = new ArrayList();

    static {
        for (int i = 1; i < 23; i++) {
            chromoList.add(Integer.toString(i));
        }
        chromoList.add("X");
    }

    /**
	 * Comparator used for sorting HIRs in descending order. We pick the biggest HIRs first.
	 */
    private Comparator hirsComparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            Region u1 = (Region) o1;
            Region u2 = (Region) o2;
            int c1 = u1.getCount();
            int c2 = u2.getCount();
            return (c1 < c2 ? 1 : (c1 == c2 ? 0 : -1));
        }
    };

    /**
	 * Called once, before files processing, by the framework (files processing framework,
	 * been developed in this project for different genome processing tasks).
	 * Here we initialize maps, constants, etc.  
	 */
    public void init(Result result) throws Exception {
        super.init(result);
        if (result.thereAreErrors()) return;
        String arg = Parameters.getString(Constants.MIN_THRESHOLD_KEY);
        if (arg != null) {
            try {
                MIN_THREDHOLD = Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                result.addError();
                System.out.println("Threshold must be a natural number");
                return;
            }
        }
        arg = Parameters.getString(Constants.SM_THRESHOLD_KEY);
        if (arg != null) {
            try {
                CM_THRESHOLD = Float.parseFloat(arg);
            } catch (NumberFormatException e) {
                result.addError();
                System.out.println("centiMorgans threshold value must be a number");
                return;
            }
        }
        calculateSimilarity = Parameters.getAsBoolean(Constants.SHOW_SIMILARITY);
        if (!CMorgans.init()) {
            result.addError();
            return;
        }
    }

    /**
	 * Files processing framework call-back on 2 files processing against each other
	 * (in case of HIR-processor - we compare 2 files always) 
	 */
    protected void processFilesPair(File file1, File file2) throws Exception {
        if (Main.trace) System.out.println("Processing " + file1.getName() + " vs " + file2.getName());
        SimilarityContext similarityContext = new SimilarityContext();
        for (int i = 0; i < chromoList.size(); i++) {
            BufferedReader reader1 = null;
            BufferedReader reader2 = null;
            try {
                reader1 = new BufferedReader(new FileReader(file1));
                reader2 = new BufferedReader(new FileReader(file2));
                ChromoContext chromoContext = new ChromoContext();
                chromoContext.chromo = (String) chromoList.get(i);
                chromoContext.file1 = file1.getName();
                chromoContext.file2 = file2.getName();
                if (Main.trace) System.out.println("processing chromosome: " + chromoContext.chromo);
                HirsContext hirsContext = new HirsContext();
                processChromo(similarityContext, chromoContext, hirsContext, reader1, reader2);
            } finally {
                if (reader1 != null) try {
                    reader1.close();
                } catch (Exception e) {
                }
                if (reader2 != null) try {
                    reader2.close();
                } catch (Exception e) {
                }
            }
        }
        if (calculateSimilarity) {
            similarityContext.finalReport();
        }
    }

    /**
     * Processes one chromosome. Readers - are streams from 2 files, provided by the framework 	
     */
    private void processChromo(SimilarityContext similarityContext, ChromoContext chromoContext, HirsContext hirsContext, BufferedReader reader1, BufferedReader reader2) throws Exception {
        HashMap firstFile = new HashMap();
        RawRecord record;
        while ((record = RawReader.getNextRecord(reader1)) != null) {
            if (!record.chromosome.equals(chromoContext.chromo)) {
                continue;
            }
            firstFile.put(record.snip, record);
        }
        secondFile(similarityContext, chromoContext, hirsContext, firstFile, reader2);
    }

    /**
	 * Here we temporarily rely on only one allowable full mismatch inside a HIR.
	 * If 2 or more will be necessary - we should implement this method in 
	 * another way: to keep Hirs in an ordered list and check each of them - 
	 * whether the number of full mismatches reached the max - 
	 * under if(fullMismatch)
	 * and increment each of them - every match/half.
	 * For now - it seems 2 are standard and nobody asks for variable, so this 
	 * approach is slightly lighter 
	 */
    private void secondFile(SimilarityContext similarityContext, ChromoContext chromoContext, HirsContext hirsContext, HashMap firstFile, BufferedReader reader2) throws Exception {
        CMRegion hir1 = null;
        CMRegion hir2 = null;
        RawRecord record2;
        int lineCounter = 0;
        while ((record2 = RawReader.getNextRecord(reader2)) != null) {
            if (!record2.chromosome.equals(chromoContext.chromo)) {
                continue;
            }
            RawRecord record1 = (RawRecord) firstFile.get(record2.snip);
            if (record1 == null) {
                continue;
            }
            if (!record1.position.equals(record2.position)) {
                if (Main.warning) System.err.println("WARN: Positions for " + record1.snip + " are different: " + record1.position + "," + record2.position + " and the second position will be shown in the report.");
            }
            lineCounter++;
            if (!isFullMismatch(record1.base, record2.base, similarityContext)) {
                if (hir1 == null) {
                    if (hir2 == null) {
                        hir1 = new CMRegion(chromoContext.chromo, lineCounter, record2.position);
                    } else {
                        if (hir2.contansOneMismatch) {
                            hir1 = new CMRegion(chromoContext.chromo, lineCounter, record2.position);
                        }
                        hir2.advance(chromoContext.chromo, lineCounter, record2.position);
                        hir2.endPos = record2.position;
                    }
                } else {
                    hir1.advance(chromoContext.chromo, lineCounter, record2.position);
                    hir1.endPos = record2.position;
                    if (hir2 == null) {
                        if (hir1.contansOneMismatch) {
                            hir2 = new CMRegion(chromoContext.chromo, lineCounter, record2.position);
                        }
                    } else {
                        hir2.advance(chromoContext.chromo, lineCounter, record2.position);
                        hir2.endPos = record2.position;
                    }
                }
                continue;
            }
            if (hir1 != null) {
                if (hir1.contansOneMismatch) {
                    hirEnded(hir1, chromoContext, hirsContext);
                    hir1 = null;
                    if (hir2 != null) {
                        if (hir2.contansOneMismatch) {
                            System.out.println("This should never occur: both intersecting regions contain mismatches");
                            return;
                        } else {
                            hir2.contansOneMismatch = true;
                            hir2.advance(chromoContext.chromo, lineCounter, record2.position);
                        }
                    }
                } else {
                    if (hir2 != null) {
                        if (hir2.contansOneMismatch) {
                            hirEnded(hir2, chromoContext, hirsContext);
                            hir2 = null;
                        } else {
                            System.out.println("This should not occur: both intersecting regions has no mismatches");
                            return;
                        }
                    }
                    hir1.contansOneMismatch = true;
                    hir1.advance(chromoContext.chromo, lineCounter, record2.position);
                }
            } else {
                if (hir2 != null) {
                    if (hir2.contansOneMismatch) {
                        hirEnded(hir2, chromoContext, hirsContext);
                        hir2 = null;
                    } else {
                        hir2.contansOneMismatch = true;
                        hir2.advance(chromoContext.chromo, lineCounter, record2.position);
                    }
                }
            }
        }
        if (hir1 != null) {
            if (hir2 != null) {
                if (hir1.start < hir2.start) {
                    hirEnded(hir1, chromoContext, hirsContext);
                    hirEnded(hir2, chromoContext, hirsContext);
                } else {
                    hirEnded(hir2, chromoContext, hirsContext);
                    hirEnded(hir1, chromoContext, hirsContext);
                }
            } else {
                hirEnded(hir1, chromoContext, hirsContext);
            }
        } else {
            if (hir2 != null) {
                hirEnded(hir2, chromoContext, hirsContext);
            }
        }
        printChromoReport(chromoContext, hirsContext);
    }

    /**
	 * Call-back on a HIR end  
	 */
    private void hirEnded(CMRegion hir, ChromoContext chromoContext, HirsContext hirsContext) {
        if (hir.getCount() < MIN_THREDHOLD) {
            return;
        }
        if (hir.cMDistance() < CM_THRESHOLD) {
            return;
        }
        hirsContext.addHir(hir);
    }

    /**
	 * After all HIRs are collected 
	 */
    private void postProcess(HirsContext context) {
        int n = context.hirs.size();
        for (int i = 0; i < n; i++) {
            CMRegion hir = (CMRegion) context.hirs.get(i);
            context.sortedHirs.add(hir);
        }
        java.util.Collections.sort(context.sortedHirs, hirsComparator);
        for (int i = 0; i < n; i++) {
            CMRegion hir = (CMRegion) context.sortedHirs.get(i);
            if (hir.removed) continue;
            int indexInList = hir.indexInNaturalList;
            if (indexInList > 0) {
                CMRegion leftNeigbour = (CMRegion) context.hirs.get(indexInList - 1);
                if (leftNeigbour.end > hir.start) {
                    leftNeigbour.removed = true;
                }
            }
            if (indexInList < n - 1) {
                CMRegion rightNeigbour = (CMRegion) context.hirs.get(indexInList + 1);
                if (rightNeigbour.start < hir.end) {
                    rightNeigbour.removed = true;
                }
            }
        }
    }

    private void printChromoReport(ChromoContext chromoContext, HirsContext hirsContext) {
        postProcess(hirsContext);
        int n = hirsContext.sortedHirs.size();
        for (int i = 0; i < n; i++) {
            CMRegion hir = (CMRegion) hirsContext.sortedHirs.get(i);
            if (!hir.removed) {
                printHir(hir, chromoContext);
            }
        }
    }

    private void printHir(CMRegion hir, ChromoContext chromoContext) {
        System.out.println(chromoContext.file1 + " " + chromoContext.file2 + " " + chromoContext.chromo + " " + hir.getCount() + " " + hir.startPos + " " + hir.endPos + " " + Constants.df.format(hir.cMDistance()));
    }

    private void print(PrintStream out) {
    }

    /**
	 * Compare 2 bases and return true - if we have a full mismatch.
	 * Example: 
	 * true returned for bases: (AD TT), (AT II), (AT DD), (A T), (D I), (A DT). 
	 * but not for bases: (AA, AT), (AD --), (-- T), (A AT)      
	 */
    private boolean isFullMismatch(String base1, String base2, SimilarityContext similarityContext) {
        if (base1.charAt(0) == '-' || base2.charAt(0) == '-') {
            return false;
        }
        if (calculateSimilarity) Similarity.calculateSimilarity(base1, base2, similarityContext);
        if (base1.length() == 1) {
            if (base2.length() == 1) {
                if (base1.charAt(0) != base2.charAt(0)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (base1.charAt(0) != base2.charAt(0) && base1.charAt(0) != base2.charAt(1)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            if (base2.length() == 1) {
                if (base1.charAt(0) != base2.charAt(0) && base2.charAt(0) != base1.charAt(1)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (base1.charAt(0) == base2.charAt(0)) {
                    return false;
                } else {
                    if (base1.charAt(1) == base2.charAt(1)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }
}
