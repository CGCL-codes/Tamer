package dsp.auraw;

import javax.swing.filechooser.FileFilter;
import java.io.*;

public class FiltruFisierAu extends FileFilter {

    public FiltruFisierAu() {
    }

    public boolean accept(File pathname) {
        return pathname.getAbsolutePath().endsWith(".au");
    }

    public String getDescription() {
        return "Fisiere au (*.au)";
    }
}
