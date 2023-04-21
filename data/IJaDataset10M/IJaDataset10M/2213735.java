package org.oc.ocvolume.dsp;

/**
 * last modified: June 15, 2002<br>
 * <b>description:</b> this is the detection of the noise and search for the spot where the acutal word is being sounded,
 * and remove the noise from the sample.<br>
 * <b>calls:</b> none<br>
 * <b>called by:</b> volume, train<br>
 * <b>input:</b> speech signal<br>
 * <b>output:</b> modified speech signal
 * @author Keith Fung
 */
public class endPt {

    /**
     * the number of samples for 5ms frame
     */
    static final int frameSize = 80;

    /**
     * a method to use this class<br>
     * calls: zeroCrossingBoolean, avgEnergy, zeroCrossing, chopping<br>
     * called by: volume
     * @param sample the sample that's going to be modified
     * @return modified sample
     */
    public static short[] absCut(short sample[]) {
        if (sample.length > 3200) {
            boolean crossingBoolean[] = zeroCrossingBoolean(sample);
            int energy[] = avgEnergy(sample);
            int crossing[] = zeroCrossing(sample, energy, crossingBoolean);
            short chopped[] = chopping(sample, crossing);
            return chopped;
        } else {
            return sample;
        }
    }

    /**
     * cut the sample into smaller part<br>
     * calls: none<br>
     * called by: absCut
     * @param sample the sample that's going to be chopped
     * @param cut the location that were returned from zeroCrossing or avgEnergy
     * @return the cut sample
     */
    public static short[] chopping(short sample[], int cut[]) {
        cut[0] *= frameSize;
        cut[1] *= frameSize;
        short chopFile[] = new short[cut[1] - cut[0]];
        for (int c = 0; c < chopFile.length; c++) {
            chopFile[c] = sample[cut[0] + c];
        }
        return chopFile;
    }

    /**
     * 5ms frame (80 samples / frame)<br>
     * determent the noise location base on the energy<br>
     * calls: none<br>
     * called by: absCut    
     * @param sample the sample which is being analyze
     * @return squared mean energy
     */
    public static int[] avgEnergy(short sample[]) {
        final double energyConst = 1.95;
        int energyCut[] = new int[2];
        double energyFrame[] = new double[(int) (sample.length / frameSize)];
        double runningSum = 0;
        double noiseEnergy = 0;
        double noiseEnergyThreshold = 0;
        int location = 0;
        int backwardLocation = 0;
        boolean belowThreshold = true;
        boolean valleyFound = true;
        for (int c = 0; c < energyFrame.length; c++) {
            runningSum = 0;
            for (int d = c * frameSize; d < (c + 1) * frameSize; d++) {
                runningSum += (sample[d] * sample[d]);
            }
            energyFrame[c] = runningSum / frameSize;
        }
        runningSum = 0;
        for (int c = 0; c < 20; c++) {
            runningSum += energyFrame[c];
        }
        noiseEnergy = runningSum / 20;
        noiseEnergyThreshold = noiseEnergy * energyConst;
        energyCut[1] = energyFrame.length - 22;
        energyCut[0] = 20;
        location = 20;
        belowThreshold = true;
        while ((location < (energyFrame.length - 36)) && (belowThreshold)) {
            if (energyFrame[location] > noiseEnergyThreshold) {
                runningSum = 0;
                for (int c = 1; c < 17; c++) {
                    if (energyFrame[location + c] > noiseEnergyThreshold) {
                        runningSum++;
                    }
                }
                if (runningSum >= 13) {
                    belowThreshold = false;
                    energyCut[0] = location;
                    valleyFound = true;
                    backwardLocation = 0;
                    while ((valleyFound) && (backwardLocation < 16) && ((location - backwardLocation) > 20)) {
                        if (energyFrame[location - backwardLocation - 1] < energyFrame[location - backwardLocation]) {
                            energyCut[0] = location - backwardLocation - 1;
                        } else {
                            valleyFound = false;
                        }
                        backwardLocation++;
                    }
                }
            }
            location++;
        }
        runningSum = 0;
        for (int c = energyFrame.length - 21; c < energyFrame.length; c++) {
            runningSum += energyFrame[c];
        }
        noiseEnergy = runningSum / 20;
        noiseEnergyThreshold = noiseEnergy * energyConst;
        energyCut[1] = energyFrame.length - 22;
        location = energyFrame.length - 22;
        belowThreshold = true;
        while ((location > 35) && (belowThreshold)) {
            if (energyFrame[location] > noiseEnergyThreshold) {
                runningSum = 0;
                for (int c = 1; c < 17; c++) {
                    if (energyFrame[location - c] > noiseEnergyThreshold) {
                        runningSum++;
                    }
                }
                if (runningSum >= 13) {
                    belowThreshold = false;
                    energyCut[1] = location;
                    valleyFound = true;
                    backwardLocation = 0;
                    while ((valleyFound) && (backwardLocation < 16) && ((location + backwardLocation) < (energyFrame.length - 22))) {
                        if (energyFrame[location + backwardLocation + 1] < energyFrame[location + backwardLocation]) {
                            energyCut[1] = location + backwardLocation + 1;
                        } else {
                            valleyFound = false;
                        }
                        backwardLocation++;
                    }
                    energyCut[1]++;
                }
            }
            location--;
        }
        return energyCut;
    }

    /**
     * calculate the avg energy for 5ms frame<br>
     * calls: none<br>
     * called by: none    
     * @param sample speech signal
     * @return array of squared mean energy in 5ms frame
     */
    private static double[] energyGraph(short sample[]) {
        double runningSum = 0;
        double energyFrame[] = new double[(int) (sample.length / frameSize)];
        for (int c = 0; c < energyFrame.length; c++) {
            runningSum = 0;
            for (int d = c * frameSize; d < (c + 1) * frameSize; d++) {
                runningSum += (sample[d] * sample[d]);
            }
            energyFrame[c] = runningSum / frameSize;
        }
        return energyFrame;
    }

