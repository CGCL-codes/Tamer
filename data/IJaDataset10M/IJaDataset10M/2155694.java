package org.myrobotlab.control;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.myrobotlab.service.Clock;
import org.myrobotlab.service.Clock.PulseDataType;
import org.myrobotlab.service.interfaces.GUI;
import org.myrobotlab.service.Runtime;

public class ClockGUI extends ServiceGUI implements ActionListener {

    static final long serialVersionUID = 1L;

    JButton startClock = null;

    ButtonGroup group = new ButtonGroup();

    JRadioButton none = new JRadioButton("none");

    JRadioButton increment = new JRadioButton("increment");

    JRadioButton integer = new JRadioButton("integer");

    JRadioButton string = new JRadioButton("string");

    JTextField interval = new JTextField("1000");

    JTextField pulseDataString = new JTextField(10);

    JIntegerField pulseDataInteger = new JIntegerField(10);

    Clock myClock = null;

    public ClockGUI(final String boundServiceName, final GUI myService) {
        super(boundServiceName, myService);
    }

    ActionListener setType = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            myClock.setType(((JRadioButton) e.getSource()).getText());
        }
    };

    public void init() {
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        display.add(getstartClockButton(), gc);
        gc.gridwidth = 1;
        ++gc.gridx;
        display.add(new JLabel("  interval  "), gc);
        ++gc.gridx;
        display.add(interval, gc);
        ++gc.gridx;
        display.add(new JLabel("  ms  "), gc);
        JPanel pulseData = new JPanel(new GridBagLayout());
        TitledBorder title;
        title = BorderFactory.createTitledBorder("pulse data");
        pulseData.setBorder(title);
        none.setActionCommand("none");
        none.setSelected(true);
        none.addActionListener(setType);
        increment.setActionCommand("increment");
        increment.addActionListener(setType);
        integer.setActionCommand("integer");
        integer.addActionListener(setType);
        string.setActionCommand("string");
        string.addActionListener(setType);
        group.add(none);
        group.add(increment);
        group.add(integer);
        group.add(string);
        gc.gridx = 0;
        gc.gridy = 0;
        pulseData.add(none, gc);
        ++gc.gridy;
        pulseData.add(increment, gc);
        ++gc.gridy;
        pulseData.add(integer, gc);
        ++gc.gridx;
        pulseData.add(pulseDataInteger, gc);
        gc.gridx = 0;
        ++gc.gridy;
        pulseData.add(string, gc);
        ++gc.gridx;
        pulseData.add(pulseDataString, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        display.add(pulseData, gc);
        myClock = (Clock) Runtime.getService(boundServiceName).service;
    }

    public JButton getstartClockButton() {
        if (startClock == null) {
            startClock = new JButton("start clock");
            startClock.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startClock.getText().compareTo("start clock") == 0) {
                        startClock.setText("stop clock");
                        myClock.interval = Integer.parseInt(interval.getText());
                        myClock.pulseDataInteger = Integer.parseInt(pulseDataInteger.getText());
                        myClock.pulseDataString = pulseDataString.getText();
                        myService.send(boundServiceName, "setState", myClock);
                        myService.send(boundServiceName, "publishState");
                        myService.send(boundServiceName, "startClock");
                    } else {
                        startClock.setText("start clock");
                        myService.send(boundServiceName, "stopClock");
                    }
                }
            });
        }
        return startClock;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        myService.send(boundServiceName, "setType", e.getActionCommand());
    }

    public void getState(Clock c) {
        if (c != null) {
            if (c.pulseDataType == PulseDataType.increment) {
                increment.setSelected(true);
            } else if (c.pulseDataType == PulseDataType.integer) {
                integer.setSelected(true);
            } else if (c.pulseDataType == PulseDataType.string) {
                string.setSelected(true);
            } else if (c.pulseDataType == PulseDataType.none) {
                none.setSelected(true);
            }
            pulseDataString.setText(c.pulseDataString);
            pulseDataInteger.setInt(c.pulseDataInteger);
            interval.setText((c.interval + ""));
            if (c.isClockRunning) {
                startClock.setText("stop clock");
            } else {
                startClock.setText("start clock");
            }
        }
    }

    @Override
    public void attachGUI() {
        sendNotifyRequest("publishState", "getState", Clock.class);
        myService.send(boundServiceName, "publishState");
    }

    @Override
    public void detachGUI() {
        removeNotifyRequest("publishState", "getState", Clock.class);
    }
}
