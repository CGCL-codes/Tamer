package uk.ac.imperial.ma.metric.apps.threePoints;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import uk.ac.imperial.ma.metric.gui.ExtendedGridBagLayout;
import uk.ac.imperial.ma.metric.plotting.AxesPlotter;
import uk.ac.imperial.ma.metric.plotting.MathPainter;
import uk.ac.imperial.ma.metric.plotting.MathPainterPanel;
import uk.ac.imperial.ma.metric.plotting.MathPainterPanelEvent;
import uk.ac.imperial.ma.metric.plotting.MathPainterPanelListener;

/** 
 *
 */
public class ThreePointsPanel extends JPanel implements ActionListener, MathPainterPanelListener, Runnable {

    public static final short SINGLE = 0;

    public static final short SLOW = 1;

    public static final short MEDIUM = 2;

    public static final short FAST = 3;

    private JPanel jpnlValues;

    private ExtendedGridBagLayout egblValues;

    private JLabel jlblA;

    private JLabel jlblB;

    private JLabel jlblC;

    private JLabel jlblOpenParen1;

    private JLabel jlblOpenParen2;

    private JLabel jlblOpenParen3;

    private JLabel jlblOpenParen4;

    private JLabel jlblComma1;

    private JLabel jlblComma2;

    private JLabel jlblComma3;

    private JLabel jlblComma4;

    private JLabel jlblCloseParen1;

    private JLabel jlblCloseParen2;

    private JLabel jlblCloseParen3;

    private JLabel jlblCloseParen4;

    private JTextField jtxtAx;

    private JTextField jtxtAy;

    private JTextField jtxtBx;

    private JTextField jtxtBy;

    private JTextField jtxtCx;

    private JTextField jtxtCy;

    private JLabel jlblXmin;

    private JLabel jlblXmax;

    private JLabel jlblYmin;

    private JLabel jlblYmax;

    private JTextField jtxtXmin;

    private JTextField jtxtXmax;

    private JTextField jtxtYmin;

    private JTextField jtxtYmax;

    private JLabel jlblSeed;

    private JTextField jtxtSeedX;

    private JTextField jtxtSeedY;

    private JPanel jpnlControls;

    private ExtendedGridBagLayout egblControls;

    private JButton jbtnSingle;

    private JButton jbtnSlow;

    private JTextField jtxtSlow;

    private JButton jbtnMedium;

    private JTextField jtxtMedium;

    private JButton jbtnFast;

    private JTextField jtxtFast;

    private JLabel jlblSkip;

    private JTextField jtxtSkip;

    private JButton jbtnStop;

    private JButton jbtnClear;

    private MathPainterPanel mpp;

    private MathPainter mp;

    private AxesPlotter ap;

    private ExtendedGridBagLayout egbl;

    private double currX;

    private double currY;

    private double vertexX;

    private double vertexY;

    private double aX;

    private double aY;

    private double bX;

    private double bY;

    private double cX;

    private double cY;

    private double xMin;

    private double xMax;

    private double yMin;

    private double yMax;

    private int nSlow;

    private int nMedium;

    private int nFast;

    private int skip;

    private short mode;

    private boolean firstFast;

    private boolean firstSingle;

    private boolean silent;

    protected boolean stop;

    protected Thread runner;

