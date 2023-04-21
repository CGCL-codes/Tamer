package de.sciss.fscape.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import de.sciss.fscape.io.*;
import de.sciss.fscape.prop.*;
import de.sciss.fscape.session.*;
import de.sciss.fscape.spect.*;
import de.sciss.fscape.util.*;
import de.sciss.io.AudioFile;
import de.sciss.io.AudioFileDescr;
import de.sciss.io.IOUtil;
import de.sciss.io.Marker;

/**
 *	Processing module for replacing occurrences of
 *	one sound by another. The idea is to correlate
 *	an input sound by an "icon" sound and whenever
 *	the correlation exceeds a threshold, a "replacement"
 *	sound is plotted to the output file. Never really worked.
 *
 *  @author		Hanns Holger Rutz
 *  @version	0.71, 14-Nov-07
 */
public class PearsonPlotDlg extends DocumentFrame {

    private static final int PR_INPUTFILE = 0;

    private static final int PR_PATTERNFILE = 1;

    private static final int PR_ICONFILE = 2;

    private static final int PR_OUTPUTFILE = 3;

    private static final int PR_OUTPUTTYPE = 0;

    private static final int PR_OUTPUTRES = 1;

    private static final int PR_GAINTYPE = 2;

    private static final int PR_TRIGSOURCE = 3;

    private static final int PR_TRIGSIGN = 4;

    private static final int PR_GAIN = 0;

    private static final int PR_TRIGTHRESH = 1;

    private static final int PR_TRIGINTERVAL = 2;

    private static final int PR_PLOTGAIN = 3;

    private static final int PR_PLOTCHANGAIN = 4;

    private static final int PR_PLOTQUANTAMOUNT = 5;

    private static final int PR_PLOTOFFSET = 6;

    private static final int PR_PLOTNUM = 7;

    private static final int PR_WRITEMARK = 0;

    private static final int PR_PLOTQUANT = 1;

    private static final int PR_PLOTMAX = 2;

    private static final int SRC_SUM = 0;

    private static final int SRC_AND = 2;

    private static final int SIGN_POS = 0;

    private static final int SIGN_NEG = 1;

    private static final String PRN_INPUTFILE = "InputFile";

    private static final String PRN_OUTPUTFILE = "OutputFile";

    private static final String PRN_PATTERNFILE = "PtrnFile";

    private static final String PRN_ICONFILE = "IconFile";

    private static final String PRN_OUTPUTTYPE = "OutputType";

    private static final String PRN_OUTPUTRES = "OutputReso";

    private static final String PRN_WRITEMARK = "WriteMark";

    private static final String PRN_TRIGTHRESH = "TrigTresh";

    private static final String PRN_TRIGINTERVAL = "TrigInterv";

    private static final String PRN_PLOTGAIN = "PltGain";

    private static final String PRN_PLOTCHANGAIN = "PltChGain";

    private static final String PRN_PLOTQUANT = "PltQuant";

    private static final String PRN_PLOTQUANTAMOUNT = "PltQuantAmt";

    private static final String PRN_PLOTOFFSET = "PltOffset";

    private static final String PRN_PLOTMAX = "PltMax";

    private static final String PRN_PLOTNUM = "PltNum";

    private static final String PRN_TRIGSOURCE = "TrigSrc";

    private static final String PRN_TRIGSIGN = "TrigSign";

    private static final String prText[] = { "", "", "", "" };

    private static final String prTextName[] = { PRN_INPUTFILE, PRN_PATTERNFILE, PRN_ICONFILE, PRN_OUTPUTFILE };

    private static final int prIntg[] = { 0, 0, GAIN_UNITY, SRC_SUM, SIGN_POS };

    private static final String prIntgName[] = { PRN_OUTPUTTYPE, PRN_OUTPUTRES, PRN_GAINTYPE, PRN_TRIGSOURCE, PRN_TRIGSIGN };

    private static final boolean prBool[] = { true, false, false };

    private static final String prBoolName[] = { PRN_WRITEMARK, PRN_PLOTQUANT, PRN_PLOTMAX };

    private static final Param prPara[] = { null, null, null, null, null, null, null, null };

    private static final String prParaName[] = { PRN_GAIN, PRN_TRIGTHRESH, PRN_TRIGINTERVAL, PRN_PLOTGAIN, PRN_PLOTCHANGAIN, PRN_PLOTQUANTAMOUNT, PRN_PLOTOFFSET, PRN_PLOTNUM };

    private static final int GG_INPUTFILE = GG_OFF_PATHFIELD + PR_INPUTFILE;

    private static final int GG_OUTPUTFILE = GG_OFF_PATHFIELD + PR_OUTPUTFILE;

    private static final int GG_PATTERNFILE = GG_OFF_PATHFIELD + PR_PATTERNFILE;

    private static final int GG_ICONFILE = GG_OFF_PATHFIELD + PR_ICONFILE;

    private static final int GG_OUTPUTTYPE = GG_OFF_CHOICE + PR_OUTPUTTYPE;

    private static final int GG_OUTPUTRES = GG_OFF_CHOICE + PR_OUTPUTRES;

    private static final int GG_GAINTYPE = GG_OFF_CHOICE + PR_GAINTYPE;

    private static final int GG_TRIGSOURCE = GG_OFF_CHOICE + PR_TRIGSOURCE;

    private static final int GG_TRIGSIGN = GG_OFF_CHOICE + PR_TRIGSIGN;

