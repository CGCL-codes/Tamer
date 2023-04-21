package org.velma.plots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import com.lowagie.text.pdf.PdfContentByte;
import org.velma.data.MSASelection;

/**
 * An {@link InterPosPlotPanel} for displaying scattergrams. The dots of the
 * scattergram can be selected. This selection can be linked to other plots of
 * the same dataset.
 * 
 * @author Andy Walsh
 * @author Hyun Kyu Shim
 */
public class Scattergram extends InterPosPlotPanel {

    private static final long serialVersionUID = -7569876870015312726L;

    protected float xVals[], yVals[];

    protected float xMinVal, xMaxVal, yMinVal, yMaxVal;

    protected boolean positionFilter[];

    private Color selBoundColor, unselBoundColor, selFillColor, unselFillColor;

    public Scattergram(MSASelection selection, String viewName, String[] keyChain, Color selectionBoxColor, float xVals[], float yVals[]) {
        super(selection, viewName, keyChain, selectionBoxColor);
        this.xVals = xVals;
        this.yVals = yVals;
        selBoundColor = new Color(255, 75, 75, 120);
        unselBoundColor = new Color(105, 105, 105, 100);
        selFillColor = new Color(255, 75, 75, 90);
        unselFillColor = new Color(105, 105, 105, 30);
        this.setToolTipText(viewName);
        calculateBounds();
        positionFilter = new boolean[selection.getPosCount()];
        for (int i = 0; i < positionFilter.length; i++) positionFilter[i] = true;
    }

    public String getToolTipText(MouseEvent e) {
        int xPos = e.getX();
        int yPos = e.getY();
        int eps = 3;
        int index;
        for (index = 0; index < xVals.length; index++) if ((Math.abs(plotToGraphX(xVals[index]) - xPos) < eps) && (Math.abs(plotToGraphY(yVals[index]) - yPos) < eps)) return "x=" + xVals[index] + ", y=" + yVals[index] + ", position=" + (index + 1);
        return null;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color gColor = g2d.getColor();
        super.paint(g2d);
        boolean[] selected = selection.getPosSelected();
        final Float nan = Float.valueOf(Float.NaN);
        int x, y;
        for (int i = 0; i < xVals.length; i++) if (!selected[i] && positionFilter[i] && !nan.equals(xVals[i]) && !nan.equals(yVals[i])) {
            g2d.setColor(unselFillColor);
            g2d.fillOval(x = plotToGraphX(xVals[i]) - 3, y = plotToGraphY(yVals[i]) - 3, 6, 6);
            g2d.setColor(unselBoundColor);
            g2d.drawOval(x, y, 6, 6);
        }
        for (int i = 0; i < xVals.length; i++) if (selected[i] && positionFilter[i] && !nan.equals(xVals[i]) && !nan.equals(yVals[i])) {
            g2d.setColor(selFillColor);
            g2d.fillOval(x = plotToGraphX(xVals[i]) - 3, y = plotToGraphY(yVals[i]) - 3, 6, 6);
            g2d.setColor(selBoundColor);
            g2d.drawOval(x, y, 6, 6);
        }
        if (isSelecting) paintSelectionBox(g2d);
        g2d.setColor(gColor);
        g2d.dispose();
    }

    public void paintPDF(PdfContentByte cb) {
        super.paintPDF(cb);
        boolean[] selected = selection.getPosSelected();
        for (int i = 0; i < xVals.length; i++) if (!selected[i] && positionFilter[i] && Float.NaN != xVals[i] && Float.NaN != yVals[i]) {
            cb.setColorFill(unselFillColor);
            cb.circle(plotToPdfX(xVals[i]), plotToPdfY(yVals[i]), 2);
            cb.fill();
        }
        for (int i = 0; i < xVals.length; i++) if (selected[i] && positionFilter[i] && Float.NaN != xVals[i] && Float.NaN != yVals[i]) {
            cb.setColorFill(selFillColor);
            cb.circle(plotToPdfX(xVals[i]), plotToPdfY(yVals[i]), 2);
            cb.fill();
        }
    }

    public void saveToDataFile(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            String endl = System.getProperty("line.separator");
            writer.write("X\tY" + endl);
            for (int i = 0; i < xVals.length; i++) writer.write(xVals[i] + "\t" + yVals[i] + endl);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calcSelected() {
        selection.clearSelection(MSASelection.MODE_POSITION);
        boolean[] selected = selection.getPosSelected();
        float lowX = graphToPlotX(Math.min(boxStartX, boxStopX)), highX = graphToPlotX(Math.max(boxStartX, boxStopX)), lowY = graphToPlotY(Math.max(boxStartY, boxStopY)), highY = graphToPlotY(Math.min(boxStartY, boxStopY));
        boolean inBox = false;
        for (int i = 0; i < selected.length; i++) {
            inBox = (xVals[i] >= lowX && xVals[i] <= highX && yVals[i] >= lowY && yVals[i] <= highY);
            selected[i] = inBox ? !selected[i] : selected[i] && addToSelection;
        }
    }

    protected void calculateBounds() {
        float dupXVals[] = new float[xVals.length];
        for (int i = 0; i < xVals.length; i++) dupXVals[i] = xVals[i];
        Arrays.sort(dupXVals);
        xMinVal = dupXVals[0];
        int k = xVals.length - 1;
        while (Float.valueOf(Float.NaN).equals(dupXVals[k])) k--;
        xMaxVal = dupXVals[k];
        float dupYVals[] = new float[yVals.length];
        for (int i = 0; i < yVals.length; i++) dupYVals[i] = yVals[i];
        Arrays.sort(dupYVals);
        yMinVal = dupYVals[0];
        k = yVals.length - 1;
        while (Float.valueOf(Float.NaN).equals(dupYVals[k])) k--;
        yMaxVal = dupYVals[k];
        xMin = xMinVal;
        xMax = xMaxVal;
        yMin = yMinVal;
        yMax = yMaxVal;
        if (xMax > 0) xAbove0 = true; else {
            xAbove0 = false;
            xMax = 0;
        }
        if (xMin < 0) xBelow0 = true; else {
            xBelow0 = false;
            xMin = 0;
        }
        if (yMax > 0) yAbove0 = true; else {
            yAbove0 = false;
            yMax = 0;
        }
        if (yMin < 0) yBelow0 = true; else {
            yBelow0 = false;
            yMin = 0;
        }
        calculateTickWH();
        if (xBelow0) xLeftBorder = 10; else xLeftBorder = yTickWidth;
        xRightBorder = 10;
        yTopBorder = 10;
        if (yBelow0) yBottomBorder = 10; else yBottomBorder = xTickHeight;
        diffX = xMax - xMin;
        diffY = yMax - yMin;
    }

    public void setPositionFilter(boolean positionFilter[]) {
        this.positionFilter = positionFilter;
        repaint();
    }
}
