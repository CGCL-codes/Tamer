package bt747.waba_view;

import waba.ui.Button;
import waba.ui.Container;
import waba.ui.Control;
import waba.ui.ControlEvent;
import waba.ui.Event;
import waba.ui.Label;
import waba.ui.MessageBox;
import gps.BT747Constants;
import gps.mvc.MtkController;
import bt747.model.ModelEvent;
import bt747.Txt;
import bt747.model.Model;
import bt747.model.ModelListener;

/**
 * Implement some buttons to easily do more complex operations or to do some
 * things not done in other tabs.
 * 
 * @author Mario De Weerd
 */
public final class GPSLogEasy extends Container implements ModelListener {

    private Button btSet5Hz;

    private Button btSet2Hz;

    private Button btStore;

    private Button btRestore;

    private Button btHotStart;

    private Button btWarmStart;

    private Button btColdStart;

    private Button btFullColdStart;

    private final Button[] chkRCR = new Button[BT747Constants.C_RCR_COUNT];

    private Button btForceErase;

    private Label lbLogUserTxt;

    private final Model m;

    private final AppController c;

    public GPSLogEasy(final Model m, final AppController c) {
        this.m = m;
        this.c = c;
    }

    protected final void onStart() {
        add(btSet5Hz = new Button(Txt.getString(Txt.BT_5HZ_FIX)), LEFT, AFTER + 3);
        add(btSet2Hz = new Button(Txt.getString(Txt.BT_2HZ_FIX)), RIGHT, SAME);
        add(btStore = new Button(Txt.getString(Txt.STORE_SETTINGS)), LEFT, AFTER + 3);
        add(btRestore = new Button(Txt.getString(Txt.RESTORE_SETTINGS)), RIGHT, SAME);
        enableStore();
        add(btHotStart = new Button(Txt.getString(Txt.BT_HOT)), LEFT, AFTER + 10);
        add(btWarmStart = new Button(Txt.getString(Txt.BT_WARM)), CENTER, SAME);
        add(btColdStart = new Button(Txt.getString(Txt.BT_COLD)), RIGHT, SAME);
        add(btFullColdStart = new Button(Txt.getString(Txt.BT_FACT_RESET)), LEFT, AFTER + 2);
        add(btForceErase = new Button(Txt.getString(Txt.BT_FORCED_ERASE)), RIGHT, SAME);
        add(lbLogUserTxt = new Label(Txt.getString(Txt.BT_PT_WITH_REASON)), LEFT, AFTER + 2);
        int x = LEFT;
        int y = SAME;
        Control rel = null;
        final int RCR_COL = 4;
        for (int i = 0; i < BT747Constants.C_RCR_COUNT; i++) {
            chkRCR[i] = new Button(Txt.getRcrString(i));
            if (i == 0) {
                x = LEFT;
            } else if ((i % (BT747Constants.C_RCR_COUNT / RCR_COL)) == 0) {
                x = getClientRect().width * (i / (BT747Constants.C_RCR_COUNT / RCR_COL)) / RCR_COL + 8;
            }
            if ((i % (BT747Constants.C_RCR_COUNT / RCR_COL)) == 0) {
                rel = lbLogUserTxt;
                y = AFTER + 6;
            } else {
                y = AFTER - 1;
            }
            add(chkRCR[i], x, y, rel);
            rel = chkRCR[i];
            chkRCR[i].setEnabled(true);
        }
    }

    private void enableStore() {
        btStore.setEnabled(c.isEnableStoreOK());
        btRestore.setEnabled(m.isStoredSetting1());
    }

    public final void onEvent(final Event event) {
        super.onEvent(event);
        switch(event.type) {
            case ControlEvent.PRESSED:
                event.consumed = true;
                if (event.target == this) {
                    c.reqSettingsForStorage();
                } else if (event.target == btSet2Hz) {
                    c.setFixInterval(500);
                } else if (event.target == btSet5Hz) {
                    c.setLogTimeInterval(2);
                    c.setFixInterval(200);
                } else if (event.target == btStore) {
                    c.storeSetting1();
                } else if (event.target == btRestore) {
                    c.restoreSetting1();
                } else if (event.target == btHotStart) {
                    c.gpsCmd(MtkController.CMD_HOTSTART);
                } else if (event.target == btColdStart) {
                    c.gpsCmd(MtkController.CMD_COLDSTART);
                } else if (event.target == btWarmStart) {
                    c.gpsCmd(MtkController.CMD_WARMSTART);
                } else if (event.target == btFullColdStart) {
                    MessageBox mb;
                    String[] szExitButtonArray = { Txt.getString(Txt.YES), Txt.getString(Txt.NO) };
                    mb = new MessageBox(Txt.getString(Txt.TITLE_ATTENTION), Txt.getString(Txt.CONFIRM_FACT_RESET), szExitButtonArray);
                    mb.popupBlockingModal();
                    if (mb.getPressedButtonIndex() == 0) {
                        c.gpsCmd(MtkController.CMD_FACTORYRESET);
                    }
                } else if (event.target == btForceErase) {
                    c.recoveryErase();
                } else {
                    for (int i = 0; i < BT747Constants.C_RCR_COUNT; i++) {
                        if (event.target == chkRCR[i]) {
                            c.logImmediate(1 << i);
                        }
                    }
                    event.consumed = false;
                }
                break;
            default:
                break;
        }
    }

    public final void modelEvent(final ModelEvent event) {
        switch(event.getType()) {
            case ModelEvent.UPDATE_FIX_PERIOD:
            case ModelEvent.UPDATE_DGPS_MODE:
            case ModelEvent.UPDATE_SBAS:
            case ModelEvent.UPDATE_SBAS_TEST:
            case ModelEvent.UPDATE_DATUM:
            case ModelEvent.UPDATE_LOG_TIME_INTERVAL:
            case ModelEvent.UPDATE_LOG_SPEED_INTERVAL:
            case ModelEvent.UPDATE_LOG_DISTANCE_INTERVAL:
            case ModelEvent.UPDATE_LOG_FORMAT:
            case ModelEvent.UPDATE_OUTPUT_NMEA_PERIOD:
                enableStore();
                break;
        }
    }
}
