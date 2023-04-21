package gov.sns.apps.scldriftbeam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import gov.sns.ca.*;
import gov.sns.tools.swing.DecimalField;
import gov.sns.tools.apputils.EdgeLayout;
import gov.sns.tools.apputils.SimpleChartPopupMenu;
import gov.sns.tools.plot.*;
import gov.sns.xal.smf.NoSuchChannelException;
import gov.sns.xal.smf.impl.SCLCavity;
import gov.sns.xal.smf.impl.sclcavity.*;

public class SCLPhase extends JPanel implements ItemListener, ActionListener {

    private static final long serialVersionUID = 1;

    DriftBeamWindow myWindow;

    SCLCavity cav;

    String bcmDtPv = "SCL_Diag:BCM00b:SamplePeriod";

    String bcmNamePv;

    String[] bcms = { "SCL:BCM00", "MEBT:BCM02", "MEBT:BCM11", "HEBT:BCM01", "DTL:BCM200", "DTL:BCM400", "CCL:BCM102" };

    JComboBox bcm;

    int currentBCM = 0;

    int iRow, iCol;

    DecimalField tfEnergy;

    DecimalField tfCurrent;

    DecimalField tfPulse;

    DecimalField tfQuality;

    DecimalField tfDetuning;

    DecimalField tfAccFld;

    DecimalField tfPhaseSt;

    DecimalField tfPhaseBm;

    DecimalField tfPhaseCav;

    DecimalField tfDccFld;

    DecimalField tfRmsSize;

    DecimalField tfOutput;

    DecimalField tfBeamLoad;

    DecimalField tfTune;

    JLabel cavFieldSP;

    JComboBox cavType;

    JButton cavOff = new JButton("Turn off Cav.");

    GridBagConstraints gbc;

    JButton btRun;

    JButton btReset;

    JButton btPulse;

    JLabel lbEnergy;

    JLabel lbCurrent;

    JLabel lbQuality;

    JLabel lbDetuning;

    JLabel lbAccFld;

    JLabel lbPhaseSt;

    JButton lbPhaseBm;

    JButton phaseAvgBtn;

    JButton phaseAvgBtn1;

    JDialog phaseWFPane;

    DecimalField phaseStartF;

    DecimalField phaseEndF;

    double phaseStart = 1298.;

    double phaseEnd = 1308.;

    double pulseWidthdt;

    double tuneRev = 0.;

    double deltaT = 0.;

    double[] bArry;

    JLabel cavPhaseSP;

    JLabel lbPhaseCav;

    JLabel lbDccFld;

    JLabel lbRmsSize;

    JLabel lbOutput;

    JLabel lbCavity;

    JButton setPhase = new JButton("Set Cav. Phase");

    JDialog dlgPulse;

    JButton btBegin;

    JButton btEnd;

    JLabel lbShape;

    JLabel lbBeamLoad;

    JLabel lbTune;

    DecimalField tfShape;

    JLabel lbIndex;

    DriftBeam df;

    double Bc, Pl, Be;

    double Ql, Df, Ac;

    double Cp, Bp, Br;

    double phaseCav, dccFld, outputE;

    double beamLoad;

    double noiseA;

    double phaseFlu;

    double ampFlu;

    double noiseP;

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    JLabel cavName = new JLabel("Cavity: ");

    FunctionGraphsJPanel pulsePlot;

    FunctionGraphsJPanel phasePlot;

    CurveData plotPhase = new CurveData();

    CurveData plotAmplitude = new CurveData();

    CurveData plotPulse = new CurveData();

    double freq = 8.05e8;

    double dCavFieldSP, dCavPhaseSP;

