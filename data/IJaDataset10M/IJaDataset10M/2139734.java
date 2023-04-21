package magictool.filefilters;

import java.io.File;

/**
 * GprjFilter is a file filter used to select project files in a file chooser
 */
public class GprjFilter extends javax.swing.filechooser.FileFilter {

    /**Constucts the project file filter*/
    public GprjFilter() {
    }

    /**
   * returns whether or not file contains the project file extension (.gprj)
   * @param fileobj file to test
   * @return whether or not file contains the project file extension (.gprj)
   */
    public boolean accept(File fileobj) {
        String extension = "";
        if (fileobj.getPath().lastIndexOf('.') > 0) extension = fileobj.getPath().substring(fileobj.getPath().lastIndexOf('.') + 1).toLowerCase();
        if (extension != "") return extension.equals("gprj"); else return fileobj.isDirectory();
    }

    /**
   * returns string description of file format
   * @return string description of file format
   */
    public String getDescription() {
        return "Project Files (*.gprj)";
    }
}
