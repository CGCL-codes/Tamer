package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import calc.Function;
import calc.Var;
import calc.Calc;

public class Graph3D extends SubPanel {

    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    /**
		 * 
		 */
    private double X_MAX, X_MIN, Y_MAX, Y_MIN, Z_MAX, Z_MIN, Z_STEP, X_STEP, Y_STEP, X_PIXEL, Y_PIXEL, THETA_STEP, THETA_MIN, THETA_MAX, POL_STEP, POL_AX_STEP;

    private double xRot, yRot, zRot, vX, vY, vZ, cX, cY, cZ;

    public int X_SIZE, Y_SIZE, LINE_SIZE, LINE_SIZE_DEFAULT, NUM_FREQ, mouseX, mouseY, mouseRefX, mouseRefY, NUM_GRAPHS;

    private boolean refPoint;

    private Calc CURRCALC;

    private NewCalc calcObj;

    private JPanel graph;

    private Function[] functions;

    public Graph3D(int xSize, int ySize, NewCalc currCalcObj) {
        calcObj = currCalcObj;
        X_SIZE = xSize;
        Y_SIZE = ySize;
        NUM_GRAPHS = 6;
        NUM_FREQ = 2;
        LINE_SIZE = 2;
        LINE_SIZE_DEFAULT = 2;
        X_MAX = 10;
        X_MIN = -10;
        Y_MAX = 10;
        Y_MIN = -10;
        X_STEP = 1;
        Y_STEP = 1;
        Z_STEP = 1;
        POL_AX_STEP = 1;
        X_PIXEL = (X_MAX - X_MIN) / X_SIZE;
        Y_PIXEL = (Y_MAX - Y_MIN) / Y_SIZE;
        POL_STEP = Math.PI / 360;
        xRot = 0;
        yRot = 0;
        zRot = 0;
        vX = 20;
        vY = 20;
        vZ = 20;
        cX = 4;
        cY = 4;
        cZ = 4;
        CURRCALC = calcObj.getBasicCalcObj();
        functions = new Function[NUM_GRAPHS];
        for (int i = 0; i < NUM_GRAPHS; i++) {
            functions[i] = new Function(CURRCALC);
        }
        graph = new SubPanel() {

            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                X_SIZE = graph.getSize().width;
                Y_SIZE = graph.getSize().height;
                g.fillRect(0, 0, X_SIZE, Y_SIZE);
                if ((X_MAX - X_MIN) / X_STEP >= 24) {
                    CURRCALC.getVarList().setVarVal("xStep", (int) (X_MAX - X_MIN) / 20);
                    X_STEP = CURRCALC.getVarList().getVarVal("xStep");
                } else if ((X_MAX - X_MIN) / X_STEP <= 16 && X_STEP >= 2) {
                    CURRCALC.getVarList().setVarVal("xStep", (int) (X_MAX - X_MIN) / 20);
                    X_STEP = CURRCALC.getVarList().getVarVal("xStep");
                }
                if ((Y_MAX - Y_MIN) / Y_STEP >= 24) {
                    CURRCALC.getVarList().setVarVal("yStep", (int) (Y_MAX - Y_MIN) / 20);
                    Y_STEP = CURRCALC.getVarList().getVarVal("yStep");
                } else if ((Y_MAX - Y_MIN) / Y_STEP <= 16 && Y_STEP >= 2) {
                    CURRCALC.getVarList().setVarVal("yStep", (int) (Y_MAX - Y_MIN) / 20);
                    Y_STEP = CURRCALC.getVarList().getVarVal("yStep");
                }
                X_PIXEL = (X_MAX - X_MIN) / X_SIZE;
                Y_PIXEL = (Y_MAX - Y_MIN) / Y_SIZE;
                draw3dAxis(g);
                Function f = null;
                for (int i = 0; i < NUM_GRAPHS; i++) {
                    f = functions[i];
                    if (f != null) {
                        if (f.isGraphing()) {
                            if (f.getGraphType() == 1) {
                                graphCart(f, g);
                            } else if (f.getGraphType() == 2) {
                                graphPolar(f, g);
                            } else if (f.getGraphType() == 4) {
                                graphCart(f, g);
                            }
                        }
                    }
                }
                if (refPoint) drawMousePlacement(g);
            }
        };
        graph.addMouseListener(new MouseListener() {

            private int xStart, yStart;

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                refPoint = true;
                mouseRefX = e.getX();
                mouseRefY = e.getY();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                refPoint = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        graph.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                CURRCALC.getVarList().updateVarVal("xMin", (mouseX - e.getX()) * X_PIXEL);
                CURRCALC.getVarList().updateVarVal("xMax", (mouseX - e.getX()) * X_PIXEL);
                CURRCALC.getVarList().updateVarVal("yMin", (e.getY() - mouseY) * Y_PIXEL);
                CURRCALC.getVarList().updateVarVal("yMax", (e.getY() - mouseY) * Y_PIXEL);
                repaint();
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseRefX = e.getX();
                mouseRefY = e.getY();
                repaint();
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
            }
        });
        GridBagConstraints bCon = new GridBagConstraints();
        bCon.fill = GridBagConstraints.BOTH;
        bCon.weightx = 1;
        bCon.weighty = 1;
        bCon.gridheight = 7;
        bCon.gridwidth = 1;
        bCon.gridx = 0;
        bCon.gridy = 0;
        this.add(graph, bCon);
        this.setPreferredSize(new Dimension(xSize, ySize));
        SubPanel props = new SubPanel();
        OCButton xRotPlus = new OCButton("xRot+", 1, 1, 0, 1, props, calcObj) {

            public void associatedAction() {
                xRot += .1;
                graph.repaint();
            }
        };
        OCButton xRotMin = new OCButton("xRot-", 1, 1, 0, 0, props, calcObj) {

            public void associatedAction() {
                xRot -= .1;
                graph.repaint();
            }
        };
        OCButton yRotPlus = new OCButton("yRot+", 1, 1, 1, 1, props, calcObj) {

            public void associatedAction() {
                yRot += .1;
                graph.repaint();
            }
        };
        OCButton yRotMin = new OCButton("yRot-", 1, 1, 1, 0, props, calcObj) {

            public void associatedAction() {
                yRot -= .1;
                graph.repaint();
            }
        };
        OCButton zRotPlus = new OCButton("zRot+", 1, 1, 2, 1, props, calcObj) {

            public void associatedAction() {
                zRot += .1;
                graph.repaint();
            }
        };
        OCButton zRotMin = new OCButton("zRot-", 1, 1, 2, 0, props, calcObj) {

            public void associatedAction() {
                zRot -= .1;
                graph.repaint();
            }
        };
        OCButton def = new OCButton("def", 1, 1, 3, 0, props, calcObj) {

            public void associatedAction() {
                xRot = 0;
                yRot = 0;
                zRot = 0;
                vX = 20;
                vY = 20;
                vZ = 20;
                cX = 4;
                cY = 4;
                cZ = 4;
                graph.repaint();
            }
        };
        bCon.fill = GridBagConstraints.BOTH;
        bCon.weightx = .1;
        bCon.weighty = .1;
        bCon.gridheight = 1;
        bCon.gridwidth = 1;
        bCon.gridx = 0;
        bCon.gridy = 7;
        this.add(props, bCon);
        this.setPreferredSize(new Dimension(xSize, ySize));
    }

    public Function[] getFunctions() {
        return functions;
    }

    public void setLineSize(int sizeInPixels) {
        LINE_SIZE = sizeInPixels;
        return;
    }

    private void drawMousePlacement(Graphics g) {
        float currX = (float) ((mouseRefX * X_PIXEL) + X_MIN);
        float currY = (float) (Y_MAX - (mouseRefY * Y_PIXEL));
        String ptText = "(" + currX + ", " + currY + ")";
        int width = g.getFontMetrics().stringWidth(ptText);
        g.setColor(Color.white);
        g.fillRect(X_SIZE - width - 5, Y_SIZE - 20, width, 12);
        g.setColor(Color.black);
        g.drawString(ptText, X_SIZE - width - 5, Y_SIZE - 10);
    }

    private void ptOn(double a, double b, Graphics g) {
        if (a <= X_MAX && a >= X_MIN && b <= Y_MAX && b >= Y_MIN) {
            g.fillRect(roundDouble((a - X_MIN) / X_PIXEL) - LINE_SIZE, (Y_SIZE - LINE_SIZE) - roundDouble((b - Y_MIN) / Y_PIXEL), LINE_SIZE, LINE_SIZE);
        }
    }

    private int gridXPtToScreen(double x) {
        return roundDouble((x - X_MIN) / X_PIXEL) - LINE_SIZE;
    }

    private int gridYPtToScreen(double y) {
        return (Y_SIZE - LINE_SIZE) - roundDouble((y - Y_MIN) / Y_PIXEL);
    }

    private void polPtOn(double r, double theta, Graphics g) {
        ptOn(r * Math.cos(theta), r * Math.sin(theta), g);
    }

    private void drawPolarAxis(Graphics g) {
        setLineSize(1);
        int numCircles = 0;
        if (X_MAX > Y_MAX || X_MAX > Math.abs(Y_MIN)) numCircles = (int) Math.abs(X_MAX - X_MIN); else if (Y_MAX > X_MAX || Y_MAX > Math.abs(X_MIN)) {
            numCircles = (int) Math.abs(Y_MAX - Y_MIN);
        } else numCircles = (int) X_MAX;
        int i = roundDouble(Y_MIN);
        for (; i <= numCircles; i++) {
            double currT = 0;
            double lastX = i * POL_AX_STEP;
            double lastY = 0, currX, currY;
            for (int j = 1; j < 240; j++) {
                currT += POL_STEP * 2;
                currX = i * Math.cos(currT);
                currY = i * Math.sin(currT);
                drawLineSeg(lastX, lastY, currX, currY, Color.gray, g);
                lastX = currX;
                lastY = currY;
            }
        }
    }

    private void draw3dAxis(Graphics g) {
        g.setColor(Color.BLUE);
        for (double i = 0; i < 20; i += .01) {
            plot3dPoint(i, 0, 0, g);
        }
        g.drawString("x", 380, 220);
        g.setColor(Color.GREEN);
        for (double i = 0; i < 20; i += .01) {
            plot3dPoint(0, i, 0, g);
        }
        g.drawString("y", 390, 220);
        g.setColor(Color.RED);
        for (double i = 0; i < 5; i += .01) {
            plot3dPoint(0, 0, i, g);
        }
        g.drawString("z", 400, 220);
    }

    private void drawAxis(Graphics g) {
        double tempY = (int) (Y_MIN / Y_STEP);
        tempY *= Y_STEP;
        int width;
        int height;
        if (X_MIN <= 0 && X_MAX >= 0) {
            while (tempY < Y_MAX) {
                setLineSize(1);
                drawLineSeg(X_MIN, tempY, X_MAX, tempY, Color.GRAY, g);
                setLineSize(LINE_SIZE_DEFAULT);
                drawLineSeg(2 * LINE_SIZE * X_PIXEL, tempY, -1 * LINE_SIZE * X_PIXEL, tempY, Color.BLACK, g);
                if (tempY % (NUM_FREQ * Y_STEP) == 0 && tempY != 0) {
                    String ptText = Double.toString(tempY);
                    width = g.getFontMetrics().stringWidth(ptText);
                    height = g.getFontMetrics().getHeight();
                    g.setColor(Color.white);
                    g.fillRect(gridXPtToScreen(0) - width - 4, gridYPtToScreen(tempY) - 4, width, 11);
                    g.setColor(Color.black);
                    g.drawString(ptText, gridXPtToScreen(0) - width - 4, gridYPtToScreen(tempY) + 6);
                }
                tempY += Y_STEP;
            }
        } else {
            if (X_MIN >= 0) {
                while (tempY < Y_MAX) {
                    setLineSize(1);
                    drawLineSeg(X_MIN, tempY, X_MAX, tempY, Color.GRAY, g);
                    setLineSize(LINE_SIZE_DEFAULT);
                    g.setColor(Color.BLACK);
                    ptOn(X_MIN + 2 * X_PIXEL, tempY, g);
                    if (tempY % (NUM_FREQ * Y_STEP) == 0 && tempY != 0) {
                        String ptText = Double.toString(tempY);
                        width = g.getFontMetrics().stringWidth(ptText);
                        height = g.getFontMetrics().getHeight();
                        g.setColor(Color.white);
                        g.fillRect(gridXPtToScreen(X_MIN) + 8, gridYPtToScreen(tempY) - 4, width, 11);
                        g.setColor(Color.black);
                        g.drawString(ptText, gridXPtToScreen(X_MIN) + 8, gridYPtToScreen(tempY) + 6);
                    }
                    tempY += Y_STEP;
                }
            } else if (X_MAX <= 0) {
                while (tempY < Y_MAX) {
                    setLineSize(1);
                    drawLineSeg(X_MIN, tempY, X_MAX, tempY, Color.GRAY, g);
                    setLineSize(LINE_SIZE_DEFAULT);
                    g.setColor(Color.BLACK);
                    ptOn(X_MAX - 1 * X_PIXEL, tempY, g);
                    if (tempY % (NUM_FREQ * Y_STEP) == 0 && tempY != 0) {
                        String ptText = Double.toString(tempY);
                        width = g.getFontMetrics().stringWidth(ptText);
                        height = g.getFontMetrics().getHeight();
                        g.setColor(Color.white);
                        g.fillRect(gridXPtToScreen(X_MAX) - width - 4, gridYPtToScreen(tempY) - 4, width, 11);
                        g.setColor(Color.black);
                        g.drawString(ptText, gridXPtToScreen(X_MAX) - width - 4, gridYPtToScreen(tempY) + 6);
                    }
                    tempY += Y_STEP;
                }
            }
        }
        double tempX = (int) (X_MIN / X_STEP);
        tempX *= X_STEP;
        height = g.getFontMetrics().getHeight();
        int tempWidth = g.getFontMetrics().stringWidth(Double.toString(tempX)) + 20;
        if (tempWidth > (int) ((X_MAX - X_MIN) / (X_STEP * NUM_FREQ))) {
            NUM_FREQ = (int) (((X_MAX - X_MIN) / (X_STEP)) / ((X_SIZE) / tempWidth)) + 1;
        }
        if (Y_MIN <= 0 && Y_MAX >= 0) {
            while (tempX < X_MAX) {
                setLineSize(1);
                drawLineSeg(tempX, Y_MIN, tempX, Y_MAX, Color.GRAY, g);
                setLineSize(LINE_SIZE_DEFAULT);
                drawLineSeg(tempX, 2 * LINE_SIZE * Y_PIXEL, tempX, -1 * LINE_SIZE * Y_PIXEL, Color.BLACK, g);
                if (tempX % (NUM_FREQ * X_STEP) == 0 && tempX != 0) {
                    String ptText = Double.toString(tempX);
                    width = g.getFontMetrics().stringWidth(ptText);
                    height = g.getFontMetrics().getHeight();
                    g.setColor(Color.white);
                    g.fillRect(gridXPtToScreen(tempX) - (width / 2), gridYPtToScreen(0) - 18, width + 2, height - 4);
                    g.setColor(Color.black);
                    g.drawString(ptText, gridXPtToScreen(tempX) - (width / 2), gridYPtToScreen(0) - 8);
                }
                tempX += X_STEP;
            }
        } else {
            if (Y_MIN >= 0) {
                while (tempX < X_MAX) {
                    setLineSize(1);
                    drawLineSeg(tempX, Y_MIN, tempX, Y_MAX, Color.GRAY, g);
                    setLineSize(LINE_SIZE_DEFAULT);
                    g.setColor(Color.BLACK);
                    ptOn(tempX, Y_MIN, g);
                    if (tempX % (NUM_FREQ * X_STEP) == 0 && tempX != 0) {
                        String ptText = Double.toString(tempX);
                        width = g.getFontMetrics().stringWidth(ptText);
                        height = g.getFontMetrics().getHeight();
                        g.setColor(Color.white);
                        g.fillRect(gridXPtToScreen(tempX) - (width / 2), gridYPtToScreen(Y_MIN) - 18, width + 2, height - 4);
                        g.setColor(Color.black);
                        g.drawString(ptText, gridXPtToScreen(tempX) - (width / 2), gridYPtToScreen(Y_MIN) - 8);
                    }
                    tempX += X_STEP;
                }
            }
            if (Y_MAX <= 0) {
                while (tempX < X_MAX) {
                    setLineSize(1);
                    drawLineSeg(tempX, Y_MIN, tempX, Y_MAX, Color.GRAY, g);
                    setLineSize(LINE_SIZE_DEFAULT);
                    g.setColor(Color.BLACK);
                    ptOn(tempX, Y_MAX - 1 * LINE_SIZE * Y_PIXEL, g);
                    if (tempX % (NUM_FREQ * X_STEP) == 0 && tempX != 0) {
                        String ptText = Double.toString(tempX);
                        width = g.getFontMetrics().stringWidth(ptText);
                        height = g.getFontMetrics().getHeight();
                        g.setColor(Color.white);
                        g.fillRect(gridXPtToScreen(tempX) - (width / 2), gridYPtToScreen(Y_MAX) + 12, width + 2, height - 4);
                        g.setColor(Color.black);
                        g.drawString(ptText, gridXPtToScreen(tempX) - (width / 2), gridYPtToScreen(Y_MAX) + 22);
                    }
                    tempX += X_STEP;
                }
            }
        }
        if (X_MIN <= 0 && X_MAX >= 0) drawLineSeg(0, Y_MIN, 0, Y_MAX, Color.BLACK, g);
        if (Y_MIN <= 0 && Y_MAX >= 0) drawLineSeg(X_MIN, 0, X_MAX, 0, Color.BLACK, g);
    }

    private void drawLineSeg(double x1, double y1, double x2, double y2, Color color, Graphics g) {
        g.setColor(color);
        double smallX = 0, bigX = 0, smallY = 0, bigY = 0;
        if (x1 == x2) {
            if (y1 > y2) {
                bigY = y1;
                smallY = y2;
            } else {
                bigY = y2;
                smallY = y1;
            }
            while (smallY <= bigY - LINE_SIZE * Y_PIXEL) {
                ptOn(x1, smallY, g);
                smallY += Y_PIXEL;
            }
        } else if (y1 == y2) {
            if (x1 > x2) {
                bigX = x1;
                smallX = x2;
            } else {
                bigX = x2;
                smallX = x1;
            }
            while (smallX <= bigX - LINE_SIZE * X_PIXEL) {
                ptOn(smallX, y1, g);
                smallX += X_PIXEL;
            }
        } else {
            double slope = (y2 - y1) / (x2 - x1);
            double b = y1 - (x1 * slope);
            if (Math.abs(slope) > 1) {
                if (y1 > y2) {
                    bigX = x1;
                    bigY = y1;
                    smallX = x2;
                    smallY = y2;
                } else {
                    bigX = x2;
                    bigY = y2;
                    smallX = x1;
                    smallY = y1;
                }
                if (smallY < Y_MIN) smallY = Y_MIN;
                smallX = (smallY - b) / slope;
                while (smallY <= Y_MAX && smallY <= bigY) {
                    ptOn(smallX, smallY, g);
                    smallX += Y_PIXEL / slope;
                    smallY += Y_PIXEL;
                }
            } else {
                if (x1 > x2) {
                    bigX = x1;
                    bigY = y1;
                    smallX = x2;
                    smallY = y2;
                } else {
                    bigX = x2;
                    bigY = y2;
                    smallX = x1;
                    smallY = y1;
                }
                if (smallX < X_MIN) smallX = X_MIN;
                smallY = smallX * slope + b;
                while (smallX <= X_MAX && smallX <= bigX) {
                    ptOn(smallX, smallY, g);
                    smallX += X_PIXEL;
                    smallY += X_PIXEL * slope;
                }
            }
        }
    }

    public int roundDouble(double a) {
        if (a % 1 >= .5) return (int) a + 1; else return (int) a;
    }

    public void graphCart(Function f, Graphics g) {
        String eqtn = f.getFuncEqtn();
        Var ind = f.getIndependentVar();
        Var dep = f.getDependentVar();
        Color color = f.getColor();
        boolean tracing = f.isTracingPt();
        double tracePt = f.getTraceVal();
        boolean isConnected = f.isConneted();
        boolean takeIntegral = f.isTakingIntegral();
        double a = f.getStartIntegral();
        double b = f.getEndIntegral();
        double derivative = f.getDerivative();
        boolean deriving = f.isDeriving();
        double lastX, lastY, currX, currY;
        g.setColor(color);
        ind.setValue(X_MIN);
        CURRCALC.parse(eqtn);
        CURRCALC.solve();
        lastX = ind.getValue();
        lastY = dep.getValue();
        for (int i = 1; i < X_SIZE; i += 2) {
            ind.updateValue(2 * X_PIXEL);
            CURRCALC.solve();
            currX = ind.getValue();
            currY = dep.getValue();
            ptOn(currX, currY, g);
            if (isConnected) {
                drawLineSeg(lastX, lastY, currX, currY, color, g);
            }
            if (takeIntegral) {
                if (currX >= a && currX <= b) {
                    color = color.brighter();
                    if (currY < Y_MAX && currY > Y_MAX) drawLineSeg(currX, 0, currX, Y_MAX, color, g); else if (currY < Y_MAX && currY > Y_MIN) drawLineSeg(currX, 0, currX, currY, color, g); else if (currY <= Y_MIN) drawLineSeg(currX, 0, currX, Y_MIN, color, g); else if (currY >= Y_MAX) drawLineSeg(currX, 0, currX, Y_MAX, color, g); else ;
                    color = color.darker();
                }
            }
            lastX = ind.getValue();
            lastY = dep.getValue();
        }
        if (tracing) {
            g.setColor(Color.black);
            CURRCALC.parse(eqtn);
            ind.setValue(tracePt);
            drawTracer(tracePt, CURRCALC.solve(), g);
        }
        if (deriving) {
            CURRCALC.parse(eqtn);
            double slope = CURRCALC.deriveAtPoint(derivative);
            ind.setValue(derivative);
            double depVal = CURRCALC.solve();
            double xChange = Math.sin(Math.atan(slope)) * 20;
            double yChange = 20 - xChange;
            if (slope > 1) yChange = -1 * yChange;
            drawLineSeg(derivative - xChange * X_PIXEL, depVal - yChange * Y_PIXEL, derivative + xChange * X_PIXEL, depVal + yChange * Y_PIXEL, new Color(255, 69, 0), g);
        }
    }

    public void graphPolar(Function f, Graphics g) {
        String eqtn = f.getFuncEqtn();
        Var ind = f.getIndependentVar();
        Var dep = f.getDependentVar();
        Color color = f.getColor();
        boolean tracing = f.isTracingPt();
        double tracePt = f.getTraceVal();
        boolean isConnected = f.isConneted();
        boolean takeIntegral = f.isTakingIntegral();
        double a = f.getStartIntegral();
        double b = f.getEndIntegral();
        int angleUnits = CURRCALC.getAngleUnits();
        setLineSize(LINE_SIZE_DEFAULT);
        double currR, currT, lastX, lastY, currX, currY;
        g.setColor(color);
        ind.setValue(THETA_MIN);
        CURRCALC.parse(eqtn);
        CURRCALC.solve();
        currR = dep.getValue();
        currT = ind.getValue();
        lastX = currR * Math.cos(currT);
        lastY = currR * Math.sin(currT);
        int numCalcs = (int) ((THETA_MAX - THETA_MIN) / THETA_STEP);
        for (int i = 1; i <= numCalcs; i++) {
            ind.updateValue(THETA_STEP);
            CURRCALC.solve();
            currR = dep.getValue();
            currT = ind.getValue();
            if (angleUnits == 2) currT *= (Math.PI / 180);
            if (angleUnits == 3) currT *= (Math.PI / 200);
            currX = currR * Math.cos(currT);
            currY = currR * Math.sin(currT);
            drawLineSeg(lastX, lastY, currX, currY, color, g);
            lastX = currX;
            lastY = currY;
        }
    }

    public void drawTracer(double x, double y, Graphics g) {
        ptOn(x, y, g);
        ptOn(x + LINE_SIZE * X_PIXEL, y + LINE_SIZE * Y_PIXEL, g);
        ptOn(x + LINE_SIZE * X_PIXEL, y - LINE_SIZE * Y_PIXEL, g);
        ptOn(x - LINE_SIZE * X_PIXEL, y + LINE_SIZE * Y_PIXEL, g);
        ptOn(x - LINE_SIZE * X_PIXEL, y - LINE_SIZE * Y_PIXEL, g);
    }

    public void plot3dPoint(double x, double y, double z, Graphics g) {
        PointIn3d a = new PointIn3d(x, y, z);
        PointIn3d c = new PointIn3d(cX, cY, cZ);
        PointIn3d t = new PointIn3d(xRot, yRot, zRot);
        PointIn3d e = new PointIn3d(vX, vY, vZ);
        PointIn3d d = new PointIn3d(0, 0, 0);
        double X, Y;
        d.setX(Math.cos(t.getY()) * (Math.sin(t.getZ()) * (a.getY() - c.getY()) + Math.cos(t.getZ()) * (a.getX() - c.getX())) - Math.sin(t.getY()) * (a.getZ() - c.getZ()));
        d.setY(Math.sin(t.getX()) * (Math.cos(t.getY()) * (a.getZ() - c.getZ()) + Math.sin(t.getY()) * (Math.sin(t.getZ()) * (a.getY() - c.getY()) + Math.cos(t.getZ()) * (a.getX() - c.getX()))) + Math.cos(t.getX()) * (Math.cos(t.getZ()) * (a.getY() - c.getY()) - Math.sin(t.getZ()) * (a.getX() - c.getX())));
        d.setZ(Math.cos(t.getX()) * (Math.cos(t.getY()) * (a.getZ() - c.getZ()) + Math.sin(t.getY()) * (Math.sin(t.getZ()) * (a.getY() - c.getY()) + Math.cos(t.getZ()) * (a.getX() - c.getX()))) - Math.sin(t.getX()) * (Math.cos(t.getZ()) * (a.getY() - c.getY()) - Math.sin(t.getZ()) * (a.getX() - c.getX())));
        X = (d.getX() - e.getX()) / (e.getZ() / d.getZ());
        Y = (d.getY() - e.getY()) / (e.getZ() / d.getZ());
        ptOn(X, Y, g);
    }
}