    /** Creates a new instance of SCLPhase */
    public SCLPhase(DriftBeamWindow window) {
        df = new DriftBeam(new SCLCavity("cav"));
        myWindow = window;
        iRow = 0;
        iCol = 0;
        deltaT = 1.E-6;
        myWindow.myDocument.bcmx = new double[1120];
        for (int i = 0; i < 1120; i++) {
            myWindow.myDocument.bcmx[i] = i;
        }
        bcmNamePv = "SCL_Diag:BCM00:currentTBT";
        btRun = new JButton("Run");
        btRun.setForeground(Color.RED);
        btRun.addActionListener(this);
        btReset = new JButton("Reset");
        btReset.addActionListener(this);
        btPulse = new JButton("Pulse_(us)");
        btPulse.addActionListener(this);
        btPulse.setEnabled(false);
        dlgPulse = new JDialog(window, "Beam Pulse Shape");
        dlgPulse.setPreferredSize(new Dimension(600, 400));
        EdgeLayout edgeLyt = new EdgeLayout();
        dlgPulse.getContentPane().setLayout(edgeLyt);
        dlgPulse.setLocationRelativeTo(myWindow);
        pulsePlot = new FunctionGraphsJPanel();
        pulsePlot.addMouseListener(new SimpleChartPopupMenu(pulsePlot));
        pulsePlot.setGraphBackGroundColor(Color.white);
        pulsePlot.setPreferredSize(new Dimension(500, 250));
        pulsePlot.setAxisNames("time (us)", "Beam current (A)");
        plotPulse.setColor(Color.RED);
        pulsePlot.addCurveData(plotPulse);
        pulsePlot.setLegendButtonVisible(false);
        edgeLyt.setConstraints(pulsePlot, 10, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(pulsePlot);
        btBegin = new JButton("Update Beam Pulse");
        btBegin.addActionListener(this);
        edgeLyt.setConstraints(btBegin, 270, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(btBegin);
        bcm = new JComboBox(bcms);
        bcm.addActionListener(this);
        edgeLyt.setConstraints(bcm, 270, 180, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(bcm);
        tfShape = new DecimalField(iRow, 4);
        lbShape = new JLabel("No. of Sample Points:");
        edgeLyt.setConstraints(lbShape, 300, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(lbShape);
        edgeLyt.setConstraints(tfShape, 300, 180, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(tfShape);
        btEnd = new JButton("Use This Pulse");
        btEnd.addActionListener(this);
        edgeLyt.setConstraints(btEnd, 330, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        dlgPulse.add(btEnd);
        dlgPulse.pack();
        phaseWFPane = new JDialog(window, "Cavity LLRF Waveform");
        phaseWFPane.setPreferredSize(new Dimension(600, 400));
        EdgeLayout edgeLayout = new EdgeLayout();
        phaseWFPane.getContentPane().setLayout(edgeLayout);
        phaseWFPane.setLocationRelativeTo(myWindow);
        phasePlot = new FunctionGraphsJPanel();
        phasePlot.addMouseListener(new SimpleChartPopupMenu(phasePlot));
        phasePlot.setGraphBackGroundColor(Color.white);
        phasePlot.setPreferredSize(new Dimension(500, 250));
        phasePlot.setAxisNames("time (us)", "LLRF signal");
        plotPhase.setColor(Color.BLUE);
        plotAmplitude.setColor(Color.RED);
        phasePlot.addCurveData(plotPhase);
        phasePlot.addCurveData(plotAmplitude);
        edgeLayout.setConstraints(phasePlot, 10, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phasePlot);
        JLabel phase = new JLabel("Phase average:");
        edgeLayout.setConstraints(phase, 270, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phase);
        JLabel startLabel = new JLabel("begin (us): ");
        edgeLayout.setConstraints(startLabel, 290, 20, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(startLabel);
        phaseStartF = new DecimalField(phaseStart, 6);
        edgeLayout.setConstraints(phaseStartF, 290, 90, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseStartF);
        JLabel endLabel = new JLabel("end (us): ");
        edgeLayout.setConstraints(endLabel, 290, 190, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(endLabel);
        phaseEndF = new DecimalField(phaseEnd, 6);
        edgeLayout.setConstraints(phaseEndF, 290, 260, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseEndF);
        lbTune = new JLabel("Motor Rev: ");
        edgeLayout.setConstraints(lbTune, 290, 360, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(lbTune);
        tfTune = new DecimalField(tuneRev, 6);
        edgeLayout.setConstraints(tfTune, 290, 440, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(tfTune);
        JButton updatePlot = new JButton("Update Plot");
        updatePlot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!myWindow.myDocument.correlatorRunning) {
                    myWindow.myDocument.startCorrelator(e);
                }
                myWindow.myDocument.getCavSelector().getCavPhaseAvg(1298., 1308., 0);
                phasePlot.refreshGraphJPanel();
            }
        });
        edgeLayout.setConstraints(updatePlot, 320, 70, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(updatePlot);
        JButton phaseDone = new JButton("OK");
        phaseDone.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (myWindow.myDocument.getCavSelector().getSelectedRfCavity() != null) {
                    cav = myWindow.myDocument.getCavSelector().getSelectedRfCavity();
                    try {
                        double phaseAvg = myWindow.myDocument.getCavSelector().getCavPhaseAvg(numberFormat.parse(phaseStartF.getText()).doubleValue(), numberFormat.parse(phaseEndF.getText()).doubleValue(), 1);
                        phasePlot.refreshGraphJPanel();
                        setresults(phaseAvg);
                    } catch (java.text.ParseException pe) {
                        System.out.println("QLoaded is not in right number format!");
                    }
                } else {
                    System.out.println("No cavity selected!");
                }
                phaseWFPane.setVisible(false);
            }
        });
        edgeLayout.setConstraints(phaseDone, 320, 260, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseDone);
        JButton phaseTune = new JButton("Tune Cavity");
        phaseTune.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                ChannelFactory caF = ChannelFactory.defaultFactory();
                cav = myWindow.myDocument.getCavSelector().getSelectedRfCavity();
                if (dCavFieldSP < 0.5) {
                    try {
                        Channel ca1 = caF.getChannel("SCL_HPRF:Tun" + cav.channelSuite().getChannel("cavAmpSet").getId().substring(12, 16) + "Mot");
                        tuneRev = tfTune.getValue();
                        ca1.putVal(ca1.getValDbl() + tuneRev);
                        Thread.sleep(2500);
                        Channel ca2 = caF.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Wf_Dt");
                        pulseWidthdt = ca2.getValDbl();
                        myWindow.myDocument.getCavSelector().getCavPhaseAvg(1298., 1308., 0);
                    } catch (ConnectionException ce) {
                        System.out.println("Cannot connect to PV.");
                    } catch (GetException ge) {
                        System.out.println("Cannot get PV value.");
                    } catch (PutException pe) {
                        System.out.println("Cannot write to PV.");
                    } catch (InterruptedException ie) {
                        System.out.println("Cannot pause for tunning!");
                    }
                } else {
                    System.out.println("You may not detune this cavity, it's on!");
                }
            }
        });
        edgeLayout.setConstraints(phaseTune, 320, 380, 0, 0, EdgeLayout.TOP, EdgeLayout.NO_GROWTH);
        phaseWFPane.add(phaseTune);
        phaseWFPane.pack();
        lbEnergy = new JLabel("Energy (MeV):");
        lbCurrent = new JLabel("Beam Current (mA):");
        lbQuality = new JLabel("Loaded Q:");
        lbDetuning = new JLabel("Res. Error (Hz):");
        lbAccFld = new JLabel("Eacc w/ TTF (MV/m):");
        lbPhaseSt = new JLabel("Cav Design Phase (deg):");
        lbPhaseBm = new JButton("Phase Avg Range");
        lbPhaseBm.setEnabled(false);
        lbPhaseBm.addActionListener(this);
        phaseAvgBtn = new JButton("20-pulse Avg.");
        phaseAvgBtn.setEnabled(false);
        phaseAvgBtn.addActionListener(this);
        phaseAvgBtn1 = new JButton("Cav Phase Avg.");
        phaseAvgBtn1.setEnabled(false);
        phaseAvgBtn1.addActionListener(this);
        lbPhaseCav = new JLabel("New Cav Phase Set Pt.:");
        lbDccFld = new JLabel("Beam Loading (MV/m):");
        lbRmsSize = new JLabel("RmsSize (deg):");
        lbOutput = new JLabel("Output Energy (MeV):");
        lbCavity = new JLabel("Cavity type:");
        lbBeamLoad = new JLabel("Measured Signal(WfA):");
        tfCurrent = new DecimalField(Bc, 6);
        tfEnergy = new DecimalField(Be, 6);
        tfPulse = new DecimalField(Pl, 6);
        tfQuality = new DecimalField(Ql, 6);
        tfDetuning = new DecimalField(Df, 6);
        tfAccFld = new DecimalField(Ac, 6);
        tfPhaseSt = new DecimalField(Cp, 6);
        tfPhaseBm = new DecimalField(Bp, 6);
        tfPhaseCav = new DecimalField(phaseCav, 12);
        tfDccFld = new DecimalField(dccFld, 12);
        tfRmsSize = new DecimalField(Br, 6);
        tfOutput = new DecimalField(outputE, 12);
        tfBeamLoad = new DecimalField(beamLoad, 12);
        tfCurrent.setValue(0.0);
        tfEnergy.setValue(0.0);
        tfPulse.setValue(0.0);
        tfQuality.setValue(0.0);
        tfDetuning.setValue(0.0);
        tfAccFld.setValue(0.0);
        tfPhaseSt.setValue(0.0);
        tfPhaseBm.setValue(0.0);
        tfPhaseCav.setValue(0.0);
        tfDccFld.setValue(0.0);
        tfRmsSize.setValue(0.0);
        tfOutput.setValue(0.0);
        tfBeamLoad.setValue(0.0);
        String[] cavTypes = { "High Beta", "Medium Beta" };
        cavType = new JComboBox(cavTypes);
        JPanel inputs = new JPanel();
        GridLayout gl = new GridLayout(5, 6);
        gl.setHgap(7);
        inputs.setLayout(gl);
        inputs.setBorder(BorderFactory.createTitledBorder("Inputs... "));
        inputs.add(lbEnergy);
        inputs.add(tfEnergy);
        inputs.add(lbCurrent);
        inputs.add(tfCurrent);
        inputs.add(btPulse);
        inputs.add(tfPulse);
        inputs.add(lbAccFld);
        inputs.add(tfAccFld);
        inputs.add(lbQuality);
        inputs.add(tfQuality);
        inputs.add(lbDetuning);
        inputs.add(tfDetuning);
        inputs.add(lbPhaseSt);
        inputs.add(tfPhaseSt);
        inputs.add(lbRmsSize);
        inputs.add(tfRmsSize);
        inputs.add(phaseAvgBtn1);
        inputs.add(tfPhaseBm);
        inputs.add(lbCavity);
        inputs.add(cavType);
        cavFieldSP = new JLabel("Field Set Pt.:");
        inputs.add(cavFieldSP);
        cavOff.setEnabled(false);
        cavOff.addActionListener(this);
        inputs.add(cavOff);
        inputs.add(phaseAvgBtn);
        inputs.add(lbPhaseBm);
        JLabel dummy2 = new JLabel("");
        JLabel dummy3 = new JLabel("");
        JLabel dummy4 = new JLabel("");
        inputs.add(cavName);
        inputs.add(dummy2);
        inputs.add(dummy3);
        inputs.add(dummy4);
        inputs.add(btReset);
        inputs.add(btRun);
        JPanel results = new JPanel();
        results.setBorder(BorderFactory.createTitledBorder("Results... "));
        GridLayout gl1 = new GridLayout(2, 6);
        gl1.setHgap(5);
        results.setLayout(gl1);
        cavPhaseSP = new JLabel("Phase Set Pt.:");
        results.add(lbPhaseCav);
        results.add(tfPhaseCav);
        results.add(lbDccFld);
        results.add(tfDccFld);
        results.add(lbBeamLoad);
        results.add(tfBeamLoad);
        results.add(cavPhaseSP);
        setPhase.setForeground(Color.RED);
        setPhase.setEnabled(false);
        setPhase.addActionListener(this);
        results.add(setPhase);
        JLabel dummy5 = new JLabel("");
        JLabel dummy6 = new JLabel("");
        results.add(lbOutput);
        results.add(tfOutput);
        results.add(dummy5);
        results.add(dummy6);
        add(inputs);
        add(results);
        setBounds(100, 100, 600, 300);
        setVisible(true);
    }

    public void itemStateChanged(ItemEvent ie) {
        Checkbox cb = (Checkbox) ie.getItemSelectable();
    }

    public void actionPerformed(ActionEvent ae) {
        ChannelFactory cf = ChannelFactory.defaultFactory();
        if (ae.getActionCommand().equals("Reset")) {
            reset(ae);
        } else if (ae.getActionCommand().equals("Pulse_(us)")) {
            dlgPulse.setVisible(true);
        } else if (ae.getActionCommand().equals("Phase Avg Range")) {
            phaseWFPane.setVisible(true);
            if (!myWindow.myDocument.correlatorRunning) {
                myWindow.myDocument.startCorrelator(ae);
            }
        } else if (ae.getActionCommand().equals("20-pulse Avg.")) {
            try {
                double phaseAvg = myWindow.myDocument.getCavSelector().getCavPhaseAvg(numberFormat.parse(phaseStartF.getText()).doubleValue(), numberFormat.parse(phaseEndF.getText()).doubleValue(), 20);
                noiseA = myWindow.myDocument.getCavSelector().getNoiseA();
                noiseP = myWindow.myDocument.getCavSelector().getNoiseP();
                ampFlu = myWindow.myDocument.getCavSelector().getAmpFlu();
                phaseFlu = myWindow.myDocument.getCavSelector().getPhaseFlu();
                if (ampFlu > 5.) {
                    tfBeamLoad.setForeground(Color.RED);
                } else if (ampFlu > 2.5) {
                    tfBeamLoad.setForeground(Color.BLUE);
                } else {
                    tfBeamLoad.setForeground(Color.BLACK);
                }
                if (phaseFlu > 5.) {
                    tfPhaseBm.setForeground(Color.RED);
                } else if (phaseFlu > 2.5) {
                    tfPhaseBm.setForeground(Color.BLUE);
                } else {
                    tfPhaseBm.setForeground(Color.BLACK);
                }
                setresults(phaseAvg);
            } catch (java.text.ParseException pe) {
                System.out.println("Error in get phase");
            }
        } else if (ae.getActionCommand().equals("Cav Phase Avg.")) {
            if (!myWindow.myDocument.correlatorRunning) {
                myWindow.myDocument.startCorrelator(ae);
            }
            try {
                double phaseAvg = myWindow.myDocument.getCavSelector().getCavPhaseAvg(numberFormat.parse(phaseStartF.getText()).doubleValue(), numberFormat.parse(phaseEndF.getText()).doubleValue(), 1);
                phasePlot.refreshGraphJPanel();
                setresults(phaseAvg);
            } catch (java.text.ParseException pe) {
                System.out.println("Error in get phase");
            }
        } else if (ae.getActionCommand().equals("Update Beam Pulse")) {
            try {
                Channel ca1 = cf.getChannel(bcmNamePv);
                bArry = ca1.getArrDbl();
                setPulsePlot(bArry, myWindow.myDocument.bcmx);
                pulsePlot.refreshGraphJPanel();
                myWindow.myDocument.beamShape = getITBTWithBeamOnly(bArry);
                iRow = myWindow.myDocument.beamShape.length;
                tfShape.setValue(iRow);
                tfPulse.setValue(iRow * deltaT * 1.E6);
                iCol = 0;
            } catch (NullPointerException ne) {
                System.out.println("Check PV name " + bcmNamePv);
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect " + bcmNamePv);
            } catch (GetException ge) {
                System.out.println("Cannot value(s) " + bcmNamePv);
            } catch (NoSuchChannelException nse) {
                System.out.println("No channel " + bcmNamePv);
            }
        } else if (ae.getActionCommand().equals(bcm.getActionCommand())) {
            currentBCM = bcm.getSelectedIndex();
            switch(currentBCM) {
                case 0:
                    bcmDtPv = "SCL_Diag:BCM00b:SamplePeriod";
                    bcmNamePv = "SCL_Diag:BCM00:currentTBT";
                    break;
                case 5:
                    bcmDtPv = "DTL_Diag:BCM400:SamplePeriod";
                    bcmNamePv = "DTL_Diag:BCM400:currentTBT";
                    break;
                case 6:
                    bcmDtPv = "CCL_Diag:BCM102:SamplePeriod";
                    bcmNamePv = "CCL_Diag:BCM102:currentTBT";
                    break;
                case 3:
                    bcmDtPv = "HEBT_Diag:BCM01:SamplePeriod";
                    bcmNamePv = "HEBT_Diag:BCM01:currentTBT";
                    break;
                case 2:
                    bcmDtPv = "DTL_Diag:BCM02t248:tSamplePeriod";
                    bcmNamePv = "MEBT_Diag:BCM11:currentTBT";
                    break;
                case 4:
                    bcmDtPv = "DTL_Diag:BCM02t248:tSamplePeriod";
                    bcmNamePv = "DTL_Diag:BCM200:currentTBT";
                    break;
                default:
                    bcmDtPv = "DTL_Diag:BCM02t248:tSamplePeriod";
                    bcmNamePv = "MEBT_Diag:BCM02:currentTBT";
            }
        } else if (ae.getActionCommand().equals("Use This Pulse")) {
            if (iRow > 1) {
                iCol = 1;
            } else {
                iCol = 0;
            }
            dlgPulse.setVisible(false);
        } else if (ae.getActionCommand().equals("Run")) {
            if (tfDccFld.getValue() != 0. && cav != null) {
                setCavity(cav);
            }
            df.getCavity().buildcavity();
            Bc = tfCurrent.getValue();
            Be = tfEnergy.getValue();
            if (Be > 90.0) {
                if (iCol == 1) {
                    df.setshape(iRow);
                    for (int p = 0; p < iRow; p++) df.setpulse(p, deltaT * p, myWindow.myDocument.beamShape[p]);
                    Pl = deltaT * iRow;
                    df.setbeam(1.0, Pl, Be);
                } else {
                    df.setshape(1);
                    if (iRow > 1) {
                        Pl = deltaT * iRow;
                        df.setbeam(0.001 * Bc, Pl, Be);
                    } else {
                        Pl = tfPulse.getValue();
                        df.setbeam(0.001 * Bc, 0.000001 * Pl, Be);
                    }
                }
                NumberFormat nf = NumberFormat.getNumberInstance();
                try {
                    Ql = nf.parse(tfQuality.getText()).doubleValue();
                } catch (java.text.ParseException pe) {
                    System.out.println("QLoaded is not in right number format!");
                }
                Df = tfDetuning.getValue();
                Ac = tfAccFld.getValue();
                if (Ql < 60.0) Ql = 7.2E5;
                df.setcavity(Ql, Df, Ac, freq);
                Cp = tfPhaseSt.getValue();
                Bp = tfPhaseBm.getValue();
                Br = tfRmsSize.getValue();
                if (Br < 0.3) Br = 2.5;
                df.setphase(Cp, Bp, Br);
                numberFormat.setMaximumFractionDigits(4);
                phaseCav = df.findphase();
                tfPhaseCav.setText(numberFormat.format(phaseCav));
                dccFld = df.getloading();
                if (tfDccFld.getValue() < 0.09) {
                    tfPhaseCav.setForeground(Color.RED);
                } else if (tfDccFld.getValue() < 0.4) {
                    tfPhaseCav.setForeground(Color.BLUE);
                } else {
                    tfPhaseCav.setForeground(Color.BLACK);
                }
                tfDccFld.setText(numberFormat.format(dccFld));
                outputE = df.getBeam().getenergy();
                tfOutput.setText(numberFormat.format(outputE));
                if (Math.abs(phaseCav - dCavPhaseSP) > 20.) {
                    cavPhaseSP.setForeground(Color.RED);
                } else if (Math.abs(phaseCav - dCavPhaseSP) > 5.) {
                    cavPhaseSP.setForeground(Color.BLUE);
                } else {
                    cavPhaseSP.setForeground(Color.BLACK);
                }
            }
            if (myWindow.myDocument.isOnline) setPhase.setEnabled(true); else setPhase.setEnabled(false);
        } else if (ae.getActionCommand().equals("Set Cav. Phase")) {
            try {
                cav.setCavPhase(phaseCav);
                if (dCavFieldSP < 5.) {
                    Channel ca2 = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RunState");
                    ca2.putVal("Ramp");
                }
                cavPhaseSP.setText("Phase Set Pt.: " + cav.getCavPhaseSetPoint());
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect to PV!");
            } catch (PutException pe) {
                System.out.println("Cannot write to PV!");
            } catch (GetException ge) {
                System.out.println("Cannot get phase set PV!");
            }
        } else if (ae.getActionCommand().equals("Turn off Cav.")) {
            try {
                Channel ca1 = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "LoopOff");
                if (dCavFieldSP > 3.) {
                    Channel LoopOff = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "LoopOff");
                    double Fld = dCavFieldSP;
                    LoopOff.putVal("Close!");
                    int steps = (int) Math.round((Fld - 2.5) / 2.);
                    for (int i = 1; i < steps; i++) {
                        cav.setCavAmp(Fld - 2 * i);
                        Thread.sleep(500);
                    }
                    cav.setCavAmp(2.5);
                    Thread.sleep(3000);
                    dCavFieldSP = 2.5;
                }
                if (dCavFieldSP < 3.) {
                    Channel RFKill = cf.getChannel(cav.channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RFKill");
                    RFKill.putVal("Kill");
                    dCavFieldSP = 0.0;
                }
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect to PV.");
            } catch (PutException pe) {
                System.out.println("Cannot write to PV.");
            } catch (InterruptedException ie) {
                System.out.println("Cannot pause for tunning!");
            }
        }
    }

