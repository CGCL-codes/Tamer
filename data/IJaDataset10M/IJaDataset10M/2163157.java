package org.tigr.microarray.mev.cluster.gui.impl.usc;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;

/**
 * Uncorrelated Shrunken Centroid Algorithm as published in <a
 * href="http://genomebiology.com/2003/4/12/R83">Genome Biology </a> by Kayee
 * Yeung. <br>
 * <br>
 * This code works only for single measurements (no replicate support). It is
 * assumed that the incoming hybs are all sorted identically and that there are
 * no null or NaN values as ratios <br>
 * <br>
 * The main steps are: <br>
 * <br>
 * 1. Split the full set of training hybs into a training subset and a test
 * subset. The cross validation will be run numFold times, wherein each hyb
 * needs to be used as a test hyb once and only once against all the other
 * training hybs. Eg. if there are 10 hybs and numFold is 5, 2 hybs are randomly
 * selected to be used as test hybs for each fold run. During the next fold run,
 * 2 others are randomly selected. This is repeated numFold times such that all
 * hybs were tested once. Remainder hybs are thrown into the last fold's test
 * set <br>
 * <br>
 * 2. Calculate the gene centroid ( the mean ratio for all hybs for each gene )
 * <br>
 * <br>
 * 3. Calculate the class centroids ( the mean ratio for the hybs in each class )
 * <br>
 * <br>
 * 4. Calculate Mk values ( standardizing factor )<br>
 * <br>
 * 5. Calculate S values ( sum of intra-class standard deviations )<br>
 * <br>
 * 6. Calculate s0 ( median S value )<br>
 * <br>
 * 7. Compute Relative Difference ( difference between class centroid and gene
 * centroid standardized by Mk, S, and s0 )<br>
 * <br>
 * 8. Shrink | Relative Difference | by delta ( delta is a random value between
 * 0 and deltaMax incremented by deltaStep )<br>
 * <br>
 * 9. Do soft thresholding on Shrunken Relative Difference ( if subtracting
 * delta from the absolute value of Relative Difference becomes negative, remove
 * that gene from the analysis because it is not significantly different from
 * the gene centroid )<br>
 * <br>
 * 10. Compute Shrunken Class Centroid ( class Centroid + standardized Shrunken
 * Relative Difference )<br>
 * <br>
 * 11. Sort the remaining genes from greatest to least Shrunken Relative
 * Difference <br>
 * <br>
 * 12. Compute pairwise correlation between each gene and the gene with the next
 * greatest Shrunken Relative Difference. <br>
 * <br>
 * 13. Remove from testing if correlation is less than rho ( rho is .5, .6, .7.
 * 8. 9, 1.0 )<br>
 * <br>
 * 14. Compute a new discriminant score for a test hyb against each class <br>
 * <br>
 * 15. Assign the new test hyb to the class with the minimum discriminant score
 * 
 * @author vu
 */
public class USCCrossValidation {

    private int deltaKount;

    private int deltaMax;

    private int foldKount;

    private int xValKount;

    private double rhoMin;

    private double rhoMax;

    private double rhoStep;

    private double deltaStep;

    /**
     * Default and sole constructor
     * 
     * @param hybSetP The training hyb set
     * @param numDeltasP
     * @param deltaMaxP
     * @param rhoMinP
     * @param rhoMaxP
     * @param rhoStepP
     * @param numFoldP
     */
    public USCCrossValidation(int numDeltasP, int deltaMaxP, double rhoMinP, double rhoMaxP, double rhoStepP, int numFoldP, int xValKountP) {
        this.deltaKount = numDeltasP;
        this.deltaMax = deltaMaxP;
        this.rhoMin = rhoMinP;
        this.rhoMax = rhoMaxP;
        this.rhoStep = rhoStepP;
        this.foldKount = numFoldP;
        this.xValKount = xValKountP;
        double dDelta = this.deltaMax;
        double dNum = this.deltaKount;
        this.deltaStep = dDelta / dNum;
    }

