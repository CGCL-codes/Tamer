package magictool.filefilters;

import java.io.File;

/**
 * TxtFilter is a file filter used to select text files in a file chooser
 */
public class TxtFilter extends javax.swing.filechooser.FileFilter {

    /**Constucts the text file filter*/
    public TxtFilter() {
    }

    /**
   * returns whether or not file contains the txt file extension (.txt)
   * @param file file to test
   * @return whether or not file contains the txt file extension (.txt)
   */
    public boolean accept(File file) {
        String extension = "";
        if (file.getPath().lastIndexOf('.') > 0) extension = file.getPath().substring(file.getPath().lastIndexOf('.') + 1).toLowerCase();
        if (extension != "") return extension.equals("txt"); else return file.isDirectory();
    }

    /**
       * returns string description of file format
       * @return string description of file format
       */
    public String getDescription() {
        return "Text Files (*.txt)";
    }
}
