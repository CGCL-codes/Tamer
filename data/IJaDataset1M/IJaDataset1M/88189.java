package org.omegat.gui.threads;

/**
 * An independent stream to save project,
 * created in order not to freese UI while project is saved (may take a lot)
 *
 * @author Keith Godfrey
 */
public class SaveThread extends Thread {

    public SaveThread() {
        setName("Save thread");
        m_timeToDie = false;
        m_saveDuration = 60000;
    }

    public void run() {
        try {
            sleep(m_saveDuration);
        } catch (InterruptedException e2) {
            ;
        }
        while (m_timeToDie == false) {
            CommandThread.core.save();
            try {
                sleep(m_saveDuration);
            } catch (InterruptedException e) {
                ;
            }
        }
    }

    public void signalStop() {
        m_timeToDie = true;
    }

    protected boolean m_timeToDie;

    protected int m_saveDuration;
}
