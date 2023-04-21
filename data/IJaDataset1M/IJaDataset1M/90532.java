package de.sciss.fscape.op;

import java.io.*;
import de.sciss.fscape.gui.*;
import de.sciss.fscape.prop.*;
import de.sciss.fscape.spect.*;
import de.sciss.fscape.util.*;

/**
 *	Operator zum Umkehren der Frequenzverteilung
 *
 *  @version	0.71, 14-Nov-07
 */
public class FlipFreqOp extends Operator {

    protected static final String defaultName = "Flip freq";

    protected static Presets static_presets = null;

    protected static Prefs static_prefs = null;

    protected static PropertyArray static_pr = null;

    protected static final int SLOT_INPUT = 0;

    protected static final int SLOT_OUTPUT = 1;

    private static final int PR_FLIPFREQ = 0;

    private static final int PR_MIDMOD = 1;

    private static final int PR_SHIFTMOD = 2;

    private static final int PR_HIMOD = 3;

    private static final int PR_LOMOD = 4;

    private static final int PR_MIDFREQ = 0;

    private static final int PR_SHIFTFREQ = 1;

    private static final int PR_HIFREQ = 2;

    private static final int PR_LOFREQ = 3;

    private static final int PR_MIDMODDEPTH = 4;

    private static final int PR_SHIFTMODDEPTH = 5;

    private static final int PR_HIMODDEPTH = 6;

    private static final int PR_LOMODDEPTH = 7;

    private static final int PR_MIDMODENV = 0;

    private static final int PR_SHIFTMODENV = 1;

    private static final int PR_HIMODENV = 2;

    private static final int PR_LOMODENV = 3;

    private static final String PRN_FLIPFREQ = "FlipFreq";

    private static final String PRN_MIDFREQ = "MidFreq";

    private static final String PRN_SHIFTFREQ = "ShiftFreq";

    private static final String PRN_HIFREQ = "HiFreq";

    private static final String PRN_LOFREQ = "LoFreq";

    private static final String PRN_MIDMOD = "MidMod";

    private static final String PRN_SHIFTMOD = "ShiftMod";

    private static final String PRN_HIMOD = "HiMod";

    private static final String PRN_LOMOD = "LoMod";

    private static final String PRN_MIDMODDEPTH = "MidModDepth";

    private static final String PRN_SHIFTMODDEPTH = "ShiftModDepth";

    private static final String PRN_HIMODDEPTH = "HiModDepth";

    private static final String PRN_LOMODDEPTH = "LoModDepth";

    private static final String PRN_MIDMODENV = "MidModEnv";

    private static final String PRN_SHIFTMODENV = "ShiftModEnv";

    private static final String PRN_HIMODENV = "HiModEnv";

    private static final String PRN_LOMODENV = "LoModEnv";

    private static final String PRN_CALCTYPE = "CalcType";

    private static final int PR_CALCTYPE_FREQRANGE = 1;

    private static final boolean prBool[] = { true, false, false, false, false };

    private static final String prBoolName[] = { PRN_FLIPFREQ, PRN_MIDMOD, PRN_SHIFTMOD, PRN_HIMOD, PRN_LOMOD };

    private static final int prIntg[] = { PR_CALCTYPE_FREQRANGE };

    private static final String prIntgName[] = { PRN_CALCTYPE };

    private static final Param prPara[] = { null, null, null, null, null, null, null, null };

    private static final String prParaName[] = { PRN_MIDFREQ, PRN_SHIFTFREQ, PRN_HIFREQ, PRN_LOFREQ, PRN_MIDMODDEPTH, PRN_SHIFTMODDEPTH, PRN_HIMODDEPTH, PRN_LOMODDEPTH };

    private static final Envelope prEnvl[] = { null, null, null, null };

    private static final String prEnvlName[] = { PRN_MIDMODENV, PRN_SHIFTMODENV, PRN_HIMODENV, PRN_LOMODENV };

