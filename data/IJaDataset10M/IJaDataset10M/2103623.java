package de.jlab.ui.modules.panels.dcg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import de.jlab.GlobalsLocator;
import de.jlab.boards.Board;
import de.jlab.boards.DCGBoard;
import de.jlab.lab.Lab;
import de.jlab.lab.SubChannelUpdatedNotification;
import de.jlab.ui.tools.DigitalPanel;
import de.jlab.ui.tools.JSliderForDoubles;

public class DCGControlPanel extends JPanel implements Observer {

    double currVoltage = 0;

    double currCurrent = 0;

    double currPower = 0;

    double currResistence = 0;

    double currCurrentInAmpere = 0;

    int currRipplePercentage = 0;

    int currRippleOn = 0;

    int currRippleOff = 0;

    Lab theLab = null;

    DigitalPanel digitalPanelVoltage = null;

    DigitalPanel digitalPanelCurrent = null;

    DigitalPanel digitalPanelPower = null;

    DigitalPanel digitalPanelResistance = null;

    JSliderForDoubles sliderVoltage = null;

    JSliderForDoubles sliderAmpere = null;

    JSlider sliderRipplePercent = new JSlider(0, 100, 0);

    DecimalFormat format = new DecimalFormat("#0.00");

    DecimalFormat powerformat = new DecimalFormat("#0.000");

    DecimalFormat resistenceformat = new DecimalFormat("#0.000");

    DecimalFormat formatRipplePercentOutput = new DecimalFormat("#0");

    JLabel jLabelVolt = new JLabel("V");

    JLabel jLabelAmpere = new JLabel("A");

    JLabel jLabelMilliAmpere = new JLabel("mA");

    JPanel jPanelVoltageEntry = new JPanel();

    JPanel jPanelCurrentEntry = new JPanel();

    JLabel jLabelPowerUnit = new JLabel("W");

    JLabel jLabelResistenceUnit = new JLabel("kΩ");

    JLabel jLabelPower = new JLabel(GlobalsLocator.translate("label-dcg-load-power"));

    JLabel jLabelResistence = new JLabel(GlobalsLocator.translate("label-dcg-load-resistence"));

    JLabel jLabelRipple = new JLabel(GlobalsLocator.translate("label-dcg-ripple"));

    JLabel jLabelRippleOn = new JLabel(GlobalsLocator.translate("label-dcg-ripple-on"));

    JLabel jLabelRippleOff = new JLabel(GlobalsLocator.translate("label-dcg-ripple-off"));

    JLabel jLabelRippleOnMs = new JLabel("ms");

    JLabel jLabelRippleOffMs = new JLabel("ms");

    JLabel jLabelRippleValuePercent = new JLabel();

    JLabel jLabelTemperature = new JLabel(GlobalsLocator.translate("label-dcg-temperature"));

    JLabel jLabelTemperatureValue = new JLabel();

    JFormattedTextField jTextFieldVoltage = new JFormattedTextField(new DecimalFormat("#0.0#"));

    JFormattedTextField jTextFieldCurrent = new JFormattedTextField(new DecimalFormat("#0.0#"));

    JFormattedTextField jTextFieldMilliCurrent = new JFormattedTextField(new DecimalFormat("#0.000#"));

    JFormattedTextField jTextFieldRippleOn = new JFormattedTextField(new DecimalFormat("#0"));

    JFormattedTextField jTextFieldRippleOff = new JFormattedTextField(new DecimalFormat("#0"));

    JRadioButton radioButtonAmpere = new JRadioButton("A");

    JRadioButton radioButtonMilliAmpere = new JRadioButton("mA");

    ButtonGroup bg = new ButtonGroup();

    JPanel jPanelRipple = new JPanel();

    JPanel panelCurrentRange = new JPanel();

    JToggleButton jButtonOnOff = new JToggleButton(GlobalsLocator.translate("button-dcg-on"));

    JButton jButtonAsDefault = new JButton(GlobalsLocator.translate("button-dcg-default"));

    boolean milliAmpere = true;

    JPanel jPanelCommand = new JPanel();

    Timer userActionTimer = new Timer("DCG User Action Timer");

    UserActionTimerTask userActionTimerTask = null;

    JPanel jPanelLoadData = new JPanel();

    DecimalFormat dfTemperature = new DecimalFormat("#0.0°C");

    DCGBoard dcgBoard = null;

    public DCGControlPanel(Lab lab, Board aBoard) {
        dcgBoard = (DCGBoard) aBoard;
        theLab = lab;
        initUI();
    }

