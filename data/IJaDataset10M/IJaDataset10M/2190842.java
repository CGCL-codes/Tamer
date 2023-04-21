package xtrememp.util;

import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Besmir Beqiri
 */
public class AudioFileFilter extends FileFilter implements FilenameFilter {

    public static final String[] audioFormatExt = { ".spx", ".snd", ".aifc", ".aif", ".wav", ".au", ".flac", ".mp1", ".mp2", ".mp3", ".ogg" };

    @Override
    public boolean accept(File dir, String name) {
        File file = new File(dir, name);
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        String s = name.toLowerCase();
        for (String ext : audioFormatExt) {
            if (s.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String s = file.getName().toLowerCase();
        for (String ext : audioFormatExt) {
            if (s.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        String description = "Supported Audio Files";
        if (audioFormatExt.length > 0) {
            description += " (";
            for (String ext : audioFormatExt) {
                description += " *" + ext;
            }
            description += " )";
        }
        return description;
    }
}