    /**
     * Simply mark off the location of Zero-Crossing<br>
     * calls: none<br>
     * called by: absCut    
     * @return a boolean array with the same size as the sample, true = a zero-crossing, false = no zero-crossing
     * @param sample speech signal
     */
    public static boolean[] zeroCrossingBoolean(short sample[]) {
        boolean crossingBoolean[] = new boolean[sample.length];
        for (int c = 0; c < sample.length - 1; c++) {
            if (((sample[c] > 0) && (sample[c + 1] < 0)) || ((sample[c] < 0) && (sample[c + 1] > 0))) {
                crossingBoolean[c] = true;
            }
        }
        return crossingBoolean;
    }

    /**
     * search for the end points based on zero-crossing and result from avgEnergy<br>
     * calls: none<br>
     * called by: absCut    
     * @param sample speech signal
     * @param energy result from avgEnergy, which is used as the starting point
     * @param crossing result from zeroCrossingBoolean
     * @return start and end of voiced speech signal
     */
    public static int[] zeroCrossing(short sample[], int energy[], boolean crossing[]) {
        final double crossingConst = 12.5;
        int crossingCut[] = new int[2];
        crossingCut[0] = energy[0];
        crossingCut[1] = energy[1] - 1;
        double crossingFrame[] = new double[(int) (sample.length / frameSize)];
        double crossingSD = 0;
        double IZC = 0;
        double IZCT = 0.15625;
        int crossingPeak = 0;
        double runningSum = 0;
        int location = 0;
        for (int c = 0; c < crossingFrame.length; c++) {
            runningSum = 0;
            for (int d = c * frameSize; d < (c + 1) * frameSize; d++) {
                if (crossing[d]) {
                    runningSum++;
                }
            }
            crossingFrame[c] = runningSum / frameSize;
        }
        runningSum = 0;
        for (int c = 0; c < 20; c++) {
            runningSum += crossingFrame[c];
        }
        IZC = runningSum / 20;
        runningSum = 0;
        for (int c = 0; c < 20; c++) {
            runningSum += crossingFrame[c] * crossingFrame[c];
        }
        crossingSD = Math.sqrt((runningSum / 20) - (IZC * IZC));
        if ((0.15625) > (IZC * 2 * crossingSD)) {
            IZCT = IZC * 2 * crossingSD;
        }
        IZCT *= crossingConst;
        location = crossingCut[0] - 16;
        if (location < 20) {
            location = 20;
        }
        crossingPeak = location;
        while (location != crossingCut[0]) {
            if (crossingFrame[crossingPeak] < crossingFrame[location]) {
                crossingPeak = location;
            }
            location++;
        }
        if (IZCT < crossingFrame[crossingPeak]) {
            crossingCut[0] = crossingPeak;
            location = crossingCut[0] - 10;
            if (location < 20) {
                location = 20;
            }
            crossingPeak = location;
            while (location != crossingCut[0]) {
                if (crossingFrame[crossingPeak] >= crossingFrame[location]) {
                    crossingPeak = location;
                }
                location++;
            }
            crossingCut[0] = crossingPeak;
        }
        runningSum = 0;
        for (int c = crossingFrame.length - 21; c < crossingFrame.length; c++) {
            runningSum += crossingFrame[c];
        }
        IZC = runningSum / 20;
        runningSum = 0;
        for (int c = crossingFrame.length - 21; c < crossingFrame.length; c++) {
            runningSum += crossingFrame[c] * crossingFrame[c];
        }
        crossingSD = Math.sqrt((runningSum / 20) - (IZC * IZC));
        IZCT = 0.15625;
        if ((0.15625) > (IZC * 2 * crossingSD)) {
            IZCT = IZC * 2 * crossingSD;
        }
        IZCT *= crossingConst;
        location = crossingCut[1] + 16;
        if (location > crossingFrame.length - 22) {
            location = crossingFrame.length - 22;
        }
        crossingPeak = location;
        while (location != crossingCut[1]) {
            if (crossingFrame[crossingPeak] < crossingFrame[location]) {
                crossingPeak = location;
            }
            location--;
        }
        if (IZCT < crossingFrame[crossingPeak]) {
            crossingCut[1] = crossingPeak;
            location = crossingCut[1] + 10;
            if (location > crossingFrame.length - 22) {
                location = crossingFrame.length - 22;
            }
            crossingPeak = location;
            while (location != crossingCut[1]) {
                if (crossingFrame[crossingPeak] >= crossingFrame[location]) {
                    crossingPeak = location;
                }
                location--;
            }
            crossingCut[1] = crossingPeak;
        }
        crossingCut[1]++;
        return crossingCut;
    }

    /**
     * calculate the zero-crossing rate for 5ms frame<br>
     * calls: none<br>
     * called by: none
     * @param sample speech signal
     * @return an array of zero-crossing rate for every 5ms frame
     */
    private static double[] crossingGraph(short sample[]) {
        boolean crossing[] = zeroCrossingBoolean(sample);
        double crossingFrame[] = new double[(int) (sample.length / frameSize)];
        double runningSum = 0;
        for (int c = 0; c < crossingFrame.length; c++) {
            runningSum = 0;
            for (int d = c * frameSize; d < (c + 1) * frameSize; d++) {
                if (crossing[d]) {
                    runningSum++;
                }
            }
            crossingFrame[c] = runningSum / frameSize;
        }
        return crossingFrame;
    }
}
