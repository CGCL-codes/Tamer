package zipperSwing;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class ZipWorker extends SwingWorker<Void, Void> {

    static final int BUFFER = 2048;

    private JTextField ZipFolderfield;

    private JTextField zipNameField;

    private JProgressBar totalBar;

    private JButton zipButton;

    public void setZipButton(final JButton l) {
        this.zipButton = l;
    }

    public void setZipNameField(final JTextField l) {
        this.zipNameField = l;
    }

    public void setZipFolderfield(final JTextField l) {
        this.ZipFolderfield = l;
    }

    public void setTotalBar(final JProgressBar l) {
        this.totalBar = l;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Zip zip = new Zip();
        File a = new File(this.ZipFolderfield.getText());
        if (!a.exists()) System.err.println("No such file or directory");
        try {
            FileOutputStream dest = new FileOutputStream(this.zipNameField.getText());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            File f = new File(this.ZipFolderfield.getText());
            zip.zipFile(f, out, data);
            if (f.isDirectory()) {
                zip.zipDirectory(f, out, data, this.totalBar);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void done() {
        this.zipButton.setEnabled(true);
        this.zipButton.setText("zip");
    }
}
