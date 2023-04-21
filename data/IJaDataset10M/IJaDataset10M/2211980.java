package Interface.Lecteur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicSliderUI;
import javazoom.jl.decoder.JavaLayerException;
import mp3player.Engine;
import mp3player.EngineListener;
import mp3player.MP3;
import mp3player.MP3Stream;

/**
 * -- Commands to control Gui --
 * showPlayer()  -  show the normal player window (this is for minimode, which is not supported yet)
 * hidePlayer()  -  hides the normal player window
 * showPl()      -  show playlist
 * hidePl()      -  hide playlist
 *
 *
 * Call these methods from the actual player:
 *
 * addFileToPlaylist(String artistAndSongTitle)  - adding file to playlist, returns fileNumber on playlist
 * setStringToShow(String artistAndSong)   - changes the song title to show on player
 * setSongPosition(int percents) - moves the progressbar to the current position (percents from 0 to 100)
 *
 * Methods to fill up: there is always a comment 'Here' at the place that should be implemented
 *
 * public void stateChanged(ChangeEvent e) 
 * public void valueChanged(ListSelectionEvent e)
 * public void actionPerformed(ActionEvent ev)    7 different places
 *
 *
 *
 */
public class GuiOld extends JFrame implements ActionListener, ChangeListener, EngineListener {

    private static final long serialVersionUID = 1L;

    private static class OurSliderUI extends BasicSliderUI {

        public OurSliderUI(JSlider arg0) {
            super(arg0);
        }

        public boolean isDragging() {
            return super.isDragging();
        }
    }

    public JPanel mainPanel;

    public JPanel buttonPanel;

    public JPanel centerPanel;

    private int mainX = 300;

    private int mainY = 100;

    private long time = 0;

    private JProgressBar progressBar;

    private JSlider position;

    private OurSliderUI positionUI;

    private JSlider volume;

    private static final String initialNowPlaying = "MP3 - Internet Files - Wake-up Timer - etc.";

    private String nowPlaying = initialNowPlaying;

    private boolean playerVisible;

    private TextAnimator textAnimator;

    private MP3 playingMP3;

    private Engine playerEngine = null;

    private JButton playButton;

    private JButton pauseButton;

    private JButton stopButton;

