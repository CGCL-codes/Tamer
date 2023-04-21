package net.sf.bt747.j4me.app;

import gps.BT747Constants;
import gps.Txt;
import gps.mvc.MtkModel;
import net.sf.bt747.j4me.app.screens.BT747Dialog;
import org.j4me.logging.Log;
import org.j4me.ui.components.CheckBox;

public final class LogFieldSelectScreen extends BT747Dialog {

    private static final int C_LOG_FMT_COUNT = 21;

    private CheckBox[] chkLogFmtItems = new CheckBox[LogFieldSelectScreen.C_LOG_FMT_COUNT];

    private boolean screenSetup = false;

    private void setupScreen() {
        if (!screenSetup) {
            screenSetup = true;
            deleteAll();
            setTitle("Set GPS Log Format");
            for (int i = 0; i < LogFieldSelectScreen.C_LOG_FMT_COUNT; i++) {
                chkLogFmtItems[i] = new CheckBox();
                chkLogFmtItems[i].setLabel(Txt.logFmtItems[i]);
                append(chkLogFmtItems[i]);
            }
        }
    }

    /** Get the format set by the user in the user interface. */
    private int getSelectedLogFormat() {
        int bitMask = 1;
        int logFormat = 0;
        for (int i = 0; i < LogFieldSelectScreen.C_LOG_FMT_COUNT - 1; i++) {
            if (chkLogFmtItems[i].isChecked()) {
                logFormat |= bitMask;
            }
            bitMask <<= 1;
        }
        if (chkLogFmtItems[LogFieldSelectScreen.C_LOG_FMT_COUNT - 1].isChecked()) {
            logFormat |= (1 << BT747Constants.FMT_LOG_PTS_WITH_VALID_FIX_ONLY_IDX);
        }
        Log.debug("getSelectedLogFormat:" + bt747.sys.JavaLibBridge.unsigned2hex(logFormat, 8));
        return logFormat;
    }

    public void showNotify() {
        setupScreen();
        updateLogFormat(m().getLogFormat());
    }

    protected void acceptNotify() {
        saveLogFormat();
        previous.show();
    }

    protected void declineNotify() {
        previous.show();
    }

    /**
     * Updates the format options shown the the user.<br>
     * This is typically done when the device responded with the current
     * settings.
     * 
     * @param pLogFormat
     *                LogFormat to set
     */
    private void updateLogFormat(final int pLogFormat) {
        Log.debug("updateLogFormat");
        int bitMask = 1;
        for (int i = 0; i < LogFieldSelectScreen.C_LOG_FMT_COUNT; i++) {
            chkLogFmtItems[i].setChecked((pLogFormat & bitMask) != 0);
            bitMask <<= 1;
        }
        chkLogFmtItems[LogFieldSelectScreen.C_LOG_FMT_COUNT - 1].setChecked((pLogFormat & (1 << BT747Constants.FMT_LOG_PTS_WITH_VALID_FIX_ONLY_IDX)) != 0);
        setLogFormatControls();
    }

    private int prevLogFormat = 0;

    private void setLogFormatControls() {
        int curLogFormat;
        curLogFormat = getSelectedLogFormat();
        if (curLogFormat != prevLogFormat) {
            prevLogFormat = curLogFormat;
            setTitle(m().getEstimatedNbrRecords(curLogFormat) + " est. pos.");
            Log.debug(getTitle());
            this.repaint();
        }
    }

    protected void keyReleased(final int keyCode) {
        setLogFormatControls();
        super.keyReleased(keyCode);
    }

    protected void pointerReleased(final int x, final int y) {
        setLogFormatControls();
        super.pointerReleased(x, y);
    }

    private void saveLogFormat() {
        c.setLogFormat(getSelectedLogFormat());
        c.setMtkDataNeeded(MtkModel.DATA_LOG_FORMAT);
    }

    private final AppModel m() {
        return c.getAppModel();
    }
}
