package gov.sns.apps.rocs;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.lang.*;
import gov.sns.tools.swing.*;
import gov.sns.tools.apputils.EdgeLayout;
import java.text.NumberFormat;
import gov.sns.tools.messaging.*;
import gov.sns.ca.*;
import java.net.URL;

public class TuneFace extends JPanel implements OpticsListener {

    CalcSettings set = new CalcSettings();

    EdgeLayout layout = new EdgeLayout();

    protected OpticsListener opticsProxy;

    private TextScrollDouble[] scrollX = new TextScrollDouble[2];

    private TextScrollDouble[] scrollY = new TextScrollDouble[2];

    private DecimalField[] decOut = new DecimalField[6];

    private DecimalField[] decRead = new DecimalField[6];

    private DecimalField energyfield;

    private JComboBox inc1, inc2;

    private JButton calcbutton, submitbutton;

    private JLabel lblOutput, lblX, lblY, lblInc, lblReadBack;

    private JLabel lblx2, lbly2, lblAdj;

    private JLabel energylabel;

    private JLabel blank;

    private JLabel[] klabel = new JLabel[6];

    private NumberFormat numFor;

    private JPanel mainPanel;

    private JPanel[] p = new JPanel[6];

    private String[] array = { "0.001", "0.005", "0.01", "0.025", "0.05", "0.1" };

    public double tuneMin, tuneMax, x, y;

    public double[] local_k = new double[6];

    public int pvcounter = 0;

    public int[] mademonitor = new int[6];

    boolean tuneKnown = false;

    GenDocument doc;

    GenWindow window;

    URL url = getClass().getResource("resources/tuneGridMaster.dat");

    int i;

    public TuneFace(GenDocument aDocument, GenWindow parent) {
        doc = aDocument;
        doc.addOpticsListener(this);
        window = parent;
        setPreferredSize(new Dimension(425, 550));
        setLayout(layout);
        numFor = NumberFormat.getNumberInstance();
        numFor.setMinimumFractionDigits(4);
        callRead();
        makeComponents();
        setColor();
        addComponents();
        setTips();
        setAction();
        reconcileK(doc.getQuadK());
    }

