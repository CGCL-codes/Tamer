package simpatest;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.*;
import alice.cartago.*;

public class MyGUITimer extends GUIArtifact {

    private MyFrame frame;

    private boolean alarmOn;

    @OPERATION
    void init() throws CartagoException {
        frame = new MyFrame();
        frame.setVisible(true);
        alarmOn = false;
        linkActionEventToOp(frame.setAlarm, "setAlarm");
        linkActionEventToOp(frame.stopAlarm, "stopAlarm");
        mapWindowClosingEvent(frame, "closed");
    }

    @OPERATION
    void setAlarm(ActionEvent ev) {
        int dt = Integer.parseInt(frame.editTimer.getText());
        frame.setButton(false);
        alarmOn = true;
        nextTimedStep("alarm", dt, dt);
    }

    @OPSTEP
    void alarm(int dt) {
        if (alarmOn) {
            frame.setSignal();
            signal("alarm");
            nextTimedStep("alarm", dt, dt);
        }
    }

    @OPERATION
    void stopAlarm(ActionEvent ev) {
        alarmOn = false;
        frame.setButton(true);
    }

    @OPERATION
    void switchOn() {
        nextStep("tick");
    }

    @OPSTEP(tguard = 1000)
    void tick() {
        frame.setTimer(new GregorianCalendar().getTime().toString());
        nextStep("tick");
    }

    class MyFrame extends JFrame {

        private static final long serialVersionUID = 1L;

        private JButton setAlarm;

        private JButton stopAlarm;

        private JTextField timerText;

        private JTextField editTimer;

        private boolean b = true;

        public MyFrame() {
            setTitle("Timer GUI Artifact");
            setSize(800, 60);
            JPanel panel = new JPanel();
            setContentPane(panel);
            setAlarm = new JButton("setAlarm");
            setAlarm.setSize(10, 50);
            stopAlarm = new JButton("stopAlarm");
            stopAlarm.setSize(10, 50);
            stopAlarm.setEnabled(false);
            timerText = new JTextField(25);
            timerText.setEditable(false);
            editTimer = new JTextField(10);
            panel.add(timerText);
            panel.add(setAlarm);
            panel.add(editTimer);
            panel.add(stopAlarm);
        }

        private void setButton(boolean b) {
            setAlarm.setEnabled(b);
            editTimer.setEnabled(b);
            stopAlarm.setEnabled(!b);
            editTimer.setBackground(Color.WHITE);
        }

        private void setTimer(final String time) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    timerText.setText(time);
                }
            });
        }

        private void setSignal() {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (b) {
                        editTimer.setBackground(Color.RED);
                        b = false;
                    } else {
                        editTimer.setBackground(Color.WHITE);
                        b = true;
                    }
                }
            });
        }
    }
}
