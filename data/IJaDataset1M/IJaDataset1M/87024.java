package es.eucm.eadventure.editor.gui.otherpanels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import es.eucm.eadventure.editor.gui.audio.Sound;
import es.eucm.eadventure.editor.gui.audio.SoundMidi;
import es.eucm.eadventure.editor.gui.audio.SoundMp3;

/**
 * This panel plays an audio file.
 * 
 * @author Bruno Torijano Bueno
 */
public class AudioPanel extends JPanel {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Play button.
     */
    private JButton playButton;

    /**
     * Stop button.
     */
    private JButton stopButton;

    /**
     * Path to the sound file.
     */
    private String audioPath;

    /**
     * Sound that is being played.
     */
    private Sound sound;

    /**
     * Constructor.
     */
    public AudioPanel() {
        super();
        audioPath = null;
        sound = null;
        Icon playImage = new ImageIcon("img/buttons/play.png");
        Icon stopImage = new ImageIcon("img/buttons/stop.png");
        addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent event) {
            }

            public void ancestorMoved(AncestorEvent event) {
            }

            public void ancestorRemoved(AncestorEvent event) {
                stopSound();
            }
        });
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        stopButton = new JButton(stopImage);
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopSound();
            }
        });
        stopButton.setPreferredSize(new Dimension(50, 50));
        stopButton.setEnabled(false);
        add(stopButton, c);
        c.gridx = 1;
        playButton = new JButton(playImage);
        playButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                playSound();
            }
        });
        playButton.setPreferredSize(new Dimension(50, 50));
        playButton.setEnabled(false);
        add(playButton, c);
    }

    /**
     * Constructor.
     * 
     * @param audioPath
     *            Path to the audio file (relative to the ZIP)
     */
    public AudioPanel(String audioPath) {
        this();
        loadAudio(audioPath);
    }

    /**
     * Sets a new audio to play in the panel.
     * 
     * @param audioPath
     *            Path to the audio file (relative to the ZIP)
     */
    public void loadAudio(String audioPath) {
        this.audioPath = audioPath;
        playButton.setEnabled(true);
        stopButton.setEnabled(true);
        stopSound();
    }

    /**
     * Deletes the audio path stored and stops the audio playing.
     */
    public void removeAudio() {
        audioPath = null;
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
        stopSound();
    }

    /**
     * Stops the sound being played (if there was one) and start playing a new
     * sound.
     */
    public synchronized void playSound() {
        stopSound();
        if (audioPath != null) {
            String lowerCasePath = audioPath.toLowerCase();
            if (lowerCasePath.endsWith("mp3")) sound = new SoundMp3(audioPath); else if (lowerCasePath.endsWith("mid") || lowerCasePath.endsWith("midi")) sound = new SoundMidi(audioPath);
            sound.startPlaying();
        }
    }

    /**
     * Stops the sound that is currently being played. If no sound was being
     * played, it does nothing.
     */
    public synchronized void stopSound() {
        if (sound != null) {
            sound.stopPlaying();
            sound = null;
        }
    }
}
