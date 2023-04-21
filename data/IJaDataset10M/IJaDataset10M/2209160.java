package com.elibera.m.xml.proc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import de.enough.polish.ui.Command;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.CommandListener;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.ui.List;
import de.enough.polish.ui.StyleSheet;
import com.elibera.m.app.MLE;
import com.elibera.m.display.DisplayCanvas;
import com.elibera.m.utils.HelperApp;
import com.elibera.m.utils.HelperStd;
import com.elibera.m.utils.HelperConverter;
import com.elibera.m.xml.PageSuite;
import com.elibera.m.xml.display.BoxElement;
import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.xml.display.InputElement;
import com.elibera.m.xml.display.SelectBoxElement;

/**
 * @author meisi
 *
 */
public class ProcSelectBoxElement extends ProcBoxElement implements CommandListener {

    public List display = null;

    public boolean lastListMultiple = false;

    public SelectBoxElement eventLastEl = null;

    public DisplayCanvas eventLastDC = null;

    public static int CLASS_ID = 9;

    public int getClassID() {
        return CLASS_ID;
    }

    Command ok;

    public ProcSelectBoxElement() {
        init(false);
    }

    private void init(boolean multiple) {
        ok = new Command(HelperApp.translateCoreWord(HelperApp.WORD_OKAY), Command.ITEM, 1);
        lastListMultiple = multiple;
        display = null;
        display = new List(HelperApp.translateWord(HelperApp.WORD_SELECTION), multiple ? List.MULTIPLE : List.EXCLUSIVE);
        display.addCommand(ok);
        display.setSelectCommand(ok);
        display.setCommandListener(this);
    }

    public void init(DisplayElement el2, DisplayCanvas dc) {
        SelectBoxElement el = (SelectBoxElement) el2;
        String v = HelperProc.getStoredInputValue(-2, el.formID + el.id, dc.ps);
        if (v != null) setValue(el, v);
        eventLastDC = dc;
    }

    public byte[] getSerialisedBytes(DisplayElement el2) {
        SelectBoxElement el = (SelectBoxElement) el2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        HelperConverter.writeByte(el.eckpunkte, dos);
        HelperConverter.writeByte(el.values, dos);
        HelperConverter.writeByte(el.text, dos);
        HelperConverter.writeByte(el.selections, dos);
        HelperConverter.writeByte(el.multiple ? 1 : 0, dos);
        HelperConverter.writeByte(el.size, dos);
        HelperConverter.writeByte(el.allowEdit, dos);
        HelperConverter.writeByte(el.id, dos);
        HelperConverter.writeByte(el.formID, dos);
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
        String[] values = HelperConverter.getStringArrayFromByte(dos);
        String[] text = HelperConverter.getStringArrayFromByte(dos);
        int[] selections = HelperConverter.getIntArrayFromByte(dos);
        boolean multiple = HelperConverter.getIntFromByte(dos) == 1 ? true : false;
        int size = HelperConverter.getIntFromByte(dos);
        boolean allowEdit = HelperConverter.getBooleanFromByte(dos);
        String id = HelperConverter.getStringFromStream(dos);
        int formID = HelperConverter.getIntFromByte(dos);
        return new SelectBoxElement(eck, values, text, multiple, selections, selections[0], id, size, allowEdit, formID, this);
    }

    public String getValue(boolean post, InputElement el) {
        SelectBoxElement el2 = (SelectBoxElement) el;
        StringBuffer ret = new StringBuffer();
        if (post) {
            storeValues(el2, eventLastDC);
            return null;
        }
        for (int i = 0; i < el2.selections.length; i++) {
            if (i > 0) ret.append(';');
            int sel = el2.selections[i];
            ret.append((el2.values.length > sel && !HelperStd.isEmpty(el2.values[sel])) ? el2.values[sel] : el2.text[sel]);
        }
        return ret.toString();
    }

    public void setValue(InputElement el2, String newValue) {
        SelectBoxElement el = (SelectBoxElement) el2;
        String[] vs = HelperStd.split(newValue, ';');
        el.selections = new int[0];
        for (int i = 0; i < vs.length; i++) {
            int _selection = HelperStd.whereIsItInArray(el.values, vs[i]);
            if (_selection >= 0) el.selections = HelperStd.incArray(el.selections, _selection);
        }
        if (el.selections.length <= 0) el.selections = new int[] { 0 };
        int _selection = el.selections[0];
        String[] _text = el.text;
        el.value = _text.length > _selection ? (!HelperStd.isEmpty(_text[_selection]) ? _text[_selection] : el.values[_selection]) : "";
        el.paintValue = null;
    }

    public void closePage(DisplayElement el2, DisplayCanvas dc) {
        storeValues(el2, dc);
        eventLastDC = null;
        eventLastEl = null;
    }

    private void storeValues(DisplayElement el2, DisplayCanvas dc) {
        SelectBoxElement el = (SelectBoxElement) el2;
        String v = getValue(false, el);
        if (v != null) {
            HelperProc.cacheFromInputValue(-2, el.formID + el.id, el.id, v, false, dc.ps);
            for (int i = 0; i < el.values.length; i++) {
                int p = HelperStd.whereIsItInArray(dc.ps.inputID, "_" + el.id + "__" + i);
                if (p >= 0) {
                    dc.ps.inputID[p] = null;
                    dc.ps.inputName[p] = null;
                }
            }
            for (int i = 0; i < el.selections.length; i++) {
                int sel = el.selections[i];
                v = (el.values.length > sel && !HelperStd.isEmpty(el.values[sel])) ? el.values[sel] : el.text[sel];
                HelperProc.cacheFromInputValue(el.formID, "_" + el.id + "__" + sel, el.id, v, false, dc.ps);
            }
        }
    }

    public boolean doEvent(BoxElement el2, DisplayCanvas dc) {
        SelectBoxElement el = (SelectBoxElement) el2;
        if (el.allowEdit) {
            if (lastListMultiple != el.multiple) init(el.multiple);
            if (eventLastEl == null || eventLastEl != el) {
                display.deleteAll();
                for (int i = 0; i < el.values.length; i++) {
                    String t = el.values[i];
                    if (!HelperStd.isEmpty(el.text[i])) t = el.text[i];
                    display.append(t, null);
                }
            }
            for (int i = 0; i < el.selections.length; i++) {
                System.out.println("set sel:" + el.selections[i]);
                display.setSelectedIndex(el.selections[i], true);
            }
            eventLastDC = dc;
            eventLastEl = el;
            MLE.midlet.setCurrent(display);
            return true;
        }
        return false;
    }

    public void commandAction(Command cmd, Displayable arg1) {
        if (cmd != null) {
            int[] sel = {};
            String selt = "";
            String add = "";
            for (int i = 0; i < eventLastEl.values.length; i++) {
                if (i == display.getSelectedIndex()) {
                    sel = HelperStd.incArray(sel, i);
                    selt = add + eventLastEl.values[i];
                    add = ";";
                }
            }
            if (sel.length <= 0) sel = new int[] { 0 };
            eventLastEl.selections = sel;
            setValue(eventLastEl, selt);
            MLE.midlet.setCurrent(eventLastDC);
            eventLastDC.repaint();
        }
    }
}
