package com.elibera.m.xml.proc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import de.enough.polish.android.lcdui.Font;
import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.ui.TextBox;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.TextField;
import de.enough.polish.ui.StyleSheet;
import com.elibera.m.app.MLE;
import com.elibera.m.display.DisplayCanvas;
import com.elibera.m.display.HelperDisplay;
import com.elibera.m.events.CanvasSwitchEvent;
import com.elibera.m.events.InputHandler;
import com.elibera.m.rms.HelperRMSStoreMLibera;
import com.elibera.m.utils.HelperApp;
import com.elibera.m.utils.HelperConverter;
import com.elibera.m.utils.HelperStd;
import com.elibera.m.xml.PageSuite;
import com.elibera.m.xml.XMLTagTextBox;
import com.elibera.m.xml.display.BoxElement;
import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.xml.display.InputElement;

public class ProcBoxElement extends ProcVierEckElement implements ProcInputElement {

    public static int CLASS_ID = 5;

    public int getClassID() {
        return CLASS_ID;
    }

    public void doPaint(DisplayElement el2, Graphics g, int offsetX, int offsetY, DisplayCanvas dc) {
        BoxElement el = (BoxElement) el2;
        Font oldf = g.getFont();
        int x = el.eckpunkte[0] + offsetX;
        int y = el.eckpunkte[1] + offsetY;
        int w = getLastLineWidth(el);
        int h = getLastLineHeight(el);
        g.setFont(XMLTagTextBox.font);
        if (!el.allowEdit) g.setColor(0xFFFFFF); else g.setColor(HelperDisplay.COLOR_TEXTBOX_BG);
        g.fillRoundRect(x, y, w, h, 10, 10);
        g.setGrayScale(0);
        g.drawRoundRect(x, y, w, h, 10, 10);
        String value = el.paintValue;
        if (value == null) {
            el.paintValue = calculateDisplayValue(el, XMLTagTextBox.font);
            value = el.paintValue;
        }
        if (el.hiddenValue) {
            value = "";
            for (int i = 0; i < el.value.length(); i++) value = value + '*';
        }
        if (!el.interaktiv) HelperProc.greyOutVierEck(g, x + 2, y + 1, x + w, y + h + 1, 4);
        g.drawString(value, x + 2 + w / 2, y + 2, Graphics.TOP | Graphics.HCENTER);
        g.setFont(oldf);
    }

    /**
	 * berechnet wie viel vom text angezeigt werden soll
	 * @param el
	 * @return
	 */
    public String calculateDisplayValue(BoxElement el, Font f) {
        int w = 0, width = el.eckpunkte[2] - el.eckpunkte[0] - 6;
        for (int i = 0; i < el.value.length(); i++) {
            w += f.charWidth(el.value.charAt(i));
            if (w > width) return el.value.substring(0, i > 3 ? i - 3 : i) + (i > 3 ? "..." : "");
        }
        return el.value;
    }

    public byte[] getSerialisedBytes(DisplayElement el2) {
        BoxElement el = (BoxElement) el2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        HelperConverter.writeByte(el.eckpunkte, dos);
        HelperConverter.writeByte(el.value, dos);
        HelperConverter.writeByte(el.size, dos);
        HelperConverter.writeByte(el.maxCharSize, dos);
        HelperConverter.writeByte(el.allowEdit ? 1 : 0, dos);
        HelperConverter.writeByte(el.textFieldConstraints, dos);
        HelperConverter.writeByte(el.id, dos);
        HelperConverter.writeByte(el.hiddenValue ? 1 : 0, dos);
        HelperConverter.writeByte(el.formID, dos);
        HelperConverter.writeByte(el.minCharSize, dos);
        return baos.toByteArray();
    }

    /**
	 * erstellt das DisplayElement anhand der Bytes
	 * deserialisieren
	 * @param data
	 * @param midlet
	 * @return
	 */
    public DisplayElement getDisplayElement(byte[] data, PageSuite ps) {
        ByteArrayInputStream baos = new ByteArrayInputStream(data);
        DataInputStream dos = new DataInputStream(baos);
        int[] eck = HelperConverter.getIntArrayFromByte(dos);
        String v = HelperConverter.getStringFromStream(dos);
        int size = HelperConverter.getIntFromByte(dos);
        int maxCharSize = HelperConverter.getIntFromByte(dos);
        int allowEdit = HelperConverter.getIntFromByte(dos);
        int textFieldConstraints = HelperConverter.getIntFromByte(dos);
        String id = HelperConverter.getStringFromStream(dos);
        int hiddenValue = HelperConverter.getIntFromByte(dos);
        int formID = HelperConverter.getIntFromByte(dos);
        int minCharSize = HelperConverter.getIntFromByte(dos);
        BoxElement el = new BoxElement(eck, v, id, size, maxCharSize, minCharSize, textFieldConstraints, allowEdit == 1 ? true : false, hiddenValue == 1 ? true : false, formID, this);
        return el;
    }

