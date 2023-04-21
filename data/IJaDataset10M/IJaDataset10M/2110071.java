package com.elibera.m.xml.proc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import de.enough.polish.android.lcdui.Font;
import de.enough.polish.android.lcdui.Graphics;
import com.elibera.m.app.MLE;
import com.elibera.m.xml.HelperXMLParser;
import com.elibera.m.display.DisplayCanvas;
import com.elibera.m.display.HelperDisplay;
import com.elibera.m.events.HelperEvent;
import com.elibera.m.events.InputHandler;
import com.elibera.m.rms.HelperRMSStoreMLibera;
import com.elibera.m.utils.HelperConverter;
import com.elibera.m.xml.PageSuite;
import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.xml.display.ImageElement;
import com.elibera.m.xml.display.ImageTextElement;
import com.elibera.m.xml.display.TextElement;

/**
 * @author meisi
 *
 */
public class ProcImageTextElement extends ProcMultiEckElement {

    public static MLE midlet;

    public static final int CLASS_ID = 7;

    public int getClassID() {
        return CLASS_ID;
    }

    public ProcImageTextElement() {
        midlet = MLE.midlet;
    }

    public void doPaint(DisplayElement el2, Graphics g, int offsetX, int offsetY, DisplayCanvas dc) {
        int oldc = g.getColor();
        ImageTextElement el = (ImageTextElement) el2;
        if (el.font != null) g.setFont(el.font);
        if (el.internalLineHeight <= 0) el.internalLineHeight = g.getFont().getHeight();
        if (el.selected || el.bgColor >= 0) {
            if (el.selected) {
                g.setColor(HelperDisplay.COLOR_MENU_ELEMENT_SELECTED_BG);
            } else g.setColor(el.bgColor);
            int[][] eck = el.linesEckpunkte;
            int starti = 0, lastY = -1, ax, aw;
            for (int i = 0; i <= eck.length; i++) {
                if (i == eck.length || (lastY < eck[i][1] && i > 0)) {
                    ax = offsetX + eck[starti][0] - (starti > 0 ? 1 : 0);
                    aw = eck[i - 1][2] - eck[starti][0] + (starti > 0 ? 1 : 0);
                    g.fillRoundRect(ax, offsetY + eck[starti][1], aw, eck[i - 1][3] - eck[starti][1], 10, 10);
                    if (el.buttonStyle) {
                        int oc = g.getColor();
                        g.setColor(0);
                        g.drawRoundRect(ax, offsetY + eck[starti][1], aw, eck[i - 1][3] - eck[starti][1], 10, 10);
                        g.setColor(oc);
                    }
                    starti = i;
                }
                if (i < eck.length) lastY = eck[i][1];
            }
            g.setColor(oldc);
        }
        HelperXMLParser.procImg.doPaint(el.img, g, offsetX, offsetY + 1, dc);
        if (el.color >= 0) g.setColor(el.color); else g.setColor(HelperDisplay.COLOR_MENU_TXT);
        if (el.selected) g.setColor(HelperDisplay.COLOR_MENU_SELECTED_TXT);
        HelperXMLParser.procText.doPaint(el.text, g, offsetX, offsetY, dc);
    }

    public boolean keyPressed(DisplayElement el2, int key, DisplayCanvas dc) {
        ImageTextElement el = (ImageTextElement) el2;
        int[] appKeys = HelperRMSStoreMLibera.appKeys;
        if (el.event != null && (key == InputHandler.DEF_KEYS[0] || key == appKeys[0])) {
            el.event.doEvent(dc);
            return true;
        }
        return false;
    }

    public boolean pointerClicked(DisplayElement el2, int x, int y, DisplayCanvas dc, int index) {
        ImageTextElement el = (ImageTextElement) el2;
        if (el.event != null) {
            el.event.doEvent(dc);
            return true;
        }
        return false;
    }

