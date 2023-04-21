package gnu.launcher.awt;

import gnu.launcher.*;
import gnu.infoset.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AwtDownloadIndicator extends Dialog implements DownloadIndicator, ActionListener {

    public AwtDownloadIndicator(Instance instance) {
        super(instance.frame);
        download = instance.messages.first("download");
        setTitle(string("title"));
        setLayout(new GridLayout(2, 1));
        Label label = new Label(string("message"), Label.CENTER);
        add(label);
        progress = new ProgressBar(300, 10);
        add(progress);
    }

    public void startDownload(long totalBytes) {
        this.totalBytes = totalBytes;
        doLayout();
        pack();
        AwtUtil.center(this, null);
        setVisible(true);
        updateDownload(0);
    }

    public void stopDownload() {
        setVisible(false);
    }

    public void updateDownload(long bytes) {
        progress.setProgress((int) bytes, (int) totalBytes);
    }

    public void actionPerformed(ActionEvent event) {
        dispose();
    }

    private Element download;

    private ProgressBar progress;

    private long totalBytes;

    private String string(String name) {
        return download.first("string", "name", name).cdata;
    }
}
