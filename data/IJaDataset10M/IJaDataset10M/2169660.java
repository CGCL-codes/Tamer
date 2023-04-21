package org.systemsbiology.dbsearch;

import java.io.*;
import java.util.*;
import org.apache.regexp.RE;
import java.text.DecimalFormat;

public class ProbID {

    Parameter param;

    SeqUtility seqUtil;

    Spectrum spec;

    SequenceFactory seqFactory;

    PepFactory pepFactory;

    SpecMatcher specMatch;

    String inputFileName;

    ProbID(String specFileName) {
        param = new Parameter();
        seqUtil = new SeqUtility();
        spec = new Spectrum(specFileName);
        inputFileName = specFileName;
    }

    /**
     *@get all necessary paramters
     */
    public void initialize() {
        param.readParamFile();
        spec.readSpec();
        spec.truncatePeakList();
        spec.normalize();
        spec.buildIndex();
    }

    /**
     *@report all top scores in .out file
     */
    public void reportTopScore(ArrayList topScore, double mean, double stddev) {
        PepScore tmpScore = null;
        String outputLine = "";
        Peptide tmpPeptide = null;
        DecimalFormat oneDigit = new DecimalFormat("0.0");
        int pos = inputFileName.indexOf("dta");
        String outFileName = inputFileName.substring(0, pos) + "out";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName));
            for (int i = 0; i < topScore.size(); i++) {
                outputLine = "";
                tmpScore = (PepScore) topScore.get(i);
                tmpPeptide = tmpScore.pep;
                outputLine = getPepSeq(tmpPeptide.pepSeq, tmpPeptide.frontChar, tmpPeptide.endChar) + "\t" + tmpPeptide.pepID + "\t" + getPepMass(oneDigit, tmpPeptide.pepMass) + "\t" + tmpScore.numMatchedIons + "/" + tmpScore.numTotalIons + "\t" + oneDigit.format(tmpScore.baysScore) + "\t" + getZScore(oneDigit, tmpScore.baysScore, mean, stddev);
                bw.write(outputLine);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error happened in output result!");
            e.printStackTrace();
        }
    }

    public String getPepSeq(String seq, char frontChar, char endChar) {
        StringBuffer returnString = new StringBuffer();
        returnString.append(Character.toUpperCase(frontChar));
        returnString.append(".");
        char tmpChar = '^';
        for (int i = 0; i < seq.length(); i++) {
            tmpChar = seq.charAt(i);
            if (Character.isLowerCase(tmpChar)) {
                if ((param.dynamicMod).indexOf(tmpChar) != -1) {
                    returnString.append(Character.toUpperCase(tmpChar));
                    returnString.append('@');
                } else returnString.append(Character.toUpperCase(tmpChar));
            } else returnString.append(tmpChar);
        }
        returnString.append(".");
        returnString.append(Character.toUpperCase(endChar));
        return (returnString.toString());
    }

    public String getPepMass(DecimalFormat oneDigit, double pepMass) {
        String returnString = null;
        double diffMass = pepMass - spec.precursorMH;
        returnString = oneDigit.format(pepMass) + "(" + oneDigit.format(diffMass) + ")";
        return returnString;
    }

    public String getZScore(DecimalFormat oneDigit, double baysScore, double mean, double stddev) {
        if (stddev == 0) return "0"; else {
            double zVal = (baysScore - mean) / stddev;
            if (zVal > 25) return "0"; else return (oneDigit.format(zVal));
        }
    }

    public void process() {
        seqFactory = new SequenceFactory(param.dbPath);
        ArrayList seqs = null;
        pepFactory = new PepFactory(param, seqUtil);
        pepFactory.setPrecursorMH(spec.precursorMH);
        specMatch = new SpecMatcher();
        specMatch.setParam(param);
        specMatch.setSpec(spec);
        specMatch.setUtility(seqUtil);
        specMatch.setCharge(spec.precursorCharge);
        while (!(seqFactory.done())) {
            seqFactory.loadNext();
            seqs = seqFactory.getSequences();
            for (int i = 0; i < seqs.size(); i++) {
                pepFactory.setSequence((Sequence) seqs.get(i));
                pepFactory.process();
                specMatch.match(pepFactory.getPeptides());
                pepFactory.emptyPeptides();
            }
        }
        FitNormal tmpFit = new FitNormal(specMatch.getAllScore());
        tmpFit.find_param();
        double allScoreMean = tmpFit.get_mean();
        double allScoreStdDev = tmpFit.get_stddev();
        ArrayList topScore = specMatch.getTopScore();
        Collections.sort(topScore, new baysComparator());
        reportTopScore(topScore, allScoreMean, allScoreStdDev);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ProbID your_spec.dta");
            System.exit(1);
        } else {
            ProbID tmpProbID = new ProbID(args[0]);
            tmpProbID.initialize();
            tmpProbID.process();
        }
    }
}
