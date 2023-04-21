package assfxmaker.lib.filter;

import java.io.File;

/**
 *
 * @author The Wingate 2940
 */
public class WavFilter extends javax.swing.filechooser.FileFilter {

    /** <p>Accepted files.<br />Fichier accepté.</p> */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (f.getName().endsWith(".wav")) {
            return true;
        }
        return false;
    }

    /** <p>Text for the combobox.<br />Le texte pour le combobox.</p> */
    @Override
    public String getDescription() {
        return "Wav file (*.wav)";
    }
}