    public byte[] getSerialisedBytes(DisplayElement el2) {
        ImageTextElement el = (ImageTextElement) el2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        HelperConverter.writeByte(HelperXMLParser.procImg.getSerialisedBytes(el.img), dos);
        HelperConverter.writeByte(HelperXMLParser.procText.getSerialisedBytes(el.text), dos);
        HelperConverter.writeByte(el.event.getClassID(), dos);
        HelperConverter.writeByte(el.event.getBytes(), dos);
        HelperConverter.writeByte(el.linesEckpunkte, dos);
        HelperConverter.writeByte(el.font == null ? -1 : el.font.getFace(), dos);
        HelperConverter.writeByte(el.font == null ? -1 : el.font.getStyle(), dos);
        HelperConverter.writeByte(el.font == null ? -1 : el.font.getSize(), dos);
        HelperConverter.writeByte(el.color, dos);
        HelperConverter.writeByte(el.bgColor, dos);
        HelperConverter.writeByte(el.fullLine, dos);
        HelperConverter.writeByte(el.buttonStyle, dos);
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
        byte[] img = HelperConverter.getByteFromStream(dos);
        byte[] text = HelperConverter.getByteFromStream(dos);
        int evID = HelperConverter.getIntFromByte(dos);
        byte[] ev = HelperConverter.getByteFromStream(dos);
        int[][] eck = HelperConverter.getIntIntArrayFromByte(dos);
        int face = HelperConverter.getIntFromByte(dos);
        int style = HelperConverter.getIntFromByte(dos);
        int size = HelperConverter.getIntFromByte(dos);
        int color = HelperConverter.getIntFromByte(dos);
        int bgcolor = HelperConverter.getIntFromByte(dos);
        boolean fullLine = HelperConverter.getBooleanFromByte(dos);
        boolean buttonStyle = HelperConverter.getBooleanFromByte(dos);
        Font font = null;
        if (face != -1) font = Font.getFont(face, style, size);
        ImageTextElement el = new ImageTextElement((ImageElement) HelperXMLParser.procImg.getDisplayElement(img, ps), (TextElement) HelperXMLParser.procText.getDisplayElement(text, ps), eck, HelperEvent.getEvent(evID, ev), font, color, bgcolor, fullLine, buttonStyle, this);
        return el;
    }

    public void init(DisplayElement el2, DisplayCanvas dc) {
        ImageTextElement el = (ImageTextElement) el2;
        if (el.event != null) el.event.initEvent(dc, el);
        if (el.fullLine) {
            int[][] eck = el.linesEckpunkte;
            for (int i = 0; i < eck.length - 1; i++) {
                if (eck[i][1] < eck[i + 1][1]) eck[i][2] = HelperDisplay.getUseableCanvasWidth(dc);
            }
            eck[eck.length - 1][2] = HelperDisplay.getUseableCanvasWidth(dc);
            el.linesEckpunkte = eck;
        }
    }

    public void resetYPosition(DisplayElement el2, int newY) {
        ImageTextElement el = (ImageTextElement) el2;
        el.text.proc.resetYPosition(el.text, newY);
        el.img.proc.resetYPosition(el.img, newY);
        calculateRootEckPosition(el);
    }

    public void resetLastLineHeight(DisplayElement el2, int height) {
        ImageTextElement el = (ImageTextElement) el2;
        el.text.proc.resetLastLineHeight(el.text, height);
        el.img.proc.resetLastLineHeight(el.img, height);
        calculateRootEckPosition(el);
    }

    public void resetXPositionForAlign(DisplayElement el2, int canvasWidth, int XFromRight) {
        ImageTextElement el = (ImageTextElement) el2;
        el.text.proc.resetXPositionForAlign(el.text, canvasWidth, XFromRight);
        if (el.text.linesEckpunkte[el.text.linesEckpunkte.length - 1][1] == el.img.eckpunkte[1]) {
            int w = el.img.proc.getLastLineWidth(el.img);
            el.img.eckpunkte[2] = el.text.linesEckpunkte[0][0] - 2;
            el.img.eckpunkte[0] = el.img.eckpunkte[2] - w;
        }
        calculateRootEckPosition(el);
    }

    public void calculateRootEckPosition(ImageTextElement el) {
        int[][] eck = new int[el.text.linesEckpunkte.length + 1][];
        eck[0] = new int[4];
        System.arraycopy(el.img.eckpunkte, 0, eck[0], 0, 4);
        for (int i = 0; i < el.text.linesEckpunkte.length; i++) {
            eck[i + 1] = el.text.linesEckpunkte[i];
        }
        el.linesEckpunkte = eck;
    }

    public int getLastLineWidth(DisplayElement el2) {
        ImageTextElement el = (ImageTextElement) el2;
        int[][] eck = el.linesEckpunkte;
        int pos = eck.length - 1;
        if (eck[0][1] == eck[pos][1]) pos = 0;
        return eck[eck.length - 1][2] - eck[pos][0];
    }

    protected void drawOutline(DisplayElement el2, Graphics g, int offsetX, int offsetY) {
    }
}
