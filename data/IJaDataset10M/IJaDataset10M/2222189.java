package titancommon.node.tasks;

import java.awt.*;
import titancommon.node.TitanTask;
import titancommon.node.DataPacket;
import titancommon.tasks.GraphPlot;

/**
 * GraphPlot plots multiple data lines into one graph
 * using up to 16 (or more) different input ports.
 *
 * @author Jeremy Constantin <jeremyc@student.ethz.ch>
 */
public class EGraphPlot extends GraphPlot implements ExecutableTitanTask {

    private TitanTask tTask;

    public void setTitanTask(TitanTask tsk) {
        tTask = tsk;
    }

    private static final int SAMPLES_DEFAULT = 20;

    private static final Color GRAPH_COLORS[] = { Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE, Color.GRAY };

    private int samples;

    private int yLow = -1;

    private int yHigh = 1;

    private boolean bAutoScale;

    private int graphData[][];

    private int posIdx[];

    private int drawNum[];

    private int graphs;

    private GraphFrame gFrame;

    public boolean setExecParameters(short[] param) {
        switch(param.length) {
            case 0:
                samples = SAMPLES_DEFAULT;
                bAutoScale = true;
                break;
            case 2:
                samples = (param[0] << 8) + param[1];
                bAutoScale = true;
                break;
            case 4:
                samples = SAMPLES_DEFAULT;
                yLow = (param[0] << 8) + param[1];
                yHigh = (param[2] << 8) + param[3];
                bAutoScale = false;
                break;
            case 6:
                samples = (param[0] << 8) + param[1];
                yLow = (param[2] << 8) + param[3];
                yHigh = (param[4] << 8) + param[5];
                bAutoScale = false;
                break;
            default:
                tTask.errSource = tTask.getRunID();
                tTask.errType = 4;
                return false;
        }
        return true;
    }

    public void init() {
        int pn = tTask.getPortsInNum();
        if (pn == 0) return;
        graphs = pn;
        graphData = new int[pn][samples];
        posIdx = new int[pn];
        drawNum = new int[pn];
        gFrame = new GraphFrame();
        gFrame.setVisible(true);
    }

    public void inDataHandler(int port, DataPacket data) {
        int tmp = (int) (data.sdata[1] << 8);
        tmp += (int) data.sdata[0];
        short sval = (short) tmp;
        int val = (int) sval;
        int idx;
        if (drawNum[port] < samples) {
            idx = drawNum[port];
            drawNum[port]++;
        } else {
            idx = posIdx[port];
            posIdx[port]++;
            if (posIdx[port] == samples) posIdx[port] = 0;
        }
        graphData[port][idx] = val;
        gFrame.redrawGraph();
    }

    private void setYLowHigh() {
        yLow = -1;
        yHigh = 1;
        for (int i = 0; i < graphs; i++) {
            for (int k = 0; k < samples; k++) {
                int val = graphData[i][k];
                if (val < yLow) {
                    yLow = val;
                } else if (val > yHigh) {
                    yHigh = val;
                }
            }
        }
    }

    private class GraphFrame extends Frame {

        private GraphCanvas gCanvas;

        private int total_w, total_h, step_h;

        public GraphFrame() {
            gCanvas = new GraphCanvas();
            gCanvas.setBackground(Color.WHITE);
            this.add(gCanvas);
        }

        public void redrawGraph() {
            gCanvas.repaint();
        }

        private int getXPos(int val) {
            double x_dbl = ((double) (val - yLow)) / ((double) (yHigh - yLow));
            return (int) (x_dbl * total_w);
        }

        private class GraphCanvas extends Canvas {

            public void paint(Graphics graphx) {
                if (bAutoScale) {
                    setYLowHigh();
                }
                total_w = this.getWidth();
                total_h = this.getHeight();
                step_h = total_h / (samples - 1);
                drawZeroLine(graphx);
                for (int i = 0; i < graphs; i++) {
                    Color col = GRAPH_COLORS[i % GRAPH_COLORS.length];
                    graphx.setColor(col);
                    int x_last = 0, y_last = 0;
                    for (int p = 0; p < drawNum[i]; p++) {
                        int val = graphData[i][(posIdx[i] + p) % samples];
                        int x = getXPos(val);
                        int y = p * step_h;
                        drawPointMark(x, y, graphx);
                        if (p > 0) {
                            graphx.drawLine(x_last, y_last, x, y);
                        }
                        x_last = x;
                        y_last = y;
                    }
                }
                drawYLowHigh(graphx);
            }

            private void drawPointMark(int x, int y, Graphics g) {
                g.drawOval(x - 1, y - 1, 3, 3);
            }

            private void drawZeroLine(Graphics g) {
                final int DASH_W = 1;
                int x = getXPos(0);
                g.setColor(Color.BLACK);
                g.drawLine(x, 0, x, total_h);
                for (int i = 0; i < samples; i++) {
                    g.drawLine(x - DASH_W, i * step_h, x + DASH_W, i * step_h);
                }
            }

            private void drawYLowHigh(Graphics g) {
                g.setColor(Color.BLACK);
                int h = g.getFontMetrics().getHeight();
                g.drawString("l: " + yLow + " h: " + yHigh, 1, h + 1);
            }
        }
    }
}
