package metier.modele;

import java.io.File;
import java.io.FileFilter;

public class SVGFileFilter implements FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.getName().endsWith(".svg")) return true;
        return false;
    }
}
