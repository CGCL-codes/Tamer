package org.encog.workbench.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.encog.StatusReportable;
import org.encog.util.benchmark.EncogBenchmark;
import org.encog.workbench.EncogWorkBench;

public class BenchmarkDialog extends JDialog implements Runnable, StatusReportable {

    private JProgressBar progress;

    private JLabel status;

    public BenchmarkDialog() {
        progress = new JProgressBar(0, 4);
        setTitle("Please wait, benchmarking Encog.");
        setSize(640, 75);
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        this.status = new JLabel("");
        content.add(this.status, BorderLayout.CENTER);
        content.add(progress, BorderLayout.SOUTH);
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        EncogBenchmark benchmark = new EncogBenchmark(this);
        String s = benchmark.process();
        dispose();
        EncogWorkBench.displayMessage("Benchmark Complete", s + "\n(higher is better)");
    }

    public void report(int total, int current, String status) {
        this.status.setText(status);
        this.progress.setValue(current);
    }

    public void reportPhase(int arg0, int arg1, String arg2) {
    }
}
