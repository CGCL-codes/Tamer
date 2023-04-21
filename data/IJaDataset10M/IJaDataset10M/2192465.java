package it.yahoo.carlo.politi.finder.varie;

import java.io.File;
import javax.swing.filechooser.*;

public class FiltroGenericoSwing extends FileFilter {

    private String extension;

    private String description;

    public FiltroGenericoSwing(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(extension);
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }
}
