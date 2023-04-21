package ggc.gui.view;

import ggc.util.DataAccess;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

public abstract class AbstractGraphView extends JComponent {

    Object oAA, oCR, oTAA, oR, oD, oFM, oI;

    DataAccess m_da = DataAccess.getInstance();

    int BGunit, maxBG, minBG, BGDiff;

    int counter = 10;

    int upperSpace = 20, lowerSpace = 30, leftSpace = 40, rightSpace = 30;

    float maxGoodBG = m_da.getSettings().getBG_TargetHigh();

    float minGoodBG = m_da.getSettings().getBG_TargetLow();

    int viewWidth = 0;

    int viewHeight = 0;

    float drawableWidth = 0;

    float drawableHeight = 0;

    float dayWidthC = 0;

    float hourWidthC = 0;

    float minuteWidthC = 0;

    float dayWidth = 0;

    float hourWidth = 0;

    float minuteWidth = 0;

    int dayCount = 0;

    String unitLabel;

    public AbstractGraphView() {
        BGunit = m_da.getSettings().getBG_unit();
        switch(BGunit) {
            case DataAccess.BG_MMOL:
                maxBG = 44;
                minBG = 0;
                unitLabel = "mmol/l";
                break;
            case DataAccess.BG_MGDL:
            default:
                maxBG = 450;
                minBG = 0;
                unitLabel = "mg/dl";
                break;
        }
        BGDiff = maxBG - minBG;
        getRenderingQuality();
    }

    protected abstract void drawFramework(Graphics2D g2D);

    protected abstract void drawValues(Graphics2D g2D);

    protected void calculateSizes() {
        Dimension dim = getSize();
        viewWidth = dim.width;
        viewHeight = dim.height;
        drawableWidth = viewWidth - rightSpace - leftSpace;
        drawableHeight = viewHeight - lowerSpace - upperSpace;
        dayWidthC = drawableWidth / (dayCount > 0 ? dayCount : 1);
        hourWidthC = dayWidthC / 24;
        minuteWidthC = hourWidthC / 60;
        dayWidth = drawableWidth;
        hourWidth = dayWidth / 24;
        minuteWidth = hourWidth / 60;
    }

    protected int BGtoCoord(float BG) {
        return (int) (drawableHeight + upperSpace - drawableHeight / BGDiff * BG);
    }

    protected int BUtoCoord(float BU) {
        return (int) (drawableHeight + upperSpace - drawableHeight / 60 * BU);
    }

    protected int InstoCoord(float Ins) {
        return (int) (drawableHeight + upperSpace - drawableHeight / 60 * Ins);
    }

    protected int InsPerBUtoCoord(float factor) {
        return (int) (drawableHeight + upperSpace - drawableHeight / 4 * factor);
    }

    protected int DateTimetoCoord(long datetime) {
        return timeToCoordReal(datetime);
    }

    private int timeToCoordReal(long datetime) {
        String dt = m_da.getDateTimeAsTimeString(datetime);
        int timeH = Integer.parseInt(dt.substring(0, 2));
        int timeM = Integer.parseInt(dt.substring(0, 2));
        return (int) (leftSpace + timeH * hourWidthC + timeM * minuteWidthC);
    }

    protected int TimetoCoord(long datetime) {
        return timeToCoordReal(datetime);
    }

    public void setNewRenderingQuality() {
        getRenderingQuality();
    }

    private void getRenderingQuality() {
        switch(m_da.getSettings().getAntiAliasing()) {
            case 1:
                oAA = RenderingHints.VALUE_ANTIALIAS_OFF;
                break;
            case 2:
                oAA = RenderingHints.VALUE_ANTIALIAS_ON;
                break;
            default:
                oAA = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
        }
        switch(m_da.getSettings().getColorRendering()) {
            case 1:
                oCR = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
                break;
            case 2:
                oCR = RenderingHints.VALUE_COLOR_RENDER_SPEED;
                break;
            default:
                oCR = RenderingHints.VALUE_COLOR_RENDER_DEFAULT;
        }
        switch(m_da.getSettings().getDithering()) {
            case 1:
                oD = RenderingHints.VALUE_DITHER_DISABLE;
                break;
            case 2:
                oD = RenderingHints.VALUE_DITHER_ENABLE;
                break;
            default:
                oD = RenderingHints.VALUE_DITHER_DEFAULT;
        }
        switch(m_da.getSettings().getFractionalMetrics()) {
            case 1:
                oFM = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
                break;
            case 2:
                oFM = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
                break;
            default:
                oFM = RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
        }
        switch(m_da.getSettings().getInterpolation()) {
            case 1:
                oI = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
                break;
            case 2:
                oI = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
                break;
            default:
                oI = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        }
        switch(m_da.getSettings().getRendering()) {
            case 1:
                oR = RenderingHints.VALUE_RENDER_QUALITY;
                break;
            case 2:
                oR = RenderingHints.VALUE_RENDER_SPEED;
                break;
            default:
                oR = RenderingHints.VALUE_RENDER_DEFAULT;
        }
        switch(m_da.getSettings().getTextAntiAliasing()) {
            case 1:
                oTAA = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
                break;
            case 2:
                oTAA = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
                break;
            default:
                oTAA = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
        }
    }
}
