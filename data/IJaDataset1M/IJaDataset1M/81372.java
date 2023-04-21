package bt747.waba_view;

import waba.ui.Button;
import waba.ui.Container;
import waba.ui.ControlEvent;
import waba.ui.Edit;
import waba.ui.Event;
import waba.ui.Label;
import waba.ui.PushButtonGroup;
import bt747.Txt;
import bt747.model.AppSettings;
import bt747.model.Model;
import bt747.model.ModelEvent;
import bt747.model.ModelListener;
import bt747.sys.JavaLibBridge;

/**
 * @author Mario De Weerd
 */
public final class GPSLogFilterAdv extends Container implements ModelListener {

    private Edit minRecCount;

    private Edit maxRecCount;

    private Edit minSpeed;

    private Edit maxSpeed;

    private Edit minDist;

    private Edit maxDist;

    private Edit maxPDOP;

    private Edit maxHDOP;

    private Edit maxVDOP;

    private Edit minNSAT;

    private Button btSet;

    private Button btClear;

    private PushButtonGroup pbPtType;

    private final String[] C_PB_TYPE_NAMES = { Txt.getString(Txt.ACTIVE), Txt.getString(Txt.INACTIVE) };

    private final Model m;

    private final AppController c;

    public GPSLogFilterAdv(final AppController c, final Model m) {
        this.m = m;
        this.c = c;
    }

    protected void onStart() {
        super.onStart();
        add(minRecCount = new Edit(), LEFT, SAME);
        add(new Label(Txt.getString(Txt.FLTR_REC)), CENTER, SAME);
        add(maxRecCount = new Edit(), RIGHT, SAME);
        add(minSpeed = new Edit(), LEFT, AFTER);
        add(new Label(Txt.getString(Txt.FLTR_SPD)), CENTER, SAME);
        add(maxSpeed = new Edit(), RIGHT, SAME);
        add(minDist = new Edit(), LEFT, AFTER);
        add(new Label(Txt.getString(Txt.FLTR_DST)), CENTER, SAME);
        add(maxDist = new Edit(), RIGHT, SAME);
        add(new Label(Txt.getString(Txt.FLTR_PDOP)), CENTER, AFTER);
        add(maxPDOP = new Edit(), RIGHT, SAME);
        add(new Label(Txt.getString(Txt.FLTR_HDOP)), CENTER, AFTER);
        add(maxHDOP = new Edit(), RIGHT, SAME);
        add(new Label(Txt.getString(Txt.FLTR_VDOP)), CENTER, AFTER);
        add(maxVDOP = new Edit(), RIGHT, SAME);
        add(minNSAT = new Edit(), LEFT, AFTER);
        add(new Label(Txt.getString(Txt.FLTR_NSAT)), CENTER, SAME);
        String allowedKeys;
        allowedKeys = Edit.numbersSet + "-";
        minRecCount.setValidChars(allowedKeys);
        maxRecCount.setValidChars(allowedKeys);
        minNSAT.setValidChars(allowedKeys);
        allowedKeys += ".";
        minSpeed.setValidChars(allowedKeys);
        maxSpeed.setValidChars(allowedKeys);
        minDist.setValidChars(allowedKeys);
        maxDist.setValidChars(allowedKeys);
        maxPDOP.setValidChars(allowedKeys);
        maxHDOP.setValidChars(allowedKeys);
        maxVDOP.setValidChars(allowedKeys);
        add(new Label(Txt.getString(Txt.IGNORE_0VALUES)), CENTER, AFTER);
        btSet = new Button(Txt.getString(Txt.SET));
        add(btSet, LEFT, AFTER + 3);
        btClear = new Button(Txt.getString(Txt.CLEAR));
        add(btClear, AFTER, SAME);
        add(pbPtType = new PushButtonGroup(C_PB_TYPE_NAMES, true, 0, 1, 2, 1, true, PushButtonGroup.NORMAL), RIGHT, SAME);
        pbPtType.setSelected(m.getBooleanOpt(AppSettings.ADVFILTACTIVE) ? 0 : 1);
        getSettings();
    }

    private void setSettings() {
        c.setIntOpt(AppSettings.MIN_RECCOUNT, JavaLibBridge.toInt(minRecCount.getText()));
        c.setIntOpt(AppSettings.MAX_RECCOUNT, JavaLibBridge.toInt(maxRecCount.getText()));
        c.setFloatOpt(AppSettings.MIN_SPEED, JavaLibBridge.toFloat(minSpeed.getText()));
        c.setFloatOpt(AppSettings.MAX_SPEED, JavaLibBridge.toFloat(maxSpeed.getText()));
        c.setFloatOpt(AppSettings.MIN_DISTANCE, JavaLibBridge.toFloat(minDist.getText()));
        c.setFloatOpt(AppSettings.MAX_DISTANCE, JavaLibBridge.toFloat(maxDist.getText()));
        c.setFloatOpt(AppSettings.MAX_PDOP, (JavaLibBridge.toFloat(maxPDOP.getText())));
        c.setFloatOpt(AppSettings.MAX_HDOP, (JavaLibBridge.toFloat(maxHDOP.getText())));
        c.setFloatOpt(AppSettings.MAX_VDOP, (JavaLibBridge.toFloat(maxVDOP.getText())));
        c.setIntOpt(AppSettings.MIN_NSAT, JavaLibBridge.toInt(minNSAT.getText()));
        c.saveSettings();
        c.setFilters();
    }

    private void getSettings() {
        minRecCount.setText("" + m.getIntOpt(AppSettings.MIN_RECCOUNT));
        maxRecCount.setText("" + m.getIntOpt(AppSettings.MAX_RECCOUNT));
        minSpeed.setText(JavaLibBridge.toString(m.getFloatOpt(AppSettings.MIN_SPEED), 2));
        maxSpeed.setText(JavaLibBridge.toString(m.getFloatOpt(AppSettings.MAX_SPEED), 2));
        minDist.setText(JavaLibBridge.toString(m.getFloatOpt(AppSettings.MIN_DISTANCE), 2));
        maxDist.setText(JavaLibBridge.toString(m.getFloatOpt(AppSettings.MAX_DISTANCE), 2));
        maxPDOP.setText(JavaLibBridge.toString(m.getFloatOpt(AppSettings.MAX_PDOP), 2));
        maxHDOP.setText(JavaLibBridge.toString(m.getFloatOpt(AppSettings.MAX_HDOP), 2));
        maxVDOP.setText(JavaLibBridge.toString(m.getFloatOpt(AppSettings.MAX_VDOP), 2));
        minNSAT.setText("" + m.getIntOpt(AppSettings.MIN_NSAT));
        c.setFilters();
    }

    private void clearSettings() {
        minRecCount.setText("0");
        maxRecCount.setText("0");
        minSpeed.setText("0");
        maxSpeed.setText("0");
        minDist.setText("0");
        maxDist.setText("0");
        maxPDOP.setText("0");
        maxHDOP.setText("0");
        maxVDOP.setText("0");
        minNSAT.setText("0");
        setSettings();
    }

    public final void onEvent(final Event event) {
        super.onEvent(event);
        switch(event.type) {
            case ControlEvent.PRESSED:
                event.consumed = true;
                if (event.target == btSet) {
                    setSettings();
                } else if (event.target == btClear) {
                    clearSettings();
                } else if (event.target == pbPtType) {
                    c.setBooleanOpt(Model.ADVFILTACTIVE, pbPtType.getSelected() == 0);
                } else if (event.target == this) {
                } else {
                    event.consumed = false;
                }
                break;
            default:
                break;
        }
    }

    public final void modelEvent(final ModelEvent event) {
    }
}
