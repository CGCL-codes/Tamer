package editor.workers;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import editor.ProgressPane;

public final class TextReadWorker extends SwingWorker<Object, String> {

    private Object filePath;

    private JEditorPane editorPane;

    private ProgressPane progress;

    private StyledEditorKit styledKit = new StyledEditorKit();

    public TextReadWorker(String filePath, JEditorPane editorPane, ProgressPane pane) {
        this.filePath = filePath;
        this.editorPane = editorPane;
        this.progress = pane;
    }

    public TextReadWorker(URL urlPath, JEditorPane editorPane, ProgressPane pane) {
        this.filePath = urlPath;
        this.editorPane = editorPane;
        this.progress = pane;
    }

    @Override
    protected Object doInBackground() throws Exception {
        editorPane.setEditorKit(styledKit);
        editorPane.setBackground(Color.white);
        if (progress != null) {
            progress.setProgress(0);
        }
        BufferedInputStream reader = null;
        try {
            if (filePath instanceof String) {
                File f = new File((String) filePath);
                reader = new BufferedInputStream(new FileInputStream(f));
            } else if (filePath instanceof URL) {
                URL u = (URL) filePath;
                reader = new BufferedInputStream(u.openStream());
            } else {
                throw new IllegalArgumentException("Could not open");
            }
            styledKit.read(reader, editorPane.getDocument(), 0);
        } catch (FileNotFoundException ex) {
            if (progress != null) {
                progress.setText("File does not exist");
            }
        } catch (IOException ex) {
            if (progress != null) {
                progress.setText("I/O Error");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        if (progress != null) {
            progress.setProgress(100);
        }
        editorPane.setEditable(true);
        return null;
    }
}
