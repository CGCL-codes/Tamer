package org.nubo.encapsulation;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.media.*;
import com.sun.media.ui.*;
import org.nubo.util.De;

public class JMFPlayer implements org.nubo.encapsulation.PlayerInterface {

    /*************************************************************************
	 * MAIN PROGRAM / STATIC METHODS
	 *************************************************************************/
    static void Fatal(String s) {
        MessageBox mb = new MessageBox("JMF Error", s);
    }

    /*************************************************************************
	 * VARIABLES
	 *************************************************************************/
    private JMFPlayer iam = this;

    private Player player = null;

    private JMFrame jmframe;

    private boolean isPaused;

    private boolean isFullScreen;

    /*************************************************************************
	 * CONSTRUCTORS
	 *************************************************************************/
    public JMFPlayer() {
        Manager.setHint(Manager.PLUGIN_PLAYER, new Boolean(false));
    }

    /**
	 * Open a media file.
	 */
    public void play(File file) {
        String mediaFile = "file:" + file.getAbsolutePath().toString();
        player = null;
        URL url = null;
        try {
            if ((url = new URL(mediaFile)) == null) {
                Fatal("Can't build URL for " + mediaFile);
                return;
            }
            try {
                player = Manager.createPlayer(url);
            } catch (NoPlayerException e) {
                Fatal("Error: " + e);
            }
        } catch (MalformedURLException e) {
            Fatal("Error:" + e);
        } catch (IOException e) {
            Fatal("Error:" + e);
        }
        if (player != null) {
            jmframe = new JMFrame(player);
            jmframe.setVisible(true);
        }
    }

    public void pause(boolean pause) {
        if (player == null) return;
        isPaused = pause;
        if (isPaused == true) {
            player.stop();
            De.bug(3, "video playback pause");
        }
        if (isPaused == false) {
            player.start();
            De.bug(3, "video playback continue");
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void rewindBackward(int i) {
        player.setMediaTime(new Time(player.getMediaTime().getSeconds() - i));
    }

    public void rewindFoward(int i) {
        player.setMediaTime(new Time(player.getMediaTime().getSeconds() + i));
    }

    public void stop() {
        if (player == null) return;
        player.stop();
        player.close();
        jmframe.setVisible(false);
        jmframe.dispose();
    }

    class JMFrame extends javax.swing.JFrame implements ControllerListener {

        Player mplayer;

        Component visual = null;

        Component control = null;

        int videoWidth = 0;

        int videoHeight = 0;

        int controlHeight = 30;

        int insetWidth = 10;

        int insetHeight = 30;

        boolean firstTime = true;

        public JMFrame(Player player) {
            super("Nubo Video - Using jffmpeg");
            getContentPane().setLayout(new BorderLayout());
            setSize(320, 10);
            setLocation(50, 50);
            setVisible(true);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    stop();
                }
            });
            mplayer = player;
            mplayer.addControllerListener((ControllerListener) this);
            mplayer.realize();
        }

        private void cycleFullScreen() {
            if (!isFullScreen) {
                isFullScreen = true;
                JMFrame.this.dispose();
                JMFrame.this.setUndecorated(true);
                JMFrame.this.setResizable(false);
                JMFrame.this.setAlwaysOnTop(true);
                JMFrame.this.setVisible(true);
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] devices = env.getScreenDevices();
                for (int i = 0; i < 1; i++) {
                }
                System.out.println("JOOOORMAAA" + devices[0].isFullScreenSupported());
                devices[0].setFullScreenWindow(JMFrame.this);
            } else if (isFullScreen) {
                isFullScreen = false;
                JMFrame.this.dispose();
                JMFrame.this.setUndecorated(false);
                setSize(videoWidth + insetWidth, videoHeight + controlHeight + insetHeight);
                setLocation(50, 50);
                JMFrame.this.setResizable(true);
                JMFrame.this.setAlwaysOnTop(false);
                JMFrame.this.setVisible(true);
            }
        }

        public void controllerUpdate(ControllerEvent ce) {
            if (ce instanceof RealizeCompleteEvent) {
                De.bug(3, "-----------PPLAYER PREFETCH");
                mplayer.prefetch();
            } else if (ce instanceof PrefetchCompleteEvent) {
                De.bug(3, "-----------PPLAYER PREFETCH DONE");
                if (visual != null) return;
                if ((visual = mplayer.getVisualComponent()) != null) {
                    Dimension size = visual.getPreferredSize();
                    videoWidth = size.width;
                    videoHeight = size.height;
                    getContentPane().add("Center", visual);
                } else videoWidth = 320;
                setSize(videoWidth + insetWidth, videoHeight + controlHeight + insetHeight);
                mplayer.getVisualComponent().addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent evt) {
                        if (evt.getClickCount() >= 2) cycleFullScreen();
                    }
                });
                mplayer.getVisualComponent().addKeyListener(new KeyAdapter() {

                    public void keyPressed(KeyEvent evt) {
                        System.out.println("JEEE: " + evt.getKeyCode());
                        if (evt.getKeyChar() == 'f' || evt.getKeyChar() == 'F') {
                            cycleFullScreen();
                        }
                        if (evt.getKeyCode() == 39) {
                            JMFPlayer.this.rewindFoward(20);
                        }
                        if (evt.getKeyCode() == 37) {
                            JMFPlayer.this.rewindBackward(20);
                        }
                        if (evt.getKeyCode() == 32) {
                            JMFPlayer.this.pause(true);
                        }
                    }
                });
                validate();
                mplayer.start();
            } else if (ce instanceof EndOfMediaEvent) {
                De.bug(3, "-----------PPLAYER END OF MEDIA EVENT");
                stop();
            }
        }
    }
}
