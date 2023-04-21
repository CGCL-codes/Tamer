package org.gstreamer.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import org.gstreamer.Gst;
import org.gstreamer.swing.VideoPlayer;

/**
 *
 */
public class SwingMultiPlayer {

    /** Creates a new instance of SwingPlayer */
    public SwingMultiPlayer() {
    }

    public static void main(String[] args) {
        System.setProperty("apple.awt.graphics.UseQuartz", "false");
        args = Gst.init("Swing Player", args);
        if (args.length < 1) {
            System.err.println("Usage: SwingPlayer <filename>");
            System.exit(1);
        }
        final File[] files = new File[args.length];
        for (int i = 0; i < args.length; ++i) {
            files[i] = new File(args[i]);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame window = new JFrame("Swing Video Player");
                JDesktopPane panel = new JDesktopPane();
                window.add(panel);
                for (int i = files.length - 1; i >= 0; --i) {
                    File file = files[i];
                    JInternalFrame frame = new JInternalFrame(file.getName());
                    frame.setResizable(true);
                    frame.setClosable(true);
                    frame.setIconifiable(true);
                    frame.setMaximizable(true);
                    frame.setLocation(i * 100, i * 100);
                    final VideoPlayer player = new VideoPlayer(file);
                    player.setPreferredSize(new Dimension(640, 480));
                    player.setControlsVisible(true);
                    frame.add(player, BorderLayout.CENTER);
                    frame.pack();
                    panel.add(frame);
                    frame.setVisible(true);
                    javax.swing.Timer timer = new javax.swing.Timer(5000 * i, new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            player.getMediaPlayer().play();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                window.setPreferredSize(new Dimension(1024, 768));
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.pack();
                window.setVisible(true);
            }
        });
    }
}
