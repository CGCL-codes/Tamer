package org.ine.telluride.jaer.cochlea;

import net.sf.jaer.chip.AEChip;
import net.sf.jaer.event.EventPacket;
import net.sf.jaer.event.TypedEvent;
import net.sf.jaer.eventprocessing.EventFilter2D;
import net.sf.jaer.hardwareinterface.HardwareInterfaceException;
import java.util.logging.Logger;
import net.sf.jaer.Description;
import org.ine.telluride.jaer.wowwee.RoboQuadCommands;
import org.ine.telluride.jaer.wowwee.WowWeeRSHardwareInterface;

/**
 * Extracts pitch from AE cochlea spike output.
 * 
 * @author tyu (teddy yu, ucsd)
 */
@Description("Extracts pitch from AE cochlea spike output.")
public class CochleaPitchExtractor extends EventFilter2D {

    private static final int NUM_CHANS = 32;

    private int[][] spikeBuffer = null;

    private boolean[] bufferFull;

    private int[] bufferIndex = null;

    private int spikeCount = 0;

    private int bufferSize = 10;

    static Logger log = Logger.getLogger("HarmonicDetector");

    int periodMin = 2000;

    int periodMax = 20000;

    int periodStep = 200;

    private int numBins = 91;

    private int[] histogram = null;

    int chanNum, ii, binNum, count;

    int popThreshold = 1000;

    int ifHarmonics = 0;

    private int isiValue;

    int isiOrder;

    int maxISIorder = 10;

    int minISIperiod = 2000;

    int lastTs = 0;

    int TsInterval = 250000;

    private RoboQuadCommands rCommands;

    private WowWeeRSHardwareInterface hw;

    int harmonicHistory = 0;

    int commandThreshold = 2;

    public CochleaPitchExtractor(AEChip chip) {
        super(chip);
        resetFilter();
    }