    public FlipFreqOp() {
        super();
        if (static_prefs == null) {
            static_prefs = new OpPrefs(getClass(), getDefaultPrefs());
        }
        if (static_pr == null) {
            static_pr = new PropertyArray();
            static_pr.bool = prBool;
            static_pr.boolName = prBoolName;
            static_pr.intg = prIntg;
            static_pr.intgName = prIntgName;
            static_pr.para = prPara;
            static_pr.para[PR_MIDFREQ] = new Param(1760.0, Param.ABS_HZ);
            static_pr.para[PR_SHIFTFREQ] = new Param(0.0, Param.OFFSET_HZ);
            static_pr.para[PR_HIFREQ] = new Param(7040.0, Param.ABS_HZ);
            static_pr.para[PR_LOFREQ] = new Param(0.0, Param.ABS_HZ);
            static_pr.para[PR_MIDMODDEPTH] = new Param(12.0, Param.OFFSET_SEMITONES);
            static_pr.para[PR_SHIFTMODDEPTH] = new Param(12.0, Param.OFFSET_SEMITONES);
            static_pr.para[PR_HIMODDEPTH] = new Param(12.0, Param.OFFSET_SEMITONES);
            static_pr.para[PR_LOMODDEPTH] = new Param(12.0, Param.OFFSET_SEMITONES);
            static_pr.paraName = prParaName;
            static_pr.envl = prEnvl;
            static_pr.envl[PR_MIDMODENV] = Envelope.createBasicEnvelope(Envelope.BASIC_TIME);
            static_pr.envl[PR_SHIFTMODENV] = Envelope.createBasicEnvelope(Envelope.BASIC_TIME);
            static_pr.envl[PR_HIMODENV] = Envelope.createBasicEnvelope(Envelope.BASIC_TIME);
            static_pr.envl[PR_LOMODENV] = Envelope.createBasicEnvelope(Envelope.BASIC_TIME);
            static_pr.envlName = prEnvlName;
            static_pr.superPr = Operator.op_static_pr;
        }
        if (static_presets == null) {
            static_presets = new Presets(getClass(), static_pr.toProperties(true));
        }
        opName = "FlipFreqOp";
        prefs = static_prefs;
        presets = static_presets;
        pr = (PropertyArray) static_pr.clone();
        slots.addElement(new SpectStreamSlot(this, Slots.SLOTS_READER));
        slots.addElement(new SpectStreamSlot(this, Slots.SLOTS_WRITER));
        icon = new OpIcon(this, OpIcon.ID_FLIPFREQ, defaultName);
    }

