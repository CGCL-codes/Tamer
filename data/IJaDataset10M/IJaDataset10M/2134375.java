package Controls;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author nrovinskiy
 */
public class MultipleChart extends JPanel {

    public static final int CHAR_HEIGHT = 20;

    public static final int PLOT_HEIGHT = 300;

    public static final int BAR_WIDTH = 10;

    public static final int BAR_SPACE = 100;

    public static final int WHITE_SPACE = 10;

    public static final int AXIS_WIDTH = 3;

    public static final int ARROW_RIZE = 2;

    public static final int DEFAULT_MAX = 100;

    private static final Color[] clSet = { Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN, Color.PINK, Color.MAGENTA };

    String[] strNames;

    double[][] dblValues;

    String[] strXnames;

    String strYname;

    int intColorCounter, intColorPointer;

    Color[] clUsedColors;

    boolean blnSizeAdjusted;

    double dblMax, dblMin;

    public MultipleChart(String[] names, double[][] values, String[] xnames, String yname) {
        super();
        blnSizeAdjusted = false;
        strNames = names;
        dblValues = new double[values.length][values[0].length];
        for (int i = 0; i < values.length; i++) for (int j = 0; j < values[0].length; j++) dblValues[i][j] = values[i][j];
        strXnames = xnames;
        strYname = yname;
        intColorCounter = 0;
        intColorPointer = 0;
        dblMax = 0;
        dblMin = 0;
        this.setIgnoreRepaint(true);
        clUsedColors = new Color[strNames.length];
        this.setBackground(Color.WHITE);
        for (int i = 0; i < clUsedColors.length; i++) clUsedColors[i] = getNextColor();
    }

    private void SetSizeOnce() {
        if (blnSizeAdjusted) return;
        int intXSize = 0;
        for (int i = 0; i < strNames.length; i++) {
            intXSize = Math.max(intXSize, this.getFontMetrics(this.getFont()).stringWidth(strNames[i]));
        }
        intXSize = Math.max(intXSize + WHITE_SPACE, BAR_WIDTH * dblValues.length * dblValues[0].length + BAR_SPACE * dblValues[0].length + WHITE_SPACE);
        this.setSize(intXSize, PLOT_HEIGHT + dblValues.length * CHAR_HEIGHT + 4 * CHAR_HEIGHT);
        this.setPreferredSize(this.getSize());
        blnSizeAdjusted = true;
    }

    public void Draw() {
        if ((this.getFontMetrics(this.getFont()) == null) || (this.getGraphics() == null)) return;
        Graphics grfOut = this.getGraphics();
        grfOut.setColor(Color.WHITE);
        grfOut.fillRect(0, 0, this.getWidth(), this.getHeight());
        Draw(grfOut);
        SetSizeOnce();
    }