    /**
     * Constructor    
     */
    public ThreePointsPanel() {
        jpnlValues = new JPanel();
        jpnlValues.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Initial Values"));
        egblValues = new ExtendedGridBagLayout();
        jlblA = new JLabel("Point A = ");
        jlblB = new JLabel("Point B = ");
        jlblC = new JLabel("Point C = ");
        jlblOpenParen1 = new JLabel("(");
        jlblOpenParen2 = new JLabel("(");
        jlblOpenParen3 = new JLabel("(");
        jlblOpenParen4 = new JLabel("(");
        jlblComma1 = new JLabel(",");
        jlblComma2 = new JLabel(",");
        jlblComma3 = new JLabel(",");
        jlblComma4 = new JLabel(",");
        jlblCloseParen1 = new JLabel(")");
        jlblCloseParen2 = new JLabel(")");
        jlblCloseParen3 = new JLabel(")");
        jlblCloseParen4 = new JLabel(")");
        jtxtAx = new JTextField("0.0");
        jtxtAy = new JTextField("0.0");
        jtxtBx = new JTextField("1.0");
        jtxtBy = new JTextField("0.0");
        jtxtCx = new JTextField("0.5");
        jtxtCy = new JTextField("0.866");
        jlblXmin = new JLabel("x min = ");
        jlblXmax = new JLabel("x max = ");
        jlblYmin = new JLabel("y min = ");
        jlblYmax = new JLabel("y max = ");
        jtxtXmin = new JTextField("-0.2");
        jtxtXmax = new JTextField("1.2");
        jtxtYmin = new JTextField("-0.2");
        jtxtYmax = new JTextField("1.2");
        jlblSeed = new JLabel("Seed = ");
        jtxtSeedX = new JTextField("0.3");
        jtxtSeedY = new JTextField("0.3");
        jpnlValues.setLayout(egblValues);
        egblValues.add(jlblA, jpnlValues, 0, 0, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblOpenParen1, jpnlValues, 1, 0, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtAx, jpnlValues, 2, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblComma1, jpnlValues, 3, 0, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtAy, jpnlValues, 4, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblCloseParen1, jpnlValues, 5, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblB, jpnlValues, 0, 1, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblOpenParen2, jpnlValues, 1, 1, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtBx, jpnlValues, 2, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblComma2, jpnlValues, 3, 1, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtBy, jpnlValues, 4, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblCloseParen2, jpnlValues, 5, 1, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblC, jpnlValues, 0, 2, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblOpenParen3, jpnlValues, 1, 2, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtCx, jpnlValues, 2, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblComma3, jpnlValues, 3, 2, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtCy, jpnlValues, 4, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblCloseParen3, jpnlValues, 5, 2, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblSeed, jpnlValues, 0, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblOpenParen4, jpnlValues, 1, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtSeedX, jpnlValues, 2, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblComma4, jpnlValues, 3, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtSeedY, jpnlValues, 4, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 100, 0, 0, 0, 0, 0);
        egblValues.add(jlblCloseParen4, jpnlValues, 5, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblXmin, jpnlValues, 0, 4, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtXmin, jpnlValues, 1, 4, 5, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblXmax, jpnlValues, 0, 5, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtXmax, jpnlValues, 1, 5, 5, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblYmin, jpnlValues, 0, 6, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtYmin, jpnlValues, 1, 6, 5, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jlblYmax, jpnlValues, 0, 7, 1, 1, 0, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 0, 0, 0, 0);
        egblValues.add(jtxtYmax, jpnlValues, 1, 7, 5, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 0, 0, 0, 0);
        jpnlControls = new JPanel();
        jpnlControls.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Iteration Controls"));
        egblControls = new ExtendedGridBagLayout();
        jbtnSingle = new JButton("Single");
        jbtnSlow = new JButton("Slow");
        jtxtSlow = new JTextField("10");
        jbtnMedium = new JButton("Medium");
        jtxtMedium = new JTextField("100");
        jbtnFast = new JButton("Fast");
        jtxtFast = new JTextField("100000");
        jlblSkip = new JLabel("Skip = ");
        jtxtSkip = new JTextField("10");
        jbtnStop = new JButton("Stop");
        jbtnClear = new JButton("Clear");
        jbtnStop.setEnabled(false);
        jbtnSingle.addActionListener(this);
        jbtnSlow.addActionListener(this);
        jbtnMedium.addActionListener(this);
        jbtnFast.addActionListener(this);
        jbtnStop.addActionListener(this);
        jbtnClear.addActionListener(this);
        jpnlControls.setLayout(egblControls);
        egblValues.add(jbtnSingle, jpnlControls, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 10, 5, 0, 0);
        egblValues.add(jbtnSlow, jpnlControls, 0, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 10, 5, 0, 0);
        egblValues.add(jtxtSlow, jpnlControls, 1, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 5, 10, 0, 0);
        egblValues.add(jbtnMedium, jpnlControls, 0, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 10, 5, 0, 0);
        egblValues.add(jtxtMedium, jpnlControls, 1, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 5, 10, 0, 0);
        egblValues.add(jbtnFast, jpnlControls, 0, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 10, 5, 0, 0);
        egblValues.add(jtxtFast, jpnlControls, 1, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 5, 10, 0, 0);
        egblValues.add(jlblSkip, jpnlControls, 0, 4, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST, 0, 0, 10, 5, 0, 0);
        egblValues.add(jtxtSkip, jpnlControls, 1, 4, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 5, 10, 0, 0);
        egblValues.add(jbtnStop, jpnlControls, 0, 5, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 10, 5, 0, 0);
        egblValues.add(jbtnClear, jpnlControls, 1, 5, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.WEST, 0, 0, 5, 10, 0, 0);
        mpp = new MathPainterPanel();
        mpp.addMathPainterPanelListener(this);
        egbl = new ExtendedGridBagLayout();
        setLayout(egbl);
        egbl.add(jpnlValues, this, 0, 0, 1, 1, 0, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER, 0, 0, 0, 0, 0, 0);
        egbl.add(mpp, this, 1, 0, 2, 2, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER, 0, 0, 0, 0, 0, 0);
        egbl.add(jpnlControls, this, 0, 1, 1, 1, 0, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER, 0, 0, 0, 0, 0, 0);
        runner = new Thread(this);
    }

