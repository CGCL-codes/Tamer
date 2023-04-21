package controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.io.*;
import javax.sound.sampled.*;
import view.AboutView;
import model.Simulator;
import controller.ScoreboardController;

/**
 * MenuController
 * @author Simon Hiemstra
 * @version 1
 */
public class MenuController extends JMenuBar implements ActionListener {

    private static final long serialVersionUID = 7211163271822852202L;

    private AudioFormat audioFormat;

    private AudioInputStream audioInputStream;

    private SourceDataLine sourceDataLine;

    private JMenuItem mntmReset;

    private JMenuItem mntmExit;

    private JMenuItem mntmScoreboard;

    private JMenuItem mntmConfiguration;

    private JMenuItem mntmAbout;

    private Simulator simulator;

    public MenuController(Simulator simulator) {
        this.simulator = simulator;
        JMenu mnMenu = new JMenu("File");
        mntmReset = new JMenuItem("Reset");
        mntmReset.addActionListener(this);
        mnMenu.add(mntmReset);
        mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(this);
        mnMenu.add(mntmExit);
        JMenu mnMenuView = new JMenu("View");
        mntmScoreboard = new JMenuItem("Scoreboard");
        mntmScoreboard.addActionListener(this);
        mntmConfiguration = new JMenuItem("Configuration");
        mntmConfiguration.addActionListener(this);
        mnMenuView.add(mntmScoreboard);
        mnMenuView.add(mntmConfiguration);
        JMenu mnHelp = new JMenu("Help");
        mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(this);
        mnHelp.add(mntmAbout);
        add(mnMenu);
        add(mnMenuView);
        add(mnHelp);
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        if (action.getSource().equals(mntmReset)) simulator.reset();
        if (action.getSource().equals(mntmExit)) {
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
            playSound();
        }
        if (action.getSource().equals(mntmScoreboard)) {
            ScoreboardController frame = new ScoreboardController(simulator);
            frame.setVisible(true);
            frame.setSize(500, 300);
            frame.setLocation(200, 200);
            frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        }
        if (action.getSource().equals(mntmConfiguration)) {
            ConfigController frame = new ConfigController(simulator);
            frame.setVisible(true);
            frame.setSize(500, 300);
            frame.setLocation(200, 200);
            frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        }
        if (action.getSource().equals(mntmAbout)) new AboutView();
    }

    private void playSound() {
        try {
            File soundFile = new File("include/sounds/exit.wav");
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audioFormat = audioInputStream.getFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            new PlayThread().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PlayThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        public void run() {
            try {
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
                int count;
                while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (count > 0) {
                        sourceDataLine.write(tempBuffer, 0, count);
                    }
                }
                sourceDataLine.drain();
                sourceDataLine.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