    @Override
    public EventPacket<?> filterPacket(EventPacket<?> in) {
        if (!isFilterEnabled()) return in;
        if (in == null) return in;
        for (Object o : in) {
            if (spikeCount == 0) {
                log.info("start period here ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }
            spikeCount++;
            TypedEvent e = (TypedEvent) o;
            chanNum = e.x & 31;
            bufferIndex[chanNum]++;
            if (bufferIndex[chanNum] >= bufferSize) {
                bufferIndex[chanNum] = 0;
                if (!bufferFull[chanNum]) bufferFull[chanNum] = true;
            }
            spikeBuffer[chanNum][bufferIndex[chanNum]] = e.timestamp;
            updateHistogram(e.x & 31, e.timestamp);
            if (e.timestamp - lastTs > TsInterval) {
                ifHarmonics = detectHarmonics();
                lastTs = lastTs + TsInterval;
                resetHistogram();
                harmonicHistory = harmonicHistory + ifHarmonics;
                if (harmonicHistory > commandThreshold) {
                    log.info("Tell Robot to Dance!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    hw.sendWowWeeCmd((short) rCommands.Dance_Demo);
                }
                if (harmonicHistory < -commandThreshold) {
                    log.info("Tell Robot to Be Surprised!!!!!!!!!!!!!!!!!!!!!");
                    hw.sendWowWeeCmd((short) rCommands.Surprise);
                }
            }
        }
        return in;
    }

    public void updateHistogram(int address, int timestamp) {
        for (isiOrder = 0; isiOrder < maxISIorder; isiOrder++) {
            isiValue = timestamp - spikeBuffer[address][isiOrder];
            if (isiValue > minISIperiod) {
                if (isiValue < periodMax) {
                    binNum = (int) ((isiValue - periodMin) / periodStep);
                    histogram[binNum]++;
                }
            }
        }
        return;
    }

    public int detectHarmonics() {
        int totalNumSpikes = 0;
        checkHardware();
        for (binNum = 0; binNum < numBins; binNum++) {
            totalNumSpikes = totalNumSpikes + histogram[binNum];
        }
        if (totalNumSpikes > popThreshold) {
            int sideOffset = 2;
            int sideRange = 8;
            int threshold = 125;
            int minNonZeroIndex = 4;
            int localMaxVal = 0;
            int localMaxPos = 0;
            double countRatio = 1.5;
            double slopeFactor = 1.2;
            double minCountLeft = 0;
            double minCountRight = 0;
            for (binNum = minNonZeroIndex; binNum < 40; binNum++) {
                if (localMaxVal < histogram[binNum]) {
                    localMaxVal = histogram[binNum];
                    localMaxPos = binNum;
                }
            }
            int minCountLeftMin = minNonZeroIndex;
            int minCountLeftMax = numBins;
            int minCountRightMin = minNonZeroIndex;
            int minCountRightMax = numBins;
            if (localMaxPos - sideOffset - sideRange > minNonZeroIndex) {
                minCountLeftMin = localMaxPos - sideOffset - sideRange;
            }
            if (localMaxPos - sideOffset + sideRange < numBins) {
                minCountLeftMax = localMaxPos - sideOffset + sideRange;
            }
            if (localMaxPos + sideOffset - sideRange > minNonZeroIndex) {
                minCountRightMin = localMaxPos + sideOffset - sideRange;
            }
            if (localMaxPos + sideOffset + sideRange < numBins) {
                minCountRightMax = localMaxPos + sideOffset + sideRange;
            }
            for (ii = minCountLeftMin; ii < minCountLeftMax; ii++) {
                minCountLeft = minCountLeft + histogram[ii];
            }
            minCountLeft = minCountLeft / (minCountLeftMax - minCountLeftMin + 1) * countRatio;
            for (ii = minCountRightMin; ii < minCountRightMax; ii++) {
                minCountRight = minCountRight + histogram[ii];
            }
            minCountRight = minCountRight / (minCountRightMax - minCountRightMin + 1) * countRatio;
            log.info(Double.toString(minCountLeft));
            log.info(Double.toString(minCountRight));
            log.info(Double.toString(minCountRight * slopeFactor));
            if (localMaxVal > threshold) {
                if (localMaxVal > minCountLeft) {
                    if (localMaxVal > minCountRight) {
                        if ((minCountRight * slopeFactor) > minCountLeft) {
                            log.info("detect coo! ***************************************");
                            return 1;
                        }
                    }
                }
            }
            log.info("detect hiss!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return -1;
        }
        log.info("not enough spiking event inputs");
        return 0;
    }

    @Override
    public void resetFilter() {
        allocateSpikeBuffer();
        resetHistogram();
        harmonicHistory = 0;
        lastTs = 0;
    }

    @Override
    public void initFilter() {
        allocateSpikeBuffer();
        resetHistogram();
        harmonicHistory = 0;
        lastTs = 0;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        getPrefs().putInt("CochleaPitchExtractor.bufferSize", bufferSize);
    }

    private void allocateSpikeBuffer() {
        spikeBuffer = new int[NUM_CHANS][bufferSize];
        bufferFull = new boolean[NUM_CHANS];
        bufferIndex = new int[NUM_CHANS];
        for (chanNum = 0; chanNum < NUM_CHANS; chanNum++) {
            for (ii = 0; ii < bufferSize; ii++) {
                spikeBuffer[chanNum][ii] = -periodMin;
            }
            bufferIndex[chanNum] = -1;
            bufferFull[chanNum] = false;
        }
    }

    private void resetHistogram() {
        histogram = new int[numBins];
        for (binNum = 0; binNum < numBins; binNum++) {
            histogram[binNum] = 0;
        }
        spikeCount = 0;
    }

    void checkHardware() {
        if (hw == null) {
            hw = new WowWeeRSHardwareInterface();
        }
        try {
            if (!hw.isOpen()) {
                hw.open();
            }
        } catch (HardwareInterfaceException e) {
            log.warning(e.toString());
        }
    }
}
