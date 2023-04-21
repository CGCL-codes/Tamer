package kr.ac.ssu.imc.durubi.report.viewer.components.graphs;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;

public class DRGRegionChart2D extends DRGAxisChart2D {

    protected Color regionLineColor = Color.black;

    public DRGRegionChart2D(String sChartType, String[] series, String[] xData, double[][] yData) {
        super(sChartType, series, xData, yData);
    }

    public DRGRegionChart2D(DRGModel doc) {
        super(doc);
    }

    public float xChartToScreen(int x) {
        return (float) (xStart + x * (axisWidth / (length2 - 1)));
    }

    public void drawChart(Graphics2D g2) {
        if (this.axisOn) super.drawChart(g2);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.targetNum > 0 && this.targetDatas != null) super.drawTargetLines(g2);
        path = new GeneralPath[length1];
        for (int i = 0; i < length1; i++) {
            path[i] = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
            path[i].moveTo(xChartToScreen(0), yChartToScreen(yData[i][0]));
            for (int j = 0; j < length2 - 1; j++) path[i].lineTo(xChartToScreen(j + 1), yChartToScreen(yData[i][j + 1]));
            if (i == 0) {
                path[i].lineTo(xChartToScreen(length2 - 1), yChartToScreen(0.0));
                path[i].lineTo(xChartToScreen(0), yChartToScreen(0.0));
            } else {
                for (int k = length2 - 1; k > -1; k--) path[i].lineTo(xChartToScreen(k), yChartToScreen(yData[i - 1][k]));
            }
            path[i].closePath();
        }
        for (int i = length1 - 1; i > -1; i--) {
            g2.setColor(this.dataColor[i]);
            g2.fill(path[i]);
            g2.setColor(this.lineColors[i]);
            if (this.lineOn) g2.draw(path[i]);
        }
        if (this.dataLabelOn) {
            g2.setColor(Color.black);
            for (int i = 0; i < this.length1; i++) {
                for (int j = 0; j < this.length2; j++) {
                    String tempString = new Double(yData[i][j]).toString();
                    if (!this.dataForm.equals("")) tempString = DRGModel.applyNumberForm(tempString, this.dataForm); else {
                        if (!this.yStepStr.equals("")) tempString = DRGModel.applyDefaultForm(tempString);
                    }
                    if (sChartType.equals(DRGModel.CT_ACCUCOLUMN2D)) tempString = tempString.concat("%");
                    if (!(this.dataPrefix.equals(""))) tempString = this.dataPrefix + tempString;
                    if (!(this.dataPostfix.equals(""))) {
                        tempString += this.dataPostfix;
                    }
                    TextLayout yValText = new TextLayout(tempString, this.dataLabelFont, g2.getFontRenderContext());
                    this.rightMargin = Math.max(rightMargin, yValText.getBounds().getWidth());
                    float x = xChartToScreen(j) - yValText.getAdvance() / 2;
                    float y;
                    if (this.dataInOut.equalsIgnoreCase(DRGModel.DIO_OUT)) {
                        if (yData[i][j] > 0.0) {
                            y = yChartToScreen(yData[i][j]) + 1;
                            if (this.dotOn) y -= this.dotSizes[i] / 2;
                        } else {
                            y = yChartToScreen(yData[i][j]) + (float) (yValText.getBounds().getHeight()) + 1;
                            if (this.dotOn) y += this.dotSizes[i] / 2;
                        }
                    } else {
                        if (yData[i][j] < 0.0) {
                            y = yChartToScreen(yData[i][j]) + 1;
                            if (this.dotOn) y -= this.dotSizes[i] / 2;
                        } else {
                            y = yChartToScreen(yData[i][j]) + (float) (yValText.getBounds().getHeight()) + 1;
                            if (this.dotOn) y += this.dotSizes[i] / 2;
                        }
                    }
                    if (this.dataRotate) {
                        g2 = super.setRotate(g2, x, y);
                        if (this.dataInOut.equalsIgnoreCase(DRGModel.DIO_OUT)) {
                            if (yData[i][j] > 0.0) yValText.draw(g2, 0 - 2 - (float) yValText.getBounds().getWidth(), -1 - (float) yValText.getBounds().getWidth() / 2 + (float) yValText.getBounds().getHeight() / 2); else yValText.draw(g2, 0 + 2 - (float) yValText.getBounds().getHeight(), -1 - (float) yValText.getBounds().getWidth() / 2 + (float) yValText.getBounds().getHeight() / 2);
                        } else {
                            if (yData[i][j] < 0.0) yValText.draw(g2, 0 - 2 - (float) yValText.getBounds().getWidth(), -1 - (float) yValText.getBounds().getWidth() / 2 + (float) yValText.getBounds().getHeight() / 2); else yValText.draw(g2, 0 + 2 - (float) yValText.getBounds().getHeight(), -1 - (float) yValText.getBounds().getWidth() / 2 + (float) yValText.getBounds().getHeight() / 2);
                        }
                        g2 = super.setUnRotate(g2, x, y);
                    } else yValText.draw(g2, x, y);
                }
            }
        }
        super.drawTargetText(g2);
    }
}