    public void addComponents() {
        mainPanel.setBorder(BorderFactory.createTitledBorder(" Set Tunes "));
        EdgeLayout toplayout = new EdgeLayout();
        p[1].setPreferredSize(new Dimension(300, 200));
        p[1].setBorder(BorderFactory.createRaisedBevelBorder());
        p[1].setLayout(toplayout);
        toplayout.setConstraints(energylabel, 10, 10, 100, 100, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(energyfield, 10, 160, 100, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(lblInc, 45, 10, 200, 100, EdgeLayout.LEFT, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(inc1, 42, 120, 100, 0, EdgeLayout.LEFT, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(lblX, 80, 10, 200, 100, EdgeLayout.LEFT, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(scrollX[0], 75, 70, 200, 0, EdgeLayout.LEFT, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(lblY, 110, 10, 200, 100, EdgeLayout.LEFT, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(scrollY[0], 105, 70, 200, 10, EdgeLayout.LEFT, EdgeLayout.NO_GROWTH);
        toplayout.setConstraints(calcbutton, 150, 50, 10, 0, EdgeLayout.LEFT, EdgeLayout.NO_GROWTH);
        p[1].add(energylabel);
        p[1].add(energyfield);
        p[1].add(lblInc);
        p[1].add(inc1);
        p[1].add(lblX);
        p[1].add(scrollX[0]);
        p[1].add(lblY);
        p[1].add(scrollY[0]);
        p[1].add(calcbutton);
        p[2].setBorder(BorderFactory.createRaisedBevelBorder());
        p[2].setLayout(new GridLayout(8, 1));
        p[2].add(blank);
        for (i = 0; i <= 5; i++) p[2].add(klabel[i]);
        p[3].setBorder(BorderFactory.createRaisedBevelBorder());
        p[3].setLayout(new GridLayout(8, 1));
        p[3].add(lblOutput);
        for (i = 0; i <= 5; i++) p[3].add(decOut[i]);
        p[3].add(submitbutton);
        p[4].setBorder(BorderFactory.createRaisedBevelBorder());
        p[4].setLayout(new GridLayout(8, 1));
        p[4].add(lblReadBack);
        for (i = 0; i <= 5; i++) p[4].add(decRead[i]);
        p[5].setBorder(BorderFactory.createRaisedBevelBorder());
        p[5].setLayout(new GridLayout(1, 3));
        p[5].add(p[2]);
        p[5].add(p[3]);
        p[5].add(p[4]);
        EdgeLayout mainlayout = new EdgeLayout();
        mainlayout.setConstraints(p[1], 10, 50, 100, 100, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        mainlayout.setConstraints(p[5], 300, 50, 100, 100, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        mainPanel.add(p[1]);
        mainPanel.add(p[5]);
        layout.add(mainPanel, this, 0, 0, EdgeLayout.TOP);
    }

    public void makeComponents() {
        scrollX[0] = new TextScrollDouble(0, 6.23, tuneMin, tuneMax, .01, .001);
        scrollY[0] = new TextScrollDouble(0, 6.20, tuneMin, tuneMax, .01, .001);
        lblOutput = new JLabel(" Calculated Fields:");
        lblReadBack = new JLabel(" Machine Settings: ");
        lblX = new JLabel("X tune:   ");
        lblY = new JLabel("Y tune:   ");
        lblInc = new JLabel("Slider Increment:    ");
        energylabel = new JLabel("Scale for Energy (GeV):");
        klabel[0] = new JLabel(" QV03a05a07 (T/m):");
        klabel[1] = new JLabel(" QH02a08 (T/m):   ");
        klabel[2] = new JLabel(" QH04a06 (T/m):   ");
        klabel[3] = new JLabel(" QV01a09 (T/m):   ");
        klabel[4] = new JLabel(" QV11a12 (T/m):   ");
        klabel[5] = new JLabel(" QH10a13 (T/m):   ");
        blank = new JLabel("");
        for (i = 0; i <= 5; i++) p[i] = new JPanel();
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(420, 450));
        inc1 = new JComboBox(array);
        inc1.setSelectedIndex(2);
        calcbutton = new JButton("Calculate Quadrupoles");
        submitbutton = new JButton("   Submit   ");
        energyfield = new DecimalField(1.0, 4, numFor);
        for (i = 0; i <= 5; i++) {
            decOut[i] = new DecimalField(0, 6, numFor);
            if (doc.quad_ch[i].isConnected() == true) {
                activateFieldSet(i, decOut[i], doc.quad_ch[i]);
            }
            if (doc.quad_ch[i].isConnected() == false) {
                decOut[i].setEnabled(false);
            }
            doc.quad_ch[i].addChannelConnectionListener(new ConnectionListener() {

                final int j = i;

                public void connectionMade(Channel aChannel) {
                    activateFieldSet(j, decOut[j], doc.quad_ch[j]);
                }

                public void connectionDropped(Channel aChannel) {
                    decOut[j].setEnabled(false);
                    pvcounter--;
                    refreshSubmitStatus();
                }
            });
        }
        for (i = 0; i <= 5; i++) {
            decRead[i] = new DecimalField(0, 6, numFor);
            if (doc.quad_ch[i].isConnected() == true) {
                decRead[i].setEnabled(true);
                if (mademonitor[i] != 1) {
                    makeFieldMonitor(decRead[i], doc.quad_ch[i]);
                    mademonitor[i] = 1;
                }
            }
            if (doc.quad_ch[i].isConnected() == false) {
                decRead[i].setEnabled(false);
            }
            doc.quad_ch[i].addChannelConnectionListener(new ConnectionListener() {

                final int j = i;

                public void connectionMade(Channel aChannel) {
                    decRead[j].setEnabled(true);
                    if (mademonitor[j] != 1) {
                        makeFieldMonitor(decRead[j], doc.quad_ch[j]);
                        mademonitor[j] = 1;
                    }
                }

                public void connectionDropped(Channel aChannel) {
                    decRead[j].setEnabled(false);
                }
            });
        }
    }

    public void activateFieldSet(int i, final DecimalField dField, ChannelAgent quad_ch) {
        dField.setEnabled(true);
        doc.quad_k_llimit[i] = quad_ch.getMagLowLimit();
        doc.quad_k_ulimit[i] = quad_ch.getMagUpLimit();
        pvcounter++;
        refreshSubmitStatus();
    }

    public void refreshSubmitStatus() {
        if (pvcounter >= 6) submitbutton.setEnabled(true); else submitbutton.setEnabled(false);
    }

    public void makeFieldMonitor(final DecimalField dField, ChannelAgent quad_ch) {
        quad_ch.addReadbackListener(new ReadbackListener() {

            public void updateReadback(Object sender, String name, double value) {
                dField.setValue(value);
            }
        });
    }

    public void setAction() {
        inc1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (inc1.getSelectedIndex() == 0) {
                    scrollX[0].setIncrement(.001);
                    scrollY[0].setIncrement(.001);
                }
                if (inc1.getSelectedIndex() == 1) {
                    scrollX[0].setIncrement(.005);
                    scrollY[0].setIncrement(.005);
                }
                if (inc1.getSelectedIndex() == 2) {
                    scrollX[0].setIncrement(.01);
                    scrollY[0].setIncrement(.01);
                }
                if (inc1.getSelectedIndex() == 3) {
                    scrollX[0].setIncrement(.025);
                    scrollY[0].setIncrement(.025);
                }
                if (inc1.getSelectedIndex() == 4) {
                    scrollX[0].setIncrement(.05);
                    scrollY[0].setIncrement(.05);
                }
                if (inc1.getSelectedIndex() == 5) {
                    scrollX[0].setIncrement(.1);
                    scrollY[0].setIncrement(.1);
                }
            }
        });
        calcbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                x = scrollX[0].getValue();
                y = scrollY[0].getValue();
                doc.callTuneCalc(url, energyfield.getValue(), x, y);
                for (i = 0; i <= 5; i++) decOut[i].setValue(local_k[i]);
                tuneKnown = true;
                doc.settingChanged(this);
            }
        });
        submitbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == submitbutton) {
                    for (i = 0; i <= 5; i++) local_k[i] = decOut[i].getValue();
                    doc.setQuadK(local_k);
                    doc.setQuadChannelAccess();
                    if (tuneKnown == true) doc.setTunes(x, y);
                    doc.setPhases(0.0, 0.0);
                    doc.settingChanged(this);
                }
            }
        });
    }

    public void setColor() {
        Color color = new Color(150, 150, 210);
        calcbutton.setBackground(color);
        submitbutton.setBackground(color);
    }

    public void callRead() {
        try {
            set.readData(url);
        } catch (IOException ioe) {
        }
        tuneMin = set.getMinx();
        tuneMax = set.getMaxx();
    }

    public void setTips() {
        inc1.setToolTipText("Increment for the x and y tunes.");
        p[1].setToolTipText("Calculated quadrupole set point.");
        p[5].setToolTipText("Quadrupole read back values.");
        calcbutton.setToolTipText("Calculation based on the x and y tune");
        submitbutton.setToolTipText("Send quadrupole values to machine");
    }

    public void updateQuadK(Object sender, double[] k) {
        reconcileK(k);
    }

    public void reconcileK(double[] k) {
        System.arraycopy(k, 0, local_k, 0, k.length);
        for (i = 0; i <= 5; i++) decOut[i].setValue(local_k[i]);
    }

    public void updateSextK(Object sender, double[] k) {
    }

    public void updateTunes(Object sender, double tunex, double tuney) {
    }

    public void updateChroms(Object sender, double chromx, double chromy) {
    }

    public void updatePhases(Object sender, double phasex, double phase) {
    }
}
