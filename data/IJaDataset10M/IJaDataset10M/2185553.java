package org.tranche.gui.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.tranche.gui.GenericLabel;
import org.tranche.gui.GenericButton;
import org.tranche.gui.Styles;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class StatusPanel extends JPanel {

    private Monitor monitor;

    private GenericLabel activity = new GenericLabel("  NOT LISTENING");

    private GenericButton pausePlayButton = new GenericButton("Stop Listening");

    private ImageIcon pauseIcon, playIcon;

    public StatusPanel(Monitor monitor) {
        this.monitor = monitor;
        setLayout(new BorderLayout());
        activity.setFont(Styles.FONT_10PT);
        add(activity, BorderLayout.CENTER);
        try {
            pauseIcon = new ImageIcon(ImageIO.read(StatusPanel.class.getResourceAsStream("/org/tranche/gui/image/pause.gif")));
            playIcon = new ImageIcon(ImageIO.read(StatusPanel.class.getResourceAsStream("/org/tranche/gui/image/play.gif")));
        } catch (Exception e) {
        }
        pausePlayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (StatusPanel.this.monitor.isListening()) {
                    StatusPanel.this.monitor.stop();
                    pausePlayButton.setText("Start Listening");
                    pausePlayButton.setIcon(playIcon);
                } else {
                    StatusPanel.this.monitor.start();
                    pausePlayButton.setText("Stop Listening");
                    pausePlayButton.setIcon(pauseIcon);
                }
            }
        });
        pausePlayButton.setFont(Styles.FONT_10PT);
        pausePlayButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        pausePlayButton.setIcon(pauseIcon);
        pausePlayButton.setBackground(Color.WHITE);
        add(pausePlayButton, BorderLayout.EAST);
    }

    public void setStatus(String status) {
        this.activity.setText("  " + status);
    }
}