    public void run() {
        runInit();
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        Param midBase, shiftBase, hiBase, loBase;
        int phaseFactor;
        boolean recalc = true;
        Param midFreq;
        Param shiftFreq;
        Param hiFreq;
        Param loFreq;
        Modulator midMod = null;
        Modulator shiftMod = null;
        Modulator hiMod = null;
        Modulator loMod = null;
        Param foo;
        double freqSpacing;
        double hiScaling;
        double loScaling;
        double loRange;
        double hiRange;
        double destFreq;
        double srcFreq;
        float srcBand;
        int srcBands[];
        float srcWeights[];
        int srcFloorBand;
        int srcCeilBand;
        float srcFloorWeight;
        float srcCeilWeight;
        float srcAmp, srcPhase;
        double destReal, destImg;
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
            srcBands = new int[runInStream.bands + 1];
            srcWeights = new float[runInStream.bands + 1];
            freqSpacing = (runInStream.hiFreq - runInStream.loFreq) / runInStream.bands;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutSlot.initWriter(runOutStream);
            if (pr.bool[PR_FLIPFREQ]) {
                phaseFactor = -1;
            } else {
                phaseFactor = +1;
            }
            midBase = Param.transform(pr.para[PR_MIDFREQ], Param.ABS_HZ, null, runInStream);
            shiftBase = Param.transform(pr.para[PR_SHIFTFREQ], Param.ABS_HZ, midBase, runInStream);
            hiBase = Param.transform(pr.para[PR_HIFREQ], Param.ABS_HZ, midBase, runInStream);
            loBase = Param.transform(pr.para[PR_LOFREQ], Param.ABS_HZ, midBase, runInStream);
            midFreq = midBase;
            shiftFreq = shiftBase;
            hiFreq = hiBase;
            loFreq = loBase;
            if (pr.bool[PR_MIDMOD]) {
                midMod = new Modulator(midBase, pr.para[PR_MIDMODDEPTH], pr.envl[PR_MIDMODENV], runInStream);
            }
            if (pr.bool[PR_SHIFTMOD]) {
                shiftMod = new Modulator(shiftBase, pr.para[PR_SHIFTMODDEPTH], pr.envl[PR_SHIFTMODENV], runInStream);
            }
            if (pr.bool[PR_HIMOD]) {
                hiMod = new Modulator(hiBase, pr.para[PR_HIMODDEPTH], pr.envl[PR_HIMODENV], runInStream);
            }
            if (pr.bool[PR_LOMOD]) {
                loMod = new Modulator(loBase, pr.para[PR_LOMODDEPTH], pr.envl[PR_LOMODENV], runInStream);
            }
            runSlotsReady();
            mainLoop: while (!threadDead) {
                if (pr.bool[PR_MIDMOD]) {
                    foo = midMod.calc();
                    if (Math.abs(foo.val - midFreq.val) >= 0.1) {
                        midFreq = foo;
                        recalc = true;
                    }
                }
                if (pr.bool[PR_SHIFTMOD]) {
                    foo = shiftMod.calc();
                    if (Math.abs(foo.val - shiftFreq.val) >= 0.1) {
                        shiftFreq = foo;
                        recalc = true;
                    }
                }
                if (pr.bool[PR_HIMOD]) {
                    foo = hiMod.calc();
                    if (Math.abs(foo.val - hiFreq.val) >= 0.1) {
                        hiFreq = foo;
                        recalc = true;
                    }
                }
                if (pr.bool[PR_LOMOD]) {
                    foo = loMod.calc();
                    if (Math.abs(foo.val - loFreq.val) >= 0.1) {
                        loFreq = foo;
                        recalc = true;
                    }
                }
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
                if (recalc) {
                    loRange = Math.max(1, midFreq.val - loFreq.val);
                    hiRange = Math.max(1, hiFreq.val - midFreq.val);
                    hiScaling = loRange / hiRange;
                    loScaling = hiRange / loRange;
                    if (pr.bool[PR_FLIPFREQ]) {
                        for (int band = 0; band <= runInStream.bands; band++) {
                            destFreq = band * freqSpacing + runOutStream.loFreq - shiftFreq.val;
                            if (destFreq >= midFreq.val) {
                                srcFreq = midFreq.val - (destFreq - midFreq.val) * hiScaling;
                            } else {
                                srcFreq = (midFreq.val - destFreq) * loScaling + midFreq.val;
                            }
                            srcBand = (float) ((srcFreq - runInStream.loFreq) / freqSpacing);
                            srcBands[band] = (int) Math.floor(srcBand);
                            srcWeights[band] = srcBand - srcBands[band];
                        }
                    } else {
                        for (int band = 0; band <= runInStream.bands; band++) {
                            destFreq = band * freqSpacing + runOutStream.loFreq + shiftFreq.val;
                            if (destFreq >= midFreq.val) {
                                srcFreq = (destFreq - midFreq.val) * loScaling + midFreq.val;
                            } else {
                                srcFreq = midFreq.val - (midFreq.val - destFreq) * hiScaling;
                            }
                            srcBand = (float) ((srcFreq - runInStream.loFreq) / freqSpacing);
                            srcBands[band] = (int) Math.floor(srcBand);
                            srcWeights[band] = srcBand - srcBands[band];
                        }
                    }
                    recalc = false;
                }
                bandLp: for (int band = 0; band < runInStream.bands; band++) {
                    srcFloorBand = srcBands[band];
                    srcCeilBand = srcBands[band + 1];
                    if (srcFloorBand > srcCeilBand) {
                        srcFloorBand = srcBands[band + 1];
                        srcCeilBand = srcBands[band];
                        srcFloorWeight = 1.0f - srcWeights[band + 1];
                        srcCeilWeight = srcWeights[band];
                    } else {
                        srcFloorWeight = 1.0f - srcWeights[band];
                        srcCeilWeight = srcWeights[band + 1];
                    }
                    if (srcFloorBand < 0) {
                        srcFloorBand = 0;
                        if (srcFloorBand < srcCeilBand) {
                            srcFloorWeight = 1.0f;
                        } else {
                            for (int ch = 0; ch < runInStream.chanNum; ch++) {
                                runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = 0.0f;
                                runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = 0.0f;
                            }
                            continue bandLp;
                        }
                    }
                    if (srcCeilBand >= runInStream.bands) {
                        srcCeilBand = runInStream.bands - 1;
                        if (srcCeilBand > srcFloorBand) {
                            srcCeilWeight = 1.0f;
                        } else {
                            for (int ch = 0; ch < runInStream.chanNum; ch++) {
                                runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = 0.0f;
                                runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = 0.0f;
                            }
                            continue bandLp;
                        }
                    }
                    if (srcFloorBand == srcCeilBand) {
                        srcFloorWeight = srcCeilWeight - (1.0f - srcFloorWeight);
                        srcCeilWeight = 0.0f;
                    }
                    for (int ch = 0; ch < runInStream.chanNum; ch++) {
                        srcAmp = runInFr.data[ch][(srcFloorBand << 1) + SpectFrame.AMP];
                        srcPhase = runInFr.data[ch][(srcFloorBand << 1) + SpectFrame.PHASE];
                        destImg = srcAmp * Math.sin(srcPhase) * srcFloorWeight;
                        destReal = srcAmp * Math.cos(srcPhase) * srcFloorWeight;
                        for (int i = srcFloorBand + 1; i < srcCeilBand; i++) {
                            srcAmp = runInFr.data[ch][(i << 1) + SpectFrame.AMP];
                            srcPhase = runInFr.data[ch][(i << 1) + SpectFrame.PHASE];
                            destImg += srcAmp * Math.sin(srcPhase);
                            destReal += srcAmp * Math.cos(srcPhase);
                        }
                        srcAmp = runInFr.data[ch][(srcCeilBand << 1) + SpectFrame.AMP];
                        srcPhase = runInFr.data[ch][(srcCeilBand << 1) + SpectFrame.PHASE];
                        destImg += srcAmp * Math.sin(srcPhase) * srcCeilWeight;
                        destReal += srcAmp * Math.cos(srcPhase) * srcCeilWeight;
                        runOutFr.data[ch][(band << 1) + SpectFrame.AMP] = (float) Math.sqrt(destImg * destImg + destReal * destReal);
                        runOutFr.data[ch][(band << 1) + SpectFrame.PHASE] = phaseFactor * (float) Math.atan2(destImg, destReal);
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
        if (type != GUI_PREFS) return null;
        gui = new PropertyGUI("gl" + GroupLabel.NAME_GENERAL + "\n" + "cbFreq bands upside-down,pr" + PRN_FLIPFREQ + "\n" + "lbMiddle frequency;pf" + Constants.absHzSpace + ",id1,pr" + PRN_MIDFREQ + "\n" + "lbPost freq shift;pf" + Constants.offsetFreqSpace + "|" + Constants.offsetHzSpace + "|" + Constants.offsetSemitonesSpace + ",re1,id2,pr" + PRN_SHIFTFREQ + "\n" + "glCalculation\n" + "lbScaling;ch,pr" + PRN_CALCTYPE + "," + "itby factor," + "itby freq range\n" + "lbHigh frequency;pf" + Constants.absHzSpace + "|" + Constants.offsetFreqSpace + "|" + Constants.offsetHzSpace + "|" + Constants.offsetSemitonesSpace + ",re1,id3,pr" + PRN_HIFREQ + "\n" + "lbLow frequency;pf" + Constants.absHzSpace + "|" + Constants.offsetFreqSpace + "|" + Constants.offsetHzSpace + "|" + Constants.offsetSemitonesSpace + ",re1,id4,pr" + PRN_LOFREQ + "\n" + "gl" + GroupLabel.NAME_MODULATION + "\n" + "cbMiddle freq,actrue|5|en|6|en,acfalse|5|di|6|di,pr" + PRN_MIDMOD + ";" + "pf" + Constants.offsetFreqSpace + "|" + Constants.offsetHzSpace + "|" + Constants.offsetSemitonesSpace + ",re1,id5,pr" + PRN_MIDMODDEPTH + ";en,id6,pr" + PRN_MIDMODENV + "\n" + "cbShift freq,actrue|7|en|8|en,acfalse|7|di|8|di,pr" + PRN_SHIFTMOD + ";" + "pf" + Constants.offsetFreqSpace + "|" + Constants.offsetHzSpace + "|" + Constants.offsetSemitonesSpace + ",re2,id7,pr" + PRN_SHIFTMODDEPTH + ";en,id8,pr" + PRN_SHIFTMODENV + "\n" + "cbHigh freq,actrue|9|en|10|en,acfalse|9|di|10|di,pr" + PRN_HIMOD + ";" + "pf" + Constants.offsetFreqSpace + "|" + Constants.offsetHzSpace + "|" + Constants.offsetSemitonesSpace + ",re3,id9,pr" + PRN_HIMODDEPTH + ";en,id10,pr" + PRN_HIMODENV + "\n" + "cbLow freq,actrue|11|en|12|en,acfalse|11|di|12|di,pr" + PRN_LOMOD + ";" + "pf" + Constants.offsetFreqSpace + "|" + Constants.offsetHzSpace + "|" + Constants.offsetSemitonesSpace + ",re4,id11,pr" + PRN_LOMODDEPTH + ";en,id12,pr" + PRN_LOMODENV);
        return gui;
    }
}
