package org.roussev.hiena.util;

import java.io.File;
import javax.swing.filechooser.FileView;

public final class MediaFileView extends FileView {

    public String getName(File f) {
        return null;
    }

    public String getDescription(File f) {
        return null;
    }

    public Boolean isTraversable(File f) {
        return null;
    }

    public String getTypeDescription(File f) {
        final String extension = Utils.getExtension(f);
        String type = null;
        if (extension != null) {
            if (extension.equals("mp3")) {
                type = "MP3 Media File";
            }
        }
        return type;
    }
}
