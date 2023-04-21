package es.eucm.eadventure.editor.auxiliar.categoryfilters;

import java.io.File;
import es.eucm.eadventure.common.auxiliar.FileFilter;

public class VideoFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        String filename = file.toString().toLowerCase();
        return filename.endsWith(".mov") || filename.endsWith(".mpg") || filename.endsWith(".avi") || file.isDirectory();
    }

    @Override
    public String getDescription() {
        return "Video files (*.mov;*.mpg;*.avi)";
    }
}