    public GuiOld() {
        super("MPi3 Player");
        mainX = 300;
        mainY = 100;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        mainPanel = new ImagePanel("/images/bg.gif");
        mainPanel.setLayout(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        buttonPanel.setOpaque(false);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
        centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 7));
        centerPanel.setOpaque(false);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setBounds(getMainWindowRect());
        addTextBox();
        addButtons();
        getContentPane().add(mainPanel);
    }

    /**
	 * stateChanged is called every time when user
	 * adjusts the volume
	 */
    public void stateChanged(ChangeEvent e) {
        if (playerEngine == null) return;
        playerEngine.setVolume(volume.getValue());
    }

    /**
	 * Sets the song position meter to
	 * right position
	 */
    public void setSongPosition(int percents) {
        if (positionUI.isDragging()) return;
        position.setValue(percents);
    }

    public void setStringToShow(String artistAndSong) {
        nowPlaying = artistAndSong;
        if (textAnimator != null) textAnimator.alive = false;
        textAnimator = new TextAnimator();
        textAnimator.start();
    }

    public void showPlayer() {
        setVisible(true);
        if (textAnimator != null) textAnimator.alive = false;
        textAnimator = new TextAnimator();
        textAnimator.start();
        playerVisible = true;
    }

    public void hidePlayer() {
        setVisible(false);
        playerVisible = false;
    }

    public class TextAnimator extends Thread {

        int length;

        int roundLength;

        boolean alive = true;

        public synchronized void run() {
            String text = nowPlaying;
            length = text.length();
            roundLength = length * 2;
            int b = 1;
            int a;
            int c;
            while (alive) {
                try {
                    a = b - length;
                    if (a < 0) a = 0;
                    c = b;
                    if (b > length) c = length;
                    progressBar.setString(text.substring(a, c));
                    b++;
                    if (b == roundLength) b = 1;
                    this.wait(150);
                    if (b == length + 1) this.wait(1400);
                    if (b == 1) {
                        progressBar.setString("");
                        this.wait(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addTextBox() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(210, 22));
        progressBar.setForeground(new Color(255, 0, 0));
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Dialog", Font.BOLD, 10));
        progressBar.setString(nowPlaying);
        progressBar.setBackground(new Color(214, 214, 204));
        centerPanel.add(progressBar, BorderLayout.CENTER);
        position = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        positionUI = new OurSliderUI(position);
        position.setUI(positionUI);
        position.setForeground(new Color(255, 0, 0));
        position.setPreferredSize(new Dimension(200, 10));
        position.setMinorTickSpacing(1);
        position.setOpaque(false);
        position.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (playerEngine == null) return;
                try {
                    playerEngine.seek(position.getValue());
                } catch (JavaLayerException e1) {
                    exceptionDialog(e1);
                } catch (IOException e1) {
                    exceptionDialog(e1);
                }
            }
        });
        centerPanel.add(position);
    }

    public void addButtons() {
        playButton = new JButton(new ImageIcon(getClass().getResource("/images/play.gif")));
        playButton.setRolloverIcon(new ImageIcon(getClass().getResource("/images/playover.gif")));
        playButton.setPreferredSize(new Dimension(45, 38));
        playButton.setBorderPainted(false);
        playButton.setBorder(null);
        playButton.setActionCommand("PLAY");
        playButton.addActionListener(this);
        buttonPanel.add(playButton);
        pauseButton = new JButton(new ImageIcon(getClass().getResource("/images/pause.gif")));
        pauseButton.setRolloverIcon(new ImageIcon(getClass().getResource("/images/pauseover.gif")));
        pauseButton.setPreferredSize(new Dimension(45, 38));
        pauseButton.setBorderPainted(false);
        pauseButton.setBorder(null);
        pauseButton.setActionCommand("PAUSE");
        pauseButton.addActionListener(this);
        buttonPanel.add(pauseButton);
        stopButton = new JButton(new ImageIcon(getClass().getResource("/images/stop.gif")));
        stopButton.setRolloverIcon(new ImageIcon(getClass().getResource("/images/stopover.gif")));
        stopButton.setPreferredSize(new Dimension(45, 38));
        stopButton.setBorderPainted(false);
        stopButton.setBorder(null);
        stopButton.setActionCommand("STOP");
        stopButton.addActionListener(this);
        buttonPanel.add(stopButton);
        volume = new JSlider(JSlider.HORIZONTAL, 0, 100, 70);
        volume.addChangeListener(this);
        volume.setUI(new BasicSliderUI(volume));
        volume.setForeground(new Color(255, 0, 0));
        volume.setPreferredSize(new Dimension(75, 10));
        volume.setMinorTickSpacing(1);
        volume.setOpaque(false);
        buttonPanel.add(volume);
    }

    public static void main(String[] args) {
        try {
            GuiOld gui = new GuiOld();
            System.out.println("test");
            gui.showPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectSong(String path) {
        MP3 song;
        try {
            song = new MP3Stream(path);
            if (song == null) return;
            playingMP3 = song;
            if (song.isStream()) setStringToShow("Connecting..."); else setStringToShow("Loading...");
            if (playerEngine != null) playerEngine.stop();
            playerEngine = new Engine(song, this);
            playerEngine.setVolume(volume.getValue());
            playerEngine.play();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JavaLayerException e1) {
            exceptionDialog(e1);
        } catch (IOException e1) {
            exceptionDialog(e1);
        }
    }

    public void actionPerformed(ActionEvent ev) {
        JButton button = (JButton) ev.getSource();
        String command = button.getActionCommand();
        if (command.equals("CLOSE")) {
            System.exit(0);
        } else if (command.equals("PLAY")) {
            if (playerEngine != null) {
                try {
                    playerEngine.play();
                } catch (JavaLayerException e) {
                    exceptionDialog(e);
                } catch (IOException e) {
                    exceptionDialog(e);
                }
            }
        } else if (command.equals("PAUSE")) {
            if (playerEngine != null) {
                playerEngine.pause();
            }
        } else if (command.equals("STOP")) {
            if (playerEngine != null) {
                playerEngine.stop();
            }
        }
    }

    public Rectangle getMainWindowRect() {
        String x = this.mainX + "";
        String y = this.mainY + "";
        String width = "398";
        String height = "100";
        return new Rectangle(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(width), Integer.parseInt(height));
    }

    public Rectangle getPlWindowRect() {
        String x = this.mainX + 398 + "";
        String y = this.mainY + "";
        String width = "398";
        String height = "396";
        return new Rectangle(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(width), Integer.parseInt(height));
    }

    class MP3FileFilter extends FileFilter implements FilenameFilter {

        public boolean accept(File dir, String name) {
            String s = name.toLowerCase();
            if (s.endsWith(".mp3")) return true;
            File file = new File(dir, name);
            if (file.exists() && file.isDirectory()) return true;
            return false;
        }

        public boolean accept(File file) {
            if (file.isDirectory()) return true;
            String s = file.getName().toLowerCase();
            if (s.endsWith(".mp3")) return true;
            return false;
        }

        public String getDescription() {
            return "MP3 Files";
        }
    }

    public void updateSongPosition(float pos) {
        setSongPosition((int) pos);
    }

    public void updateSongStarted() {
        if (playingMP3 == null) return;
        setStringToShow(playingMP3.getDisplayString());
    }

    public void updateSongEnded() {
    }

    private void exceptionDialog(Exception e) {
        StringBuilder st = new StringBuilder();
        for (StackTraceElement ste : e.getStackTrace()) {
            st.append(ste.toString());
            st.append('\n');
        }
        errorDialog(e.getClass().getName() + ": " + e.getMessage(), st.toString());
    }

    private void errorDialog(String problem, String details) {
        JOptionPane.showMessageDialog(this, new Object[] { problem, new JScrollPane(new JTextArea(details, 6, 40)) }, "mp3player error", JOptionPane.ERROR_MESSAGE);
    }
}