    /**
     * Cross Validate the data.
     * 
     * The number of results is the # of Delta/Rho possibilties ( i.e. numDeltas *
     * 6 )
     * 
     * @param fullSet
     * @return
     */
    public USCDeltaRhoResult[][][] crossValidate(USCHybSet fullSet, Frame frame) {
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        USCDeltaRhoResult[][][] xResult = null;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new SpringLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.add(new JLabel("     "));
        JPanel rightPanel = new JPanel();
        rightPanel.add(new JLabel("     "));
        JPanel midPanel = new JPanel();
        BoxLayout midBox = new BoxLayout(midPanel, BoxLayout.Y_AXIS);
        midPanel.setLayout(midBox);
        JLabel label = new JLabel("Cross Validating... Please Wait");
        JLabel label2 = new JLabel("This will take a few minutes");
        JLabel foldLabel = new JLabel("Fold/CrossVal runs");
        JLabel deltaLabel = new JLabel("Deltas");
        JLabel rhoLabel = new JLabel("Rhos");
        JLabel corrLabel = new JLabel("Pairwise Genes");
        JLabel blankLabel = new JLabel(" ");
        midPanel.add(label);
        midPanel.add(label2);
        midPanel.add(blankLabel);
        JProgressBar foldBar = new JProgressBar(0, (this.foldKount * this.xValKount));
        foldBar.setIndeterminate(false);
        foldBar.setStringPainted(true);
        JProgressBar deltaBar = new JProgressBar(0, this.deltaKount);
        deltaBar.setIndeterminate(false);
        deltaBar.setStringPainted(true);
        JProgressBar rhoBar = new JProgressBar(5, 11);
        rhoBar.setIndeterminate(false);
        rhoBar.setStringPainted(true);
        JProgressBar corrBar = new JProgressBar(0, fullSet.getNumGenes());
        corrBar.setIndeterminate(false);
        corrBar.setStringPainted(true);
        midPanel.add(foldLabel);
        midPanel.add(foldBar);
        midPanel.add(deltaLabel);
        midPanel.add(deltaBar);
        midPanel.add(rhoLabel);
        midPanel.add(rhoBar);
        midPanel.add(corrLabel);
        midPanel.add(corrBar);
        mainPanel.add(leftPanel);
        mainPanel.add(midPanel);
        mainPanel.add(rightPanel);
        SpringUtilities.makeCompactGrid(mainPanel, 1, 3, 0, 0, 0, 0);
        JFrame jf = new JFrame("Cross Validating");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(mainPanel);
        jf.setSize(250, 250);
        jf.show();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation((screenSize.width - 200) / 2, (screenSize.height - 100) / 2);
        int iProgress = 0;
        int iRho = 0;
        double currentRho = this.rhoMin;
        while (currentRho < this.rhoMax) {
            iRho++;
            currentRho += this.rhoStep;
        }
        int iTrainStep = this.foldKount * this.xValKount;
        int resultKount = this.deltaKount * iRho;
        xResult = new USCDeltaRhoResult[this.xValKount][this.foldKount][];
        int xResultKount = 0;
        for (int m = 0; m < this.xValKount; m++) {
            for (int f = 0; f < this.foldKount; f++) {
                deltaBar.setValue(0);
                xResult[m][f] = new USCDeltaRhoResult[resultKount];
                int iResult = 0;
                USCHyb[] subTestArray = fullSet.getTestArray(f);
                USCHyb[] subTrainArray = fullSet.getTrainArray(f);
                double delta = 0.0f;
                for (int d = 0; d < this.deltaKount; d++) {
                    rhoBar.setValue(5);
                    double rho;
                    for (int r = 5; r < 11; r++) {
                        rho = (double) r * 0.1f;
                        USCDeltaRhoResult drResult = this.doDR(subTrainArray, subTestArray, delta, rho, fullSet.getNumGenes(), fullSet.getNumClasses(), fullSet.getUniqueClasses(), corrBar, r);
                        if (drResult == null) {
                            xResult[m][f][iResult] = new USCDeltaRhoResult();
                        } else {
                            xResult[m][f][iResult] = drResult;
                            iResult++;
                        }
                        rhoBar.setValue(r);
                    }
                    delta += this.deltaStep;
                    deltaBar.setValue(d + 1);
                }
                xResultKount++;
                foldBar.setIndeterminate(false);
                iProgress++;
                foldBar.setValue(iProgress);
                foldBar.setStringPainted(true);
            }
        }
        jf.dispose();
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return xResult;
    }

