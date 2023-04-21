package com.elibera.m.fileio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import com.elibera.m.display.DisplayCanvas;
import com.elibera.m.events.InputHandler;
import com.elibera.m.events.RootThread;
import com.elibera.m.rms.HelperRMSStoreMLibera;
import com.elibera.m.utils.HelperApp;
import com.elibera.m.utils.HelperConverter;
import com.elibera.m.utils.HelperStd;
import com.elibera.m.xml.PageSuite;
import com.elibera.m.xml.XMLTagImage;
import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.xml.display.InputElement;
import com.elibera.m.xml.proc.HelperProc;
import com.elibera.m.xml.proc.ProcInputElement;
import com.elibera.m.xml.proc.ProcVierEckElement;

public class ProcFileBrowser extends ProcVierEckElement implements ProcInputElement {

    public static int CLASS_ID = 202;

    public FileSelector fs = null;

    public void doPaint(DisplayElement el2, Graphics g, int offsetX, int offsetY, DisplayCanvas dc) {
        FileBrowserElement el = (FileBrowserElement) el2;
        int x = el.eckpunkte[0] + offsetX + XMLTagImage.IMAGE_BORDER;
        int y = el.eckpunkte[3] + offsetY - getLastLineHeight(el2) / 2;
        g.drawImage(el.img, x, y, Graphics.VCENTER | Graphics.LEFT);
        if (el.fileName != null) g.drawImage(el.img2, x, y, Graphics.VCENTER | Graphics.LEFT);
    }

    public byte[] getSerialisedBytes(DisplayElement el2) {
        FileBrowserElement el = (FileBrowserElement) el2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        HelperConverter.writeByte(el.eckpunkte, dos);
        HelperConverter.writeByte(el.id, dos);
        HelperConverter.writeByte(el.rootFolder, dos);
        HelperConverter.writeByte(el.formID, dos);
        HelperConverter.writeByte(el.useAllFiles, dos);
        HelperConverter.writeByte(el.selectOnlyDirectory, dos);
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
        String id = HelperConverter.getStringFromStream(dos);
        String rootfolder = HelperConverter.getStringFromStream(dos);
        int formID = HelperConverter.getIntFromByte(dos);
        boolean useAllFiles = HelperConverter.getBooleanFromByte(dos);
        boolean useOnlyDirectories = HelperConverter.getBooleanFromByte(dos);
        return new FileBrowserElement(eck, id, rootfolder, useAllFiles, useOnlyDirectories, formID, this);
    }

    public String getObjectId(boolean post, InputElement el) {
        return ((FileBrowserElement) el).id;
    }

    public String getValue(boolean post, InputElement el2) {
        FileBrowserElement el = (FileBrowserElement) el2;
        return el.fileName;
    }

    public byte[] getByteValue(InputElement el2) {
        return getByteValue(el2, null);
    }

    public byte[] getByteValue(InputElement el2, RootThread thread) {
        FileBrowserElement el = (FileBrowserElement) el2;
        if (el.fileName == null) return null;
        if (el.selectOnlyDirectory) return el.fileName.getBytes();
        return HelperFileIO.getFileData(el.fileName, thread, null);
    }

    public String getFilename(InputElement el2) {
        FileBrowserElement el = (FileBrowserElement) el2;
        int p = el.fileName.lastIndexOf('/') + 1;
        if (p < 0) p = 0;
        return el.fileName.substring(p);
    }

    public String getContentType(InputElement el) {
        return ((FileBrowserElement) el).contentType;
    }

    /**
	 * sets the filename
	 */
    public void setValue(InputElement el2, byte[] newValue) {
        setValue(el2, new String(newValue));
    }

    public void setValue(InputElement el2, String newValue) {
        FileBrowserElement el = (FileBrowserElement) el2;
        el.fileName = newValue;
        HelperApp.cacheAppBinary(el.id + el.formID + "fn", newValue == null ? new byte[0] : newValue.getBytes(), el.contentType);
    }

    public int getClassID() {
        return CLASS_ID;
    }

    /**
	 * sollte das element geklickt worden sein, wird diese Methode aufgerufen
	 */
    public boolean pointerClicked(DisplayElement el2, int x, int y, DisplayCanvas dc, int index) {
        return doEvent(el2, dc);
    }

    /**
	 * sollte das Element selektiert sein, werden alle KeyEvents an dieses Ding weitergeleitet
	 * wird true zurückgegeben, werden keine weiteren Events mehr abgefragt
	 */
    public boolean keyPressed(DisplayElement el2, int key, DisplayCanvas dc) {
        if (key == HelperRMSStoreMLibera.appKeys[0] || key == InputHandler.DEF_KEYS[0]) return doEvent(el2, dc);
        return false;
    }

    /**
	 * führt das Event, welches zu dem Bild zugeordnet ist aus
	 * gibt true zurück, wenn alles richtig war, und das event ausgeführt wurde
	 */
    private boolean doEvent(DisplayElement el2, DisplayCanvas dc) {
        openFileBrowser(el2, dc);
        return false;
    }

    /**
	 * öffnet den Datei-Browser
	 * @param el2
	 * @param dc
	 */
    public void openFileBrowser(DisplayElement el2, Displayable dc) {
        try {
            FileBrowserElement el = (FileBrowserElement) el2;
            el.dc = dc;
            if (fs != null) fs.closePage();
            fs = new FileSelector(el, el.rootFolder);
            el.rootFolder = null;
        } catch (Exception e) {
            HelperStd.log(e, "doEvent");
        }
    }

    /**
	 * wird aufgerufen, wenn die Seitenelemente (für die aktuelle Seite) generiert weren
	 * @param dc
	 */
    public void init(DisplayElement el2, DisplayCanvas dc) {
        if (dc == null || dc.ps == null) return;
        FileBrowserElement el = (FileBrowserElement) el2;
        el.fileSize = HelperStd.parseLong(HelperProc.getStoredInputValue(-2, el.id + el.formID + "_sz", dc.ps), -1);
        el.fileName = HelperProc.getStoredInputValue(-2, el.id + el.formID + "_fn", dc.ps);
        el.contentType = HelperProc.getStoredInputValue(-2, el.id + el.formID + "_cn", dc.ps);
    }

    /**
	 * wird aufgerufen, wenn die Seite geschlossen wird
	 * @param dc
	 */
    public void closePage(DisplayElement el2, DisplayCanvas dc) {
        if (fs != null) fs.closePage();
        fs = null;
        if (dc == null || dc.ps == null) return;
        FileBrowserElement el = (FileBrowserElement) el2;
        cacheSelectedFile(el, dc);
    }

    public void cacheSelectedFile(FileBrowserElement el, DisplayCanvas dc) {
        if (el.selectOnlyDirectory) return;
        HelperProc.cacheFromInputValue(-2, el.id + el.formID + "_sz", null, el.fileSize + "", false, dc.ps);
        HelperProc.cacheFromInputValue(-2, el.id + el.formID + "_fn", null, el.fileName, false, dc.ps);
        HelperProc.cacheFromInputValue(-2, el.id + el.formID + "_cn", null, el.contentType, false, dc.ps);
        HelperProc.cacheFromInputValue(el.formID, el.id, null, "fileio", true, dc.ps);
    }

    /**
	 * die zugeordnete Form ID oder -1
	 * @param el
	 * @return
	 */
    public int getFormID(InputElement el) {
        return ((FileBrowserElement) el).formID;
    }
}
