package windowUnits;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarThread extends Thread {

    private JPanel statusBarPanel;

    private JProgressBar progress;

    private JLabel progressLabel = new JLabel();

    private JPanel in;

    public ProgressBarThread(JPanel statusBarPanel, String progLabel) {
        this.statusBarPanel = statusBarPanel;
        progressLabel.setText(progLabel);
    }

    public void stopProgress() {
        progress.setIndeterminate(false);
        statusBarPanel.remove(in);
        statusBarPanel.repaint();
    }

    public void run() {
        progress = new JProgressBar(0, 100);
        progress.setSize(20, 10);
        progress.setPreferredSize(new Dimension(40, 12));
        progress.setIndeterminate(true);
        in = new JPanel();
        in.add(progressLabel);
        in.add(progress);
        statusBarPanel.add(in, BorderLayout.EAST);
        statusBarPanel.repaint();
    }
}
