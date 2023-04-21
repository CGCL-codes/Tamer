package editor.workers;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.SwingWorker;
import editor.ProgressPane;

public final class RtfWriteWorker extends SwingWorker<Object, String> {

    private Object filePath;

    private JEditorPane textArea;

    private ProgressPane progress;

    public RtfWriteWorker(String filePath, JEditorPane area, ProgressPane pane) {
        this.filePath = filePath;
        this.textArea = area;
        this.progress = pane;
    }

    @Override
    protected Object doInBackground() throws Exception {
        if (progress != null) {
            progress.setProgress(0);
        }
        BufferedOutputStream writer = null;
        try {
            if (filePath instanceof String) {
                writer = new BufferedOutputStream(new FileOutputStream((String) filePath));
            } else if (filePath instanceof URL) {
                URL u = (URL) filePath;
                writer = new BufferedOutputStream(u.openConnection().getOutputStream());
            }
            char[] lines = textArea.getText().toCharArray();
            for (int lineCount = 0; lineCount < lines.length; lineCount++) {
                writer.write(lines[lineCount]);
                if (progress != null) {
                    progress.setProgress(lineCount);
                }
            }
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        if (progress != null) {
            progress.setProgress(100);
        }
        return null;
    }

    public void process(List<String> chunks) {
    }
}