    /**
     * Does Discriminant Score Class Assignments to hybs in testArray for one
     * d/r
     * 
     * @param trainArray USCHyb[] of the hybs used for Training
     * @param testArray USCHyb[] of the hybs to be classified
     * @param delta amount by which Dik should be shrunk
     * @param rho Low Correlation threshold
     * @param numGenes Number of Genes in hybs
     * @param numClasses Number of Classes in set of hybs
     * @param uniqueClasses String[] of the labels of each class in set of hybs
     * @return double[ numTestHybs ][ numClasses ]
     */
    public USCDeltaRhoResult doDR(USCHyb[] trainArray, USCHyb[] testArray, double delta, double rho, int numGenes, int numClasses, String[] uniqueClassLabels, JProgressBar rhoBar, int iRho) {
        USCOrder[] order = new USCOrder[numGenes];
        for (int g = 0; g < numGenes; g++) {
            order[g] = new USCOrder(g);
        }
        double[] geneCentroids = this.computeGeneCentroids(trainArray, numGenes);
        double[][] classCentroids = this.computeClassCentroids(trainArray, uniqueClassLabels, numGenes);
        double[] mks = this.computeMks(trainArray, uniqueClassLabels);
        double[] sis = this.computeSis(trainArray, classCentroids, uniqueClassLabels, numGenes);
        double s0 = this.computeMedian(sis);
        double[][] dik = this.computeRelativeDifferences(classCentroids, geneCentroids, mks, sis, s0);
        double[][] dikShrunk = this.shrinkDiks(delta, dik);
        double[][] shrunkenCentroids = this.computeShrunkenClassCentroid(geneCentroids, mks, sis, s0, dikShrunk);
        for (int g = 0; g < numGenes; g++) {
            double maxDik = 0;
            for (int c = 0; c < numClasses; c++) {
                double toTest = Math.abs(dikShrunk[c][g]);
                if (toTest > maxDik) {
                    maxDik = toTest;
                }
            }
            order[g].setBeta(maxDik);
        }
        BitSet bsUse = this.findRelevantGenes(dikShrunk, order);
        int numRelevant = 0;
        for (int g = 0; g < numGenes; g++) {
            if (bsUse.get(g) == true) {
                numRelevant++;
            } else {
            }
        }
        Arrays.sort(order, new USCRelevanceComparator());
        for (int g = 0; g < order.length; g++) {
            order[g].setIRelevant(g);
        }
        if (numRelevant > 0) {
            this.doCorrelationTesting(order, trainArray, rho, rhoBar, geneCentroids, iRho);
        }
        double[][] discScores = this.computeDiscriminantScores(trainArray, testArray, shrunkenCentroids, order, sis, s0, uniqueClassLabels);
        if (discScores == null) {
            return null;
        } else {
            int iGenes = 0;
            for (int g = 0; g < order.length; g++) {
                if (order[g].use()) {
                    iGenes++;
                }
            }
            int numWrong = 0;
            int numRight = 0;
            for (int h = 0; h < testArray.length; h++) {
                USCHyb hyb = testArray[h];
                String label = hyb.getHybLabel();
                double dLow = 9999999;
                int iMin = 0;
                for (int c = 0; c < discScores[h].length; c++) {
                    if (discScores[h][c] < dLow) {
                        dLow = discScores[h][c];
                        iMin = c;
                    }
                }
                if (uniqueClassLabels[iMin].equals(label)) {
                    numRight++;
                } else {
                    numWrong++;
                }
            }
            return new USCDeltaRhoResult(delta, rho, numWrong, numRight, iGenes);
        }
    }