    private static final int GG_GAIN = GG_OFF_PARAMFIELD + PR_GAIN;

    private static final int GG_TRIGTHRESH = GG_OFF_PARAMFIELD + PR_TRIGTHRESH;

    private static final int GG_TRIGINTERVAL = GG_OFF_PARAMFIELD + PR_TRIGINTERVAL;

    private static final int GG_PLOTGAIN = GG_OFF_PARAMFIELD + PR_PLOTGAIN;

    private static final int GG_PLOTCHANGAIN = GG_OFF_PARAMFIELD + PR_PLOTCHANGAIN;

    private static final int GG_PLOTQUANTAMOUNT = GG_OFF_PARAMFIELD + PR_PLOTQUANTAMOUNT;

    private static final int GG_PLOTOFFSET = GG_OFF_PARAMFIELD + PR_PLOTOFFSET;

    private static final int GG_PLOTNUM = GG_OFF_PARAMFIELD + PR_PLOTNUM;

    private static final int GG_WRITEMARK = GG_OFF_CHECKBOX + PR_WRITEMARK;

    private static final int GG_PLOTQUANT = GG_OFF_CHECKBOX + PR_PLOTQUANT;

    private static final int GG_PLOTMAX = GG_OFF_CHECKBOX + PR_PLOTMAX;

    private static PropertyArray static_pr = null;

    private static Presets static_presets = null;

    private JLabel lbTrigs;

    private static final String ERR_CHANNELS = "Input + pattern must share\nsame # of channels!\n(or use sum source)";

    private static final String ERR_TOOSMALL = "Input cannot be shorter than pattern";

    private static final String ERR_SILENCE = "Pattern contains pure silence";

    private static final String MARK_TRIG = "Trig";

    /**
	 *	!! setVisible() bleibt dem Aufrufer ueberlassen
	 */
    public PearsonPlotDlg() {
        super("Pearson Plotter");
        init2();
    }

