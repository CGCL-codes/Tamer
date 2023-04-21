package de.sciss.fscape.op;

import java.io.*;
import de.sciss.fscape.gui.*;
import de.sciss.fscape.prop.*;
import de.sciss.fscape.spect.*;
import de.sciss.fscape.util.*;

/**
 *  @version	0.71, 14-Nov-07
 */
public class TarnishOp extends Operator {

    protected static final String defaultName = "Tarnish";

    protected static Presets static_presets = null;

    protected static Prefs static_prefs = null;

    protected static PropertyArray static_pr = null;

    protected static final int SLOT_INPUT = 0;

    protected static final int SLOT_OUTPUT = 1;

    private static final int PR_LOFREQ = 0;

    private static final int PR_HIFREQ = 1;

    private static final int PR_WINDOW = 0;

    private static final int PR_OVERSMP = 1;

    private static final int PR_MODE = 2;

    private static final int PR_RESIDUAL = 0;

    private static final int PR_MINPHASE = 1;

    protected static final int MODE_OVERTONES = 0;

    protected static final int MODE_ALL = 1;

    private static final String PRN_LOFREQ = "LoFreq";

    private static final String PRN_HIFREQ = "HiFreq";

    private static final String PRN_OVERSMP = "OverSmp";

    private static final String PRN_WINDOW = "Window";

    private static final String PRN_RESIDUAL = "Residual";

    private static final String PRN_MINPHASE = "MinPhase";

    private static final String PRN_MODE = "Mode";

    private static final Param prPara[] = { null, null };

    private static final String prParaName[] = { PRN_LOFREQ, PRN_HIFREQ };

    private static final int prIntg[] = { 0, 0, MODE_OVERTONES };

    private static final String prIntgName[] = { PRN_OVERSMP, PRN_WINDOW, PRN_MODE };

    private static final boolean prBool[] = { false, true };

    private static final String prBoolName[] = { PRN_RESIDUAL, PRN_MINPHASE };

    public TarnishOp() {
        super();
        if (static_prefs == null) {
            static_prefs = new OpPrefs(getClass(), getDefaultPrefs());
        }
        if (static_pr == null) {
            static_pr = new PropertyArray();
            static_pr.para = prPara;
            static_pr.para[PR_LOFREQ] = new Param(100.0, Param.ABS_HZ);
            static_pr.para[PR_HIFREQ] = new Param(3000.0, Param.ABS_HZ);
            static_pr.paraName = prParaName;
            static_pr.intg = prIntg;
            static_pr.intgName = prIntgName;
            static_pr.bool = prBool;
            static_pr.boolName = prBoolName;
            static_pr.superPr = Operator.op_static_pr;
        }
        if (static_presets == null) {
            static_presets = new Presets(getClass(), static_pr.toProperties(true));
        }
        opName = "TarnishOp";
        prefs = static_prefs;
        presets = static_presets;
        pr = (PropertyArray) static_pr.clone();
        slots.addElement(new SpectStreamSlot(this, Slots.SLOTS_READER));
        slots.addElement(new SpectStreamSlot(this, Slots.SLOTS_WRITER));
        icon = new OpIcon(this, OpIcon.ID_FLIPFREQ, defaultName);
    }

