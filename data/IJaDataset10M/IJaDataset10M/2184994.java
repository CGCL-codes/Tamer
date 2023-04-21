package DEREditorlib.visual;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FiltroArchivo extends FileFilter {

    String ext, desc;

    public FiltroArchivo(String ext, String desc) {
        this.ext = ext;
        this.desc = desc;
    }

    ;

    private String getExtension(File f) {
        String nombre = f.getName();
        if (nombre.lastIndexOf(".") != -1) return nombre.substring(nombre.lastIndexOf(".")).toLowerCase(); else return ".";
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (getExtension(f).equals(ext)) return true; else return false;
    }

    public String getDescription() {
        return desc;
    }
}
