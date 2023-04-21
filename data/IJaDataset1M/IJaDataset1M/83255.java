package org.vizzini.example.musicteacher.ui;

import org.junit.Test;
import org.vizzini.ui.ApplicationSupport;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Provides tests for the <code>CandidateUI</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class TestPianoKeyboardUI {

    private final Logger LOGGER = Logger.getLogger(PianoKeyboardUI.class.getName());

    /**
     * Test the user interface.
     */
    @Test
    public void testUI() {
        ApplicationSupport.setStringBundleName("org.vizzini.ui.resources");
        ApplicationSupport.setConfigBundleName("org.vizzini.ui.config");
        PianoKeyboardUI pianoKeyboardUI = new PianoKeyboardUI();
        JPanel west = new JPanel();
        west.setPreferredSize(new Dimension(50, 50));
        JPanel east = new JPanel();
        east.setPreferredSize(new Dimension(50, 50));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(west, BorderLayout.WEST);
        mainPanel.add(pianoKeyboardUI, BorderLayout.CENTER);
        mainPanel.add(east, BorderLayout.EAST);
        JFrame frame = new JFrame("PianoKeyboardUI Test");
        frame.getContentPane().add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.setSize(1920, 240);
        frame.setVisible(true);
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
