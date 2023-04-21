package projectviewer;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A file filter that filters for files that are not in the give 
 * {@link FileView}.
 */
public class NonViewFileFilter extends FileFilter {

    private FileView view;

    /**
    * Create a new <code>NonViewFileFilter</code>.
    */
    public NonViewFileFilter(FileView aView) {
        view = aView;
    }

    /**
    * Accept files that are not in the given view.
    */
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        return !view.isFileInView(f);
    }

    /**
    * Returns a description of this filter.
    */
    public String getDescription() {
        return "Unadded Files";
    }
}
