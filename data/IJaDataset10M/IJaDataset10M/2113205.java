package imtek.optsuite.psi.unwrapper.impl;

import imtek.optsuite.psi.acquisition.PSIAcquiredData;
import imtek.optsuite.psi.unwrapper.AbstractUnwrapper;
import imtek.optsuite.psi.unwrapper.UnwrappingException;
import java.awt.Dimension;

/**
 * SimpleUnwrapper performes a vertical unwrapping first and then
 * tries to adjust non monotonic data horizontally. 
 *
 * @author Alexander Bieber
 */
public class SimpleUnwrapper extends AbstractUnwrapper {

    private Dimension res;

    public SimpleUnwrapper() {
    }

    private int getArrayIndex(int row, int col) {
        return ((row - 1) * res.width + col - 1);
    }

    public static double mean(double[] p) {
        double sum = 0;
        int count = p.length;
        for (int i = 0; i < p.length; i++) {
            if (Double.isNaN(p[i])) {
                count--;
            } else sum += p[i];
        }
        return sum / p.length;
    }

    public void performUnwrapping(PSIAcquiredData data) throws UnwrappingException {
        if (false) return;
        try {
            System.out.println("Unwrapper: starting unwrapping.");
            res = data.getDimension();
            double[] phaseData = data.getPhaseData();
            int middleRow = (int) res.height / 2;
            int distance = 0;
            int colfractionSteps = 120;
            double phaseStepThreshold = 0.05;
            for (int colfraction = 0; colfraction < colfractionSteps; colfraction++) {
                int fractionInc = 1;
                if (colfraction != 0) fractionInc = 0;
                for (int col = res.width * colfraction / colfractionSteps + fractionInc; col < res.width * (colfraction + 1) / colfractionSteps; col++) {
                    double currPhase = phaseData[getArrayIndex(middleRow, col)];
                    double nextPhase = phaseData[getArrayIndex(middleRow, col + 1)];
                    while (Double.isNaN(nextPhase) && (col < res.width * (colfraction + 1) / colfractionSteps)) {
                        col++;
                        nextPhase = phaseData[getArrayIndex(middleRow, col + 1)];
                    }
                    if (col == res.width * (colfraction + 1) / colfractionSteps) break;
                    double diff = currPhase - nextPhase;
                    if (Math.abs(diff) > phaseStepThreshold) {
                        if (distance > 20) {
                            boolean doPhaseAdjustment = true;
                            for (int j = col + 1; (j < res.width - 1) && (j <= col + 10); j++) {
                                if (Double.isNaN(phaseData[getArrayIndex(middleRow, j + 1)])) continue;
                                double diff2 = phaseData[getArrayIndex(middleRow, j + 1)] - phaseData[getArrayIndex(middleRow, j)];
                                if ((Math.abs(diff2) > phaseStepThreshold * Math.PI)) {
                                    if ((diff > 0) && (diff2 < 0)) {
                                        doPhaseAdjustment = false;
                                    }
                                    if ((diff < 0) && (diff2 > 0)) {
                                        doPhaseAdjustment = false;
                                    }
                                }
                            }
                            if (doPhaseAdjustment) {
                                double phaseStep;
                                if (diff > 0) phaseStep = +Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI; else phaseStep = -Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI;
                                for (int j = col + 1; j < res.width; j++) {
                                    phaseData[getArrayIndex(middleRow, j)] += phaseStep;
                                }
                            }
                            distance = 0;
                        }
                    }
                    distance++;
                }
                for (int col = res.width * colfraction / colfractionSteps + fractionInc; col < res.width * (colfraction + 1) / colfractionSteps; col++) {
                    distance = 0;
                    for (int row = middleRow; row > 1; row--) {
                        double currPhase = phaseData[getArrayIndex(row, col)];
                        double nextPhase = phaseData[getArrayIndex(row - 1, col)];
                        while (Double.isNaN(nextPhase) && (row > 2)) {
                            row--;
                            nextPhase = phaseData[getArrayIndex(row - 1, col)];
                        }
                        if (row == 1) {
                            break;
                        }
                        double diff = currPhase - nextPhase;
                        if (Math.abs(diff) > phaseStepThreshold) {
                            double phaseStep;
                            if (diff > 0) phaseStep = +Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI; else phaseStep = -Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI;
                            for (int j = row - 1; j > 1; j--) {
                                phaseData[getArrayIndex(j, col)] += phaseStep;
                            }
                        }
                    }
                    for (int row = middleRow; row < res.height; row++) {
                        double currPhase = phaseData[getArrayIndex(row, col)];
                        double nextPhase = phaseData[getArrayIndex(row + 1, col)];
                        double diff = currPhase - nextPhase;
                        if (Math.abs(diff) > phaseStepThreshold) {
                            double phaseStep;
                            if (diff > 0) phaseStep = +Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI; else phaseStep = -Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI;
                            for (int j = row + 1; j < res.height; j++) {
                                phaseData[getArrayIndex(j, col)] += phaseStep;
                            }
                        }
                    }
                }
            }
            for (int col = 1; col < res.width - 1; col++) {
                double currPhase = phaseData[getArrayIndex(middleRow, col)];
                double nextPhase = phaseData[getArrayIndex(middleRow, col + 1)];
                double diff = currPhase - nextPhase;
                if (Math.abs(diff) > phaseStepThreshold) {
                    if (distance > 5) {
                        boolean doPhaseAdjustment = true;
                        for (int j = col + 1; (j < res.width - 1) && (j <= col + 1); j++) {
                            if (Double.isNaN(phaseData[getArrayIndex(middleRow, j + 1)])) continue;
                            double diff2 = phaseData[getArrayIndex(middleRow, j + 1)] - phaseData[getArrayIndex(middleRow, j)];
                            if ((Math.abs(diff2) > phaseStepThreshold * Math.PI)) {
                                if ((diff > 0) && (diff2 < 0)) {
                                    doPhaseAdjustment = false;
                                }
                                if ((diff < 0) && (diff2 > 0)) {
                                    doPhaseAdjustment = false;
                                }
                            }
                        }
                        if (doPhaseAdjustment) {
                            double phaseStep;
                            if (diff > 0) phaseStep = +Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI; else phaseStep = -Math.ceil(Math.abs(diff) / (2 * Math.PI)) * 2 * Math.PI;
                            for (int j = col + 1; j < res.width; j++) {
                                for (int rows = 1; rows < res.height; rows++) {
                                    if (!Double.isNaN(phaseData[getArrayIndex(rows, j)])) phaseData[getArrayIndex(rows, j)] += phaseStep;
                                }
                            }
                        }
                        distance = 0;
                    }
                }
                distance++;
            }
            for (int test = 1; test < 4; test++) {
                middleRow = (int) res.height / 2;
                middleRow = middleRow / 2;
                for (int col = 1; col < res.width - 1; col++) {
                    double currPhase = phaseData[getArrayIndex(middleRow, col)];
                    double nextPhase = phaseData[getArrayIndex(middleRow, col + 1)];
                    double diff = currPhase - nextPhase;
                    if (Math.abs(diff) > phaseStepThreshold) {
                        if (distance >= 0) {
                            boolean doPhaseAdjustment = true;
                            for (int j = col + 1; (j < res.width - 1) && (j <= col + 1); j++) {
                                if (Double.isNaN(phaseData[getArrayIndex(middleRow, j + 1)])) continue;
                                double diff2 = phaseData[getArrayIndex(middleRow, j + 1)] - phaseData[getArrayIndex(middleRow, j)];
                                if ((Math.abs(diff2) > phaseStepThreshold * Math.PI)) {
                                    if ((diff > 0) && (diff2 < 0)) {
                                        doPhaseAdjustment = false;
                                    }
                                    if ((diff < 0) && (diff2 > 0)) {
                                        doPhaseAdjustment = false;
                                    }
                                }
                            }
                            if (doPhaseAdjustment) {
                                double phaseStep;
                                phaseStep = diff;
                                for (int j = col + 1; j < res.width; j++) {
                                    for (int rows = 1; rows < res.height / 2; rows++) {
                                        if (!Double.isNaN(phaseData[getArrayIndex(rows, j)])) phaseData[getArrayIndex(rows, j)] += phaseStep;
                                    }
                                }
                            }
                            distance = 0;
                        }
                    }
                    distance++;
                }
                middleRow = middleRow * 3;
                for (int col = 1; col < res.width - 1; col++) {
                    double currPhase = phaseData[getArrayIndex(middleRow, col)];
                    double nextPhase = phaseData[getArrayIndex(middleRow, col + 1)];
                    double diff = currPhase - nextPhase;
                    if (Math.abs(diff) > phaseStepThreshold) {
                        if (distance >= 0) {
                            boolean doPhaseAdjustment = true;
                            for (int j = col + 1; (j < res.width - 1) && (j <= col + 1); j++) {
                                if (Double.isNaN(phaseData[getArrayIndex(middleRow, j + 1)])) continue;
                                double diff2 = phaseData[getArrayIndex(middleRow, j + 1)] - phaseData[getArrayIndex(middleRow, j)];
                                if ((Math.abs(diff2) > phaseStepThreshold * Math.PI)) {
                                    if ((diff > 0) && (diff2 < 0)) {
                                        doPhaseAdjustment = false;
                                    }
                                    if ((diff < 0) && (diff2 > 0)) {
                                        doPhaseAdjustment = false;
                                    }
                                }
                            }
                            if (doPhaseAdjustment) {
                                double phaseStep;
                                phaseStep = diff;
                                for (int j = col + 1; j < res.width; j++) {
                                    for (int rows = res.height / 2; rows < res.height; rows++) {
                                        if (!Double.isNaN(phaseData[getArrayIndex(rows, j)])) phaseData[getArrayIndex(rows, j)] += phaseStep;
                                    }
                                }
                            }
                            distance = 0;
                        }
                    }
                    distance++;
                }
            }
            int middleCol = res.width / 2;
            for (int row = 1; row < res.height - 1; row++) {
                double currPhase = phaseData[getArrayIndex(row, middleCol)];
                double nextPhase = phaseData[getArrayIndex(row + 1, middleCol)];
                double diff = currPhase - nextPhase;
                if (Math.abs(diff) > phaseStepThreshold) {
                    if (distance >= 0) {
                        boolean doPhaseAdjustment = true;
                        for (int j = row + 1; (j < res.height - 1) && (j <= row + 1); j++) {
                            if (Double.isNaN(phaseData[getArrayIndex(j + 1, middleCol)])) continue;
                            double diff2 = phaseData[getArrayIndex(j + 1, middleCol)] - phaseData[getArrayIndex(j, middleCol)];
                            if ((Math.abs(diff2) > phaseStepThreshold * Math.PI)) {
                                if ((diff > 0) && (diff2 < 0)) {
                                    doPhaseAdjustment = false;
                                }
                                if ((diff < 0) && (diff2 > 0)) {
                                    doPhaseAdjustment = false;
                                }
                            }
                        }
                        if (doPhaseAdjustment) {
                            double phaseStep;
                            phaseStep = diff;
                            for (int j = row + 1; j < res.height; j++) {
                                for (int cols = 1; cols < res.width; cols++) {
                                    if (!Double.isNaN(phaseData[getArrayIndex(j, cols)])) phaseData[getArrayIndex(j, cols)] += phaseStep;
                                }
                            }
                        }
                        distance = 0;
                    }
                }
                distance++;
            }
        } catch (Exception e) {
            throw new UnwrappingException(e);
        }
    }
}
