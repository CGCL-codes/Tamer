package com.sun.spot.spotworld.gui;

import com.sun.spot.spotworld.participants.EmulatedSunSPOT;
import java.awt.*;
import java.awt.geom.*;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.*;

/**
 * @author  Ron Goldman
 * Created on September 12, 2007, 11:21 PM
 */
public class VirtualSensorPanel extends JPanel {

    private JLabel outputPinLabels[];

    private JRadioButton inputPinButtons[];

    private JRadioButton inputPinButtons2[];

    private EmulatedSunSPOT spot;

    private int accScale;

    private JLabel[] leds;

    /** Creates new form VirtualSensorPanel */
    public VirtualSensorPanel(EmulatedSunSPOT spot, IUIObject obj) {
        this.spot = spot;
        initComponents();
        leds = new JLabel[] { LED1, LED2, LED3, LED4, LED5, LED6, LED7, LED8 };
        Hashtable<Integer, JComponent> d = new Hashtable<Integer, JComponent>();
        d.put(0, new JLabel("0"));
        d.put(341, new JLabel("1"));
        d.put(682, new JLabel("2"));
        d.put(1023, new JLabel("3"));
        scalarSliderA0.setLabelTable(d);
        scalarSliderA0.setPaintLabels(true);
        scalarSliderA1.setLabelTable(d);
        scalarSliderA1.setPaintLabels(true);
        scalarSliderA2.setLabelTable(d);
        scalarSliderA2.setPaintLabels(true);
        scalarSliderA3.setLabelTable(d);
        scalarSliderA3.setPaintLabels(true);
        scalarSliderA4.setLabelTable(d);
        scalarSliderA4.setPaintLabels(true);
        scalarSliderA5.setLabelTable(d);
        scalarSliderA5.setPaintLabels(true);
        ((GradientPanel) temperaturePanel).setLeftColor(Color.BLUE);
        ((GradientPanel) temperaturePanel).setMiddleColor(new Color(255, 0, 255));
        ((GradientPanel) temperaturePanel).setRightColor(Color.RED);
        outputPinLabels = new JLabel[4];
        outputPinLabels[0] = stateLabelH0;
        outputPinLabels[1] = stateLabelH1;
        outputPinLabels[2] = stateLabelH2;
        outputPinLabels[3] = stateLabelH3;
        inputPinButtons = new JRadioButton[5];
        inputPinButtons[0] = lowButtonD0;
        inputPinButtons[1] = lowButtonD1;
        inputPinButtons[2] = lowButtonD2;
        inputPinButtons[3] = lowButtonD3;
        inputPinButtons[4] = lowButtonD4;
        inputPinButtons2 = new JRadioButton[5];
        inputPinButtons2[0] = highButtonD0;
        inputPinButtons2[1] = highButtonD1;
        inputPinButtons2[2] = highButtonD2;
        inputPinButtons2[3] = highButtonD3;
        inputPinButtons2[4] = highButtonD4;
        accScale = 1;
        setAccScale(spot.getAccScale());
        oneGButtonActionPerformed(null);
        setSliderFont(temperatureSlider);
        setSliderFont(lightsensorSlider);
        setSliderFont(scalarSliderA0);
        setSliderFont(scalarSliderA1);
        setSliderFont(scalarSliderA2);
        setSliderFont(scalarSliderA3);
        setSliderFont(scalarSliderA4);
        setSliderFont(scalarSliderA5);
        try {
            URL url = obj.getClass().getResource("/com/sun/spot/spotworld/gridview/images/lineSPOT2.gif");
            ImageIcon icon = new ImageIcon(url);
            accelLabel.setIcon(icon);
        } catch (Exception ex) {
            SpotWorldPortal.msg("Failed to load accel image");
        }
        for (int i = 0; i < 4; i++) {
            setOutputPin(i, spot.getOutputPin(i + 5));
        }
        for (int i = 0; i < 5; i++) {
            boolean dir = spot.isOutputPin(i);
            setIOPinDirection(i, dir);
            if (dir) {
                setIOPin(i, spot.getOutputPin(i));
            }
        }
        for (int i = 0; i < 8; i++) {
            setLED(i, spot.getLED(i));
        }
        for (int i = 0; i < 2; i++) {
            setSwitch(i, spot.getSwitch(i));
        }
        setGreenLED(spot.getGreenLED());
        setRedLED(spot.getRedLED());
    }

