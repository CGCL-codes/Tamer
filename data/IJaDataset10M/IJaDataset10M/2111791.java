package org.ces.cagt.commun;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author root
 */
public class filtreFichiers extends FileFilter {

    private String description;

    private String extension;

    public filtreFichiers(String description, String extension) {
        if (description == null || extension == null) {
            throw new NullPointerException("La description (ou extension) ne peut être null.");
        }
        this.description = description;
        this.extension = extension;
    }

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String nomFichier = file.getName().toLowerCase();
        return nomFichier.endsWith(extension);
    }

    public String getDescription() {
        return description;
    }
}
