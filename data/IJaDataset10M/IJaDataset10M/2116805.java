package jemu.ui.paint;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ImageFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) || extension.equals(Utils.tif) || extension.equals(Utils.gif) || extension.equals(Utils.jpeg) || extension.equals(Utils.jpg) || extension.equals(Utils.scr) || extension.equals(Utils.bmp) || extension.equals(Utils.tga) || extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String getDescription() {
        return "Just Images";
    }
}