    private void setSliderFont(JSlider slider) {
        Font font = slider.getFont();
        Dictionary d = slider.getLabelTable();
        Enumeration e = d.elements();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o instanceof JComponent) ((JComponent) o).setFont(font);
        }
    }

    public void setAccScale(int newScale) {
        if (accScale != newScale) {
            accScale = newScale;
            Hashtable<Integer, JComponent> d = new Hashtable<Integer, JComponent>();
            if (accScale == 2) {
                d.put(-600, new JLabel("-2"));
                d.put(-300, new JLabel("-1"));
                d.put(0, new JLabel("0"));
                d.put(300, new JLabel("1"));
                d.put(600, new JLabel("2"));
                xSlider.setValue(Math.max(Math.min(xSlider.getValue() * 3, 600), -600));
                ySlider.setValue(Math.max(Math.min(ySlider.getValue() * 3, 600), -600));
                zSlider.setValue(Math.max(Math.min(zSlider.getValue() * 3, 600), -600));
                xSlider.setMinorTickSpacing(150);
                ySlider.setMinorTickSpacing(150);
                zSlider.setMinorTickSpacing(150);
            } else {
                d.put(-600, new JLabel("-6"));
                d.put(-300, new JLabel("-3"));
                d.put(0, new JLabel("0"));
                d.put(300, new JLabel("3"));
                d.put(600, new JLabel("6"));
                xSlider.setValue(xSlider.getValue() / 3);
                ySlider.setValue(ySlider.getValue() / 3);
                zSlider.setValue(zSlider.getValue() / 3);
                xSlider.setMinorTickSpacing(100);
                ySlider.setMinorTickSpacing(100);
                zSlider.setMinorTickSpacing(100);
            }
            xSlider.setLabelTable(d);
            xSlider.setPaintLabels(true);
            ySlider.setLabelTable(d);
            ySlider.setPaintLabels(true);
            zSlider.setLabelTable(d);
            zSlider.setPaintLabels(true);
            setSliderFont(xSlider);
            setSliderFont(ySlider);
            setSliderFont(zSlider);
        }
    }

    private void accelerometerChanged() {
        double x = xSlider.getValue() / (100.0 * (accScale == 2 ? 3.0 : 1.0));
        double y = ySlider.getValue() / (100.0 * (accScale == 2 ? 3.0 : 1.0));
        double z = zSlider.getValue() / (100.0 * (accScale == 2 ? 3.0 : 1.0));
        spot.accelerometerChanged(x, y, z);
    }

    private class GradientPanel extends JPanel {

        private Color leftColor = Color.BLACK;

        private Color middleColor = new Color(220, 220, 220);

        private Color rightColor = Color.WHITE;

        public void setLeftColor(Color col) {
            leftColor = col;
        }

        public void setMiddleColor(Color col) {
            middleColor = col;
        }

        public void setRightColor(Color col) {
            rightColor = col;
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = 180;
            int w2 = w / 2;
            int x = 100;
            GradientPaint gradient = new GradientPaint(x, 0, leftColor, x + w2, 0, middleColor);
            g2.setPaint(gradient);
            g2.fill(new Rectangle2D.Double(x, 15, w2, 10));
            x += w2;
            GradientPaint gradient2 = new GradientPaint(x, 0, middleColor, x + w2, 0, rightColor);
            g2.setPaint(gradient2);
            g2.fill(new Rectangle2D.Double(x, 15, w2, 10));
            g2.setPaint(Color.BLACK);
        }
    }

    public void setOutputPin(int index, boolean high) {
        outputPinLabels[index].setText((high ? "High" : "Low "));
    }

    public void setIOPin(int index, boolean high) {
        inputPinButtons[index].setSelected(!high);
        inputPinButtons2[index].setSelected(high);
    }

    public void setIOPinDirection(int index, boolean output) {
        inputPinButtons[index].setEnabled(!output);
        inputPinButtons2[index].setEnabled(!output);
    }

    private void inputPinChanged(int index, boolean high) {
        spot.inputPinChanged(index, high);
    }

    private void voltageChanged(int index, int val) {
        spot.voltageChanged(index, val);
    }

    public void setLED(int index, Color col) {
        leds[index].setBackground(col);
    }

    public void setSwitch(int sw, boolean value) {
        if (sw == 0) {
            switchBox1.setSelected(value);
        } else {
            switchBox2.setSelected(value);
        }
    }

    public void setGreenLED(boolean on) {
        LED10.setBackground(on ? Color.GREEN : Color.BLACK);
    }

    public void setRedLED(boolean on) {
        LED9.setBackground(on ? Color.RED : Color.BLACK);
    }

    private void initComponents() {
        buttonGroupD0 = new javax.swing.ButtonGroup();
        buttonGroupD1 = new javax.swing.ButtonGroup();
        buttonGroupD2 = new javax.swing.ButtonGroup();
        buttonGroupD3 = new javax.swing.ButtonGroup();
        buttonGroupD4 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        lightsensorPanel = new GradientPanel();
        jLabel1 = new javax.swing.JLabel();
        lightsensorSlider = new javax.swing.JSlider();
        temperaturePanel = new GradientPanel();
        jLabel2 = new javax.swing.JLabel();
        temperatureSlider = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        scalarPanel1 = new javax.swing.JPanel();
        scalarSliderA0 = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        scalarPanel2 = new javax.swing.JPanel();
        scalarSliderA1 = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        scalarPanel3 = new javax.swing.JPanel();
        scalarSliderA2 = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        scalarPanel4 = new javax.swing.JPanel();
        scalarSliderA3 = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        scalarPanel5 = new javax.swing.JPanel();
        scalarSliderA4 = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        scalarPanel6 = new javax.swing.JPanel();
        scalarSliderA5 = new javax.swing.JSlider();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lowButtonD0 = new javax.swing.JRadioButton();
        highButtonD0 = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lowButtonD1 = new javax.swing.JRadioButton();
        highButtonD1 = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lowButtonD2 = new javax.swing.JRadioButton();
        highButtonD2 = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        lowButtonD3 = new javax.swing.JRadioButton();
        highButtonD3 = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lowButtonD4 = new javax.swing.JRadioButton();
        highButtonD4 = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        stateLabelH0 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        stateLabelH1 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        stateLabelH2 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        stateLabelH3 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        accelLabel = new javax.swing.JLabel();
        zSlider = new javax.swing.JSlider();
        xSlider = new javax.swing.JSlider();
        ySlider = new javax.swing.JSlider();
        oneGButton = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        LED1 = new javax.swing.JLabel();
        LED2 = new javax.swing.JLabel();
        LED3 = new javax.swing.JLabel();
        LED4 = new javax.swing.JLabel();
        LED5 = new javax.swing.JLabel();
        LED6 = new javax.swing.JLabel();
        LED7 = new javax.swing.JLabel();
        LED8 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        LED10 = new javax.swing.JLabel();
        LED9 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        switchBox1 = new javax.swing.JCheckBox();
        switchBox2 = new javax.swing.JCheckBox();
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        setBackground(new java.awt.Color(241, 249, 255));
        setMinimumSize(new java.awt.Dimension(321, 171));
        setPreferredSize(new java.awt.Dimension(321, 171));
        jTabbedPane1.setBackground(new java.awt.Color(241, 249, 255));
        jTabbedPane1.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(321, 171));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(321, 171));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 120));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 120));
        lightsensorPanel.setBackground(new java.awt.Color(231, 241, 255));
        lightsensorPanel.setMinimumSize(new java.awt.Dimension(300, 50));
        lightsensorPanel.setOpaque(false);
        lightsensorPanel.setPreferredSize(new java.awt.Dimension(300, 50));
        jLabel1.setText("Light Sensor");
        lightsensorPanel.add(jLabel1);
        lightsensorSlider.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        lightsensorSlider.setMajorTickSpacing(150);
        lightsensorSlider.setMaximum(750);
        lightsensorSlider.setMinorTickSpacing(50);
        lightsensorSlider.setPaintLabels(true);
        lightsensorSlider.setPaintTicks(true);
        lightsensorSlider.setPaintTrack(false);
        lightsensorSlider.setToolTipText("Current Light Sensor reading");
        lightsensorSlider.setValue(450);
        lightsensorSlider.setMinimumSize(new java.awt.Dimension(190, 44));
        lightsensorSlider.setName("LightSensorValue");
        lightsensorSlider.setPreferredSize(new java.awt.Dimension(190, 44));
        lightsensorSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                lightsensorSliderStateChanged(evt);
            }
        });
        lightsensorPanel.add(lightsensorSlider);
        jPanel1.add(lightsensorPanel);
        temperaturePanel.setBackground(new java.awt.Color(231, 241, 255));
        temperaturePanel.setMinimumSize(new java.awt.Dimension(300, 50));
        temperaturePanel.setOpaque(false);
        temperaturePanel.setPreferredSize(new java.awt.Dimension(300, 50));
        jLabel2.setText("Temperature");
        temperaturePanel.add(jLabel2);
        temperatureSlider.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        temperatureSlider.setMajorTickSpacing(40);
        temperatureSlider.setMaximum(140);
        temperatureSlider.setMinimum(-40);
        temperatureSlider.setMinorTickSpacing(10);
        temperatureSlider.setPaintLabels(true);
        temperatureSlider.setPaintTicks(true);
        temperatureSlider.setPaintTrack(false);
        temperatureSlider.setToolTipText("Current temperature reading");
        temperatureSlider.setValue(70);
        temperatureSlider.setMinimumSize(new java.awt.Dimension(190, 44));
        temperatureSlider.setName("LightSensorValue");
        temperatureSlider.setPreferredSize(new java.awt.Dimension(190, 44));
        temperatureSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                temperatureSliderStateChanged(evt);
            }
        });
        temperaturePanel.add(temperatureSlider);
        jPanel1.add(temperaturePanel);
        jTabbedPane1.addTab("Enviro", null, jPanel1, "Set lightsensor and temperature");
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 120));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 120));
        scalarPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        scalarPanel1.setBackground(new java.awt.Color(231, 241, 255));
        scalarPanel1.setMinimumSize(new java.awt.Dimension(50, 150));
        scalarPanel1.setOpaque(false);
        scalarPanel1.setPreferredSize(new java.awt.Dimension(50, 150));
        scalarSliderA0.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        scalarSliderA0.setMajorTickSpacing(341);
        scalarSliderA0.setMaximum(1023);
        scalarSliderA0.setMinorTickSpacing(171);
        scalarSliderA0.setOrientation(javax.swing.JSlider.VERTICAL);
        scalarSliderA0.setPaintTicks(true);
        scalarSliderA0.setToolTipText("Voltage for pin A0");
        scalarSliderA0.setValue(0);
        scalarSliderA0.setMinimumSize(new java.awt.Dimension(33, 100));
        scalarSliderA0.setName("");
        scalarSliderA0.setPreferredSize(new java.awt.Dimension(33, 100));
        scalarSliderA0.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scalarSliderA0StateChanged(evt);
            }
        });
        scalarPanel1.add(scalarSliderA0);
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("A0");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel3.setMaximumSize(new java.awt.Dimension(30, 12));
        jLabel3.setMinimumSize(new java.awt.Dimension(25, 12));
        jLabel3.setPreferredSize(new java.awt.Dimension(25, 12));
        jLabel3.setRequestFocusEnabled(false);
        jLabel3.setVerifyInputWhenFocusTarget(false);
        scalarPanel1.add(jLabel3);
        jPanel2.add(scalarPanel1);
        scalarPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        scalarPanel2.setBackground(new java.awt.Color(231, 241, 255));
        scalarPanel2.setMinimumSize(new java.awt.Dimension(50, 150));
        scalarPanel2.setOpaque(false);
        scalarPanel2.setPreferredSize(new java.awt.Dimension(50, 150));
        scalarSliderA1.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        scalarSliderA1.setMajorTickSpacing(341);
        scalarSliderA1.setMaximum(1023);
        scalarSliderA1.setMinorTickSpacing(171);
        scalarSliderA1.setOrientation(javax.swing.JSlider.VERTICAL);
        scalarSliderA1.setPaintTicks(true);
        scalarSliderA1.setToolTipText("Voltage for pin A1");
        scalarSliderA1.setValue(0);
        scalarSliderA1.setMinimumSize(new java.awt.Dimension(33, 100));
        scalarSliderA1.setName("");
        scalarSliderA1.setPreferredSize(new java.awt.Dimension(33, 100));
        scalarSliderA1.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scalarSliderA1StateChanged(evt);
            }
        });
        scalarPanel2.add(scalarSliderA1);
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("A1");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel4.setMaximumSize(new java.awt.Dimension(30, 12));
        jLabel4.setMinimumSize(new java.awt.Dimension(25, 12));
        jLabel4.setPreferredSize(new java.awt.Dimension(25, 12));
        jLabel4.setRequestFocusEnabled(false);
        jLabel4.setVerifyInputWhenFocusTarget(false);
        scalarPanel2.add(jLabel4);
        jPanel2.add(scalarPanel2);
        scalarPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        scalarPanel3.setBackground(new java.awt.Color(231, 241, 255));
        scalarPanel3.setMinimumSize(new java.awt.Dimension(50, 150));
        scalarPanel3.setOpaque(false);
        scalarPanel3.setPreferredSize(new java.awt.Dimension(50, 150));
        scalarSliderA2.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        scalarSliderA2.setMajorTickSpacing(341);
        scalarSliderA2.setMaximum(1023);
        scalarSliderA2.setMinorTickSpacing(171);
        scalarSliderA2.setOrientation(javax.swing.JSlider.VERTICAL);
        scalarSliderA2.setPaintTicks(true);
        scalarSliderA2.setToolTipText("Voltage for pin A2");
        scalarSliderA2.setValue(0);
        scalarSliderA2.setMinimumSize(new java.awt.Dimension(33, 100));
        scalarSliderA2.setName("");
        scalarSliderA2.setPreferredSize(new java.awt.Dimension(33, 100));
        scalarSliderA2.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scalarSliderA2StateChanged(evt);
            }
        });
        scalarPanel3.add(scalarSliderA2);
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("A2");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel5.setMaximumSize(new java.awt.Dimension(30, 12));
        jLabel5.setMinimumSize(new java.awt.Dimension(25, 12));
        jLabel5.setPreferredSize(new java.awt.Dimension(25, 12));
        jLabel5.setRequestFocusEnabled(false);
        jLabel5.setVerifyInputWhenFocusTarget(false);
        scalarPanel3.add(jLabel5);
        jPanel2.add(scalarPanel3);
        scalarPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        scalarPanel4.setBackground(new java.awt.Color(231, 241, 255));
        scalarPanel4.setMinimumSize(new java.awt.Dimension(50, 150));
        scalarPanel4.setOpaque(false);
        scalarPanel4.setPreferredSize(new java.awt.Dimension(50, 150));
        scalarSliderA3.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        scalarSliderA3.setMajorTickSpacing(341);
        scalarSliderA3.setMaximum(1023);
        scalarSliderA3.setMinorTickSpacing(171);
        scalarSliderA3.setOrientation(javax.swing.JSlider.VERTICAL);
        scalarSliderA3.setPaintTicks(true);
        scalarSliderA3.setToolTipText("Voltage for pin A3");
        scalarSliderA3.setValue(0);
        scalarSliderA3.setMinimumSize(new java.awt.Dimension(33, 100));
        scalarSliderA3.setName("");
        scalarSliderA3.setPreferredSize(new java.awt.Dimension(33, 100));
        scalarSliderA3.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scalarSliderA3StateChanged(evt);
            }
        });
        scalarPanel4.add(scalarSliderA3);
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("A3");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel6.setMaximumSize(new java.awt.Dimension(30, 12));
        jLabel6.setMinimumSize(new java.awt.Dimension(25, 12));
        jLabel6.setPreferredSize(new java.awt.Dimension(25, 12));
        jLabel6.setRequestFocusEnabled(false);
        jLabel6.setVerifyInputWhenFocusTarget(false);
        scalarPanel4.add(jLabel6);
        jPanel2.add(scalarPanel4);
        scalarPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        scalarPanel5.setBackground(new java.awt.Color(231, 241, 255));
        scalarPanel5.setMinimumSize(new java.awt.Dimension(50, 150));
        scalarPanel5.setOpaque(false);
        scalarPanel5.setPreferredSize(new java.awt.Dimension(50, 150));
        scalarSliderA4.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        scalarSliderA4.setMajorTickSpacing(341);
        scalarSliderA4.setMaximum(1023);
        scalarSliderA4.setMinorTickSpacing(171);
        scalarSliderA4.setOrientation(javax.swing.JSlider.VERTICAL);
        scalarSliderA4.setPaintTicks(true);
        scalarSliderA4.setToolTipText("Voltage for pin A4");
        scalarSliderA4.setValue(0);
        scalarSliderA4.setMinimumSize(new java.awt.Dimension(33, 100));
        scalarSliderA4.setName("");
        scalarSliderA4.setPreferredSize(new java.awt.Dimension(33, 100));
        scalarSliderA4.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scalarSliderA4StateChanged(evt);
            }
        });
        scalarPanel5.add(scalarSliderA4);
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("A4");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel7.setMaximumSize(new java.awt.Dimension(30, 12));
        jLabel7.setMinimumSize(new java.awt.Dimension(25, 12));
        jLabel7.setPreferredSize(new java.awt.Dimension(25, 12));
        jLabel7.setRequestFocusEnabled(false);
        jLabel7.setVerifyInputWhenFocusTarget(false);
        scalarPanel5.add(jLabel7);
        jPanel2.add(scalarPanel5);
        scalarPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        scalarPanel6.setBackground(new java.awt.Color(231, 241, 255));
        scalarPanel6.setMinimumSize(new java.awt.Dimension(50, 150));
        scalarPanel6.setOpaque(false);
        scalarPanel6.setPreferredSize(new java.awt.Dimension(50, 150));
        scalarSliderA5.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        scalarSliderA5.setMajorTickSpacing(341);
        scalarSliderA5.setMaximum(1023);
        scalarSliderA5.setMinorTickSpacing(171);
        scalarSliderA5.setOrientation(javax.swing.JSlider.VERTICAL);
        scalarSliderA5.setPaintTicks(true);
        scalarSliderA5.setToolTipText("Voltage for pin A5");
        scalarSliderA5.setValue(0);
        scalarSliderA5.setMinimumSize(new java.awt.Dimension(33, 100));
        scalarSliderA5.setName("");
        scalarSliderA5.setPreferredSize(new java.awt.Dimension(33, 100));
        scalarSliderA5.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                scalarSliderA5StateChanged(evt);
            }
        });
        scalarPanel6.add(scalarSliderA5);
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("A5");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel8.setMaximumSize(new java.awt.Dimension(30, 12));
        jLabel8.setMinimumSize(new java.awt.Dimension(25, 12));
        jLabel8.setPreferredSize(new java.awt.Dimension(25, 12));
        jLabel8.setRequestFocusEnabled(false);
        jLabel8.setVerifyInputWhenFocusTarget(false);
        scalarPanel6.add(jLabel8);
        jPanel2.add(scalarPanel6);
        jTabbedPane1.addTab("Analog In", null, jPanel2, "Set analog voltages");
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 3));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setMinimumSize(new java.awt.Dimension(300, 125));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 125));
        jPanel4.setLayout(new java.awt.GridLayout(5, 0));
        jPanel4.setMinimumSize(new java.awt.Dimension(150, 120));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(150, 120));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));
        jPanel5.setOpaque(false);
        jLabel9.setText("D0 ");
        jLabel9.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel9.setMaximumSize(new java.awt.Dimension(22, 18));
        jLabel9.setPreferredSize(new java.awt.Dimension(22, 18));
        jPanel5.add(jLabel9);
        buttonGroupD0.add(lowButtonD0);
        lowButtonD0.setSelected(true);
        lowButtonD0.setText("Low ");
        lowButtonD0.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lowButtonD0.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lowButtonD0.setOpaque(false);
        lowButtonD0.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowButtonD0ActionPerformed(evt);
            }
        });
        jPanel5.add(lowButtonD0);
        buttonGroupD0.add(highButtonD0);
        highButtonD0.setText("High");
        highButtonD0.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        highButtonD0.setMargin(new java.awt.Insets(0, 0, 0, 0));
        highButtonD0.setOpaque(false);
        highButtonD0.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highButtonD0ActionPerformed(evt);
            }
        });
        jPanel5.add(highButtonD0);
        jPanel4.add(jPanel5);
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));
        jPanel6.setOpaque(false);
        jLabel10.setText("D1 ");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel10.setMaximumSize(new java.awt.Dimension(22, 18));
        jLabel10.setPreferredSize(new java.awt.Dimension(22, 18));
        jPanel6.add(jLabel10);
        buttonGroupD1.add(lowButtonD1);
        lowButtonD1.setSelected(true);
        lowButtonD1.setText("Low ");
        lowButtonD1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lowButtonD1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lowButtonD1.setOpaque(false);
        lowButtonD1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowButtonD1ActionPerformed(evt);
            }
        });
        jPanel6.add(lowButtonD1);
        buttonGroupD1.add(highButtonD1);
        highButtonD1.setText("High");
        highButtonD1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        highButtonD1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        highButtonD1.setOpaque(false);
        highButtonD1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highButtonD1ActionPerformed(evt);
            }
        });
        jPanel6.add(highButtonD1);
        jPanel4.add(jPanel6);
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));
        jPanel7.setOpaque(false);
        jLabel11.setText("D2 ");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel11.setPreferredSize(new java.awt.Dimension(22, 18));
        jPanel7.add(jLabel11);
        buttonGroupD2.add(lowButtonD2);
        lowButtonD2.setSelected(true);
        lowButtonD2.setText("Low ");
        lowButtonD2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lowButtonD2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lowButtonD2.setOpaque(false);
        lowButtonD2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowButtonD2ActionPerformed(evt);
            }
        });
        jPanel7.add(lowButtonD2);
        buttonGroupD2.add(highButtonD2);
        highButtonD2.setText("High");
        highButtonD2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        highButtonD2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        highButtonD2.setOpaque(false);
        highButtonD2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highButtonD2ActionPerformed(evt);
            }
        });
        jPanel7.add(highButtonD2);
        jPanel4.add(jPanel7);
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));
        jPanel8.setOpaque(false);
        jLabel12.setText("D3 ");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel12.setPreferredSize(new java.awt.Dimension(22, 18));
        jPanel8.add(jLabel12);
        buttonGroupD3.add(lowButtonD3);
        lowButtonD3.setSelected(true);
        lowButtonD3.setText("Low ");
        lowButtonD3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lowButtonD3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lowButtonD3.setOpaque(false);
        lowButtonD3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowButtonD3ActionPerformed(evt);
            }
        });
        jPanel8.add(lowButtonD3);
        buttonGroupD3.add(highButtonD3);
        highButtonD3.setText("High");
        highButtonD3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        highButtonD3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        highButtonD3.setOpaque(false);
        highButtonD3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highButtonD3ActionPerformed(evt);
            }
        });
        jPanel8.add(highButtonD3);
        jPanel4.add(jPanel8);
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));
        jPanel9.setOpaque(false);
        jLabel13.setText("D4 ");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel13.setPreferredSize(new java.awt.Dimension(22, 18));
        jPanel9.add(jLabel13);
        buttonGroupD4.add(lowButtonD4);
        lowButtonD4.setSelected(true);
        lowButtonD4.setText("Low ");
        lowButtonD4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lowButtonD4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lowButtonD4.setOpaque(false);
        lowButtonD4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lowButtonD4ActionPerformed(evt);
            }
        });
        jPanel9.add(lowButtonD4);
        buttonGroupD4.add(highButtonD4);
        highButtonD4.setText("High");
        highButtonD4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        highButtonD4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        highButtonD4.setOpaque(false);
        highButtonD4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highButtonD4ActionPerformed(evt);
            }
        });
        jPanel9.add(highButtonD4);
        jPanel4.add(jPanel9);
        jPanel3.add(jPanel4);
        jPanel10.setLayout(new java.awt.GridLayout(5, 0));
        jPanel10.setMinimumSize(new java.awt.Dimension(127, 120));
        jPanel10.setOpaque(false);
        jPanel10.setPreferredSize(new java.awt.Dimension(127, 120));
        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 2));
        jPanel11.setOpaque(false);
        jLabel14.setText("H0 ");
        jPanel11.add(jLabel14);
        stateLabelH0.setText("Low ");
        jPanel11.add(stateLabelH0);
        jPanel10.add(jPanel11);
        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 2));
        jPanel12.setOpaque(false);
        jLabel15.setText("H1 ");
        jPanel12.add(jLabel15);
        stateLabelH1.setText("Low ");
        jPanel12.add(stateLabelH1);
        jPanel10.add(jPanel12);
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 2));
        jPanel13.setOpaque(false);
        jLabel16.setText("H2 ");
        jPanel13.add(jLabel16);
        stateLabelH2.setText("Low ");
        jPanel13.add(stateLabelH2);
        jPanel10.add(jPanel13);
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 2));
        jPanel14.setOpaque(false);
        jLabel17.setText("H3 ");
        jPanel14.add(jLabel17);
        stateLabelH3.setText("Low ");
        jPanel14.add(stateLabelH3);
        jPanel10.add(jPanel14);
        jPanel3.add(jPanel10);
        jTabbedPane1.addTab("Digital Pins", null, jPanel3, "Set digital inputs and display output pins");
        jLayeredPane1.setMinimumSize(new java.awt.Dimension(300, 120));
        accelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        accelLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        accelLabel.setMinimumSize(new java.awt.Dimension(150, 100));
        accelLabel.setPreferredSize(new java.awt.Dimension(150, 100));
        accelLabel.setBounds(80, -1, 150, 100);
        jLayeredPane1.add(accelLabel, new Integer(1));
        zSlider.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        zSlider.setMajorTickSpacing(300);
        zSlider.setMaximum(600);
        zSlider.setMinimum(-600);
        zSlider.setMinorTickSpacing(100);
        zSlider.setOrientation(javax.swing.JSlider.VERTICAL);
        zSlider.setPaintTicks(true);
        zSlider.setToolTipText("Acceleration along Z-axis");
        zSlider.setValue(1);
        zSlider.setMinimumSize(new java.awt.Dimension(51, 100));
        zSlider.setPreferredSize(new java.awt.Dimension(51, 100));
        zSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zSliderStateChanged(evt);
            }
        });
        zSlider.setBounds(0, -7, 51, 100);
        jLayeredPane1.add(zSlider, new Integer(5));
        xSlider.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        xSlider.setMajorTickSpacing(300);
        xSlider.setMaximum(600);
        xSlider.setMinimum(-600);
        xSlider.setMinorTickSpacing(100);
        xSlider.setPaintTicks(true);
        xSlider.setToolTipText("Acceleration along X-axis");
        xSlider.setValue(0);
        xSlider.setMinimumSize(new java.awt.Dimension(100, 50));
        xSlider.setPreferredSize(new java.awt.Dimension(100, 50));
        xSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                xSliderStateChanged(evt);
            }
        });
        xSlider.setBounds(20, 82, 100, 50);
        jLayeredPane1.add(xSlider, new Integer(4));
        ySlider.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        ySlider.setMajorTickSpacing(300);
        ySlider.setMaximum(600);
        ySlider.setMinimum(-600);
        ySlider.setMinorTickSpacing(100);
        ySlider.setPaintTicks(true);
        ySlider.setToolTipText("Acceleration along Y-axis");
        ySlider.setValue(0);
        ySlider.setMinimumSize(new java.awt.Dimension(100, 50));
        ySlider.setPreferredSize(new java.awt.Dimension(100, 50));
        ySlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ySliderStateChanged(evt);
            }
        });
        ySlider.setBounds(160, 82, 100, 50);
        jLayeredPane1.add(ySlider, new Integer(4));
        oneGButton.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        oneGButton.setText("Default 1G");
        oneGButton.setMargin(new java.awt.Insets(3, 8, 3, 8));
        oneGButton.setMaximumSize(new java.awt.Dimension(80, 25));
        oneGButton.setMinimumSize(new java.awt.Dimension(70, 25));
        oneGButton.setOpaque(false);
        oneGButton.setPreferredSize(new java.awt.Dimension(75, 25));
        oneGButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneGButtonActionPerformed(evt);
            }
        });
        oneGButton.setBounds(220, -2, 75, 25);
        jLayeredPane1.add(oneGButton, new Integer(2));
        jLabel18.setText("Z");
        jLabel18.setBounds(45, 0, 8, 16);
        jLayeredPane1.add(jLabel18, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLabel19.setText("X");
        jLabel19.setBounds(120, 100, 10, 16);
        jLayeredPane1.add(jLabel19, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLabel20.setText("Y");
        jLabel20.setBounds(260, 100, 10, 16);
        jLayeredPane1.add(jLabel20, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jTabbedPane1.addTab("Accel", null, jLayeredPane1, "Set acceleration measured by SPOT");
        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));
        jPanel15.setMinimumSize(new java.awt.Dimension(300, 125));
        jPanel15.setOpaque(false);
        jPanel15.setPreferredSize(new java.awt.Dimension(300, 125));
        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        jPanel16.setMinimumSize(new java.awt.Dimension(305, 45));
        jPanel16.setOpaque(false);
        jPanel16.setPreferredSize(new java.awt.Dimension(305, 45));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("LED");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel21.setMaximumSize(new java.awt.Dimension(31, 35));
        jLabel21.setMinimumSize(new java.awt.Dimension(35, 35));
        jLabel21.setPreferredSize(new java.awt.Dimension(35, 35));
        jLabel21.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel16.add(jLabel21);
        jPanel17.setLayout(new java.awt.GridLayout(2, 8, 6, 3));
        jPanel17.setOpaque(false);
        LED1.setBackground(new java.awt.Color(0, 0, 0));
        LED1.setText("  ");
        LED1.setMaximumSize(new java.awt.Dimension(16, 16));
        LED1.setMinimumSize(new java.awt.Dimension(16, 16));
        LED1.setOpaque(true);
        LED1.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED1);
        LED2.setBackground(new java.awt.Color(0, 0, 0));
        LED2.setText("  ");
        LED2.setMaximumSize(new java.awt.Dimension(16, 16));
        LED2.setMinimumSize(new java.awt.Dimension(16, 16));
        LED2.setOpaque(true);
        LED2.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED2);
        LED3.setBackground(new java.awt.Color(0, 0, 0));
        LED3.setText("  ");
        LED3.setMaximumSize(new java.awt.Dimension(16, 16));
        LED3.setMinimumSize(new java.awt.Dimension(16, 16));
        LED3.setOpaque(true);
        LED3.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED3);
        LED4.setBackground(new java.awt.Color(0, 0, 0));
        LED4.setText("  ");
        LED4.setMaximumSize(new java.awt.Dimension(16, 16));
        LED4.setMinimumSize(new java.awt.Dimension(16, 16));
        LED4.setOpaque(true);
        LED4.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED4);
        LED5.setBackground(new java.awt.Color(0, 0, 0));
        LED5.setText("  ");
        LED5.setMaximumSize(new java.awt.Dimension(16, 16));
        LED5.setMinimumSize(new java.awt.Dimension(16, 16));
        LED5.setOpaque(true);
        LED5.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED5);
        LED6.setBackground(new java.awt.Color(0, 0, 0));
        LED6.setText("  ");
        LED6.setMaximumSize(new java.awt.Dimension(16, 16));
        LED6.setMinimumSize(new java.awt.Dimension(16, 16));
        LED6.setOpaque(true);
        LED6.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED6);
        LED7.setBackground(new java.awt.Color(0, 0, 0));
        LED7.setText("  ");
        LED7.setMaximumSize(new java.awt.Dimension(16, 16));
        LED7.setMinimumSize(new java.awt.Dimension(16, 16));
        LED7.setOpaque(true);
        LED7.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED7);
        LED8.setBackground(new java.awt.Color(0, 0, 0));
        LED8.setText("  ");
        LED8.setMaximumSize(new java.awt.Dimension(16, 16));
        LED8.setMinimumSize(new java.awt.Dimension(16, 16));
        LED8.setOpaque(true);
        LED8.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(LED8);
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("1");
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel23.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel23.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel23.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel23);
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("2");
        jLabel24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel24.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel24.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel24.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel24);
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("3");
        jLabel25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel25.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel25.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel25.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel25);
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("4");
        jLabel26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel26.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel26.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel26.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel26);
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("5");
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel27.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel27.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel27.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel27);
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("6");
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel28.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel28.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel28.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel28);
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("7");
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel29.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel29.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel29.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel29);
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("8");
        jLabel30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel30.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel30.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel30.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel17.add(jLabel30);
        jPanel16.add(jPanel17);
        jPanel16.add(jLabel33);
        jPanel18.setLayout(new java.awt.GridLayout(2, 2, 6, 3));
        jPanel18.setOpaque(false);
        jPanel18.setPreferredSize(new java.awt.Dimension(37, 35));
        LED10.setBackground(new java.awt.Color(0, 0, 0));
        LED10.setText("  ");
        LED10.setMaximumSize(new java.awt.Dimension(16, 16));
        LED10.setMinimumSize(new java.awt.Dimension(16, 16));
        LED10.setOpaque(true);
        LED10.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel18.add(LED10);
        LED9.setBackground(new java.awt.Color(0, 0, 0));
        LED9.setText("  ");
        LED9.setMaximumSize(new java.awt.Dimension(16, 16));
        LED9.setMinimumSize(new java.awt.Dimension(16, 16));
        LED9.setOpaque(true);
        LED9.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel18.add(LED9);
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("G");
        jLabel32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel32.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel32.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel32.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel18.add(jLabel32);
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("R");
        jLabel31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel31.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel31.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel31.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel18.add(jLabel31);
        jPanel16.add(jPanel18);
        jPanel15.add(jPanel16);
        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));
        jPanel19.setOpaque(false);
        jLabel22.setText("Switches");
        jPanel19.add(jLabel22);
        switchBox1.setText("SW1");
        switchBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        switchBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        switchBox1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchBox1ActionPerformed(evt);
            }
        });
        jPanel19.add(switchBox1);
        switchBox2.setText("SW2");
        switchBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        switchBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        switchBox2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchBox2ActionPerformed(evt);
            }
        });
        jPanel19.add(switchBox2);
        jPanel15.add(jPanel19);
        jTabbedPane1.addTab("LEDs", null, jPanel15, "Set switches and display LEDs\n");
        add(jTabbedPane1);
    }

    private void switchBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        spot.setSwitch(1, switchBox2.isSelected());
    }

    private void switchBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        spot.setSwitch(0, switchBox1.isSelected());
    }

    private void zSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        accelerometerChanged();
    }

    private void xSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        accelerometerChanged();
    }

    private void ySliderStateChanged(javax.swing.event.ChangeEvent evt) {
        accelerometerChanged();
    }

    private void oneGButtonActionPerformed(java.awt.event.ActionEvent evt) {
        xSlider.setValue(0);
        ySlider.setValue(0);
        zSlider.setValue(accScale == 2 ? 300 : 100);
        accelerometerChanged();
    }

    private void temperatureSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        spot.temperatureChanged(temperatureSlider.getValue());
    }

    private void highButtonD4ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(4, true);
    }

    private void highButtonD3ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(3, true);
    }

    private void highButtonD2ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(2, true);
    }

    private void highButtonD1ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(1, true);
    }

    private void highButtonD0ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(0, true);
    }

    private void lowButtonD4ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(4, false);
    }

    private void lowButtonD3ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(3, false);
    }

    private void lowButtonD2ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(2, false);
    }

    private void lowButtonD1ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(1, false);
    }

    private void lowButtonD0ActionPerformed(java.awt.event.ActionEvent evt) {
        inputPinChanged(0, false);
    }

    private void scalarSliderA5StateChanged(javax.swing.event.ChangeEvent evt) {
        voltageChanged(5, scalarSliderA5.getValue());
    }

    private void scalarSliderA4StateChanged(javax.swing.event.ChangeEvent evt) {
        voltageChanged(4, scalarSliderA4.getValue());
    }

    private void scalarSliderA3StateChanged(javax.swing.event.ChangeEvent evt) {
        voltageChanged(3, scalarSliderA3.getValue());
    }

    private void scalarSliderA2StateChanged(javax.swing.event.ChangeEvent evt) {
        voltageChanged(2, scalarSliderA2.getValue());
    }

    private void scalarSliderA1StateChanged(javax.swing.event.ChangeEvent evt) {
        voltageChanged(1, scalarSliderA1.getValue());
    }

    private void scalarSliderA0StateChanged(javax.swing.event.ChangeEvent evt) {
        voltageChanged(0, scalarSliderA0.getValue());
    }

    private void lightsensorSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        spot.lightSensorChanged(lightsensorSlider.getValue());
    }

    private javax.swing.JLabel LED1;

    private javax.swing.JLabel LED10;

    private javax.swing.JLabel LED2;

    private javax.swing.JLabel LED3;

    private javax.swing.JLabel LED4;

    private javax.swing.JLabel LED5;

    private javax.swing.JLabel LED6;

    private javax.swing.JLabel LED7;

    private javax.swing.JLabel LED8;

    private javax.swing.JLabel LED9;

    private javax.swing.JLabel accelLabel;

    private javax.swing.ButtonGroup buttonGroupD0;

    private javax.swing.ButtonGroup buttonGroupD1;

    private javax.swing.ButtonGroup buttonGroupD2;

    private javax.swing.ButtonGroup buttonGroupD3;

    private javax.swing.ButtonGroup buttonGroupD4;

    private javax.swing.JRadioButton highButtonD0;

    private javax.swing.JRadioButton highButtonD1;

    private javax.swing.JRadioButton highButtonD2;

    private javax.swing.JRadioButton highButtonD3;

    private javax.swing.JRadioButton highButtonD4;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JLabel jLabel29;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel30;

    private javax.swing.JLabel jLabel31;

    private javax.swing.JLabel jLabel32;

    private javax.swing.JLabel jLabel33;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLayeredPane jLayeredPane1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel11;

    private javax.swing.JPanel jPanel12;

    private javax.swing.JPanel jPanel13;

    private javax.swing.JPanel jPanel14;

    private javax.swing.JPanel jPanel15;

    private javax.swing.JPanel jPanel16;

    private javax.swing.JPanel jPanel17;

    private javax.swing.JPanel jPanel18;

    private javax.swing.JPanel jPanel19;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JPanel lightsensorPanel;

    private javax.swing.JSlider lightsensorSlider;

    private javax.swing.JRadioButton lowButtonD0;

    private javax.swing.JRadioButton lowButtonD1;

    private javax.swing.JRadioButton lowButtonD2;

    private javax.swing.JRadioButton lowButtonD3;

    private javax.swing.JRadioButton lowButtonD4;

    private javax.swing.JButton oneGButton;

    private javax.swing.JPanel scalarPanel1;

    private javax.swing.JPanel scalarPanel2;

    private javax.swing.JPanel scalarPanel3;

    private javax.swing.JPanel scalarPanel4;

    private javax.swing.JPanel scalarPanel5;

    private javax.swing.JPanel scalarPanel6;

    private javax.swing.JSlider scalarSliderA0;

    private javax.swing.JSlider scalarSliderA1;

    private javax.swing.JSlider scalarSliderA2;

    private javax.swing.JSlider scalarSliderA3;

    private javax.swing.JSlider scalarSliderA4;

    private javax.swing.JSlider scalarSliderA5;

    private javax.swing.JLabel stateLabelH0;

    private javax.swing.JLabel stateLabelH1;

    private javax.swing.JLabel stateLabelH2;

    private javax.swing.JLabel stateLabelH3;

    private javax.swing.JCheckBox switchBox1;

    private javax.swing.JCheckBox switchBox2;

    private javax.swing.JPanel temperaturePanel;

    private javax.swing.JSlider temperatureSlider;

    private javax.swing.JSlider xSlider;

    private javax.swing.JSlider ySlider;

    private javax.swing.JSlider zSlider;
}
