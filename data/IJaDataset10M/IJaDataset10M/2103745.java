package org.opensourcephysics.frames;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.display2d.*;

/**
 * A DrawingFrame that displays 2D plots of scalar fields.
 *
 * @author W. Christian
 * @version 1.0
 */
public class Scalar2DFrame extends DrawingFrame {

    String plotType = "";

    int paletteType = ColorMapper.SPECTRUM;

    boolean showGrid = true;

    GridData gridData;

    Plot2D plot = new GridPlot(null);

    SurfacePlotMouseController surfacePlotMC;

    JMenuItem surfaceItem, contourItem, gridItem, interpolatedItem, grayscaleItem;

    GridTableFrame tableFrame;

    /**
    * Constructs a Scalar2DFrame with the given axes labels and frame title.
    * @param xlabel String
    * @param ylabel String
    * @param frameTitle String
    */
    public Scalar2DFrame(String xlabel, String ylabel, String frameTitle) {
        super(new PlottingPanel(xlabel, ylabel, null));
        drawingPanel.setPreferredSize(new Dimension(350, 350));
        setTitle(frameTitle);
        ((PlottingPanel) drawingPanel).getAxes().setShowMajorXGrid(false);
        ((PlottingPanel) drawingPanel).getAxes().setShowMajorYGrid(false);
        drawingPanel.addDrawable(plot);
        addMenuItems();
        setAnimated(true);
        setAutoclear(true);
    }

    /**
    * Constructs a Scalar2DFrame with the given frame title but without axes.
    * @param frameTitle String
    */
    public Scalar2DFrame(String frameTitle) {
        super(new InteractivePanel());
        setTitle(frameTitle);
        drawingPanel.addDrawable(plot);
        addMenuItems();
        setAnimated(true);
        setAutoclear(true);
    }

    /**
    * Gets the x coordinate for the given index.
    *
    * @param i int
    * @return double the x coordiante
    */
    public double indexToX(int i) {
        if (gridData == null) {
            throw new IllegalStateException("Data has not been set.  Invoke setAll before invoking this method.");
        }
        return gridData.indexToX(i);
    }

    /**
    * Gets the index that is closest to the given x value
    *
    * @return double the x coordiante
    */
    public int xToIndex(double x) {
        if (gridData == null) {
            throw new IllegalStateException("Data has not been set.  Invoke setAll before invoking this method.");
        }
        return gridData.xToIndex(x);
    }

    /**
    * Gets the index that is closest to the given y value
    *
    * @return double the y coordiante
    */
    public int yToIndex(double y) {
        if (gridData == null) {
            throw new IllegalStateException("Data has not been set.  Invoke setAll before invoking this method.");
        }
        return gridData.yToIndex(y);
    }

    /**
    * Gets the y coordinate for the given index.
    *
    * @param i int
    * @return double the y coordiante
    */
    public double indexToY(int i) {
        if (gridData == null) {
            throw new IllegalStateException("Data has not been set.  Invoke setAll before invoking this method.");
        }
        return gridData.indexToY(i);
    }

    /**
    * Gets the number of x entries.
    * @return nx
    */
    public int getNx() {
        if (gridData == null) {
            return 0;
        }
        return gridData.getNx();
    }

    /**
    * Gets the number of y entries.
    * @return nx
    */
    public int getNy() {
        if (gridData == null) {
            return 0;
        }
        return gridData.getNy();
    }

    /**
    * Sets the autoscale flag and the floor and ceiling values for the colors.
    *
    * If autoscaling is true, then the min and max values of z are span the colors.
    *
    * If autoscaling is false, then floor and ceiling values limit the colors.
    * Values below min map to the first color; values above max map to the last color.
    *
    * @param isAutoscale
    * @param floor
    * @param ceil
    */
    public void setZRange(boolean isAutoscale, double floor, double ceil) {
        plot.setAutoscaleZ(isAutoscale, floor, ceil);
    }

    /**
    * Gets the maximum z value of the plot.
    * @return zmax
    */
    public double getCeiling() {
        return plot.getCeiling();
    }

    /**
    * Gets the minimum z value of the plot.
    * @return zmin
    */
    public double getFloor() {
        return plot.getFloor();
    }

    /**
    * Gets the autoscale flag for z.
    *
    * @return boolean
   */
    public boolean isAutoscaleZ() {
        return plot.isAutoscaleZ();
    }

    /**
    * Sets the color palette that will be used to color the scalar field.  Palette types are defined in ColorMapper.
    * @param type
    */
    public void setPaletteType(int type) {
        paletteType = type;
        plot.setPaletteType(type);
    }