    /**
     * Does Discriminant Score Class Assignments to hybs in testArray for one
     * d/r
     * 
     * @param trainArray
     *            USCHyb[] of the hybs used for Training
     * @param testArray
     *            USCHyb[] of the hybs to be classified
     * @param delta
     *            amount by which Dik should be shrunk
     * @param rho
     *            Low Correlation threshold
     * @param numGenes
     *            Number of Genes in hybs
     * @param numClasses
     *            Number of Classes in set of hybs
     * @param uniqueClasses
     *            String[] of the labels of each class in set of hybs
     * @return double[ numTestHybs ][ numClasses ]
     */
    public USCResult testTest(USCHyb[] trainArray, USCHyb[] testArray, double delta, double rho, int numGenes, int numClasses, String[] uniqueClassLabels, JProgressBar corrBar, int iRho) {
        USCOrder[] order = new USCOrder[numGenes];
        for (int g = 0; g < numGenes; g++) {
            order[g] = new USCOrder(g);
        }
        double[] geneCentroids = this.computeGeneCentroids(trainArray, numGenes);
        double[][] classCentroids = this.computeClassCentroids(trainArray, uniqueClassLabels, numGenes);
        double[] mks = this.computeMks(trainArray, uniqueClassLabels);
        double[] sis = this.computeSis(trainArray, classCentroids, uniqueClassLabels, numGenes);
        double s0 = this.computeMedian(sis);
        double[][] dik = this.computeRelativeDifferences(classCentroids, geneCentroids, mks, sis, s0);
        double[][] dikShrunk = this.shrinkDiks(delta, dik);
        double[][] shrunkenCentroids = this.computeShrunkenClassCentroid(geneCentroids, mks, sis, s0, dikShrunk);
        for (int g = 0; g < numGenes; g++) {
            double maxDik = 0;
            for (int c = 0; c < numClasses; c++) {
                double toTest = Math.abs(dikShrunk[c][g]);
                if (toTest > maxDik) {
                    maxDik = toTest;
                }
            }
            order[g].setBeta(maxDik);
        }
        BitSet bsUse = this.findRelevantGenes(dikShrunk, order);
        int numRelevant = 0;
        for (int g = 0; g < numGenes; g++) {
            if (bsUse.get(g) == true) {
                numRelevant++;
            } else {
            }
        }
        Arrays.sort(order, new USCRelevanceComparator());
        for (int g = 0; g < order.length; g++) {
            order[g].setIRelevant(g);
        }
        if (numRelevant > 0) {
            this.doCorrelationTesting(order, trainArray, rho, corrBar, geneCentroids, iRho);
        }
        double[][] discScores = this.computeDiscriminantScores(trainArray, testArray, shrunkenCentroids, order, sis, s0, uniqueClassLabels);
        if (discScores == null) {
            return null;
        } else {
            int iGenes = 0;
            for (int g = 0; g < order.length; g++) {
                if (order[g].use()) {
                    iGenes++;
                }
            }
            return new USCResult(discScores, iGenes, delta, rho, order);
        }
    }

