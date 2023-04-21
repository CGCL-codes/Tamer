package vavi.swing.plaf.vavi;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

/**
 * Original look&feel (filechooser).
 *
 * @author        <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class VaviFileChooserUI extends MetalFileChooserUI {

    /** */
    public VaviFileChooserUI(JFileChooser filechooser) {
        super(filechooser);
    }

    /** */
    public static ComponentUI createUI(JComponent c) {
        return new VaviFileChooserUI((JFileChooser) c);
    }

    /** */
    public void setFileName(String filename) {
        String oldName;
        File file;
        oldName = getFileName();
        file = getFileChooser().getSelectedFile();
        if (file == null) {
            super.setFileName(oldName);
            return;
        }
        if (file.isDirectory()) {
            if (getFileChooser().isDirectorySelectionEnabled()) {
                super.setFileName(filename);
            } else {
                super.setFileName(oldName);
            }
        } else {
            if (getFileChooser().isFileSelectionEnabled()) {
                super.setFileName(filename);
            } else {
                super.setFileName(oldName);
            }
        }
    }

    /** */
    public static void main(String[] args) {
        JFileChooser fc = new JFileChooser();
        System.err.println(fc.getUI().getClass());
        fc.setSelectedFile(new File("T400.java"));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.showOpenDialog(null);
        System.err.println(fc.getSelectedFile());
        System.exit(0);
    }
}
