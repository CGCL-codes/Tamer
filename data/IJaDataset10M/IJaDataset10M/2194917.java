package net.sf.ulmac.ui.widgets;

import java.util.List;
import net.sf.ulmac.core.types.FileFormat;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class FileFormatCombo {

    private Combo fCombo;

    private List<FileFormat> fOutputFormats;

    public FileFormatCombo(Composite parent, int style, List<FileFormat> outputFormats) {
        fCombo = new Combo(parent, style);
        for (FileFormat fileFormat : outputFormats) {
            fCombo.add(fileFormat.getExtension());
        }
        fOutputFormats = outputFormats;
    }

    public Combo getCombo() {
        return fCombo;
    }

    public FileFormat getFileFormat() {
        for (FileFormat fileFormat : fOutputFormats) {
            if (fileFormat.getExtension().equals(fCombo.getText())) {
                return (fileFormat);
            }
        }
        return null;
    }

    public boolean isEnabled() {
        return fCombo.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        fCombo.setEnabled(enabled);
    }

    public void setFileFormat(FileFormat fileFormat) {
        int index = 0;
        for (String display : fCombo.getItems()) {
            if (fileFormat.getExtension().equals(display)) {
                fCombo.select(index);
            }
            ++index;
        }
    }
}
