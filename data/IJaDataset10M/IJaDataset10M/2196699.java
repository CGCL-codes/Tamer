package com.degani;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.degani.audio.AudioInput;
import com.degani.audio.AudioOutput;
import com.degani.plugins.PianoPlugin;
import com.degani.plugins.Plugin;
import com.degani.plugins.StaffPlugin;

public class AppMain {

    public static final int DEFAULT_WIDTH = 800;

    public static final int DEFAULT_HEIGHT = 480;

    public static DrawPanel getDrawPanel() {
        return drawPanel;
    }

    public static SidePanel getSidePanel() {
        return sidePanel;
    }

    public static TopPanel getTopPanel() {
        return topPanel;
    }

    public static Container getRootContentPane() {
        return rootContentPane;
    }

    public static AudioInput getAudioInput() {
        return audioInput;
    }

    public static AudioOutput getAudioOutput() {
        return audioOutput;
    }

    public static ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    public static List<Plugin> getPlugins() {
        return plugins;
    }

    private static DrawPanel drawPanel;

    private static SidePanel sidePanel;

    private static TopPanel topPanel;

    private static BottomPanel bottomPanel;

    private static Container rootContentPane;

    private static AudioInput audioInput;

    private static AudioOutput audioOutput;

    private static ScheduledExecutorService scheduledExecutor;

    private static List<Plugin> plugins;

    public static void main(String args[]) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background", new Color(250, 250, 250));
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        final JFrame frame = new JFrame("Blackboard Video");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        final Container contentPane = frame.getContentPane();
        scheduledExecutor = Executors.newScheduledThreadPool(2);
        audioInput = new AudioInput();
        audioOutput = new AudioOutput();
        drawPanel = new DrawPanel();
        sidePanel = new SidePanel(drawPanel);
        topPanel = new TopPanel();
        bottomPanel = new BottomPanel();
        rootContentPane = contentPane;
        initPlugins();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(sidePanel, BorderLayout.WEST);
        contentPane.add(drawPanel, BorderLayout.EAST);
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        frame.pack();
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private static void initPlugins() {
        plugins = new ArrayList<Plugin>();
        plugins.add(new StaffPlugin());
        plugins.add(new PianoPlugin());
    }
}
