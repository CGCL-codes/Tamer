package CurveEditor.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.*;

public final class DrawArea extends JPanel {

    private static final long serialVersionUID = 1L;

    private static int CONTROLPOINTWIDTH = 2;

    private static int HOOVEREDCURVEWIDTH = 1;

    private static int DEFAULTCURVEWIDTH = 0;

    private int curveWidth = DEFAULTCURVEWIDTH;

    private int controlWidth = CONTROLPOINTWIDTH;

    private boolean coords;

    private boolean nrs;

    private boolean tangents;

    private boolean drawRectangle;

    private Vector<Curve> curves;

    private Vector<Curve> selectedCurves;

    private Vector<Curve> hooveredCurves;

    private Vector<Point> selectedPoints;

    private Vector<Point> hooveredPoints;

    private Graphics g;

    private Point runPoint;

    private int xBegin = -1, yBegin = -1, xEnd = -1, yEnd = -1;

    private DrawAreaProperties drawProp;

    public DrawArea(Vector<Curve> curves, Vector<Curve> selectedCurves, Vector<Curve> hooveredCurves, Vector<Point> selectedPoints, Vector<Point> hooveredPoints, DrawAreaProperties drawProp) throws InvalidArgumentException {
        this.drawProp = drawProp;
        init(curves, selectedCurves, hooveredCurves, selectedPoints, hooveredPoints, false, false, false);
    }

    public void init(Vector<Curve> curves, Vector<Curve> selectedCurves, Vector<Curve> hooveredCurves, Vector<Point> selectedPoints, Vector<Point> hooveredPoints, boolean coords, boolean nrs, boolean tangents) throws InvalidArgumentException {
        setSize();
        setBackground(new Color(255, 255, 255));
        if (curves == null || selectedCurves == null || hooveredCurves == null || selectedCurves == null || selectedPoints == null || hooveredPoints == null) throw new InvalidArgumentException("DrawArea.java - init(): Invalid Argument.");
        this.curves = curves;
        this.selectedCurves = selectedCurves;
        this.hooveredCurves = hooveredCurves;
        this.selectedPoints = selectedPoints;
        this.hooveredPoints = hooveredPoints;
        this.coords = coords;
        this.nrs = nrs;
        this.tangents = tangents;
        this.drawRectangle = true;
        this.repaint();
    }

    public boolean coords() {
        return coords;
    }

    public boolean nrs() {
        return nrs;
    }

    public boolean tangents() {
        return tangents;
    }

    public void toggleCoords() {
        coords = !coords;
    }

    public void toggleNrs() {
        nrs = !nrs;
    }

    public void toggleTangents() {
        tangents = !tangents;
    }

    public void update() {
        paint(g);
    }

    public void drawRunner(Point p) {
        runPoint = p;
        repaint();
    }

