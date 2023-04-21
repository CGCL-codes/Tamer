package net.url404.umodulargui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import net.url404.umodular.DebugTools;
import net.url404.umodular.AudioPort;
import net.url404.umodular.SoundModule;
import net.url404.umodular.ComponentConnector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * The GUI desktop, everything on it: toolbars, menus, the
 * actual workspace.
 *
 * @author makela@url404.net
 */
public class GUIDesktop {

    /** Application desktop
   */
    public JDesktopPane desktopPane;

    /** Menu bar
   */
    public GUIMenuBar menuBar;

    /** Tool bar
   */
    public GUIToolBar toolBar;

    private JLabel statusBar;

    private String statusMsg = "Welcome to GUI " + GUIProperties.VERSION_STRING + " | build " + "$Id: GUIDesktop.java,v 1.1.1.1 2003-01-28 19:45:28 mamakela Exp $";

    private JFrame mainFrame;

    private SoundModuleWindow soundModuleWin;

    private ArrayList moduleWindows;

    private AudioPort audioport;

    private AudioPlayback audioPlayback;

    private SourceDataLine audioOutLine;

    /**
   * Construct with a link to the main JFrame.
   *
   * @param mainFrame
   */
    public GUIDesktop(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.moduleWindows = new ArrayList();
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(150, 150, 150));
        desktopPane.setOpaque(true);
        mainFrame.getContentPane().add(desktopPane, BorderLayout.CENTER);
        statusBar = new JLabel(statusMsg);
        statusBar.setPreferredSize(new Dimension(800, 20));
        statusBar.setFont(new Font("Lucida Console", Font.PLAIN, 11));
        mainFrame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        menuBar = new GUIMenuBar(this);
        mainFrame.setJMenuBar(menuBar);
        toolBar = new GUIToolBar(this, menuBar);
        mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);
    }

    /**
   * Say something on the status bar
   */
    public void changeStatusMsg(String msg) {
        statusMsg = msg;
        statusBar.setText(msg);
    }

    /**
   * Get main frame.
   */
    public JFrame getMainFrame() {
        return mainFrame;
    }

    /**
   * Get desktop pane.
   */
    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    /**
   * Get audio playback thread.
   */
    public AudioPlayback getAudioPlayback() {
        return audioPlayback;
    }

    /**
   * Change playback status.
   */
    public void setPlaybackStatus(int status) {
        toolBar.setStatus(status);
    }

    /**
   * Get currently open sound module window
   */
    public SoundModuleWindow getSoundModuleWindow() {
        return soundModuleWin;
    }

    /**
   * Get menubar (mostly used to access actions)
   */
    public GUIMenuBar getMenuBar() {
        return menuBar;
    }

    /**
   * Get the AudioPort.
   */
    public AudioPort getAudioPort() {
        return audioport;
    }

    /**
   * Set currently open sound module window. Set to null when no sound module
   * is open. This automatically enables and disables menu actions, sets the
   * sound module status on toolbar and also initializes a new AudioPlayback
   * thread.
   *
   * @param win 
   */
    public void setSoundModuleWindow(SoundModuleWindow win) {
        if (soundModuleWin != win) {
            soundModuleWin = win;
            menuBar.setActionsEnabled(win != null);
            if (audioPlayback != null) {
                audioPlayback.setPlayback(false);
                audioPlayback = null;
            }
            if (win == null) {
                toolBar.setStatus(GUIProperties.PLAYBACK_STATUS_NOTLOADED);
            } else {
                toolBar.setStatus(GUIProperties.PLAYBACK_STATUS_OK);
                float fSampleRate = 44100.0F;
                float fAmplitude = 0.9F;
                AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fSampleRate, 16, 2, 4, fSampleRate, false);
                SoundModule soundModule = soundModuleWin.getSoundModule();
                ComponentConnector outlet = soundModule.getOutput(0);
                audioport = new AudioPort(outlet, fSampleRate, fAmplitude, audioFormat);
                SourceDataLine line = AudioPlayback.createSourceDataLine(audioFormat);
                audioOutLine = line;
                audioPlayback = new AudioPlayback(this, soundModule, audioport, line);
                audioPlayback.start();
                audioPlayback.setPlayback(true);
            }
        }
    }

    /**
   * Ok to open a new SoundModuleWindow?
   *
   * @return Is it okay?
   */
    public boolean okToCreateModuleWindow() {
        return true;
    }

    /**
   * Add new SoundModuleWindow to open windows list
   */
    public void addSoundModuleWindow(SoundModuleWindow win) {
        moduleWindows.add((Object) win);
        DebugTools.msg(100, "added " + win + ", idx = " + moduleWindows.indexOf((Object) win));
    }

    /**
   * Remove SoundModuleWindow by reference
   */
    public void removeSoundModuleWindow(SoundModuleWindow ref) {
        int i = moduleWindows.indexOf((Object) ref);
        if (i != -1) {
            moduleWindows.remove(i);
            DebugTools.msg(100, "removed" + ref + ", idx = " + i);
        }
    }

    /**
   * Stop the Audio DataLine
   */
    public void stopAudio() {
        if (audioOutLine != null) {
            audioOutLine.stop();
        }
    }
}
