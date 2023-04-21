package com.explosion.utilities.dialog;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import com.explosion.utilities.process.threads.ProcessThread;

/**
 * @author Stephen Cowx Date created:@12-Feb-2003
 */
public class ProgressDialog extends BusyDialog {

    private JProgressBar progressBar;

    public ProgressDialog(JFrame owner, String title, ProcessThread workerThread, Image[] images) {
        super(owner, title, workerThread, images);
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        this.getContentPane().add(progressBar, BorderLayout.CENTER);
        pack();
    }

    /**
     * @see com.explosion.utilities.process.threads.UnitisedProcess#processUnit()
     */
    public void processUnit() throws Exception {
        if (screenImage == null) {
            screenImage = canvas.createVolatileImage(width, height);
            screenGraphics = screenImage.getGraphics();
        }
        screenGraphics.setColor(controlPanel.getBackground());
        screenGraphics.fillRect(0, 0, width, height);
        image.draw(screenGraphics);
        image.cycle();
        canvas.getGraphics().drawImage(screenImage, 0, 0, null);
        canvas.getGraphics().drawImage(screenImage, 0, 0, null);
        Runnable updateProgress = new Runnable() {

            public void run() {
                progressBar.setValue(workerThread.getProcess().getPercentComplete());
            }
        };
        SwingUtilities.invokeLater(updateProgress);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }
}
