package org.sosy_lab.util.interfaces;

/**
 * The implementing class should be able to provide information
 * about the progress of a (slow) task.
 */
public interface Progressing {

    /**
   * Register a receiver for progress updates.
   * @param receiver
   */
    public void addProgressReceiver(ProgressReceiver receiver);

    /**
   * Set the progress.
   * @param pos
   * @param max
   * @param message
   */
    public void setProgress(int pos, int max, String message);
}
