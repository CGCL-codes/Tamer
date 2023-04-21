package uk.ac.bath.domains.mover;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Observable;
import java.util.Vector;
import uk.ac.bath.domains.misc.Point2DInt;
import uk.ac.bath.domains.misc.FoodSupply;
import uk.ac.bath.base.CellAgeComparator;
import uk.ac.bath.base.Cell;
import uk.ac.bath.base.EnvironmentIF;
import uk.ac.bath.gui.View;

public class MoverPanel extends View {

    private int w, h;

    Vector<Cell> population;

    EnvironmentIF env;

    float fitref;

    boolean dbg = false;

    public MoverPanel(Environment env) {
        this.env = env;
        h = env.getSpace().sizeY();
        w = env.getSpace().sizeX();
        setBackground(Color.black);
        setDoubleBuffered(true);
        fitref = 0.1f;
    }

    public void setDebug(boolean flag) {
        dbg = flag;
    }

    public void myPaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setColor(Color.white);
        g.drawString(Integer.toString(env.getTickCount()), getWidth() - 100, getHeight() - 20);
        Graphics2D go = (Graphics2D) g.create();
        double xScale = getWidth() / (double) w;
        double yScale = getHeight() / (double) h;
        g.scale(xScale, yScale);
        FoodSupply food = ((Environment) env).getFoodSupply();
        Rectangle rect = food.getBoundingRect();
        int l = rect.x;
        int sx = rect.width;
        int t = rect.y;
        int sy = rect.height;
        double[][] fa = food.getArray();
        double factX = (food.foodLimit() + 50);
        for (int i = 0; i < sx; i++) {
            for (int j = 0; j < sy; j++) {
                double val = fa[i][j];
                if (val == 0) {
                    g.setColor(Color.black);
                } else {
                    int green = (int) (((val + 50) * 254.0) / factX * 0.5);
                    green = Math.min(green, 255);
                    green = Math.max(green, 0);
                    g.setColor(new Color(green, green, green));
                }
                g.fillRect(i + l, j + t, 1, 1);
            }
        }
        population = new Vector<Cell>(((MoverEnvironment) env).getPopulation());
        Collections.sort(population, new CellAgeComparator());
        Collections.reverse(population);
        if (population == null) {
            return;
        }
        int iy = 10;
        int ix = 10;
        int ih = 12;
        FontMetrics m = go.getFontMetrics();
        int cw = m.charWidth('X') / 2;
        int ch = m.getAscent() / 2;
        if (dbg) {
            g.setColor(Color.WHITE);
            go.drawString("[ type:id ] Generation:Age:Fitness)", ix, iy);
        }
        DecimalFormat f = new DecimalFormat("####.###");
        for (int i = 0; i < population.size(); i++) {
            Cell cell = (Cell) (population.elementAt(i));
            float hue = (cell.getId() % 3600) / 6.1f;
            float sat = 1.0f;
            float fit = (float) cell.getFitness();
            if (fit < 0) {
                continue;
            }
            fitref = Math.max(fit, fitref * 0.95f);
            float bri = 0.5f + 10 * fit / fitref;
            if (bri > 1.0f) {
                bri = 1.0f;
            }
            go.setColor(Color.getHSBColor(hue, sat, bri));
            if (i < 30 && dbg) {
                go.drawString("[" + cell.getTag() + "] " + cell.getGeneration() + ":" + cell.getAge() + ":" + f.format(cell.getFitness()), ix, iy + ih * (i + 1));
            }
            Point2DInt p1 = (Point2DInt) (cell.getPoint());
            double x1 = (p1.getX() + 0.5) * xScale;
            double y1 = (p1.getY() + 0.5) * yScale;
            int d = 3;
            go.fillArc((int) (x1 - d), (int) (y1 - d), 2 * d, 2 * d, 0, 360);
            if (cell instanceof MoverCell) {
                Point2DInt p0 = (Point2DInt) (((MoverCell) cell).getPrevPoint());
                double x0 = (p0.getX() + 0.5) * xScale;
                double y0 = (p0.getY() + 0.5) * yScale;
                go.drawLine((int) x1, (int) y1, (int) x0, (int) y0);
            }
        }
    }
}
