package net.sf.jhylafax;

import java.io.File;
import org.xnap.commons.gui.FileChooserPanel;
import org.xnap.commons.util.QuotedStringTokenizer;

public class ExecutableChooserPanel extends FileChooserPanel {

    public ExecutableChooserPanel(File file, int columns) {
        super(file, columns);
    }

    public ExecutableChooserPanel(int columns) {
        super(columns);
    }

    @Override
    public File getFile() {
        QuotedStringTokenizer t = new QuotedStringTokenizer(getTextField().getText());
        return (t.hasMoreTokens()) ? new File(t.nextToken()) : new File("");
    }

    @Override
    public void setFile(File file) {
        if (file == null) {
            getTextField().setText("");
        } else if (file.getAbsolutePath().contains(" ")) {
            getTextField().setText("\"" + file.getAbsolutePath() + "\"");
        } else {
            getTextField().setText(file.getAbsolutePath());
        }
    }
}
