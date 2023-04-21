package motiontrackerdesktopv20;

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;

public class ImagesFileFilterView extends FileView {

    @Override
    public String getName(File f) {
        return null;
    }

    @Override
    public String getDescription(File f) {
        return null;
    }

    @Override
    public Boolean isTraversable(File f) {
        return null;
    }

    @Override
    public String getTypeDescription(File f) {
        String extension = FileUtilities.getExtension(f);
        String type = null;
        if (extension != null) {
            if (extension.equals(FileUtilities.jpeg) || extension.equals(FileUtilities.jpg)) {
                type = "JPEG Image";
            } else if (extension.equals(FileUtilities.gif)) {
                type = "GIF Image";
            } else if (extension.equals(FileUtilities.tiff) || extension.equals(FileUtilities.tif)) {
                type = "TIFF Image";
            } else if (extension.equals(FileUtilities.png)) {
                type = "PNG Image";
            }
        }
        return type;
    }

    @Override
    public Icon getIcon(File f) {
        return null;
    }
}
