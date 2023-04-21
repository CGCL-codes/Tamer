package util;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import model.Settings;

public class StudyCardFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        String extension = Utilities.getExtension(file);
        if (extension == null) return true;
        return extension.equals(Settings.FILE_EXTENSION);
    }

    @Override
    public String getDescription() {
        return "StudyCards Desktop files (." + Settings.FILE_EXTENSION + ")";
    }
}