    public void init() {
        if (getUserValues()) {
            mp = mpp.init(xMin, yMin, xMax - xMin, yMax - yMin, MathPainterPanel.EQUAL_SCALES);
            mp.setScales();
            ap = new AxesPlotter(mp);
            mpp.clearCompletely();
            mp.setPaint(Color.WHITE);
            mp.setBackground();
            mp.setPaint(Color.BLUE);
            ap.drawAxes();
            ap.drawTicks();
            mp.setPaint(Color.RED);
            mp.fillCircle(aX, aY);
            mp.fillCircle(bX, bY);
            mp.fillCircle(cX, cY);
            mp.setPaint(Color.BLACK);
            mpp.setBase();
            mp.fillCircle(currX, currY);
            mpp.update();
        }
    }

    private boolean getUserValues() {
        try {
            currX = Double.parseDouble(jtxtSeedX.getText());
            currY = Double.parseDouble(jtxtSeedY.getText());
            aX = Double.parseDouble(jtxtAx.getText());
            aY = Double.parseDouble(jtxtAy.getText());
            bX = Double.parseDouble(jtxtBx.getText());
            bY = Double.parseDouble(jtxtBy.getText());
            cX = Double.parseDouble(jtxtCx.getText());
            cY = Double.parseDouble(jtxtCy.getText());
            xMin = Double.parseDouble(jtxtXmin.getText());
            xMax = Double.parseDouble(jtxtXmax.getText());
            yMin = Double.parseDouble(jtxtYmin.getText());
            yMax = Double.parseDouble(jtxtYmax.getText());
            nSlow = Integer.parseInt(jtxtSlow.getText());
            nMedium = Integer.parseInt(jtxtMedium.getText());
            nFast = Integer.parseInt(jtxtFast.getText());
            skip = Integer.parseInt(jtxtSkip.getText());
            return true;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return false;
        }
    }

    private void setVertex() {
        int n = (int) (Math.floor(3.0 * Math.random()));
        switch(n) {
            case 0:
                vertexX = aX;
                vertexY = aY;
                break;
            case 1:
                vertexX = bX;
                vertexY = bY;
                break;
            default:
                vertexX = cX;
                vertexY = cY;
        }
    }

