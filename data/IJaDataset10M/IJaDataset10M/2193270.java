package hydrogenorbitalapplet2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import plotlib.GridObject;

/**
 * A class that handles all graphics used in the program
 * @author Carlo Barraco
 */
public class Canvas extends GridObject implements MouseMotionListener, MouseListener {

    int Resolution = 1, n = 1, l = 0, m = 0;

    Point2D.Double Mouse = new Point2D.Double();

    double[][] Probabilities = new double[(int) (this.getSize().width / Resolution)][(int) (this.getSize().height / Resolution)];

    double[][] Hues = new double[(int) (this.getSize().width / Resolution)][(int) (this.getSize().height / Resolution)];

    boolean ReCalculate = true;

    /**
     * Initalizes the GridObject settings
     */
    Canvas() {
        setDrawGrid(true);
        setBackground(Color.BLACK);
        setGridColor(Color.DARK_GRAY);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    /**
     * Receives the mouse position from the user and repaints the canvas
     * @param evt The mouse event
     */
    public void mouseMoved(MouseEvent evt) {
        Mouse = new Point2D.Double(evt.getX(), evt.getY());
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
    }

    @Override
    public void mouseExited(MouseEvent evt) {
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
    }

    @Override
    public void mousePressed(MouseEvent evt) {
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
    }

    /**
     * Paints the probabilities
     * @param g The graphics to paint on
     */
    @Override
    protected void paintFrame(Graphics g) {
        double pro = 0;
        double pMax = -1;
        double pMin = 99999999;
        if (ReCalculate) {
            for (double i = Resolution; i < this.getSize().width; i += 2 * Resolution) {
                for (double j = Resolution; j < this.getSize().height; j += 2 * Resolution) {
                    int I = (int) ((i - Resolution) / (2 * Resolution));
                    int J = (int) ((j - Resolution) / (2 * Resolution));
                    Probabilities[I][J] = Physics.prob(n, l, m, i / 2, j / 2);
                    if (pMin > Probabilities[I][J]) {
                        pMin = Probabilities[I][J];
                    }
                    if (pMax < Probabilities[I][J]) {
                        pMax = Probabilities[I][J];
                    }
                }
            }
            for (double i = Resolution; i < this.getSize().width; i += 2 * Resolution) {
                for (double j = Resolution; j < this.getSize().height; j += 2 * Resolution) {
                    int I = (int) ((i - Resolution) / (2 * Resolution));
                    int J = (int) ((j - Resolution) / (2 * Resolution));
                    Hues[I][J] = (float) (Probabilities[I][J] - pMin);
                    Hues[I][J] /= (float) (pMax - pMin);
                }
            }
            ReCalculate = false;
        }
        for (double i = Resolution; i < this.getSize().width; i += 2 * Resolution) {
            for (double j = Resolution; j < this.getSize().height; j += 2 * Resolution) {
                int I = (int) ((i - Resolution) / (2 * Resolution));
                int J = (int) ((j - Resolution) / (2 * Resolution));
                g.setColor(Color.getHSBColor(0.0f, 0.0f, (float) Hues[I][J]));
                g.fillRect((int) i - Resolution, (int) j - Resolution, 2 * Resolution, 2 * Resolution);
            }
        }
        pro = Physics.prob(n, l, m, Mouse.getX() / 2, Mouse.getY() / 2);
        super.paintFrame(g);
        g.setColor(Color.WHITE);
        Main.Probability = pro;
    }
}