    /**
     * Computes the discriminant score for a new hyb compared to a trained class
     * by comparing the ratios of the new hyb to the shrunken class centroids
     * 
     * @param testArray ratios of the new test hyb
     * @param shrkClsCntrds shrunkenClassCentroids
     * @param order USCOrder[ numGenes ] contains sorted & original indices
     * @param sis s values
     * @param s0 s0
     * @param numHybs total # of hybs in training set
     * @return double[ numTestHybs ][ numClasses ]
     */
    private double[][] computeDiscriminantScores(USCHyb[] trainArray, USCHyb[] testArray, double[][] shrkClsCntrds, USCOrder[] order, double[] sis, double s0, String[] uniqueClasses) {
        double[][] toReturn = new double[testArray.length][uniqueClasses.length];
        for (int h = 0; h < testArray.length; h++) {
            for (int c = 0; c < shrkClsCntrds.length; c++) {
                double classScore = 0.0f;
                double classProb = 0.0f;
                double fHybsInClassKount = (double) this.getNumClassHybsInUSCHybArray(trainArray, uniqueClasses[c]);
                double fHybKount = (double) trainArray.length;
                classProb = Math.log(fHybsInClassKount / fHybKount);
                for (int g = 0; g < order.length; g++) {
                    if (order[g].use()) {
                        int iOrig = order[g].getIOriginal();
                        double ratio = testArray[h].getRatio(iOrig);
                        double diffSquare = (ratio - shrkClsCntrds[c][iOrig]) * (ratio - shrkClsCntrds[c][iOrig]);
                        double denom = (sis[iOrig] + s0) * (sis[iOrig] + s0);
                        double geneScore = (diffSquare / denom);
                        classScore = classScore + geneScore;
                    } else {
                    }
                }
                toReturn[h][c] = (classScore - (2 * classProb));
            }
        }
        return toReturn;
    }

    /**
     * Having sorted the genes from Greatest to Least Beta, compare pairwise
     * correlation.
     * 
     * @param order
     * @param trainArray
     * @param rho
     */
    private void doCorrelationTesting(USCOrder[] order, USCHyb[] trainArray, double rho, JProgressBar corrBar, double[] geneCentroids, int iRho) {
        for (int r = 9; r >= iRho; r--) {
            double dRho = (double) r / 10.0;
            for (int o = 0; o < order.length; o++) {
                if (order[o].use()) {
                    int iFirstGene = order[o].getIOriginal();
                    for (int j = (o + 1); j < order.length; j++) {
                        if (order[j].use()) {
                            int iSecondGene = order[j].getIOriginal();
                            if (iFirstGene != iSecondGene) {
                                double correlation = Math.abs(this.computeCorrelation(trainArray, iFirstGene, iSecondGene, geneCentroids));
                                if ((correlation > dRho) || (correlation == dRho)) {
                                    order[j].setCorrelated(true);
                                } else {
                                    order[j].setCorrelated(false);
                                }
                            }
                        }
                    }
                }
                corrBar.setValue(o);
                corrBar.setStringPainted(true);
            }
        }
    }

    /**
     * Computes pairwise correlation between geneX and geneY
     * 
     * @param trainArray
     *            USCHyb[] training set
     * @param geneCentroids
     *            Overall centroid for each gene
     * @param iGeneX
     *            Original gene index of 1st gene
     * @param iGeneY
     *            Original gene index of 2nd gene
     * @return
     */
    private double computeCorrelation(USCHyb[] trainArray, int iGeneX, int iGeneY, double[] geneCentroids) {
        double toReturn = 0;
        double[] xRatios = new double[trainArray.length];
        double[] yRatios = new double[trainArray.length];
        for (int i = 0; i < trainArray.length; i++) {
            USCHyb hyb = trainArray[i];
            xRatios[i] = hyb.getRatio(iGeneX);
            yRatios[i] = hyb.getRatio(iGeneY);
        }
        double xMean = geneCentroids[iGeneX];
        double yMean = geneCentroids[iGeneY];
        double numSum = 0;
        double xSum = 0;
        double ySum = 0;
        for (int i = 0; i < trainArray.length; i++) {
            USCHyb hyb = trainArray[i];
            numSum += ((hyb.getRatio(iGeneX) - xMean) * (hyb.getRatio(iGeneY) - yMean));
            xSum += ((xRatios[i] - xMean) * (xRatios[i] - xMean));
            ySum += ((yRatios[i] - yMean) * (yRatios[i] - yMean));
        }
        toReturn = numSum / (double) Math.sqrt(xSum * ySum);
        return toReturn;
    }