    private void setresults(double phase) {
        beamLoad = myWindow.myDocument.getCavSelector().getBeamLoad();
        noiseA = myWindow.myDocument.getCavSelector().getNoiseA();
        noiseP = myWindow.myDocument.getCavSelector().getNoiseP();
        lbBeamLoad.setForeground(Color.BLACK);
        if (noiseA > 5) {
            if (beamLoad / noiseA < 3) {
                lbBeamLoad.setForeground(Color.RED);
            } else if (beamLoad / noiseA < 6) {
                lbBeamLoad.setForeground(Color.BLUE);
            }
        }
        tfBeamLoad.setText(numberFormat.format(beamLoad));
        tfPhaseBm.setText(numberFormat.format(phase));
    }

    protected void setPhasePlot(double[] y, double[] x) {
        plotPhase.setPoints(x, y);
    }

    protected void setAmpPlot(double[] y, double[] x) {
        plotAmplitude.setPoints(x, y);
    }

    protected void setPulsePlot(double[] y, double[] x) {
        plotPulse.setPoints(x, y);
    }

    protected void setCavity(SCLCavity sclCav) {
        cav = sclCav;
        df = new DriftBeam(cav);
    }

    protected void setFrequency(double cavFreq) {
        freq = cavFreq;
    }

    private double[] getITBTWithBeamOnly(double[] fullArray) {
        double iMax = -100.;
        for (int i = 0; i < fullArray.length; i++) {
            if (fullArray[i] > iMax) iMax = fullArray[i];
        }
        double[] beamArray;
        if (iMax > 0.005) {
            int start;
            int end;
            int counter = 0;
            while (fullArray[counter] < iMax / 9.) {
                counter++;
            }
            start = counter;
            while (fullArray[counter] > iMax / 9.) {
                counter++;
            }
            end = counter;
            if (start != end) {
                beamArray = new double[end - start];
                System.arraycopy(fullArray, start, beamArray, 0, end - start);
            } else {
                beamArray = new double[1];
            }
        } else {
            beamArray = new double[1];
        }
        return beamArray;
    }

    protected void cavFieldSetPt(double field) {
        dCavFieldSP = field;
    }

    protected void cavPhaseSetPt(double phase) {
        dCavPhaseSP = phase;
    }

    public void reset(ActionEvent ae) {
        if (myWindow.myDocument.cavSelector != null) {
            myWindow.myDocument.stopCorrelator();
            myWindow.myDocument.cavSelector.initCavity(ae);
        } else {
            tfEnergy.setValue(0.0);
            tfQuality.setValue(0.0);
            tfDetuning.setValue(0.0);
            tfAccFld.setValue(0.0);
            tfPhaseSt.setValue(0.0);
            tfRmsSize.setValue(0.0);
            tfOutput.setValue(0.0);
            tfCurrent.setValue(0.0);
            tfPulse.setValue(0.0);
            tfPhaseBm.setValue(0.0);
            tfPhaseCav.setValue(0.0);
            tfDccFld.setValue(0.0);
            tfBeamLoad.setValue(0.0);
        }
        tfShape.setValue(0.0);
        iRow = 0;
        iCol = 0;
    }
}
