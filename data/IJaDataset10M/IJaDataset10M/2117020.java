package com.rapidminer.gui.plotter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import com.rapidminer.datatable.DataTable;
import com.rapidminer.gui.plotter.conditions.ColumnsPlotterCondition;
import com.rapidminer.gui.plotter.conditions.PlotterCondition;

/**
 * <code>GridViz</code> is a simple extension of <code>RadViz</code> that
 * places the dimensional anchors on a rectangular grid instead of using the
 * perimeter of a circle. The number of dimensions that can be displayed
 * increases significantly.
 * 
 * @author Daniel Hakenjos, Ingo Mierswa
 */
public class GridVizPlotter extends RadVizPlotter {

    private static final long serialVersionUID = 9178351977037267613L;

    private static final int MAX_NUMBER_OF_COLUMNS = 10000;

    /** Constructs a new GridVizPlotter. */
    public GridVizPlotter(PlotterConfigurationModel settings) {
        super(settings);
    }

    /** Constructs a new GridVizPlotter. */
    public GridVizPlotter(PlotterConfigurationModel settings, DataTable dataTable) {
        super(settings);
        setDataTable(dataTable);
    }

    @Override
    public PlotterCondition getPlotterCondition() {
        return new ColumnsPlotterCondition(MAX_NUMBER_OF_COLUMNS);
    }

    protected void calculateAttributeVectors(int totalSize, int gridSize, int gridDelta) {
        int dim = this.dataTable.getNumberOfColumns();
        anchorVectorX = new double[dim];
        anchorVectorY = new double[dim];
        int gridX = 0;
        int gridY = totalSize;
        int counter = 0;
        for (int i = 0; i < this.dataTable.getNumberOfColumns(); i++) {
            if ((i == colorColumn) || (shouldIgnoreColumn(i))) continue;
            if (counter % gridSize == 0) {
                gridX = 0;
                gridY -= gridDelta;
            }
            anchorVectorX[i] = gridX;
            anchorVectorY[i] = gridY;
            gridX += gridDelta;
            counter++;
        }
    }

    @Override
    protected void paintPlotter(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.BLACK);
        int totalSize = Math.min(width, height) - 2 * MARGIN;
        int numberOfColumns = this.dataTable.getNumberOfColumns();
        if (colorColumn >= 0) numberOfColumns--;
        numberOfColumns -= ignoreList.getSelectedIndices().length;
        if (numberOfColumns == 0) return;
        int gridSize = (int) Math.ceil(Math.sqrt(numberOfColumns));
        int gridDelta = totalSize / gridSize;
        calculateAttributeVectors(totalSize, gridSize, gridDelta);
        g.setFont(LABEL_FONT);
        for (int i = 0; i < this.dataTable.getNumberOfColumns(); i++) {
            if ((i == colorColumn) || (shouldIgnoreColumn(i))) continue;
            int x = (int) (MARGIN + anchorVectorX[i]);
            int y = (int) (MARGIN + totalSize - anchorVectorY[i]);
            if (this.dataTable.isSupportingColumnWeights()) {
                g.setColor(getWeightColor(this.dataTable.getColumnWeight(columnMapping[i]), this.maxWeight));
                Rectangle2D weightRect = new Rectangle2D.Double(x, y - gridDelta, gridDelta, gridDelta);
                g.fill(weightRect);
            }
            g.setColor(GRID_COLOR);
            g.drawLine(x, y, x + gridDelta, y);
            g.drawLine(x, y, x, y - gridDelta);
            g.drawString(this.dataTable.getColumnName(columnMapping[i]), x + 5, y - 5);
        }
        calculateSamplePoints();
        int centerPoint = totalSize / 2 + MARGIN;
        Iterator<PlotterPoint> i = plotterPoints.iterator();
        ColorProvider colorProvider = getColorProvider();
        while (i.hasNext()) {
            drawPoint(g, i.next(), colorProvider, centerPoint, centerPoint, 1.0d);
        }
        if ((colorColumn != -1) && (plotterPoints.size() > 0)) {
            drawLegend(g, dataTable, colorColumn);
        }
    }
}