    /**
     * Calculates the shrunken class centroid = class centroid + mk( si + s0
     * )dikShrunk
     * 
     * @param geneCentroids
     * @param mks
     * @param sis
     * @param s0
     * @param dikShrunk
     * @return
     */
    private double[][] computeShrunkenClassCentroid(double[] geneCentroids, double[] mks, double[] sis, double s0, double[][] dikShrunk) {
        double[][] toReturn = new double[mks.length][geneCentroids.length];
        for (int c = 0; c < mks.length; c++) {
            for (int g = 0; g < geneCentroids.length; g++) {
                toReturn[c][g] = geneCentroids[g] + (mks[c] * dikShrunk[c][g] * (sis[g] + s0));
            }
        }
        return toReturn;
    }

    /**
     * Finds the shrunken dik value whose absolute value is greatest
     * 
     * @param dikSig
     * @return
     */
    private double[] findBeta(double[][] dikSig) {
        double[] toReturn = new double[dikSig[0].length];
        for (int i = 0; i < toReturn.length; i++) {
            double currentHigh = 0;
            for (int j = 0; j < dikSig.length; j++) {
                if (dikSig[j][i] > currentHigh) {
                    currentHigh = dikSig[j][i];
                }
            }
            toReturn[i] = currentHigh;
        }
        return toReturn;
    }

    /**
     * Removes the genes identified by index in vRemove
     * 
     * @param dikShrunk
     *            double[ class ][ genes ]
     * @param vRemove
     *            Vector of Integer objects representing gene index of genes to
     *            remove
     * @return new double[ class ][ genes ] with insignificant genes removed
     */
    private double[][] removeInsignificantGenes(double[][] dikShrunk, Vector vRemove) {
        int numGenes = dikShrunk[0].length;
        int iSignificant = numGenes - vRemove.size();
        int index = 0;
        double[][] toReturn = new double[dikShrunk.length][iSignificant];
        for (int i = 0; i < numGenes; i++) {
            boolean include = true;
            for (int j = 0; j < vRemove.size(); j++) {
                Integer IRemove = (Integer) vRemove.elementAt(j);
                if (i == IRemove.intValue()) {
                    include = false;
                    break;
                }
            }
            if (include) {
                for (int j = 0; j < dikShrunk.length; j++) {
                    toReturn[j][index] = dikShrunk[j][i];
                }
                index++;
            }
        }
        return toReturn;
    }

    /**
     * Looks through the Shrunken d values (dikStrunk) for those that DO NOT
     * have at least 1 non zero dikShrunk
     * 
     * @param dikShrunk
     * @return BitSet 0 should be removed. 1 should be used. to be removed.
     */
    private BitSet findRelevantGenes(double[][] dikShrunk, USCOrder[] order) {
        BitSet toReturn = new BitSet(dikShrunk[0].length);
        for (int i = 0; i < dikShrunk[0].length; i++) {
            for (int j = 0; j < dikShrunk.length; j++) {
                if (Math.abs(dikShrunk[j][i]) > 0.000000000000000) {
                    toReturn.flip(i);
                    order[i].setRelevant(true);
                    break;
                }
            }
        }
        return toReturn;
    }

    /**
     * Computes the mean ratio value of all the hybs in each gene
     * 
     * @param trainArray
     *            USCHyb[] consisting of training hybs
     * @param numGenes
     *            The number of genes in these hybs
     * @return double[ numGenes ] of mean ratio
     */
    private double[] computeGeneCentroids(USCHyb[] trainArray, int numGenes) {
        double[] toReturn = new double[numGenes];
        for (int i = 0; i < numGenes; i++) {
            double ratioTotal = 0;
            for (int j = 0; j < trainArray.length; j++) {
                ratioTotal = ratioTotal + trainArray[j].getRatio(i);
            }
            toReturn[i] = (ratioTotal / (double) trainArray.length);
        }
        return toReturn;
    }

