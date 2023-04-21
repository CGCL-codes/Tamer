package src.eleconics.filechoice;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * Class 'GifFilter is an Image Filtering class for a JFileChooser
 * that will isolate gif files.
 * @author Forrest Dillaway
 */
public class GifFilter extends FileFilter {

    /**
     * Accept all directories and all gif files.
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = ImageFileChoiceUtils.getExtension(f);
        return (extension != null) && (extension.equals(ImageFileChoiceUtils.gif));
    }

    /**
     * Return a description of this filter to be shown in the
     * chooser drop-down.
     * @return The description of this image filter.
     */
    public String getDescription() {
        return "Gif Files(*.gif)";
    }
}
