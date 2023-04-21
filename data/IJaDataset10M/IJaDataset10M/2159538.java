package org.espenra.threads;

import javax.swing.JLabel;
import org.espenra.FileSystemReaderResults;

public class UpdateListLabelWithDirectoryNameThread extends Thread {

    private JLabel m_labelToUpdate;

    private boolean m_run = false;

    public void Stop() {
        m_run = false;
    }

    public void SetLabel(JLabel label) {
        m_labelToUpdate = label;
    }

    private void SetDirectory(String string) {
        m_labelToUpdate.setText(string);
    }

    public void run() {
        m_run = true;
        while (m_run) {
            SetDirectory(FileSystemReaderResults.GetCurrentDirectory());
            try {
                Thread.sleep(50);
            } catch (Exception exp) {
            }
        }
    }
}