    public void paintComponent(Graphics g) {
        try {
            this.g = g;
            super.paintComponent(g);
            emptyField();
            this.g.setColor(Color.LIGHT_GRAY);
            if (drawRectangle) this.drawSelectionRectangle(); else this.drawArrow();
            this.g.setColor(drawProp.getColor(DrawAreaProperties.UNSELECTED_LINE));
            this.drawOutput(curves, false, false);
            this.g.setColor(drawProp.getColor(DrawAreaProperties.SELECTED_LINE));
            drawOutput(selectedCurves, coords, nrs);
            if (tangents) {
                this.g.setColor(drawProp.getColor(DrawAreaProperties.TANGENS));
                drawTangents(selectedCurves);
            }
            this.g.setColor(drawProp.getColor(DrawAreaProperties.SELECTED_POINT));
            drawSelectedPoints(selectedPoints);
            this.g.setColor(drawProp.getColor(DrawAreaProperties.HOOVERED_LINE));
            this.curveWidth = drawProp.getTickness(DrawAreaProperties.LINE);
            this.controlWidth = drawProp.getTickness(DrawAreaProperties.POINT);
            drawOutput(hooveredCurves, false, false);
            this.g.setColor(drawProp.getColor(DrawAreaProperties.HOOVERED_POINT));
            drawSelectedPoints(hooveredPoints);
            if (runPoint != null) {
                this.g.setColor(Color.CYAN);
                g.fillRect(runPoint.X() - 3 * controlWidth, runPoint.Y() - 3 * controlWidth, 6 * controlWidth + 1, 6 * controlWidth + 1);
                runPoint = null;
            }
            this.g.setColor(drawProp.getColor(DrawAreaProperties.UNSELECTED_LINE));
        } catch (InvalidArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Curve Editor - Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void emptyField() {
        try {
            g.clipRect(0, 0, DisplaySize.DRAWWIDTH, DisplaySize.DRAWHEIGHT);
            g.setColor(drawProp.getColor(DrawAreaProperties.BACKGROUND));
            g.fillRect(0, 0, DisplaySize.DRAWWIDTH, DisplaySize.DRAWHEIGHT);
            g.setColor(drawProp.getColor(DrawAreaProperties.UNSELECTED_LINE));
        } catch (InvalidArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Curve Editor - Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawSelectionRectangle() {
        if (xBegin != -1 && yBegin != -1 && xEnd != -1 && yEnd != -1) {
            int rectWidth = Math.abs(xBegin - xEnd);
            int rectHeigth = Math.abs(yBegin - yEnd);
            int yStart = (yBegin > yEnd) ? yEnd : yBegin;
            int xStart = (xBegin > xEnd) ? xEnd : xBegin;
            g.fillRect(xStart, yStart, rectWidth, rectHeigth);
        }
    }

    private void drawArrow() {
        if (xBegin != -1 && yBegin != -1 && xEnd != -1 && yEnd != -1) {
            g.drawLine(xBegin, yBegin, xEnd, yEnd);
            g.drawLine(xBegin, yBegin + 1, xEnd, yEnd + 1);
            g.drawLine(xBegin, yBegin - 1, xEnd, yEnd - 1);
        }
    }

    public void resetDragging() {
        xBegin = -1;
        yBegin = -1;
        xEnd = -1;
        yEnd = -1;
        drawRectangle = true;
    }

    public void beginSelectionRectangle(int x, int y) {
        xBegin = x;
        yBegin = y;
        xEnd = x;
        yEnd = y;
        drawRectangle = true;
    }

    public void beginMovingArrow(int x, int y) {
        xBegin = x;
        yBegin = y;
        xEnd = x;
        yEnd = y;
        drawRectangle = false;
    }

    public void updateDragging(int x, int y) {
        xEnd = x;
        yEnd = y;
    }

    public boolean draggingStarted() {
        return xBegin != -1 && yBegin != -1;
    }

    public int getXBegin() {
        return xBegin;
    }

    public int getYBegin() {
        return yBegin;
    }

    public int getXEnd() {
        return xEnd;
    }

    public int getYEnd() {
        return yEnd;
    }

    private void drawOutput(Vector<Curve> curves, boolean coords, boolean nrs) {
        Vector<Point> out;
        Vector<Point> in;
        for (int i = 0; i < curves.size(); ++i) {
            out = curves.get(i).getOutput();
            for (int j = 0; j < out.size() - 1; ++j) {
                Point p = out.get(j);
                Point p2 = out.get(j + 1);
                g.drawLine(out.get(j).X(), out.get(j).Y(), out.get(j + 1).X(), out.get(j + 1).Y());
                if (curveWidth != 0) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(curveWidth));
                    g2.drawLine(out.get(j).X(), out.get(j).Y(), out.get(j + 1).X(), out.get(j + 1).Y());
                }
            }
            in = curves.get(i).getInput();
            for (int j = 0; j < in.size(); ++j) {
                g.fillRect(in.get(j).X() - controlWidth, in.get(j).Y() - controlWidth, 2 * controlWidth + 1, 2 * controlWidth + 1);
                if (coords) g.drawString(in.get(j).X() + ", " + in.get(j).Y(), in.get(j).X() + 4, in.get(j).Y() + 10);
                if (nrs) g.drawString("C" + i + ", P" + j, in.get(j).X() + 4, in.get(j).Y() - 0);
            }
        }
    }

    private void drawTangents(Vector<Curve> curves) {
        for (int i = 0; i < curves.size(); ++i) {
            Vector<Point> vip = curves.get(i).getInput();
            if (curves.get(i).getType() == 'H') {
                for (int j = 0; j + 1 < vip.size(); j = j + 2) {
                    g.drawLine(vip.get(j).X(), vip.get(j).Y(), vip.get(j + 1).X(), vip.get(j + 1).Y());
                }
            } else if ((curves.get(i).getType() == 'B' || curves.get(i).getType() == 'C' || curves.get(i).getType() == 'G') && curves.get(i).getDegree() == 3) {
                for (int j = 0; j < vip.size(); j = j + 3) {
                    if (j + 1 < vip.size()) g.drawLine(vip.get(j).X(), vip.get(j).Y(), vip.get(j + 1).X(), vip.get(j + 1).Y());
                    if (j - 1 >= 0) g.drawLine(vip.get(j - 1).X(), vip.get(j - 1).Y(), vip.get(j).X(), vip.get(j).Y());
                }
            }
        }
    }

    private void drawSelectedPoints(Vector<Point> v) {
        for (int j = 0; j < v.size(); ++j) {
            g.fillRect(v.get(j).X() - (controlWidth + 1), v.get(j).Y() - (controlWidth + 1), 2 * (controlWidth + 1) + 1, 2 * (controlWidth + 1) + 1);
        }
    }

    public void setSize() {
        setBounds(DisplaySize.CHOICEWIDTH, 0, DisplaySize.DRAWWIDTH, DisplaySize.DRAWHEIGHT);
        repaint();
        updateUI();
    }

    public void saveToFile(String fileName, String extension) {
        BufferedImage bi = new BufferedImage(DisplaySize.DRAWWIDTH, DisplaySize.DRAWHEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bi.createGraphics();
        Graphics temp = g;
        paintComponent(ig2);
        g = temp;
        try {
            if (extension.compareToIgnoreCase("png") == 0 || extension.compareToIgnoreCase("gif") == 0) ImageIO.write(bi, extension.toUpperCase(), new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleNr() {
        nrs = !nrs;
    }
}