    /**
    * Sets the buffered image option.
    *
    * Buffered panels copy the offscreen image into the panel during a repaint unless the image
    * has been invalidated.  Use the render() method to draw the image immediately.
    *
    * @param b
    */
    public void setBuffered(boolean b) {
        drawingPanel.setBuffered(b);
    }

    /**
    * Outlines the data grid's boundaries.
    *
    * @param showGrid
   */
    public void setShowGrid(boolean show) {
        showGrid = show;
        plot.setShowGridLines(show);
    }

    /**
    * True if the data grid's boundaries are shown.
    *
    * @return showGrid
   */
    public boolean isShowGrid() {
        return showGrid;
    }

    /**
    * Adds Views menu items on the menu bar.
    */
    protected void addMenuItems() {
        JMenuBar menuBar = getJMenuBar();
        if (menuBar == null) {
            return;
        }
        JMenu helpMenu = this.removeMenu(DisplayRes.getString("DrawingFrame.Help_menu_item"));
        JMenu menu = getMenu(DisplayRes.getString("DrawingFrame.Views_menu"));
        if (menu == null) {
            menu = new JMenu(DisplayRes.getString("DrawingFrame.Views_menu"));
            menuBar.add(menu);
            menuBar.validate();
        } else {
            menu.addSeparator();
        }
        if (helpMenu != null) menuBar.add(helpMenu);
        ButtonGroup menubarGroup = new ButtonGroup();
        gridItem = new JRadioButtonMenuItem("Grid Plot");
        menubarGroup.add(gridItem);
        gridItem.setSelected(true);
        ActionListener tableListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                convertToGridPlot();
            }
        };
        gridItem.addActionListener(tableListener);
        menu.add(gridItem);
        contourItem = new JRadioButtonMenuItem("Contour Plot");
        menubarGroup.add(contourItem);
        tableListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                convertToContourPlot();
            }
        };
        contourItem.addActionListener(tableListener);
        menu.add(contourItem);
        surfaceItem = new JRadioButtonMenuItem("Surface Plot");
        menubarGroup.add(surfaceItem);
        tableListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                convertToSurfacePlot();
            }
        };
        surfaceItem.addActionListener(tableListener);
        menu.add(surfaceItem);
        interpolatedItem = new JRadioButtonMenuItem("Interpolated Plot");
        menubarGroup.add(interpolatedItem);
        tableListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                convertToInterpolatedPlot();
            }
        };
        interpolatedItem.addActionListener(tableListener);
        menu.add(interpolatedItem);
        grayscaleItem = new JRadioButtonMenuItem("Grayscale Plot");
        menubarGroup.add(grayscaleItem);
        tableListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                convertToGrayscalePlot();
            }
        };
        grayscaleItem.addActionListener(tableListener);
        menu.add(grayscaleItem);
        menu.addSeparator();
        JMenuItem tableItem = new JMenuItem(DisplayRes.getString("DrawingFrame.DataTable_menu_item"));
        tableItem.setAccelerator(KeyStroke.getKeyStroke('T', MENU_SHORTCUT_KEY_MASK));
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showDataTable(true);
            }
        };
        tableItem.addActionListener(actionListener);
        menu.add(tableItem);
        if ((drawingPanel != null) && (drawingPanel.getPopupMenu() != null)) {
            JMenuItem item = new JMenuItem(DisplayRes.getString("DrawingFrame.DataTable_menu_item"));
            item.addActionListener(actionListener);
            drawingPanel.getPopupMenu().add(item);
        }
    }

    /**
    * Clears drawable objects added by the user from this frame.
    */
    public void clearDrawables() {
        drawingPanel.clear();
        drawingPanel.addDrawable(plot);
    }

    /**
    * Gets Drawable objects added by the user to this frame.
    *
    * @return the list
    */
    public synchronized ArrayList getDrawables() {
        ArrayList list = super.getDrawables();
        list.remove(plot);
        return list;
    }

    /**
    * Gets Drawable objects added by the user of an assignable type. The list contains
    * objects that are assignable from the class or interface.
    *
    * @param c the type of Drawable object
    *
    * @return the cloned list
    *
    * @see #getObjectOfClass(Class c)
    */
    public synchronized ArrayList getDrawables(Class c) {
        ArrayList list = super.getDrawables(c);
        list.remove(plot);
        return list;
    }

    /**
    * Sets the scalar field to zero.
    */
    public void clearData() {
        if (gridData != null) {
            setAll(new double[gridData.getNx()][gridData.getNy()]);
        }
        drawingPanel.invalidateImage();
    }

    /**
    * Sets the type of plot type so that it cannot be changed from the menu.
    * @param type String
    */
    public void setPlotType(String type) {
        plotType = type;
        if (type.toLowerCase().equals("contour")) {
            convertToContourPlot();
            surfaceItem.setEnabled(false);
            contourItem.setEnabled(true);
            gridItem.setEnabled(false);
            interpolatedItem.setEnabled(false);
            grayscaleItem.setEnabled(false);
        } else if (type.toLowerCase().equals("grayscale")) {
            convertToGrayscalePlot();
            surfaceItem.setEnabled(false);
            contourItem.setEnabled(false);
            gridItem.setEnabled(false);
            interpolatedItem.setEnabled(false);
            grayscaleItem.setEnabled(true);
        } else if (type.toLowerCase().equals("grid")) {
            convertToGridPlot();
            surfaceItem.setEnabled(false);
            contourItem.setEnabled(false);
            gridItem.setEnabled(true);
            interpolatedItem.setEnabled(false);
            grayscaleItem.setEnabled(false);
        } else if (type.toLowerCase().equals("interpolated")) {
            convertToInterpolatedPlot();
            surfaceItem.setEnabled(false);
            contourItem.setEnabled(false);
            gridItem.setEnabled(false);
            interpolatedItem.setEnabled(true);
            grayscaleItem.setEnabled(false);
        } else if (type.toLowerCase().equals("surface")) {
            convertToSurfacePlot();
            surfaceItem.setEnabled(true);
            contourItem.setEnabled(false);
            gridItem.setEnabled(false);
            interpolatedItem.setEnabled(false);
            grayscaleItem.setEnabled(false);
        } else {
            plotType = "";
            surfaceItem.setEnabled(true);
            contourItem.setEnabled(true);
            gridItem.setEnabled(true);
            interpolatedItem.setEnabled(true);
            grayscaleItem.setEnabled(true);
        }
    }

    /**
    * Converts to a contour plot.
    */
    public void convertToContourPlot() {
        if (!(plot instanceof ContourPlot)) {
            if (surfacePlotMC != null) {
                drawingPanel.removeMouseListener(surfacePlotMC);
                drawingPanel.removeMouseMotionListener(surfacePlotMC);
                surfacePlotMC = null;
                drawingPanel.resetGutters();
                drawingPanel.setClipAtGutter(true);
                if (drawingPanel instanceof PlottingPanel) {
                    ((PlottingPanel) drawingPanel).getAxes().setVisible(true);
                }
                drawingPanel.setShowCoordinates(true);
            }
            boolean isAutoscaleZ = plot.isAutoscaleZ();
            double floor = plot.getFloor();
            double ceil = plot.getCeiling();
            Plot2D oldPlot = plot;
            plot = new ContourPlot(gridData);
            plot.setPaletteType(paletteType);
            plot.setAutoscaleZ(isAutoscaleZ, floor, ceil);
            drawingPanel.replaceDrawable(oldPlot, plot);
            plot.update();
            if ((tableFrame != null) && tableFrame.isShowing()) {
                tableFrame.refreshTable();
            }
            drawingPanel.repaint();
            contourItem.setSelected(true);
        }
    }

    public void convertToInterpolatedPlot() {
        if (!(plot instanceof InterpolatedPlot)) {
            if (surfacePlotMC != null) {
                drawingPanel.removeMouseListener(surfacePlotMC);
                drawingPanel.removeMouseMotionListener(surfacePlotMC);
                surfacePlotMC = null;
                drawingPanel.resetGutters();
                drawingPanel.setClipAtGutter(true);
                if (drawingPanel instanceof PlottingPanel) {
                    ((PlottingPanel) drawingPanel).getAxes().setVisible(true);
                }
                drawingPanel.setShowCoordinates(true);
            }
            boolean isAutoscaleZ = plot.isAutoscaleZ();
            double floor = plot.getFloor();
            double ceil = plot.getCeiling();
            Plot2D oldPlot = plot;
            plot = new InterpolatedPlot(gridData);
            plot.setPaletteType(paletteType);
            plot.setAutoscaleZ(isAutoscaleZ, floor, ceil);
            drawingPanel.replaceDrawable(oldPlot, plot);
            plot.update();
            if ((tableFrame != null) && tableFrame.isShowing()) {
                tableFrame.refreshTable();
            }
            drawingPanel.repaint();
            interpolatedItem.setSelected(true);
        }
    }

    public void convertToGridPlot() {
        if (!(plot instanceof GridPlot)) {
            if (surfacePlotMC != null) {
                drawingPanel.removeMouseListener(surfacePlotMC);
                drawingPanel.removeMouseMotionListener(surfacePlotMC);
                surfacePlotMC = null;
                drawingPanel.resetGutters();
                drawingPanel.setClipAtGutter(true);
                if (drawingPanel instanceof PlottingPanel) {
                    ((PlottingPanel) drawingPanel).getAxes().setVisible(true);
                }
                drawingPanel.setShowCoordinates(true);
            }
            boolean isAutoscaleZ = plot.isAutoscaleZ();
            double floor = plot.getFloor();
            double ceil = plot.getCeiling();
            Plot2D oldPlot = plot;
            plot = new GridPlot(gridData);
            plot.setShowGridLines(showGrid);
            plot.setPaletteType(paletteType);
            plot.setAutoscaleZ(isAutoscaleZ, floor, ceil);
            drawingPanel.replaceDrawable(oldPlot, plot);
            plot.update();
            if ((tableFrame != null) && tableFrame.isShowing()) {
                tableFrame.refreshTable();
            }
            drawingPanel.repaint();
            gridItem.setSelected(true);
        }
    }

    public void convertToGrayscalePlot() {
        if (!(plot instanceof GrayscalePlot)) {
            if (surfacePlotMC != null) {
                drawingPanel.removeMouseListener(surfacePlotMC);
                drawingPanel.removeMouseMotionListener(surfacePlotMC);
                surfacePlotMC = null;
                drawingPanel.resetGutters();
                drawingPanel.setClipAtGutter(true);
                if (drawingPanel instanceof PlottingPanel) {
                    ((PlottingPanel) drawingPanel).getAxes().setVisible(true);
                }
                drawingPanel.setShowCoordinates(true);
            }
            boolean isAutoscaleZ = plot.isAutoscaleZ();
            double floor = plot.getFloor();
            double ceil = plot.getCeiling();
            Plot2D oldPlot = plot;
            plot = new GrayscalePlot(gridData);
            plot.setPaletteType(paletteType);
            plot.setAutoscaleZ(isAutoscaleZ, floor, ceil);
            drawingPanel.replaceDrawable(oldPlot, plot);
            plot.update();
            if ((tableFrame != null) && tableFrame.isShowing()) {
                tableFrame.refreshTable();
            }
            drawingPanel.repaint();
            grayscaleItem.setSelected(true);
        }
    }

    /**
    * Converts to a SurfacePlot plot.
    */
    public void convertToSurfacePlot() {
        if (!(plot instanceof SurfacePlot)) {
            Plot2D oldPlot = plot;
            try {
                SurfacePlot newPlot = new SurfacePlot(gridData);
                if (drawingPanel instanceof PlottingPanel) {
                    String xLabel = ((PlottingPanel) drawingPanel).getAxes().getXLabel();
                    String yLabel = ((PlottingPanel) drawingPanel).getAxes().getYLabel();
                    newPlot.setAxisLabels(xLabel, yLabel, null);
                }
                plot = newPlot;
            } catch (IllegalArgumentException ex) {
                surfaceItem.setEnabled(false);
                gridItem.setSelected(true);
                convertToGridPlot();
                return;
            }
            if (drawingPanel instanceof PlottingPanel) {
                ((PlottingPanel) drawingPanel).getAxes().setVisible(false);
            }
            drawingPanel.setShowCoordinates(false);
            drawingPanel.setGutters(0, 0, 0, 0);
            drawingPanel.setClipAtGutter(false);
            boolean isAutoscaleZ = oldPlot.isAutoscaleZ();
            double floor = oldPlot.getFloor();
            double ceil = oldPlot.getCeiling();
            plot.setPaletteType(paletteType);
            plot.setAutoscaleZ(isAutoscaleZ, floor, ceil);
            drawingPanel.replaceDrawable(oldPlot, plot);
            plot.update();
            if ((tableFrame != null) && tableFrame.isShowing()) {
                tableFrame.refreshTable();
            }
            drawingPanel.repaint();
            if (surfacePlotMC == null) {
                surfacePlotMC = new SurfacePlotMouseController(drawingPanel, plot);
            }
            drawingPanel.addMouseListener(surfacePlotMC);
            drawingPanel.addMouseMotionListener(surfacePlotMC);
            surfaceItem.setSelected(true);
        }
    }

    /**
    * Resizes the number of columns and rows.
    *
    * @param nx int
    * @param ny int
    */
    public void resizeGrid(int nx, int ny) {
        double xmin, xmax, ymin, ymax;
        boolean cellScale = false;
        if (gridData == null) {
            xmin = drawingPanel.getPreferredXMin();
            xmax = drawingPanel.getPreferredXMax();
            ymin = drawingPanel.getPreferredYMin();
            ymax = drawingPanel.getPreferredYMax();
        } else {
            xmin = gridData.getLeft();
            xmax = gridData.getRight();
            ymin = gridData.getBottom();
            ymax = gridData.getTop();
            cellScale = gridData.isCellData();
        }
        gridData = new ArrayData(nx, ny, 1);
        gridData.setComponentName(0, "Amp");
        if (nx != ny) {
            surfaceItem.setEnabled(false);
            if (plot instanceof SurfacePlot) {
                convertToGridPlot();
            }
        } else {
            if (plotType.equals("")) {
                surfaceItem.setEnabled(true);
            }
        }
        if (cellScale) {
            gridData.setCellScale(xmin, xmax, ymin, ymax);
        } else {
            gridData.setScale(xmin, xmax, ymin, ymax);
        }
        plot.setGridData(gridData);
        plot.update();
        if ((tableFrame != null) && tableFrame.isShowing()) {
            tableFrame.refreshTable();
        }
        drawingPanel.invalidateImage();
        drawingPanel.repaint();
    }

    /**
    * Sets the data in the given row to new values.
    *
    * @param row  int
    * @param vals double[] new values
    * @throws IllegalArgumentException if array length does not match grid size.
    */
    public void setRow(int row, double[] vals) throws IllegalArgumentException {
        if (gridData.getNx() != vals.length) {
            throw new IllegalArgumentException("Row data length does not match grid size.");
        }
        double[] rowData = gridData.getData()[0][row];
        System.arraycopy(vals, 0, rowData, 0, vals.length);
        plot.update();
        if ((tableFrame != null) && tableFrame.isShowing()) {
            tableFrame.refreshTable();
        }
        drawingPanel.invalidateImage();
    }

    /**
    * Sets the scalar field's values and scale..
    *
    * @param vals int[][] the new values
    * @param xmin double
    * @param xmax double
    * @param ymin double
    * @param ymax double
    */
    public void setAll(double[][] vals, double xmin, double xmax, double ymin, double ymax) {
        setAll(vals);
        if (gridData.isCellData()) {
            gridData.setCellScale(xmin, xmax, ymin, ymax);
        } else {
            gridData.setScale(xmin, xmax, ymin, ymax);
        }
    }

    /**
    * Sets the scalar field's values.
    *
    * @param vals double[][] new values
    */
    public void setAll(double[][] vals) {
        if ((gridData == null) || (gridData.getNx() != vals.length) || (gridData.getNy() != vals[0].length)) {
            resizeGrid(vals.length, vals[0].length);
        }
        double[][] data = gridData.getData()[0];
        int ny = vals[0].length;
        for (int i = 0, nx = data.length; i < nx; i++) {
            System.arraycopy(vals[i], 0, data[i], 0, ny);
        }
        plot.update();
        if ((tableFrame != null) && tableFrame.isShowing()) {
            tableFrame.refreshTable();
        }
        drawingPanel.invalidateImage();
    }

    /**
    * Sets all the scalar field values using the given array.
    *
    * The array is assumed to contain numbers in row-major format.
    *
    * @param vals double[] field values
    */
    public void setAll(double[] vals) {
        if (gridData == null) {
            throw new IllegalArgumentException("Grid size must be set before using row-major format.");
        }
        int nx = gridData.getNx(), ny = gridData.getNy();
        if (vals.length != nx * ny) {
            throw new IllegalArgumentException("Grid does not have the correct size.");
        }
        double[][] mag = gridData.getData()[0];
        for (int j = 0; j < ny; j++) {
            int offset = j * nx;
            for (int i = 0; i < nx; i++) {
                mag[i][j] = vals[offset + i];
            }
        }
        plot.update();
        if ((tableFrame != null) && tableFrame.isShowing()) {
            tableFrame.refreshTable();
        }
        drawingPanel.invalidateImage();
    }

    /**
    * Shows or hides the data table.
    *
    * @param show boolean
    */
    public synchronized void showDataTable(boolean show) {
        if (show) {
            if (tableFrame == null || !tableFrame.isDisplayable()) {
                if (gridData == null) {
                    return;
                }
                tableFrame = new GridTableFrame(gridData);
                tableFrame.setTitle("Scalar Field Data");
                tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
            tableFrame.refreshTable();
            tableFrame.setVisible(true);
        } else {
            tableFrame.setVisible(false);
            tableFrame.dispose();
            tableFrame = null;
        }
    }
}
