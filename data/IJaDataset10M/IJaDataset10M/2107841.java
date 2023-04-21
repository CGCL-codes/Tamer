package assfxmaker.lib.filter;

import java.io.File;

/**
 * <p>This filter accept only ADF files.<br />
 * Ce filtre accepte seulement les fichiers ADF.</p>
 * @author The Wingate 2940
 */
public class DrawingFilter extends javax.swing.filechooser.FileFilter {

    /** <p>Accepted files.<br />Fichier accepté.</p> */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (f.getName().endsWith(".adf")) {
            return true;
        }
        return false;
    }

    /** <p>Text for the combobox.<br />Le texte pour le combobox.</p> */
    @Override
    public String getDescription() {
        return "Ass Drawing File (*.adf)";
    }
}