    /**
     * Computes the mean ratio value of the hybs in each class for each gene
     * 
     * @param trainArray
     *            USCHyb[] consisting of training hybs
     * @param classLabels
     *            String[] of unique class labels
     * @param numGenes
     *            Number of genes
     * @return double[ class ][ genes ] of class mean ratios
     */
    private double[][] computeClassCentroids(USCHyb[] trainArray, String[] classLabels, int numGenes) {
        double[][] toReturn = new double[classLabels.length][numGenes];
        for (int i = 0; i < numGenes; i++) {
            for (int j = 0; j < classLabels.length; j++) {
                double total = 0;
                int kount = 0;
                for (int k = 0; k < trainArray.length; k++) {
                    if (trainArray[k].getHybLabel().equalsIgnoreCase(classLabels[j])) {
                        total = total + trainArray[k].getRatio(i);
                        kount++;
                    }
                }
                toReturn[j][i] = (total / (double) kount);
            }
        }
        return toReturn;
    }

    /**
     * Computes the Relative Difference between a class a gene centroid
     * 
     * @param classCentroids
     * @param geneCentroids
     * @param mks
     * @param sis
     * @param s0
     * @return
     */
    private double[][] computeRelativeDifferences(double[][] classCentroids, double[] geneCentroids, double[] mks, double[] sis, double s0) {
        double[][] toReturn = new double[classCentroids.length][classCentroids[0].length];
        for (int c = 0; c < classCentroids.length; c++) {
            for (int g = 0; g < classCentroids[0].length; g++) {
                toReturn[c][g] = (classCentroids[c][g] - geneCentroids[g]) / (mks[c] * (sis[g] + s0));
            }
        }
        return toReturn;
    }

    /**
     * Computes and sums the within class standard deviations of classes of a
     * gene
     * 
     * @param trainArray
     * @param classCentroids
     * @param classLabels
     * @param numGenes
     * @return
     */
    private double[] computeSis(USCHyb[] trainArray, double[][] classCentroids, String[] classLabels, int numGenes) {
        double firstTerm = 1.00f / ((double) trainArray.length - (double) classLabels.length);
        double[] toReturn = new double[numGenes];
        for (int i = 0; i < numGenes; i++) {
            double geneSum = 0;
            for (int j = 0; j < classLabels.length; j++) {
                double classDiffSquareSum = 0;
                for (int k = 0; k < trainArray.length; k++) {
                    if (trainArray[k].getHybLabel().equalsIgnoreCase(classLabels[j])) {
                        double difference = trainArray[k].getRatio(i) - classCentroids[j][i];
                        double diffSquare = difference * difference;
                        classDiffSquareSum = classDiffSquareSum + diffSquare;
                    }
                }
                geneSum = geneSum + classDiffSquareSum;
            }
            toReturn[i] = (double) Math.sqrt(firstTerm * geneSum);
        }
        return toReturn;
    }

    /**
     * Computes Mk
     * 
     * @param trainArray
     * @param classLabels
     * @return
     */
    private double[] computeMks(USCHyb[] trainArray, String[] classLabels) {
        double[] toReturn = new double[classLabels.length];
        for (int i = 0; i < classLabels.length; i++) {
            int kount = 0;
            for (int j = 0; j < trainArray.length; j++) {
                if (trainArray[j].getHybLabel().equalsIgnoreCase(classLabels[i])) {
                    kount++;
                }
            }
            double firstTerm = 1.00f / (double) kount;
            double secondTerm = 1.00f / (double) trainArray.length;
            toReturn[i] = (double) Math.sqrt(firstTerm + secondTerm);
        }
        return toReturn;
    }

