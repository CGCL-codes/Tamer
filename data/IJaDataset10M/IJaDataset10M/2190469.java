package org.tzi.wr.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import org.tzi.wr.DeviceCache;
import org.tzi.wr.ScanLog;
import org.tzi.wr.Settings;
import org.tzi.wr.WR;

public class SettingsForm extends MyForm implements CommandListener {

    DialogListener mListener;

    TextField mAniSpeed;

    TextField mAniAppear;

    TextField mAniDisappear;

    TextField mOnScreenTimeout;

    TextField mBubbleRadius;

    ChoiceGroup mScanEnabled;

    TextField mMinSleep;

    TextField mMaxSleep;

    TextField mScanTimeout;

    TextField mMeetingTimeout;

    ChoiceGroup mConsoleEnabled;

    private Command mCmdSave;

    private Command mCmdCancel;

    private Command mCmdClearEverything;

    private ChoiceGroup mQuitOnBTErrorEnabled;

    private ChoiceGroup mInstantSendEnabled;

    private ChoiceGroup mCellIDEnabled;

    private Alert mClearLogAlert;

    private Command mCmdConfCancel;

    private Command mCmdConfClear;

    public SettingsForm(DialogListener aListener) {
        super("Settings");
        mListener = aListener;
        mCmdSave = new Command("Ok", "Ok", Command.BACK, 1);
        addCommand(mCmdSave);
        mCmdCancel = new Command("Cancel", "Cancel", Command.CANCEL, 2);
        addCommand(mCmdCancel);
        if (Settings.getInstance().mExpertMode) {
            mCmdClearEverything = new Command("Clear Everything", "Clear", Command.SCREEN, 6);
            addCommand(mCmdClearEverything);
        }
        mCmdConfCancel = new Command("Cancel", "Cancel", Command.CANCEL, 1);
        mCmdConfClear = new Command("Clear", "Clear", Command.OK, 1);
        mClearLogAlert = new Alert("", "Clear all log data and forget about everything?", null, javax.microedition.lcdui.AlertType.CONFIRMATION);
        mClearLogAlert.addCommand(mCmdConfCancel);
        mClearLogAlert.addCommand(mCmdConfClear);
        mClearLogAlert.setCommandListener(this);
        mClearLogAlert.setTimeout(-2);
        setCommandListener(this);
    }