    public void Draw(Graphics grfOut) {
        double dblScale = 0;
        for (int i = 0; i < dblValues.length; i++) for (int j = 0; j < dblValues[i].length; j++) dblMax = Math.max(dblMax, dblValues[i][j]);
        for (int i = 0; i < dblValues.length; i++) for (int j = 0; j < dblValues[i].length; j++) dblMin = Math.min(dblMin, dblValues[i][j]);
        if ((dblMax == 0) && (dblMin == 0)) dblMax = DEFAULT_MAX;
        dblScale = (dblMax - dblMin) / PLOT_HEIGHT;
        double dblGridStep = PLOT_HEIGHT / 10;
        grfOut.setColor(Color.BLACK);
        for (double i = (dblMax / dblScale) + CHAR_HEIGHT; i >= CHAR_HEIGHT; i -= dblGridStep) {
            grfOut.drawLine(WHITE_SPACE, (int) i, this.getWidth(), (int) i);
        }
        for (double i = (dblMax / dblScale) + CHAR_HEIGHT; i <= (CHAR_HEIGHT - (dblMin - dblMax) / dblScale); i += dblGridStep) {
            grfOut.drawLine(WHITE_SPACE, (int) i, this.getWidth(), (int) i);
        }
        for (int i = 0; i < dblValues.length; i++) {
            for (int j = 0; j < dblValues[i].length; j++) {
                grfOut.setColor(clUsedColors[i]);
                double dblHeight = (int) (dblValues[i][j] / dblScale);
                if (dblValues[i][j] > 0) {
                    grfOut.fillRect(WHITE_SPACE + i * BAR_WIDTH + j * BAR_SPACE + j * BAR_WIDTH * dblValues.length, (int) ((dblMax / dblScale) - dblHeight + CHAR_HEIGHT), BAR_WIDTH, (int) dblHeight);
                    grfOut.setColor(Color.BLACK);
                    grfOut.drawRect(WHITE_SPACE + i * BAR_WIDTH + j * BAR_SPACE + j * BAR_WIDTH * dblValues.length, (int) ((dblMax / dblScale) - dblHeight + CHAR_HEIGHT), BAR_WIDTH, (int) dblHeight);
                } else {
                    grfOut.fillRect(WHITE_SPACE + i * BAR_WIDTH + j * BAR_SPACE + j * BAR_WIDTH * dblValues.length, (int) (CHAR_HEIGHT + dblMax / dblScale), BAR_WIDTH, -(int) dblHeight);
                    grfOut.setColor(Color.BLACK);
                    grfOut.drawRect(WHITE_SPACE + i * BAR_WIDTH + j * BAR_SPACE + j * BAR_WIDTH * dblValues.length, (int) (CHAR_HEIGHT + dblMax / dblScale), BAR_WIDTH, -(int) dblHeight);
                }
            }
        }
        grfOut.setColor(Color.BLACK);
        int[] YPoints = { PLOT_HEIGHT + CHAR_HEIGHT, CHAR_HEIGHT, CHAR_HEIGHT, 0, CHAR_HEIGHT, CHAR_HEIGHT, PLOT_HEIGHT + CHAR_HEIGHT };
        int[] XPoints = { WHITE_SPACE - AXIS_WIDTH, WHITE_SPACE - AXIS_WIDTH, WHITE_SPACE - AXIS_WIDTH - ARROW_RIZE - 1, WHITE_SPACE - AXIS_WIDTH / 2 - 1, WHITE_SPACE + ARROW_RIZE, WHITE_SPACE, WHITE_SPACE };
        grfOut.fillPolygon(XPoints, YPoints, 7);
        grfOut.drawString(strYname, WHITE_SPACE + ARROW_RIZE, CHAR_HEIGHT);
        int intIteration = 0;
        for (double i = (dblMax / dblScale) + CHAR_HEIGHT; i >= CHAR_HEIGHT; i -= dblGridStep) {
            if (i == CHAR_HEIGHT) {
                grfOut.drawString(Integer.toString((int) (intIteration * dblGridStep * dblScale)), 0, (int) (i + CHAR_HEIGHT / 2));
            } else {
                grfOut.drawString(Integer.toString((int) (intIteration * dblGridStep * dblScale)), 0, (int) i);
            }
            intIteration++;
        }
        intIteration = 0;
        for (double i = (dblMax / dblScale) + CHAR_HEIGHT; i <= (CHAR_HEIGHT - (dblMin - dblMax) / dblScale); i += dblGridStep) {
            grfOut.drawString(Integer.toString((int) (-dblScale * intIteration * dblGridStep)), 0, (int) i);
            intIteration++;
        }
        intIteration = 0;
        YPoints = new int[7];
        XPoints = new int[7];
        YPoints[0] = (int) (dblMax / dblScale + CHAR_HEIGHT - AXIS_WIDTH / 2);
        YPoints[1] = (int) (dblMax / dblScale + CHAR_HEIGHT - AXIS_WIDTH / 2);
        YPoints[2] = (int) (dblMax / dblScale + CHAR_HEIGHT - AXIS_WIDTH / 2 - ARROW_RIZE - 1);
        YPoints[3] = (int) (dblMax / dblScale + CHAR_HEIGHT);
        YPoints[4] = (int) (dblMax / dblScale + CHAR_HEIGHT + AXIS_WIDTH / 2 + ARROW_RIZE + 1);
        YPoints[5] = (int) (dblMax / dblScale + CHAR_HEIGHT + AXIS_WIDTH / 2 + 1);
        YPoints[6] = (int) (dblMax / dblScale + CHAR_HEIGHT + AXIS_WIDTH / 2 + 1);
        XPoints[0] = WHITE_SPACE - AXIS_WIDTH;
        XPoints[1] = this.getWidth() - CHAR_HEIGHT;
        XPoints[2] = this.getWidth() - CHAR_HEIGHT;
        XPoints[3] = this.getWidth();
        XPoints[4] = this.getWidth() - CHAR_HEIGHT;
        XPoints[5] = this.getWidth() - CHAR_HEIGHT;
        XPoints[6] = WHITE_SPACE - AXIS_WIDTH;
        grfOut.fillPolygon(XPoints, YPoints, XPoints.length);
        for (int i = 0; i < strXnames.length; i++) grfOut.drawString(strXnames[i], WHITE_SPACE + i * (BAR_SPACE + BAR_WIDTH * dblValues.length), (int) (dblMax / dblScale + AXIS_WIDTH / 2 + 1 + 1.5 * CHAR_HEIGHT));
        int intLegendBegin = 2 * CHAR_HEIGHT + PLOT_HEIGHT;
        for (int i = 0; i < strNames.length; i++) {
            grfOut.setColor(clUsedColors[i]);
            grfOut.fillRect(WHITE_SPACE, (int) (intLegendBegin + i * 1.5 * CHAR_HEIGHT), 2 * CHAR_HEIGHT, CHAR_HEIGHT);
            grfOut.setColor(Color.BLACK);
            grfOut.drawRect(WHITE_SPACE, (int) (intLegendBegin + i * 1.5 * CHAR_HEIGHT), 2 * CHAR_HEIGHT, CHAR_HEIGHT);
            grfOut.drawString(strNames[i], WHITE_SPACE + 2 * CHAR_HEIGHT + 10, (int) (intLegendBegin + (i * 1.5 + 1) * CHAR_HEIGHT));
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        Draw();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Draw();
    }

    private Color getNextColor() {
        Color clNew = null;
        if (intColorCounter < clSet.length) {
            return clSet[intColorCounter++];
        } else {
            int[] rgb = { 10, 10, 10 };
            boolean blnContains = false;
            do {
                blnContains = false;
                rgb[intColorPointer] = intColorCounter;
                if (rgb[intColorPointer] > 255) rgb[intColorPointer] = 255;
                clNew = new Color(rgb[0], rgb[1], rgb[2]);
                for (int i = 0; i < clSet.length; i++) if (clNew.equals(clSet[i])) blnContains = true;
                intColorCounter++;
                intColorPointer++;
                if (intColorPointer == rgb.length) intColorPointer = 0;
            } while (blnContains);
            return (clNew);
        }
    }

    public void setMaxValue(int value) {
        dblMax = (double) value;
    }

    public void setMinValue(int value) {
        dblMin = (double) value;
    }
}
