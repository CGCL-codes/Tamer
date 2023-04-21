package de.buelowssiege.jaymail.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import de.buelowssiege.jaymail.gui.frames.ProgressFrame;

/**
 * This class implements a basic thread that closely works together with a
 * ProgressFrame.
 * 
 * @author Maximilian Schwerin
 * @created 13. Mai 2002
 */
public abstract class ProgressThread extends Thread implements ActionListener {

    private ProgressFrame progressFrame;

    private boolean runOn = true;

    /**
     * Constructor for the ProgressThread object
     * 
     * @param progressFrame
     *            Description of the Parameter
     */
    public ProgressThread(ProgressFrame progressFrame) {
        this.progressFrame = progressFrame;
        if (progressFrame != null) {
            progressFrame.getCancelButton().addActionListener(this);
            if (!progressFrame.isShowing()) {
                progressFrame.setVisible(true);
            }
        }
    }

    /**
     * This should be called to stop the thread.
     */
    public void stopThread() {
        runOn = false;
    }

    /**
     * Returns true if the thread should continue running;
     * 
     * @return Description of the Return Value
     */
    public boolean shouldRunOn() {
        return (runOn);
    }

    /**
     * Main processing method for the ProgressThread object
     */
    public abstract void run();

    /**
     * Sets the enabledCancelButton attribute of the ProgressThread object
     * 
     * @param enabled
     *            The new enabledCancelButton value
     */
    protected void setEnabledCancelButton(boolean enabled) {
        if (progressFrame != null) {
            progressFrame.getCancelButton().setEnabled(enabled);
        }
    }

    /**
     * Gets the progressBarValue attribute of the ProgressThread object
     */
    protected int getProgressBarValue() {
        if (progressFrame != null) {
            return (progressFrame.getProgressBar().getValue());
        }
        return (0);
    }

    /**
     * Sets the value of the progressbar
     * 
     * @param value
     *            The new progressBar value
     * @param str
     *            The new progressBar value
     */
    protected void setProgressBar(int value, String str) {
        if (progressFrame != null) {
            progressFrame.getProgressBar().setValue(value);
            progressFrame.getProgressBar().setString(str);
        }
    }

    /**
     * Sets the minimum and maximum of the progressbar.
     * 
     * @param min
     *            The new progressBarMinMax value
     * @param max
     *            The new progressBarMinMax value
     */
    protected void setProgressBarMinMax(int min, int max) {
        if (progressFrame != null) {
            int value = progressFrame.getProgressBar().getValue();
            progressFrame.getProgressBar().setMinimum(min);
            progressFrame.getProgressBar().setValue(value);
            progressFrame.getProgressBar().setMaximum(max);
        }
    }

    /**
     * Sets the progressBarString attribute of the ProgressThread object
     * 
     * @param str
     *            The new progressBarString value
     */
    protected void setProgressBarString(String str) {
        if (progressFrame != null) {
            progressFrame.getProgressBar().setString(str);
        } else {
            System.out.println(str);
        }
    }

    /**
     * Sets the title of the progressFrame.
     * 
     * @param title
     *            The new title value
     */
    protected void setTitle(String title) {
        if (progressFrame != null) {
            progressFrame.setTitle(title);
        }
    }

    /**
     * Disposes the progressFrame.
     */
    protected void disposeFrame() {
        if (progressFrame != null) {
            progressFrame.dispose();
        }
    }

    /**
     * Returns the progressFrame.
     * 
     * @return The frame value
     */
    protected ProgressFrame getFrame() {
        return (progressFrame);
    }

    /**
     * Displays error messages.
     */
    protected void displayError(Exception ex) {
        if (progressFrame != null) {
            progressFrame.getProgressBar().setString("ERROR : " + ex.getMessage());
        }
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public void actionPerformed(ActionEvent ae) {
        if (progressFrame != null && ae.getSource() == progressFrame.getCancelButton()) {
            stopThread();
            disposeFrame();
        }
    }
}
