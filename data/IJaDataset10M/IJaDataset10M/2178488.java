package com.frinika.audio.toot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LatencyTesterPanel extends JPanel {

    private LatencyTester tester;

    private JTextField latencyLabel;

    private JLabel currentVal;

    private JFrame frame;

    public LatencyTesterPanel(JFrame frame1) {
        this.frame = frame1;
        tester = new LatencyTester();
        add(latencyLabel = new JTextField(10));
        int lat = FrinikaAudioSystem.getTotalLatency();
        add(new JLabel("samples latency. (current value=" + lat + ")"));
        tester.addObserver(new Observer() {

            public void update(Observable o, Object arg) {
                String str;
                int latency = tester.getLatencyInSamples();
                if (latency < 0) {
                    str = "No signal";
                } else {
                    str = "" + latency;
                }
                latencyLabel.setText(str);
            }
        });
        JButton set = new JButton("Apply");
        add(set);
        set.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int lat = Integer.parseInt(latencyLabel.getText());
                FrinikaAudioSystem.setTotalLatency(lat);
            }
        });
        JButton reset = new JButton("Reset");
        add(reset);
        set.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tester.reset();
            }
        });
        JButton abort = new JButton("Quit");
        add(abort);
        abort.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        if (frame != null) {
            frame.addWindowListener(new WindowListener() {

                public void windowActivated(WindowEvent e) {
                }

                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    if (tester != null) tester.stop();
                    tester = null;
                    if (frame != null) frame.dispose();
                    frame = null;
                }

                public void windowDeactivated(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowOpened(WindowEvent e) {
                }
            });
            tester.start(frame);
        }
    }

    void dispose() {
        tester.stop();
        tester = null;
        frame.dispose();
        frame = null;
    }
}