    protected void buildGUI() {
        if (static_pr == null) {
            static_pr = new PropertyArray();
            static_pr.text = prText;
            static_pr.textName = prTextName;
            static_pr.intg = prIntg;
            static_pr.intgName = prIntgName;
            static_pr.bool = prBool;
            static_pr.boolName = prBoolName;
            static_pr.para = prPara;
            static_pr.para[PR_TRIGTHRESH] = new Param(50.0, Param.FACTOR_AMP);
            static_pr.para[PR_TRIGINTERVAL] = new Param(50.0, Param.ABS_MS);
            static_pr.para[PR_PLOTGAIN] = new Param(50.0, Param.FACTOR_AMP);
            static_pr.para[PR_PLOTCHANGAIN] = new Param(50.0, Param.FACTOR_AMP);
            static_pr.para[PR_PLOTQUANTAMOUNT] = new Param(1.0, Param.ABS_BEATS);
            static_pr.para[PR_PLOTOFFSET] = new Param(0.0, Param.ABS_MS);
            static_pr.para[PR_PLOTNUM] = new Param(10.0, Param.NONE);
            static_pr.paraName = prParaName;
            fillDefaultAudioDescr(static_pr.intg, PR_OUTPUTTYPE, PR_OUTPUTRES);
            fillDefaultGain(static_pr.para, PR_GAIN);
            static_presets = new Presets(getClass(), static_pr.toProperties(true));
        }
        presets = static_presets;
        pr = (PropertyArray) static_pr.clone();
        GridBagConstraints con;
        PathField ggInputFile, ggOutputFile, ggPtrnFile, ggIconFile;
        PathField[] ggInputs;
        JCheckBox ggWriteMark, ggPltQuant, ggPltMax;
        JComboBox ggTrigSrc, ggTrigSign;
        Component[] ggGain;
        ParamField ggTrigThresh, ggTrigInterval, ggPltGain, ggPltChGain, ggPltOffset, ggPltQuantAmt, ggPlotNum;
        ParamSpace[] spcInterval, spcOffset;
        gui = new GUISupport();
        con = gui.getGridBagConstraints();
        con.insets = new Insets(1, 2, 1, 2);
        ItemListener il = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                int ID = gui.getItemID(e);
                switch(ID) {
                    case GG_PLOTQUANT:
                    case GG_PLOTMAX:
                        pr.bool[ID - GG_OFF_CHECKBOX] = ((JCheckBox) e.getSource()).isSelected();
                        reflectPropertyChanges();
                        break;
                    case GG_TRIGSOURCE:
                        pr.intg[ID - GG_OFF_CHOICE] = ((JComboBox) e.getSource()).getSelectedIndex();
                        reflectPropertyChanges();
                        break;
                }
            }
        };
        con.fill = GridBagConstraints.BOTH;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addLabel(new GroupLabel("Waveform I/O", GroupLabel.ORIENT_HORIZONTAL, GroupLabel.BRACE_NONE));
        ggInputFile = new PathField(PathField.TYPE_INPUTFILE + PathField.TYPE_FORMATFIELD, "Select input file");
        ggInputFile.handleTypes(GenericFile.TYPES_SOUND);
        con.gridwidth = 1;
        con.weightx = 0.1;
        gui.addLabel(new JLabel("Control input", SwingConstants.RIGHT));
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.weightx = 0.9;
        gui.addPathField(ggInputFile, GG_INPUTFILE, null);
        ggPtrnFile = new PathField(PathField.TYPE_INPUTFILE + PathField.TYPE_FORMATFIELD, "Select matching pattern file");
        ggPtrnFile.handleTypes(GenericFile.TYPES_SOUND);
        con.gridwidth = 1;
        con.weightx = 0.1;
        gui.addLabel(new JLabel("Pattern input", SwingConstants.RIGHT));
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.weightx = 0.9;
        gui.addPathField(ggPtrnFile, GG_PATTERNFILE, null);
        ggIconFile = new PathField(PathField.TYPE_INPUTFILE + PathField.TYPE_FORMATFIELD, "Select plot source file");
        ggIconFile.handleTypes(GenericFile.TYPES_SOUND);
        con.gridwidth = 1;
        con.weightx = 0.1;
        gui.addLabel(new JLabel("Icon input", SwingConstants.RIGHT));
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.weightx = 0.9;
        gui.addPathField(ggIconFile, GG_ICONFILE, null);
        ggOutputFile = new PathField(PathField.TYPE_OUTPUTFILE + PathField.TYPE_FORMATFIELD + PathField.TYPE_RESFIELD, "Select output file");
        ggOutputFile.handleTypes(GenericFile.TYPES_SOUND);
        ggInputs = new PathField[2];
        ggInputs[0] = ggInputFile;
        ggInputs[1] = ggIconFile;
        ggOutputFile.deriveFrom(ggInputs, "$D0$B0Plot$B1$E");
        con.gridwidth = 1;
        con.weightx = 0.1;
        gui.addLabel(new JLabel("Plot output", SwingConstants.RIGHT));
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.weightx = 0.9;
        gui.addPathField(ggOutputFile, GG_OUTPUTFILE, null);
        gui.registerGadget(ggOutputFile.getTypeGadget(), GG_OUTPUTTYPE);
        gui.registerGadget(ggOutputFile.getResGadget(), GG_OUTPUTRES);
        ggGain = createGadgets(GGTYPE_GAIN);
        con.weightx = 0.1;
        con.gridwidth = 1;
        gui.addLabel(new JLabel("Gain", SwingConstants.RIGHT));
        con.weightx = 0.4;
        gui.addParamField((ParamField) ggGain[0], GG_GAIN, null);
        con.weightx = 0.5;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addChoice((JComboBox) ggGain[1], GG_GAINTYPE, il);
        gui.addLabel(new GroupLabel("Trigger settings", GroupLabel.ORIENT_HORIZONTAL, GroupLabel.BRACE_NONE));
        ggTrigSrc = new JComboBox();
        ggTrigSrc.addItem("Sum");
        ggTrigSrc.addItem("Parallel (OR)");
        ggTrigSrc.addItem("Serial (AND)");
        con.weightx = 0.1;
        con.gridwidth = 1;
        gui.addLabel(new JLabel("Source channels", SwingConstants.RIGHT));
        con.weightx = 0.4;
        gui.addChoice(ggTrigSrc, GG_TRIGSOURCE, il);
        ggTrigThresh = new ParamField(Constants.spaces[Constants.ratioAmpSpace]);
        con.weightx = 0.1;
        gui.addLabel(new JLabel("Threshold", SwingConstants.RIGHT));
        con.weightx = 0.4;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addParamField(ggTrigThresh, GG_TRIGTHRESH, null);
        ggTrigSign = new JComboBox();
        ggTrigSign.addItem("Pos.(in phase)");
        ggTrigSign.addItem("Neg.(antiphase)");
        ggTrigSign.addItem("Both");
        con.weightx = 0.1;
        con.gridwidth = 1;
        gui.addLabel(new JLabel("Thresh sign", SwingConstants.RIGHT));
        con.weightx = 0.4;
        gui.addChoice(ggTrigSign, GG_TRIGSIGN, il);
        spcInterval = new ParamSpace[2];
        spcInterval[0] = Constants.spaces[Constants.absMsSpace];
        spcInterval[1] = Constants.spaces[Constants.absBeatsSpace];
        ggTrigInterval = new ParamField(spcInterval);
        con.weightx = 0.1;
        gui.addLabel(new JLabel("Min.interval", SwingConstants.RIGHT));
        con.weightx = 0.4;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addParamField(ggTrigInterval, GG_TRIGINTERVAL, null);
        gui.addLabel(new GroupLabel("Plotter settings", GroupLabel.ORIENT_HORIZONTAL, GroupLabel.BRACE_NONE));
        ggPltGain = new ParamField(Constants.spaces[Constants.ratioAmpSpace]);
        con.weightx = 0.1;
        con.gridwidth = 1;
        gui.addLabel(new JLabel("Variable gain", SwingConstants.RIGHT));
        con.weightx = 0.4;
        gui.addParamField(ggPltGain, GG_PLOTGAIN, null);
        spcOffset = new ParamSpace[2];
        spcOffset[0] = Constants.spaces[Constants.offsetMsSpace];
        spcOffset[1] = Constants.spaces[Constants.offsetBeatsSpace];
        ggPltOffset = new ParamField(spcOffset);
        con.weightx = 0.1;
        gui.addLabel(new JLabel("Offset", SwingConstants.RIGHT));
        con.weightx = 0.4;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addParamField(ggPltOffset, GG_PLOTOFFSET, null);
        ggPltChGain = new ParamField(Constants.spaces[Constants.ratioAmpSpace]);
        con.weightx = 0.1;
        con.gridwidth = 1;
        gui.addLabel(new JLabel("Channel gain", SwingConstants.RIGHT));
        con.weightx = 0.4;
        gui.addParamField(ggPltChGain, GG_PLOTCHANGAIN, null);
        ggPltQuant = new JCheckBox("Quantisize");
        con.weightx = 0.1;
        gui.addCheckbox(ggPltQuant, GG_PLOTQUANT, il);
        ggPltQuantAmt = new ParamField(spcInterval);
        con.weightx = 0.4;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addParamField(ggPltQuantAmt, GG_PLOTQUANTAMOUNT, null);
        ggWriteMark = new JCheckBox();
        con.weightx = 0.1;
        con.gridwidth = 1;
        gui.addLabel(new JLabel("Write markers", SwingConstants.RIGHT));
        con.weightx = 0.4;
        gui.addCheckbox(ggWriteMark, GG_WRITEMARK, il);
        ggPltMax = new JCheckBox("Limit #plots");
        con.weightx = 0.1;
        gui.addCheckbox(ggPltMax, GG_PLOTMAX, il);
        ggPlotNum = new ParamField(new ParamSpace(1.0, 10000.0, 1.0, Param.NONE));
        con.weightx = 0.4;
        con.gridwidth = GridBagConstraints.REMAINDER;
        gui.addParamField(ggPlotNum, GG_PLOTNUM, null);
        gui.addLabel(new GroupLabel("Trigger View", GroupLabel.ORIENT_HORIZONTAL, GroupLabel.BRACE_NONE));
        lbTrigs = new JLabel(" ");
        lbTrigs.setBackground(Color.white);
        gui.addLabel(lbTrigs);
        initGUI(this, FLAGS_PRESETS | FLAGS_PROGBAR, gui);
    }

    /**
	 *	Werte aus Prop-Array in GUI uebertragen
	 */
    public void fillGUI() {
        super.fillGUI();
        super.fillGUI(gui);
    }

    /**
	 *	Werte aus GUI in Prop-Array uebertragen
	 */
    public void fillPropertyArray() {
        super.fillPropertyArray();
        super.fillPropertyArray(gui);
    }

    protected void process() {
        int i, j, k, m, n, off, ch, len;
        long progOff, progLen;
        float f1, f2;
        double d1;
        boolean b1;
        AudioFile inF = null;
        AudioFile outF = null;
        AudioFile ptrnF = null;
        AudioFile iconF = null;
        AudioFileDescr inStream = null;
        AudioFileDescr outStream = null;
        AudioFileDescr ptrnStream = null;
        AudioFileDescr iconStream = null;
        FloatFile[] outFloatF = null;
        File outTempFile[] = null;
        int inChanNum, outChanNum, ptrnChanNum, iconChanNum, trigChanNum;
        float[][] inBuf, chunkBuf, ptrnBuf, overBuf;
        float[] convBuf1, convBuf2;
        int inOff, outOff, chunkLength;
        int inLength, outLength, ptrnLength, iconLength;
        int frameSize, overOff, overLength, fftLength;
        int framesRead, framesWritten, framesWritten2;
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        float gain = 1.0f;
        float maxAmp = 0.0f;
        boolean sumChannels = pr.intg[PR_TRIGSOURCE] == SRC_SUM;
        float trigThresh, trigPos, trigNeg;
        int trigCount, trigInterval, lastTrig;
        int plotQuant, plotIdx, plotIdx2, plotTableLen, plotEntryLen, plotOff;
        int plotNum = 0;
        int[] plotTable = null;
        byte[] byteBuf = null;
        RandomAccessFile plotF = null;
        File plotTempFile = null;
        float varGain, chanGain;
        int iconMem, pass, passes, passLen;
        PathField ggOutput;
        Graphics g = null;
        Dimension gDim;
        java.util.List markers;
        topLevel: try {
            inF = AudioFile.openAsRead(new File(pr.text[PR_INPUTFILE]));
            inStream = inF.getDescr();
            inChanNum = inStream.channels;
            inLength = (int) inStream.length;
            if ((inLength < 1) || (inChanNum < 1)) throw new EOFException(ERR_EMPTY);
            if (!threadRunning) break topLevel;
            ptrnF = AudioFile.openAsRead(new File(pr.text[PR_PATTERNFILE]));
            ptrnStream = ptrnF.getDescr();
            ptrnChanNum = ptrnStream.channels;
            ptrnLength = (int) ptrnStream.length;
            if ((ptrnLength < 1) || (ptrnChanNum < 1)) throw new EOFException(ERR_EMPTY);
            if (!threadRunning) break topLevel;
            if ((inChanNum != ptrnChanNum) && !sumChannels) {
                throw new IOException(ERR_CHANNELS);
            }
            if (inLength < ptrnLength) throw new IOException(ERR_TOOSMALL);
            iconF = AudioFile.openAsRead(new File(pr.text[PR_ICONFILE]));
            iconStream = iconF.getDescr();
            iconChanNum = iconStream.channels;
            iconLength = (int) iconStream.length;
            if ((iconLength < 1) || (iconChanNum < 1)) throw new EOFException(ERR_EMPTY);
            if (!threadRunning) break topLevel;
            trigChanNum = sumChannels ? 1 : inChanNum;
            outChanNum = Math.max(trigChanNum, iconChanNum);
            ggOutput = (PathField) gui.getItemObj(GG_OUTPUTFILE);
            if (ggOutput == null) throw new IOException(ERR_MISSINGPROP);
            IOUtil.createEmptyFile(new File(pr.text[PR_OUTPUTFILE]));
            outStream = new AudioFileDescr(inStream);
            ggOutput.fillStream(outStream);
            outStream.channels = outChanNum;
            markers = (java.util.List) outStream.getProperty(AudioFileDescr.KEY_MARKERS);
            if (markers == null && pr.bool[PR_WRITEMARK]) {
                markers = new Vector();
                outStream.setProperty(AudioFileDescr.KEY_MARKERS, markers);
            }
            if (!threadRunning) break topLevel;
            g = lbTrigs.getGraphics();
            gDim = lbTrigs.getSize();
            if (g != null) {
                lbTrigs.repaint();
                g.setColor(Color.red);
            }
            trigThresh = (float) (Param.transform(pr.para[PR_TRIGTHRESH], Param.ABS_AMP, ampRef, null)).val;
            trigInterval = (int) (AudioFileDescr.millisToSamples(inStream, pr.para[PR_TRIGINTERVAL].val) + 0.5);
            trigPos = trigThresh;
            trigNeg = -trigThresh;
            switch(pr.intg[PR_TRIGSIGN]) {
                case SIGN_POS:
                    trigNeg = Float.NEGATIVE_INFINITY;
                    break;
                case SIGN_NEG:
                    trigPos = Float.POSITIVE_INFINITY;
                    break;
            }
            plotQuant = pr.bool[PR_PLOTQUANT] ? (int) (AudioFileDescr.millisToSamples(inStream, (Param.transform(pr.para[PR_PLOTQUANTAMOUNT], Param.ABS_MS, new Param(0.0, Param.ABS_MS), null)).val) + 0.5) : 1;
            plotOff = (int) (AudioFileDescr.millisToSamples(inStream, (Param.transform(pr.para[PR_PLOTOFFSET], Param.ABS_MS, new Param(0.0, Param.ABS_MS), null)).val) + 0.5);
            for (i = 2 * ptrnLength - 1, fftLength = 2; fftLength < i; fftLength <<= 1) ;
            frameSize = fftLength - ptrnLength + 1;
            overOff = frameSize - 2;
            overLength = fftLength - overOff;
            inBuf = new float[Math.max(Math.max(inChanNum, ptrnChanNum), iconChanNum)][8192];
            ptrnBuf = new float[sumChannels ? 1 : ptrnChanNum][fftLength + 2];
            chunkBuf = new float[trigChanNum][fftLength + 2];
            overBuf = new float[trigChanNum][overLength];
            Util.clear(ptrnBuf);
            Util.clear(chunkBuf);
            plotEntryLen = (outChanNum + 1);
            plotTableLen = plotEntryLen * 256;
            plotTable = new int[plotTableLen];
            byteBuf = new byte[256];
            plotIdx = 0;
            plotTempFile = IOUtil.createTempFile();
            plotF = new RandomAccessFile(plotTempFile, "rw");
            varGain = (float) (Param.transform(pr.para[PR_PLOTGAIN], Param.ABS_AMP, ampRef, null)).val;
            chanGain = (float) (Param.transform(pr.para[PR_PLOTCHANGAIN], Param.ABS_AMP, ampRef, null)).val;
            progOff = 0;
            progLen = (long) ptrnLength * (1 + (sumChannels ? 1 : ptrnChanNum)) + (long) inLength * (1 + trigChanNum);
            progLen *= 2;
            outTempFile = new File[outChanNum];
            outFloatF = new FloatFile[outChanNum];
            for (ch = 0; ch < outChanNum; ch++) {
                outTempFile[ch] = null;
                outFloatF[ch] = null;
            }
            for (ch = 0; ch < outChanNum; ch++) {
                outTempFile[ch] = IOUtil.createTempFile();
                outFloatF[ch] = new FloatFile(outTempFile[ch], GenericFile.MODE_OUTPUT);
            }
            if (!threadRunning) break topLevel;
            for (framesRead = 0; threadRunning && (framesRead < ptrnLength); ) {
                len = Math.min(ptrnLength - framesRead, 8192);
                ptrnF.readFrames(inBuf, 0, len);
                if (sumChannels && (ptrnChanNum > 1)) {
                    convBuf2 = ptrnBuf[0];
                    for (ch = 0; ch < ptrnChanNum; ch++) {
                        convBuf1 = inBuf[ch];
                        for (i = 0, j = ptrnLength - framesRead; i < len; ) {
                            convBuf2[--j] += convBuf1[i++];
                        }
                    }
                } else {
                    for (ch = 0; ch < ptrnChanNum; ch++) {
                        convBuf1 = inBuf[ch];
                        convBuf2 = ptrnBuf[ch];
                        for (i = 0, j = ptrnLength - framesRead; i < len; ) {
                            convBuf2[--j] = convBuf1[i++];
                        }
                    }
                }
                framesRead += len;
                progOff += len;
                setProgression((float) progOff / (float) progLen);
            }
            if (!threadRunning) break topLevel;
            if (sumChannels) ptrnChanNum = 1;
            for (ch = 0; threadRunning && (ch < ptrnChanNum); ch++) {
                convBuf1 = ptrnBuf[ch];
                for (j = 0, d1 = 0.0; j < ptrnLength; ) {
                    d1 += convBuf1[j++];
                }
                f1 = (float) (-d1 / ptrnLength);
                for (j = 0, d1 = 0.0; j < ptrnLength; ) {
                    convBuf1[j++] += f1;
                }
                d1 = Math.sqrt(Filter.calcEnergy(convBuf1, 0, ptrnLength));
                if (d1 > 0.0) {
                    Util.mult(convBuf1, 0, ptrnLength, (float) (1.0 / d1));
                } else throw new IOException(ERR_SILENCE);
                Fourier.realTransform(convBuf1, fftLength, Fourier.FORWARD);
                progOff += ptrnLength;
                setProgression((float) progOff / (float) progLen);
            }
            if (!threadRunning) break topLevel;
            ptrnF.close();
            ptrnF = null;
            ptrnStream = null;
            inOff = 1;
            framesRead = 0;
            framesWritten = 0;
            lastTrig = -trigInterval;
            while (threadRunning && (framesRead < inLength)) {
                for (off = inOff, chunkLength = Math.min(inLength - framesRead + inOff, frameSize); threadRunning && (off < chunkLength); ) {
                    len = Math.min(8192, chunkLength - off);
                    if (sumChannels && (inChanNum > 1)) {
                        inF.readFrames(inBuf, 0, len);
                        System.arraycopy(inBuf[0], 0, chunkBuf[0], off, len);
                        for (ch = 1; ch < inChanNum; ch++) {
                            Util.add(inBuf[ch], 0, chunkBuf[0], off, len);
                        }
                    } else {
                        inF.readFrames(chunkBuf, off, len);
                    }
                    framesRead += len;
                    off += len;
                    progOff += len;
                    setProgression((float) progOff / (float) progLen);
                }
                if (!threadRunning) break topLevel;
                for (ch = 0; ch < trigChanNum; ch++) {
                    convBuf1 = chunkBuf[ch];
                    for (i = 0; i < inOff; ) {
                        convBuf1[i++] = 0.0f;
                    }
                    for (i = chunkLength; i < fftLength; ) {
                        convBuf1[i++] = 0.0f;
                    }
                }
                for (ch = 0; threadRunning && (ch < trigChanNum); ch++) {
                    Fourier.realTransform(chunkBuf[ch], fftLength, Fourier.FORWARD);
                    Fourier.complexMult(ptrnBuf[ch % ptrnChanNum], 0, chunkBuf[ch], 0, chunkBuf[ch], 0, fftLength + 2);
                    Fourier.realTransform(chunkBuf[ch], fftLength, Fourier.INVERSE);
                    Util.add(overBuf[ch], 0, chunkBuf[ch], 0, overLength);
                    System.arraycopy(chunkBuf[ch], frameSize - 2, overBuf[ch], 0, overLength);
                    progOff += chunkLength;
                    setProgression((float) progOff / (float) progLen);
                }
                if (!threadRunning) break topLevel;
                for (ch = 0, trigCount = 0; ch < trigChanNum; ch++) {
                    convBuf1 = chunkBuf[ch];
                    trigLp: for (i = 1; i < chunkLength - 1; i++) {
                        f1 = convBuf1[i];
                        f2 = f1 - convBuf1[i - 1];
                        if (f2 > 0.0f) {
                            f2 = convBuf1[i + 1] - f1;
                            if ((f2 < 0.0f) && (f1 > trigPos)) {
                                trigCount++;
                                continue trigLp;
                            }
                        } else if (f2 < 0.0f) {
                            f2 = convBuf1[i + 1] - f1;
                            if ((f2 > 0.0f) && (f1 < trigNeg)) {
                                trigCount++;
                                continue trigLp;
                            }
                        }
                        convBuf1[i] = 0.0f;
                    }
                }
                if (trigCount > 0) {
                    switch(pr.intg[PR_TRIGSOURCE]) {
                        case SRC_AND:
                            andLp: for (i = 1; i < chunkLength - 1; i++) {
                                for (ch = 0; ch < trigChanNum; ch++) {
                                    if (chunkBuf[ch][i] == 0.0f) continue andLp;
                                }
                                j = framesWritten + i - 1;
                                if ((j - lastTrig) >= trigInterval) {
                                    lastTrig = j;
                                    if (pr.bool[PR_WRITEMARK]) {
                                        markers.add(new Marker(j, MARK_TRIG));
                                    }
                                    if (g != null) {
                                        k = (int) (((float) j / (float) inLength) * gDim.width + 0.5f);
                                        g.drawLine(k, 0, k, gDim.height - 1);
                                    }
                                    j += plotQuant >> 1;
                                    j += plotOff - (j % plotQuant);
                                    for (ch = 0, f1 = 0.0f; ch < trigChanNum; ch++) {
                                        f1 += chunkBuf[ch][i];
                                    }
                                    f1 /= trigChanNum;
                                    plotTable[plotIdx++] = j;
                                    for (ch = 0; ch < outChanNum; ch++) {
                                        f2 = 1.0f + varGain * ((f1 + chanGain * (chunkBuf[ch % trigChanNum][i] - f1)) - 1.0f);
                                        plotTable[plotIdx++] = Float.floatToIntBits(f2);
                                    }
                                    if (plotIdx == plotTableLen) {
                                        writeBytes(plotF, plotTable, 0, plotTableLen, byteBuf);
                                        plotNum += plotIdx;
                                        plotIdx = 0;
                                    }
                                }
                            }
                            break;
                        default:
                            orLp: for (i = 1; i < chunkLength - 1; i++) {
                                for (ch = 0; ch < trigChanNum; ch++) {
                                    if (chunkBuf[ch][i] != 0.0f) {
                                        j = framesWritten + i - 1;
                                        if ((j - lastTrig) >= trigInterval) {
                                            lastTrig = j;
                                            if (pr.bool[PR_WRITEMARK]) {
                                                markers.add(new Marker(j, MARK_TRIG));
                                            }
                                            if (g != null) {
                                                k = (int) (((float) j / (float) inLength) * gDim.width + 0.5f);
                                                g.drawLine(k, 0, k, gDim.height - 1);
                                            }
                                            j += plotQuant >> 1;
                                            j += plotOff - (j % plotQuant);
                                            for (ch = 0, f1 = 0.0f; ch < trigChanNum; ch++) {
                                                f1 += chunkBuf[ch][i];
                                            }
                                            f1 /= trigChanNum;
                                            plotTable[plotIdx++] = j;
                                            for (ch = 0; ch < outChanNum; ch++) {
                                                f2 = 1.0f + varGain * ((f1 + chanGain * (chunkBuf[ch % trigChanNum][i] - f1)) - 1.0f);
                                                plotTable[plotIdx++] = Float.floatToIntBits(f2);
                                            }
                                            if (plotIdx == plotTableLen) {
                                                writeBytes(plotF, plotTable, 0, plotTableLen, byteBuf);
                                                plotNum += plotIdx;
                                                plotIdx = 0;
                                            }
                                        }
                                        continue orLp;
                                    }
                                }
                            }
                            break;
                    }
                }
                framesWritten = framesRead - 1;
                inOff = 2;
            }
            if (!threadRunning) break topLevel;
            inF.close();
            inF = null;
            inStream = null;
            ptrnBuf = null;
            chunkBuf = null;
            if (g != null) {
                g.dispose();
                g = null;
            }
            writeBytes(plotF, plotTable, 0, plotIdx, byteBuf);
            plotF.seek(0L);
            plotNum += plotIdx;
            plotNum /= plotEntryLen;
            outF = AudioFile.openAsWrite(outStream);
            if (plotNum > 0) {
                System.gc();
                plotTableLen = plotNum * plotEntryLen;
                plotTable = new int[plotTableLen];
                for (off = 0; threadRunning && (off < plotTableLen); ) {
                    len = Math.min(plotTable.length - off, 8192);
                    readBytes(plotF, plotTable, off, len, byteBuf);
                    off += len;
                    setProgression(getProgression());
                }
                if (!threadRunning) break topLevel;
                iconMem = (int) ((Runtime.getRuntime().freeMemory() >> 4) * 3 / iconChanNum);
                if (iconMem >= iconLength) {
                    iconMem = iconLength;
                } else if (iconMem < 1024) throw new OutOfMemoryError(ERR_MEMORY);
                ptrnBuf = new float[iconChanNum][iconMem];
                passes = (iconLength - 1) / iconMem;
                outLength = plotTable[plotTableLen - plotEntryLen] + iconLength;
                outOff = plotTable[0];
                progLen = (long) iconLength + (long) outLength + outOff;
                for (pass = 0, inOff = 0; pass <= passes; pass++, inOff += iconMem) {
                    framesWritten = outOff + inOff;
                    progLen += outLength - framesWritten;
                }
                progOff = progLen;
                progLen *= 2;
                framesWritten2 = 0;
                if (outOff > 0) {
                    Util.clear(inBuf);
                    while (threadRunning && (framesWritten2 < outOff)) {
                        len = Math.min(8192, outOff - framesWritten2);
                        for (ch = 0; ch < outChanNum; ch++) {
                            outFloatF[ch].writeFloats(inBuf[0], 0, len);
                        }
                        framesWritten2 += len;
                        progOff += len;
                        setProgression((float) progOff / (float) progLen);
                    }
                    if (!threadRunning) break topLevel;
                }
                for (pass = 0, inOff = 0; threadRunning && (pass <= passes); pass++, inOff += iconMem) {
                    framesWritten = outOff + inOff;
                    iconF.seekFrame(inOff);
                    passLen = Math.min(iconLength - inOff, iconMem);
                    for (off = 0; threadRunning && (off < passLen); ) {
                        len = Math.min(8192, passLen - off);
                        iconF.readFrames(ptrnBuf, off, len);
                        off += len;
                        progOff += len;
                        setProgression((float) progOff / (float) progLen);
                    }
                    if (!threadRunning) break topLevel;
                    chunkLength = Math.min(8192, outLength - framesWritten);
                    for (plotIdx = 0; plotIdx < plotTable.length; plotIdx += plotEntryLen) {
                        if (plotTable[plotIdx] + inOff + passLen > framesWritten) break;
                    }
                    for (plotIdx2 = plotIdx; plotIdx2 < plotTable.length; plotIdx2 += plotEntryLen) {
                        if (plotTable[plotIdx2] >= framesWritten + chunkLength) break;
                    }
                    while (threadRunning && (framesWritten < outLength)) {
                        chunkLength = Math.min(8192, outLength - framesWritten);
                        for (; plotIdx < plotTable.length; plotIdx += plotEntryLen) {
                            if (plotTable[plotIdx] + inOff + passLen > framesWritten) break;
                        }
                        for (; plotIdx2 < plotTable.length; plotIdx2 += plotEntryLen) {
                            if (plotTable[plotIdx2] >= framesWritten + chunkLength) break;
                        }
                        b1 = (plotIdx2 > plotIdx) || (pass == passes);
                        if (b1) {
                            i = Math.min(chunkLength, Math.max(0, framesWritten2 - framesWritten));
                            for (ch = 0; ch < outChanNum; ch++) {
                                convBuf1 = inBuf[ch];
                                outFloatF[ch].seekFloat(framesWritten);
                                outFloatF[ch].readFloats(convBuf1, 0, i);
                                for (j = i; j < chunkLength; ) {
                                    convBuf1[j++] = 0.0f;
                                }
                            }
                            for (i = plotIdx; i < plotIdx2; ) {
                                j = plotTable[i++] + inOff;
                                off = j - framesWritten;
                                if (off < 0) {
                                    k = -off;
                                    off = 0;
                                } else {
                                    k = 0;
                                }
                                len = Math.min(chunkLength, passLen - k + off);
                                for (ch = 0; ch < outChanNum; ch++) {
                                    f1 = Float.intBitsToFloat(plotTable[i++]);
                                    convBuf1 = ptrnBuf[ch % iconChanNum];
                                    convBuf2 = inBuf[ch];
                                    for (m = off, n = k; m < len; ) {
                                        convBuf2[m++] += f1 * convBuf1[n++];
                                    }
                                }
                            }
                            for (ch = 0; ch < outChanNum; ch++) {
                                outFloatF[ch].seekFloat(framesWritten);
                                outFloatF[ch].writeFloats(inBuf[ch], 0, chunkLength);
                            }
                        }
                        framesWritten += chunkLength;
                        progOff += chunkLength;
                        setProgression((float) progOff / (float) progLen);
                        if (!threadRunning) break topLevel;
                    }
                    framesWritten2 = (int) outFloatF[0].getSize();
                }
                if (!threadRunning) break topLevel;
            }
            if (pr.intg[PR_GAINTYPE] == GAIN_UNITY) {
                gain = (float) (Param.transform(pr.para[PR_GAIN], Param.ABS_AMP, new Param(1.0 / maxAmp, Param.ABS_AMP), null)).val;
            } else {
                gain = (float) (Param.transform(pr.para[PR_GAIN], Param.ABS_AMP, ampRef, null)).val;
            }
            normalizeAudioFile(outFloatF, outF, inBuf, gain, 1.0f);
            for (ch = 0; ch < outChanNum; ch++) {
                outFloatF[ch].cleanUp();
                outFloatF[ch] = null;
                outTempFile[ch].delete();
                outTempFile[ch] = null;
            }
            if (!threadRunning) break topLevel;
            outF.close();
            outF = null;
            outStream = null;
            iconF.close();
            iconF = null;
            iconStream = null;
            inBuf = null;
            maxAmp *= gain;
        } catch (IOException e1) {
            setError(e1);
        } catch (OutOfMemoryError e2) {
            inStream = null;
            outStream = null;
            ptrnStream = null;
            iconStream = null;
            inBuf = null;
            ptrnBuf = null;
            chunkBuf = null;
            convBuf1 = null;
            convBuf2 = null;
            System.gc();
            setError(new Exception(ERR_MEMORY));
            ;
        }
        if (g != null) {
            g.dispose();
            g = null;
        }
        if (inF != null) {
            inF.cleanUp();
            inF = null;
        }
        if (outF != null) {
            outF.cleanUp();
            outF = null;
        }
        if (iconF != null) {
            iconF.cleanUp();
            iconF = null;
        }
        if (ptrnF != null) {
            ptrnF.cleanUp();
            ptrnF = null;
        }
        if (outFloatF != null) {
            for (ch = 0; ch < outFloatF.length; ch++) {
                if (outFloatF[ch] != null) outFloatF[ch].cleanUp();
                if (outTempFile[ch] != null) outTempFile[ch].delete();
            }
        }
        if (plotF != null) {
            try {
                plotF.close();
            } catch (Exception e99) {
            }
        }
        if (plotTempFile != null) plotTempFile.delete();
    }

    protected void writeBytes(RandomAccessFile f, int[] intBuf, int off, int len, byte[] byteBuf) throws IOException {
        int num, i, j, k;
        len += off;
        while (off < len) {
            num = Math.min(len - off, byteBuf.length >> 2);
            for (i = 0, k = 0; i < num; i++) {
                j = intBuf[off++];
                byteBuf[k++] = (byte) (j >> 24);
                byteBuf[k++] = (byte) (j >> 16);
                byteBuf[k++] = (byte) (j >> 8);
                byteBuf[k++] = (byte) j;
            }
            f.write(byteBuf, 0, k);
        }
    }

    protected void readBytes(RandomAccessFile f, int[] intBuf, int off, int len, byte[] byteBuf) throws IOException {
        int num, k;
        len += off;
        while (off < len) {
            num = Math.min((len - off) << 2, byteBuf.length);
            f.readFully(byteBuf, 0, num);
            for (k = 0; k < num; ) {
                intBuf[off++] = (byteBuf[k++] << 24) | ((byteBuf[k++] & 0xFF) << 16) | ((byteBuf[k++] & 0xFF) << 8) | (byteBuf[k++] & 0xFF);
            }
        }
    }

    protected void reflectPropertyChanges() {
        super.reflectPropertyChanges();
        Component c;
        c = gui.getItemObj(GG_PLOTQUANTAMOUNT);
        if (c != null) {
            c.setEnabled(pr.bool[PR_PLOTQUANT]);
        }
        c = gui.getItemObj(GG_PLOTNUM);
        if (c != null) {
            c.setEnabled(pr.bool[PR_PLOTMAX]);
        }
        c = gui.getItemObj(GG_PLOTCHANGAIN);
        if (c != null) {
            c.setEnabled(pr.intg[PR_TRIGSOURCE] != SRC_SUM);
        }
    }
}
