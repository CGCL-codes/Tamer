package it.southdown.avana.appcontrol;

import it.southdown.avana.alignment.*;
import it.southdown.avana.alignscan.*;
import it.southdown.avana.analysis.Analysis;
import it.southdown.avana.comparison.*;
import it.southdown.avana.entropy.*;
import it.southdown.avana.util.*;
import java.util.*;

public class AvanaController {

    private static AvanaController instance = new AvanaController();

    public static AvanaController instance() {
        return instance;
    }

    private AlignmentManager alignmentManager;

    private AlignmentScanManager scanManager;

    private SetAnalysisManager setManager;

    private PairComparativeAnalysisManager pairComparisonManager;

    private MultiComparativeAnalysisManager multiComparisonManager;

    private Analysis currentAnalysis;

    private Region[] regions;

    private AvanaController() {
        alignmentManager = new AlignmentManager(this);
        scanManager = new AlignmentScanManager(this);
        setManager = new SetAnalysisManager(this);
        pairComparisonManager = new PairComparativeAnalysisManager(this);
        multiComparisonManager = new MultiComparativeAnalysisManager(this);
        alignmentManager.addAlignmentManagerListener(new AlignmentManagerListener() {

            public void alignmentListChanged() {
            }

            public void alignmentChanged(Alignment a) {
                getPairComparativeAnalysisManager().alignmentChanged(a);
                getMultiComparativeAnalysisManager().alignmentChanged(a);
                getSetAnalysisManager().alignmentChanged(a);
                getAlignmentScanManager().alignmentChanged(a);
                updateCurrentAnalysis();
            }
        });
    }

    public Analysis getCurrentAnalysis() {
        return currentAnalysis;
    }

    public void setCurrentAnalysis(Analysis newAnalysis) {
        currentAnalysis = newAnalysis;
        if (currentAnalysis != null) {
            currentAnalysis.updateRegions(regions);
        }
    }

    private Alignment getUpdatedAlignment(Alignment alignment) {
        String alignmentName = alignment.getName();
        Alignment a = alignmentManager.getAlignment(alignmentName);
        if (a == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return a;
    }

    public void updateCurrentAnalysis() {
        if (currentAnalysis == null) {
            return;
        }
        try {
            if (currentAnalysis instanceof SetAnalysis) {
                Alignment alignment = getUpdatedAlignment(((SetAnalysis) currentAnalysis).getAlignment());
                currentAnalysis = setManager.getSetAnalysis(alignment);
            } else if (currentAnalysis instanceof PairComparativeAnalysis) {
                Pair<Alignment> pair = ((PairComparativeAnalysis) currentAnalysis).getAlignmentPair();
                Pair<Alignment> newPair = new Pair<Alignment>(getUpdatedAlignment(pair.getMember1()), getUpdatedAlignment(pair.getMember2()));
                currentAnalysis = pairComparisonManager.getComparativeAnalysis(newPair);
            } else if (currentAnalysis instanceof MultiComparativeAnalysis) {
                Alignment[] alignments = ((MultiComparativeAnalysis) currentAnalysis).getAlignments();
                ArrayList<Alignment> newList = new ArrayList<Alignment>();
                for (Alignment a : alignments) {
                    newList.add(getUpdatedAlignment(a));
                }
                currentAnalysis = multiComparisonManager.getComparativeAnalysis(newList);
            }
        } catch (IllegalArgumentException e) {
            Alignment master = alignmentManager.getMasterAlignment();
            currentAnalysis = setManager.getSetAnalysis(master);
        }
    }

    public void clearAllAnalysis() {
        scanManager.clear();
        setManager.clear();
        pairComparisonManager.clear();
        multiComparisonManager.clear();
    }

    public AlignmentManager getAlignmentManager() {
        return alignmentManager;
    }

    public AlignmentScanManager getAlignmentScanManager() {
        return scanManager;
    }

    public SetAnalysisManager getSetAnalysisManager() {
        return setManager;
    }

    public PairComparativeAnalysisManager getPairComparativeAnalysisManager() {
        return pairComparisonManager;
    }

    public MultiComparativeAnalysisManager getMultiComparativeAnalysisManager() {
        return multiComparisonManager;
    }

    public Region[] getRegions() {
        return regions;
    }

    public void setRegions(Region[] regions) {
        this.regions = regions;
        if (currentAnalysis != null) {
            currentAnalysis.updateRegions(regions);
        }
    }
}