    /**
     * Computes the average value of the doubles in array
     * 
     * @param array
     * @return
     */
    private double computeMean(double[] array) {
        double toReturn = 0;
        for (int i = 0; i < array.length; i++) {
            toReturn = toReturn + array[i];
        }
        return toReturn / (double) array.length;
    }

    /**
     * Finds or computes the median value in array
     * 
     * @param array
     * @return
     */
    private double computeMedian(double[] array) {
        double[] copy = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i];
        }
        Arrays.sort(copy);
        int half = copy.length / 2;
        int remainder = copy.length % 2;
        if (remainder == 0) {
            return copy[half];
        } else {
            return copy[half];
        }
    }

    /**
     * Does soft thresholding on Dik values by subtracting delta from the
     * absolute value of Dik and then reattaching the sign or replacing with 0
     * if the subtraction is negative
     * 
     * @param delta
     * @param diks
     * @return
     */
    private double[][] shrinkDiks(double delta, double[][] diks) {
        double[][] toReturn = new double[diks.length][diks[0].length];
        for (int i = 0; i < diks.length; i++) {
            for (int j = 0; j < diks[0].length; j++) {
                toReturn[i][j] = this.shrinkDik(delta, diks[i][j]);
            }
        }
        return toReturn;
    }

    /**
     * Shrink dik
     * 
     * @param delta
     * @param dik
     * @return
     */
    private double shrinkDik(double delta, double dik) {
        double toReturn = 0;
        if (dik < 0) {
            toReturn = -dik - delta;
        } else {
            toReturn = dik - delta;
        }
        if (toReturn < 0) {
            toReturn = 0;
        } else if (dik < 0) {
            toReturn = -toReturn;
        }
        return toReturn;
    }

    /**
     * Wierd and stupid. Knee deep in my own shit.
     * 
     * @param trainArray
     * @param sClassLabel
     * @return
     */
    public int[] findHybIndicesForClass(USCHyb[] trainArray, int classIndex, USCHybSet hybSet) {
        Vector vHybInClass = new Vector();
        for (int i = 0; i < trainArray.length; i++) {
            USCHyb hyb = trainArray[i];
            if (hyb.getHybLabel().equals(hybSet.getUniqueClass(classIndex))) {
                vHybInClass.add(new Integer(i));
            }
        }
        int[] toReturn = new int[vHybInClass.size()];
        for (int i = 0; i < toReturn.length; i++) {
            Integer I = (Integer) vHybInClass.elementAt(i);
            toReturn[i] = I.intValue();
        }
        return toReturn;
    }

    /**
     * Kounts the # of hybs that belong to the a class
     * 
     * @param hybs
     * @param label
     * @return
     */
    private int getNumClassHybsInUSCHybArray(USCHyb[] hybs, String label) {
        int kount = 0;
        for (int h = 0; h < hybs.length; h++) {
            USCHyb hyb = hybs[h];
            if (hyb.getHybLabel().equals(label)) {
                kount++;
            }
        }
        return kount;
    }

    /**
     * Creates and returns an array of all the USCHybs that belong to a class
     * 
     * @param hybs
     * @param label
     * @return
     */
    private USCHyb[] getClassHybsInUSCHybArray(USCHyb[] hybs, String label) {
        USCHyb[] toReturn = new USCHyb[this.getNumClassHybsInUSCHybArray(hybs, label)];
        int kount = 0;
        for (int h = 0; h < toReturn.length; h++) {
            USCHyb hyb = hybs[h];
            if (hyb.getHybLabel().equals(label)) {
                toReturn[kount] = hyb;
                kount++;
            }
        }
        return toReturn;
    }

    /**
     * Computes log base 10 of x
     * 
     * @param x
     * @return
     */
    private double computeCommonLog(double x) {
        double toReturn = 0.0f;
        toReturn = (double) Math.log(x) / (double) Math.log(10);
        return toReturn;
    }
}
