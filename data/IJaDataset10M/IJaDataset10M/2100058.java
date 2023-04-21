package org.sink.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.Player;

/**
 * A window which holds the video image.
 */
public class VideoWindow extends JFrame {

    private Player videoPlayer, audioPlayer;

    private JFrame frame = this;

    private boolean receiving = false;

    /**
     * Creates a new video window with the given player.
     * Sets receiving to false.
     * 
     * This constructor should be used for a local video image.
     * 
     * @param player
     */
    public VideoWindow(Player player) {
        this(player, false);
    }

    /**
     * Creates a new video window with the given player.
     * Sets receiving to the given boolean paramenter.
     * 
     * This constructor should be used for a video stream image where receving needs to be set to true.
     * 
     * @param player
     * @param receiving
     */
    public VideoWindow(Player player, boolean receiving) {
        this.videoPlayer = player;
        this.receiving = receiving;
        if (receiving == true) setTitle("Live Video Stream"); else setTitle("My Webcam");
        initialize();
        setVisible(true);
    }

    private void initialize() {
        this.addWindowListener(new VideoWindowListener());
        setLayout(new BorderLayout());
        add("Center", new PlayerPanel(videoPlayer));
    }

    private void addAudioControlls(Player player) {
        this.audioPlayer = player;
        if (videoPlayer != null) add("South", new ControllerPanel(audioPlayer));
    }

    private void close() {
        if (videoPlayer != null) {
            setVisible(false);
            dispose();
            videoPlayer.stop();
            videoPlayer.deallocate();
            videoPlayer.close();
            if (receiving == true) MediaPlayController.stop(); else BuddyListController.getInstance().setLocalVideoInProgress(false);
        }
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.deallocate();
            audioPlayer.close();
            if (receiving == true) MediaPlayController.stop();
        }
    }

    public void addNotify() {
        super.addNotify();
        pack();
    }

    class VideoWindowListener extends WindowAdapter {

        /**
		 * Disposes the frame if the window encounters the windowClosing() event.
		 */
        public void windowClosing(WindowEvent e) {
            close();
        }
    }
}

/**
 * A panel to hold the player's visual component.
 */
class PlayerPanel extends JPanel {

    private Component vc, cc;

    /**
	 * Adds the player to the panel.
	 * 
	 * @param p the player
	 */
    public PlayerPanel(Player p) {
        setLayout(new BorderLayout());
        if ((vc = p.getVisualComponent()) != null) add("Center", vc);
    }

    public Dimension getPreferredSize() {
        int w = 0, h = 0;
        if (vc != null) {
            Dimension size = vc.getPreferredSize();
            w = size.width;
            h = size.height;
        }
        if (cc != null) {
            Dimension size = cc.getPreferredSize();
            if (w == 0) w = size.width;
            h += size.height;
        }
        if (w < 160) w = 160;
        return new Dimension(w, h);
    }
}

/**
 * A panel to hold the Player's controls.
 */
class ControllerPanel extends JPanel {

    Component cc;

    /**
	 * Adds the controls of a given player to the panel.
	 * 
	 * @param p the player
	 */
    public ControllerPanel(Player p) {
        setLayout(new BorderLayout());
        if ((cc = p.getControlPanelComponent()) != null) add("South", cc);
    }

    public Dimension getPreferredSize() {
        int w = 0, h = 0;
        if (cc != null) {
            Dimension size = cc.getPreferredSize();
            if (w == 0) w = size.width;
            h += size.height;
        }
        if (w < 160) w = 160;
        return new Dimension(w, h);
    }
}
