package com.elibera.m.mle.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import de.enough.polish.android.lcdui.Font;
import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.android.lcdui.Image;
import com.elibera.m.display.DisplayCanvas;
import com.elibera.m.mle.HelperRMSStoreLearningObject;
import com.elibera.m.utils.HelperApp;
import com.elibera.m.utils.HelperConverter;
import com.elibera.m.xml.PageSuite;
import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.xml.proc.ProcVierEckElement;

public class ProcResultElement extends ProcVierEckElement {

    public static int CLASS_ID = 304;

    public static int BORDER = 2;

    public void doPaint(DisplayElement el2, Graphics g, int offsetX, int offsetY, DisplayCanvas dc) {
        ResultElement el = (ResultElement) el2;
        int[] eckpunkte = el.eckpunkte;
        Image img = el.img;
        System.out.println("points:" + el.msg + ";" + el.percent + "," + el.img);
        Font f = g.getFont();
        g.setFont(XMLTagQuestion.FONT_RESULT);
        g.drawImage(img, eckpunkte[0] + getLastLineWidth(el2) / 2 + offsetX, eckpunkte[1] + offsetY, Graphics.TOP | Graphics.HCENTER);
        g.drawString(el.msg, eckpunkte[0] + getLastLineWidth(el2) / 2 + offsetX, eckpunkte[3] - BORDER + offsetY, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(el.percent + "%", eckpunkte[0] + getLastLineWidth(el2) / 2 + offsetX, eckpunkte[1] + img.getHeight() / 2 + offsetY - 4, Graphics.HCENTER | Graphics.TOP);
        g.setFont(f);
    }

    public byte[] getSerialisedBytes(DisplayElement el2) {
        ResultElement el = (ResultElement) el2;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        HelperConverter.writeByte(el.eckpunkte, dos);
        HelperConverter.writeByte(el.msgs, dos);
        HelperConverter.writeByte(el.msgPoints, dos);
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
        String[] ch = HelperConverter.getStringArrayFromByte(dos);
        int[] msgPoints = HelperConverter.getIntArrayFromByte(dos);
        return new ResultElement(eck, HelperApp.getSystemImage(HelperApp.IMAGE_QUESTION_RESULT), ch, msgPoints, this);
    }

    public int getClassID() {
        return CLASS_ID;
    }

    public void init(DisplayElement el2, DisplayCanvas dc) {
        super.init(el2, dc);
        ResultElement el = (ResultElement) el2;
        int maxPoints = HelperRMSStoreLearningObject.getMaxReachablePointsOfSolvedQuestions(dc.ps);
        int reachedPoints = HelperRMSStoreLearningObject.getReachedPointsOfSolvedQuestions(dc.ps);
        if (reachedPoints > 0 && maxPoints > 0) el.percent = 100 * reachedPoints / maxPoints; else el.percent = 0;
        System.out.println("points:" + maxPoints + ";" + reachedPoints + "," + el.percent);
        for (int i = 0; i < el.msgs.length - 1; i++) {
            if (i < el.msgPoints.length && el.percent > el.msgPoints[i]) {
                el.msg = el.msgs[i];
                return;
            }
        }
        if (el.percent < el.msgPoints[el.msgPoints.length - 1]) el.msg = el.msgs[el.msgs.length - 1];
    }
}
