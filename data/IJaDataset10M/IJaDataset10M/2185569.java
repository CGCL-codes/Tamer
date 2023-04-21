package midikernel.gui;

import java.io.File;
import javax.swing.JFileChooser;
import util.Constants;

/**
 * Opens up a window where a midi-file can be selected.
 */
public class MidiFileSelector implements Constants {

    /**
     * Sole purpose is: JFileChooser.setFileFilter(FileFilter).
     */
    class MidiFileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".midi") || f.getName().toLowerCase().endsWith(".mid");
        }

        public String getDescription() {
            return ".midi files";
        }
    }

    private String filename;

    private File file;

    public MidiFileSelector() {
        JFileChooser fc = new JFileChooser(DEFAULT_MUSIC_DIRECTORY);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(new MidiFileFilter());
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            filename = file.getAbsolutePath();
        } else {
            System.exit(0);
        }
    }

    public String getFilename() {
        return filename;
    }

    public File getFile() {
        return file;
    }
}
