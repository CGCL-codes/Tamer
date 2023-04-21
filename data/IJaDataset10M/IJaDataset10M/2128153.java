package net.maizegenetics;

import java.io.*;
import java.util.*;

public class Settings implements Serializable {

    public String saveDir, openDir, dataBaseName, theLogin, thePassword;

    public boolean matchChar = false;

    public boolean calcDiseq, acrossLoci, graphDiseq;

    public boolean removeAdjIdenticalSites, removeAdjLinkedSites, ignoreMissingWhenEvalIdentical;

    public boolean regPermutation, regStepwise, regSimple, regIncludeBackground, regIncPolyBackground, regIncGeneBackground[];

    public int regPermutationNumber;

    public double regThresholdPValue, regBackThresholdPValue;

    public int diversityWindowSize, diversityStepSize, haploWindowSize;

    public boolean includeSSR, includeGenes;

    public boolean filterPolyByStateNumber, filterRowsWithMissingData;

    public boolean[] geneInc, polyInc;

    public int[] startSite, endSite, alignment, minCount;

    public int maxPolyStates, minPolyStates;

    public double[] minFreq;

    public int totalPossibleGenes, totalPossiblePoly, totalPossiblePhenotypes, sampleSizeChoice, samplePopulation;

    public boolean sampleAll, sampleAllPops;

    public int samplingStrategy;

    public double missingData = -999.0;

    public Vector environInc;

    public String phenotypeName;

    public boolean standardPhenoAgainstLine;

    public int standardPhenoStockID;

    public double preferredMinFreqForFilteringSites;

    public String polyLocusQuery, polyIDFilter, stockIDFilter, stockFilterName;

    public boolean allStockIncluded, allPolyIncluded;

    public Settings() {
        initialize();
    }

    private void initialize() {
        saveDir = "Data";
        openDir = "Data";
        dataBaseName = "jdbc:oracle:thin:@gaut.statgen.ncsu.edu:1521:panzea";
        theLogin = "web";
        thePassword = "panzea99";
        calcDiseq = true;
        acrossLoci = true;
        graphDiseq = true;
        regPermutation = true;
        regStepwise = false;
        regSimple = false;
        regIncludeBackground = false;
        regIncPolyBackground = false;
        regPermutationNumber = 100;
        regThresholdPValue = 0.01;
        regBackThresholdPValue = 0.01;
        diversityWindowSize = 200;
        diversityStepSize = 50;
        haploWindowSize = 10;
        includeSSR = false;
        includeGenes = false;
        removeAdjIdenticalSites = true;
        removeAdjLinkedSites = true;
        ignoreMissingWhenEvalIdentical = true;
        filterPolyByStateNumber = false;
        filterRowsWithMissingData = true;
        polyLocusQuery = "";
        totalPossibleGenes = 6;
        totalPossiblePoly = 1;
        totalPossiblePhenotypes = 1;
        maxPolyStates = 2;
        minPolyStates = 2;
        sampleAll = true;
        sampleAllPops = true;
        sampleSizeChoice = 1000;
        samplePopulation = 0;
        samplingStrategy = 0;
        environInc = new Vector();
        allStockIncluded = true;
        standardPhenoAgainstLine = false;
        standardPhenoStockID = 13;
        phenotypeName = "DSILK";
        stockFilterName = "All";
        geneInc = new boolean[totalPossibleGenes];
        regIncGeneBackground = new boolean[totalPossibleGenes];
        polyInc = new boolean[totalPossiblePoly];
        startSite = new int[totalPossibleGenes];
        endSite = new int[totalPossibleGenes];
        minFreq = new double[totalPossibleGenes];
        alignment = new int[totalPossibleGenes];
        minCount = new int[totalPossibleGenes];
        for (int i = 0; i < totalPossibleGenes; i++) {
            geneInc[i] = false;
            regIncGeneBackground[i] = false;
            startSite[i] = 0;
            endSite[i] = 1000;
            alignment[i] = -999;
            minFreq[i] = 0.05;
            minCount[i] = 50;
        }
        for (int i = 0; i < totalPossiblePoly; i++) {
            polyInc[i] = false;
        }
        preferredMinFreqForFilteringSites = 0.0010;
    }

    public void resetGeneAndPolyInclusion() {
        for (int i = 0; i < totalPossibleGenes; i++) {
            geneInc[i] = false;
        }
        for (int i = 0; i < totalPossiblePoly; i++) {
            polyInc[i] = false;
        }
        includeSSR = false;
    }
}
