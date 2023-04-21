package be.vds.jtbtaskplanner.client.swing.component;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import be.vds.jtbtaskplanner.client.util.RuntimeMemoryMonitorSource;

public class HeapMonitorShortPanel extends JPanel {

    private static final long serialVersionUID = -6490114612597414648L;

    private int interval;

    private JProgressBar progress;

    public HeapMonitorShortPanel() {
        this(2000);
    }

    public HeapMonitorShortPanel(int interval) {
        this.interval = interval;
        init();
        startHeapRecorder();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        progress = new JProgressBar();
        progress.setStringPainted(true);
        this.add(progress);
    }

    private void redrawHeapValues() {
        int currentHeap = (int) RuntimeMemoryMonitorSource.getTotal();
        int used = (int) RuntimeMemoryMonitorSource.getUsed();
        progress.setMaximum((int) currentHeap);
        progress.setValue((int) used);
        progress.setString(formatSize(used) + " / " + formatSize(currentHeap));
    }

    private String formatSize(float size) {
        String[] suffix = { "B", "K", "M", "G" };
        int si = 0;
        float result = size;
        while (result > 1024) {
            result /= 1024;
            si++;
        }
        return (int) result + suffix[si];
    }

    private void startHeapRecorder() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            if (HeapMonitorShortPanel.this.isVisible()) redrawHeapValues();
                        }
                    });
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
