package eu.irreality.dai.ui.simascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import eu.irreality.dai.ui.cell.DisplayableCharacter;
import eu.irreality.dai.ui.cell.DisplayableObject;
import eu.irreality.dai.ui.wrapper.Display;
import eu.irreality.dai.ui.wrapper.DisplayLayer;
import eu.irreality.dai.util.Debug;
import eu.irreality.dai.world.level.Level;

/**
 * Swing display simulating an ASCII terminal.
 */
public class SimulatedAsciiDisplay implements Display {

    private int rows;

    private int cols;

    private int pixelRows;

    private int pixelCols;

    private BufferedImage bi;

    private Graphics buffer;

    private FontMetrics fm;

    public SimulatedAsciiDisplay() {
    }

    private void initBuffer() {
        bi = new BufferedImage(pixelCols, pixelRows, BufferedImage.TYPE_INT_ARGB);
        buffer = bi.getGraphics();
    }

    private int rowToPixel(int row) {
        double pixelsPerRow = (double) pixelRows / (double) rows;
        if (row >= 0) {
            return (int) (((double) row) * pixelsPerRow);
        } else {
            return (int) (pixelRows + row * pixelsPerRow);
        }
    }

    private int colToPixel(int col) {
        double pixelsPerCol = (double) pixelCols / (double) cols;
        if (col >= 0) {
            return (int) (((double) col) * pixelsPerCol);
        } else {
            return (int) (pixelCols + col * pixelsPerCol);
        }
    }

    private void readyAsciiFont() {
        double pixelsPerRow = (double) pixelRows / (double) rows;
        double pixelsPerCol = (double) pixelCols / (double) cols;
        Font f;
        f = new java.awt.Font("Lucida Console", Font.PLAIN, 24);
        double sz = 5.0;
        int vert;
        int horiz;
        do {
            Font g = f.deriveFont((float) sz);
            fm = buffer.getFontMetrics(g);
            vert = fm.getHeight();
            horiz = fm.getMaxAdvance();
            sz = sz + 1.0;
            Debug.println("v" + vert + " h" + horiz);
            Debug.println("ppr " + pixelsPerRow + " ppc " + pixelsPerCol);
        } while (vert < pixelsPerRow && horiz < pixelsPerCol);
        Font theFont = f.deriveFont((float) (sz - 1.0));
        buffer.setFont(theFont);
        fm = buffer.getFontMetrics(theFont);
        Debug.println("Font readied to " + (sz - 1.0));
        Debug.println(fm.getHeight() + " " + pixelsPerRow + " " + fm.getMaxAdvance() + " " + pixelsPerCol);
    }

    public void setPixelSize(int pixelRows, int pixelCols) {
        if (this.pixelRows == pixelRows && this.pixelCols == pixelCols) return;
        this.pixelRows = pixelRows;
        this.pixelCols = pixelCols;
        initBuffer();
        readyAsciiFont();
    }

    public void addText(int row, int col, String text) {
        for (int i = 0; i < text.length(); i++) {
            textLayer.addContent(row, col + i, new DisplayableCharacter(text.charAt(i), Color.LIGHT_GRAY));
        }
    }

    public void setText(String text, Color color) {
        textLayer.setText(text, color, true);
    }

    public void appendText(String text, Color color) {
        textLayer.appendText(text, color);
    }

    public void setData(String key, String message, Color color) {
        SimulatedAsciiDisplayLayer dataLayer = getDataLayer(key);
        assert dataLayer != null : "No data layer under name " + key + " in display";
        dataLayer.setText(message, color, true);
    }

    protected void drawToBuffer(int row, int col, DisplayableObject dObj) {
        char ch = dObj.getChar();
        Color color = dObj.getColor();
        buffer.setColor(color);
        buffer.drawString(String.valueOf(ch), colToPixel(col), rowToPixel(row) + fm.getAscent());
        Debug.println(row + "," + col + ": " + colToPixel(col) + "," + rowToPixel(row));
    }

