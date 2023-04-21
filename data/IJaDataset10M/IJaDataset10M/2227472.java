package eric;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class JFileFilter extends FileFilter {

    public String description;

    public String extension;

    public JFileFilter(final String description, final String extension) {
        if (description == null || extension == null) {
            throw new NullPointerException("La description (ou extension) ne peut être null.");
        }
        this.description = description;
        this.extension = extension;
    }

    @Override
    public boolean accept(final File file) {
        if (file.isDirectory()) {
            return true;
        }
        final String nomFichier = file.getName().toLowerCase();
        return nomFichier.endsWith(extension);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
