package com.agentfactory.vacworld.vacGui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Top level GUI object.
 * Constructor creates and shows the entire GUI.
 */
public class AppView implements PropertyChangeListener {

    private final JLabel dustCleanedLabel;

    private final JLabel elapsedTimeLabel;

    public AppView(VacWorld world) {
        for (VacBot vacBot : VacWorld.getVacBots()) vacBot.addPropertyChangeListener(this);
        Timer timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                timerUpdate();
            }
        });
        JFrame frame = new JFrame("VacWorld");
        GridView grid = new GridView(world);
        frame.add(grid);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        dustCleanedLabel = new JLabel(getDustStatus());
        infoPanel.add(dustCleanedLabel);
        infoPanel.add(new JLabel("      "));
        elapsedTimeLabel = new JLabel(getTimeStatus());
        infoPanel.add(elapsedTimeLabel);
        frame.add(infoPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        timer.start();
    }

    private int dustCleaned = 0;

    private String getDustStatus() {
        return String.format("Dust cleaned: %02d", dustCleaned);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(VacBot.CLEAN_STOP)) {
            ++dustCleaned;
            dustCleanedLabel.setText(getDustStatus());
        }
    }

    private int elapsedSeconds = 0;

    private int elapsedMinutes = 0;

    private int elapsedHours = 0;

    private String getTimeStatus() {
        return String.format("Elapsed time: %02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    public void timerUpdate() {
        ++elapsedSeconds;
        if (elapsedSeconds >= 60) {
            elapsedSeconds = 0;
            ++elapsedMinutes;
            if (elapsedMinutes >= 60) {
                elapsedMinutes = 0;
                ++elapsedHours;
            }
        }
        elapsedTimeLabel.setText(getTimeStatus());
    }
}