    public void run() {
        runInit();
        int ch, i, j, k, m, n, p, q;
        float f1, f2, f3;
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        int srcBands, fftSize, fullFFTsize, complexFFTsize, winSize, winHalf;
        float[] fftBuf, convBuf1, convBuf2, win;
        int[] belowMin, aboveMin;
        float freqSpacing, minAmp;
        float[] slope, maxima;
        int oversmp = pr.intg[PR_OVERSMP];
        int loBand, hiBand;
        int numPeaks;
        topLevel: try {
            runInSlot = (SpectStreamSlot) slots.elementAt(SLOT_INPUT);
            if (runInSlot.getLinked() == null) {
                runStop();
            }
            for (boolean initDone = false; !initDone && !threadDead; ) {
                try {
                    runInStream = runInSlot.getDescr();
                    initDone = true;
                } catch (InterruptedException e) {
                }
                runCheckPause();
            }
            if (threadDead) break topLevel;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutSlot.initWriter(runOutStream);
            srcBands = runInStream.bands;
            winSize = (srcBands - 1) << 1 >> oversmp;
            winHalf = winSize >> 1;
            win = Filter.createFullWindow(winSize, pr.intg[PR_WINDOW]);
            fftSize = srcBands - 1;
            fullFFTsize = fftSize << 1;
            complexFFTsize = fullFFTsize << 1;
            fftBuf = new float[complexFFTsize];
            freqSpacing = (runInStream.hiFreq - runInStream.loFreq) / runInStream.bands;
            System.arraycopy(win, winHalf, fftBuf, 0, winHalf);
            System.arraycopy(win, 0, fftBuf, fullFFTsize - winHalf, winHalf);
            win = new float[fftSize + 1];
            Fourier.realTransform(fftBuf, fullFFTsize, Fourier.FORWARD);
            f1 = 1.0f / fftBuf[0];
            for (i = 0, j = 0; i <= fullFFTsize; i += 2, j++) {
                win[j] = fftBuf[i] * f1;
            }
            for (j = 0; j <= fftSize; j++) {
                if (win[j] < 0.125f) break;
            }
            winSize = j;
            loBand = Math.max(winSize, Math.min(srcBands - 1, (int) (((pr.para[PR_LOFREQ].val - runInStream.loFreq) / freqSpacing) + 0.5)));
            hiBand = Math.min(srcBands - winSize, Math.max(loBand, (int) (((pr.para[PR_HIFREQ].val - runInStream.loFreq) / freqSpacing) + 0.5)));
            slope = new float[srcBands];
            maxima = new float[srcBands];
            belowMin = new int[srcBands];
            aboveMin = new int[srcBands];
            minAmp = 1.0e-3f;
            runSlotsReady();
            mainLoop: while (!threadDead) {
                for (boolean readDone = false; (readDone == false) && !threadDead; ) {
                    try {
                        runInFr = runInSlot.readFrame();
                        readDone = true;
                        runOutFr = runOutStream.allocFrame();
                    } catch (InterruptedException e) {
                    } catch (EOFException e) {
                        break mainLoop;
                    }
                    runCheckPause();
                }
                if (threadDead) break mainLoop;
                for (ch = 0; ch < runOutStream.chanNum; ch++) {
                    convBuf1 = runInFr.data[ch];
                    convBuf2 = runOutFr.data[ch];
                    Util.clear(maxima);
                    f1 = 0.0f;
                    for (i = 0, j = 0; j < srcBands; i += 2, j++) {
                        f2 = f1;
                        f1 = convBuf1[i];
                        slope[j] = f1 - f2;
                    }
                    f1 = slope[0];
                    for (i = loBand, m = i << 1; i < (srcBands - winSize); i++, m += 2) {
                        f2 = f1;
                        f1 = slope[i];
                        f3 = convBuf1[m];
                        peakPick: if ((f2 >= 0.0f) && (f1 <= 0.0f) && (f3 > minAmp)) {
                            k = i - winSize;
                            for (j = i; j > k; ) {
                                if (slope[--j] <= 0.0f) break peakPick;
                            }
                            while (j > 0) {
                                if (slope[--j] <= 0.0f) break;
                            }
                            j++;
                            k = i + winSize;
                            for (p = i; p < k; ) {
                                if (slope[++p] >= 0.0f) break peakPick;
                            }
                            while (p < fftSize) {
                                if (slope[++p] >= 0.0f) break;
                            }
                            p--;
                            maxima[i] = f3;
                            belowMin[i] = j;
                            aboveMin[i] = p;
                        }
                    }
                    for (i = 0; i <= fftSize; ) {
                        fftBuf[i++] = 1.0f;
                    }
                    numPeaks = 0;
                    for (i = loBand; i <= hiBand; i++) {
                        if (maxima[i] == 0.0f) continue;
                        switch(pr.intg[PR_MODE]) {
                            case MODE_OVERTONES:
                                for (j = i << 1, q = 2; j < srcBands; j += i, q++) {
                                    for (m = Math.max(i + 1, j - winSize - q); m < Math.min(srcBands, j + winSize + q); m++) {
                                        if (maxima[m] == 0.0f) continue;
                                        n = belowMin[m];
                                        p = aboveMin[m];
                                        for (n = m - 1, p = 0; p < (m - 1); n--, p++) {
                                            fftBuf[n] *= 1.0f - win[p];
                                        }
                                        fftBuf[m] = 0.0f;
                                        for (n = m + 1, p = 0; p < fftSize - m; n++, p++) {
                                            fftBuf[n] *= 1.0f - win[p];
                                        }
                                        maxima[m] = 0.0f;
                                        numPeaks++;
                                    }
                                }
                                break;
                            case MODE_ALL:
                                m = i;
                                n = belowMin[m];
                                p = aboveMin[m];
                                for (n = m - 1, p = 0; p < (m - 1); n--, p++) {
                                    fftBuf[n] *= 1.0f - win[p];
                                }
                                fftBuf[m] = 0.0f;
                                for (n = m + 1, p = 0; p < fftSize - m; n++, p++) {
                                    fftBuf[n] *= 1.0f - win[p];
                                }
                                maxima[m] = 0.0f;
                                numPeaks++;
                                break;
                        }
                    }
                    if (pr.bool[PR_RESIDUAL]) {
                        for (i = fftSize + 1, j = fullFFTsize + 2; i > 0; ) {
                            fftBuf[--j] = 0.0f;
                            fftBuf[--j] = 1.0f - fftBuf[--i];
                        }
                    } else {
                        for (i = fftSize + 1, j = fullFFTsize + 2; i > 0; ) {
                            fftBuf[--j] = 0.0f;
                            fftBuf[--j] = fftBuf[--i];
                        }
                    }
                    if (pr.bool[PR_MINPHASE] && (numPeaks > 0)) {
                        for (i = 0; i <= fullFFTsize; i += 2) {
                            fftBuf[i] = (float) Math.log(Math.max(1.0e-24, fftBuf[i]));
                        }
                        for (i = fullFFTsize + 2, j = fullFFTsize - 2; i < complexFFTsize; j -= 2) {
                            fftBuf[i++] = fftBuf[j];
                            fftBuf[i++] = 0.0f;
                        }
                        Fourier.complexTransform(fftBuf, fullFFTsize, Fourier.INVERSE);
                        for (i = 2, j = complexFFTsize - 2; i < fullFFTsize; i += 2, j -= 2) {
                            fftBuf[i] += fftBuf[j];
                            fftBuf[i + 1] -= fftBuf[j + 1];
                        }
                        i++;
                        fftBuf[i] = -fftBuf[i];
                        i++;
                        while (i < complexFFTsize) fftBuf[i++] = 0.0f;
                        Fourier.complexTransform(fftBuf, fullFFTsize, Fourier.FORWARD);
                        for (i = 0; i <= fullFFTsize; i += 2) {
                            fftBuf[i] = (float) Math.exp(fftBuf[i]);
                        }
                    }
                    for (i = 0; i <= fullFFTsize; ) {
                        convBuf2[i] = convBuf1[i] * fftBuf[i];
                        i++;
                        convBuf2[i] = convBuf1[i] + fftBuf[i];
                        i++;
                    }
                }
                runInSlot.freeFrame(runInFr);
                for (boolean writeDone = false; (writeDone == false) && !threadDead; ) {
                    try {
                        runOutSlot.writeFrame(runOutFr);
                        writeDone = true;
                        runFrameDone(runOutSlot, runOutFr);
                        runOutStream.freeFrame(runOutFr);
                    } catch (InterruptedException e) {
                    }
                    runCheckPause();
                }
            }
            runInStream.closeReader();
            runOutStream.closeWriter();
        } catch (IOException e) {
            runQuit(e);
            return;
        } catch (SlotAlreadyConnectedException e) {
            runQuit(e);
            return;
        }
        runQuit(null);
    }

    public PropertyGUI createGUI(int type) {
        PropertyGUI gui;
        String[] winNames = Filter.getWindowNames();
        StringBuffer winChoise = new StringBuffer();
        if (type != GUI_PREFS) return null;
        for (int i = 0; i < winNames.length; i++) {
            winChoise.append(",it");
            winChoise.append(winNames[i]);
        }
        gui = new PropertyGUI("gl" + GroupLabel.NAME_GENERAL + "\n" + "lbLow frequency;pf" + Constants.absHzSpace + ",pr" + PRN_LOFREQ + "\n" + "lbHigh frequency;pf" + Constants.absHzSpace + ",pr" + PRN_HIFREQ + "\n" + "lbTarnish;ch,pr" + PRN_MODE + ",itOvertones,itAll Harmonics\n" + "lbWindow;ch,pr" + PRN_WINDOW + winChoise.toString() + "\n" + "lbOversampling;ch,pr" + PRN_OVERSMP + "," + "it1x (none)," + "it2x," + "it4x," + "it8x\n" + "cbMinimum phase,pr" + PRN_MINPHASE + "\n" + "cbResidual,pr" + PRN_RESIDUAL + "\n");
        return gui;
    }
}