    private void refreshBuffer() {
        Debug.println("sizepix " + pixelRows + "," + pixelCols);
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0, 0, pixelCols + 1000, pixelRows + 1000);
        for (Iterator<SimulatedAsciiDisplayLayer> iter = displayLayers.iterator(); iter.hasNext(); ) {
            SimulatedAsciiDisplayLayer dl = iter.next();
            dl.drawGridToDisplay();
        }
    }

    public void refresh(Graphics graphics) {
        refreshBuffer();
        graphics.drawImage(bi, 0, 0, null);
    }

    private int viewCenterRow;

    private int viewCenterCol;

    public void setSize(int rows, int cols) {
        setSize(rows + 5, cols, 3, 0, rows, cols);
    }

    private List<SimulatedAsciiDisplayLayer> displayLayers = new LinkedList<SimulatedAsciiDisplayLayer>();

    private SimulatedAsciiDisplayLayer mainLayer;

    private SimulatedAsciiDisplayLayer textLayer;

    private Map<String, SimulatedAsciiDisplayLayer> dataLayers = new HashMap<String, SimulatedAsciiDisplayLayer>();

    public void addDataLayer(String ident, DisplayLayer dl) {
        dataLayers.put(ident, (SimulatedAsciiDisplayLayer) dl);
        displayLayers.add((SimulatedAsciiDisplayLayer) dl);
    }

    public SimulatedAsciiDisplayLayer getDataLayer(String ident) {
        return dataLayers.get(ident);
    }

    public void addDisplayLayer(DisplayLayer dl) {
        displayLayers.add((SimulatedAsciiDisplayLayer) dl);
    }

    public void setMainLayer(DisplayLayer dl) {
        mainLayer = (SimulatedAsciiDisplayLayer) dl;
    }

    public SimulatedAsciiDisplayLayer getMainLayer() {
        return mainLayer;
    }

    public void setTextLayer(DisplayLayer dl) {
        textLayer = (SimulatedAsciiDisplayLayer) dl;
    }

    public SimulatedAsciiDisplayLayer getTextLayer() {
        return textLayer;
    }

    public void setSize(int rows, int cols, int viewportFirstRow, int viewportFirstCol, int viewportRows, int viewportCols) {
        this.rows = rows;
        this.cols = cols;
        SimulatedAsciiDisplayLayer mainLayer = new SimulatedAsciiDisplayLayer(this, viewportFirstRow, viewportFirstCol, viewportRows, viewportCols);
        addDisplayLayer(mainLayer);
        setMainLayer(mainLayer);
        SimulatedAsciiDisplayLayer textLayer = new SimulatedAsciiDisplayLayer(this, 0, 0, 3, viewportCols);
        addDisplayLayer(textLayer);
        setTextLayer(textLayer);
        int firstStatusRow = viewportFirstRow + viewportRows;
        SimulatedAsciiDisplayLayer statusLayer = new SimulatedAsciiDisplayLayer(this, firstStatusRow, 0, 2, cols);
        statusLayer.setText("r:   , c:   ", Color.LIGHT_GRAY, true);
        addDisplayLayer(statusLayer);
        SimulatedAsciiDisplayLayer rLayer = new SimulatedAsciiDisplayLayer(this, firstStatusRow, 2, 1, 2);
        addDataLayer("r", rLayer);
        SimulatedAsciiDisplayLayer cLayer = new SimulatedAsciiDisplayLayer(this, firstStatusRow, 10, 1, 2);
        addDataLayer("c", cLayer);
        Debug.println("Grid initted " + rows + "," + cols);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void addContent(int row, int col, DisplayableObject dObj) {
        mainLayer.addContent(row, col, dObj);
    }

    public void setLevel(Level l) {
        mainLayer.setLevel(l);
    }

    public void clear() {
        for (Iterator iterator = displayLayers.iterator(); iterator.hasNext(); ) {
            SimulatedAsciiDisplayLayer dl = (SimulatedAsciiDisplayLayer) iterator.next();
            dl.clear();
        }
    }

    public void clearCell(int row, int col) {
        mainLayer.clearCell(row, col);
    }

    public void setCameraPoint(int row, int col) {
        mainLayer.setCameraPoint(row, col);
    }
}
