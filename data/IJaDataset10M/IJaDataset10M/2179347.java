package kr.ac.ssu.imc.whitehole.report.viewer.rdobjects;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Calendar;
import java.text.DateFormat;
import kr.ac.ssu.imc.whitehole.report.viewer.*;

public class RDOHeader extends JComponent {

    private RVGDDoc tGDDoc;

    private Point tHeaderLoc;

    private Dimension tHeaderSize;

    public int nHeaderTimes;

    private static final int HD_HEIGHT = 10;

    private static final int HD_FONTSIZE = 11;

    private static final String HD_FONT = "����";

    /** �Ӹ����� ���Ѵ�.
   *  @param tGDDoc ���� ��ü.
   */
    public RDOHeader(RVGDDoc tGDDoc) {
        this.tGDDoc = tGDDoc;
        nHeaderTimes = 1;
    }

    public RDOHeader getAClone(int nTimes) {
        RDOHeader tNewObject = new RDOHeader(tGDDoc);
        tNewObject.nHeaderTimes = nTimes;
        return tNewObject;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension tSize = this.getSize();
        int nCount = 0;
        if (tGDDoc.bHFDate) {
            g2d.setColor(Color.black);
            Font tFont = new Font(HD_FONT, Font.PLAIN, HD_FONTSIZE * nHeaderTimes);
            g2d.setFont(tFont);
            Calendar tCurrentDate = Calendar.getInstance();
            DateFormat tDateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
            String sDate = tDateFormat.format(tCurrentDate.getTime());
            Rectangle2D tBoundsRect = tFont.getStringBounds(sDate, g2d.getFontRenderContext());
            g2d.drawString(sDate, (int) (tSize.width - tBoundsRect.getWidth() - 10 * nHeaderTimes), (int) (tSize.height - 2 * nHeaderTimes - (HD_FONTSIZE + 1) * nCount * nHeaderTimes));
            nCount++;
        }
        if (tGDDoc.bHFHeader && tGDDoc.sHFHeader != null && tGDDoc.sHFHeader.length() > 0) {
            g2d.setColor(Color.black);
            Font tFont = new Font(HD_FONT, Font.PLAIN, HD_FONTSIZE * nHeaderTimes);
            g2d.setFont(tFont);
            Rectangle2D tBoundsRect = tFont.getStringBounds(tGDDoc.sHFHeader, g2d.getFontRenderContext());
            g2d.drawString(tGDDoc.sHFHeader, (int) (tSize.width - tBoundsRect.getWidth() - 10 * nHeaderTimes), (int) (tSize.height - 2 * nHeaderTimes - (HD_FONTSIZE + 1) * nCount * nHeaderTimes));
        }
    }
}