    /**
	 * gibt die Onjekt ID für dieses DIsplayElement zurück
	 * muss überschrieben werden, macht für Input-Elemente Sinn
	 * @return
	 */
    public String getObjectId(boolean post, InputElement el) {
        return ((BoxElement) el).id;
    }

    public String getValue(boolean post, InputElement el) {
        return ((BoxElement) el).value;
    }

    public byte[] getByteValue(InputElement el) {
        return ((BoxElement) el).value.getBytes();
    }

    public String getContentType(InputElement el) {
        return null;
    }

    public void setValue(InputElement el, byte[] newValue) {
        setValue(el, new String(newValue));
    }

    public void setValue(InputElement el2, String newValue) {
        BoxElement el = (BoxElement) el2;
        el.value = newValue;
        if (el.value == null) el.value = "";
        if (el.value.length() > el.maxCharSize) el.value = el.value.substring(0, el.maxCharSize);
        el.paintValue = null;
    }

    /**
	 * gibt den Wert als int zurück, ansonsten -1
	 */
    public int getIntValue(BoxElement el) {
        return HelperStd.parseInt(el.value, -1);
    }

    /**
	 * sollte das element geklickt worden sein, wird diese Methode aufgerufen
	 */
    public boolean pointerClicked(DisplayElement el2, int x, int y, DisplayCanvas dc, int index) {
        BoxElement el = (BoxElement) el2;
        System.out.println("Pointer clicked:");
        return doEvent(el, dc);
    }

    /**
	 * sollte das Element selektiert sein, werden alle KeyEvents an dieses Ding weitergeleitet
	 * wird true zurückgegeben, werden keine weiteren Events mehr abgefragt
	 */
    public boolean keyPressed(DisplayElement el2, int key, DisplayCanvas dc) {
        BoxElement el = (BoxElement) el2;
        int[] keys = HelperRMSStoreMLibera.appKeys;
        if (!(key == keys[1] || key == InputHandler.DEF_KEYS[1] || key == keys[2] || key == InputHandler.DEF_KEYS[2] || key == keys[3] || key == InputHandler.DEF_KEYS[3] || key == keys[7] || key == InputHandler.DEF_KEYS[7] || key == keys[4] || key == InputHandler.DEF_KEYS[4])) return doEvent(el, dc);
        return false;
    }

    /**
	 * ist das ding editierbar, wird der edit screen gezeigt
	 * gibt true zurück, wenn alles richtig war, und das event ausgeführt wurde
	 */
    public boolean doEvent(BoxElement el, DisplayCanvas dc) {
        if (el.allowEdit) {
            MLE midlet = MLE.midlet;
            String v = el.value;
            if (el.textFieldConstraints == TextField.PASSWORD) v = "";
            TextBox tb = new TextBox(HelperApp.translateCoreWord(HelperApp.WORD_INPUT), v, el.maxCharSize, el.textFieldConstraints);
            tb.addCommand(midlet.ok);
            tb.addCommand(midlet.abbrechen);
            CanvasSwitchEvent ev = new CanvasSwitchEvent(tb, dc, dc, true, el);
            midlet.startThread(ev, dc);
            el.paintValue = null;
            return true;
        }
        return false;
    }

    /**
	 * die zugeordnete Form ID oder -1
	 * @param el
	 * @return
	 */
    public int getFormID(InputElement el) {
        return ((BoxElement) el).formID;
    }

    /**
	 * wird aufgerufen, wenn die Seitenelemente (für die aktuelle Seite) generiert weren
	 * @param dc
	 */
    public void init(DisplayElement el2, DisplayCanvas dc) {
        BoxElement el = (BoxElement) el2;
        String v = HelperProc.getStoredInputValue(el.formID, el.id, dc.ps);
        if (v != null) setValue(el, v);
    }

    /**
	 * wird aufgerufen, wenn die Seite geschlossen wird
	 * @param dc
	 */
    public void closePage(DisplayElement el2, DisplayCanvas dc) {
        BoxElement el = (BoxElement) el2;
        el.paintValue = null;
        String v = getValue(true, el);
        if (v != null) HelperProc.cacheFromInputValue(el.formID, el.id, null, v, false, dc.ps);
    }

    public String getFilename(InputElement el2) {
        return null;
    }
}
