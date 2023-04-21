package de.sciss.fscape.op;

import java.io.*;
import de.sciss.fscape.gui.*;
import de.sciss.fscape.prop.*;
import de.sciss.fscape.spect.*;
import de.sciss.fscape.util.*;

/**
 *  @version	0.71, 14-Nov-07
 */
public class ZoomOp extends Operator {

    protected static final String defaultName = "Zoom";

    protected static Presets static_presets = null;

    protected static Prefs static_prefs = null;

    protected static PropertyArray static_pr = null;

    protected static final int SLOT_INPUT = 0;

    protected static final int SLOT_OUTPUT = 1;

    private static final int PR_FACTOR = 0;

    private static final int PR_BAND = 0;

    private static final String PRN_FACTOR = "Factor";

    private static final String PRN_BAND = "Band";

    private static final int prIntg[] = { 0 };

    private static final String prIntgName[] = { PRN_FACTOR };

    private static final Param prPara[] = { null };

    private static final String prParaName[] = { PRN_BAND };

    public ZoomOp() {
        super();
        if (static_prefs == null) {
            static_prefs = new OpPrefs(getClass(), getDefaultPrefs());
        }
        if (static_pr == null) {
            static_pr = new PropertyArray();
            static_pr.intg = prIntg;
            static_pr.intgName = prIntgName;
            static_pr.para = prPara;
            static_pr.para[PR_BAND] = new Param(16000.0, Param.ABS_HZ);
            static_pr.paraName = prParaName;
            static_pr.superPr = Operator.op_static_pr;
        }
        if (static_presets == null) {
            static_presets = new Presets(getClass(), static_pr.toProperties(true));
        }
        opName = "ZoomOp";
        prefs = static_prefs;
        presets = static_presets;
        pr = (PropertyArray) static_pr.clone();
        slots.addElement(new SpectStreamSlot(this, Slots.SLOTS_READER));
        slots.addElement(new SpectStreamSlot(this, Slots.SLOTS_WRITER));
        icon = new OpIcon(this, OpIcon.ID_FLIPFREQ, defaultName);
    }

    public void run() {
        runInit();
        int ch;
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        int zoomFactor;
        int srcBands, destBands;
        int startBand;
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
            zoomFactor = 2 << pr.intg[PR_FACTOR];
            srcBands = runInStream.bands;
            destBands = (srcBands - 1) / zoomFactor + 1;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutStream.bands = destBands;
            runOutStream.smpPerFrame /= zoomFactor;
            runOutSlot.initWriter(runOutStream);
            startBand = (int) (pr.para[PR_BAND].val / ((runInStream.hiFreq - runInStream.loFreq) / srcBands) + 0.5);
            startBand = startBand - (destBands >> 1);
            if (startBand < 0) {
                startBand = 0;
            } else if ((startBand + destBands) > srcBands) {
                startBand = srcBands - destBands;
            }
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
                    System.arraycopy(runInFr.data[ch], startBand << 1, runOutFr.data[ch], 0, destBands << 1);
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
        gui = new PropertyGUI("gl" + GroupLabel.NAME_GENERAL + "\n" + "lbZoom factor;ch,pr" + PRN_FACTOR + "," + "it1:2,it1:4,it1:8,it1:16\n" + "lbZoom to bands around;pf" + Constants.absHzSpace + ",id1,pr" + PRN_BAND);
        return gui;
    }
}