    public void loadValues() {
        deleteAll();
        appendHeading("Graphics");
        mAniSpeed = new TextField("Animation speed (FPS)", Settings.getInstance().mAniSpeed + "", 2, TextField.NUMERIC);
        append(mAniSpeed);
        mBubbleRadius = new TextField("Bubble radius (pixel)", Settings.getInstance().mBubbleRadius + "", 2, TextField.NUMERIC);
        append(mBubbleRadius);
        if (Settings.getInstance().mExpertMode) {
            mAniAppear = new TextField("Appearance ani. (sec.)", Settings.getInstance().mAniAppear + "", 3, TextField.NUMERIC);
            append(mAniAppear);
            mAniDisappear = new TextField("Disappearance ani (sec.)", Settings.getInstance().mAniDisappear + "", 3, TextField.NUMERIC);
            append(mAniDisappear);
            appendSpacer();
            appendHeading("Scanning");
            mScanEnabled = new ChoiceGroup(null, ChoiceGroup.MULTIPLE);
            mScanEnabled.append("Scanning enabled", null);
            boolean[] a = new boolean[1];
            a[0] = Settings.getInstance().mScanEnabled;
            mScanEnabled.setSelectedFlags(a);
            append(mScanEnabled);
            mMinSleep = new TextField("Min. sleep (sec.)", Settings.getInstance().mMinSleep + "", 3, TextField.NUMERIC);
            append(mMinSleep);
            mMaxSleep = new TextField("Max. sleep (sec.)", Settings.getInstance().mMaxSleep + "", 3, TextField.NUMERIC);
            append(mMaxSleep);
            mScanTimeout = new TextField("Scan timeout (sec.)", Settings.getInstance().mScanTimeout + "", 3, TextField.NUMERIC);
            append(mScanTimeout);
            mInstantSendEnabled = new ChoiceGroup(null, ChoiceGroup.MULTIPLE);
            mInstantSendEnabled.append("Instant Log Send", null);
            boolean[] ise = new boolean[1];
            ise[0] = Settings.getInstance().mInstantSend;
            mInstantSendEnabled.setSelectedFlags(ise);
            append(mInstantSendEnabled);
            mQuitOnBTErrorEnabled = new ChoiceGroup(null, ChoiceGroup.MULTIPLE);
            mQuitOnBTErrorEnabled.append("Quit on BT Error", null);
            boolean[] bte = new boolean[1];
            bte[0] = Settings.getInstance().mQuitOnBTError;
            mQuitOnBTErrorEnabled.setSelectedFlags(bte);
            append(mQuitOnBTErrorEnabled);
            mCellIDEnabled = new ChoiceGroup(null, ChoiceGroup.MULTIPLE);
            mCellIDEnabled.append("Cell ID", null);
            boolean[] cte = new boolean[1];
            cte[0] = Settings.getInstance().mCellIDEnabled;
            mCellIDEnabled.setSelectedFlags(cte);
            append(mCellIDEnabled);
            appendSpacer();
            appendHeading("Statistics");
            mMeetingTimeout = new TextField("Meeting timeout (#scans)", Settings.getInstance().mMeetingTimeout + "", 2, TextField.NUMERIC);
            append(mMeetingTimeout);
            appendSpacer();
            appendHeading("Debugging");
            mConsoleEnabled = new ChoiceGroup(null, ChoiceGroup.MULTIPLE);
            mConsoleEnabled.append("Console enabled", null);
            boolean[] b = new boolean[1];
            b[0] = Settings.getInstance().mConsoleEnabled;
            mConsoleEnabled.setSelectedFlags(b);
            append(mConsoleEnabled);
        }
    }

    private void saveChanges() {
        Settings s = Settings.getInstance();
        s.mAniSpeed = Integer.parseInt(mAniSpeed.getString());
        if (Settings.getInstance().mExpertMode) {
            s.mAniAppear = Integer.parseInt(mAniAppear.getString());
            s.mAniDisappear = Integer.parseInt(mAniDisappear.getString());
        }
        s.mBubbleRadius = Integer.parseInt(mBubbleRadius.getString());
        if (Settings.getInstance().mExpertMode) {
            s.mScanEnabled = mScanEnabled.isSelected(0);
            s.mMinSleep = Integer.parseInt(mMinSleep.getString());
            s.mMaxSleep = Integer.parseInt(mMaxSleep.getString());
            s.mScanTimeout = Integer.parseInt(mScanTimeout.getString());
            s.mQuitOnBTError = mQuitOnBTErrorEnabled.isSelected(0);
            s.mMeetingTimeout = Integer.parseInt(mMeetingTimeout.getString());
            s.mConsoleEnabled = mConsoleEnabled.isSelected(0);
            s.mInstantSend = mInstantSendEnabled.isSelected(0);
            s.mCellIDEnabled = mCellIDEnabled.isSelected(0);
        }
        s.saveSetting();
    }

    public void commandAction(Command aCmd, Displayable arg1) {
        if (aCmd == mCmdSave) {
            saveChanges();
            mListener.screenClosed(this);
        } else if (aCmd == mCmdCancel) {
            mListener.screenClosed(this);
        } else if (aCmd == mCmdClearEverything) {
            Display.getDisplay(WR.mMIDlet).setCurrent(mClearLogAlert, this);
        } else if (aCmd == mCmdConfClear) {
            ScanLog.getInstance().clearLog();
            DeviceCache.getInstance().clearEverything();
            Display.getDisplay(WR.mMIDlet).setCurrent(this);
        } else if (aCmd == mCmdConfCancel) {
            Display.getDisplay(WR.mMIDlet).setCurrent(this);
        }
    }
}