    private void initUI() {
        this.setPreferredSize(new Dimension(300, 440));
        jPanelRipple.setLayout(new GridBagLayout());
        sliderVoltage = new JSliderForDoubles(JSlider.HORIZONTAL, 0, dcgBoard.getMaxVoltage(), 0, 2);
        sliderAmpere = new JSliderForDoubles(JSlider.HORIZONTAL, 0, dcgBoard.getMaxCurrent(), 0, 3);
        double voltageSpacing = dcgBoard.getMaxVoltage() / 8;
        double currentSpacing = dcgBoard.getMaxCurrent() / 5;
        sliderVoltage.setTickSpacings(voltageSpacing, voltageSpacing / 10, "#0.00V");
        sliderAmpere.setTickSpacings(currentSpacing, currentSpacing / 5, "#0.000A");
        sliderRipplePercent.setMajorTickSpacing(10);
        sliderRipplePercent.setMinorTickSpacing(2);
        sliderRipplePercent.setPaintTicks(true);
        sliderRipplePercent.setPaintLabels(true);
        jPanelLoadData.setLayout(new GridBagLayout());
        bg.add(radioButtonAmpere);
        bg.add(radioButtonMilliAmpere);
        panelCurrentRange.add(radioButtonAmpere);
        panelCurrentRange.add(radioButtonMilliAmpere);
        radioButtonMilliAmpere.setSelected(true);
        this.setLayout(new GridBagLayout());
        jPanelVoltageEntry.add(jTextFieldVoltage);
        jPanelVoltageEntry.add(jLabelVolt);
        jTextFieldVoltage.setColumns(4);
        jTextFieldRippleOn.setColumns(4);
        jTextFieldRippleOff.setColumns(4);
        jTextFieldMilliCurrent.setColumns(5);
        jTextFieldCurrent.setColumns(5);
        jPanelCurrentEntry.setLayout(new GridBagLayout());
        jPanelCurrentEntry.add(jTextFieldCurrent, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelCurrentEntry.add(jLabelAmpere, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        jPanelCurrentEntry.add(jTextFieldMilliCurrent, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelCurrentEntry.add(jLabelMilliAmpere, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        digitalPanelVoltage = new DigitalPanel(format);
        digitalPanelVoltage.setBackground(Color.BLACK);
        digitalPanelVoltage.setColorDigits(Color.red);
        digitalPanelCurrent = new DigitalPanel(format);
        digitalPanelCurrent.setBackground(Color.BLACK);
        digitalPanelCurrent.setColorDigits(Color.red);
        digitalPanelPower = new DigitalPanel(format);
        digitalPanelPower.setBackground(Color.BLACK);
        digitalPanelPower.setColorDigits(Color.red);
        digitalPanelResistance = new DigitalPanel(format);
        digitalPanelResistance.setBackground(Color.BLACK);
        digitalPanelResistance.setColorDigits(Color.red);
        jTextFieldVoltage.addKeyListener(new EnterKeyListener(jTextFieldVoltage, sliderVoltage, 0, 2));
        jTextFieldCurrent.addKeyListener(new EnterKeyListener(jTextFieldCurrent, sliderAmpere, 1, 3));
        jTextFieldMilliCurrent.addKeyListener(new EnterKeyListener(jTextFieldMilliCurrent, 2, 3));
        jTextFieldRippleOn.addKeyListener(new EnterKeyListener(jTextFieldRippleOn, null, DCGBoard.CHANNEL_NOMINAL_RIPPLE_ON, DCGBoard.CHANNEL_NOMINAL_RIPPLE_ON));
        jTextFieldRippleOff.addKeyListener(new EnterKeyListener(jTextFieldRippleOff, null, DCGBoard.CHANNEL_NOMINAL_RIPPLE_OFF, DCGBoard.CHANNEL_NOMINAL_RIPPLE_OFF));
        jPanelRipple.add(jLabelRipple, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanelRipple.add(sliderRipplePercent, new GridBagConstraints(1, 0, 6, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        jPanelRipple.add(jLabelRippleValuePercent, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        jPanelRipple.add(jLabelRippleOn, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        jPanelRipple.add(jTextFieldRippleOn, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        jPanelRipple.add(jLabelRippleOnMs, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        jPanelRipple.add(jLabelRippleOff, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        jPanelRipple.add(jTextFieldRippleOff, new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        jPanelRipple.add(jLabelRippleOffMs, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
        this.add(digitalPanelVoltage, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelVoltageEntry, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(digitalPanelPower, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jLabelPowerUnit, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(sliderVoltage, new GridBagConstraints(0, 1, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
        this.add(panelCurrentRange, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(digitalPanelCurrent, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelCurrentEntry, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(digitalPanelResistance, new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jLabelResistenceUnit, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(sliderAmpere, new GridBagConstraints(0, 4, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelRipple, new GridBagConstraints(0, 5, 4, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelCommand, new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelCommand.add(jButtonOnOff);
        jButtonOnOff.setSelected(true);
        jPanelCommand.add(jButtonAsDefault);
        jPanelCommand.add(jLabelTemperature);
        jPanelCommand.add(jLabelTemperatureValue);
        sliderVoltage.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (userActionTimerTask != null) {
                    userActionTimerTask.cancel();
                }
                userActionTimerTask = new UserActionTimerTask(sliderVoltage.getDoubleValue(), -1, -1);
                userActionTimer.schedule(userActionTimerTask, 300);
            }
        });
        sliderAmpere.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (userActionTimerTask != null) {
                    userActionTimerTask.cancel();
                }
                userActionTimerTask = new UserActionTimerTask(-1, sliderAmpere.getDoubleValue(), -1);
                userActionTimer.schedule(userActionTimerTask, 300);
            }
        });
        sliderRipplePercent.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (userActionTimerTask != null) {
                    userActionTimerTask.cancel();
                }
                userActionTimerTask = new UserActionTimerTask(-1, -1, sliderRipplePercent.getValue());
                userActionTimer.schedule(userActionTimerTask, 300);
            }
        });
        radioButtonAmpere.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                milliAmpere = false;
                digitalPanelCurrent.setFormat(new DecimalFormat("#0.000"));
            }
        });
        radioButtonMilliAmpere.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                milliAmpere = true;
                digitalPanelCurrent.setFormat(new DecimalFormat("#0.00"));
            }
        });
        jButtonAsDefault.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double voltage = dcgBoard.queryNominalVoltage();
                double ampere = dcgBoard.queryNominalCurrent();
                dcgBoard.setDefaultVoltage(voltage);
                dcgBoard.setDefaultCurrent(ampere);
                dcgBoard.setDefaultNominalRippleOn(((Integer) jTextFieldRippleOn.getValue()).intValue());
                dcgBoard.setDefaultNominalRippleOff(((Integer) jTextFieldRippleOff.getValue()).intValue());
                dcgBoard.setDefaultNominalRipplePercentage(currRipplePercentage);
            }
        });
        jButtonOnOff.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (jButtonOnOff.isSelected()) {
                    jButtonOnOff.setText(GlobalsLocator.translate("button-dcg-on"));
                    dcgBoard.setVoltagePercentage(100);
                    dcgBoard.setCurrentPercentage(100);
                } else {
                    jButtonOnOff.setText(GlobalsLocator.translate("button-dcg-off"));
                    dcgBoard.setVoltagePercentage(0);
                    dcgBoard.setCurrentPercentage(0);
                }
            }
        });
        sliderVoltage.setDoubleValue(dcgBoard.queryNominalVoltage());
        sliderAmpere.setDoubleValue(dcgBoard.queryNominalCurrent());
        sliderRipplePercent.setValue(dcgBoard.queryNominalRipplePercentage());
        jTextFieldRippleOn.setValue(dcgBoard.queryNominalRippleOn());
        jTextFieldRippleOff.setValue(dcgBoard.queryNominalRippleOff());
        jTextFieldCurrent.setValue(dcgBoard.queryNominalCurrent());
        jTextFieldMilliCurrent.setValue(dcgBoard.queryNominalMilliCurrent());
        jTextFieldVoltage.setValue(dcgBoard.queryNominalVoltage());
    }

    public void setVoltage(double voltage) {
        dcgBoard.setVoltage(voltage);
        dcgBoard.queryVoltageAsynchronously();
        jTextFieldVoltage.setValue(voltage);
    }

    public void setCurrent(double ampere) {
        dcgBoard.setCurrent(ampere);
        jTextFieldCurrent.setValue(ampere);
        if (milliAmpere) dcgBoard.queryMilliCurrentAsynchronously(); else dcgBoard.queryCurrentAsynchronously();
    }

    public void setRipplePercent(int ripplePercent) {
        this.currRipplePercentage = ripplePercent;
        dcgBoard.setNominalRipplePercentage(ripplePercent);
        jLabelRippleValuePercent.setText(formatRipplePercentOutput.format(ripplePercent));
    }

    public double checkVoltage(double wantedVoltage) {
        if (wantedVoltage < 0 || wantedVoltage > dcgBoard.getMaxVoltage()) wantedVoltage = currVoltage;
        return wantedVoltage;
    }

    public double checkCurrent(double wantedCurrent) {
        if (wantedCurrent < 0 || wantedCurrent > dcgBoard.getMaxCurrent()) wantedCurrent = currCurrent;
        return wantedCurrent;
    }

    public void update(Observable o, Object arg) {
        if (!(arg instanceof SubChannelUpdatedNotification)) return;
        SubChannelUpdatedNotification notification = (SubChannelUpdatedNotification) arg;
        if (notification.fitsBoard(dcgBoard)) {
            if (notification.getSubchannel() == DCGBoard.CHANNEL_ACTUAL_VOLTAGE) {
                currVoltage = notification.getDoubleValue();
                digitalPanelVoltage.setValue(currVoltage);
            }
            if (notification.getSubchannel() == DCGBoard.CHANNEL_ACTUAL_CURRENT && !milliAmpere) {
                currCurrent = notification.getDoubleValue();
                digitalPanelCurrent.setValue(currCurrent);
                currCurrentInAmpere = currCurrent;
            }
            if (notification.getSubchannel() == DCGBoard.CHANNEL_ACTUAL_MILLICURRENT && milliAmpere) {
                currCurrent = notification.getDoubleValue();
                digitalPanelCurrent.setValue(currCurrent);
                currCurrentInAmpere = currCurrent / 1000;
            }
            if (notification.getSubchannel() == DCGBoard.CHANNEL_TEMPERATURE) {
                double currTemp = notification.getDoubleValue();
                jLabelTemperatureValue.setText(dfTemperature.format(currTemp));
            }
            if (notification.getSubchannel() == DCGBoard.CHANNEL_NOMINAL_RIPPLE_ON) {
                int currOn = (int) notification.getDoubleValue();
                jTextFieldRippleOn.setValue(currOn);
            }
            if (notification.getSubchannel() == DCGBoard.CHANNEL_NOMINAL_RIPPLE_OFF) {
                int currOff = (int) notification.getDoubleValue();
                jTextFieldRippleOff.setValue(currOff);
            }
            if (notification.getSubchannel() == DCGBoard.CHANNEL_NOMINAL_RIPPLE_PERCENT) {
                int currPercentage = (int) notification.getDoubleValue();
                sliderRipplePercent.setValue(currPercentage);
            }
            currPower = currVoltage * currCurrentInAmpere;
            currResistence = currVoltage / currCurrentInAmpere;
            digitalPanelPower.setValue(currPower);
            digitalPanelResistance.setValue(currResistence / 1000);
        }
    }

    class UserActionTimerTask extends TimerTask {

        double setVoltage = -1;

        double setAmpere = -1;

        int setRipple = -1;

        public UserActionTimerTask(double setVoltage, double setAmpere, int setRipple) {
            super();
            this.setVoltage = setVoltage;
            this.setAmpere = setAmpere;
            this.setRipple = setRipple;
        }

        public void run() {
            if (setVoltage != -1) {
                setVoltage(setVoltage);
            }
            if (setAmpere != -1) {
                setCurrent(setAmpere);
            }
            if (setRipple != -1) {
                setRipplePercent(setRipple);
            }
            userActionTimerTask = null;
        }
    }

    class EnterKeyListener extends KeyAdapter {

        JFormattedTextField datafield = null;

        int setChannel = 0;

        int queryChannel = 0;

        JSliderForDoubles slider = null;

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                try {
                    datafield.commitEdit();
                } catch (ParseException e1) {
                }
                double newValue = 0;
                Object value = datafield.getValue();
                if (value == null) return;
                if (value instanceof Long) newValue = (Long) value; else newValue = (Double) value;
                if (slider != null) slider.setDoubleValue(newValue);
                dcgBoard.sendCommand(setChannel, newValue);
                dcgBoard.queryValueAsynchronously(queryChannel);
            }
        }

        public EnterKeyListener(JFormattedTextField datafield, int setChannel, int queryChannel) {
            super();
            this.datafield = datafield;
            this.setChannel = setChannel;
            this.queryChannel = queryChannel;
        }

        public EnterKeyListener(JFormattedTextField datafield, JSliderForDoubles slider, int setChannel, int queryChannel) {
            super();
            this.datafield = datafield;
            this.setChannel = setChannel;
            this.queryChannel = queryChannel;
            this.slider = slider;
        }
    }
}
