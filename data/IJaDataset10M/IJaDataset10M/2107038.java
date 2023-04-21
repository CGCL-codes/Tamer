package ggc.gui.view;

import ggc.data.HbA1cValues;
import ggc.util.I18nControl;
import java.awt.*;
import java.text.DecimalFormat;

public class HbA1cView extends AbstractGraphView {

    private HbA1cValues hbValues = null;

    private I18nControl m_ic = I18nControl.getInstance();

    public HbA1cView() {
        super();
    }

    public HbA1cView(HbA1cValues hbValues) {
        this();
        setHbA1cValues(hbValues);
    }

    public void setHbA1cValues(HbA1cValues hbValues) {
        this.hbValues = hbValues;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oAA);
        g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, oCR);
        g2D.setRenderingHint(RenderingHints.KEY_DITHERING, oD);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oFM);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oI);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, oR);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oTAA);
        calculateSizes();
        drawValues(g2D);
    }

    @Override
    protected void drawFramework(Graphics2D g2D) {
    }

    @Override
    protected void drawValues(Graphics2D g2D) {
        g2D.setPaint(Color.white);
        g2D.fillRect(0, 0, viewWidth, viewHeight);
        int offset = 25;
        int width = viewWidth - 50;
        int CenterX = offset;
        int CenterY = offset;
        float arc = 0;
        float sumarc = 0;
        int h = viewHeight - 70;
        g2D.setPaint(Color.red);
        arc = hbValues.getPercentOfDaysInClass(0) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int) sumarc, (int) arc);
        sumarc += arc;
        g2D.fillRect(offset, h - 10, 12, 12);
        g2D.setPaint(Color.orange);
        arc = hbValues.getPercentOfDaysInClass(1) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int) sumarc, (int) arc);
        sumarc += arc;
        g2D.fillRect(offset, h + 5, 12, 12);
        g2D.setPaint(Color.yellow);
        arc = hbValues.getPercentOfDaysInClass(2) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int) sumarc, (int) arc);
        sumarc += arc;
        g2D.fillRect(offset, h + 20, 12, 12);
        g2D.setPaint(Color.green.brighter());
        arc = hbValues.getPercentOfDaysInClass(3) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int) sumarc, (int) arc);
        sumarc += arc;
        g2D.fillRect(offset, h + 35, 12, 12);
        g2D.setPaint(Color.green.darker());
        arc = hbValues.getPercentOfDaysInClass(4) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int) sumarc, (int) arc);
        g2D.fillRect(offset, h + 50, 12, 12);
        g2D.setPaint(Color.black);
        DecimalFormat df = new DecimalFormat("00.00");
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_0_1") + " [" + df.format(hbValues.getPercentOfDaysInClass(0) * 100) + " %]", offset + 20, h);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_2_3") + " [" + df.format(hbValues.getPercentOfDaysInClass(1) * 100) + " %]", offset + 20, h + 15);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_4_5") + " [" + df.format(hbValues.getPercentOfDaysInClass(2) * 100) + " %]", offset + 20, h + 30);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_6_7") + " [" + df.format(hbValues.getPercentOfDaysInClass(3) * 100) + " %]", offset + 20, h + 45);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_MORE_7") + "    [" + df.format(hbValues.getPercentOfDaysInClass(4) * 100) + " %]", offset + 20, h + 60);
    }
}