    public void actionPerformed(ActionEvent ae) {
        stopRunner();
        if (ae.getSource() == jbtnSingle) {
            mode = SINGLE;
            if (firstSingle) {
                getUserValues();
                mpp.clear();
            }
            startRunner();
            firstSingle = false;
        } else {
            firstSingle = true;
        }
        if (ae.getSource() == jbtnSlow) {
            getUserValues();
            mode = SLOW;
            mpp.clear();
            startRunner();
        } else if (ae.getSource() == jbtnMedium) {
            getUserValues();
            mode = MEDIUM;
            mpp.clear();
            startRunner();
        } else if (ae.getSource() == jbtnFast) {
            getUserValues();
            mode = FAST;
            firstFast = true;
            mpp.clear();
            startRunner();
        } else if (ae.getSource() == jbtnStop) {
        } else if (ae.getSource() == jbtnClear) {
            init();
        }
    }

    private void stopRunner() {
        stop = true;
        while (runner.isAlive()) {
            Thread.currentThread().yield();
        }
        runner = new Thread(this);
    }

    private void startRunner() {
        stop = false;
        jbtnStop.setEnabled(true);
        runner.start();
    }

    public void run() {
        switch(mode) {
            case SINGLE:
                runSingle();
                break;
            case SLOW:
                for (int i = 0; i < nSlow; i++) {
                    if (stop) {
                        break;
                    }
                    runSingle();
                }
                break;
            case MEDIUM:
                for (int i = 0; i < skip; i++) {
                    setVertex();
                    if (stop) {
                        break;
                    }
                    if (firstFast) {
                        mp.setPaint(Color.WHITE);
                        mp.fillCircle(currX, currY);
                    }
                    currX = 0.5 * (currX + vertexX);
                    currY = 0.5 * (currY + vertexY);
                    firstFast = false;
                }
                for (int i = 0; i < nMedium; i++) {
                    setVertex();
                    if (stop) {
                        break;
                    }
                    if (firstFast) {
                        mp.setPaint(Color.WHITE);
                        mp.fillCircle(currX, currY);
                    }
                    mp.setPaint(Color.BLACK);
                    mp.drawLine(currX, currY, currX, currY);
                    mpp.update();
                    currX = 0.5 * (currX + vertexX);
                    currY = 0.5 * (currY + vertexY);
                    firstFast = false;
                }
                break;
            case FAST:
                for (int i = 0; i < skip; i++) {
                    setVertex();
                    if (stop) {
                        break;
                    }
                    if (firstFast) {
                        mp.setPaint(Color.WHITE);
                        mp.fillCircle(currX, currY);
                    }
                    currX = 0.5 * (currX + vertexX);
                    currY = 0.5 * (currY + vertexY);
                    firstFast = false;
                }
                mp.setPaint(Color.BLACK);
                for (int i = 0; i < nFast; i++) {
                    setVertex();
                    if (stop) {
                        break;
                    }
                    mp.drawLine(currX, currY, currX, currY);
                    currX = 0.5 * (currX + vertexX);
                    currY = 0.5 * (currY + vertexY);
                }
                mpp.update();
                break;
            default:
        }
        jbtnStop.setEnabled(false);
    }

    private void runSingle() {
        try {
            setVertex();
            mp.setPaint(Color.BLUE);
            mp.drawLine(currX, currY, vertexX, vertexY);
            mpp.update();
            Thread.currentThread().sleep(1000);
            mp.setPaint(Color.BLACK);
            mp.fillCircle(0.5 * (currX + vertexX), 0.5 * (currY + vertexY));
            mpp.update();
            Thread.currentThread().sleep(1000);
            mpp.clear();
            mp.setPaint(Color.BLACK);
            mp.fillCircle(0.5 * (currX + vertexX), 0.5 * (currY + vertexY));
            mpp.update();
            currX = 0.5 * (currX + vertexX);
            currY = 0.5 * (currY + vertexY);
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public void mathPainterPanelAction(MathPainterPanelEvent mppe) {
    }

    public void mathPainterPanelResized() {
        init();
    }
}
