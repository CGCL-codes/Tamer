package archivingTool;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class CompressionWorker extends SwingWorker<Object, Integer> {

    private final JProgressBar progressBar;

    private final String compressionPath;

    public CompressionWorker(String path, JProgressBar bar) {
        this.compressionPath = path;
        this.progressBar = bar;
    }

    @Override
    protected Object doInBackground() throws Exception {
        BufferedOutputStream bufout = null;
        try {
            bufout = new BufferedOutputStream(new FileOutputStream(compressionPath));
        } finally {
            if (bufout != null) {
                bufout.close();
            }
        }
        return new Object();
    }

    @Override
    public void process(List<Integer> chunks) {
        if (progressBar != null) {
            for (Integer intr : chunks) {
                progressBar.setValue(intr.intValue());
            }
        }
    }
}
