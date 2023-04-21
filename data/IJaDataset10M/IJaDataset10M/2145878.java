package com.onyourmind.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BubbleCanvas extends ChartCanvas {

    private static final long serialVersionUID = -1791169088363173194L;

    public double[][][] chartData = { { { 6.0, 0.0, 10.2 }, { 150.0, 145.0, 160.0 }, { 45.01, 166.0, 37.6 }, { 242.5, 233.0, 61.4 }, { 119.13, 333.0, 22.4 } } };

    public String[][] bubbleLabels = { { "Humulin 80/20 U-100 Vial", "Humulin 90/10 U-100 Vial", "Humulin Lente U-100 Vial", "Humulin NPH 3.0ml Cartridge", "Humulin Regular 3.0ml Cartridge" } };

    public String[] legendLabels;

    public void drawCanvas(Graphics g) {
        if (chartData.length == 0 || chartData[0].length == 0) isClearChart = true;
        super.drawCanvas(g);
        if (chartData.length == 0 || chartData[0].length == 0) drawNoDataNote(g);
        if (isClearChart) return;
        chartTop = drawChartTitle(g) - 10;
        ResourceBundle rb = ResourceBundle.getBundle("i18n.LabelBundle", getLocale());
        chartBottom = drawLegend(g, rb.getString("Legend"));
        chartBottom = drawXAxisLabel(g, false, null, false);
        chartBottom += 10;
        chartRight = getSize().width - 20;
        chartLeft = drawYAxisLabel(g);
        boundsAndScale();
        g.setColor(Color.black);
        g.setFont(new Font("Helv", Font.PLAIN, 14));
        drawYAxis(g, 3, 3, Color.black);
        drawXAxis(g, false, true);
        drawData(g);
    }

    public void findBounds() {
        bound[0] = (int) chartData[0][0][0];
        bound[1] = (int) chartData[0][0][0];
        bound[2] = (int) chartData[0][0][1];
        bound[3] = (int) chartData[0][0][1];
        for (int nSeries = 0; nSeries < chartData.length; nSeries++) {
            for (int nRow = 0; nRow < chartData[nSeries].length; nRow++) {
                for (int nCol = 0; nCol < 2; nCol++) {
                    int nValue = (int) chartData[nSeries][nRow][nCol];
                    boolean bInteger = (Math.abs(chartData[nSeries][nRow][nCol] - (float) nValue) == 0.0);
                    if (nValue + 1 > bound[nCol * 2]) bound[nCol * 2] = bInteger ? nValue : ((nValue > 0) ? nValue + 1 : nValue);
                    if (nValue - 1 < bound[nCol * 2 + 1]) bound[nCol * 2 + 1] = bInteger ? nValue : ((nValue < 0) ? nValue - 1 : nValue);
                }
            }
        }
    }

    public void resetBounds() {
        super.resetBounds();
        int nMultiplier = 3;
        for (int nType = 0; nType < 4; nType = nType + 2) {
            if (bound[nType] < bound[nType + 1]) {
                bound[nType] -= nMultiplier * scale[nType];
                bound[nType + 1] += nMultiplier * scale[nType];
            } else {
                bound[nType] += nMultiplier * scale[nType];
                bound[nType + 1] -= nMultiplier * scale[nType];
            }
        }
    }

    public void drawData(Graphics g) {
        double fMaxBubbleSize = chartData[0][0][2];
        for (int nSeries = 0; nSeries < chartData.length; nSeries++) for (int nRow = 0; nRow < chartData[nSeries].length; nRow++) if (chartData[nSeries][nRow][2] > fMaxBubbleSize) fMaxBubbleSize = chartData[nSeries][nRow][2];
        int nChartWidth = chartRight - chartLeft;
        int nChartHeight = chartBottom - chartTop;
        int nChartMinDim = nChartHeight;
        if (nChartWidth < nChartHeight) nChartMinDim = nChartWidth;
        double fSizeScale = (3.0 / 4.0) * nChartMinDim / fMaxBubbleSize;
        g.setClip(chartLeft + 2, chartTop, nChartWidth - 2, nChartHeight - 2);
        java.util.List<int[]> usedLabelPos = new ArrayList<int[]>();
        for (int nSeries = 0; nSeries < chartData.length; nSeries++) {
            int[] nXPos = new int[chartData[nSeries].length];
            int[] nYPos = new int[chartData[nSeries].length];
            int[] nSize = new int[chartData[nSeries].length];
            g.setColor(getSeriesColor(nSeries));
            for (int nRow = 0; nRow < chartData[nSeries].length; nRow++) {
                nXPos[nRow] = chartLeft + (int) (xPixelsPerUnit * (chartData[nSeries][nRow][0] - xStart));
                nYPos[nRow] = chartBottom - (int) (yPixelsPerUnit * (chartData[nSeries][nRow][1] - yStart));
                nSize[nRow] = (int) (chartData[nSeries][nRow][2] * fSizeScale);
                g.fillOval(nXPos[nRow] - nSize[nRow] / 2, nYPos[nRow] - nSize[nRow] / 2, nSize[nRow], nSize[nRow]);
            }
            g.setFont(new Font("Helv", Font.PLAIN, 12));
            g.setColor(Color.black);
            for (int nRow = 0; nRow < chartData[nSeries].length; nRow++) {
                g.drawOval(nXPos[nRow] - nSize[nRow] / 2, nYPos[nRow] - nSize[nRow] / 2, nSize[nRow], nSize[nRow]);
                if (nRow < bubbleLabels[nSeries].length) {
                    int[] nXYPos = getFreeXYPos(usedLabelPos, nXPos[nRow] + nSize[nRow] / 2 + 2, nYPos[nRow]);
                    g.drawString(bubbleLabels[nSeries][nRow], nXYPos[0], nXYPos[1]);
                }
            }
        }
        g.setClip(0, 0, getSize().width, getSize().height);
    }

    public int[] getFreeXYPos(java.util.List<int[]> list, int nXPos, int nYPos) {
        int nYFurthestDistance = 0;
        boolean bUp = true;
        for (int[] nXYPos : list) {
            if (Math.abs(nXYPos[0] - nXPos) < 30) {
                int nYDistance = nXYPos[1] - nYPos;
                int nYAbsDistance = Math.abs(nYDistance);
                if (nYAbsDistance >= nYFurthestDistance) {
                    nYFurthestDistance = nYAbsDistance;
                    bUp = (nYDistance < 0);
                }
            }
        }
        if (!bUp) nYPos = nYPos - nYFurthestDistance - 10; else nYPos = nYPos + nYFurthestDistance;
        int[] nXYPos = { nXPos, nYPos };
        list.add(nXYPos);
        return nXYPos;
    }

    public int drawLegend(Graphics g, String strLegendTitle) {
        if (chartData.length < 2) return (getSize().height);
        int[] nMarkerWidth = new int[chartData.length];
        for (int i = 0; i < nMarkerWidth.length; i++) nMarkerWidth[i] = 4;
        return drawLegend(g, strLegendTitle, legendLabels, nMarkerWidth, true, true);
    }

    public void drawSecondaryLegendMarker(Graphics g, int nXPos, int nYPos, int nItem) {
    }

    public void drawMarker(int nType, Graphics g, int nX, int nY, int nHalf, Color pColor) {
        drawSquareMark(g, nX, nY, nHalf, pColor);
    }

    public Color getMarkerColor(int i) {
        return getSeriesColor(i);
    }
}
